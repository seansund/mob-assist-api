package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.NotificationChannelResult;
import com.dex.mobassist.server.model.NotificationResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationResultCargo implements NotificationResult {
    private String type;
    private List<? extends NotificationChannelResult> channels = new ArrayList<>();

    public NotificationResultCargo() {
    }
    public NotificationResultCargo(String type) {
        this.type = type;
    }

    @Override
    public <T extends NotificationResult> T addChannel(String channel, int count) {
        return addChannel(new NotificationChannelResultCargo(channel).withCount(count));
    }
}
