package com.disputetrackingsystem.DTO;

public class DisputeStatusUpdateRequest {
    private String statusName;
    private String subStatusName;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String comments;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSubStatusName() {
        return subStatusName;
    }

    public void setSubStatusName(String subStatusName) {
        this.subStatusName = subStatusName;
    }
}
