package com.example.repository;

import com.example.entity.BimgCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BimgRepository extends JpaRepository<BimgCHG, Long> {

}
