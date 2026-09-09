import { chromium } from '@playwright/test';
import 'dotenv/config';
import { Pf3HomePage } from '../pages/pf3/pf3-home.page';

/** Inspect the PF3 menu items + the TOC/Search views to fix the 3 failing scenarios. */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const pf3 = new Pf3HomePage(page);
  await pf3.goto();
  await pf3.openMenu();
  await page.waitForTimeout(1500);

  // Dump visible menu item texts (spans/buttons/anchors in the open drawer).
  const menuTexts = await page
    .locator('mat-sidenav a, mat-sidenav button, mat-sidenav span, .mat-drawer a, .mat-drawer button, [class*="menu"] span')
    .evaluateAll((els) =>
      Array.from(new Set(els.map((e) => (e.textContent || '').trim()).filter((t) => t && t.length < 30))).slice(0, 40),
    )
    .catch(() => []);
  console.log('[pf3] menu item texts:', JSON.stringify(menuTexts));

  // Click TOC and dump headers.
  const tocItem = page.locator("//span[contains(text(),'TOC')]").first();
  console.log('[pf3] TOC item count =', await page.locator("//span[contains(text(),'TOC')]").count());
  await pf3.jsClick(tocItem).catch((e) => console.log('[pf3] TOC click:', e.message));
  await page.waitForTimeout(2500);
  const headers = await page
    .locator('h1, h2, h3, mat-toolbar, [class*="header"], [class*="Header"], [class*="title"]')
    .evaluateAll((els) => Array.from(new Set(els.map((e) => (e.textContent || '').trim()).filter((t) => t && t.length < 50))).slice(0, 25))
    .catch(() => []);
  console.log('[pf3] after TOC click — headings/titles:', JSON.stringify(headers));
  console.log('[pf3] "Table of Contents" any =', await page.getByText('Table of Contents', { exact: false }).count());

  await page.screenshot({ path: 'test-results/inspect-pf3-toc.png', fullPage: true }).catch(() => {});

  // Re-open menu, inspect Search entry.
  await page.keyboard.press('Escape').catch(() => {});
  await pf3.goto();
  await pf3.openMenu();
  await page.waitForTimeout(1000);
  console.log('[pf3] menu Search span count =', await page.locator("//span[text()='Search']").count(),
    '| toolbar Search btn =', await page.locator("//button[@mattooltip='Search']").count());
  const searchItem = (await page.locator("//span[text()='Search']").count())
    ? page.locator("//span[text()='Search']").first()
    : page.locator("//button[@mattooltip='Search']").first();
  await pf3.jsClick(searchItem).catch((e) => console.log('[pf3] Search click:', e.message));
  await page.waitForTimeout(2000);
  console.log('[pf3] after Search — mat-input count =', await page.locator("//input[contains(@id,'mat-input-')]").count(),
    '| any input =', await page.locator('input').count(),
    '| radio today =', await page.locator("//*[@value='today']").count());

  await page.screenshot({ path: 'test-results/inspect-pf3-search.png', fullPage: true }).catch(() => {});
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
