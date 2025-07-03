import {repository} from '@loopback/repository';
import {MemberApi} from './member.api';
import {
  GroupMemberModel,
  GroupModel,
  isMemberEmail,
  isMemberId,
  isMemberPhone,
  MemberIdentifier,
  MemberModel,
} from '../../datatypes';
import {
  GroupMemberRepository,
  GroupRepository,
  MemberMemberRoleRepository,
  MemberRepository,
  MemberRoleRepository,
  SignupRepository,
} from '../../repositories';
import {
  GroupMember,
  Member,
  MemberGroup,
  MemberMemberRole,
  MemberRole,
} from '../../models';
import {entitiesToModels, entityToModel} from '../../util';

export class MemberImpl implements MemberApi {

  constructor(
    @repository('MemberRepository') protected repo: MemberRepository,
    @repository('SignupRepository') protected signupRepo: SignupRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('MemberRoleRepository') protected memberRoleRepo: MemberRoleRepository,
    @repository('MemberMemberRoleRepository') protected memberMemberRoleRepo: MemberMemberRoleRepository,
  ) {}

  async create(data: Omit<MemberModel, "id">): Promise<MemberModel> {
    const member = await this.repo.create({...data});

    return {
      ...data,
      id: member.getId(),
    }
  }

  async createAll(data: Omit<MemberModel, "id">[]): Promise<MemberModel[]> {
    return this.repo.createAll(data).then(entityToModel);
  }

  async delete(where: MemberIdentifier): Promise<MemberModel | undefined> {

    if (!isMemberId(where) && !isMemberPhone(where) && !isMemberEmail(where)) {
      throw new Error('One of member id, email or phone must be provided');
    }

    const member = await this.repo
      .findOne({where});

    if (!member) {
      throw new Error('Member not found: ' + where);
    }

    await this.repo.deleteById(member.getId());

    return entityToModel(member);
  }

  async get(where: MemberIdentifier): Promise<MemberModel | undefined> {

    if (!isMemberId(where) && !isMemberPhone(where) && !isMemberEmail(where)) {
      throw new Error('One of member id, email or phone must be provided');
    }

    return this.repo.findOne({where})
      .then(entityToModel);
  }

  async list(): Promise<MemberModel[]> {
    return this.repo
      .find({order: ['lastName ASC', 'firstName ASC']})
      .then(entitiesToModels);
  }

  async addUpdate(data: Omit<MemberModel, 'id'>): Promise<MemberModel> {

    const members: Member[] = await this.repo.find({
      where: {
        or: [{email: data.email}, {phone: data.phone}],
      }
    });

    if (members.length > 1) {
      throw new Error('Multiple members found with same email or phone: ' + data.email + ' ' + data.phone);
    } else if (members.length === 0) {
      return this.repo.create({...data}).then(entityToModel);
    }

    const updatedMember: Member = Object.assign(members[0], data);

    await this.repo.updateById(updatedMember.getId(), updatedMember);

    return entityToModel(updatedMember);
  }

  async update(where: MemberIdentifier, data: Partial<MemberModel>): Promise<MemberModel> {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const memberId = (where as any).id;

    const member: MemberModel | null = await this.repo
      .findOne({where: {id: memberId}})
      .then(entityToModel);

    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const updatedMember: MemberModel = Object.assign(member, data);

    await this.repo.updateById(memberId, updatedMember);

    return updatedMember;
  }

  async listForSignup(signupId: string): Promise<MemberModel[]> {

    // TODO optimize
    const signup = await this.signupRepo.findOne({where: {id: signupId}});

    if (!signup) {
      return [];
    }

    const groupId = signup.groupId;

    const groupMembers = await this.groupMemberRepo.find({where: {groupId}});

    const memberIds = groupMembers.map(groupMember => groupMember.memberId);

    return this.repo
      .find({where: {id: {inq: memberIds}}, order: ['lastName ASC', 'firstName ASC']})
      .then(entitiesToModels);
  }

  async getGroups(member: Member): Promise<MemberGroup[]> {
    const groupMembers: GroupMember[] = await this.groupMemberRepo.find({where: {memberId: member.getId()}});

    const groupIds = groupMembers.map((m: GroupMember) => m.groupId);

    const groups: GroupModel[] = await this.groupRepo
      .find({where: {id: {inq: groupIds}}})
      .then(entitiesToModels);

    return groups
      .map(group => {
        const groupMember = groupMembers
          .find(g => g.groupId.toString() === group.id.toString())

        return {
          ...group,
          roleId: groupMember?.roleId,
        } as MemberGroup;
      })
  }

  async getRoles(member: Member): Promise<MemberRole[]> {
    const memberMemberRoles: MemberMemberRole[] = await this.memberMemberRoleRepo.find({where: {memberId: member.getId()}});

    const memberRoleIds = memberMemberRoles.map((memberMemberRole: MemberMemberRole) => memberMemberRole.memberRoleId);

    return this.memberRoleRepo.find({where: {id: {inq: memberRoleIds}}});
  }

  async addAllToGroup(memberIds: string[], groupId: string, roleId?: string): Promise<MemberModel[]> {

    const members: MemberModel[] = await this.repo
      .find({where: {id: {inq: memberIds}}})
      .then(entitiesToModels);

    if (members.length === 0) {
      throw new Error('Members not found: ' + memberIds);
    }

    const group: GroupModel | null = await this.groupRepo
      .findOne({where: {id: groupId}})
      .then(entityToModel);

    if (!group) {
      throw new Error('Group not found: ' + groupId);
    }

    const existingGroupMembers: GroupMemberModel[] = await this.groupMemberRepo
      .find({where: {groupId, memberId: {inq: memberIds}}})
      .then(entitiesToModels);

    const existingMemberIds: string[] = existingGroupMembers
      .map(g => g.member?.id)
      .filter(id => !!id) as string[]

    const missingMemberIds: string[] = memberIds.filter(id => !existingMemberIds.includes(id));
    if (missingMemberIds.length === 0) {
      return members;
    }

    const groupMembers: {groupId: string, memberId: string}[] = missingMemberIds
      .map(memberId => ({groupId, memberId, roleId}));

    await this.groupMemberRepo.createAll(groupMembers);

    return members;
  }

  async addRole(memberId: string, memberRoleId: string): Promise<MemberModel> {
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

    return entityToModel(member);
  }

  async addToGroup(memberId: string, groupId: string, roleId?: string): Promise<MemberModel> {

    const member: MemberModel = await this.repo
      .findById(memberId)
      .then(entityToModel);

    await this.groupRepo
      .findById(groupId)
      .then(entityToModel);

    const groupMember: GroupMember | null = await this.groupMemberRepo
      .findOne({where: {groupId, memberId}})

    if (groupMember) {
      await this.groupMemberRepo.updateById(groupMember.getId(), {...groupMember, roleId});
    } else {
      await this.groupMemberRepo.create({groupId, memberId, roleId});
    }

    return member;
  }

  async removeFromGroup(memberId: string, groupId: string): Promise<MemberModel> {

    const member: MemberModel | null = await this.repo
      .findOne({where: {id: memberId}})
      .then(entityToModel);

    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const groupMember: GroupMember | null = await this.groupMemberRepo
      .findOne({where: {groupId, memberId}});

    if (groupMember) {
      await this.groupMemberRepo.delete(groupMember.getId());
    }

    return member;
  }

  async removeRole(memberId: string, memberRoleId: string): Promise<MemberModel> {
    const member: Member | null = await this.repo.findOne({where: {id: memberId}});
    if (!member) {
      throw new Error('Member not found: ' + memberId);
    }

    const memberMemberRole: MemberMemberRole | null = await this.memberMemberRoleRepo.findOne({where: {memberId, memberRoleId}});
    if (memberMemberRole) {
      await this.memberMemberRoleRepo.delete(memberMemberRole.getId());
    }

    return entityToModel(member);
  }

  async removeAllFromGroup(memberIds: string[], groupId: string): Promise<MemberModel[]> {
    const members: MemberModel[] = await this.repo
      .find({where: {id: {inq: memberIds}}})
      .then(entitiesToModels);

    await this.groupMemberRepo.deleteAll({groupId, memberId: {inq: memberIds}});

    return members;
  }

}
