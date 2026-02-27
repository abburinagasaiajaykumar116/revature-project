package org.example.revshop.serviceimpltest;


import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.exception.UnauthorizedException;
import org.example.revshop.model.User;
import org.example.revshop.repos.UserRepository;
import org.example.revshop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    // ✅ REGISTER SUCCESS
    @Test
    void testRegister_Success() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setPassword("1234");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.empty());
        when(encoder.encode("1234"))
                .thenReturn("encoded1234");

        userService.register(user);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(repo).save(captor.capture());

        assertEquals("encoded1234",
                captor.getValue().getPassword());
    }

    // ❌ REGISTER DUPLICATE EMAIL
    @Test
    void testRegister_DuplicateEmail() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setPassword("1234");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> userService.register(user));
    }

    // ❌ REGISTER INVALID PASSWORD
    @Test
    void testRegister_InvalidPassword() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setPassword("12");

        assertThrows(BadRequestException.class,
                () -> userService.register(user));
    }

    // ✅ LOGIN SUCCESS
    @Test
    void testLogin_Success() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setPassword("encoded1234");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("1234", "encoded1234"))
                .thenReturn(true);

        User result = userService.login("ajay@test.com", "1234");

        assertEquals(user, result);
    }

    // ❌ LOGIN WRONG PASSWORD
    @Test
    void testLogin_WrongPassword() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setPassword("encoded1234");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("wrong", "encoded1234"))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> userService.login("ajay@test.com", "wrong"));
    }

    // ❌ LOGIN EMAIL NOT FOUND
    @Test
    void testLogin_EmailNotFound() {

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> userService.login("ajay@test.com", "1234"));
    }

    // ✅ CHANGE PASSWORD SUCCESS
    @Test
    void testChangePassword_Success() {

        User user = new User();
        user.setUserId(1);
        user.setPassword("old");

        when(repo.findById(1))
                .thenReturn(Optional.of(user));
        when(encoder.encode("newpass"))
                .thenReturn("encodedNew");

        userService.changePassword(1, "newpass");

        assertEquals("encodedNew", user.getPassword());
        verify(repo).save(user);
    }

    // ❌ CHANGE PASSWORD USER NOT FOUND
    @Test
    void testChangePassword_UserNotFound() {

        when(repo.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.changePassword(1, "newpass"));
    }

    // ✅ FORGOT PASSWORD SUCCESS
    @Test
    void testForgotPassword_Success() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setSecurityAnswer("blue");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));
        when(encoder.encode("newpass"))
                .thenReturn("encodedNew");

        userService.forgotPassword("ajay@test.com",
                "blue", "newpass");

        assertEquals("encodedNew", user.getPassword());
        verify(repo).save(user);
    }

    // ❌ FORGOT PASSWORD WRONG ANSWER
    @Test
    void testForgotPassword_WrongAnswer() {

        User user = new User();
        user.setEmail("ajay@test.com");
        user.setSecurityAnswer("blue");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class,
                () -> userService.forgotPassword(
                        "ajay@test.com",
                        "red",
                        "newpass"));
    }

    // ✅ GET SECURITY QUESTION
    @Test
    void testGetSecurityQuestion_Success() {

        User user = new User();
        user.setSecurityQuestion("Your favorite color?");

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.of(user));

        String question =
                userService.getSecurityQuestion("ajay@test.com");

        assertEquals("Your favorite color?", question);
    }

    // ❌ GET SECURITY QUESTION USER NOT FOUND
    @Test
    void testGetSecurityQuestion_NotFound() {

        when(repo.findByEmail("ajay@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getSecurityQuestion("ajay@test.com"));
    }
}