import {MemberIdentifier, MemberModel} from '../../datatypes';
import {BaseApi} from '../base.api';
import {Group, Member, MemberRole} from '../../models';

export const MEMBER_API = 'services.MemberApi';

// eslint-disable-next-line
export abstract class MemberApi extends BaseApi<MemberModel, Omit<MemberModel, 'id'>, any, MemberIdentifier> {

  abstract createAll(members: Omit<MemberModel, 'id'>[]): Promise<MemberModel[]>;
  abstract addUpdate(member: Omit<MemberModel, 'id'>): Promise<MemberModel>;
  abstract listForSignup(signupId: string): Promise<MemberModel[]>;
  abstract addRole(memberId: string, roleId: string): Promise<MemberModel>;
  abstract removeRole(memberId: string, roleId: string): Promise<MemberModel>;
  abstract addToGroup(memberId: string, groupId: string): Promise<MemberModel>;
  abstract addAllToGroup(memberIds: string[], groupId: string): Promise<MemberModel[]>;
  abstract removeFromGroup(memberId: string, groupId: string): Promise<MemberModel>;
  abstract removeAllFromGroup(memberIds: string[], groupId: string): Promise<MemberModel[]>;

  abstract getRoles(member: Member): Promise<MemberRole[]>;
  abstract getGroups(member: Member): Promise<Group[]>;
}
