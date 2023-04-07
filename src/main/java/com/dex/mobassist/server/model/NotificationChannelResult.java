package com.dex.mobassist.server.model;

public interface NotificationChannelResult {
    String getChannel();
    void setChannel(String channel);
    default <T extends NotificationChannelResult> T withChannel(String channel) {
        setChannel(channel);

        return (T) this;
    }

    Integer getCount();
    void setCount(Integer count);
    default <T extends NotificationChannelResult> T withCount(Integer count) {
        setCount(count);

        return (T) this;
    }
}
