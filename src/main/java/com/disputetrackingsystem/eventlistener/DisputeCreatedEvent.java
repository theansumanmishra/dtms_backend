package com.disputetrackingsystem.eventlistener;

import com.disputetrackingsystem.model.Dispute;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DisputeCreatedEvent extends ApplicationEvent {

    private final Dispute dispute;

    public DisputeCreatedEvent(Object source, Dispute dispute) {
        super(source);
        this.dispute = dispute;
    }
}
