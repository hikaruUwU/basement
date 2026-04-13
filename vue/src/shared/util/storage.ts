export const $localStorage = {
  set(key: string, value: string) {
    localStorage.setItem(key, value);
  },

  get(key: string): string | null {
    return localStorage.getItem(key);
  },

  remove(key: string) {
    localStorage.removeItem(key);
  },

  clear() {
    localStorage.clear();
  },
};
