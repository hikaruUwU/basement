import { createI18n } from 'vue-i18n';

export const baseLanguage = 'en';

export const supportedLanguage = new Set([baseLanguage, 'zh'] as const);

export const I18NCore = createI18n({
  legacy: false,
  locale: supportedLanguage.has(
    localStorage.getItem('locale') as typeof supportedLanguage extends Set<infer T> ? T : never,
  )
    ? (localStorage.getItem('locale') as typeof supportedLanguage extends Set<infer T> ? T : never)
    : 'en',
  fallbackLocale: 'en',
});
