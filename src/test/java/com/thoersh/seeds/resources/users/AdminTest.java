package com.thoersh.seeds.resources.users;

import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.representations.users.UserLogin;
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

//    private Cluster cluster;
//    private ForumPost forumPost;
//    private ForumPostRepository forumPostRepository;
//    private ForumPostResource forumPostResource;

    private UsersResource usersResourceMock;
    private User user;
    private User userAdmin;
    private UserLogin userLogin;
    private UserLogin userAdminLogin;
    private UserLogin incorrectLogin;

    @Before
    public void init() {
        this.usersResourceMock = new UsersResource(usersRepository);

//        this.forumPostRepository = new ForumPostResource(forumPostRepository);


        user = new User("James", "Shaw", "js@gmail.com", "face.png", "password", User.UserRole.admin);
        userAdmin = new User("John", "Seinfeld", "js@gmail.com", "face.png", "password", User.UserRole.developer);

        userLogin = new UserLogin("hj@gmail.com", "q");
        userAdminLogin = new UserLogin("bw@gmail.com", "q");
        incorrectLogin = new UserLogin("hj@gmail.com", "qw");
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


}