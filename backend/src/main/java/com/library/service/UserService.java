package com.library.service;

import com.library.dto.LoginRequest;
import com.library.dto.LoginResponse;
import com.library.dto.RegisterRequest;
import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginAttemptService loginAttemptService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setStudentNo(request.getStudentNo());
        user.setRole(request.getRole());
        user.setStatus(1);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();

        if (loginAttemptService.isLocked(username)) {
            long lockSeconds = loginAttemptService.getLockRemainingSeconds(username);
            long minutes = lockSeconds / 60;
            long seconds = lockSeconds % 60;
            throw new RuntimeException("账号已被锁定，请 " + minutes + " 分 " + seconds + " 秒后重试");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    loginAttemptService.loginFailed(username);
                    int remaining = loginAttemptService.getRemainingAttempts(username);
                    if (remaining > 0) {
                        return new RuntimeException("用户名或密码错误，还剩 " + remaining + " 次尝试机会");
                    } else {
                        return new RuntimeException("登录失败次数过多，账号已锁定 30 分钟");
                    }
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            loginAttemptService.loginFailed(username);
            int remaining = loginAttemptService.getRemainingAttempts(username);
            if (remaining > 0) {
                throw new RuntimeException("用户名或密码错误，还剩 " + remaining + " 次尝试机会");
            } else {
                throw new RuntimeException("登录失败次数过多，账号已锁定 30 分钟");
            }
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        loginAttemptService.loginSucceeded(username);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
