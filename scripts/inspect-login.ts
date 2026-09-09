import { chromium } from '@playwright/test';
import 'dotenv/config';
import { CMS_URL, credentials } from '../utils/env';

/** Interactive diagnostic for the CMS login flow (not a test). */
async function main() {
  const browser = await chromium.launch();
  const ctx = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await ctx.newPage();
  const url = CMS_URL();
  console.log('URL:', url);
  await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 120_000 });
  await page.waitForTimeout(5000);

  console.log('--- after load ---');
  console.log('title:', await page.title());
  console.log('current url:', page.url());

  const inputs = await page.locator('input').evaluateAll((els) =>
    els.map((e) => ({
      name: (e as HTMLInputElement).name,
      type: (e as HTMLInputElement).type,
      id: e.id,
      placeholder: (e as HTMLInputElement).placeholder,
      visible: (e as HTMLElement).offsetParent !== null,
    })),
  );
  console.log('inputs:', JSON.stringify(inputs, null, 2));

  const buttons = await page
    .locator('button, input[type=submit], [name=login]')
    .evaluateAll((els) =>
      els.map((e) => ({ tag: e.tagName, name: (e as HTMLInputElement).name, id: e.id, text: (e.textContent || '').trim().slice(0, 30) })),
    );
  console.log('buttons:', JSON.stringify(buttons, null, 2));

  // Attempt login
  const c = credentials();
  const userField = page.locator('input[name="username"]');
  if (await userField.count()) {
    console.log('--- attempting login ---');
    await userField.fill(c.username);
    await page.locator('input[name="password"]').fill(c.password);
    await page.locator('[name="login"]').click();
    await page.waitForTimeout(20_000);
    console.log('post-login url:', page.url());
    console.log('post-login title:', await page.title());
    const bodyText = (await page.locator('body').innerText().catch(() => '')).slice(0, 1500);
    console.log('--- body text (first 1500) ---\n', bodyText);
    // search for the banner / loading indicators
    for (const phrase of ['Print shapes', 'loading complete', 'Home', 'Invalid', 'incorrect', 'password']) {
      const cnt = await page.getByText(phrase, { exact: false }).count();
      console.log(`text "${phrase}": ${cnt} match(es)`);
    }
    await page.screenshot({ path: 'test-results/inspect-postlogin.png', fullPage: true }).catch(() => {});
  } else {
    console.log('!! no input[name=username] found — login form differs');
    await page.screenshot({ path: 'test-results/inspect-loginpage.png', fullPage: true }).catch(() => {});
  }

  await browser.close();
}

main().catch((e) => {
  console.error(e);
  process.exit(1);
});
