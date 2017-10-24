package com.thoersch.seeds.resources.users;

import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/issues/{issueId}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class IssuesUsersResource {
    private final IssuesRepository issuesRepository;

    @Inject
    public IssuesUsersResource(IssuesRepository issuesRepository) {
        this.issuesRepository = issuesRepository;
    }

    @GET
    public List<User> getAssignees(@PathParam("issueId") long issueId) {
         Issue issue = issuesRepository.findOne(issueId);

        if(issue == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return issue.getAssignees();
    }
}