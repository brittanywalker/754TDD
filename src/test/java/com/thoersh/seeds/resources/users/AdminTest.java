package com.thoersh.seeds.resources.users;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.UserLogin;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersch.seeds.resources.users.UsersResource;
import com.thoersch.seeds.representations.users.User;

import com.github.springtestdbunit.*;
import com.github.springtestdbunit.annotation.*;
import com.thoersch.seeds.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.*;

import static org.junit.Assert.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(AdminTest.DATA_SET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { AdminTest.DATA_SET })
@DirtiesContext
public class AdminTest {

    protected static final String DATA_SET = "classpath:admin-items.xml";


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private IssuesRepository issuesRepository;

    private IssuesResource issuesResource;

//    private Cluster cluster;
//    private ForumPost forumPost;
//    private ForumPostRepository forumPostRepository;
//    private ForumPostResource forumPostResource;

    private UsersResource usersResourceMock;


    @Before
    public void init() {
        this.usersResourceMock = new UsersResource(usersRepository);
        this.issuesResource = new IssuesResource(issuesRepository);
//        this.forumPostRepository = new ForumPostResource(forumPostRepository);
    }

    //TODO remove
    @Test
    public void testDummy(){
        return;
    }


//    /**
//     * TEST ID: 7
//     *
//     * Admin can add a forum post to a cluster
//     */
//    @Test
//    public void testAddingPostToCluster(){
//        Boolean actual = forumPost.addToCluster(cluster, userAdmin);
//        assertEquals(Boolean.TRUE, actual);
//    }
//
//    /**
//     * TEST ID: 7.1
//     *
//     * Developer cannot add a forum post to a cluster
//     */
//    @Test
//    public void testFailAddingPostToCluster(){
//        Boolean actual = forumPost.addToCluster(cluster, user);
//        assertEquals(Boolean.FALSE, actual);
//    }
//
//    /**
//     * TEST ID: 8
//     *
//     * Admin can remove a forum post to a cluster
//     */
//    @Test
//    public void testRemovePostFromCluster(){
//        forumPost.addToCluster(cluster, user);
//        Boolean actual = forumPost.removeCluster(cluster, userAdmin);
//        assertEquals(Boolean.TRUE, actual);
//    }

    /**
     * TEST ID: 10 / 10.2
     *
     * Admin assigns issue to a developer
     */
    @Test
    public void testIssueAssign(){
        Issue issue = issuesResource.getIssue(1);
        User assignee = usersResourceMock.getUser(1);
        User admin = usersResourceMock.getUser(2);
        issue.addAssignee(assignee, admin);
    }

    /**
     * TEST ID: 10.1
     *
     * Non admin assigns issue to a developer
     */
    @Test(expected = IllegalAccessError.class)
    public void testNonAdminIssueAssign(){
        Issue issue = issuesResource.getIssue(1);
        User assignee = usersResourceMock.getUser(1);
        User nonAdmin = usersResourceMock.getUser(1);
        issue.addAssignee(assignee, nonAdmin);
    }

    /**
     * TEST ID: 11 / 11.2
     *
     * Admin un-assigns an issue to a developer
     */
    @Test
    public void testRemoveIssue(){
        Issue issue = issuesResource.getIssue(2);
        User assignee = usersResourceMock.getUser(1);
        User admin = usersResourceMock.getUser(2);
        Boolean success = issue.removeAssignee(assignee, admin);
        assertTrue(success);
    }

    /**
     * TEST ID: 11.1
     *
     * Non-admin un-assigns an issue to a developer
     */
    @Test(expected = IllegalAccessError.class)
    public void testNonAdminRemoveIssue(){
        Issue issue = issuesResource.getIssue(2);
        User assignee = usersResourceMock.getUser(1);
        User nonAdmin = usersResourceMock.getUser(1);
        Boolean success = issue.removeAssignee(assignee, nonAdmin);
    }

    /**
     * TEST ID: 11.3
     *
     * Admin un-assigns an issue to a developer they arent assigned to
     */
    @Test
    public void testRemoveNonAssignedIssue(){
        Issue issue = issuesResource.getIssue(1);
        User assignee = usersResourceMock.getUser(1);
        User admin = usersResourceMock.getUser(2);
        Boolean success = issue.removeAssignee(assignee, admin);
        assertFalse(success);
    }




}