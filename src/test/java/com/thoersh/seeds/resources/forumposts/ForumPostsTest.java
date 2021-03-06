package com.thoersh.seeds.resources.forumposts;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.thoersch.seeds.Application;
import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.ForumPostCategorizeForm;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.resources.forumposts.ForumPostsResource;
import com.thoersch.seeds.resources.issues.IssuesPostsResource;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersch.seeds.resources.users.UsersResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test Suite 8: An administrator can remove forum post from a cluster.
 */

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(ForumPostsTest.DATA_SET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ForumPostsTest.DATA_SET })
@DirtiesContext
public class ForumPostsTest {

    @SuppressWarnings("WeakerAccess")
    protected static final String DATA_SET = "classpath:forumpost-items.xml";

    @Autowired
    private ForumPostsRepository repo;

    @Autowired
    private IssuesRepository issuesRepo;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ForumPostsRepository forumPostsRepository;

    private ForumPostsResource resource;
    private IssuesResource issuesResource;
    private IssuesPostsResource postsResource;

    private User adminUser;
    private User devUser;

    @Before
    public void init() {
        this.resource = new ForumPostsResource(repo);
        this.issuesResource = new IssuesResource(issuesRepo);
        this.postsResource = new IssuesPostsResource(issuesRepo, forumPostsRepository, usersRepository);

        final UsersResource usersResource = new UsersResource(usersRepository);
        adminUser = usersResource.getUser(1L);
        devUser = usersResource.getUser(2L);

    }
    
    /**
     * TEST ID: 8.1
     * Administrator can remove forum post from a cluster with more than one post
     */
    @Test
    public void testAdminRemoveForumPost() {
        //Get an issue that has multiple posts in it
        Issue issue = issuesResource.getIssue(1l);

        //Get the first post in it and try to remove it
        ForumPost toRemove = issue.getForumPosts().get(0);

        final ForumPostCategorizeForm form = new ForumPostCategorizeForm(toRemove.get_id(), adminUser.getId(), issue.getId());
        ResponseEntity actual = postsResource.removePostFromIssue(form);

        if (actual.getStatusCode().value() != HttpStatus.ACCEPTED.value()) {
            fail("Could not remove forum post from cluster, response: \n" + actual.getBody());
        }

        //status code is ACCEPTED but there is no guarantee the correct operation has been performed correctly on the list of forum posts belonging to the issue

        // Check if it is still in the same issue. If so, fail test.
        if (issuesResource.getIssue(1l).getForumPosts().contains(toRemove)) {
            fail("Forum post still in same issue.");
        }

        List<Issue> issues = issuesResource.getIssues();
        Integer singlePostIssueCount = 0; //how many times post appears as only post in issue
        for (Issue i : issues) {
            if (i.getForumPosts().contains(toRemove) && i.getForumPosts().size() == 1) {
                singlePostIssueCount++;
            }
        }

        if (singlePostIssueCount > 1) {
            fail("Too many issues where post is the only one.");
        }
    }

    /**
     * TEST ID: 8.2
     * Admin removes forum post from cluster with only one post.
     * Expected behaviour: forum post goes to new single-post cluster.
     * Test is essentially the same as 8.1, a different issue (with only one associated post)
     * is retrieved from the database for the test.
     */
    @Test
    public void testAdminRemovePostFromSinglePostCluster() {
        //get an issue that has only one post in it (id 2 in migration data)
        List<Issue> issues = issuesResource.getIssues();
        Issue issue = issuesResource.getIssue(2l);

        //Get the only post in it and try to remove it
        ForumPost toRemove = issue.getForumPosts().get(0);
        ResponseEntity actual = postsResource.removePostFromIssue(new ForumPostCategorizeForm(
                toRemove.get_id(),
                adminUser.getId(),
                issue.getId()
        ));


        if (actual.getStatusCode() != HttpStatus.ACCEPTED) {
            fail("Could not remove forum post from cluster, response: \n" + actual.getBody());
        }

        //status code is ACCEPTED but there is no guarantee the correct operation has been performed correctly on the list of forum posts belonging to the issue

        // Check if it is still in the same issue. If so, fail test.
        if (issuesResource.getIssue(1l).getForumPosts().contains(toRemove)) {
            fail("Forum post still in same issue.");
        }

        issues = issuesResource.getIssues();
        Integer singlePostIssueCount = 0; //how many times post appears as only post in issue
        for (Issue i : issues) {
            if (i.getForumPosts().contains(toRemove) && i.getForumPosts().size() == 1) {
                singlePostIssueCount++;
            }
        }

        if (singlePostIssueCount > 1) {
            fail("Too many issues where post is the only one.");
        }
    }

    /**
     * TEST ID: 8.3
     * Administrator attempts to remove forum post from cluster when it is not in it
     */
    @Test
    public void testRemovePostFromClusterItDoesNotBelongTo() {
        // Would arise exception if trying to remove a post that doesn't exist.
        Issue issue = issuesResource.getIssue(1l);
        ForumPost toRemove = issue.getForumPosts().get(0);
        ResponseEntity test = postsResource.removePostFromIssue(new ForumPostCategorizeForm(
                toRemove.get_id(),
                adminUser.getId(),
                issue.getId()
        ));

        // Removing twice will provide an issue that is no longer in said cluster.
        ResponseEntity actual = postsResource.removePostFromIssue(new ForumPostCategorizeForm(
                toRemove.get_id(),
                adminUser.getId(),
                issue.getId()
        ));

        if (actual.getStatusCode() != HttpStatus.ACCEPTED) {
            fail("Could not remove forum post from cluster, response: \n" + actual.toString());
        }
    }

    /**
     * TEST ID: 8.4
     * Developer attempt to remove forum post from cluster (disallowed, insufficient privileges)
     */
    @Test(expected = WebApplicationException.class)
    public void testDeveloperRemoveForumPost() {
        try {
            //Get the first post in it and try to remove it
            Issue issue = issuesResource.getIssue(3L);
            ForumPost toRemove = issue.getForumPosts().get(0);

            postsResource.removePostFromIssue(new ForumPostCategorizeForm( toRemove.get_id(), devUser.getId(), issue.getId()));
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            throw e;
        }
    }
}
