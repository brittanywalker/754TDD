package com.thoersch.seeds.resources.users;

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
@Path("/users/{userId}/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class UsersIssuesResource {
    private final UsersRepository usersRepository;

    @Inject
    public UsersIssuesResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GET
    public List<Issue> getUsersIssues(@PathParam("userId") long userId) {
        User user = usersRepository.findOne(userId);

        if(user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return user.getIssues();
    }
}