package com.example.repository;

import com.example.entity.ChatCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatCHG, Integer> {
    //List<ChatCHG> findBySendMemberAndReceiveMember(int smember, int rmember);
    
}
