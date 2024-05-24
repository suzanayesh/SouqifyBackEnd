package com.code.auth.repo;

import com.code.auth.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromUserAndToUserOrderByTimestampAsc(String fromUser, String toUser);
    List<Message> findByToUserAndFromUserOrderByTimestampAsc(String toUser, String fromUser);

    @Query("SELECT DISTINCT m.toUser FROM Message m WHERE m.fromUser = :username UNION SELECT DISTINCT m.fromUser FROM Message m WHERE m.toUser = :username")
    List<String> findAllChatPartners(@Param("username") String username);
}
