import { type Page, expect } from '@playwright/test';
import { CmsBasePage } from './cms-base.page';
import { HomePage } from './home.page';
import { getCongeroData } from '../../utils/data.utils';

/**
 * CMS Congero Administration module: Lookup Data and Congero Types.
 * Ported from Selenium pages/Libercus/CMS/{LookupData,CongeroTypes}.java + RegressionSteps.java.
 */
export class CongeroPage extends CmsBasePage {
  readonly saveAndCloseButton = this.page.locator('button.lib-button-saveclose');

  constructor(page: Page) {
    super(page);
  }

  async saveAndClose(): Promise<void> {
    await expect(this.saveAndCloseButton.first(), 'Save & Close should be attached').toBeAttached({
      timeout: this.defaultWaitMs,
    });
    const n = await this.saveAndCloseButton.count();
    for (let i = 0; i < n; i++) {
      if (await this.saveAndCloseButton.nth(i).isVisible()) {
        await this.jsClick(this.saveAndCloseButton.nth(i));
        return;
      }
    }
    await this.jsClick(this.saveAndCloseButton.first());
  }

  // ---- Lookup Data ----------------------------------------------------------
  readonly lookupNewButton = this.page.locator('#congerolookuptypesPanel').getByRole('button', { name: 'New', exact: true });
  readonly lookupNameLabel = this.page.getByText('Lookup name', { exact: true });
  readonly lookupNameInput = this.page.locator("input[name='LookupName']");
  readonly lookupEnabled = this.page.locator('input.manualsize.lib-post.libCheckboxNoValue');
  readonly lookupDescription = this.page.locator("textarea[name='Description']");
  readonly lookupSource = this.page.locator("select[name='Source']");

  async clickLookupNew(): Promise<void> {
    await this.expectVisible(this.lookupNewButton, 'Lookup Data "New" button should be visible');
    await this.jsClick(this.lookupNewButton);
  }

  /** Selenium "I should see LookupName element". */
  async lookupNameDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.lookupNameLabel, '"Lookup name" label should show');
  }

  /** Fill the Lookup Data form: name, enabled, description, Source=Tags. */
  async fillNewLookupData(): Promise<void> {
    await this.fillVisible(this.lookupNameInput, 'Base Media Files');
    await this.jsClick((await this.firstVisible(this.lookupEnabled)) ?? this.lookupEnabled.first());
    await this.fillVisible(this.lookupDescription, 'Base Media files');
    await this.selectOptionLoose((await this.firstVisible(this.lookupSource)) ?? this.lookupSource.first(), 'Tags');
  }

  // ---- Congero Types --------------------------------------------------------
  readonly typeNewButton = this.page.locator('#congerotypesPanel').getByRole('button', { name: 'New', exact: true });
  readonly typeNameLabel = this.page.getByText('Type Name', { exact: true });
  readonly typeNameInput = this.page.locator("input[name='TypeName']");
  readonly typeSectionId = this.page.locator("select[name='SectionID']");
  readonly typeObjectFileName = this.page.locator("input[name='CongeroLabel']");
  readonly typeEnabled = this.page.locator("#congerotypesEdit-main input[name='Active']");
  readonly typeShowInMenu = this.page.locator("input[name='InMenu']");
  readonly typeIncludeInSearch = this.page.locator("input[name='IndexSearch']");
  readonly typeFieldDefDataType = this.page.locator('select.libListSelectButtonBarItem.lib-framefield-select-FieldDefs');
  readonly typeAddButton = this.page.locator('.libAdvancedField.libListField.lib-group1').getByRole('button', { name: 'Add', exact: true });
  readonly typeFieldDefLine = this.page.locator('.libListLine');
  readonly typeFieldId = this.page.locator("input[name='FieldID']");
  readonly typeFieldName = this.page.locator("input[name='FieldName']");
  readonly typeDataSource = this.page.locator("select[name='FieldSource']");
  readonly typeNewButtonOfCongero = this.page.locator("button[class='lib-button-new ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary btnFixed'] span[class='ui-button-text']");

  private get congeroType() {
    return getCongeroData('CongeroType');
  }

  async clickTypeNew(): Promise<void> {
    await this.expectVisible(this.typeNewButton, 'Congero Types "New" button should be visible');
    await this.jsClick(this.typeNewButton);
  }

  /** Selenium "I should see TypeName element". */
  async typeNameDisplayed(): Promise<void> {
    await this.expectAnyVisible(this.typeNameLabel, '"Type Name" label should show');
  }

  /** Fill the Congero Type form up to the FieldDefinitions data-type select. */
  async fillNewCongeroType(): Promise<void> {
    await this.fillVisible(this.typeNameInput, String(this.congeroType.TypeName));
    await this.selectOptionLoose((await this.firstVisible(this.typeSectionId)) ?? this.typeSectionId.first(), 'test');
    await this.fillVisible(this.typeObjectFileName, String(this.congeroType.ObjectFileName));
    await this.jsClick((await this.firstVisible(this.typeEnabled)) ?? this.typeEnabled.first());
    await this.jsClick((await this.firstVisible(this.typeShowInMenu)) ?? this.typeShowInMenu.first());
    await this.jsClick((await this.firstVisible(this.typeIncludeInSearch)) ?? this.typeIncludeInSearch.first());
    await this.selectOptionLoose(
      (await this.firstVisible(this.typeFieldDefDataType)) ?? this.typeFieldDefDataType.first(),
      'Select Field',
    );
  }

  /** Selenium "I click on Add element" + "verify field is added under the field definition". */
  async addFieldDefinition(): Promise<void> {
    await this.jsClick(this.typeAddButton);
    await this.expectVisible(this.typeFieldDefLine.first(), 'a field-definition line should appear after Add');
  }

  /** Fill the field-definition ID, Name and data source, then save & close. */
  async fillFieldDefinition(): Promise<void> {
    await this.fillVisible(this.typeFieldId, String(this.congeroType.FieldId));
    await this.fillVisible(this.typeFieldName, String(this.congeroType.FieldName));
    await this.selectOptionLoose((await this.firstVisible(this.typeDataSource)) ?? this.typeDataSource.first(), 'Tags');
  }

  async refresh(): Promise<void> {
    await this.page.reload({ waitUntil: 'domcontentloaded', timeout: 120_000 });
  }

  /**
   * Selenium verifyCongeroTypeisCreated: from the "Congero" main menu, open the
   * submenu named after the created type — proving the type now exists as a menu item.
   */
  async verifyCongeroTypeCreated(): Promise<void> {
    const home = new HomePage(this.page);
    await home.clickMainMenu('Congero');
    await home.clickSubMenu(String(this.congeroType.TypeName));
  }

  /** Selenium: click the type's own New, then verify the custom field label is displayed. */
  async clickNewOfCongeroAndVerifyField(): Promise<void> {
    await this.expectVisible(this.typeNewButtonOfCongero.first(), 'the Congero type "New" button should show');
    await this.jsClick(this.typeNewButtonOfCongero.first());
    // Selenium verifyFieldIsDisplayed: //label[text()='<FieldName>'].
    const fieldLabel = this.page.getByText(String(this.congeroType.FieldName), { exact: true });
    await this.expectVisible(fieldLabel.first(), `custom field "${this.congeroType.FieldName}" should be displayed`);
  }
}
