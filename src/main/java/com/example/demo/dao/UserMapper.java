package com.example.demo.dao;


import com.example.demo.entiry.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMapper extends JpaRepository<User, Long> {
}
