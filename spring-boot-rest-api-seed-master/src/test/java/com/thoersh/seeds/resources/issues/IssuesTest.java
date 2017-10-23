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
import org.springframework.transaction.annotation.*;

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

    protected static final String DATA_SET = "classpath:datasets/issue-items.xml";

    @Autowired
    private IssuesRepository repo;

    private IssuesResource resource;

    private Issue issue;

    @Before
    public void init() {
        this.resource = new IssuesResource(repo);

        issue = new Issue();
        issue.setDescription("Some details");
        issue.setStatus(Issue.IssueStatus.PENDING);
    }

    @Test
    public void testIfSummeryOfIssueIsGenerated() {
        assertNotNull(issue.getDescription());
    }

    /**
     * TEST ID :
     *
     * Tests if the issue summery is less than 1000 characters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIfSummeryOfIssueIsNotLengthy() {
        //issue = resource.getIssue(1);
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
    public void testAddAssigneeWhenIssueIsRejectd() {
        issue.setStatus(Issue.IssueStatus.REJECTED);
        issue.addAssignee("Hello");
    }

    @Test
    public void testIfNumberOfRelatedPostsAreGreaterThanZero() {
        assertTrue(issue.getNumberOfRelatedIssues() > 0);
    }


}
