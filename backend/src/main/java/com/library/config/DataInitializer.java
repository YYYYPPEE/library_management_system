package com.library.config;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BookRepository bookRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 初始化管理员账号
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        if (adminOpt.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setRole(1);
            admin.setStatus(0);
            userRepository.save(admin);
            System.out.println("管理员账号创建成功: admin / admin123");
        }

        // 初始化学生账号
        Optional<User> studentOpt = userRepository.findByUsername("student");
        if (studentOpt.isEmpty()) {
            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRealName("张三");
            student.setStudentNo("2024001");
            student.setRole(0);
            student.setStatus(0);
            userRepository.save(student);
            System.out.println("学生账号创建成功: student / student123");
        }

        // 初始化测试图书
        if (bookRepository.count() == 0) {
            Book book1 = new Book();
            book1.setTitle("Java编程思想");
            book1.setAuthor("Bruce Eckel");
            book1.setIsbn("9787111213826");
            book1.setTotalStock(5);
            book1.setAvailableStock(5);
            book1.setStatus(0);
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("Spring Boot实战");
            book2.setAuthor("Craig Walls");
            book2.setIsbn("9787115420452");
            book2.setTotalStock(3);
            book2.setAvailableStock(3);
            book2.setStatus(0);
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("深入理解计算机系统");
            book3.setAuthor("Randal E. Bryant");
            book3.setIsbn("9787111544937");
            book3.setTotalStock(2);
            book3.setAvailableStock(2);
            book3.setStatus(0);
            bookRepository.save(book3);

            Book book4 = new Book();
            book4.setTitle("算法导论");
            book4.setAuthor("Thomas H. Cormen");
            book4.setIsbn("9787111407010");
            book4.setTotalStock(4);
            book4.setAvailableStock(4);
            book4.setStatus(0);
            bookRepository.save(book4);

            Book book5 = new Book();
            book5.setTitle("设计模式：可复用面向对象软件的基础");
            book5.setAuthor("Erich Gamma");
            book5.setIsbn("9787111075752");
            book5.setTotalStock(3);
            book5.setAvailableStock(3);
            book5.setStatus(0);
            bookRepository.save(book5);

            System.out.println("测试图书初始化完成，共5本");
        }
    }
}
