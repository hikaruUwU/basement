import { supportedLanguage } from '@/core/internationalization/core';

const AuthModuleI18nMessage = {
  login: {
    en: 'Login',
    zh: '登录',
  },
  account: {
    en: 'Account',
    zh: '账号',
  },
  password: {
    en: 'Password',
    zh: '密码',
  },
  verificationCode: {
    en: 'verification code',
    zh: '验证码',
  },
  acknowledgePolicies: {
    en: 'By continuing to log in, you acknowledge that you have read and agreed to the terms',
    zh: '继续登录表示您已阅读并同意条款',
  },
  cookiePolicy: {
    en: 'Cookie Policy',
    zh: 'Cookie政策',
  },
  privacyPolicy: {
    en: 'Privacy Policy',
    zh: '隐私政策'
  }
} as const satisfies Record<
  string,
  Record<typeof supportedLanguage extends Set<infer T> ? T : never, string>
>;

export type AuthModuleI18nMessageIdentifier = keyof typeof AuthModuleI18nMessage;

export default AuthModuleI18nMessage;
