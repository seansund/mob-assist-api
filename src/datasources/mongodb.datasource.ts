import {inject, lifeCycleObserver, LifeCycleObserver} from '@loopback/core';
import {AnyObject, juggler} from '@loopback/repository';

const config = {
  name: 'mongodb',
  connector: 'mongodb',
  host: 'localhost',
  port: 27017,
  user: 'mongodb',
  password: 'mongodb',
  database: 'mongodb',
  useNewUrlParser: true
};

function updateConfig(dsConfig: AnyObject) {
  dsConfig.url = process.env.MONGODB_CONNECT_STRING;
  dsConfig.host = process.env.MONGODB_HOST;
  dsConfig.port = +process.env.MONGODB_PORT!;
  dsConfig.user = process.env.MONGODB_USERNAME;
  dsConfig.password = process.env.MONGODB_PASSWORD;
  dsConfig.database = process.env.MONGODB_DATABASE;

  return dsConfig;
}


// Observe application's life cycle to disconnect the datasource when
// application is stopped. This allows the application to be shut down
// gracefully. The `stop()` method is inherited from `juggler.DataSource`.
// Learn more at https://loopback.io/doc/en/lb4/Life-cycle.html
@lifeCycleObserver('datasource')
export class MongodbDataSource extends juggler.DataSource
  implements LifeCycleObserver {
  static dataSourceName = 'mongodb';
  static readonly defaultConfig = config;

  constructor(
    @inject('datasources.config.mongodb', {optional: true})
    dsConfig: object = config,
  ) {
    super(updateConfig(dsConfig));
  }
}
