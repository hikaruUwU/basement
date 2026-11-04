import {
  type ConfigParams,
  type ProxyConfig,
  type RsbuildPlugin,
  rspack,
  type RsbuildConfig,
  type OutputConfig,
} from '@rsbuild/core';
import { pluginVue } from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import { pluginVueJsx } from '@rsbuild/plugin-vue-jsx';
import { pluginSass } from '@rsbuild/plugin-sass';
import { pluginCompression } from 'rsbuild-plugin-compression';
import * as zlib from 'node:zlib';
import { pluginTypeCheck } from '@rsbuild/plugin-type-check';
import { pluginImageCompress } from '@rsbuild/plugin-image-compress';
import { availableParallelism } from 'node:os';

const fixed_proxy: ProxyConfig = [];

//---------------------------------------------------------------------------------------------

const resolveProxy = () => ({
  resolved: () => {
    try {
      return Object.fromEntries(
        Object.entries(
          JSON.parse(process.env.PUBLIC_PROXY_FROM_TO || '{}') as Record<string, string>,
        ).map(([path, target]) => [
          path,
          {
            target,
            ws: true,
            changeOrigin: true,
            secure: false,
            pathRewrite: { [`^${path}`]: '' },
          },
        ]),
      );
    } catch (e) {
      throw new Error('PUBLIC_PROXY_FROM_TO is not a valid Record<string, string> : ' + e);
    }
  },
});

const flag = () => {
  console.log(`
   ___  ___  ________  ___  ___  ________  ________  ___     
|\\  \\|\\  \\|\\   __  \\|\\  \\|\\  \\|\\_____  \\|\\   __  \\|\\  \\    
\\ \\  \\\\\\  \\ \\  \\|\\  \\ \\  \\\\\\  \\\\|___/  /\\ \\  \\|\\  \\ \\  \\   
 \\ \\   __  \\ \\  \\\\\\  \\ \\  \\\\\\  \\   /  / /\\ \\   __  \\ \\  \\  
  \\ \\  \\ \\  \\ \\  \\\\\\  \\ \\  \\\\\\  \\ /  /_/__\\ \\  \\ \\  \\ \\  \\ 
   \\ \\__\\ \\__\\ \\_______\\ \\_______\\\\________\\ \\__\\ \\__\\ \\__\\
    \\|__|\\|__|\\|_______|\\|_______|\\|_______|\\|__|\\|__|\\|__|
          `);

  console.log(`${new Date().toDateString()} ${new Date().toTimeString().split(' ')[0]}`);
  console.log(`--`.repeat(7).repeat(8));
};
//------------------------------------------------------------------------------------
const createRsbuildConfig = (_env: ConfigParams): RsbuildConfig => ({
  server: {
    proxy: Object.assign({}, resolveProxy().resolved(), fixed_proxy),
    compress: false,
  },
  output: {
    ...(_env.envMode === 'development'
      ? {
          sourceMap: {
            js: 'source-map',
            css: false,
          } as OutputConfig,
        }
      : {}),
    assetPrefix: '/extl/',
  } as OutputConfig,
  plugins: [
    pluginTypeCheck({
      enable: process.env.NODE_ENV === 'development',
      tsCheckerOptions: {
        typescript: {
          mode: 'readonly',
          tsgo: true,
        },
        devServer: false,
      },
    }),
    pluginImageCompress([
      {
        use: 'jpeg',
        quality: 82,
      },
      {
        use: 'png',
        minQuality: 70,
        maxQuality: 85,
      },
      {
        use: 'webp',
        quality: 82,
      },
      {
        use: 'svg',
        floatPrecision: 2,
      },
      {
        use: 'ico',
      },
    ]),
    pluginCompression({
      algorithms: [
        {
          name: 'brotli',
          options: {
            params: {
              [zlib.constants.BROTLI_PARAM_QUALITY]: 11,
            },
          },
        },
        {
          name: 'gzip',
          options: {
            level: zlib.constants.Z_BEST_COMPRESSION,
          },
        },
      ],
      threshold: 10240,
      concurrency: availableParallelism() / 2,
      printResult: false,
    }),
    pluginVue({
      splitChunks: {
        vue: true,
        router: true,
      },
    }),
    pluginSass(),
    pluginVueJsx({
      vueJsxOptions: {
        resolveType: true,
        enableObjectSlots: true,
        optimize: true,
      },
    }),
    {
      name: 'flag',
      setup(api) {
        api.onBeforeBuild(() => flag());
        api.onAfterStartDevServer(() => flag());
      },
    } as RsbuildPlugin,
  ],
  tools: {
    lightningcssLoader: true,
    rspack: {
      plugins: [
        AutoImport({
          imports: ['vue', 'vue-router', 'pinia', 'vue-i18n'],
          resolvers: [
            ElementPlusResolver({
              importStyle: 'css',
            }),
          ],
          dts: true,
        }),
        Components({
          resolvers: [
            ElementPlusResolver({
              importStyle: 'css',
            }),
          ],
        }),
        new rspack.CircularCheckRspackPlugin({}),
        new rspack.CaseSensitivePlugin(),
      ],
      experiments: {
        nativeWatcher: true,
        deferImport: true,
      },
      cache: {
        type: 'memory',
      },
      stats: {
        preset: 'detailed',
      },
      optimization: {
        chunkIds: 'compat-hashed',
        moduleIds: 'compat-hashed',
      },
    },
    swc: {
      jsc: {
        parser: {
          syntax: 'typescript',
          tsx: true,
        },
        transform: {
          react: {
            runtime: 'automatic',
            importSource: 'vue',
          },
        },
      },
    },
  },
  security: {
    sri: {
      algorithm: 'sha512',
      enable: _env.envMode !== 'development',
    },
  },
  splitChunks: {
    preset: 'default',
  },
  performance: {
    prefetch: {
      type: 'async-chunks',
    },
    buildCache: true,
    printFileSize: {
      diff: true,
      compressed: {
        type: 'brotli',
        level: 11,
      },
    },
  },
  dev: {
    lazyCompilation: {
      imports: false,
      entries: false,
    },
    browserLogs: {
      stackTrace: 'summary',
    },
    progressBar: {
      id: _env.envMode,
    },
  },
  html: {
    template: './src/template.html',
  },
});

export default createRsbuildConfig;
