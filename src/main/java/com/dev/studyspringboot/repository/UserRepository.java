package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void softDeleteById(@Param("userId") Long userId);

    List<User> findAllByDeletedAtIsNull();
    User findByIdAndDeletedAtIsNull(Long userId);

    @Query("SELECT count (u) > 0 FROM User u WHERE u.username = :username OR u.email = :email")
    boolean existsByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("SELECT u " +
            "FROM User u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role r " +
            "WHERE u.username = :username AND u.deletedAt is NULL")
    Optional<User> findUserAndRoleToLogin(String username);
}
