package com.arjun.crm.repository;

import com.arjun.crm.entity.User;
import com.arjun.crm.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query("""
            select u from User u
            where u.status = :status
              and u.id <> :currentUserId
              and (
                lower(u.fullName) like lower(concat('%', :query, '%'))
                or lower(u.email) like lower(concat('%', :query, '%'))
              )
            order by u.fullName asc
            """)
    Page<User> searchActiveUsers(String query, UserStatus status, Long currentUserId, Pageable pageable);
}
