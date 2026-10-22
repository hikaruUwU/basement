export type Completion = Array<
  {
    value: string | number;
    label?: string;
  } & Record<string, string>
>;
