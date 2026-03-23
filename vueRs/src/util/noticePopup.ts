import {ElMessage, type MessageProps} from 'element-plus';

import 'element-plus/es/components/message/style/css';

export const notify = (properties: MessageProps): void => {
    ElMessage(properties);
};

export const notifyError = (content: string): void => {
    ElMessage({
        showClose: true,
        message: content,
        type: 'error',
        grouping: true,
        plain: true,
    });
};

export const notifySuccess = (content: string): void => {
    ElMessage({
        showClose: true,
        message: content,
        type: 'success',
        grouping: true,
        plain: true,
    });
};

export const notifyPlain = (content: string): void => {
    ElMessage({
        showClose: true,
        message: content,
        type: 'info',
        grouping: true,
        plain: true,
    });
};
