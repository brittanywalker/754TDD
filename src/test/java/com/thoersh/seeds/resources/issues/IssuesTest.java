package com.thoersh.seeds.resources.issues;

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
import com.thoersch.seeds.representations.issues.IssueAssignForm;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.resources.forumposts.ForumPostsResource;
import com.thoersch.seeds.resources.issues.IssuesPostsResource;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersch.seeds.resources.issues.IssuesUsersResource;
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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(IssuesTest.DATA_SET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { IssuesTest.DATA_SET })
@DirtiesContext
public class IssuesTest {

    private static final Long USER_ID_ADMIN = 2L;
    private static final Long USER_ID_DEV_1 = 1L;
    private static final Long USER_ID_DEV_2 = 3L;

    private static final Long ISSUE_ID_01 = 1L;
    private static final Long ISSUE_ID_02 = 2L;
    private static final Long ISSUE_ID_03 = 3L;

    @SuppressWarnings("WeakerAccess")
    protected static final String DATA_SET = "classpath:issue-items.xml";

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ForumPostsRepository forumPostsRepository;

    private IssuesResource issuesResource;
    private UsersResource usersResource;
    private IssuesUsersResource issuesUsersResource;
    private IssuesPostsResource issuesPostsResource;
    private ForumPostsResource forumPostsResource;

    @Before
    public void init() {
        this.issuesResource = new IssuesResource(issuesRepository);
        this.usersResource = new UsersResource(usersRepository);
        this.forumPostsResource = new ForumPostsResource(forumPostsRepository);
        this.issuesUsersResource = new IssuesUsersResource(issuesRepository, usersRepository);
        this.issuesPostsResource = new IssuesPostsResource(issuesRepository, forumPostsRepository, usersRepository);
    }

    /**
     * TEST ID : 5.1.1
     *
     * Tests that if the summary is more than 1000 characters an exception will fire
     */
    @Test(expected = IllegalArgumentException.class)
    public void unsuccessfulIssueSummaryIsOver1000Test() {
        final Issue issue = new Issue();
        final StringBuilder builder = new StringBuilder("_TEST_");
        for (int i = 0; i < 100; i++) {
            builder.append("_test_|_test_");
        }

        assertTrue(builder.toString().length() > 1000);
        issue.setDescription(builder.toString());
    }

    /**
     * TEST ID : 5.1.2
     *
     * Tests the exception if the summary is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void unsuccessfulIssueSummaryIsEmptyTest() {
        final Issue issue = new Issue();
        issue.setDescription("");
    }

    /**
     * TEST ID : 5.2
     *
     * Tests if assignees cannot be added when the issue is already
     * marked as completed
     */
    @Test(expected = IllegalArgumentException.class)
    public void unsuccessfulAssigneeAdditionToResolvedIssueTest() {
        final Issue issue = new Issue();
        User assignee = usersResource.getUser(1);
        User admin = usersResource.getUser(2);
        issue.setStatus(Issue.IssueStatus.COMPLETED);
        issue.addAssignee(assignee, admin);
    }

    /**
     * TEST ID : 5.3
     *
     * Tests if assignees cannot be added when the issue is already
     * marked as rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void unsuccessfulAssigneeAdditionToRejectedIssueTest() {
        final Issue issue = new Issue();
        User assignee = usersResource.getUser(1);
        User admin = usersResource.getUser(2);
        issue.setStatus(Issue.IssueStatus.REJECTED);
        issue.addAssignee(assignee, admin);
    }

    /**
     * TEST ID : 5.4
     *
     * Tests if admin users can add new assignees for an issue
     */
    @Test
    public void successfulAssigneeAdditionByAdminTest() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_01);
        issue.setStatus(Issue.IssueStatus.PENDING);

        final User dev = usersResource.getUser(USER_ID_DEV_1);
        assertTrue(dev.getRole() == User.UserRole.developer);

        final IssueAssignForm form = new IssueAssignForm(dev.getId(), admin.getId(), issue.getId());
        issuesUsersResource.assignIssue(form);

        assertTrue(issuesUsersResource.getAssignees(issue.getId()).contains(dev));
    }

    /**
     * TEST ID : 5.5
     *
     * Tests if developer can add new assignees for an issue.
     * This test should fail since developers are not allowed to add new
     * assignees
     */
    @Test(expected = WebApplicationException.class)
    public void unsuccessfulAssigneeAdditionByDeveloperTest() {
        final User dev1 = usersResource.getUser(USER_ID_DEV_1);
        assertTrue(dev1.getRole() != User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_01);
        issue.setStatus(Issue.IssueStatus.PENDING);

        final User dev2 = usersResource.getUser(USER_ID_DEV_1);
        assertTrue(dev2.getRole() == User.UserRole.developer);

        final IssueAssignForm form = new IssueAssignForm(dev2.getId(), dev1.getId(), issue.getId());
        try {
            issuesUsersResource.assignIssue(form);
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            throw e;
        }
    }

    /**
     * TEST ID : 6.1
     *
     * Test if the issues can be sorted by the name
     */
    @Test
    public void successfulIssueSortByTitleTest() {
        final List<Issue> issues = issuesResource.getIssues("title");
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        assertEquals("A_Title_FIRST", issues.get(0).getTitle());
    }

    /**
     * TEST ID : 6.2
     *
     * Test if the issues can be sorted by the description
     */
    @Test
    public void successfulIssueSortByDescriptionTest() {
        final List<Issue> issues = issuesResource.getIssues("description");
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        assertEquals("A_DESC_FIRST", issues.get(0).getDescription());
    }

    /**
     * TEST ID : 6.3
     *
     * Test if the issues can be sorted by the priority (i.e according to the number of forum posts)
     */
    @Test
    public void successfulIssueSortByPriorityTest() {
        final ForumPost post1 = forumPostsResource.getForumPost(1L);
        final ForumPost post2 = forumPostsResource.getForumPost(2L);
        final ForumPost post3 = forumPostsResource.getForumPost(3L);

        final Issue issue1 = issuesResource.getIssue(ISSUE_ID_01);
        final Issue issue2 = issuesResource.getIssue(ISSUE_ID_03);

        final ForumPostCategorizeForm form1ForIssue1 = new ForumPostCategorizeForm(post1.get_id(), USER_ID_ADMIN, issue1.getId());
        final ForumPostCategorizeForm form2ForIssue1 = new ForumPostCategorizeForm(post2.get_id(), USER_ID_ADMIN, issue1.getId());
        final ForumPostCategorizeForm form1ForIssue2 = new ForumPostCategorizeForm(post3.get_id(), USER_ID_ADMIN, issue2.getId());

        //Add those to issues
        issuesPostsResource.addPostToIssue(form1ForIssue1);
        issuesPostsResource.addPostToIssue(form2ForIssue1);
        issuesPostsResource.addPostToIssue(form1ForIssue2);

        //Since Issue1 has more posts, if should be prioritized
        final List<Issue> issues = issuesResource.getIssues("priority");
        assertTrue(issues != null);
        assertTrue(issues.size() > 0);
        assertTrue(issues.get(0) != null);

        assertTrue(issuesResource.getIssue(ISSUE_ID_01).getPriority() == 2);
        assertTrue(issuesResource.getIssue(ISSUE_ID_02).getPriority() == 0);
        assertTrue(issuesResource.getIssue(ISSUE_ID_03).getPriority() == 1);

        assertEquals(issues.get(0).getId(), issue1.getId());
    }

    /**
     * TEST ID : 12.1
     *
     * Test if an admin can mark an issue as resolved
     */
    @Test
    public void successfulMarkingIssueAsResolvedByAdminTest() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        ResponseEntity response = issuesResource.changeIssueStatus(admin, ISSUE_ID_01, "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
        assertEquals(response.getBody().toString(), "IssueID: 1  was marked as COMPLETED");
    }

    /**
     * TEST ID : 12.2
     *
     * Test if a developer who is assigned to an issue can mark it as resolved
     */
    @Test
    public void successfulDeveloperMarkingAssignedIssueAsResolvedTest() {
        final User devAssigned = usersResource.getUser(USER_ID_DEV_1);
        final User admin = usersResource.getUser(USER_ID_ADMIN);

        assertTrue(admin.getRole() == User.UserRole.admin);

        //Assign the user first
        Issue issue = issuesRepository.findOne(ISSUE_ID_02);
        final IssueAssignForm form = new IssueAssignForm(devAssigned.getId(), admin.getId(), issue.getId());
        issuesUsersResource.assignIssue(form);

        //Get the issue and check if he is assigned
        assertTrue(issuesUsersResource.getAssignees(issue.getId()).contains(devAssigned));

        ResponseEntity response = issuesResource.changeIssueStatus(devAssigned, issue.getId(), "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    /**
     * TEST ID : 12.3
     *
     * Test if a developer who is not assigned to an issue can mark it as resolved
     */
    @Test(expected = WebApplicationException.class)
    public void unsuccessfulDevelopedMarkingUnassignedIssueAsResolvedTest() {
        final User devUnassigned = usersResource.getUser(USER_ID_DEV_2);
        final User admin = usersResource.getUser(USER_ID_ADMIN);

        assertTrue(admin.getRole() == User.UserRole.admin);

        //Un-assign if the user is already assigned
        Issue issue = issuesRepository.findOne(ISSUE_ID_03);
        final IssueAssignForm form = new IssueAssignForm(devUnassigned.getId(), admin.getId(), issue.getId());
        issuesUsersResource.removeIssue(form);

        assertTrue(!issuesUsersResource.getAssignees(issue.getId()).contains(devUnassigned));

        try {
            issuesResource.changeIssueStatus(devUnassigned, issue.getId(), "completed");
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            throw e;
        }
    }

    /**
     * TEST ID : 12.4.1
     *
     * Test if the issue status can be changed to rejected
     */
    @Test
    public void testIfIssueStatusCanBeChangedToRejected() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_03);

        //Check rejected
        ResponseEntity response = issuesResource.changeIssueStatus(admin, issue.getId(), "rejected");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    /**
     * TEST ID : 12.4.2
     *
     * Test if the issue status can be changed to pending
     */
    @Test
    public void testIfIssueStatusCanBeChangedToPending() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_03);

        //Check pending
        ResponseEntity response = issuesResource.changeIssueStatus(admin, issue.getId(), "pending");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    /**
     * TEST ID : 12.4.3
     *
     * Test if the issue status can be changed to assigned
     */
    @Test
    public void testIfIssueStatusCanBeChangedToAssigned() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_03);

        //Check assigned
        ResponseEntity response = issuesResource.changeIssueStatus(admin, issue.getId(), "assigned");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    /**
     * TEST ID : 12.4.4
     *
     * Test if the issue status can not be changed to an invalid status
     * Only supported status are PENDING, ASSIGNED, COMPLETED, REJECTED
     */
    @Test(expected = WebApplicationException.class)
    public void testInvalidIssueStatus() {
        final User admin = usersResource.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResource.getIssue(ISSUE_ID_03);

        //Check invalid status
        try {
            issuesResource.changeIssueStatus(admin, issue.getId(), "invalid");
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
            throw e;
        }
    }
}
