package com.shiro.demo.service;

import com.shiro.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findByUsername(String username);
}
