package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.UserRepository;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    UserRepository repository;
    @Autowired
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("The user wasn't found");
        String role = user.getIsEmployee()?"Employee":"Customer";

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(role).build();

    }
}
