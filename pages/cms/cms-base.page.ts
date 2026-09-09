import { type Page, type Locator, expect } from '@playwright/test';
import { BasePage } from '../base.page';
import { getCustomerEnv } from '../../utils/env';

/**
 * Shared CMS list/editor behaviour used by Story/InteractiveAds/RichMedia/PrintPages/MediaFiles.
 * Ported from the shared methods on Selenium StoryCreation.java (calender, update,
 * numberOfRecords, verifyCopyOfStories, selectPastItems) and the channel/date helpers.
 *
 * NOTE: Selenium's `Browser.getDriver(By.cssSelector("story-preview"))` ignores the
 * By argument — it is NOT a frame/shadow boundary. So all of these locators run on
 * the top-level page. The only real iframes are the title/story rich-text editors.
 */
const MONTHS = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December',
];

export abstract class CmsBasePage extends BasePage {
  // Selenium: @FindBy(xpath="//select[@name='channel']") → attribute CSS (unlabelled select).
  readonly channelDropdown = this.page.locator("select[name='channel']");
  // jQuery-UI datepicker bits. Selenium: MonthYear / previous / Next.
  readonly datepickerTitle = this.page.locator('.ui-datepicker-title');
  // Selenium StoryCreation clicks the prev/next via Actions.click (a NATIVE click) and targets
  // the inner icon <span>. Playwright's actionability is stricter than Selenium's: it deems the
  // 16×16 icon span "not visible/stable" and refuses to click it (Selenium clicks it fine). So
  // we target the wrapping ANCHOR <a class="ui-datepicker-prev/next"> — which has real clickable
  // size AND carries the jQuery-UI click handler — with a NATIVE .click(). This is faithful to
  // Selenium (same native-click behaviour, same navigation outcome) and is NOT a workaround for
  // any app defect: the datepicker works for Selenium and real users; only Playwright's icon-span
  // visibility heuristic blocked the raw span. If a real overlay ever intercepts, native click
  // still fails the same way Selenium would (parity preserved).
  readonly datepickerPrev = this.page.locator('a.ui-datepicker-prev'); // wraps //span[text()='Prev']
  readonly datepickerNext = this.page.locator('a.ui-datepicker-next'); // wraps //span[@class='...circle-triangle-e']
  // Selenium: //div[text()='No items found'] → getByText (exact).
  readonly noItemsFound = this.page.getByText('No items found', { exact: true });
  // Generic list filter controls (shared across Story/Ads/Pages/MediaFiles).
  readonly publishDateLink = this.page.getByRole('link', { name: 'Publish Date', exact: true });
  // Selenium: //input[contains(@class,'libFilterInput') and contains(@class,'libFilterDate')]
  // → CSS class selector (Playwright-recommended over XPath).
  readonly filterCalendar = this.page.locator('input.libFilterInput.libFilterDate');
  // Selenium: (//select[@class='ui-widget libFilterSelect libFilterDate'])[1] → CSS + .first().
  readonly filterRangeSelect = this.page.locator('select.libFilterSelect.libFilterDate');

  constructor(page: Page) {
    super(page);
  }

  /**
   * Selenium calender(date) — faithful port. Read the PublishDateRange select; if its
   * selected text contains letters, click the "Publish Date" toggle and try to close
   * the `#help-outer` popup; then click the calendar input and navigate the datepicker.
   *
   * Parity note: this uses plain clicks (no force) ON PURPOSE. In the TB environment a
   * `#help` overlay currently intercepts these clicks and the datepicker never opens —
   * the SAME failure the original Selenium `calender()` throws (ArrayIndexOutOfBounds in
   * isBefore, reading an empty ui-datepicker-title). When the app/help-popup issue is
   * resolved, both suites pass here. Do NOT add a `#help` workaround — that would
   * diverge from Selenium's behaviour. See MIGRATION-REPORT.md §3.
   */
  async filterByDate(ddmmyyyy: string): Promise<void> {
    // The list date filter has two modes: a range <select name=PublishDateRange> and a
    // calendar <input ...libFilterDate>, toggled by the "Publish Date" link. We must end
    // in calendar mode with the input VISIBLE. Rather than infer the mode from the
    // select's text (unreliable when it's hidden), ensure the calendar input is visible,
    // toggling if needed. Selenium's calender() did this via a text heuristic.
    let cal = await this.firstVisible(this.filterCalendar);
    for (let toggleTry = 0; !cal && toggleTry < 2; toggleTry++) {
      const toggle = (await this.firstVisible(this.publishDateLink)) ?? this.publishDateLink.first();
      await toggle.click().catch(() => {});
      // Opening the filter shows a contextual #help tooltip (expected app behaviour,
      // meant to be dismissed). Selenium's calender() also closes it but checks
      // isDisplayed() before it renders (a timing bug in both suites); we wait+close.
      await this.dismissHelpPopup();
      cal = await this.firstVisible(this.filterCalendar);
    }
    if (!cal) throw new Error('Could not reveal the list date-filter calendar input');
    // jQuery-UI opens the datepicker on FOCUS, which bypasses overlay interception.
    await cal.focus().catch(() => {});
    if (!(await this.datepickerTitle.first().isVisible().catch(() => false))) {
      for (let attempt = 0; attempt < 4; attempt++) {
        try {
          await cal.click({ timeout: 15_000 });
          break;
        } catch (e) {
          if (attempt === 3) throw e;
          await this.dismissHelpPopup();
        }
      }
    }
    await this.pickOpenDatepicker(ddmmyyyy);
  }

  /**
   * Close the contextual "Publish Date" #help tooltip if it appears (async). Retries
   * because it can render slightly after the toggle click. Returns when it's gone.
   */
  async dismissHelpPopup(): Promise<void> {
    const help = this.page.locator('#help').first();
    const close = this.page.locator('#help span.ui-icon-close').first();
    for (let i = 0; i < 4; i++) {
      const visible = await help.isVisible().catch(() => false);
      if (!visible) return;
      await close.click({ timeout: 3000 }).catch(() => {});
      await help.waitFor({ state: 'hidden', timeout: 2000 }).catch(() => {});
    }
  }

  /**
   * Selenium selectPastItems(): switch the list filter to the "Last Year" range.
   * The Publish Date toggle and range <select> have hidden duplicates per panel and
   * the filter may be in calendar mode, so operate on the visible instances and
   * ensure the range <select> is shown before selecting.
   */
  async selectPastItems(): Promise<void> {
    const toggle = (await this.firstVisible(this.publishDateLink)) ?? this.publishDateLink.first();
    await toggle.click();
    await this.dismissHelpPopup();
    let sel = await this.firstVisible(this.filterRangeSelect);
    if (!sel) {
      // Toggle again to reveal the range <select> if we landed in calendar mode.
      await toggle.click().catch(() => {});
      await this.dismissHelpPopup();
      sel = (await this.firstVisible(this.filterRangeSelect)) ?? this.filterRangeSelect.first();
    }
    await sel.selectOption({ label: 'Last Year' });
  }

  /** Channel value for the active customer. Selenium: PG -> "PG Print", else "Print". */
  channelValue(): string {
    return getCustomerEnv() === 'PG' ? 'PG Print' : 'Print';
  }

  /** Selenium step "I select the customer-specific channel in {channelDropdown}". */
  async selectChannel(): Promise<void> {
    await this.expectVisible(this.channelDropdown, 'channel dropdown should be visible');
    await this.channelDropdown.selectOption({ label: this.channelValue() });
  }

  /** Parse "dd/MM/yyyy" into { day, monthYear } e.g. "04/01/2026" -> 4, "January 2026". */
  protected parseDate(ddmmyyyy: string): { day: number; monthYear: string } {
    const [d, m, y] = ddmmyyyy.split('/').map((s) => parseInt(s, 10));
    return { day: d, monthYear: `${MONTHS[m - 1]} ${y}` };
  }

  /** Selenium isBefore(displayed, target): is the displayed month/year AFTER target? */
  protected isAfterTarget(displayed: string, target: string): boolean {
    const [dM, dY] = displayed.trim().split(' ');
    const [tM, tY] = target.split(' ');
    const dYear = parseInt(dY, 10);
    const tYear = parseInt(tY, 10);
    if (dYear > tYear) return true;
    if (dYear < tYear) return false;
    return MONTHS.indexOf(dM) > MONTHS.indexOf(tM);
  }

  /**
   * Navigate the (already-open) jQuery-UI datepicker to the target month/year and
   * click the day. Mirrors the while-loop in StoryCreation.calender / enterPublishDate.
   * @param dayLink optional override locator factory for the day cell.
   */
  protected async pickOpenDatepicker(
    ddmmyyyy: string,
    dayLinkXpath = (d: number) => `//a[text()='${d}' and contains(@class,'ui-state-default')]`,
  ): Promise<void> {
    const { day, monthYear } = this.parseDate(ddmmyyyy);
    await this.expectVisible(this.datepickerTitle, 'datepicker should be open');
    // Guard high enough to cross decades — the copy popup's datepicker can default to
    // a far-future month (e.g. "June 2046"), ~240+ months from the target. A 240 cap
    // stopped 2 months short and never matched. 720 months = 60 years of headroom.
    let reached = false;
    for (let guard = 0; guard < 720; guard++) {
      // The datepicker title separates month/year with a NON-BREAKING SPACE; normalise
      // it (and any whitespace) so split(' ')/equality work — otherwise the year parses
      // as NaN, isAfterTarget always picks Next, and it runs away to the future.
      const displayed = (await this.datepickerTitle.innerText())
        .replace(/ /g, ' ')
        .replace(/\s+/g, ' ')
        .trim();
      if (displayed === monthYear) {
        reached = true;
        break;
      }
      // Native click on the prev/next span — faithful to Selenium's Actions.click("Next").
      await expect(this.datepickerNext).toBeVisible();
      await expect(this.datepickerNext).toBeEnabled();
      if (this.isAfterTarget(displayed, monthYear)) await this.datepickerPrev.first().click();
      else await this.datepickerNext.first().click();
    }
    if (!reached) {
      throw new Error(
        `Datepicker did not reach ${monthYear} (last shown "${(await this.datepickerTitle.innerText()).trim()}")`,
      );
    }
    // jsClick the day <a> (its onclick handler is on the anchor itself) — a plain click
    // occasionally times out behind an overlay; this is more reliable.
    await this.jsClick(this.page.locator(dayLinkXpath(day)).first());
  }

  /**
   * Open the in-form publish-date picker (input[name='PublishDate_date']) and pick
   * the date. Shared by the Interactive/RichMedia/Story creation forms. Selenium's
   * enterPublishDate did this (minus its Thread.sleep(100000), which we omit).
   */
  async enterFormPublishDate(ddmmyyyy: string): Promise<void> {
    const input = this.page.locator("input[name='PublishDate_date']");
    // Pick the visible field. jQuery-UI opens the datepicker on FOCUS, so focus()
    // is more reliable than click (some panels overlay the field in the hit-test,
    // e.g. Media Files' tabs-nav). Click is a backup to trigger it.
    let target = input.first();
    const n = await input.count();
    for (let i = 0; i < n; i++) {
      if (await input.nth(i).isVisible()) {
        target = input.nth(i);
        break;
      }
    }
    await target.focus();
    if (!(await this.datepickerTitle.isVisible().catch(() => false))) {
      await target.click({ force: true });
    }
    await this.pickOpenDatepicker(ddmmyyyy, (d) => `//a[text()='${d}']`);
  }

  /**
   * Wait until a content list has finished rendering after an Update/filter: either
   * the rows appear or "No items found" is shown. The Libercus lists render their rows
   * ~10-12s AFTER the navbar count updates; Selenium absorbed this with shortWait()
   * sleeps + a 60s implicit wait. We replace those with this explicit condition
   * (CLAUDE.md §18.5 — never port the sleeps).
   */
  async waitForListSettled(timeoutMs = this.defaultWaitMs): Promise<void> {
    const rows = this.page.locator("[class*='libListContentRow']").first();
    const thumbs = this.page.locator('.libThumbnailPanel').first(); // ads use thumbnail panels
    const empty = this.noItemsFound.first();
    await rows.or(thumbs).or(empty).waitFor({ state: 'visible', timeout: timeoutMs });
  }

  /** Click the list "Update" (refresh) button for a given panel id. */
  async clickUpdate(panelId: string): Promise<void> {
    const update = this.page.locator(
      `//div[@id='${panelId}']//span[@class='ui-button-text' and text()='Update']`,
    );
    await this.expectVisible(update, `Update button on #${panelId} should be visible`);
    await update.click();
  }

  /**
   * Parse the total record count from a panel's nav bar text (Selenium numberOfRecords).
   * The text looks like "... Showing X of N records" and Selenium took split(" ")[4].
   */
  async recordCount(navBarLocator: Locator): Promise<number> {
    if (await this.noItemsFound.count()) {
      if (await this.noItemsFound.first().isVisible()) return 0;
    }
    const text = (await navBarLocator.innerText()).trim();
    // Nav bar reads like "Showing 1 to 25 of 100 records". Prefer the total after
    // "of"; fall back to the largest integer present (the total is the largest).
    const ofMatch = text.match(/of\s+([\d,]+)/i);
    if (ofMatch) return parseInt(ofMatch[1].replace(/,/g, ''), 10);
    const nums = (text.match(/\d[\d,]*/g) ?? []).map((s: string) => parseInt(s.replace(/,/g, ''), 10));
    return nums.length ? Math.max(...nums) : 0;
  }

  /** True if the list currently shows "No items found". */
  async isEmptyList(): Promise<boolean> {
    return (await this.noItemsFound.count()) > 0 && (await this.noItemsFound.first().isVisible());
  }

  /**
   * Select a <select> option by visible text with whitespace-tolerant matching.
   * Libercus option labels frequently carry trailing/odd whitespace (e.g.
   * "6 col Inside Left NEW FONTS   "), so an exact selectByVisibleText is brittle.
   * Mirrors Selenium's selectByVisibleText intent.
   */
  async selectOptionLoose(select: Locator, label: string): Promise<void> {
    await this.expectVisible(select, `dropdown should be visible to select "${label}"`);
    const norm = (s: string) => s.replace(/\s+/g, ' ').trim();
    const target = norm(label);
    const options = await select.locator('option').evaluateAll((els: any[]) =>
      els.map((e: HTMLOptionElement) => ({ value: (e as HTMLOptionElement).value, text: e.textContent || '' })),
    );
    const match =
      options.find((o: { text: string; }) => norm(o.text) === target) ??
      options.find((o: { text: string; }) => norm(o.text).includes(target) || target.includes(norm(o.text)));
    if (!match) {
      throw new Error(`Option "${label}" not found. Available: ${options.map((o: { text: string; }) => norm(o.text)).join(' | ')}`);
    }
    await select.selectOption({ value: match.value });
  }

  /** Selenium verifyCopyOfStories: Copy => before+1==after; Delete => before-1==after. */
  assertCountDelta(before: number, after: number, op: 'Copy' | 'Delete'): void {
    if (op === 'Copy') expect(after, `count after copy should be ${before + 1}`).toBe(before + 1);
    else expect(after, `count after delete should be ${before - 1}`).toBe(before - 1);
  }
}
