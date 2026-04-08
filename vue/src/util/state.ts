import { ref, shallowRef, type Ref } from 'vue';
import {isFunction} from "radash";

export const $state = <T>(maybeOrGetter: T | (() => T), isShallow = false) => {
    const setup = () => (isFunction(maybeOrGetter) ? maybeOrGetter() : maybeOrGetter);

    const data = (isShallow ? shallowRef(setup()) : ref(setup())) as Ref<T>;

    return {
        data,

        set(val: T) {
            data.value = val;
        },

        peek() {
            return data.value;
        },

        reset() {
            data.value = setup();
        }
    };
};