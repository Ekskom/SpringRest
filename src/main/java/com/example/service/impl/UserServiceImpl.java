package com.example.service.impl;


import com.example.model.Role;
import com.example.model.Status;
import com.example.model.User;
import com.example.repo.RoleRepo;
import com.example.repo.UserRepo;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepo.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepo.save(user);

        log.info("Register user - user: {} registered", registeredUser);
        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepo.findAll();

        log.info("In getAll - {} users", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepo.findByUsername(username);

        log.info("In findByUsername - user: {} fonded: ", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepo.findById(id).orElse(null);

        if(result == null){
            log.warn("In findById - not found user by id: {}", id);
            return null;
        }

        log.info("In findByUsername - user: {} fonded: ", result, id);
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);

        log.info("User deleted - id: {}", id);
    }
}
