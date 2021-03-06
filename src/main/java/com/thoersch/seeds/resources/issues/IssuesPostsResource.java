package com.thoersch.seeds.resources.issues;

import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.ForumPostCategorizeForm;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/issues/{issueId}/forumposts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class IssuesPostsResource {
    private final IssuesRepository issuesRepository;
    private final ForumPostsRepository forumPostsRepository;
    private final UsersRepository usersRepository;

    @Inject
    public IssuesPostsResource(IssuesRepository issuesRepository, ForumPostsRepository forumPostsRepository, UsersRepository usersRepository) {
        this.issuesRepository = issuesRepository;
        this.forumPostsRepository = forumPostsRepository;
        this.usersRepository = usersRepository;
    }

    @GET
    public List<ForumPost> getForumPosts(@PathParam("issueId") long issueId) {
        Issue issue = issuesRepository.findOne(issueId);
        if(issue == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return issue.getForumPosts();
    }

    @POST
    public ResponseEntity addPostToIssue(@Valid ForumPostCategorizeForm issueCategorizeForm) {
        Issue issue = issuesRepository.findOne(issueCategorizeForm.getIssueId());
        ForumPost toAdd = forumPostsRepository.findOne(issueCategorizeForm.getForumPostId());
        User user = usersRepository.findOne(issueCategorizeForm.getAssignerId());

        //if user is not admin, do not allow
        if (user.getRole() != User.UserRole.admin) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        issue.addForumPost(toAdd);

        issuesRepository.save(issue);

        return new ResponseEntity<>("UserID: " +user.getId() + " added forum post: "+toAdd.get_id() + " to issue: " + issue.getId(), HttpStatus.ACCEPTED);
    }

    @DELETE
    public ResponseEntity removePostFromIssue(@Valid ForumPostCategorizeForm issueCategorizeForm) {
        Issue issue = issuesRepository.findOne(issueCategorizeForm.getIssueId());
        ForumPost toRemove = forumPostsRepository.findOne(issueCategorizeForm.getForumPostId());
        User user = usersRepository.findOne(issueCategorizeForm.getAssignerId());

        //if user is not admin, do not allow
        if (user.getRole() != User.UserRole.admin) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        issue.removeForumPost(toRemove);

        issuesRepository.save(issue);

        return new ResponseEntity<>("UserID: " +user.getId() + " removed forum post: "+toRemove.get_id() + " to issue: " + issue.getId(), HttpStatus.ACCEPTED);

    }

}
