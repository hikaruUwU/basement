import { ElNotification, type NotificationOptions } from 'element-plus';
import type { NotificationProps } from 'element-plus/es/components/notification/src/notification';
import { $applicationContext } from '@/src';

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

export const $notify = (options?: NotificationOptions) => {
  return {
    call: ElNotification(options, $applicationContext),
    preset: elNotificationPresetProp,
  };
};
