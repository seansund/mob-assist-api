package com.dex.mobassist.server.model;

import java.util.List;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public interface NotificationResult {
    String getType();
    void setType(String type);
    default <T extends NotificationResult> T withType(String type) {
        setType(type);

        return (T) this;
    }

    List<? extends NotificationChannelResult> getChannels();
    void setChannels(List<? extends NotificationChannelResult> channels);
    default <T extends NotificationResult> T withChannels(List<? extends NotificationChannelResult> channels) {
        setChannels(channels);

        return (T) this;
    }

    <T extends NotificationResult> T addChannel(String channel, int count);

    default <T extends NotificationResult> T addChannel(NotificationChannelResult channel) {
        if (channel == null) {
            return (T) this;
        }

        final List<NotificationChannelResult> newList = concat(getChannels().stream(), of(channel)).toList();

        setChannels(newList);

        return (T) this;
    }

    default <T extends NotificationResult> T addChannels(List<? extends NotificationChannelResult> channels) {
        final List<NotificationChannelResult> newList = concat(getChannels().stream(), channels.stream()).toList();

        setChannels(newList);

        return (T) this;
    }

    default <T extends NotificationResult> T withResult(NotificationResult result) {
        return addChannels(result.getChannels());
    }
}
