package com.example.repository;

import java.util.List;

import com.example.entity.IqcommentCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IqcommentRepository extends JpaRepository<IqcommentCHG, Long> {

    List<IqcommentCHG> findByInquirychg_qnoOrderByIqcmtnoDesc(long qno);

}
