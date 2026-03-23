import {defineConfig, ProxyConfig} from '@rsbuild/core';
import { pluginVue } from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';

const proxies : ProxyConfig = {
  '/api': {
    target: 'https://jsonplaceholder.typicode.com/posts',
    changeOrigin: true,
    pathRewrite: {
      '^/api': '',
    },
  },
};

export default defineConfig({
  server: {
    proxy: proxies
  },
  plugins: [pluginVue()],
  tools: {
    rspack: {
      plugins: [
        AutoImport({
          resolvers: [ElementPlusResolver()],
        }),
        Components({
          resolvers: [ElementPlusResolver()],
        }),
      ],
    },
  },
});
