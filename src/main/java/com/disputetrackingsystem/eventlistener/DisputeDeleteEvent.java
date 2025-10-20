package com.disputetrackingsystem.eventlistener;

import com.disputetrackingsystem.model.Dispute;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DisputeDeleteEvent extends ApplicationEvent {

    private final Dispute dispute;

    public DisputeDeleteEvent(Object source, Dispute dispute) {
        super(source);
        this.dispute = dispute;
    }
}
