package com.thoersch.seeds.persistence.forumposts;

import com.thoersch.seeds.representations.forumposts.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mira on 23/10/2017.
 */
public interface ForumPostsRepository extends JpaRepository<ForumPost, Long> {
}
