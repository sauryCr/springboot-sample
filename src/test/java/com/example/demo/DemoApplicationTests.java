package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.demo.dao.UserMapper;
import com.example.demo.entiry.User;
import com.example.demo.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    @Test
    public void excelExport() {
        List<Person> people = new ArrayList<>();
        List<Person> people1 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            people.add(new Person(String.valueOf(i), "lijingyu" + i));
        }

        for (int i = 10; i < 20; i++) {
            people1.add(new Person(String.valueOf(i), "lijingyu" + i));
        }
		try (ExcelWriter excelWriter = EasyExcel.write("renlei.xlsx", Person.class).build()) {
			WriteSheet sheet = EasyExcel.writerSheet("商品详情").build();
			excelWriter.write(people, sheet);
			excelWriter.write(people1, sheet);
		}

    }

    @Data
    @AllArgsConstructor
    public static class Person {
        private String id;
        private String name;
    }

}
