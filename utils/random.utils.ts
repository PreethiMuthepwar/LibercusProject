/**
 * Random helpers — ported from Selenium `framework/Random.java`.
 * Behaviour preserved exactly (same alphabets, same semantics).
 */

const ALPHA = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';

/** Inclusive integer in [min, max]. Selenium getRandomIntBetween. */
export function randomIntBetween(min: number, max: number): number {
  return Math.floor(Math.random() * (max + 1 - min)) + min;
}

/** Random index in [0, size). Selenium getRandomIndex. */
export function randomIndex(size: number): number {
  return randomIntBetween(0, size - 1);
}

function getRandom(size: number, input: string): string {
  let sb = '';
  while (sb.length < size) sb += input.charAt(randomIndex(input.length));
  return sb;
}

/** Random alphabetic string of given length. Selenium getRandomString. */
export function randomString(size: number): string {
  return getRandom(size, ALPHA);
}
