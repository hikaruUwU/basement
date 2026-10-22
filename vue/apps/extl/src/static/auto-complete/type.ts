export type Completion = {
  value: string
  label?: string
} & Record<string, string>

export const CompletionLoader = <T extends Completion>(filePos: string) => {
  
  
}