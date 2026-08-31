# Locator Audit — Playwright-standards review

_Reviewed all page objects against Playwright's official locator priority
(`getByRole` > `getByLabel`/`getByText`/`getByPlaceholder`/… > `getByTestId`; avoid
XPath/CSS coupled to DOM structure; chain/filter). Grounded in the LIVE accessibility
trees of both apps, inspected via the Playwright MCP._

## Headline

| | Count (before) | Verdict |
|---|---|---|
| XPath locators in page objects | 93 | ❌ not recommended — refactor |
| `getByRole` | 20 (mostly new PF3) | ✅ |
| `getByText` | 7 | ✅ where text-based |
| `getByLabel` | 0 | ➖ should be used for CMS login + labelled fields |
| CSS attribute (non-XPath) | 14 | ✅ acceptable (attribute selectors, not structural) |

**The CMS objects were faithful Selenium `@FindBy` XPath ports — correct behaviour, wrong
style.** Both apps expose far better semantics than the XPath assumed.

## What the live a11y trees show

**CMS login** (`dialog "Login"`): `textbox "Username"`, `textbox "Password"`, `button "Login"`
→ use `getByLabel('Username')`, `getByLabel('Password')`, `getByRole('button', {name:'Login'})`.

**CMS dashboard / nav:**
- `link "Log Out"` → `getByRole('link', {name:'Log Out'})` (was `a#logout`).
- Main menu: `heading/link "Content"`, "Congero", "Feeds", "Print Administration", … →
  `getByRole('link', {name:'Content'})` (was `//h3/a[text()='Content']`).
- Submenu items: `link "Stories"`, "Home", "User Preferences" → `getByRole('link', {name:'Stories'})`
  (was `//div[@class='menuItem' and text()='Stories']`).
- Content tabs: `tab "Home"` → `getByRole('tab', {name:'Home'})`.

**CMS story toolbar** (from the live DOM dump): real `<button name="new">…<span class="ui-button-text">New</span></button>`
→ panel-scoped `page.locator('#storyPanel').getByRole('button', {name:'New'})`
(was `//div[@id='storyPanel']//span[@class='ui-button-text' and text()='New']`).
The same applies to Edit/Delete/Copy/Update/Save & Close/OK/Add.

**CMS form controls:** `<select name="channel|Status|Shape">`, `<input name="Slug|…">` have **no
visible `<label>`** in this jQuery-UI app, so `getByLabel`/`getByRole('combobox',{name})` can't name
them. The recommended fallback is an **attribute CSS selector** (`select[name="channel"]`,
`input[name="Slug"]`) — stable and NOT structure-coupled, strictly better than XPath.

**PF3 reader:** menu items are `button "TOC"`, `button "Sections"`, …; views open as `dialog`s with
headings; nav arrows are icon-only (no name). → `getByRole('button', {name})` for the menu,
`getByRole('dialog')` to verify the opened view, `button[mattooltip="Next Section"]` (attribute CSS)
for the unnamed arrows, `getByText('Menu')` for the role-less trigger `<div>`. **Already refactored.**

## Recommended replacement rules (applied / to apply)

| Old (XPath) | New (recommended) |
|---|---|
| `//button[…]//span[…text()='New']` (toolbar) | `page.locator('#<panel>').getByRole('button', {name:'New'})` |
| `//a[@id='logout']` | `getByRole('link', {name:'Log Out'})` |
| `//h3/a[text()='Content']`, `//div[@class='menuItem' and text()='Stories']` | `getByRole('link', {name:'…'})` |
| `//a[text()='Home']` (CMS) | `getByRole('tab', {name:'Home'})` / `getByRole('link', …)` |
| `input[name="username"]` / `//input[@name='Slug']` | `getByLabel('Username')` (login) / `input[name="Slug"]` (unlabelled) |
| `//select[@name='channel']` | `select[name="channel"]` (attribute CSS) |
| view headers / list text | `getByText(...)` (pierces shadow DOM; XPath does not) |

## Status — COMPLETE (all page objects refactored; only 2 justified XPath locators remain)

**Correction (2026-06-16 code review):** the earlier "0 XPath" claim was inaccurate — two
class-predicate XPath locators survived in `cms-base.page.ts` (`filterCalendar`,
`filterRangeSelect`) and were converted to CSS (`input.libFilterInput.libFilterDate`,
`select.libFilterSelect.libFilterDate`) during the review. The ONLY remaining XPath is in
`print-pages.page.ts` — `dragSourceStory`/`dragSourceAd` use `xpath=//…/..` for the
parent-axis step (selecting the draggable wrapper of `.libPageStory`/`.libPageSidebar`),
which CSS cannot express; this is the documented, justified exception.

Verified: `grep -rn "xpath=" pages/` → only the 2 `/..` drag locators; no `locator('//…')`
remains; `npx tsc --noEmit` → clean.

| Page object | Now uses | Validated live |
|---|---|---|
| `login.page.ts` | getByLabel / getByRole | ✅ pass |
| `home.page.ts` | getByRole(tab/link) | ✅ pass |
| `pf3-home.page.ts` | getByRole(button/menuitem/dialog/document) / getByText / attr-CSS | ✅ 8/8 menu |
| `story.page.ts` | getByRole / getByText / attr-CSS / nth-chaining | ✅ Create/Delete/Search (Copy/Update re-validating) |
| `cms-base.page.ts` | getByRole / getByText / attr-CSS | ✅ via the above |
| `interactive-ads.page.ts` | panel-scoped getByRole / attr-CSS / getByText | ⏳ re-validating |
| `rich-media-ads.page.ts` | panel-scoped getByRole / attr-CSS / getByText | ⏳ re-validating |
| `print-ads.page.ts` | panel-scoped getByRole / attr-CSS / getByText | ⏳ re-validating |
| `print-pages.page.ts` | panel-scoped getByRole / attr-CSS / nth-chaining | ⏳ re-validating |
| `media-files.page.ts` | panel-scoped getByRole / attr-CSS | ⏳ re-validating |

**How faithfulness (Selenium parity) was preserved during the refactor:**
- Each new locator targets the **same element** the Selenium `@FindBy` did — attribute CSS for
  `@name`/`@id` controls (`select[name='X']`, `input[name='X']`, `#id`), `getByText` for text/labels,
  panel-scoped `getByRole('button')` for toolbar buttons (clicking the button activates the same
  handler as Selenium's click on the inner `<span>`).
- **Click semantics are unchanged** — wherever the methods used `jsClick` vs native `.click()` (mirroring
  Selenium's `Actions.click`), that is untouched. The refactor changed locator **style only**, never behaviour.
- **Datepicker prev/next was deliberately NOT made `jsClick`.** Selenium's `Actions.click("Next")` is a
  *native* click on `//span[@class='ui-icon ui-icon-circle-triangle-e']`; we match that exactly
  (`span.ui-icon-circle-triangle-e`, native `.click()`). If a real overlay intercepts it, our test must
  fail the same way Selenium would — a JS click there would mask a genuine app/env bug.

**Principle:** none of this changes behaviour — it raises locator quality to the Playwright standard
(`getByRole` > `getByText`/`getByLabel` > attribute-CSS; no structural XPath), while keeping the Selenium
`@FindBy` provenance comments and exact element/click parity for traceability.
