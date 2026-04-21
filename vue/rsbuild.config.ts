import { defineConfig, type RsbuildPlugin } from '@rsbuild/core';
import { pluginVue } from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import { pluginVueJsx } from '@rsbuild/plugin-vue-jsx';
import { pluginSass } from '@rsbuild/plugin-sass';
import ElementPlus from 'unplugin-element-plus';
import os from 'os';

const flag = (mode: string | undefined) => {
  console.log(`
              <-.(\`-')  (\`-')  _  (\`-').->(\`-')  _<-. (\`-')   (\`-')  _<-. (\`-')_ (\`-')      
               __( OO)  (OO ).-/  ( OO)_  ( OO).-/   \\(OO )_  ( OO).-/   \\( OO) )( OO).->   
              '-'---.\\  / ,---.  (_)--\\_)(,------.,--./  ,-.)(,------.,--./ ,--/ /    '._   
              | .-. (/  | \\ /\`.\\ /    _ / |  .---'|   \`.'   | |  .---'|   \\ |  | |'--...__) 
              | '-' \`.) '-'|_.' |\\_..\`--.(|  '--. |  |'.'|  |(|  '--. |  . '|  |)\`--.  .--' 
              | /\`'.  |(|  .-.  |.-._)   \\|  .--' |  |   |  | |  .--' |  |\\    |    |  |    
              | '--'  / |  | |  |\\       /|  \`---.|  |   |  | |  \`---.|  | \\   |    |  |    
              \`------'  \`--' \`--' \`-----' \`------'\`--'   \`--' \`------'\`--'  \`--'    \`--'    
          `);

  console.log(`${mode}, ${new Date().toDateString()} ${new Date().toTimeString().split(' ')[0]}`);
  console.log(`--`.repeat(7).repeat(8));
};

export default defineConfig((_env) => ({
  server: {
    proxy: [],
  },
  source: {
    define: {
      // 'import.meta.env.BUILD_TIME': JSON.stringify(new Date().toLocaleString()),
    },
  },
  output: {
    ...(_env.envMode === 'development'
      ? {
          sourceMap: {
            js: 'source-map',
            css: true,
          },
        }
      : {}),
    // sourceMap: {
    //   js: 'source-map',
    //   css: true
    // },
  },
  plugins: [
    pluginVue(),
    pluginSass(),
    pluginVueJsx({
      vueJsxOptions: {
        resolveType: true,
        enableObjectSlots: true,
      },
    }),
    {
      name: 'flag',
      setup(api) {
        api.onBeforeBuild(() => {
          flag(_env.envMode);
          console.log(
            `[Build] ${os.hostname()}, Node:${process.version}, ` +
              `CPU(S):${os.cpus().length}, ` +
              `RAM:${((os.totalmem() - os.freemem()) / 1024 / 1024 / 1024).toFixed(2)}GB/${(os.totalmem() / 1024 / 1024 / 1024).toFixed(2)}GB` +
              '\n',
          );
        });
        api.onAfterStartDevServer(() => flag(_env.envMode));
      },
    } as RsbuildPlugin,
  ],
  tools: {
    lightningcssLoader: true,
    rspack: {
      plugins: [
        AutoImport({
          imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
          resolvers: [
            ElementPlusResolver({
              importStyle: 'sass',
            }),
          ],
          dts: true,
        }),
        Components({
          resolvers: [
            ElementPlusResolver({
              importStyle: 'sass',
            }),
          ],
        }),
        ElementPlus.rspack({
          useSource: false,
        }),
      ],
      experiments: {
        nativeWatcher: true,
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
  performance: {
    chunkSplit: {
      strategy: 'split-by-experience',
    },
    preload: true,
    prefetch: {
      type: 'async-chunks',
    },
    buildCache: true,
    printFileSize: {
      diff: true,
    },
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
  dev: {
    lazyCompilation: true,
  },
}));
