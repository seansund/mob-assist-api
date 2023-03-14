package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.SignupOptionRef;
import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.model.SignupOptionSetRef;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("signupOptionSet")
public class MongoDBSignupOptionSet extends MongoDBSignupOptionSetRef implements SignupOptionSet {
    @NonNull
    private String name = "";
    @NonNull
    @DBRef
    private List<? extends MongoDBSignupOption> options = List.of();

    public MongoDBSignupOptionSet() {
        this(null);
    }

    public MongoDBSignupOptionSet(String id) {
        super(id);
    }

    public static MongoDBSignupOptionSet createSignupOptionSet(SignupOptionSetRef optionSetRef) {
        if (optionSetRef == null) {
            return null;
        }

        if (optionSetRef instanceof MongoDBSignupOptionSet) {
            return (MongoDBSignupOptionSet) optionSetRef;
        }

        final MongoDBSignupOptionSet result = new MongoDBSignupOptionSet(optionSetRef.getId());

        if (optionSetRef instanceof final SignupOptionSet optionSet) {
            result.updateWith(optionSet);
        }

        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends SignupOptionRef> getOptions() {
        return options;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setOptions(List<? extends SignupOptionRef> options) {
        this.options = options.stream().map(MongoDBSignupOption::createSignupOption).toList();
    }

    public MongoDBSignupOptionSet updateWith(SignupOptionSet optionSet) {
        if (optionSet == null) {
            return this;
        }

        return this
                .withName(optionSet.getName())
                .withOptions(MongoDBSignupOption.createSignupOptions(optionSet.getOptions()));
    }
}
