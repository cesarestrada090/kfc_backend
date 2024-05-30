package com.kfc.app.dto;

import com.kfc.app.entities.User;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link User}
 */
public class UserDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    private String username;
    @Size(max = 45)
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PersonDto person;
    public UserDto() {
    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.person = new PersonDto(user.getPerson());
    }
    

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public boolean hasDifferentUserName(String username){
        return this.getUsername()!= null && !this.getUsername().equals(username);
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }
}