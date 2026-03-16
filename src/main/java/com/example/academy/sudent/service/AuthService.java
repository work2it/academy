package com.example.academy.sudent.service;

import com.example.academy.student.entity.StudentEntity;
import com.example.academy.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthService implements UserDetailsService {
    protected final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findByEmail(email);
        if (studentEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with email not found");
        }
        StudentEntity student = studentEntityOptional.get();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        return new User(student.getEmail(), student.getPassword(), authorities);
    }
}
