
export type Optional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;

export type RequireOne<T, K extends keyof T> = {
  [X in Exclude<keyof T, K>]?: T[X]
} & {
  [P in K]-?: T[P]
}

export const isEmpty = <T>(value: T[] | undefined): boolean => {
  return value === undefined || value.length === 0;
}
