package com.thoersh.seeds.resources.issues;

import com.thoersch.seeds.persistence.issues.IssuesRepository;
import com.thoersch.seeds.representations.issues.Issue;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
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

    @Test
    public void testIfSummeryOfIssueIsNotLengthy() {
        assertNotNull(issue.getIssueDetails());
        assertTrue(issue.getIssueDetails().length() > 0);
        assertTrue(issue.getIssueDetails().length() < 1000);
    }

    @Test
    public void testIfNumberOfRelatedPostsAreGreaterThanZero() {
        assertTrue(issue.getNumberOfRelatedIssues() > 0);
    }


}
