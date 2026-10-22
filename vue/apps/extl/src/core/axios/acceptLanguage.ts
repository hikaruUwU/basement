import { I18NCore, supportedLanguage } from '@/core/internationalization/core';
import { computed } from 'vue';

const AcceptLanguageHeader = Object.freeze({
  zh: 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6,zh-TW;q=0.5',
  en: 'en,en-US;q=0.9,en-GB;q=0.8,zh-CN;q=0.7,zh;q=0.6',
}) satisfies Record<typeof supportedLanguage extends Set<infer T> ? T : never, string>;

const AcceptLanguage = computed(() => {
  const locale = I18NCore.global.locale.value;

  return AcceptLanguageHeader[locale as keyof typeof AcceptLanguageHeader];
});

export { AcceptLanguage };
