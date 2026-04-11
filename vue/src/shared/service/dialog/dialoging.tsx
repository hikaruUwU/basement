import { h, ref, render, type VNode, watch } from 'vue';
import { type DialogProps, ElButton, ElDialog, ElDivider } from 'element-plus';
import { $applicationContext } from '@/src';

export const $dialog = () => {
  return {
    call: (content: VNode | string, dialogProps: Partial<DialogProps> = {}) => {
      const container = document.createElement('div');
      const visible = ref(true);

      const destroy = () => {
        render(null, container);
        container.remove();
      };

      const update = () => {
        const vnode = h(
          ElDialog,
          {
            title: 'Hint',
            ...dialogProps,
            modelValue: visible.value,
            'onUpdate:modelValue': (val: boolean) => {
              visible.value = val;
            },
            onClosed: destroy,
          },
          { default: () => content },
        );

        if ($applicationContext) vnode.appContext = $applicationContext;
        render(vnode, container);
      };

      const stopWatch = watch(visible, (newVal) => {
        update();
        if (!newVal) {
          stopWatch();
        }
      });

      update();
      document.body.appendChild(container);

      return {
        visible,
        close: () => {
          visible.value = false;
        },
      };
    },
    preset: {
      plain: ({
        content,
        onConfirm,
        onCancel,
        onBoth,
        confirmText = 'Confirm',
        cancelText = 'Cancel',
        style = {},
      }: {
        content: any;
        onConfirm?: () => void;
        onCancel?: () => void;
        onBoth?: () => void;
        confirmText?: string;
        cancelText?: string;
        style?: any;
      }) => (
        <div>
          <div style={style}>{content}</div>
          <ElDivider style={{ margin: '12px 0' }} />
          <ElButton
            type="primary"
            onClick={() => {
              onConfirm?.();
              onBoth?.();
            }}
          >
            {() => confirmText}
          </ElButton>
          <ElButton
            plain
            onClick={() => {
              onCancel?.();
              onBoth?.();
            }}
          >
            {() => cancelText}
          </ElButton>
        </div>
      ),
    },
  };
};
