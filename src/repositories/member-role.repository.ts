import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {MemberRole, MemberRoleRelations} from '../models';

export class MemberRoleRepository extends DefaultCrudRepository<
  MemberRole,
  typeof MemberRole.prototype.id,
  MemberRoleRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(MemberRole, dataSource);
  }
}
