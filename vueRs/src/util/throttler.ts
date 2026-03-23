const throttleManager: Map<string, number> = new Map<string, number>();

export const throttler = (
  key: string,
  runnable: () => void,
  delay: number = 300,
): void => {
  const now = Date.now();
  const lastExecTime = throttleManager.get(key) || 0;

  if (now - lastExecTime >= delay) runnable();
};
