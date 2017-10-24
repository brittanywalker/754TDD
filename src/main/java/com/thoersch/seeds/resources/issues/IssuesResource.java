package com.thoersch.seeds.resources.issues;

import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import org.springframework.data.domain.*;
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
@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class IssuesResource {

    private final IssuesRepository issuesRepository;

    @Inject
    public IssuesResource(IssuesRepository issuesRepository) {
        this.issuesRepository = issuesRepository;
    }

    @GET
    public List<Issue> getIssues() {
        return issuesRepository.findAll();
    }


    @GET
    @Path("/sort/{sort}")
    public List<Issue> getIssues(@PathParam("sort") String sortBy) {
        final Sort sort = new Sort(sortBy);
        return issuesRepository.findAll(sort);
    }

    @GET
    @Path("/{id}")
    public Issue getIssue(@PathParam("id") long id) {
        Issue issue = issuesRepository.findOne(id);

        if(issue == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return issue;
    }

    @POST
    public Issue saveIssue(@Valid Issue issue) {
        return issuesRepository.save(issue);
    }

    @DELETE
    @Path("/{id}")
    public void deleteIssue(@PathParam("id") long id) {
        issuesRepository.delete(id);
    }

    /**
     * Mark an issue as either PENDING, ASSIGNED, COMPLETED, REJECTED
     * Url - /1/?status=completed
     * @param user
     * @param issueId
     * @param status can be either PENDING, ASSIGNED, COMPLETED, REJECTED
     * @return
     */
    @POST
    @Path("/{id}/{userId}")
    public ResponseEntity changeIssueStatus(@Valid User user, @PathParam("id") long issueId, @QueryParam("status") String status) {
        final Issue issue = getIssue(issueId);

        final Issue.IssueStatus issueStatus;
        switch (status) {
            case "COMPLETED":
                issueStatus = Issue.IssueStatus.COMPLETED;
        }

        return new ResponseEntity<>("UserID:  was assigned to IssueId: "+issue.getId(), HttpStatus.ACCEPTED);
    }
}
