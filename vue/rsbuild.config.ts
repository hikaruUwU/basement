import {defineConfig, type ProxyConfig, type RsbuildPlugin, rspack} from '@rsbuild/core';
import {pluginVue} from '@rsbuild/plugin-vue';
import AutoImport from 'unplugin-auto-import/rspack';
import Components from 'unplugin-vue-components/rspack';
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers';
import {pluginVueJsx} from '@rsbuild/plugin-vue-jsx';
import {pluginSass} from '@rsbuild/plugin-sass';
import ElementPlus from 'unplugin-element-plus';
import {pluginCompression} from "rsbuild-plugin-compression";
import * as zlib from "node:zlib";

const fixed_proxy: ProxyConfig = [];

//---------------------------------------------------------------------------------------------

const resolveProxy = () => {
    const convert = () => {
        try {
            return JSON.parse(process.env.PUBLIC_PROXY_FROM_TO || '{}') as Record<string, string>;
        } catch (e) {
            throw new Error('PUBLIC_PROXY_FROM_TO is not a valid Record<string, string> : ' + e);
        }
    };

    return {
        convertor: () => convert(),
        resolved: () => {
            return Object.entries(convert()).reduce(
                (acc, [path, target]) => {
                    acc[path] = {
                        target,
                        ws: true,
                        changeOrigin: true,
                        secure: false,
                        pathRewrite: {
                            [`^${path}`]: '',
                        },
                    };

                    return acc;
                },
                {} as Record<string, any>,
            );
        },
    };
};
const flag = () => {
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

    console.log(
        `${new Date().toDateString()} ${new Date().toTimeString().split(' ')[0]}`,
    );

    console.log(`--`.repeat(7).repeat(8));
};

//------------------------------------------------------------------------------------

export default defineConfig((_env) => ({
    server: {
        proxy: Object.assign({}, resolveProxy().resolved(), fixed_proxy),
        compress: false
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
                    css: false,
                },
            }
            : {}),
        // sourceMap: {
        //   js: 'source-map',
        //   css: true
        // },
    },
    plugins: [
        pluginCompression({
            algorithms: [
                {
                    name: "gzip",
                    options:{
                        level: _env.envMode === 'development' ? 1 : 9
                    }
                },
                {
                    name: "brotli",
                    options:{
                        params: {
                            [zlib.constants.BROTLI_PARAM_QUALITY]: _env.envMode === 'development' ? 4 : 11
                        }
                    }
                }
            ],
            threshold: 10240,
            concurrency: 8,
            printResult: true
        }),
        pluginVue({
            splitChunks: {
                vue: true,
                router: true
            }
        }),
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
                api.onBeforeBuild(() => {flag()});
                api.onAfterStartDevServer(() => flag());
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
                    useSource: true,
                }),
                new rspack.CircularDependencyRspackPlugin({}),
                new rspack.CaseSensitivePlugin()
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
    security: {
        sri: {
            algorithm: 'sha512',
            enable: 'auto'
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
        printFileSize: false
    },
    dev: {
        lazyCompilation: true,
        browserLogs: {
            stackTrace: "summary"
        },
        progressBar: {
            id: _env.envMode
        },

    },
    html: {
        template: './src/template.html',
    }
}));
