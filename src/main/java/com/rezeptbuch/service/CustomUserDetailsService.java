package com.rezeptbuch.service;

import com.rezeptbuch.model.Role;
import com.rezeptbuch.model.RoleRepository;
import com.rezeptbuch.model.User;
import com.rezeptbuch.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toSet())
        );
    }
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void createUser(String username, String password, String roleName) {
        if (userExists(username)) {
            throw new IllegalArgumentException("Benutzername ist bereits vergeben!");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });

        User newUser = new User();
        newUser.setUsername(username.toLowerCase());
        newUser.setPassword(password);
        newUser.setRoles(Set.of(role));

        userRepository.save(newUser);
    }

}

