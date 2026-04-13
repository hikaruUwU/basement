import { ref, shallowRef, type Ref } from 'vue';
import { isFunction } from 'radash';

export const $state = {
  assemble: <T, K extends Record<string, T>>(
    maybeOrGetter: T | (() => T),
    isShallow = false,
    toggler?: K,
  ) => {
    const setup = () =>
      isFunction(maybeOrGetter) ? maybeOrGetter() : maybeOrGetter;
    const data = (isShallow ? shallowRef(setup()) : ref(setup())) as Ref<T>;

    return {
      data,

      set(val: T) {
        data.value = val;
      },

      toggle(key?: keyof K) {
        if (key !== undefined && toggler) {
          this.set(toggler[key]);
        } else if (typeof data.value === 'boolean') {
          (data.value as any) = !data.value;
        }
      },

      peek() {
        return data.value;
      },

      reset() {
        data.value = setup();
      },
    };
  },
};
