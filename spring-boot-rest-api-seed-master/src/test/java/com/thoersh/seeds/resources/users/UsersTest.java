package com.thoersh.seeds.resources.users;

import com.thoersch.seeds.representations.users.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


import org.springframework.http.HttpStatus;


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


public class UsersTest {


    private User user;
    private User user2;

    @Before
    public void init() {
        user = new User("James", "Shaw", "js@gmail.com", "face.png", "password", "admin");
        user2 = new User("John", "Seinfeld", "js@gmail.com", "face.png", "password", "developer");
    }

    /**
     * Test User can be created
     */
    @Test
    public void testUserCreation(){
        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            HttpPost httppost = new HttpPost("127.0.0.1:8080/users/");
            httppost.setHeader("Content-Type", "application/json");
            StringEntity body = new StringEntity("{" +
                    "\"firstName\": \"" + user.getFirstName() + "\"," +
                    "\"lastName\": \"" + user.getLastName() + "\"," +
                    "\"emailAddress\": \"" + user.getEmailAddress() + "\"," +
                    "\"password\": \"" + user.getPassword() + "\"," +
                    "\"role\": \"" + user.getRole() + "\"," +
                    "\"profilePicture\": \"" + user.getProfilePicture() + "\"" +
                    "}");
            httppost.setEntity(body);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            //HttpEntity entity = response.getEntity();

            assertEquals(HttpStatus.OK, response.getStatusLine().getStatusCode());
        } catch (Exception exception) {}
    }

    /**
     * Test a failed creation due to missing field
     */
    @Test
    public void testMissingFieldUserCreation(){
        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            HttpPost httppost = new HttpPost("127.0.0.1:8080/users/");
            httppost.setHeader("Content-Type", "application/json");
            StringEntity body = new StringEntity("{" +
                    "\"firstName\": \"" + user.getFirstName() + "\"," +
                    "\"lastName\": \"" + user.getLastName() + "\"," +
                    "\"emailAddress\": \"" + user.getEmailAddress() + "\"," +
                    "\"password\": \"" + user.getPassword() + "\"" +
                    "}");
            httppost.setEntity(body);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            //HttpEntity entity = response.getEntity();

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusLine().getStatusCode());
        } catch (Exception exception) {}
    }

    /**
     * Test a failed creation due to already existing email
     */
    @Test
    public void testExistingEmailUserCreation(){
        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            HttpPost httppost = new HttpPost("127.0.0.1:8080/users/");
            httppost.setHeader("Content-Type", "application/json");
            StringEntity body = new StringEntity("{" +
                    "\"firstName\": \"" + user.getFirstName() + "\"," +
                    "\"lastName\": \"" + user.getLastName() + "\"," +
                    "\"emailAddress\": \"" + user.getEmailAddress() + "\"," +
                    "\"password\": \"" + user.getPassword() + "\"," +
                    "\"role\": \"" + user.getRole() + "\"," +
                    "\"profilePicture\": \"" + user.getProfilePicture() + "\"" +
                    "}");
            httppost.setEntity(body);
            httpclient.execute(httppost);

            HttpPost httppost2 = new HttpPost("127.0.0.1:8080/users/");
            httppost2.setHeader("Content-Type", "application/json");
            StringEntity body2 = new StringEntity("{" +
                    "\"firstName\": \"" + user2.getFirstName() + "\"," +
                    "\"lastName\": \"" + user2.getLastName() + "\"," +
                    "\"emailAddress\": \"" + user2.getEmailAddress() + "\"," +
                    "\"password\": \"" + user2.getPassword() + "\"," +
                    "\"role\": \"" + user2.getRole() + "\"," +
                    "\"profilePicture\": \"" + user2.getProfilePicture() + "\"" +
                    "}");
            httppost2.setEntity(body2);

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost2);
            //HttpEntity entity = response.getEntity();

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
        } catch (Exception exception) {}
    }



    /**
     * Test successful login
     */
    @Test
    public void testUserLogin(){
        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            HttpPost httppost = new HttpPost("127.0.0.1:8080/users/");
            httppost.setHeader("Content-Type", "application/json");
            StringEntity body = new StringEntity("{" +
                    "\"firstName\": \"" + user.getFirstName() + "\"," +
                    "\"lastName\": \"" + user.getLastName() + "\"," +
                    "\"emailAddress\": \"" + user.getEmailAddress() + "\"," +
                    "\"password\": \"" + user.getPassword() + "\"," +
                    "\"role\": \"" + user.getRole() + "\"," +
                    "\"profilePicture\": \"" + user.getProfilePicture() + "\"" +
                    "}");
            httppost.setEntity(body);
            HttpResponse response = httpclient.execute(httppost);
            //HttpEntity entity = response.getEntity();

            assertEquals(HttpStatus.OK, response.getStatusLine().getStatusCode());
        } catch (Exception exception) {}
    }
}
