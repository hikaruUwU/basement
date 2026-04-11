export const $url = () => {
  return {
    getPath(): string {
      return window.location.pathname;
    },

    getParam(key: string): string | null {
      return new URLSearchParams(window.location.search).get(key);
    },

    getAllParams(): Record<string, string> {
      return Object.fromEntries(
        new URLSearchParams(window.location.search).entries(),
      );
    },

    replace(url: string) {
      window.location.replace(url);
    },
  };
};
