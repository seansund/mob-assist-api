package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.repository.SignupOptionRepository;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBSignupOption;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository("SignupOptionRepository")
@Profile("mongodb")
public class SignupOptionRepositoryMongoDB extends AbstractRepositoryMongoDB<SignupOption, MongoDBSignupOption> implements SignupOptionRepository {
    protected SignupOptionRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBSignupOption.class);
    }

    @Override
    protected <A extends SignupOption> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends SignupOption> MongoDBSignupOption updateDomainWithCargo(@NonNull MongoDBSignupOption domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "sortIndex");
    }

    @Override
    protected MongoDBSignupOption createDomainObject(@NonNull SignupOption cargo) {
        return MongoDBSignupOption.createSignupOption(cargo);
    }

    @Override
    public List<? extends SignupOption> findAllById(List<String> optionIds) {
        return mongoTemplate().find(
                query(where("id").in(optionIds)),
                MongoDBSignupOption.class
        );
    }
}
