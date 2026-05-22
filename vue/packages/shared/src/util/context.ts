import { type AppContext } from 'vue';

let _context: AppContext | null = null;

export const setAppContext = (ctx: AppContext) => {
  _context = ctx;
};

export const getAppContext = () => {
  if (!_context) {
    throw new Error('Application Context not initialized');
  }
  return _context;
};
