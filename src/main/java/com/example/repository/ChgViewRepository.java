package com.example.repository;

import java.util.List;

import com.example.entity.ChallengeCHGView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChgViewRepository extends JpaRepository<ChallengeCHGView, Long> {

    // 인기순 챌린지 목록 ( 갯수 9개로 제한)
	@Query(
        value = " SELECT * FROM "
                + " (SELECT ROW_NUMBER() OVER (ORDER BY CHGNO ASC) "
                + " CHGNO, CHGLIKE, CHGLEVEL, CHGTITLE, "
                + " CHGINTRO, CHGCONTENT, CHGCNT, CHGFEE, "
                + " CHGSTART, CHGEND, " 
                + " RECRUITSTART, RECRUITEND, CHGREGDATE, "
                + " RECSTATE " 
                + " FROM "
                + " CHALLENGECHG) WHERE CHGLIKE BETWEEN 1 AND 9 ORDER BY CHGLIKE DESC", 
        nativeQuery = true
    )
    public List<ChallengeCHGView> selectLikeCHG(String challenge);


    // 난이도순 챌린지 목록 ( 갯수 9개로 제한)
	@Query(
        value = " SELECT * FROM "
                + " (SELECT ROW_NUMBER() OVER (ORDER BY CHGNO ASC) "
                + " CHGNO, CHGLIKE, CHGLEVEL, CHGTITLE, "
                + " CHGINTRO, CHGCONTENT, CHGCNT, CHGFEE, "
                + " CHGSTART, CHGEND, " 
                + " RECRUITSTART, RECRUITEND, CHGREGDATE, "
                + " RECSTATE " 
                + " FROM "
                + " CHALLENGECHG) WHERE CHGNO BETWEEN 1 AND 9 ORDER BY CHGLEVEL DESC", 
        nativeQuery = true
    )
    public List<ChallengeCHGView> selectLevelCHG(String challenge);
}
