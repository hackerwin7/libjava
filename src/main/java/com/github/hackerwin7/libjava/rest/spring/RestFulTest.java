package com.github.hackerwin7.libjava.rest.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/15
 * Time: 11:31 AM
 * Desc:
 */
@SpringBootApplication
public class RestFulTest {
    public static void main(String[] args) {
        SpringApplication.run(RestFulTest.class, args);
    }
}

class User {

    private String name;
    private String val;

    public User(String n, String v) {
        name = n;
        val = v;
    }

    public String getName() {
        return name;
    }

    public String getVal() {
        return val;
    }
}

@RestController
@RequestMapping("/rest/test")
class UserController {
    @RequestMapping("/usr")
    public User getUsr(@RequestParam(value = "name", defaultValue = "root") String name,
                       @RequestParam(value = "code", defaultValue = "001") String code) {
        return new User(name, code);
    }
}