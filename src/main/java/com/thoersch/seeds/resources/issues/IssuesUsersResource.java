package com.thoersch.seeds.resources.issues;

import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.issues.IssueAssignForm;
import com.thoersch.seeds.representations.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private final UsersRepository usersRepository;

    @Inject
    public IssuesUsersResource(IssuesRepository issuesRepository, UsersRepository usersRepository) {
        this.issuesRepository = issuesRepository;
        this.usersRepository = usersRepository;
    }

    @GET
    public List<User> getAssignees(@PathParam("issueId") long issueId) {
         Issue issue = issuesRepository.findOne(issueId);

        if(issue == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return issue.getAssignees();
    }

    @POST
    public ResponseEntity assignIssue(@Valid IssueAssignForm issueAssignForm){

        User assignee = usersRepository.findOne(issueAssignForm.getAssigneeId());
        User assigner = usersRepository.findOne(issueAssignForm.getAssignerId());
        Issue issue = issuesRepository.findOne(issueAssignForm.getIssueId());

        try{
            issue.addAssignee(assignee,assigner);
        }catch (IllegalAccessError | Exception e){
            return new ResponseEntity<>("Failed: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>("UserID: " +assignee.getId() + " was assigned to IssueId: "+issue.getId(),HttpStatus.ACCEPTED);
    }

    @POST
    @Path("/remove")
    public ResponseEntity removeIssue(@Valid IssueAssignForm issueAssignForm){

        User assignee = usersRepository.findOne(issueAssignForm.getAssigneeId());
        User assigner = usersRepository.findOne(issueAssignForm.getAssignerId());
        Issue issue = issuesRepository.findOne(issueAssignForm.getIssueId());
        Boolean success = Boolean.FALSE;
        try{
            success = issue.removeAssignee(assignee,assigner);
        }catch (IllegalAccessError e){
            return new ResponseEntity<>("Failed: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }


        if (success){
            return new ResponseEntity<>("UserID: " +assignee.getId() + " was removed from IssueId: "+issue.getId(),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Failed: Issue was not previously assigned",HttpStatus.BAD_REQUEST);
        }
    }
}

