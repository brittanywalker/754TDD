package com.thoersh.seeds.resources.forumposts;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.thoersch.seeds.Application;
import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.ForumPostCategorizeForm;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.resources.forumposts.ForumPostsResource;
import com.thoersch.seeds.resources.issues.IssuesPostsResource;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersh.seeds.resources.issues.IssuesTest;
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

import java.util.List;

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

    private ForumPostsResource resource;

    private IssuesResource issuesResource;

    private IssuesPostsResource postsResource;

    private User adminUser;

    @Before
    public void init() {
        this.resource = new ForumPostsResource(repo);
        this.issuesResource = new IssuesResource(issuesRepo);

        //set up mock admin user
        adminUser = Mockito.mock(User.class);
        Mockito.when(adminUser.getRole()).thenReturn(User.UserRole.admin);

    }


    /**
     * TEST ID: 8.1
     * Administrator can remove forum post from a cluster with more than one post
     */
    @Test
    public void testAdminRemoveForumPost() {
        List<Issue> issues = issuesResource.getIssues();
        Issue issue = issuesResource.getIssue(1l);
        ForumPost toRemove = issue.getForumPosts().get(0);
        ResponseEntity actual = postsResource.removePostFromIssue(new ForumPostCategorizeForm(
                toRemove.get_question_id(),
                adminUser.getId(),
                issue.getId()
        ));
        if (actual.getStatusCode() != HttpStatus.ACCEPTED) {
            fail("Could not remove forum post from cluster, response: \n" + actual.toString());
        }

        //status code is ACCEPTED but there is no guarantee the correct operation has been performed correctly on the list of forum posts belonging to the issue
        List<ForumPost> expectedPostList = issue.removeForumPost(toRemove);
        //post must be moved to only ONE (new) issue, and must be only post in issue.
        //confirm that removed forum post has been put into own issue/cluster.
        issues = issuesResource.getIssues();
        Integer presenceCount = 0;
        for (Issue i : issues) {
            if (i.getForumPosts().contains(toRemove) && i.getForumPosts().size() == 1) {
                presenceCount++;
            } else if (i.getForumPosts().contains(toRemove) && i.getForumPosts().size() != 1) {
                fail("Forum post has not been moved into its own new issue");
            }
        }

        if (presenceCount == 0) {
            fail("Forum post not present in any issues. Should be present in at least one.");
        }
    }

    /**
     * TEST ID: 8.2
     * Admin remove forum post from cluster with only one post (Where should the forum post go? New cluster? Next best cluster?)
     */
    @Test
    public void testAdminRemovePostFromSinglePostCluster() {

    }

    /**
     * TEST ID: 8.3
     * Administrator attempts to remove forum post from cluster when it is not in it
     */
    @Test
    public void testRemovePostFromClusterItDoesNotBelongTo() {

    }

    /**
     * TEST ID: 8.4
     * Developer attempt to remove forum post from cluster (disallowed, insufficient privileges)
     */
    @Test
    public void testRemovePostFromClusterAsDev() {

    }
}
