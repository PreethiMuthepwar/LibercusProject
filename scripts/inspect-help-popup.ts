import { chromium } from '@playwright/test';
import 'dotenv/config';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { StoryCreationPage } from '../pages/cms/story.page';

/** Inspect the #help overlay: is it dismissable (close control), and does dismissing
 *  it unblock the list date-filter? Determines app-bug vs stale-selector script-bug.
 *  The popup appears when the Publish Date filter is OPENED, so open it first. */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  await new LoginPage(page).ensureLoggedIn();
  const home = new HomePage(page);
  const story = new StoryCreationPage(page);
  await home.navigate('Stories', 'Content');
  await story.selectChannel().catch((e) => console.log('[help] channel:', e.message));
  await page.waitForTimeout(1500);
  // Open the Publish Date filter (this is what triggers the contextual #help popup).
  const toggle = await story.firstVisible(page.locator("//a[text()='Publish Date']"));
  if (toggle) {
    console.log('[help] clicking Publish Date toggle…');
    await toggle.click().catch((e) => console.log('[help] toggle click:', e.message));
  } else {
    console.log('[help] no visible Publish Date toggle');
  }
  await page.waitForTimeout(2500);

  const help = page.locator('#help');
  console.log('[help] #help count=', await help.count(), 'visible=', await help.first().isVisible().catch(() => false));

  // Dump the help popup HTML (first 2.5k) to find a close control.
  const html = await help.first().evaluate((el) => (el as HTMLElement).outerHTML.slice(0, 2500)).catch((e) => `<err ${e.message}>`);
  console.log('[help] #help outerHTML:\n', html);

  // Probe common close-control candidates inside / near the help popup.
  const candidates = [
    '#help .ui-icon-close',
    '#help .ui-icon-closethick',
    '#help [title="close" i]',
    '#help-title .ui-icon-close',
    '#help button',
    '#help a.close',
    '#help .libDialogClose',
    '.ui-dialog-titlebar-close',
    '#help .ui-icon[class*="close"]',
  ];
  for (const sel of candidates) {
    const loc = page.locator(sel);
    const n = await loc.count();
    if (n > 0) {
      console.log(`[help] candidate ${sel}: count=${n} visible=${await loc.first().isVisible().catch(() => false)}`);
    }
  }

  // Try the most likely close icon and re-check whether the filter calendar becomes usable.
  const closeIcon = page.locator('#help .ui-icon-close, #help .ui-icon-closethick, #help-title .ui-icon-close, .ui-dialog-titlebar-close').first();
  if (await closeIcon.count()) {
    console.log('[help] clicking close icon…');
    await closeIcon.click({ force: true }).catch((e) => console.log('[help] close click err:', e.message));
    await page.waitForTimeout(1500);
    console.log('[help] after close: #help visible=', await help.first().isVisible().catch(() => false));
  } else {
    console.log('[help] no obvious close control found via candidates');
  }

  await page.screenshot({ path: 'test-results/inspect-help-popup.png', fullPage: true }).catch(() => {});
  await browser.close();
}

main().catch((e) => { console.error(e); process.exit(1); });
