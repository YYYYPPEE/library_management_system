package com.library.controller;

import com.library.dto.Result;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发测试控制器 - 仅在dev_test分支使用
 * 不会影响其他分支的核心功能
 */
@RestController
@RequestMapping("/api/dev-test")
public class DevTestController {

    /**
     * 基础测试接口
     */
    @GetMapping("/hello")
    public Result<Map<String, Object>> hello() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello from dev_test branch!");
        data.put("timestamp", LocalDateTime.now());
        data.put("branch", "dev_test");
        return Result.success(data);
    }

    /**
     * 系统状态检查
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "running");
        data.put("branch", "dev_test");
        data.put("timestamp", LocalDateTime.now());
        data.put("javaVersion", System.getProperty("java.version"));
        return Result.success(data);
    }

    /**
     * 回显测试
     */
    @PostMapping("/echo")
    public Result<Map<String, Object>> echo(@RequestBody Map<String, Object> input) {
        Map<String, Object> data = new HashMap<>();
        data.put("received", input);
        data.put("timestamp", LocalDateTime.now());
        data.put("branch", "dev_test");
        return Result.success(data);
    }
}
