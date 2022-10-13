package com.example.demo.controller;

import com.example.demo.dao.UserMapper;
import com.example.demo.entiry.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping(value = "user")
    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")})
    public User getUser() {
            List<User> ret = userMapper.findAll();
            return ret.isEmpty() ? new User(1L, "1", "2") : ret.get(0);
    }

    public User fallBack(Throwable throwable) {
        System.out.println(throwable.getCause());
        return new User(1L, "2", "3");
    }
}
