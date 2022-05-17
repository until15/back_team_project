package com.example.repository;

import com.example.entity.PayCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<PayCHG, String> {

}
