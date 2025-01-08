package com.example.pastebin.services;

import com.example.pastebin.pojo.User;
import com.example.pastebin.repositories.UserRepository;
import com.example.pastebin.util.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findOneName(String name) {return userRepository.findByName(name);}

    public User findOneByName(String name) {
        Optional<User> foundSensor = userRepository.findByName(name);
        return foundSensor.orElseThrow(UserNotFound::new);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User findOneNameIdOrThrow(String name) {
        return userRepository.findByName(name).orElseThrow(UserNotFound::new);
    }
}
