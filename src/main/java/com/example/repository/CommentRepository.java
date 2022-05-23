package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.CommentCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentCHG, Long> {

    List<CommentCHG> findByCommunitychg_bnoOrderByCmtnoDesc(long bno);

    List<CommentCHG> findBycmtnoOrderByCmtnoDesc(long bno);

    CommentCHG findByMemberchg_memailContaining(String memail);

    CommentCHG findByMemberchg_memailAndCmtnoEqualsOrderByCmtnoDesc(String memail, long cmtno);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM COMMUNITYCHG WHERE CMTNO =:cmtno", nativeQuery = true)
    public int deleteBoardOne(@Param(value = "cmtno") long cmtno);
}
