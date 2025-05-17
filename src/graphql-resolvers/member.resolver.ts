import {repository} from '@loopback/repository';
import {
  arg,
  fieldResolver, ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';

import {
  GroupMemberRepository,
  GroupRepository,
  MemberMemberRoleRepository,
  MemberRepository,
  MemberRoleRepository,
} from '../repositories';
import {
  GroupMemberModel,
  GroupModel, isMemberEmail, isMemberId, isMemberPhone, MemberIdentifier,
  MemberMemberRoleDataModel,
  MemberModel,
} from '../datatypes';
import {
  Group,
  GroupMember,
  Member, MemberIdentityInput,
  MemberInput,
  MemberMemberRole,
  MemberRole,
  MemberRoleInput,
  MemberUpdateInput, MemberWithRelations,
} from '../models';

@resolver(() => Member)
export class MemberResolver implements ResolverInterface<Member> {
  constructor(
    @repository('MemberRepository') protected repo: MemberRepository,
    @repository('MemberRoleRepository') protected memberRoleRepo: MemberRoleRepository,
    @repository('MemberMemberRoleRepository') protected memberMemberRoleRepo: MemberMemberRoleRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
  ) {}

  @query(() => [Member])
  async listMembers(): Promise<MemberModel[]> {
    // TODO filter? (by group name, last name, first name, email, phone)
    return this.repo.find();
  }

  @query(() => Member, {nullable: true})
  async getMember(
    @arg('memberId', () => MemberIdentityInput) where: MemberIdentifier
  ): Promise<MemberModel | null> {

    if (!isMemberId(where) && !isMemberPhone(where) && !isMemberEmail(where)) {
      throw new Error('One of member id, email or phone must be provided');
    }

    return this.repo.findOne({where});
  }

  @mutation(() => Member)
  async deleteMember(
    @arg('memberId', () => MemberIdentityInput) where: MemberIdentifier
  ): Promise<MemberModel | null> {

    if (!isMemberId(where) && !isMemberPhone(where) && !isMemberEmail(where)) {
      throw new Error('One of member id, email or phone must be provided');
    }

    const member = await this.repo.findOne({where});

    if (!member) {
      throw new Error('Member not found: ' + where);
    }

    await this.repo.deleteById(member.getId());

    return member;
  }

  @mutation(() => Member)
  async createMember(
    @arg('member') data: MemberInput,
  ): Promise<MemberModel> {
    const member = await this.repo.create({...data});

    return {
      ...data,
      id: member.getId(),
    }
  }

  @mutation(() => Member)
  async addUpdateMember(
    @arg('member') data: MemberInput,
  ): Promise<MemberModel> {

    const members: Member[] = await this.repo.find({
      where: {
        or: [{email: data.email}, {phone: data.phone}],
      }
    });

    if (members.length > 1) {
      throw new Error('Multiple members found with same email or phone: ' + data.email + ' ' + data.phone);
    } else if (members.length === 0) {
      return this.repo.create({...data});
    }

    const updatedMember: Member = Object.assign(members[0], data);

    await this.repo.updateById(updatedMember.getId(), updatedMember);

    return updatedMember;
  }

  @mutation(() => [Member])
  async createMembers(
    @arg('members', () => [MemberInput]) data: MemberInput[],
  ): Promise<MemberModel[]> {
    return this.repo.createAll(data);
  }

  @mutation(() => Member)
  async updateMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('member') data: MemberUpdateInput,
  ): Promise<MemberModel> {
    const member: MemberModel | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const updatedMember: MemberModel = Object.assign(member, data);

    await this.repo.updateById(memberId, updatedMember);

    return updatedMember;
  }

  @mutation(() => Member)
  async addRoleToMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('memberRoleId', () => ID) memberRoleId: string,
  ): Promise<MemberModel> {
    const member: Member | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const memberRole: MemberRole | null = await this.memberRoleRepo.findOne({where: {id: memberRoleId}});
    if (!memberRole) {
      throw new Error('MemberRole not found: ' + memberRoleId);
    }

    const memberMemberRole: MemberMemberRole | null = await this.memberMemberRoleRepo.findOne({where: {memberId, memberRoleId}});
    if (memberMemberRole) {
      throw new Error('Member already has role: ' + memberRoleId);
    }

    await this.memberMemberRoleRepo.create({memberId, memberRoleId});

    return member;
  }

  @mutation(() => Member)
  async removeRoleFromMember(
    @arg('memberId', () => ID) memberId: string,
    @arg('memberRoleId', () => ID) memberRoleId: string,
  ): Promise<MemberModel> {
    const member: Member | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const memberMemberRole: MemberMemberRole | null = await this.memberMemberRoleRepo.findOne({where: {memberId, memberRoleId}});
    if (memberMemberRole) {
      await this.memberMemberRoleRepo.delete(memberMemberRole.getId());
    }

    return member;
  }

  @mutation(() => Member)
  async addMemberToGroup(
    @arg('memberId', () => ID) memberId: string,
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel> {

    const member: MemberModel | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const group: GroupModel | null = await this.groupRepo.findOne({where: {id: groupId}});
    if (!group) {
      throw new Error('Group not found: ' + groupId);
    }

    const groupMember: GroupMemberModel | null = await this.groupMemberRepo.findOne({where: {groupId, memberId}});
    if (groupMember) {
      throw new Error('Member already in group: ' + memberId);
    }

    await this.groupMemberRepo.create({groupId, memberId});

    return member;
  }

  @mutation(() => [Member])
  async addMembersToGroup(
    @arg('memberIds', () => [ID]) memberIds: string[],
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel[]> {

    const members: MemberModel[] = await this.repo.find({where: {id: {inq: memberIds}}});
    if (members.length === 0) {
      throw new Error('Members not found: ' + memberIds);
    }

    const group: GroupModel | null = await this.groupRepo.findOne({where: {id: groupId}});
    if (!group) {
      throw new Error('Group not found: ' + groupId);
    }

    const existingGroupMembers: GroupMemberModel[] = await this.groupMemberRepo.find({where: {groupId, memberId: {inq: memberIds}}});
    const existingMemberIds: string[] = existingGroupMembers
      .map(g => g.member?.id)
      .filter(id => !!id) as string[]

    const missingMemberIds: string[] = memberIds.filter(id => !existingMemberIds.includes(id));
    if (missingMemberIds.length === 0) {
      return members;
    }

    const groupMembers: {groupId: string, memberId: string}[] = missingMemberIds
      .map(memberId => ({groupId, memberId}));

    await this.groupMemberRepo.createAll(groupMembers);

    return members;
  }

  @mutation(() => Member)
  async removeMemberFromGroup(
    @arg('memberId', () => ID) memberId: string,
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel> {

    const member: MemberModel | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const groupMember: GroupMember | null = await this.groupMemberRepo.findOne({where: {groupId, memberId}});
    if (groupMember) {
      await this.groupMemberRepo.delete(groupMember.getId());
    }

    return member;
  }

  @mutation(() => [Member])
  async removeMembersFromGroup(
    @arg('memberIds', () => [ID]) memberIds: string[],
    @arg('groupId', () => ID) groupId: string,
  ): Promise<MemberModel[]> {
    const members: MemberModel[] = await this.repo.find({where: {id: {inq: memberIds}}});

    await this.groupMemberRepo.deleteAll({groupId, memberId: {inq: memberIds}});

    return members;
  }


  @fieldResolver(() => [MemberRole])
  async roles(@root() member: Member): Promise<MemberRole[]> {
    const memberMemberRoles: MemberMemberRole[] = await this.memberMemberRoleRepo.find({where: {memberId: member.getId()}});

    const memberRoleIds = memberMemberRoles.map((memberMemberRole: MemberMemberRole) => memberMemberRole.memberRoleId);

    return this.memberRoleRepo.find({where: {id: {inq: memberRoleIds}}});
  }

  @fieldResolver(() => [Group])
  async groups(@root() member: Member): Promise<Group[]> {
    const groupMember: GroupMember[] = await this.groupMemberRepo.find({where: {memberId: member.getId()}});

    const groupIds = groupMember.map((m: GroupMember) => m.groupId);

    return this.groupRepo.find({where: {id: {inq: groupIds}}});
  }
}
