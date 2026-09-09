/**
 * Shared types. Expanded as page objects/data loaders are ported.
 * The Selenium project used loosely-typed YAML maps (Map<String,Object>); we
 * narrow them progressively as each data file is ported in utils/data.utils.ts.
 */

export interface Credentials {
  username: string;
  password: string;
}

/** A generic record loaded from a YAML data file (StoryCreation, Tags, etc.). */
export type DataRecord = Record<string, unknown>;
