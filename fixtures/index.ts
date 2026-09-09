import { test as base, type Page, expect } from '@playwright/test';
import { LoginPage } from '../pages/cms/login.page';
import { HomePage } from '../pages/cms/home.page';
import { StoryCreationPage } from '../pages/cms/story.page';
import { InteractiveAdsPage } from '../pages/cms/interactive-ads.page';
import { RichMediaAdsPage } from '../pages/cms/rich-media-ads.page';
import { PrintAdsPage } from '../pages/cms/print-ads.page';
import { MediaFilesPage } from '../pages/cms/media-files.page';
import { PrintPagesPage } from '../pages/cms/print-pages.page';
import { PrintAdminPage } from '../pages/cms/print-admin.page';
import { ProductAdminPage } from '../pages/cms/product-admin.page';
import { CongeroPage } from '../pages/cms/congero.page';
import { WireFeedsPage } from '../pages/cms/wire-feeds.page';
import { ProductionViewPage } from '../pages/cms/production-view.page';
import { Pf3HomePage } from '../pages/pf3/pf3-home.page';

/**
 * Single fixtures file (per playbook §10). Injects page objects bound to the
 * active (authenticated, via project storageState) page, plus a `ctx` bag for
 * sharing ids across serial steps.
 */
type Fixtures = {
  loginPage: LoginPage;
  homePage: HomePage;
  storyPage: StoryCreationPage;
  interactiveAdsPage: InteractiveAdsPage;
  richMediaAdsPage: RichMediaAdsPage;
  printAdsPage: PrintAdsPage;
  mediaFilesPage: MediaFilesPage;
  printPagesPage: PrintPagesPage;
  printAdminPage: PrintAdminPage;
  productAdminPage: ProductAdminPage;
  congeroPage: CongeroPage;
  wireFeedsPage: WireFeedsPage;
  productionViewPage: ProductionViewPage;
  pf3Home: Pf3HomePage;
  ctx: { storyTitle?: string; appId?: string; [k: string]: unknown };
};

export const test = base.extend<Fixtures>({
  loginPage: async ({ page }: { page: Page }, use) => use(new LoginPage(page)),
  homePage: async ({ page }: { page: Page }, use) => use(new HomePage(page)),
  storyPage: async ({ page }: { page: Page }, use) => use(new StoryCreationPage(page)),
  interactiveAdsPage: async ({ page }: { page: Page }, use) => use(new InteractiveAdsPage(page)),
  richMediaAdsPage: async ({ page }: { page: Page }, use) => use(new RichMediaAdsPage(page)),
  printAdsPage: async ({ page }: { page: Page }, use) => use(new PrintAdsPage(page)),
  mediaFilesPage: async ({ page }: { page: Page }, use) => use(new MediaFilesPage(page)),
  printPagesPage: async ({ page }: { page: Page }, use) => use(new PrintPagesPage(page)),
  printAdminPage: async ({ page }: { page: Page }, use) => use(new PrintAdminPage(page)),
  productAdminPage: async ({ page }: { page: Page }, use) => use(new ProductAdminPage(page)),
  congeroPage: async ({ page }: { page: Page }, use) => use(new CongeroPage(page)),
  wireFeedsPage: async ({ page }: { page: Page }, use) => use(new WireFeedsPage(page)),
  productionViewPage: async ({ page }: { page: Page }, use) => use(new ProductionViewPage(page)),
  pf3Home: async ({ page }: { page: Page }, use) => use(new Pf3HomePage(page)),
  ctx: async ({}, use) => use({}),
});

export { expect };
