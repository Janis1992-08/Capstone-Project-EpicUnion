package org.example.backend.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.MongoUserDto;
import org.example.backend.service.MongoUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MongoUserController {
    private final MongoUserService service;

    @GetMapping
    public String getMe(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/login")
    public String login(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/register")
    public void register(@RequestBody MongoUserDto newUser){
        service.registerNewUser(newUser);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
        SecurityContextHolder.clearContext();
    }



}
