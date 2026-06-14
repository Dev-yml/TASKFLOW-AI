package com.arjun.crm.repository;

import com.arjun.crm.entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
    
    List<Mention> findByCommentId(Long commentId);
    
    List<Mention> findByMentionedUserId(Long userId);
}
