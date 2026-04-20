import type { MessageProps } from 'element-plus/es/components/message/src/message';
import { ElMessage, type MessageParams } from 'element-plus';
import { getAppContext } from '@shared/util/context.ts';

const joint = {
  duration: 3000,
  plain: true,
  showClose: true,
  grouping: true,
} as const satisfies Partial<MessageProps>;

export const elMessagePresetProp = {
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
} as const satisfies Record<string, Partial<MessageProps>>;

export const $message = {
  call: (options?: MessageParams) => ElMessage(options, getAppContext()),
  preset: elMessagePresetProp,
};
