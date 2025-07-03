import {BootMixin} from '@loopback/boot';
import {ApplicationConfig} from '@loopback/core';
import {GraphQLBindings, GraphQLComponent} from '@loopback/graphql';
import {
  RestExplorerBindings,
  RestExplorerComponent,
} from '@loopback/rest-explorer';
import {RepositoryMixin} from '@loopback/repository';
import {RestApplication} from '@loopback/rest';
import {ServiceMixin} from '@loopback/service-proxy';

import path from 'path';

import {MySequence} from './sequence';
import {bindServices} from './services';
import {bindConfig} from './config';

export {ApplicationConfig};

export class MobAssistApplication extends BootMixin(
  ServiceMixin(RepositoryMixin(RestApplication)),
) {
  constructor(options: ApplicationConfig = {}) {
    super(options);

    this.component(GraphQLComponent);
    this.configure(GraphQLBindings.GRAPHQL_SERVER).to({
      asMiddlewareOnly: true,
    });
    const server = this.getSync(GraphQLBindings.GRAPHQL_SERVER);
    this.expressMiddleware('middleware.express.GraphQL', server.expressApp);


    // Set up the custom sequence
    this.sequence(MySequence);

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    bindConfig(this as any);
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    bindServices(this as any);

    // Set up default home page
    this.static('/', path.join(__dirname, '../public'));

    // Customize @loopback/rest-explorer configuration here
    this.configure(RestExplorerBindings.COMPONENT).to({
      path: '/explorer',
    });
    this.component(RestExplorerComponent);

    this.projectRoot = __dirname;
    // Customize @loopback/boot Booter Conventions here
    this.bootOptions = {
      controllers: {
        // Customize ControllerBooter Conventions here
        dirs: ['controllers'],
        extensions: ['.controller.js'],
        nested: true,
      },
      graphqlResolvers: {
        // Customize ControllerBooter Conventions here
        dirs: ['graphql-resolvers'],
        extensions: ['.resolver.js'],
        nested: true,
      },
    };
  }
}
