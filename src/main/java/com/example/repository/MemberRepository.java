package com.example.repository;

import com.example.entity.MemberCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberCHG, String> {

}
