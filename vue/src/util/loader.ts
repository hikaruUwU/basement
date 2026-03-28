import { ElLoading } from 'element-plus';
import 'element-plus/es/components/loading/style/css';

const cubeSvg = `
  <svg viewBox="0 0 100 100" class="custom-loading-svg">
    <rect x="10" y="35" width="20" height="20" fill="#409eff">
      <animate attributeName="y" values="35;20;35" dur="0.6s" repeatCount="indefinite" begin="0s" />
      <animate attributeName="opacity" values="1;0.5;1" dur="0.6s" repeatCount="indefinite" begin="0s" />
    </rect>
    <rect x="40" y="35" width="20" height="20" fill="#409eff">
      <animate attributeName="y" values="35;20;35" dur="0.6s" repeatCount="indefinite" begin="0.2s" />
      <animate attributeName="opacity" values="1;0.5;1" dur="0.6s" repeatCount="indefinite" begin="0.2s" />
    </rect>
    <rect x="70" y="35" width="20" height="20" fill="#409eff">
      <animate attributeName="y" values="35;20;35" dur="0.6s" repeatCount="indefinite" begin="0.4s" />
      <animate attributeName="opacity" values="1;0.5;1" dur="0.6s" repeatCount="indefinite" begin="0.4s" />
    </rect>
  </svg>
`;
const DEFAULT_OPTIONS = {
  lock: true,
  text: 'Loading...',
  background: 'rgba(0, 0, 0, 0.3)',
  customClass: 'custom-loading-wrapper',
  spinner: cubeSvg,
};

export const AreaCoverWhen = async <T>(
  runner: () => Promise<T> | T,
  targetElement: string | HTMLElement = 'body',
): Promise<Awaited<Promise<T> | T>> => {
  const loading = ElLoading.service({
    ...DEFAULT_OPTIONS,
    target: targetElement,
    fullscreen: true,
  });
  try {
    const result = await runner();
    return result;
  } finally {
    loading.close();
  }
};

export const CoverWhen = async <T>(
  runner: () => Promise<T> | T,
): Promise<Awaited<Promise<T> | T>> => {
  const loading = ElLoading.service({
    ...DEFAULT_OPTIONS,
    fullscreen: true,
  });
  try {
    const result = await runner();
    return result;
  } finally {
    loading.close();
  }
};
