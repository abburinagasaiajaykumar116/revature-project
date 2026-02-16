package org.example.revshop.service.impl;


import org.example.revshop.model.User;
import org.example.revshop.repos.UserRepository;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
    public class UserServiceImpl implements UserService {

        @Autowired
        private UserRepository repo;

        @Autowired
        private PasswordEncoder encoder;

        public void register(User user) {

            if (user.getEmail() == null || user.getEmail().isEmpty())
                throw new RuntimeException("Email cannot be empty");

            if (user.getPassword().length() < 4)
                throw new RuntimeException("Weak password");

            user.setPassword(encoder.encode(user.getPassword()));

            repo.save(user);
        }
    @Override
    public User getByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User login(String email, String password) {

            User user = repo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!encoder.matches(password, user.getPassword()))
                throw new RuntimeException("Invalid credentials");



        return user;
        }

        public void changePassword(Long userId, String newPassword) {

            User user = repo.findById(userId).orElseThrow();
            user.setPassword(encoder.encode(newPassword));
            repo.save(user);
        }

        public void forgotPassword(String email, String answer, String newPassword) {

            User user = repo.findByEmail(email).orElseThrow();

            if (!user.getSecurityAnswer().equals(answer))
                throw new RuntimeException("Wrong answer");

            user.setPassword(encoder.encode(newPassword));
            repo.save(user);
        }

        public String getSecurityQuestion(String email) {
            return repo.findByEmail(email)
                    .map(User::getSecurityQuestion)
                    .orElse(null);
        }
    }



