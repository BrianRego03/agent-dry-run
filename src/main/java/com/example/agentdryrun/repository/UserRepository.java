package com.example.agentdryrun.repository;

import com.example.agentdryrun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

}
