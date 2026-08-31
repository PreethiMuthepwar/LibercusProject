# playwright-libercus

Playwright/TypeScript migration of the `LibercusProject` Selenium/Cucumber suite
(Libercus **CMS** admin + **PF3** reader). See **[MIGRATION-REPORT.md](./MIGRATION-REPORT.md)**
for full status and **[PARITY-REPORT.md](./PARITY-REPORT.md)** for the Playwright-vs-Selenium
pass/fail cross-check. Migration playbook + verification policy: `../CLAUDE.md`.

## Quick start

```bash
npm ci
npx playwright install chromium
cp .env.example .env          # set LIBERCUS_USERNAME / LIBERCUS_PASSWORD / PROJECT_ENV=TB
npx playwright test --project=cms --grep @smoke
npx playwright show-report
```

> The committed test credentials work against **TB** (`PROJECT_ENV=TB`), not PG.

## Layout

- `playwright.config.ts` — projects: `cms`, `pf3`, `regression`; generous timeouts (slow app).
- `global-setup.ts` — logs into the CMS once, saves `auth/cms.json` (storageState).
- `pages/base.page.ts` + `pages/cms/*` — page objects (every locator carries its Selenium `@FindBy` provenance).
- `pages/cms/cms-base.page.ts` — shared CMS helpers: channel/date filter, datepicker, `waitForListSettled`, record counts.
- `fixtures/index.ts` — single fixtures file injecting page objects.
- `tests/cms/*.spec.ts` — Logout, ContentCreation, Copy, Delete, Download, Filter, Update.
- `utils/` — `env` (PG/TB + URL routing), `data.utils` (YAML loaders), `random.utils`.
- `data/` — YAML test data + assets (copied verbatim from the Selenium project).
- `scripts/inspect-*.ts` — interactive DOM diagnostics used to derive selectors (run with `npx tsx`).

## Running

```bash
npx playwright test --project=cms                 # all CMS tests
npx playwright test --project=cms -g "Copy a Story"
npx playwright test --project=cms --grep @Filter
HEADLESS=false npx playwright test --headed -g "..."   # watch in a browser
```

## Notes / gotchas (see `../CLAUDE.md` §18 + memory)

- The app is **slow**: lists render ~10–12 s after Update — handled by `waitForListSettled()`.
- The "Publish Date" filter shows a contextual `#help` tooltip that must be dismissed
  (Selenium's dismiss has a timing bug; the port fixes it — see PARITY-REPORT §3).
- Many controls are overlapped by overlays → use `jsClick`; the datepicker title uses a
  non-breaking space (normalise before parsing).
- Several flows are flaky→green on retry; CI uses `retries: 3`.

## Known gaps (next work)

- CMS smoke (with data): `Update → Print Page Section Letter` (flaky), `Download → Rich Media`.
- Deferred (drag-drop / dialog): ContentCreation→Print Pages, Production View, Output of Print Pages.
- **Not yet ported: PF3 (~33), CMS regression (~24).**
