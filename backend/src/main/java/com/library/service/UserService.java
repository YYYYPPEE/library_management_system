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
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
