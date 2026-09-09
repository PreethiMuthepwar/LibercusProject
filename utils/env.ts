/**
 * Environment + URL resolution.
 *
 * Mirrors Selenium `framework/Config.java`:
 *   - getCustomerEnv()  -> PROJECT_ENV (PG | TB), defaults to "PG" for DATA selection
 *   - getCustomerUrl()  -> picks a base URL by customer × surface (CMS vs PF3)
 *
 * Faithful port note: in Selenium, when PROJECT_ENV is unset the URL routing
 * falls back to the TB customer, while data selection (getCustomerEnv) defaults
 * to PG. We preserve both behaviours and allow every URL to be overridden by env.
 */

export type Customer = 'PG' | 'TB';
export type Surface = 'cms' | 'pf3' | 'pf3admin';

/** Customer used for DATA selection (channels, shapes). Selenium: getCustomerEnv(). */
export function getCustomerEnv(): Customer {
  const env = (process.env.PROJECT_ENV ?? '').trim().toUpperCase();
  return env === 'TB' ? 'TB' : 'PG';
}

/** Customer used for URL routing. Selenium: getCustomerUrl() defaults to TB when unset. */
function getUrlCustomer(): Customer {
  const env = (process.env.PROJECT_ENV ?? '').trim().toUpperCase();
  if (!env) return 'TB';
  return env === 'TB' ? 'TB' : 'PG';
}

// Defaults transcribed verbatim from Config.getCustomerUrl(). Override via .env.
const DEFAULT_URLS: Record<Customer, Record<Surface, string>> = {
  TB: {
    pf3: 'https://tbmuate-edition.libercus.net/pf3/',
    cms: 'https://tbmuatedit.libercus.net/admin',
    pf3admin: 'https://pgmuat-pf3manage.libercus.net/pf3admin',
  },
  PG: {
    pf3: 'https://pgmuate-edition.libercus.net/pf3/',
    cms: 'https://pgmuatedit.libercus.net/admin',
    pf3admin: 'https://tbqapf3admin.libercus.net/pf3admin',
  },
};

/** Resolve the base URL for a given surface, honouring .env overrides. */
export function baseUrl(surface: Surface): string {
  const customer = getUrlCustomer();
  const overrideKey = `BASE_URL_${customer}_${surface.toUpperCase()}`;
  const override = process.env[overrideKey];
  if (override && override.trim()) return override.trim();
  // Generic single-surface override (e.g. BASE_URL_CMS) if customer-agnostic.
  const generic = process.env[`BASE_URL_${surface.toUpperCase()}`];
  if (generic && generic.trim()) return generic.trim();
  return DEFAULT_URLS[customer][surface];
}

export const CMS_URL = () => baseUrl('cms');
export const PF3_URL = () => baseUrl('pf3');
export const PF3_ADMIN_URL = () => baseUrl('pf3admin');

/** CMS credentials. Selenium read these from data/LoginDetails.yml (committed cleartext). */
export function credentials(): { username: string; password: string } {
  const username = process.env.LIBERCUS_USERNAME;
  const password = process.env.LIBERCUS_PASSWORD;
  if (!username || !password) {
    throw new Error(
      'LIBERCUS_USERNAME / LIBERCUS_PASSWORD must be set (see .env.example). ' +
        'In Selenium these lived in data/LoginDetails.yml — do not commit them here.',
    );
  }
  return { username, password };
}
