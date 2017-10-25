package com.thoersch.seeds.resources.forumposts;

import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by Mira on 23/10/2017.
 */

@Component
@Transactional
public class ForumPostsResource {

    private final ForumPostsRepository forumPostsRepository;

    @Inject
    public ForumPostsResource(ForumPostsRepository forumPostsRepository) {
        this.forumPostsRepository = forumPostsRepository;
    }

    @GET
    public List<ForumPost> getPosts() {
        List<ForumPost> posts = forumPostsRepository.findAll();
        return posts;
    }
}
