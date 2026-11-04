import { I18NCore, supportedLanguage } from '@/core/internationalization/core';

const registry = new Set<symbol>();

export const I18NMessageLoader = {
  load: async (
    moduleKey: symbol,
    importFn: () => Promise<{
      default: Record<
        string,
        {
          en: string;
        } & Partial<Record<typeof supportedLanguage extends Set<infer T> ? T : never, string>>
      >;
    }>,
  ) => {
    if (registry.has(moduleKey)) return;

    const resolved = (await importFn()).default;

    const mod: Record<string, Record<string, string>> = {};

    for (const [key, locales] of Object.entries(resolved))
      for (const [locale, text] of Object.entries(locales))
        if (text !== undefined) {
          if (!mod[locale]) mod[locale] = {};
          mod[locale][key] = text;
        }

    for (const [locale, messages] of Object.entries(mod))
      I18NCore.global.mergeLocaleMessage(locale, messages);

    registry.add(moduleKey);
  },
};
