import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {MemberMemberRole, MemberMemberRoleRelations} from '../models';

export class MemberMemberRoleRepository extends DefaultCrudRepository<
  MemberMemberRole,
  typeof MemberMemberRole.prototype.id,
  MemberMemberRoleRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(MemberMemberRole, dataSource);
  }
}
