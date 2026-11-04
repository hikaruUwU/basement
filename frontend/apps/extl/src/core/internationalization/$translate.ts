import { useI18n } from 'vue-i18n';

export const $translate = <KeyType extends string>() => {
  const { t, ...oth } = useI18n();

  return {
    t: (key: KeyType, ...args: any[]) => (t as any)(key, ...args),
    oth: oth as Omit<ReturnType<typeof useI18n>, 't'>
  };
};
