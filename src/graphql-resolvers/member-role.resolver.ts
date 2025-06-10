import {
  MemberMemberRoleRepository,
  MemberRoleRepository,
} from '../repositories';
import {repository} from '@loopback/repository';
import {MemberRoleModel} from '../datatypes';
import {MemberRole} from '../models';
import {arg, ID, mutation, query, resolver} from '@loopback/graphql';
import {entitiesToModels, entityToModel} from '../util';


@resolver(() => MemberRole)
export class MemberRoleResolver {

  constructor(
    @repository('MemberRoleRepository') protected repo: MemberRoleRepository,
    @repository('MemberMemberRoleRepository') protected memberMemberRoleRepo: MemberMemberRoleRepository,
  ) {}

  @query(() => [MemberRole])
  async listMemberRoles(): Promise<MemberRoleModel[]> {
    return this.repo.find().then(entitiesToModels);
  }

  @mutation(() => MemberRole)
  async addMemberRole(
    @arg('name') name: string,
  ): Promise<MemberRoleModel> {

    const memberRoles = await this.repo.find({where: {name}});

    if (memberRoles.length > 0) {
      throw new Error('MemberRole already exists: ' + name);
    }

    return this.repo.create({name}).then(entityToModel);
  }

  @mutation(() => MemberRole)
  async deleteMemberRole(
    @arg('memberRoleId', () => ID) memberRoleId: string,
  ): Promise<MemberRoleModel> {

    const memberRole: MemberRole | null = await this.repo.findOne({where: {id: memberRoleId}});
    if (!memberRole) {
      throw new Error('MemberRole not found: ' + memberRoleId);
    }

    await this.memberMemberRoleRepo.deleteAll({memberRoleId});
    await this.repo.deleteById(memberRoleId);

    return entityToModel(memberRole);
  }
}
