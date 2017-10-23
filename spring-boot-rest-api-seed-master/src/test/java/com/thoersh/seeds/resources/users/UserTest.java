package com.thoersh.seeds.resources.users;

import com.thoersch.seeds.persistence.users.UsersRepository;
import com.thoersch.seeds.resources.users.UsersResource;
import com.thoersch.seeds.representations.users.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = User.class)
@DatabaseSetup(ItemRepositoryIT.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ItemRepositoryIT.DATASET })
@DirtiesContext
public class UserTest {

    private UsersRepository usersRepository;
    private UsersResource usersResourceMock = new UsersResource(usersRepository);

    private User user;
    private User user2;

    @Before
    public void init() {
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
