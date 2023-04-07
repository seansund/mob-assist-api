package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBMemberRole;
import com.dex.mobassist.server.repository.MemberRoleRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("MemberRoleRepository")
@Profile("db-mongodb")
public class MemberRoleRepositoryMongoDB extends AbstractRepositoryMongoDB<MemberRole, MongoDBMemberRole> implements MemberRoleRepository {
    protected MemberRoleRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBMemberRole.class);
    }

    @Override
    protected <A extends MemberRole> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends MemberRole> MongoDBMemberRole updateDomainWithCargo(@NonNull MongoDBMemberRole domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    @Override
    protected MongoDBMemberRole createDomainObject(@NonNull MemberRole cargo) {
        return MongoDBMemberRole.createMemberRole(cargo);
    }
}
