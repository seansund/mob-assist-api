import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {Option, OptionRelations} from '../models';

export class OptionRepository extends DefaultCrudRepository<
  Option,
  typeof Option.prototype.id,
  OptionRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(Option, dataSource);
  }
}
