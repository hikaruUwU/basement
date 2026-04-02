import type {MessageProps} from "element-plus/es/components/message/src/message";
import {ElMessage, type MessageParams} from "element-plus";
import {$applicationContext} from "../../index.ts";

const joint = {
    duration: 3000,
    plain: true,
    showClose: true,
    grouping: true,
} satisfies Partial<MessageProps>

export const elMessagePresetProp = {
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
} satisfies Record<string, Partial<MessageProps>>

export const $message = (options?: MessageParams) => ElMessage(options, $applicationContext)

export const $message_preset = (which: keyof typeof elMessagePresetProp) => {
    return elMessagePresetProp[which]
}