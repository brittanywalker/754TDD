package com.thoersh.seeds.resources.issues;

import com.github.springtestdbunit.*;
import com.github.springtestdbunit.annotation.*;
import com.thoersch.seeds.*;
import com.thoersch.seeds.persistence.issues.*;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.resources.issues.IssuesResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    private IssuesRepository repo;

    private IssuesResource resource;

    @Before
    public void init() {
        this.resource = new IssuesResource(repo);
    }

    @Test
    public void testIfSummeryOfIssueIsGenerated() {
        final Issue issue = resource.getIssue(1);
        assertNotNull(issue.getDescription());
    }

    /**
     * TEST ID :
     *
     * Tests if the issue summery is less than 1000 characters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIfSummeryOfIssueIsNotLengthy() {
        final Issue issue = resource.getIssue(1);
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
        issue.setStatus(Issue.IssueStatus.COMPLETED);
        issue.addAssignee("Hello");
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
        issue.setStatus(Issue.IssueStatus.REJECTED);
        issue.addAssignee("Hello");
    }

    /**
     * TEST ID :
     *
     */
    @Test
    public void testAddingNewIssue() {
        final Issue issue = new Issue();
//        issue.setId(10L);
        issue.setTitle("New Testing issue");
        issue.setDescription("New Issue");
        issue.setStatus(Issue.IssueStatus.COMPLETED);
        resource.saveIssue(issue);
    }

    /**
     * TEST ID :
     *
     * Test if all the issues can be retrieved from the system
     */
    @Test
    public void testGettingAllIssues() {
        final List<Issue> issues = resource.getIssues();
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
        final List<Issue> issues = resource.getIssues("title");
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
        final List<Issue> issues = resource.getIssues("description");
        assertTrue(!issues.isEmpty());
        assertNotNull(issues.get(0));

        assertEquals("A_DESC_FIRST", issues.get(0).getDescription());
    }

    /**
     * TEST ID :
     *
     */
    @Test
    public void testIfNumberOfRelatedPostsAreGreaterThanZero() {
        final Issue issue = resource.getIssue(1);
        assertTrue(issue.getNumberOfRelatedIssues() > 0);
    }

}
