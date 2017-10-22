package com.thoersch.seeds.resources.users;

import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.representations.users.UserLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class UsersResource {

    private final UsersRepository usersRepository;

    @Inject
    public UsersResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GET
    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") long id) {
        User user = usersRepository.findOne(id);

        if(user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return user;
    }

    @GET
    @Path("/email/{email}")
    public User getUser(@PathParam("email") String email) {

        List<User> userList = getUsers();
        User user = null;
        for (User u : userList){
            if (u.getEmailAddress().equals(email)){
                user = u;
            }
        }

        if(user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return user;
    }

    @POST
    @Path("/login")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity authUser(@Valid UserLogin userLogin) {

        List<User> userList = getUsers();
        for (User u : userList){
            if (u.getEmailAddress().equals(userLogin.getEmailAddress())){
                if (u.getPassword().equals(userLogin.getPassword())){
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            }
        }

        throw new ResourceUnauthorizedException();

//        return new ResponseEntity<String>("Unauthorised", HttpStatus.UNAUTHORIZED);
    }


    @POST
    public User saveUser(@Valid User user) {
        return usersRepository.save(user);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") long id) {
        usersRepository.delete(id);
    }
}

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
final class ResourceUnauthorizedException extends RuntimeException {
    
}
