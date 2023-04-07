package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.NotificationChannelResult;
import lombok.Data;

@Data
public class NotificationChannelResultCargo implements NotificationChannelResult {
    private String channel;
    private Integer count;

    public NotificationChannelResultCargo() {
    }
    public NotificationChannelResultCargo(String channel) {
        this(channel, 0);
    }
    public NotificationChannelResultCargo(String channel, Integer count) {
        this.channel = channel;
        this.count = count;
    }
}
