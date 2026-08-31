import { defineConfig, devices } from '@playwright/test';
import 'dotenv/config';

/**
 * Libercus Playwright config.
 *
 * Selenium parity notes:
 *  - The app (Angular/jQuery CMS) is slow: Selenium used a 60s implicit wait,
 *    a 90s default explicit wait, and a 240s login-ready wait. Timeouts here are
 *    set generously to match.
 *  - Two surfaces => two projects: `cms` and `pf3`. Each reuses storageState from
 *    global-setup. PF3 reader pages are mostly public/edition views.
 *  - Customer (PG/TB) is selected via PROJECT_ENV and resolved in utils/env.ts.
 */

const HEADLESS = process.env.HEADLESS !== 'false';

export default defineConfig({
  testDir: './tests',
  outputDir: './test-results',
  fullyParallel: false, // CMS flows mutate shared content; default to serial-safe.
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 3 : 1,
  // Single worker: the CMS/regression suites mutate shared lists and assert ABSOLUTE
  // count deltas (before±1). Running multiple files concurrently (workers>1, or sharding
  // copy+delete onto different agents) lets a second mutation land between the before-read
  // and the delta-poll → false failures. Selenium ran these serially; we match that.
  // The read-only PF3 project is safe to parallelize/shard separately if speed is needed.
  workers: 1,
  reporter: [
    ['list'],
    ['html', { open: 'never', outputFolder: 'playwright-report' }],
    ['json', { outputFile: 'test-results/results.json' }],
  ],

  // Selenium default explicit wait was 90s; login-ready was 240s. Per-test 5 min.
  timeout: 5 * 60 * 1000,
  expect: { timeout: 90 * 1000 },

  globalSetup: './global-setup.ts',

  use: {
    headless: HEADLESS,
    viewport: { width: 1920, height: 1080 },
    actionTimeout: 90 * 1000,
    navigationTimeout: 120 * 1000,
    ignoreHTTPSErrors: true,
    acceptDownloads: true,
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },

  projects: [
    {
      name: 'cms',
      testDir: './tests/cms',
      use: {
        ...devices['Desktop Chrome'],
        headless: HEADLESS,
        storageState: 'auth/cms.json',
      },
    },
    {
      name: 'pf3',
      testDir: './tests/pf3',
      use: {
        ...devices['Desktop Chrome'],
        headless: HEADLESS,
        // PF3 edition/reader is a PUBLIC Angular app — no CMS auth needed.
        storageState: { cookies: [], origins: [] },
      },
    },
    {
      name: 'regression',
      testDir: './tests/regression',
      use: {
        ...devices['Desktop Chrome'],
        headless: HEADLESS,
        storageState: 'auth/cms.json',
      },
    },
  ],
});
