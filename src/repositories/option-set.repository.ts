import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {OptionSet, OptionSetRelations} from '../models';

export class OptionSetRepository extends DefaultCrudRepository<
  OptionSet,
  typeof OptionSet.prototype.id,
  OptionSetRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(OptionSet, dataSource);
  }
}
