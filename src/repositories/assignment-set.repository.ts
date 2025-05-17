import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {AssignmentSet, AssignmentSetRelations} from '../models';

export class AssignmentSetRepository extends DefaultCrudRepository<
  AssignmentSet,
  typeof AssignmentSet.prototype.id,
  AssignmentSetRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(AssignmentSet, dataSource);
  }
}
