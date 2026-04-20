import { ElLoading, type LoadingInstance, type LoadingOptions } from 'element-plus';
import { getAppContext } from '@shared/util/context.ts';

const getInstance = (options?: LoadingOptions) =>
  ElLoading.service(
    {
      lock: true,
      text: 'Loading',
      background: 'rgba(0, 0, 0, 0.7)',
      ...options,
    },
    getAppContext(),
  );

export const $loading = {
  call: (options?: LoadingOptions) => {
    let instance: LoadingInstance | null = null;

    const activate = () => {
      if (!instance) {
        instance = getInstance(options);
      }
      return instance;
    };

    const deactivate = () => {
      if (instance) {
        instance.close();
        instance = null;
      }
    };

    const closeAfter = async <T>(runner: () => T | Promise<T>): Promise<T> => {
      activate();
      try {
        return await runner();
      } finally {
        deactivate();
      }
    };

    return {
      get instance() {
        return instance;
      },
      activate,
      deactivate,
      closeAfter,
    };
  },
};
