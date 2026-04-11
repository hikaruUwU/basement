import {defineConfig, type ProxyConfig} from '@rsbuild/core';
import {pluginVue} from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers';
import {pluginVueJsx} from "@rsbuild/plugin-vue-jsx";

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
    plugins: [
        pluginVue(),
        pluginVueJsx({
            vueJsxOptions: {
                resolveType: true,
                enableObjectSlots: true
            }
        }),
        {
            name: 'flag',
            setup(api) {
                api.onBeforeBuild(() => {
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

                    console.log('-'.repeat(108));
                });
            },
        },
    ],
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
        swc:{
            jsc:{
                parser:{
                    syntax: 'typescript',
                    tsx: true
                },
                transform:{
                    react:{
                        runtime: "automatic",
                        importSource: "vue"
                    }
                }
            }
        }
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
