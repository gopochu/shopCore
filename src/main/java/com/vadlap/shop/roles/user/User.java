package com.vadlap.shop.roles.user;

import com.vadlap.shop.roles.Role;
import com.vadlap.shop.roles.RoleDetails;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Table(name = "users")
public class User implements RoleDetails, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullname;
    private String email;
    private boolean isAccountLocked;

    @Override
    public boolean getIsAccountLocked() {
        return isAccountLocked;
    }

    public void setIsAccountLocked(boolean isLocked) {
        isAccountLocked = isLocked;
    }

    // Можно потом добавить Enum Role
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }
}
