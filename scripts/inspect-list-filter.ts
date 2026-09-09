import { chromium } from '@playwright/test';
import 'dotenv/config';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { StoryCreationPage } from '../pages/cms/story.page';

/** Diagnose the Stories list "Publish Date" filter datepicker (the step where
 *  Selenium's calender()/isBefore() throws in TB today). Not a test. */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const login = new LoginPage(page);
  const home = new HomePage(page);
  const story = new StoryCreationPage(page);

  await login.ensureLoggedIn();
  console.log('[diag] logged in:', page.url());
  await home.navigate('Stories', 'Content');
  await page.waitForTimeout(3000);
  await story.selectChannel().catch((e) => console.log('[diag] channel select:', e.message));

  // Report the filter controls present.
  const dump = async (label: string, sel: string) => {
    const loc = page.locator(sel);
    const n = await loc.count();
    const info: Array<Record<string, unknown>> = [];
    for (let i = 0; i < Math.min(n, 6); i++) {
      const el = loc.nth(i);
      info.push({
        i,
        visible: await el.isVisible().catch(() => false),
        text: (await el.innerText().catch(() => '')).slice(0, 40),
        value: await el.inputValue().catch(() => undefined),
      });
    }
    console.log(`[diag] ${label} (${sel}) count=${n}`, JSON.stringify(info));
  };

  await dump('PublishDate toggle', "//a[text()='Publish Date']");
  await dump('filter calendar input', "//input[contains(@class,'libFilterInput') and contains(@class,'libFilterDate')]");
  await dump('PublishDateRange select', "//select[@name='PublishDateRange']");

  // Try to open the datepicker the Selenium way and read the title.
  const toggle = await story.firstVisible(page.locator("//a[text()='Publish Date']"));
  if (toggle) {
    await toggle.click().catch((e) => console.log('[diag] toggle click:', e.message));
    await page.waitForTimeout(1500);
  }
  const cal = await story.firstVisible(
    page.locator("//input[contains(@class,'libFilterInput') and contains(@class,'libFilterDate')]"),
  );
  if (cal) {
    await cal.click().catch((e) => console.log('[diag] cal click:', e.message));
    await page.waitForTimeout(1500);
  }
  const title = page.locator("//div[@class='ui-datepicker-title']");
  console.log('[diag] datepicker title count=', await title.count(),
    'visible=', await title.first().isVisible().catch(() => false),
    'text=', JSON.stringify(await title.first().innerText().catch(() => '<none>')));
  const dp = page.locator('#ui-datepicker-div, .ui-datepicker');
  console.log('[diag] datepicker container count=', await dp.count(),
    'visible=', await dp.first().isVisible().catch(() => false));

  await page.screenshot({ path: 'test-results/inspect-list-filter.png', fullPage: true }).catch(() => {});
  console.log('[diag] screenshot saved');
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
