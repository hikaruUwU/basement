import * as fs from 'node:fs';
import * as path from 'node:path';
import {fileURLToPath} from 'node:url';

const modules = [
    {
        appsPath: 'apps/web',
        distDir: 'dist',
        targetName: 'web'
    },
    {
        appsPath: 'apps/seo',
        distDir: 'dist',
        targetName: 'seo'
    },
];

//------------------------------------------------------------------

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const rootDir = path.resolve(__dirname, '..');
const targetRootDir = path.join(rootDir, 'dist');
const sourcePublicDir = path.join(rootDir, 'public');

if (fs.existsSync(targetRootDir)) {
    fs.rmSync(targetRootDir, {recursive: true, force: true});
}
fs.mkdirSync(targetRootDir, {recursive: true});

console.log(`> Monorepo Target Collection Starting, ${new Date().toDateString()} ${new Date().toTimeString().split(' ')[0]}`);
console.log()
console.log('result')
console.log(' | ')

if (fs.existsSync(sourcePublicDir)) {
    fs.cpSync(sourcePublicDir, targetRootDir, {recursive: true});
} else {
    console.warn(`> [public] directory not found`);
}

modules.forEach(({appsPath, distDir, targetName}) => {
    const sourceDist = path.join(rootDir, appsPath, distDir);
    const destDist = path.join(targetRootDir, targetName);

    if (fs.existsSync(sourceDist)) {
        fs.cpSync(sourceDist, destDist, {recursive: true});
        console.log(` |-> target/${targetName}/`);
    } else {
        console.warn(`> [${appsPath}] - (${distDir}) unfounded`);
    }
});

console.log()
console.log(`> Monorepo Target Collection Finished, ${new Date().toDateString()} ${new Date().toTimeString().split(' ')[0]}`);