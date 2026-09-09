import { readFileSync, existsSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join, basename, resolve } from 'node:path';
import { parse as parseYaml } from 'yaml';
import { getCustomerEnv } from './env';
import type { DataRecord } from '../types';

/**
 * Data loaders — ported from Selenium `utils/testDataUtils.java` + `yaml_util.java`.
 *
 * The Selenium project read YAML maps from src/main/resources/.../data/. We copied
 * those files verbatim into ./data and load them the same way. Asset path fields
 * inside the YAML (Image/ZipFile/etc.) point at the old Selenium tree, so we
 * resolve them by basename against ./data.
 */

const __dirname = dirname(fileURLToPath(import.meta.url));
const DATA_DIR = join(__dirname, '..', 'data');

const cache = new Map<string, DataRecord>();

/** yaml_util.loadData — read + parse a YAML file from ./data. */
function loadData(fileName: string): DataRecord {
  if (cache.has(fileName)) return cache.get(fileName)!;
  const path = join(DATA_DIR, fileName);
  if (!existsSync(path)) throw new Error(`Data file not found: ${path}`);
  const parsed = (parseYaml(readFileSync(path, 'utf8')) ?? {}) as DataRecord;
  cache.set(fileName, parsed);
  return parsed;
}

function section(fileName: string, key: string): DataRecord | null {
  const data = loadData(fileName);
  return (key in data ? (data[key] as DataRecord) : null);
}

/**
 * Resolve an asset path referenced inside a YAML file (e.g.
 * "./src/main/resources/test/automation/data/Ad1.jpg") to the real file under
 * ./data. Returns an absolute path. Throws if the asset is missing.
 */
export function resolveAsset(yamlPath: string): string {
  const name = basename(yamlPath);
  const abs = resolve(DATA_DIR, name);
  if (!existsSync(abs)) throw new Error(`Asset not found in data/: ${name} (from "${yamlPath}")`);
  return abs;
}

// --- Per-file accessors (mirror testDataUtils method names) -----------------

export function getStoryData(story: string): DataRecord {
  const d = section('StoryCreation.yml', story);
  if (!d) throw new Error(`Story key not found in StoryCreation.yml: ${story}`);
  return d;
}

/** getShapeForCurrentEnv — picks the Shape for the active PROJECT_ENV (PG/TB). */
export function getShapeForCurrentEnv(storyKey: string): string {
  const story = getStoryData(storyKey);
  const customer = getCustomerEnv();
  const envData = Object.entries(story).find(
    ([k]) => k.toLowerCase() === customer.toLowerCase(),
  )?.[1] as DataRecord | undefined;
  if (!envData) {
    throw new Error(
      `No shape data for project ${customer} under ${storyKey}. Keys: ${Object.keys(story).join(', ')}`,
    );
  }
  return String(envData.Shape);
}

export function getInteractiveAdsData(key: string): DataRecord {
  const d = section('InteractiveAds.yml', key);
  if (!d) throw new Error(`InteractiveAds key not found: ${key}`);
  return d;
}

export function getRichMediaAdData(key: string): DataRecord {
  const d = section('RichMediaAds.yml', key);
  if (!d) throw new Error(`RichMediaAds key not found: ${key}`);
  return d;
}

export function getImageData(key: string): DataRecord {
  const d = section('MediaFiles.yml', key);
  if (!d) throw new Error(`MediaFiles key not found: ${key}`);
  return d;
}

export function getCongeroData(key: string): DataRecord {
  const d = section('CongeroType.yml', key);
  if (!d) throw new Error(`CongeroType key not found: ${key}`);
  return d;
}


export function getDates(): DataRecord {
  return loadData('Dates.yml');
}

export function getUserData(): DataRecord {
  return loadData('Users.yml');
}

export function getTagData(): DataRecord {
  return loadData('Tags.yml');
}
