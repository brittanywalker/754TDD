package com.thoersch.seeds.representations.issues;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@javax.persistence.Entity
@Table(name = "issues")
public class Issue {

    public enum IssueStatus {
        PENDING, ASSIGNED, COMPLETED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 1000)
    @NotNull
    private String issueDetails;

    private int numberOfRelatedIssues;

    @NotNull
    @Enumerated(STRING)
    private IssueStatus issueStatus = IssueStatus.PENDING;

    //private List<String> assignees; //TODO change to appropriate class (person?)

    public Issue() {
        //this.assignees = new ArrayList<>(1);
    }

    public String getIssueDetails() {
        return issueDetails;
    }

    public void setIssueDetails(String issueDetails) {
        this.issueDetails = issueDetails;
    }

    public int getNumberOfRelatedIssues() {
        return numberOfRelatedIssues;
    }

    public void setNumberOfRelatedIssues(int numberOfRelatedIssues) {
        this.numberOfRelatedIssues = numberOfRelatedIssues;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }
//
//    public void addAssignee(String assignee) { //TODO change the class
//        this.assignees.add(assignee);
//    }
//
//    public List<String> getAssignees() { //TODO change the class
//        return assignees;
//    }
}
