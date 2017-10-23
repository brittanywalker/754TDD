package com.thoersh.seeds.resources.users;

import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.resources.users.UsersResource;
import com.thoersch.seeds.representations.users.User;

import com.github.springtestdbunit.*;
import com.github.springtestdbunit.annotation.*;
import com.thoersch.seeds.*;
import com.thoersch.seeds.persistence.issues.*;
import com.thoersch.seeds.representations.issues.Issue;
import com.thoersch.seeds.resources.issues.IssuesResource;
import com.thoersh.seeds.resources.issues.IssuesTest;
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

import static org.junit.Assert.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(UserTest.DATA_SET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UserTest.DATA_SET })
@DirtiesContext
public class UserTest {

    protected static final String DATA_SET = "classpath:user-items.xml";


    @Autowired
    private UsersRepository usersRepository;

    private UsersResource usersResourceMock;
    private User user;
    private User user2;

    @Before
    public void init() {
        this.usersResourceMock = new UsersResource(usersRepository);

        user = new User("James", "Shaw", "js@gmail.com", "face.png", "password", "admin");
        user2 = new User("John", "Seinfeld", "js@gmail.com", "face.png", "password", "developer");
    }

    @Test
    public void testUserRegistration(){
        usersResourceMock.saveUser(user);
        User actual = usersResourceMock.getUser("js@gmail.com");
        assertEquals(user, actual);
    }


}
