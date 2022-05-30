package com.example.repository;

import java.util.List;

import com.example.entity.PoseVideoVIEW;
import com.example.entity.VideoCHG;
import com.example.entity.VideoCHGProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoCHG, Long> {

    List<VideoCHGProjection> findByPosechg_pnoOrderByVnoDesc(long pno);

    // @Query(value = "SELECT * FROM POSEVIDEO_VIEW where pno =:pno", nativeQuery=true)
    VideoCHG findByPosechg_pnoEquals(long pno);

}
