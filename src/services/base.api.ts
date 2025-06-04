
// eslint-disable-next-line
export abstract class BaseApi<T, C = Omit<T, 'id'>, F = any, I = string> {
  abstract list(filter?: F): Promise<T[]>;
  abstract create(data: C): Promise<T>;
  abstract get(id: I): Promise<T | undefined>;
  abstract update(id: I, data: Partial<T>): Promise<T>;
  abstract delete(id: I): Promise<T | undefined>;
}
