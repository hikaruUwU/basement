import type { Completion } from '@/static/auto-complete/type';

const data = [
  { value: 'Telex', label: 'Telex, 电放费' },
  { value: 'Equipment Interchange Receipt', label: 'Equipment Interchange Receipt, 设备交接单费' },
  { value: 'Seal', label: 'Seal, 封条费' },
  { value: 'Insurance', label: 'Insurance, 保险费' },
] satisfies Readonly<Completion>;

export const fruitCompletion = (
  queried: string | number,
  callback: (suggestions: Completion) => void,
) => {
  const keyword = String(queried).trim().toLowerCase();
  callback(
    data.filter(
      (currency) =>
        !keyword ||
        currency.value.toLowerCase().includes(keyword) ||
        currency.label.toLowerCase().includes(keyword),
    ),
  );
};
