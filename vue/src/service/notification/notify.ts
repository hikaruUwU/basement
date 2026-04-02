import {ElNotification, type NotificationOptions} from "element-plus";
import {$applicationContext} from "../../index.ts";
import type {NotificationProps} from "element-plus/es/components/notification/src/notification";

const joint = {
    duration: 3000,
    showClose: true,
} as const satisfies Partial<NotificationProps>

export const elNotificationPresetProp = {
    success: {
        type: 'success',
        ...joint
    },
    error: {
        type: 'error',
        ...joint
    },
    warning: {
        type: 'warning',
        ...joint
    },
    info: {
        type: 'info',
        ...joint
    }
} as const satisfies Record<string, Partial<NotificationProps>>

export const $notify = (options?: NotificationOptions) => ElNotification(options, $applicationContext)