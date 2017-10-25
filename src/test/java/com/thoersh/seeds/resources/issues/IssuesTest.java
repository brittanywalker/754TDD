package com.thoersh.seeds.resources.issues;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.thoersch.seeds.Application;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.issues.IssueAssignForm;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersch.seeds.resources.issues.IssuesUsersResource;
import com.thoersch.seeds.resources.users.UsersResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    private static final Long ISSUE_01 = 1L;
    private static final Long ISSUE_02 = 2L;
    private static final Long ISSUE_03 = 3L;

    @SuppressWarnings("WeakerAccess")
    protected static final String DATA_SET = "classpath:issue-items.xml";

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private UsersRepository usersRepository;

    private IssuesResource issuesResourceMock;
    private UsersResource usersResourceMock;
    private IssuesUsersResource issuesUsersResourceMock;

    @Before
    public void init() {
        this.issuesResourceMock = new IssuesResource(issuesRepository);
        this.usersResourceMock = new UsersResource(usersRepository);
        this.issuesUsersResourceMock = new IssuesUsersResource(issuesRepository, usersRepository);
    }

    @Test
    public void testIfSummeryOfIssueIsGenerated() {
        final Issue issue = issuesResourceMock.getIssue(1);
        assertNotNull(issue.getDescription());
    }

    /**
     * TEST ID :
     *
     * Tests if the issue summery is less than 1000 characters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIfSummeryOfIssueIsNotLengthy() {
        final Issue issue = issuesResourceMock.getIssue(1);
        final StringBuilder builder = new StringBuilder("_TEST_");
        for (int i = 0; i < 100; i++) {
            builder.append("_test_|_test_");
        }

        issue.setDescription(builder.toString());
    }

    /**
     * TEST ID :
     *
     * Tests if the issue summery is not empty
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIfSummeryOfIssueIsTooSmall() {
        final Issue issue = new Issue();
        issue.setDescription("");
    }

    /**
     * TEST ID :
     *
     * Tests if assignees cannot be added when the issue is already
     * marked as completed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddAssigneeWhenIssueIsCompleted() {
        final Issue issue = new Issue();
        User assignee = usersResourceMock.getUser(1);
        User admin = usersResourceMock.getUser(2);
        issue.setStatus(Issue.IssueStatus.COMPLETED);
        issue.addAssignee(assignee, admin);
    }

    /**
     * TEST ID :
     *
     * Tests if assignees cannot be added when the issue is already
     * marked as rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddAssigneeWhenIssueIsRejected() {
        final Issue issue = new Issue();
        User assignee = usersResourceMock.getUser(1);
        User admin = usersResourceMock.getUser(2);
        issue.setStatus(Issue.IssueStatus.REJECTED);
        issue.addAssignee(assignee, admin);
    }

    @Test
    public void testAddAssigneeAsAnAdmin() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_01);
        issue.setStatus(Issue.IssueStatus.PENDING);

        final User dev = usersResourceMock.getUser(USER_ID_DEV_1);
        assertTrue(dev.getRole() == User.UserRole.developer);

        final IssueAssignForm form = new IssueAssignForm(dev.getId(), admin.getId(), issue.getId());
        issuesUsersResourceMock.assignIssue(form);

        assertTrue(issuesUsersResourceMock.getAssignees(issue.getId()).contains(dev));
    }

    @Test(expected = WebApplicationException.class)
    public void testAddAssigneeAsADeveloper() {
        final User dev1 = usersResourceMock.getUser(USER_ID_DEV_1);
        assertTrue(dev1.getRole() != User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_01);
        issue.setStatus(Issue.IssueStatus.PENDING);

        final User dev2 = usersResourceMock.getUser(USER_ID_DEV_1);
        assertTrue(dev2.getRole() == User.UserRole.developer);

        final IssueAssignForm form = new IssueAssignForm(dev2.getId(), dev1.getId(), issue.getId());
        try {
            issuesUsersResourceMock.assignIssue(form);
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            throw e;
        }
    }

    /**
     * TEST ID :
     *
     */
    //@Test
//    public void testAddingNewIssue() {
//        final Issue issue = new Issue();
//        issue.setId(10L);
//        issue.setTitle("New Testing issue");
//        issue.setDescription("New Issue");
//        issue.setStatus(Issue.IssueStatus.COMPLETED);
//        issuesResourceMock.saveIssue(issue);
//    }

    /**
     * TEST ID :
     *
     * Test if all the issues can be retrieved from the system
     */
    @Test
    public void testGettingAllIssues() {
        final List<Issue> issues = issuesResourceMock.getIssues();
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        final Issue issue = issues.get(0);
        //assertEquals(1, issue.getId());
    }

    /**
     * TEST ID :
     *
     * Test if the issues can be sorted by the name
     */
    @Test
    public void testSortingIssuesByTitle() {
        final List<Issue> issues = issuesResourceMock.getIssues("title");
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        assertEquals("A_Title_FIRST", issues.get(0).getTitle());
    }

    /**
     * TEST ID :
     *
     * Test if the issues can be sorted by the description
     */
    @Test
    public void testSortIssuesByDescription() {
        final List<Issue> issues = issuesResourceMock.getIssues("description");
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        assertEquals("A_DESC_FIRST", issues.get(0).getDescription());
    }

    /**
     * TEST ID :
     *
     * Test if an admin can mark an issue as resolved
     */
    @Test
    public void testIfAdminCanMarkAnIssueAsResolved() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        ResponseEntity response = issuesResourceMock.changeIssueStatus(admin, ISSUE_01, "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
        assertEquals(response.getBody().toString(), "IssueID: 1  was marked as COMPLETED");
    }

    /**
     * TEST ID :
     *
     * Test if a developer who is assigned to an issue can mark it as resolved
     */
    @Test
    public void testIfDeveloperAssignedToAnIssueCanMarkItAsResolved() {
        final User devAssigned = usersResourceMock.getUser(USER_ID_DEV_1);
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);

        assertTrue(admin.getRole() == User.UserRole.admin);

        //Assign the user first
        Issue issue = issuesRepository.findOne(ISSUE_02);
        final IssueAssignForm form = new IssueAssignForm(devAssigned.getId(), admin.getId(), issue.getId());
        issuesUsersResourceMock.assignIssue(form);

        //Get the issue and check if he is assigned
        assertTrue(issuesUsersResourceMock.getAssignees(issue.getId()).contains(devAssigned));

        ResponseEntity response = issuesResourceMock.changeIssueStatus(devAssigned, issue.getId(), "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    /**
     * TEST ID :
     *
     * Test if a developer who is not assigned to an issue can mark it as resolved
     */
    @Test(expected = WebApplicationException.class)
    public void testIfDeveloperNotAssignedToAnIssueCanMarkItAsResolved() {
        final User devUnassigned = usersResourceMock.getUser(USER_ID_DEV_2);
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);

        assertTrue(admin.getRole() == User.UserRole.admin);

        //Un-assign if the user is already assigned
        Issue issue = issuesRepository.findOne(ISSUE_03);
        final IssueAssignForm form = new IssueAssignForm(devUnassigned.getId(), admin.getId(), issue.getId());
        issuesUsersResourceMock.removeIssue(form);

        assertTrue(!issuesUsersResourceMock.getAssignees(issue.getId()).contains(devUnassigned));

        try {
            issuesResourceMock.changeIssueStatus(devUnassigned, issue.getId(), "completed");
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testIfIssueStatusCanBeChangedToRejected() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_03);

        //Check rejected
        ResponseEntity response = issuesResourceMock.changeIssueStatus(admin, issue.getId(), "rejected");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    @Test
    public void testIfIssueStatusCanBeChangedToPending() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_03);

        //Check pending
        ResponseEntity response = issuesResourceMock.changeIssueStatus(admin, issue.getId(), "pending");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    @Test
    public void testIfIssueStatusCanBeChangedToAssigned() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_03);

        //Check assigned
        ResponseEntity response = issuesResourceMock.changeIssueStatus(admin, issue.getId(), "assigned");
        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
    }

    @Test(expected = WebApplicationException.class)
    public void testInvalidIssueStatus() {
        final User admin = usersResourceMock.getUser(USER_ID_ADMIN);
        assertTrue(admin.getRole() == User.UserRole.admin);

        final Issue issue = issuesResourceMock.getIssue(ISSUE_03);

        //Check invalid status
        try {
            issuesResourceMock.changeIssueStatus(admin, issue.getId(), "invalid");
        } catch (WebApplicationException e) {
            assertEquals(e.getResponse().getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
            throw e;
        }
    }

    /**
     * TEST ID :
     *
     */
    @Test
    public void testIfNumberOfRelatedPostsAreGreaterThanZero() {
        final Issue issue = issuesResourceMock.getIssue(1);
        assertTrue(issue.getNumberOfRelatedIssues() > 0);
    }

}
