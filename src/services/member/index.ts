import {Context} from '@loopback/context';
import {MEMBER_API} from './member.api';
import {MemberImpl} from './member.impl';

export * from './member.api';

export const bindMemberApi = (service: Context) => {
  service.bind(MEMBER_API).toClass(MemberImpl);
}
