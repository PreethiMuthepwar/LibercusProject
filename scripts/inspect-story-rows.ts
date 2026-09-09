import { chromium } from '@playwright/test';
import 'dotenv/config';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { StoryCreationPage } from '../pages/cms/story.page';
import { getDates } from '../utils/data.utils';

/** Find WHERE the story list rows live: main frame, a nested iframe, or rendered late. */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const login = new LoginPage(page);
  const home = new HomePage(page);
  const story = new StoryCreationPage(page);

  await login.ensureLoggedIn();
  await home.navigate('Stories', 'Content');
  await story.selectChannel().catch((e) => console.log('[rows] channel:', e.message));
  await story.filterByDate(String(getDates().dateForStories)).catch((e) => console.log('[rows] filter:', e.message));
  await story.update().catch((e) => console.log('[rows] update:', e.message));

  // Poll the main page for rows over 15s to detect late rendering.
  for (let t = 0; t <= 15; t += 3) {
    const exact = await page.locator("//*[@class='libListContentRow libLine']").count();
    const loose = await page.locator("[class*='libListContentRow']").count();
    const cbs = await page.locator('input[type=checkbox]').count();
    const navbar = (await page.locator("//div[@id='storyPanel']//div[@class='libNavBarTextArea']").innerText().catch(() => '')).trim();
    console.log(`[rows] t=${t}s main: exact=${exact} loose=${loose} checkboxes=${cbs} navbar="${navbar}"`);
    if (loose > 0) break;
    await page.waitForTimeout(3000);
  }

  // Enumerate ALL frames and where the rows / checkboxes live.
  console.log('[rows] frames:', page.frames().length);
  for (const f of page.frames()) {
    const loose = await f.locator("[class*='libListContentRow']").count().catch(() => -1);
    const cbs = await f.locator('input[type=checkbox]').count().catch(() => -1);
    if (loose > 0 || cbs > 0) {
      console.log(`[rows]   frame url=${f.url().slice(0, 80)} libListContentRow=${loose} checkboxes=${cbs}`);
    }
  }

  // Dump a slice of the list container HTML to read the real classes.
  const containerHtml = await page
    .locator("//div[@id='storyPanel']")
    .evaluate((el) => (el as HTMLElement).innerHTML.slice(0, 1800))
    .catch(() => '<no storyPanel>');
  console.log('[rows] #storyPanel innerHTML (1.8k):\n', containerHtml);

  await page.screenshot({ path: 'test-results/inspect-story-rows.png', fullPage: true }).catch(() => {});
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
