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
@DatabaseSetup(UserTest.DATA_SET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UserTest.DATA_SET })
@DirtiesContext
public class UserTest {

    protected static final String DATA_SET = "classpath:user-items.xml";


    @Autowired
    private UsersRepository usersRepository;

    private UsersResource usersResourceMock;
    private User user;
    private User userAdmin;
    private UserLogin userLogin;
    private UserLogin userAdminLogin;
    private UserLogin incorrectLogin;

    @Before
    public void init() {
        this.usersResourceMock = new UsersResource(usersRepository);

        user = new User("James", "Shaw", "js@gmail.com", "face.png", "password", User.UserRole.admin);
        userAdmin = new User("John", "Seinfeld", "js@gmail.com", "face.png", "password", User.UserRole.developer);

        userLogin = new UserLogin("hj@gmail.com", "q");
        userAdminLogin = new UserLogin("bw@gmail.com", "q");
        incorrectLogin = new UserLogin("hj@gmail.com", "qw");
    }

    /**
     * TEST ID: 2.1.1
     *
     * Tests a user registration worked
     */
    @Test
    public void successfulUserRegistrationTest(){
        usersResourceMock.saveUser(user);
        User actual = usersResourceMock.getUser("js@gmail.com");
        assertEquals(user, actual);
    }

    /**
     * TEST ID: 2.1.2
     *
     * Tests for a user registration where the email being registered is already registered into the database.
     */
    @Test
    public void userRegistrationFailureDueToExistingRegisteredEmailTest(){
        usersResourceMock.saveUser(user);
        try{
            usersResourceMock.saveUser(userAdmin);
        }catch (DataIntegrityViolationException exception){
            return;
        }
        Assert.fail("User registration should have failed");
    }

    /**
     * TEST ID: 3.1.1
     *
     * Test for a successful developer login
     */
    @Test
    public void successfulDeveloperUserLoginTest(){
        ResponseEntity actual = usersResourceMock.authUser(userLogin);
        ResponseEntity<String> expected = new ResponseEntity<>("UserID: 1, Role: developer", HttpStatus.ACCEPTED);
        assertEquals(expected, actual);
    }

    /**
     * TEST ID: 3.1.2 / 3.2.2
     *
     * Test for unsuccessful login of a non-specific user using the wrong login credentials
     */
    @Test
    public void unsuccessfulGeneralUserLoginWithTheWrongCredentialsTest(){
        ResponseEntity actual = usersResourceMock.authUser(incorrectLogin);
        ResponseEntity<String> expected = new ResponseEntity<>("Unauthorised", HttpStatus.UNAUTHORIZED);
        assertEquals(expected, actual);
    }

    /**
     * TEST ID: 3.2.1
     *
     * Test for successful admin login
     */
    @Test
    public void successfulAdminUserLoginTest() {
        ResponseEntity actual = usersResourceMock.authUser(userAdminLogin);
        ResponseEntity<String> expected = new ResponseEntity<>("UserID: 2, Role: admin", HttpStatus.ACCEPTED);
        assertEquals(expected, actual);
    }


}
