import { defineConfig, ProxyConfig } from '@rsbuild/core';
import { pluginVue } from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';

const proxies: ProxyConfig = {
  '/mock': {
    target: 'https://jsonplaceholder.typicode.com/posts',
    changeOrigin: true,
    pathRewrite: {
      '^/mock': '',
    },
  },
};

export default defineConfig({
  server: {
    proxy: proxies,
  },
  // output: {
  //   sourceMap: {
  //     js: 'source-map',
  //     css: true
  //   }
  // },
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
  performance: {
    chunkSplit: {
      strategy: 'split-by-experience',
    },
    preload: true,
    prefetch: {
      type: 'async-chunks',
    },
    buildCache: true,
    // bundleAnalyze: {
    //     analyzerMode: 'static',
    //     reportTitle: () => `PerformanceAnalyzer - ${new Date().toLocaleString()}`,
    //     defaultSizes: 'gzip',
    //     openAnalyzer: false,
    //     excludeAssets: [/\.(png|jpe?g|gif|svg|webp)$/i],
    //     generateStatsFile: true,
    //     statsFilename: 'bundle-stats.json',
    // }
  },
});
