import { chromium } from '@playwright/test';
import 'dotenv/config';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { StoryCreationPage } from '../pages/cms/story.page';
import { getDates } from '../utils/data.utils';
import { randomIntBetween } from '../utils/random.utils';

/** Reproduce up to the Copy popup and dump its structure (datepicker, Libercus, slug, OK)
 *  to fix why the copy doesn't land on the target date. */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const story = new StoryCreationPage(page);
  await new LoginPage(page).ensureLoggedIn();
  await new HomePage(page).navigate('Stories', 'Content');
  await story.selectChannel();
  await story.filterByDate(String(getDates().dateForUpdateStories));
  await story.update();
  if (await story.isEmptyList()) {
    await story.selectPastItems();
    await story.update();
  }

  const n = await story.rowCheckboxes.count();
  console.log('[copy] row checkboxes=', n);
  const idx = n > 1 ? randomIntBetween(1, n - 1) : 0;
  await story.jsClick(story.rowCheckboxes.nth(idx));
  await page.waitForTimeout(1000);
  console.log('[copy] copy button enabled=', await story.copyButton.isEnabled().catch(() => false));
  await story.copyButton.click().catch((e) => console.log('[copy] copy click:', e.message));
  await page.waitForTimeout(2500);

  // What appeared? Datepicker, Libercus option, slug input, OK button, any dialog.
  const dpTitle = page.locator("//div[@class='ui-datepicker-title']");
  console.log('[copy] datepicker title count=', await dpTitle.count(), 'text=', JSON.stringify(await dpTitle.first().innerText().catch(() => '<none>')));
  console.log('[copy] #help visible=', await page.locator('#help').first().isVisible().catch(() => false));
  for (const [label, sel] of [
    ['Libercus option', "//span[text()='Libercus']"],
    ['slug input', "//input[@name='slug']"],
    ['OK span', "//span[@class='ui-button-text' and text()='OK']"],
    ['dialog', "//div[contains(@class,'ui-dialog') and not(contains(@style,'display: none'))]"],
    ['Channel select', "//select[@name='Channel']"],
  ] as const) {
    const loc = page.locator(sel);
    console.log(`[copy] ${label}: count=${await loc.count()} visible=${await loc.first().isVisible().catch(() => false)}`);
  }

  // Dump any visible ui-dialog title + button area to understand the popup.
  const dialogHtml = await page
    .locator("//div[contains(@class,'ui-dialog')]")
    .first()
    .evaluate((el) => (el as HTMLElement).outerHTML.slice(0, 2200))
    .catch((e) => `<err ${e.message}>`);
  console.log('[copy] first ui-dialog outerHTML:\n', dialogHtml);

  await page.screenshot({ path: 'test-results/inspect-copy-popup.png', fullPage: true }).catch(() => {});
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
