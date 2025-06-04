
export interface EmptyConfig {
  valid: false;
}

export const isEmptyConfig = (value: unknown): value is EmptyConfig => {
  return !!value && !(value as EmptyConfig).valid;
}

