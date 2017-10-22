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

    @Before
    public void init() {
        user = new User("James", "Shaw", "js@gmail.com", "face.png", "password", "admin");
    }

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
            HttpEntity entity = response.getEntity();

            assertEquals(HttpStatus.OK, response.getStatusLine().getStatusCode());


        } catch (Exception exception) {

        }

    }
}
