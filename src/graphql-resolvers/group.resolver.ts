import {
  arg,
  fieldResolver, ID,
  mutation,
  query,
  resolver, ResolverInterface,
  root,
} from '@loopback/graphql';
import {Count, repository} from '@loopback/repository';

import {GroupModel, GroupSummaryModel, MemberModel} from '../datatypes';
import {
  Group, GroupInput,
  GroupMember,
  GroupMemberWithRelations, GroupSummary,
  GroupWithRelations,
  Member,
} from '../models';
import {
  GroupMemberRepository,
  GroupRepository,
  MemberRepository,
} from '../repositories';
import {entitiesToModels, entityToModel} from '../util';


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

  @query(() => Group)
  async getGroup(
    @arg('groupId', () => ID) id: string,
  ): Promise<GroupModel> {
    return this.repo.findById(id).then(entityToModel);
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

  @mutation(() => Group, {nullable: true})
  async updateGroup(
    @arg('groupId', () => ID) groupId: string,
    @arg('data') data: GroupInput
  ): Promise<GroupModel | undefined> {
    return this.repo
      .updateById(groupId, data)
      .then(entityToModel);
  }

  @mutation(() => Group, {nullable: true})
  async addMemberToGroup(
    @arg('groupId', () => ID) groupId: string,
    @arg('memberId', () => ID) memberId: string,
  ): Promise<GroupModel | undefined> {
    const group = await this.repo.findById(groupId);

    if (!group) {
      return undefined;
    }

    const groupMember = await this.groupMemberRepo.findOne({where: {groupId, memberId}});

    if (!groupMember) {
      await this.groupMemberRepo.create({groupId, memberId});
    } else {
      console.log('Already member of group: ', {groupId, memberId});
    }

    return entityToModel(group);
  }

  @mutation(() => Group, {nullable: true})
  async addMembersToGroup(
    @arg('groupId', () => ID) groupId: string,
    @arg('memberIds', () => [ID]) memberIds: string[],
  ): Promise<GroupModel | undefined> {
    const group = await this.repo.findById(groupId);

    if (!group) {
      return undefined;
    }

    const groupMembers = await this.groupMemberRepo
      .find({where: {groupId, memberId: {inq: memberIds}}});

    const existingMemberIds: string[] = groupMembers.map(m => m.memberId.toString())

    const newMemberIds: string[] = memberIds.filter(id => !existingMemberIds.includes(id))

    if (newMemberIds.length > 0) {
      await this.groupMemberRepo.createAll(newMemberIds.map(memberId => ({groupId, memberId})));
    } else {
      console.log('Already member(s) of group: ', {groupId, memberIds});
    }

    return entityToModel(group);
  }

  @mutation(() => Group, {nullable: true})
  async removeMemberFromGroup(
    @arg('groupId', () => ID) groupId: string,
    @arg('memberId', () => ID) memberId: string,
  ): Promise<GroupModel | undefined> {
    const group = await this.repo.findById(groupId);

    if (!group) {
      return undefined;
    }

    const groupMember = await this.groupMemberRepo.findOne({where: {groupId, memberId}});

    if (groupMember) {
      await this.groupMemberRepo.deleteById(groupMember.id);
    } else {
      console.log('No currently member of group: ', {groupId, memberId});
    }

    return entityToModel(group);
  }

  @fieldResolver(() => [Member])
  async members(@root() group: Group): Promise<MemberModel[]> {

    const groupMembers: GroupMemberWithRelations[] = await this.groupMemberRepo.find({where: {groupId: group.getId()}})

    const memberIds = groupMembers.map((groupMember: GroupMember) => groupMember.memberId);

    return this.memberRepo.find({where: {id: {inq: memberIds}}, order: ['lastName ASC', 'firstName ASC']}).then(entitiesToModels);
  }

  @fieldResolver(() => [GroupSummary])
  async summary(@root() group: Group): Promise<GroupSummaryModel> {
    const result: Count = await this.groupMemberRepo.count({groupId: group.getId()});

    return {
      memberCount: result.count,
    };
  }
}
