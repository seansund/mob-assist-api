import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {MemberSignupResponseAssignment, MemberSignupResponseAssignmentRelations} from '../models';

export class MemberSignupResponseAssignmentRepository extends DefaultCrudRepository<
  MemberSignupResponseAssignment,
  typeof MemberSignupResponseAssignment.prototype.id,
  MemberSignupResponseAssignmentRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(MemberSignupResponseAssignment, dataSource);
  }
}
