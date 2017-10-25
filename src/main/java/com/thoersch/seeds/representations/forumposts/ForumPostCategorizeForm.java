package com.thoersch.seeds.representations.forumposts;

import javax.validation.constraints.NotNull;

public class ForumPostCategorizeForm {
    @NotNull
    private Long forumPostId;
    @NotNull
    private Long assignerId;
    @NotNull
    private Long issueId;

    public ForumPostCategorizeForm(){}

    public ForumPostCategorizeForm(Long forumPostId, Long assignerId, Long issueId) {
        this.forumPostId = forumPostId;
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

    public Long getForumPostId() {
        return forumPostId;
    }

    public void setForumPostId(Long forumPostId) {
        this.forumPostId = forumPostId;
    }

    @Override
    public String toString() {
        return "IssueAssignForm{" +
                "forumPostId=" + forumPostId +
                ", assignerId=" + assignerId +
                ", issueId=" + issueId +
                '}';
    }
}
