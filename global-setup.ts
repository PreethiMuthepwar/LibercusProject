import { chromium, type FullConfig } from '@playwright/test';
import { existsSync, mkdirSync } from 'node:fs';
import { LoginPage } from './pages/cms/login.page';

/**
 * Auth bootstrap. Logs into the CMS once (via the same LoginPage used by tests)
 * and saves storageState to auth/cms.json so tests skip the slow UI login.
 *
 * Ported from Selenium pages/Libercus/CMS/Login.java#validLogin(). Running this
 * is the first live validation of the auth flow (per the project's Playwright-first
 * verification policy in CLAUDE.md §0).
 */
async function globalSetup(_config: FullConfig): Promise<void> {
  if (!existsSync('auth')) mkdirSync('auth', { recursive: true });

  const browser = await chromium.launch();
  const context = await browser.newContext({ ignoreHTTPSErrors: true });
  const page = await context.newPage();

  try {
    const login = new LoginPage(page);
    console.log('[global-setup] CMS login…');
    await login.goto();
    await login.login();
    console.log('[global-setup] CMS ready. Saving storageState → auth/cms.json');
    await context.storageState({ path: 'auth/cms.json' });
  } finally {
    await context.close();
    await browser.close();
  }
}

export default globalSetup;
