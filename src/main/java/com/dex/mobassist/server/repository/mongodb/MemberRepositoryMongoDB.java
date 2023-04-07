package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBMember;
import com.dex.mobassist.server.repository.MemberRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository("MemberRepository")
@Profile("db-mongodb")
public class MemberRepositoryMongoDB extends AbstractRepositoryMongoDB<Member, MongoDBMember> implements MemberRepository {
    protected MemberRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBMember.class);
    }

    @Override
    public Optional<? extends Member> findByPhone(String phone) {

        return Optional.ofNullable(mongoTemplate().findOne(
                query(where("phone").is(phone)),
                MongoDBMember.class
        ));
    }

    @Override
    protected <A extends Member> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends Member> MongoDBMember updateDomainWithCargo(@NonNull MongoDBMember domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "lastName", "firstName");
    }

    @Override
    protected MongoDBMember createDomainObject(@NonNull Member cargo) {
        final MongoDBMember result = MongoDBMember.createMember(cargo);

        System.out.println();

        return result;
    }
}
