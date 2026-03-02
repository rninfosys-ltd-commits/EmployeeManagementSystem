package com.schoolapp.repository;

import com.schoolapp.entity.UserRoleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRoleDetailsRepository extends JpaRepository<UserRoleDetails, Long> {
    Optional<UserRoleDetails> findByUsername(String username);
}
