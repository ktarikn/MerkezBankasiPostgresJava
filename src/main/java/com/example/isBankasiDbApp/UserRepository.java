package com.example.isBankasiDbApp;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    com.example.isBankasiDbApp.User findByUsername(String username);
}
