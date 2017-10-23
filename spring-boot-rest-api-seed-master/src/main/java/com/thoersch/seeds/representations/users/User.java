package com.thoersch.seeds.representations.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoersch.seeds.persistence.converters.LocalDateTimeConverter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@javax.persistence.Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 100)
    @NotNull
    private String firstName;

    @Length(max = 100)
    @NotNull
    private String lastName;

    @Length(max = 255)
    @NotNull
    private String emailAddress;

    @Length(max = 100)
    @NotNull
    private String password;

    @Length(max = 100)
    @NotNull
    private String role;

    @Length(max = 255)
    @NotNull
    private String profilePicture;

    @JsonIgnore
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updated;

    public User() {

    }

    public User(String firstName, String lastName, String emailAddress, String profilePicture, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    @PreUpdate
    public void preUpdate() {
        setUpdated(LocalDateTime.now(ZoneOffset.UTC));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (emailAddress != null ? !emailAddress.equals(user.emailAddress) : user.emailAddress != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (profilePicture != null ? !profilePicture.equals(user.profilePicture) : user.profilePicture != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (profilePicture != null ? profilePicture.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
