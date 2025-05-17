import {inject} from '@loopback/core';
import {DefaultCrudRepository} from '@loopback/repository';
import {MongodbDataSource} from '../datasources';
import {Signup, SignupRelations} from '../models';

export class SignupRepository extends DefaultCrudRepository<
  Signup,
  typeof Signup.prototype.id,
  SignupRelations
> {
  constructor(
    @inject('datasources.mongodb') dataSource: MongodbDataSource,
  ) {
    super(Signup, dataSource);
  }
}
