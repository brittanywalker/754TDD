package com.thoersch.seeds.resources.issues;

import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.representations.issues.Issue;
import org.springframework.data.domain.*;
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
}
