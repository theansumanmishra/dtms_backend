package com.disputetrackingsystem.eventlistener;

import com.disputetrackingsystem.model.Dispute;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DisputeClosedEvent extends ApplicationEvent {

    private final Dispute dispute;

    public DisputeClosedEvent(Object source, Dispute dispute) {
        super(source);
        this.dispute = dispute;
    }
}