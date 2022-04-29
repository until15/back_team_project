package com.example.repository;

import java.util.List;

import com.example.entity.CommentCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentCHG, Long> {

    List<CommentCHG> findByCommunitychg_bnoOrderByCmtnoDesc(long bno);

}
