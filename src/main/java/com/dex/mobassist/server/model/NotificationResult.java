package com.dex.mobassist.server.model;

public interface NotificationResult {
    String getType();
    void setType(String type);
    default <T extends NotificationResult> T withType(String type) {
        setType(type);

        return (T) this;
    }
    Integer getCount();
    void setCount(Integer count);
    default <T extends NotificationResult> T withCount(Integer count) {
        setCount(count);

        return (T) this;
    }
}
