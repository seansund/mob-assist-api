package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.repository.BaseRepository;
import com.mongodb.client.result.DeleteResult;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepositoryMongoDB<T, D extends T> implements BaseRepository<T> {
    private final BehaviorSubject<List<? extends T>> subject;
    private final MongoTemplate mongoTemplate;
    private final Class<? extends T> entityClass;

    protected AbstractRepositoryMongoDB(MongoTemplate mongoTemplate, Class<? extends T> entityClass) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;

        subject = BehaviorSubject.createDefault(List.of());
    }

    protected MongoTemplate mongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public List<? extends T> findAll() {
        System.out.println("Finding all from mongodb: " + entityClass.getSimpleName());

        return mongoTemplate.find(
                new Query().with(defaultSort()),
                entityClass
        );
    }

    @Override
    public <A extends T> Optional<? extends A> findById(String id) {
        return Optional.ofNullable((A) mongoTemplate.findById(id, entityClass));
    }

    protected abstract <A extends T> String getId(@NonNull A cargo);

    @Override
    public <A extends T> A save(@NonNull A cargo) {
        final String id = getId(cargo);

        final Optional<D> optionalDomain = id != null
                ? findById(id).map(val -> (D)val)
                : Optional.empty();

        final D domain = updateDomainWithCargo(
                optionalDomain.isEmpty()
                        ? createDomainObject(cargo)
                        : optionalDomain.get(),
                cargo
        );

        System.out.println("Populated domain: " + domain);

        final D result = mongoTemplate.save(domain);

        onChange();

        return (A) result;
    }

    protected abstract <A extends T> D createDomainObject(@NonNull A cargo);

    protected abstract <A extends T> D updateDomainWithCargo(@NonNull D domain, @NonNull A cargo);

    @Override
    public boolean deleteById(@NonNull String id) {
        final DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("id").is(id)), entityClass);

        final boolean deleted = result.getDeletedCount() > 0;

        if (deleted) {
            onChange();
        }

        return deleted;
    }

    @Override
    public Observable<List<? extends T>> observable() {
        return subject;
    }

    protected abstract Sort defaultSort();

    protected void onNext(List<? extends T> value) {
        subject.onNext(value);
    }

    protected void onChange() {
        onNext(findAll());
    }
}
