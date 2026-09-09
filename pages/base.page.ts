import { type Page, type Locator, expect } from '@playwright/test';

/**
 * BasePage — common helpers ported from Selenium `framework/Actions.java` and
 * `framework/Page.java`.
 *
 * The Libercus CMS is an Angular/jQuery app whose primary view renders inside a
 * `<story-preview>` custom element. Playwright's auto-wait replaces Selenium's
 * isPageLoaded/isJQueryDone/isAngularDone polling (web-first assertions wait on the
 * actual condition), so those framework-readiness helpers are intentionally not ported.
 */
export abstract class BasePage {
  // Selenium Actions.waitSeconds = 90
  protected readonly defaultWaitMs = 90_000;

  constructor(protected readonly page: Page) {}

  /** JS-driven click — mirrors Selenium jsClick for elements under overlays. */
  async jsClick(locator: Locator): Promise<void> {
    await locator.evaluate((el) => (el as HTMLElement).click());
  }

  /**
   * Fill the first VISIBLE match of a locator. The CMS declares the same form field
   * in multiple hidden panel templates, so a bare `.fill()` would hit strict-mode;
   * this targets the one currently-visible editor field.
   */
  async fillVisible(locator: Locator, value: string, timeoutMs = this.defaultWaitMs): Promise<void> {
    let target: Locator | null = null;
    await expect(async () => {
      target = await this.firstVisible(locator);
      expect(target, 'a visible field to fill').not.toBeNull();
    }).toPass({ timeout: timeoutMs });
    await target!.fill(value);
  }

  /** Assert an element is visible with a clear message (no silent failures). */
  async expectVisible(locator: Locator, message: string, timeoutMs = this.defaultWaitMs): Promise<void> {
    await expect(locator, message).toBeVisible({ timeout: timeoutMs });
  }

  /**
   * Assert that at least one match of a locator becomes visible. The metadata-driven
   * CMS renders the same label/field in many hidden panel templates, so `.first()` is
   * often a hidden duplicate; this polls until a VISIBLE instance appears.
   */
  async expectAnyVisible(locator: Locator, message: string, timeoutMs = this.defaultWaitMs): Promise<void> {
    await expect(async () => {
      expect(await this.firstVisible(locator), message).not.toBeNull();
    }, message).toPass({ timeout: timeoutMs });
  }

  /**
   * Return the first currently-visible match of a locator, or null. Useful for the
   * metadata-driven CMS where the same control (Publish Date, calendar input, …)
   * is declared in many hidden panels and only one is active.
   */
  async firstVisible(locator: Locator): Promise<Locator | null> {
    const n = await locator.count();
    for (let i = 0; i < n; i++) {
      if (await locator.nth(i).isVisible()) return locator.nth(i);
    }
    return null;
  }
}
