package org.kiworkshop.blind.user.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String pictureUrl;
    private boolean active;

    @Builder
    private User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void update(User userToUpdate) {
        this.id = userToUpdate.id;
        this.email = userToUpdate.email;
        this.password = userToUpdate.password;
        this.name = userToUpdate.name;
    }

    public boolean matchPassword(String encryptedPassword) {
        return password.equals(encryptedPassword);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (("admin").equals(this.email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getAuthority()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
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

    public boolean matchEmail(String email) {
        return this.email.equals(email);
    }
}

