import { type Page, expect, type Locator } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { randomIntBetween } from '../../utils/random.utils';

/**
 * CMS Print Administration module: Shapes, Styles, Templates, Image Styles, Jump Styles.
 * Ported from Selenium pages/Libercus/CMS/{Shapes,Styles,Templates,ImageStyles,JumpStyles}.java
 * + RegressionSteps.java.
 *
 * These five "pages" are structurally identical jQuery-UI list+editor panels (New →
 * fill → Save & Close; search; copy/delete with a ±1 count assertion). They are
 * consolidated into one page object (CLAUDE.md §5.3 "consolidate where sensible"),
 * one method group per entity, each carrying its Selenium provenance.
 */
export class PrintAdminPage extends CmsBasePage {
  // Save & Close — Selenium targets the panel-specific button, but all panels share
  // the `lib-button-saveclose` class; iterate to the visible one (same approach as Story).
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');
  // The OK confirmation button inside the jQuery-UI "Libercus" dialog (copy/delete
  // confirmations: "You are about to … Continue?"). Selenium used //span[text()='OK'],
  // but that matches many hidden "OK" spans; target the dialog's actual OK button.
  readonly dialogOk = this.page.locator('.ui-dialog').getByRole('button', { name: 'OK', exact: true });

  constructor(page: Page) {
    super(page);
  }

  // ---- shared helpers -------------------------------------------------------

  /** Panel-scoped "New" button. Selenium: //div[@id='<panel>']//span[text()='New']. */
  private newButton(panelId: string): Locator {
    return this.page.locator(`#${panelId}`).getByRole('button', { name: 'New', exact: true });
  }

  /** Panel-scoped "Update" (list refresh) button. */
  private updateButton(mainId: string): Locator {
    return this.page.locator(`#${mainId}`).getByRole('button', { name: 'Update', exact: true });
  }

  /** Row "marked" checkboxes within a list panel. Selenium: //div[@id='<main>']//input[@name='marked']. */
  private rowCheckboxes(mainId: string): Locator {
    return this.page.locator(`#${mainId} input[name='marked']`);
  }

  /** Nav-bar record-count text for a list panel. */
  private navBar(mainId: string): Locator {
    return this.page.locator(`#${mainId} .libNavBarTextArea`);
  }

  /** Result rows in a list panel. Selenium: .libListContentRow.libLine. */
  private resultRows(mainId: string): Locator {
    return this.page.locator(`#${mainId} .libListContentRow.libLine`);
  }

  async clickNew(panelId: string): Promise<void> {
    const btn = this.newButton(panelId);
    await this.expectVisible(btn, `"New" button on #${panelId} should be visible`);
    await this.jsClick(btn);
  }

  /** Click the visible generic "New" (lib-button-new) — Selenium ImageStyles uses this. */
  async clickNewGeneric(): Promise<void> {
    const btn = this.page.locator('button.lib-button-new').getByText('New', { exact: true });
    const target = (await this.firstVisible(btn)) ?? btn.first();
    await this.expectVisible(target, '"New" button should be visible');
    await this.jsClick(target);
  }

  /**
   * Click OK on the jQuery-UI "Continue?" confirmation dialog(s) and wait until they
   * close. A bare jsClick on the OK <button> did NOT dismiss the dialog for large lists,
   * so use a real click (the default/[active] button), with an Enter-key fallback, and
   * confirm the "… Continue?" prompt disappears. Handles up to 2 stacked dialogs.
   */
  private async confirmDialogOk(): Promise<void> {
    const prompt = this.page.getByText(/You are about to .*Continue\?/i);
    for (let i = 0; i < 2; i++) {
      let ok: Locator | null = null;
      try {
        await expect(async () => {
          ok = await this.firstVisible(this.dialogOk);
          expect(ok, 'confirmation dialog OK button should be visible').not.toBeNull();
        }).toPass({ timeout: i === 0 ? this.defaultWaitMs : 5_000 });
      } catch {
        if (i === 0) throw new Error('Confirmation dialog OK never appeared');
        return; // no further dialog
      }
      // Real click (trusted) — the OK is the default/[active] button.
      await ok!.click({ timeout: 10_000 }).catch(async () => {
        await ok!.focus().catch(() => {});
        await this.page.keyboard.press('Enter').catch(() => {});
      });
      // Confirm this prompt closed before checking for another.
      await prompt.first().waitFor({ state: 'hidden', timeout: 30_000 }).catch(() => {});
    }
  }

  /**
   * Save & Close (Selenium iClickSaveAndCloseButtonOn): jsClick the visible save&close,
   * then if a "Save & Continue" prompt appears, click it and save&close again.
   */
  async saveAndClose(): Promise<void> {
    await expect(this.saveAndCloseButton.first(), 'Save & Close should be attached').toBeAttached({
      timeout: this.defaultWaitMs,
    });
    const clickVisibleOrFirst = async () => {
      const n = await this.saveAndCloseButton.count();
      for (let i = 0; i < n; i++) {
        if (await this.saveAndCloseButton.nth(i).isVisible()) {
          await this.jsClick(this.saveAndCloseButton.nth(i));
          return;
        }
      }
      await this.jsClick(this.saveAndCloseButton.first());
    };
    await clickVisibleOrFirst();
    const saveContinue = this.page.getByText('Save & Continue', { exact: true });
    if (await saveContinue.count()) {
      await this.jsClick(saveContinue.first());
      await clickVisibleOrFirst();
    }
  }

  /**
   * Refresh the page (Selenium refreshThePage): reload then wait for the print
   * shapes/styles caching to finish ("Print shapes and styles loading complete!").
   * Selenium waited up to 240s for that banner; we wait too but tolerate its
   * absence (it can flash by) so we don't hang if the reload is instant.
   */
  async refreshAndWaitForShapes(): Promise<void> {
    await this.page.reload({ waitUntil: 'domcontentloaded', timeout: 120_000 });
    const done = this.page.getByText('Print shapes and styles loading complete!', { exact: false });
    await done.first().waitFor({ state: 'visible', timeout: 240_000 }).catch(() => {});
  }

  /** Load a list panel (click Update, wait for rows/empty) before counting/selecting. */
  private async loadList(mainId: string): Promise<void> {
    await this.jsClick(this.updateButton(mainId));
    await this.waitForListSettled().catch(() => {});
    // Rows render ~10-12s after the navbar count updates; wait for a marked checkbox.
    await this.rowCheckboxes(mainId)
      .first()
      .waitFor({ state: 'attached', timeout: this.defaultWaitMs })
      .catch(() => {});
  }

  /**
   * Select a data row's "marked" checkbox so the Copy/Delete button enables. Selenium
   * jsClicked a random `input[name='marked']` from the panel, but that set includes the
   * header select-all box and hidden template rows; clicking those doesn't enable the
   * action. We pick a checkbox inside a VISIBLE data row, and fall back to clicking the
   * row container if the box click doesn't register, retrying until the action enables.
   */
  private async selectRowForAction(mainId: string, op: 'Copy' | 'Delete'): Promise<Locator> {
    const actionBtn = this.page.locator(`button.lib-button-${op.toLowerCase()}`);
    const rows = this.resultRows(mainId);
    const total = await rows.count();
    if (total < 1) throw new Error(`No selectable rows in #${mainId}`);
    // Visible data rows only.
    const visibleRows: number[] = [];
    for (let i = 0; i < total; i++) if (await rows.nth(i).isVisible()) visibleRows.push(i);
    if (!visibleRows.length) throw new Error(`No visible rows in #${mainId}`);
    const enabled = async () => {
      const b = (await this.firstVisible(actionBtn)) ?? actionBtn.first();
      return b.isEnabled().catch(() => false);
    };
    const order = [...visibleRows].sort(() => Math.random() - 0.5);
    for (const idx of order) {
      const row = rows.nth(idx);
      const box = row.locator("input[name='marked']").first();
      if (await box.count()) await this.jsClick(box).catch(() => {});
      if (await enabled()) return actionBtn;
      // Fallback: click the row's check column / the row itself.
      await this.jsClick(row).catch(() => {});
      if (await enabled()) return actionBtn;
    }
    // Last wait in case enabling is slightly delayed.
    const visible = (await this.firstVisible(actionBtn)) ?? actionBtn.first();
    await expect(visible, `${op} button should be enabled`).toBeEnabled({ timeout: 10_000 });
    return actionBtn;
  }

  /**
   * Copy/Delete a random row then assert the count changed by ±1.
   * Selenium select{Shape,Style}ToCopy → click Copy/Delete → okButton → verifyCount.
   */
  private async copyOrDeleteWithCount(
    mainId: string,
    op: 'Copy' | 'Delete',
  ): Promise<void> {
    await this.loadList(mainId);
    const before = await this.recordCount(this.navBar(mainId));
    const actionBtn = await this.selectRowForAction(mainId, op);
    await this.jsClick(await this.firstVisible(actionBtn) ?? actionBtn.first());
    await this.confirmDialogOk();
    // Recompute and assert ±1 (Selenium verifyCopyOfStories). The nav-bar total only
    // refreshes when the list re-queries, so re-click Update each poll iteration — for
    // large lists (Shapes ~1285 rows) the count lags well behind the copy/delete.
    const target = op === 'Copy' ? before + 1 : before - 1;
    await expect
      .poll(
        async () => {
          await this.jsClick(this.updateButton(mainId)).catch(() => {});
          await this.waitForListSettled().catch(() => {});
          return this.recordCount(this.navBar(mainId));
        },
        {
          timeout: 150_000,
          intervals: [2_000, 4_000, 6_000, 8_000],
          message: `record count should reach ${target}`,
        },
      )
      .toBe(target);
  }

  /** Search a list then assert every result row contains the term (Selenium verifySearchResults). */
  private async searchAndVerify(
    mainId: string,
    searchInput: Locator,
    term: string,
  ): Promise<void> {
    await searchInput.fill(term);
    await this.jsClick(this.updateButton(mainId));
    await this.waitForListSettled();
    // Selenium only asserts when results exist (empty list → skip), per-result contains.
    const rows = this.resultRows(mainId);
    const total = await rows.count();
    for (let i = 0; i < total; i++) {
      const txt = await rows.nth(i).innerText();
      expect(txt, `result row ${i + 1} should contain "${term}"`).toContain(term);
    }
  }

  // ---- Shapes ---------------------------------------------------------------
  static readonly SHAPE_NAME = 'testShapeCreation';
  readonly shapeEnabled = this.page.locator("input[name='Enabled']");
  readonly shapeEnabledLabel = this.page.getByText('Enabled', { exact: true });
  readonly shapeId = this.page.locator("input[name='ShapeID']");
  readonly shapeName = this.page.locator("input[name='ShapeName']");
  readonly shapeTemplateDropdown = this.page.locator("select[name='Canvas']");
  readonly shapeSearch = this.page.locator("input[name='Shape']");

  async shapesCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.shapeEnabledLabel, 'Shapes creation "Enabled" label should show');
  }

  async fillNewShape(templateText: string): Promise<void> {
    // await this.jsClick((await this.firstVisible(this.shapeEnabled)) ?? this.shapeEnabled.first());
    await this.fillVisible(this.shapeId, 'test' + randomIntBetween(0, 100));
    await this.fillVisible(this.shapeName, PrintAdminPage.SHAPE_NAME);
    await this.selectOptionLoose(
      (await this.firstVisible(this.shapeTemplateDropdown)) ?? this.shapeTemplateDropdown.first(),
      templateText,
    );
  }

  async searchShapes(): Promise<void> {
    await this.searchAndVerify('printshapes-main', this.shapeSearch, PrintAdminPage.SHAPE_NAME);
  }

  async copyShape(): Promise<void> {
    await this.copyOrDeleteWithCount('printshapes-main', 'Copy');
  }

  async deleteShape(): Promise<void> {
    await this.copyOrDeleteWithCount('printshapes-main', 'Delete');
  }

  // ---- Styles ---------------------------------------------------------------
  static readonly STYLE_NAME = 'styleNameText';
  readonly styleNotes = this.page.getByRole('link', { name: 'Notes', exact: true });
  readonly styleName = this.page.locator("input[name='StyleName']");
  readonly styleSearch = this.page.locator("input[name='Style']");

  async stylesCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.styleNotes, 'Styles creation "Notes" tab should show');
  }

  async fillNewStyle(): Promise<void> {
    await this.fillVisible(this.styleName, PrintAdminPage.STYLE_NAME);
  }

  async searchStyles(): Promise<void> {
    await this.searchAndVerify('printstyles-main', this.styleSearch, PrintAdminPage.STYLE_NAME);
  }

  async copyStyle(): Promise<void> {
    await this.copyOrDeleteWithCount('printstyles-main', 'Copy');
  }

  async deleteStyle(): Promise<void> {
    await this.copyOrDeleteWithCount('printstyles-main', 'Delete');
  }

  // ---- Templates ------------------------------------------------------------
  static readonly TEMPLATE_NAME = 'Created';
  readonly templateLayoutTab = this.page.getByRole('link', { name: 'Layout', exact: true });
  readonly templateName = this.page.locator("input[name='PageDefName']");
  readonly templateColumns = this.page.locator("input[name='PageDefColumns']");
  readonly templateSearch = this.page.locator("input[name='Template']");

  async templatesCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.templateLayoutTab, 'Templates creation "Layout" tab should show');
  }

  async fillNewTemplate(): Promise<void> {
    await this.fillVisible(this.templateName, PrintAdminPage.TEMPLATE_NAME);
    await this.fillVisible(this.templateColumns, '6');
  }

  async searchTemplates(): Promise<void> {
    await this.searchAndVerify('printpagetemplates-main', this.templateSearch, PrintAdminPage.TEMPLATE_NAME);
  }

  /** Templates Copy/Delete just click OK (Selenium has NO count assertion for templates). */
  async selectTemplateAndConfirm(op: 'Copy' | 'Delete'): Promise<void> {
    await this.loadList('printpagetemplates-main');
    const actionBtn = await this.selectRowForAction('printpagetemplates-main', op);
    await this.jsClick(await this.firstVisible(actionBtn) ?? actionBtn.first());
    await this.confirmDialogOk();
  }

  // ---- Image Styles ---------------------------------------------------------
  static readonly IMAGE_STYLE_NAME = 'Created through auto';
  readonly imageStyleNotes = this.page.getByRole('link', { name: 'Notes', exact: true });
  // ImageStyles/JumpStyles reuse input[name='StyleName'].
  readonly imageStyleName = this.page.locator("input[name='StyleName']");

  async imageStyleCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.imageStyleNotes, 'Image Style creation "Notes" tab should show');
  }

  async fillNewImageStyle(): Promise<void> {
    await this.fillVisible(this.imageStyleName, PrintAdminPage.IMAGE_STYLE_NAME);
  }

  /** Selenium verifyimagestyleIsCreated: first list-item text equals the name. */
  async verifyImageStyleCreated(): Promise<void> {
    const firstItem = this.page.locator('.libListContentRow.libLine .libListContentItem.libCol').first();
    await this.expectVisible(firstItem, 'a created image-style row should appear');
    const txt = (await firstItem.innerText()).trim();
    expect(txt.toLowerCase(), 'first image-style row should be the created one').toBe(
      PrintAdminPage.IMAGE_STYLE_NAME.toLowerCase(),
    );
  }

  // ---- Jump Styles ----------------------------------------------------------
  static readonly JUMP_STYLE_NAME = 'created through auto';
  readonly jumpStyleEnabledLabel = this.page.locator('#printjumpstylesEdit-main').getByText('Enabled', { exact: true });
  readonly jumpStyleName = this.page.locator("#printjumpstylesEdit-main input[name='StyleName']");

  async jumpStyleCreationDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.jumpStyleEnabledLabel, 'Jump Style creation "Enabled" label should show');
  }

  async fillNewJumpStyle(): Promise<void> {
    await this.fillVisible(this.jumpStyleName, PrintAdminPage.JUMP_STYLE_NAME);
  }

  /** Selenium verifyJumpStyleIsCreated: first list-item text equals the name. */
  async verifyJumpStyleCreated(): Promise<void> {
    const firstItem = this.page
      .locator("#printjumpstylesPanel .libListContentRow.libLine .libListContentItem.libCol")
      .first();
    await this.expectVisible(firstItem, 'a created jump-style row should appear');
    const txt = (await firstItem.innerText()).trim();
    expect(txt.toLowerCase(), 'first jump-style row should be the created one').toBe(
      PrintAdminPage.JUMP_STYLE_NAME.toLowerCase(),
    );
  }
}
