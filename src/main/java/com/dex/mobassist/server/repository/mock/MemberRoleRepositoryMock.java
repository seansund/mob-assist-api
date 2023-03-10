package com.dex.mobassist.server.repository.mock;

import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.repository.MemberRoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("MemberRoleRepository")
@Profile("mock")
public class MemberRoleRepositoryMock extends AbstractRepositoryMock<MemberRole> implements MemberRoleRepository {
    @Override
    protected <A extends MemberRole> A updateValueWithId(A value, int id) {
        return value.withId(id);
    }

    @Override
    protected <A extends MemberRole> String getId(A value) {
        return value.getId();
    }
}
