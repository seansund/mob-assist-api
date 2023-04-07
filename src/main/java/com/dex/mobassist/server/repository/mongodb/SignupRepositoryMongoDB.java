package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.repository.mongodb.domain.MongoDBSignup;
import com.dex.mobassist.server.model.Signup;
import com.dex.mobassist.server.repository.SignupRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.dex.mobassist.server.util.Dates.yesterday;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository("SignupRepository")
@Profile("db-mongodb")
public class SignupRepositoryMongoDB extends AbstractRepositoryMongoDB<Signup, MongoDBSignup> implements SignupRepository {

    public static final String dateField = "date";

    protected SignupRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBSignup.class);
    }

    @Override
    public List<? extends Signup> findAll() {

        return mongoTemplate().find(
                new Query().with(defaultSort()),
                MongoDBSignup.class
        );
    }

    @Override
    protected <A extends Signup> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends Signup> MongoDBSignup updateDomainWithCargo(@NonNull MongoDBSignup domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, dateField);
    }

    @Override
    public List<? extends Signup> findSignupsAfter(Date target) {

        return mongoTemplate().find(
                query(where(dateField).gte(target)).with(defaultSort()),
                MongoDBSignup.class
        );
    }

    @Override
    public List<? extends Signup> findSignupsBetween(Date start, Date end) {

        return mongoTemplate().find(
                query(where(dateField).gte(start).lt(end)).with(defaultSort()),
                MongoDBSignup.class
        );
    }

    @Override
    public Optional<? extends Signup> getCurrent() {

        return Optional.ofNullable(mongoTemplate().findOne(
                query(where(dateField).gte(yesterday())).with(defaultSort()),
                MongoDBSignup.class)
        );
    }

    @Override
    protected MongoDBSignup createDomainObject(@NonNull Signup cargo) {
        return MongoDBSignup.createSignup(cargo);
    }
}
