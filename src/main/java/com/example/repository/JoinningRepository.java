package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinCHGView;

@Repository
public interface JoinningRepository extends JpaRepository<JoinCHGView, Long>{

	// 진행 중인 첼린지 조회 ( 갯수 9개로 제한)
	@Query(value = "SELECT * FROM "
			+ " (SELECT "
			+ "  J.*, ROW_NUMBER() OVER (ORDER BY JNO ASC) ROWN "
			+ "FROM "
			+ " JOINCHG_VIEW J "
			+ " WHERE "
			+ "MEMAIL=:em AND CHGSTATE=:state) WHERE ROWN BETWEEN 1 AND 5", nativeQuery = true)
	public List<JoinCHGView> selectJoinningCHG(@Param("em") String memail, @Param("state") long state);
	
}
