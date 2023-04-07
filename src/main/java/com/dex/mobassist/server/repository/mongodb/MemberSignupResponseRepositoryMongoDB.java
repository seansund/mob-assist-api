package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.*;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBMemberSignupResponse;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBResponseCount;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository("MemberSignupResponseRepository")
@Profile("db-mongodb")
public class MemberSignupResponseRepositoryMongoDB extends AbstractRepositoryMongoDB<MemberSignupResponse, MongoDBMemberSignupResponse> implements MemberSignupResponseRepository {

    private final SignupRepository signupRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;
    private final SignupOptionRepository signupOptionRepository;

    protected MemberSignupResponseRepositoryMongoDB(
            MongoTemplate mongoTemplate,
            SignupRepository signupRepository,
            SignupOptionSetRepository signupOptionSetRepository,
            SignupOptionRepository signupOptionRepository
    ) {
        super(mongoTemplate, MongoDBMemberSignupResponse.class);

        this.signupRepository = signupRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
        this.signupOptionRepository = signupOptionRepository;
    }

    @Override
    public List<? extends MemberSignupResponse> listByUser(String phone) {
        return mongoTemplate().find(
                query(where("member.id").is(phone)),
                MongoDBMemberSignupResponse.class
        );
    }

    @Override
    public List<? extends MemberSignupResponse> listBySignup(String id) {

        return mongoTemplate().find(
                query(where("signup.id").is(id)),
                MongoDBMemberSignupResponse.class
        );
    }

    @Override
    public Optional<? extends MemberSignupResponse> findForSignupAndMember(String id, String phone) {
        return Optional.ofNullable(mongoTemplate().findOne(
                query(where("signup.id").is(id).andOperator(where("member.id").is(phone))),
                MongoDBMemberSignupResponse.class)
        );
    }

    @Override
    public MemberSignupResponse signUp(Signup signup, Member member, SignupOption selectedOption) {
        final Optional<? extends MemberSignupResponse> response = findForSignupAndMember(signup.getId(), member.getPhone());

        final MongoDBMemberSignupResponse resp = response
                .orElseGet(() -> new MongoDBMemberSignupResponse().withSignup(signup).withMember(member))
                .withSelectedOption(selectedOption);

        return save(resp);
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfUserResponses(String phone) {

        onNext(listByUser(phone));

        return observable();
    }

    @Override
    public Observable<List<? extends MemberSignupResponse>> observableOfSignupResponses(String id) {

        onNext(listBySignup(id));

        return observable();
    }

    @Override
    protected <A extends MemberSignupResponse> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends MemberSignupResponse> MongoDBMemberSignupResponse updateDomainWithCargo(@NonNull MongoDBMemberSignupResponse domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "id");
    }

    @Override
    protected MongoDBMemberSignupResponse createDomainObject(@NonNull MemberSignupResponse cargo) {
        return MongoDBMemberSignupResponse.createMemberSignupResponse(cargo);
    }

    public List<? extends SignupOptionResponse> getCountsForSignupOptions(String signupId) {
        final Optional<List<? extends SignupOption>> optionalOptions = signupRepository
                .findById(signupId)
                .flatMap(signup -> getOptionSet(signup.getOptions()))
                .map(optionSet -> getOptions(optionSet.getOptions()));

        if (optionalOptions.isEmpty()) {
            return List.of();
        }

        final List<String> optionIds = optionalOptions.get().stream().map(ModelRef::getId).toList();

        // TODO should be able to combine these into one query
        Map<String, Integer> responseCounts = getResponseCounts(signupId, optionIds);
        Map<String, Integer> assignmentCounts = getAssignmentCounts(signupId, optionIds);

        return optionalOptions.get()
                .stream()
                .map(createSignupOptionResponse(responseCounts, assignmentCounts))
                .toList();
    }

    protected Map<String, Integer> getResponseCounts(final String signupId, final List<String> optionIds) {

        final Aggregation aggregate = newAggregation(
                match(where("signup.id")
                        .is(signupId)
                        .andOperator(
                                where("selectedOption.id").in(optionIds)
                        )),
                group("selectedOption.id"),
                group().count().as("count")
        );

        final AggregationResults<MongoDBResponseCount> result = mongoTemplate().aggregate(
                aggregate,
                MongoDBMemberSignupResponse.collectionName,
                MongoDBResponseCount.class
        );

        return result.getMappedResults()
                .stream()
                .collect(toMap(MongoDBResponseCount::getSelectedOptionId, MongoDBResponseCount::getCount));
    }

    protected Map<String, Integer> getAssignmentCounts(final String signupId, final List<String> optionIds) {

        final Aggregation aggregation = newAggregation(
                match(where("signup.id")
                        .is(signupId)
                        .andOperator(
                                where("selectedOption.id").in(optionIds)
                        ).andOperator(where("assignments").exists(true).ne(new ArrayList<>()))),
                group("selectedOption.id"),
                group().count().as("count")
        );

        final AggregationResults<MongoDBResponseCount> result = mongoTemplate().aggregate(
                aggregation,
                MongoDBMemberSignupResponse.collectionName,
                MongoDBResponseCount.class
        );

        return result.getMappedResults()
                .stream()
                .collect(toMap(MongoDBResponseCount::getSelectedOptionId, MongoDBResponseCount::getCount));
    }

    protected Function<? super SignupOption, ? extends SignupOptionResponse> createSignupOptionResponse(final Map<String, Integer> responseCounts, final Map<String, Integer> assignmentCounts) {
        return (SignupOption option) -> new SimpleSignupOptionResponse()
                .withOption(option)
                .withCount(responseCounts.getOrDefault(option.getId(), 0))
                .withAssignments(assignmentCounts.getOrDefault(option.getId(), 0));
    }

    protected Optional<? extends SignupOptionSet> getOptionSet(SignupOptionSetRef ref) {
        if (ref instanceof SignupOptionSet) {
            return Optional.of((SignupOptionSet) ref);
        }

        if (ref == null) {
            return Optional.empty();
        }

        return signupOptionSetRepository.findById(ref.getId());
    }

    protected List<? extends SignupOption> getOptions(List<? extends SignupOptionRef> refs) {
        if (refs == null || refs.isEmpty()) {
            return null;
        }

        if (refs.stream().allMatch(ref -> ref instanceof SignupOption)) {
            return refs.stream().map(ref -> (SignupOption) ref).toList();
        }

        return signupOptionRepository.findAllById(refs.stream().map(ModelRef::getId).toList());
    }
}
