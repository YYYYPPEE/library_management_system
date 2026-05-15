package com.library.controller;

import com.library.dto.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        return Result.success("这是公开接口，任何人都可以访问");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public Result<String> userEndpoint(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success("用户接口，用户ID: " + userId + " 可以访问");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> adminEndpoint() {
        return Result.success("管理员接口，只有管理员可以访问");
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<String> studentEndpoint() {
        return Result.success("学生接口，只有学生可以访问");
    }
}
