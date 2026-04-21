import { ElNotification, type NotificationOptions } from 'element-plus';
import type { NotificationProps } from 'element-plus/es/components/notification/src/notification';
import { getAppContext } from '@shared/util/context.ts';

const joint = {
  duration: 3000,
  showClose: true,
} as const satisfies Partial<NotificationProps>;

export const elNotificationPresetProp = {
  success: {
    type: 'success',
    ...joint,
  },
  error: {
    type: 'error',
    ...joint,
  },
  warning: {
    type: 'warning',
    ...joint,
  },
  info: {
    type: 'info',
    ...joint,
  },
} as const satisfies Record<string, Partial<NotificationProps>>;

export const $notify = {
  call: (options?: NotificationOptions) => ElNotification(options, getAppContext()),
  preset: (key: keyof typeof elNotificationPresetProp) => (options?: NotificationOptions) => {
    ElNotification({ ...elNotificationPresetProp[key], ...options }, getAppContext());
  },
};
