package com.thoersh.seeds.resources.issues;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.thoersch.seeds.Application;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.representations.users.User;
import com.thoersch.seeds.resources.issues.IssuesResource;
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

    @SuppressWarnings("WeakerAccess")
    protected static final String DATA_SET = "classpath:issue-items.xml";

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private UsersRepository usersRepository;

    private IssuesResource issuesResourceMock;
    private UsersResource usersResourceMock;

    @Before
    public void init() {
        this.issuesResourceMock = new IssuesResource(issuesRepository);
        this.usersResourceMock = new UsersResource(usersRepository);
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
        final List<Issue> issues = issuesResourceMock.getIssues("");
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
        final Issue issue = issuesResourceMock.getIssue(1L);
        final User admin = usersResourceMock.getUser(2L);

        ResponseEntity response = issuesResourceMock.changeIssueStatus(admin, 1L, "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
        assertEquals(response.getBody().toString(), "");
    }

    /**
     * TEST ID :
     *
     * Test if a developer who is assigned to an issue can mark it as resolved
     */
    @Test
    public void testIfDeveloperAssignedToAnIssueCanMarkItAsResolved() {
        final User devAssigned = usersResourceMock.getUser(1L);
        final User admin = usersResourceMock.getUser(2L);

        //Assign the user first
        final Issue issue = issuesRepository.findOne(2L);
        issue.addAssignee(devAssigned, admin);

        ResponseEntity response = issuesResourceMock.changeIssueStatus(devAssigned, issue.getId(), "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
        assertEquals(response.getBody().toString(), "");
    }

    /**
     * TEST ID :
     *
     * Test if a developer who is not assigned to an issue can mark it as resolved
     */
    @Test
    public void testIfDeveloperNotAssignedToAnIssueCanMarkItAsResolved() {
        final User devUnassigned = usersResourceMock.getUser(3L);
        final User admin = usersResourceMock.getUser(2L);

        //Un-assign if the user is already assigned
        final Issue issue = issuesRepository.findOne(3L);
        issue.removeAssignee(devUnassigned, admin);

        ResponseEntity response = issuesResourceMock.changeIssueStatus(devUnassigned, issue.getId(), "completed");

        assertEquals(response.getStatusCode().value(), HttpStatus.ACCEPTED.value());
        assertEquals(response.getBody().toString(), "");
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
