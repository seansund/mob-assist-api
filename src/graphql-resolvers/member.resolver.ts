import {inject} from '@loopback/core';
import {
  arg,
  fieldResolver,
  ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';
import {
  GroupModel,
  MemberIdentifier,
  MemberModel,
  MemberRoleModel,
} from '../datatypes';
import {
  Group,
  Member,
  MemberIdentityInput,
  MemberInput,
  MemberRole,
  MemberUpdateInput,
} from '../models';
import {MEMBER_API, MemberApi} from '../services/member';
import {entitiesToModels} from '../util';

@resolver(() => Member)
export class MemberResolver implements ResolverInterface<Member> {
  constructor(
    @inject(MEMBER_API) protected service: MemberApi,
  ) {}

  @query(() => [Member])
  async listMembers(): Promise<MemberModel[]> {
    // TODO filter? (by group name, last name, first name, email, phone)
    return this.service.list();
  }

  @query(() => Member, {nullable: true})
  async getMember(
    @arg('memberId', () => MemberIdentityInput) where: MemberIdentifier
  ): Promise<MemberModel | undefined> {
    return this.service.get(where);
  }

  @mutation(() => Member)
  async deleteMember(
    @arg('memberId', () => MemberIdentityInput) where: MemberIdentifier
  ): Promise<MemberModel | undefined> {
    return this.service.delete(where);
  }

  @mutation(() => Member)
  async createMember(
    @arg('member') data: MemberInput,
  ): Promise<MemberModel> {
    return this.service.create(data);
  }

  @mutation(() => Member)
  async addUpdateMember(
    @arg('member') data: MemberInput,
  ): Promise<MemberModel> {
    return this.service.addUpdate(data);
  }

  @mutation(() => [Member])
  async createMembers(
    @arg('members', () => [MemberInput]) data: MemberInput[],
  ): Promise<MemberModel[]> {
    return this.service.createAll(data);
  }

  @mutation(() => Member)
  async updateMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('member') data: MemberUpdateInput,
  ): Promise<MemberModel> {
    return this.service.update({id: memberId}, data);
  }

  @mutation(() => Member)
  async addRoleToMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('memberRoleId', () => ID) memberRoleId: string,
  ): Promise<MemberModel> {
    return this.service.addRole(memberId, memberRoleId);
  }

  @mutation(() => Member)
  async removeRoleFromMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('memberRoleId', () => ID) memberRoleId: string,
  ): Promise<MemberModel> {
    return this.service.removeRole(memberId, memberRoleId);
  }

  @mutation(() => Member)
  async addMemberToGroup(
    @arg('memberId', () => ID) memberId: string,
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel> {
    return this.service.addToGroup(memberId, groupId);
  }

  @mutation(() => [Member])
  async addMembersToGroup(
    @arg('memberIds', () => [ID]) memberIds: string[],
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel[]> {
    return this.service.addAllToGroup(memberIds, groupId);
  }

  @mutation(() => Member)
  async removeMemberFromGroup(
    @arg('memberId', () => ID) memberId: string,
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel> {
    return this.service.removeFromGroup(memberId, groupId);
  }

  @mutation(() => [Member])
  async removeMembersFromGroup(
    @arg('memberIds', () => [ID]) memberIds: string[],
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel[]> {
    return this.service.removeAllFromGroup(memberIds, groupId);
  }


  @fieldResolver(() => [MemberRole])
  async roles(@root() member: Member): Promise<MemberRoleModel[]> {
    return this.service.getRoles(member).then(entitiesToModels);
  }

  @fieldResolver(() => [Group])
  async groups(@root() member: Member): Promise<GroupModel[]> {
    return this.service.getGroups(member).then(entitiesToModels);
  }
}
