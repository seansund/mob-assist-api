package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBSignupOptionSet;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("SignupOptionSetRepository")
@Profile("db-mongodb")
public class SignupOptionSetRepositoryMongoDB extends AbstractRepositoryMongoDB<SignupOptionSet, MongoDBSignupOptionSet> implements SignupOptionSetRepository {
    protected SignupOptionSetRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBSignupOptionSet.class);
    }

    @Override
    protected <A extends SignupOptionSet> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends SignupOptionSet> MongoDBSignupOptionSet updateDomainWithCargo(@NonNull MongoDBSignupOptionSet domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    @Override
    protected MongoDBSignupOptionSet createDomainObject(@NonNull SignupOptionSet cargo) {
        return MongoDBSignupOptionSet.createSignupOptionSet(cargo);
    }
}
