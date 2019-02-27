package com.tasklist.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "User")
@Table(name="users")
public class User implements UserDetails {

    @Id
    private String username;
    private String password;
    private String name;
    private String email;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = { CascadeType.ALL})
    @JoinTable(
            name = "users_task_list",
            joinColumns = { @JoinColumn(name="users_username")},
            inverseJoinColumns = { @JoinColumn(name = "tasklist_id")}
    )
    private Set<TaskList> tasklists = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public User() {
    }

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<TaskList> getTasklists() {
        return tasklists;
    }

    public void setTasklists(Set<TaskList> tasklists) {
        this.tasklists = tasklists;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tasklists=" + tasklists.toString() +
                '}';
    }
}
