package com.arjun.crm.repository;

import com.arjun.crm.entity.TaskWatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskWatcherRepository extends JpaRepository<TaskWatcher, Long> {
    
    boolean existsByTaskIdAndUserId(Long taskId, Long userId);
    
    Optional<TaskWatcher> findByTaskIdAndUserId(Long taskId, Long userId);
    
    List<TaskWatcher> findByTaskIdOrderByWatchedAtDesc(Long taskId);
    
    void deleteByTaskIdAndUserId(Long taskId, Long userId);
}
