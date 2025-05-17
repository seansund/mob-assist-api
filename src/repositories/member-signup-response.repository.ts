import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {MemberSignupResponse, MemberSignupResponseRelations} from '../models';

export class MemberSignupResponseRepository extends DefaultCrudRepository<
  MemberSignupResponse,
  typeof MemberSignupResponse.prototype.id,
  MemberSignupResponseRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(MemberSignupResponse, dataSource);
  }
}
