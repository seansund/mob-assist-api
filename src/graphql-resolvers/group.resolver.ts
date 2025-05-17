import {
  arg,
  fieldResolver, ID,
  mutation,
  query,
  resolver, ResolverInterface,
  root,
} from '@loopback/graphql';
import {repository} from '@loopback/repository';

import {GroupModel} from '../datatypes';
import {
  Group,
  GroupMember,
  GroupMemberWithRelations,
  GroupWithRelations,
  Member,
} from '../models';
import {
  GroupMemberRepository,
  GroupRepository,
  MemberRepository,
} from '../repositories';


@resolver(() => Group)
export class GroupResolver implements ResolverInterface<Group> {
  constructor(
    @repository('GroupRepository') protected repo: GroupRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
  ) {}

  @query(() => [Group])
  async listGroups(): Promise<Group[]> {
    // TODO filter
    return this.repo.find();
  }

  @mutation(() => Group)
  async createGroup(
    @arg('name') name: string,
  ): Promise<Group> {
    const groups: GroupWithRelations[] = await this.repo.find({where: {name}});

    if (groups.length > 0) {
      throw new Error('Group already exists: ' + name);
    }

    return this.repo.create({name});
  }

  @mutation(() => Group)
  async deleteGroup(
    @arg('groupId', () => ID) groupId: string,
  ): Promise<GroupModel> {
    const group: Group | null = await this.repo.findOne({where: {id: groupId}});

    if (!group) {
      throw new Error('Group not found: ' + groupId);
    }

    await this.groupMemberRepo.deleteAll({groupId});
    await this.repo.deleteById(groupId);

    return {
      id: group.getId(),
      name: group.name,
    };
  }

  @fieldResolver(() => [Member])
  async members(@root() group: Group): Promise<Member[]> {

    const groupMembers: GroupMemberWithRelations[] = await this.groupMemberRepo.find({where: {groupId: group.getId()}})

    const memberIds = groupMembers.map((groupMember: GroupMember) => groupMember.memberId);

    return this.memberRepo.find({where: {id: {inq: memberIds}}});
  }
}
