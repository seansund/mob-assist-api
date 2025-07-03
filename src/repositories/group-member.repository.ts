import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {GroupMember, GroupMemberRelations} from '../models';

export class GroupMemberRepository extends DefaultCrudRepository<
  GroupMember,
  typeof GroupMember.prototype.id,
  GroupMemberRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(GroupMember, dataSource);
  }
}
