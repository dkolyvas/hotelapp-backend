package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User getUserByUsername(String username);

    public User getUserById(Long id);

    @Query("select u from User  u where u.isEmployee = true ")
    public List<User> findAllEnterpriseUsers();

}
