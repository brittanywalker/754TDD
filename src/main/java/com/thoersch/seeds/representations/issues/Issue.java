package com.thoersch.seeds.representations.issues;

import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.users.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
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

    @NotNull
    private String title;

    @Length(max = 1000)
    @NotNull
    private String description;

    @NotNull
    @Enumerated(STRING)
    private IssueStatus status = IssueStatus.PENDING;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_issues", inverseJoinColumns = @JoinColumn(name = "user_id", updatable = false, nullable = false),
            joinColumns = @JoinColumn(name = "issue_id", updatable = false, nullable = false))
    private List<User> assignees = new ArrayList<User>();

    private int priority = 0;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "forumposts_issues", inverseJoinColumns = @JoinColumn(name = "forumpost_id", updatable = false, nullable = false),
            joinColumns = @JoinColumn(name = "issue_id", updatable = false, nullable = false))
    private List<ForumPost> forumPosts = new ArrayList<ForumPost>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.length() == 0 || description.length() > 1000) {
            throw new IllegalArgumentException("Details should have a maximum word count of 1000");
        }

        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public void addAssignee(User assignee, User assigner){
        if (!(assigner.getRole().equals(User.UserRole.admin))){
            throw new IllegalAccessError("Unauthorised User");
        }
        if (this.status == IssueStatus.REJECTED || this.status == IssueStatus.COMPLETED) {
            throw new IllegalArgumentException("This issue is already rejected or completed");
        }

        this.assignees.add(assignee);
    }

    public Boolean removeAssignee(User assignee, User assigner){
        if (!(assigner.getRole().equals(User.UserRole.admin))){
            throw new IllegalAccessError("Unauthorised User");
        }
        for (Iterator<User> iterator = this.assignees.listIterator(); iterator.hasNext();){
            User u = iterator.next();
            if (Objects.equals(u.getId(), assignee.getId())){
                iterator.remove();
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public List<ForumPost> getForumPosts() {
        return forumPosts;
    }

    public void addForumPost(ForumPost post) {
        this.forumPosts.add(post);
        this.priority++;
    }

    public List<ForumPost> removeForumPost(ForumPost post) {
        this.forumPosts.removeIf(post1 -> Objects.equals(post1.get_id(), post.get_id()));

        return forumPosts;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
