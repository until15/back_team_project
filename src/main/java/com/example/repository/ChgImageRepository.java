package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.example.entity.CHGImgView;

@Repository
public interface ChgImageRepository extends JpaRepository<CHGImgView, Long> {

	@Nullable
	public CHGImgView findByChgno(long chgno);
	
}
