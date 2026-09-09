import { chromium } from '@playwright/test';
import 'dotenv/config';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { PrintPagesPage } from '../pages/cms/print-pages.page';
import { getDates } from '../utils/data.utils';

/** Diagnose the Print Pages list/editor: row selectors, checkboxes, and the open-page
 *  editor structure (Section Letter input / Print Pages tab). */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const pp = new PrintPagesPage(page);
  await new LoginPage(page).ensureLoggedIn();
  await new HomePage(page).navigate('Print Pages', 'Content');
  await pp.selectChannel();
  await pp.filterByDate(String(getDates().fromDateForPages));
  await pp.clickUpdate();
  if (await pp.isEmptyList()) { await pp.selectPastItems(); await pp.clickUpdate(); }

  console.log('[pp] navbar count =', await pp.pageCount().catch(() => -1));
  console.log('[pp] listCheckboxes(name=marked) =', await page.locator("//input[@type='checkbox' and @name='marked']").count());
  console.log('[pp] panel checkboxes =', await page.locator("//div[@id='printpagesPanel']//input[@type='checkbox']").count());
  console.log('[pp] pageRows(current locator) =', await pp.pageRows.count().catch(() => -1));
  console.log('[pp] libListContentRow rows =', await page.locator("//div[@id='printpagesPanel']//div[contains(@class,'libListContentRow')]").count());

  // Open a page (try the row that the current openRandomPage would use) and inspect editor.
  const rows = page.locator("//div[@id='printpagesPanel']//div[contains(@class,'libListContentRow')]");
  const rc = await rows.count();
  if (rc > 0) {
    await pp.jsClick(rows.first());
    await page.waitForTimeout(4000);
    console.log('[pp] after open: SectionLetter input count =', await page.locator("//input[@name='SectionLetter']").count(),
      'visible =', await page.locator("//input[@name='SectionLetter']").first().isVisible().catch(() => false));
    console.log('[pp] Print Pages tab count =', await page.locator("//li[@class='ui-state-default ui-corner-top']/a[text()='Print Pages']").count());
    console.log('[pp] any tab anchors:', JSON.stringify(
      await page.locator('.ui-tabs-nav a').evaluateAll((els) => els.map((e) => (e.textContent || '').trim()).filter(Boolean).slice(0, 12)).catch(() => []),
    ));
  } else {
    console.log('[pp] no rows to open');
  }

  await page.screenshot({ path: 'test-results/inspect-printpages.png', fullPage: true }).catch(() => {});
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
