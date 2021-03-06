package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.CommunityCHG;
import com.example.entity.CommunityCHGProjection;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityCHG, Long> {

        List<CommunityCHGProjection> findByMemberchg_memailAndBtitleContainingOrderByBnoDesc(String memail,
                        String btitle,
                        Pageable page);

        long countByMemberchg_memailAndBtitleContaining(String memail, String btitle);

        long countByBtitleContaining(String btitle);

        // CommunityCHG findByBno(Long bno);

        CommunityCHGProjection findByBno(Long bno);

        // 이전글 ex)20 이면 작은것중에서 가장 큰것 1개 19...
        CommunityCHGProjection findTop1ByBnoLessThanOrderByBnoDesc(long bno);

        // 다음글 ex)20이면 큰것중에서 가장 작은것 1개 21...
        CommunityCHGProjection findTop1ByBnoGreaterThanOrderByBnoAsc(long bno);

        CommunityCHG findByMemberchg_memailAndBnoEqualsOrderByBnoDesc(String memail, long bno);

        // findBy 컬럼명 ContainingOrderBy컬럼명Desc
        // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
        // WHERE BTITLE LIKE '%' || '검색어' || '%'
        List<CommunityCHGProjection> findByBtitleContainingOrderByBnoDesc(
                        String btitle, Pageable page);

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE COMMUNITYCHG SET BHIT=BHIT+1 WHERE BNO=:bno", nativeQuery = true)
        public int updateBoardHitOne(@Param(value = "bno") long bno);

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "DELETE FROM COMMUNITYCHG WHERE BNO =:bno", nativeQuery = true)
        public int deleteBoardOne(@Param(value = "bno") long bno);

}
