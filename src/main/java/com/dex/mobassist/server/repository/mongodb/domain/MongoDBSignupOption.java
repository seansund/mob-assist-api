package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.SignupOption;
import com.dex.mobassist.server.model.SignupOptionRef;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("signupOption")
public class MongoDBSignupOption extends MongoDBSignupOptionRef implements SignupOption {
    @Indexed(unique = true)
    @NonNull private String value = "";
    @NonNull private String shortName = "";
    @NonNull private Boolean declineOption = false;
    @Indexed
    @NonNull private Integer sortIndex = 0;

    public MongoDBSignupOption() {
        this(null);
    }

    public MongoDBSignupOption(String id) {
        super(id);
    }

    public static MongoDBSignupOption createSignupOption(SignupOptionRef optionRef) {
        if (optionRef == null) {
            return null;
        }

        if (optionRef instanceof MongoDBSignupOption) {
            return (MongoDBSignupOption) optionRef;
        }

        final MongoDBSignupOption result = new MongoDBSignupOption(optionRef.getId());

        if (optionRef instanceof final SignupOption option) {
            result.updateWith(option);
        }

        return result;
    }

    public static List<? extends MongoDBSignupOption> createSignupOptions(List<? extends SignupOptionRef> refs) {
        if (refs == null) {
            return null;
        }

        return refs.stream().map(MongoDBSignupOption::createSignupOption).filter(Objects::nonNull).toList();
    }

    public MongoDBSignupOption updateWith(SignupOption signupOption) {
        return this
                .withValue(signupOption.getValue())
                .withDeclineOption(signupOption.getDeclineOption())
                .withSortIndex(signupOption.getSortIndex())
                .withShortName(signupOption.getShortName());
    }

    @Override
    public String getShortName() {
        if (!Strings.isNullOrEmpty(shortName)) {
            return shortName;
        }

        return value.replace(":", "").replace("am", "");
    }
}
