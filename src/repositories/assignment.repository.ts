import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {Assignment, AssignmentRelations} from '../models';

export class AssignmentRepository extends DefaultCrudRepository<
  Assignment,
  typeof Assignment.prototype.id,
  AssignmentRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(Assignment, dataSource);
  }
}
