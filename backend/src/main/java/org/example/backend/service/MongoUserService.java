package org.example.backend.service;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.MongoUserDto;
import org.example.backend.model.MongoUserModel;
import org.example.backend.repository.MongoUserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MongoUserService implements UserDetailsService {

    private final MongoUserRepo repo;

    private Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MongoUserModel user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not Found!"));
        return new User(user.username(), user.password(), Collections.emptyList());
    }

    public void registerNewUser(MongoUserDto newUser){
        MongoUserModel user = MongoUserModel.builder()
                .id(UUID.randomUUID().toString())
                .username(newUser.username())
                .password(encoder.encode(newUser.password()))
                .email(newUser.email())
                .build();
        repo.save(user);
    }

}
