package com.thoersch.seeds.representations.issues;

import javax.validation.constraints.NotNull;

public class IssueAssignForm{
    @NotNull
    private Long assigneeId;
    @NotNull
    private Long assignerId;
    @NotNull
    private Long issueId;

    public IssueAssignForm(){}

    public IssueAssignForm(Long assigneeId, Long assignerId, Long issueId) {
        this.assigneeId = assigneeId;
        this.assignerId = assignerId;
        this.issueId = issueId;
    }

    public Long getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(Long assignerId) {
        this.assignerId = assignerId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String toString() {
        return "IssueAssignForm{" +
                "assigneeId=" + assigneeId +
                ", assignerId=" + assignerId +
                ", issueId=" + issueId +
                '}';
    }
}