package com.thoersch.seeds.resources.issues;

import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.ForumPostCategorizeForm;
import com.thoersch.seeds.representations.issues.Issue;
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

    @Inject
    public IssuesPostsResource(IssuesRepository issuesRepository, ForumPostsRepository forumPostsRepository) {
        this.issuesRepository = issuesRepository;
        this. forumPostsRepository = forumPostsRepository;
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

    }

    @POST
    public ResponseEntity removePostFromIssue(@Valid ForumPostCategorizeForm issueCategorizeForm) {

    }

}
