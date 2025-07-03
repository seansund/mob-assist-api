import {ModelRef} from '../datatypes';

// eslint-disable-next-line
export const entityToModel = <E = any, M extends E & ModelRef = any> (val: E): M => {
  // eslint-disable-next-line
  return val as any
}

// eslint-disable-next-line
export const entitiesToModels = <E = any, M extends E & ModelRef = any> (vals: E[]): M[] => {
  // eslint-disable-next-line
  return vals as any
}
