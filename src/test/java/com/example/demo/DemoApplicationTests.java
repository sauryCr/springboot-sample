package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.entiry.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private UserMapper userMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testUserGet() {
		List<User> ret = userMapper.findAll();
		System.out.println(ret.isEmpty() ? new User(1L, "1", "2") : ret.get(0));
	}

}
