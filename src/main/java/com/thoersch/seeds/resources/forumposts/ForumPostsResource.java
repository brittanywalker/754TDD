package com.thoersch.seeds.resources.forumposts;

import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by Mira on 23/10/2017.
 */

@SuppressWarnings("ALL")
@Component
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class ForumPostsResource {

    private final ForumPostsRepository forumPostsRepository;

    @Inject
    public ForumPostsResource(ForumPostsRepository forumPostsRepository) {
        this.forumPostsRepository = forumPostsRepository;
    }

    @GET
    public List<ForumPost> getForumPosts() {
        return forumPostsRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public ForumPost getForumPost(@PathParam("id") long id) {
        ForumPost post = forumPostsRepository.findOne(id);

        if(post == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return post;
    }

    @POST
    public ForumPost saveForumPost(@Valid ForumPost post) {
        return forumPostsRepository.save(post);
    }

    @DELETE
    @Path("/{id}")
    public void deleteForumPost(@PathParam("id") long id) {
        forumPostsRepository.delete(id);
    }
}
