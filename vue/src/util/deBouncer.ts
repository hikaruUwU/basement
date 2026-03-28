const manager = new WeakMap<Object, ReturnType<typeof setTimeout>>();

export const deBouncer = (
  key: Object,
  runnable: () => void,
  delay: number = 300,
): void => {
  if (manager.has(key)) clearTimeout(manager.get(key));

  const timer = setTimeout(() => {
    runnable();
    manager.delete(key);
  }, delay);

  manager.set(key, timer);
};
