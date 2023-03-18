package com.dex.mobassist.server.model;

import java.util.Objects;

public interface SignupOption extends SignupOptionRef {
    String getValue();

    String getShortName();

    Boolean getDeclineOption();

    Integer getSortIndex();

    void setValue(String value);

    default <T extends SignupOption> T withValue(String value) {
        if (Objects.nonNull(value)) {
            setValue(value);
        }

        return (T) this;
    }

    void setShortName(String shortName);

    default <T extends SignupOption> T withShortName(String shortName) {
        if (Objects.nonNull(shortName)) {
            setShortName(shortName);
        }

        return (T) this;
    }

    void setDeclineOption(Boolean declineOption);

    default <T extends SignupOption> T withDeclineOption(Boolean declineOption) {
        if (Objects.nonNull(declineOption)) {
            setDeclineOption(declineOption);
        }

        return (T) this;
    }

    void setSortIndex(Integer sortIndex);

    default <T extends SignupOption> T withSortIndex(Integer sortIndex) {
        if (Objects.nonNull(sortIndex)) {
            setSortIndex(sortIndex);
        }

        return (T) this;
    }
}
