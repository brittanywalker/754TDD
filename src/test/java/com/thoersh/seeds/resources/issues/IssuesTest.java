package com.thoersh.seeds.resources.issues;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.thoersch.seeds.Application;
import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.representations.issues.Issue;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

   // @Autowired
    //private IssuesRepository repo;

    private Issue issue;

    @Before
    public void init() {
        issue = new Issue();
        issue.setIssueDetails("Some details");
        issue.setIssueStatus(Issue.IssueStatus.PENDING);
        issue.setNumberOfRelatedIssues(3);
    }

    @Test
    public void testIfSummeryOfIssueIsGenerated() {
        assertNotNull(issue.getIssueDetails());
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
//        resource.saveIssue(issue);
//    }

    /**
     * TEST ID :
     *
     * Test if all the issues can be retrieved from the system
     */
    //@Test
//    public void testGettingAllIssues() {
//        final List<Issue> issues = resource.getIssues(null);
//        assertTrue(!issues.isEmpty());
//        assertNotNull(issues.get(0));
//
//        final Issue issue = issues.get(0);
//        //assertEquals(1, issue.getId());
//    }

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

    @Test
    public void testIfNumberOfRelatedPostsAreGreaterThanZero() {
        assertTrue(issue.getNumberOfRelatedIssues() > 0);
    }


}
