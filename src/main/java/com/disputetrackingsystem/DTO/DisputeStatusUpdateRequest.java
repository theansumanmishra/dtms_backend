package com.disputetrackingsystem.DTO;

import java.math.BigDecimal;

public class DisputeStatusUpdateRequest {
    private String statusName;
    private String subStatusName;
    private String comments;
    private BigDecimal refund;
    private Boolean vendorVerified;

    public Boolean getVendorVerified() {
        return vendorVerified;
    }

    public void setVendorVerified(Boolean vendorVerified) {
        this.vendorVerified = vendorVerified;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

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
