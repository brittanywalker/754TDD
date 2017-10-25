package com.thoersch.seeds.resources.forumposts;

import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.Path;


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
}
