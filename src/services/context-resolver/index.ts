import {Context} from '@loopback/context';
import {CONTEXT_RESOLVER_API} from './context-resolver.api';
import {ContextResolverImpl} from './context-resolver.impl';

export * from './context-resolver.api';

export const bindContextResolverApi = (service: Context) => {
  service.bind(CONTEXT_RESOLVER_API).toClass(ContextResolverImpl)
}
