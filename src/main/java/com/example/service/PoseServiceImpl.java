package com.example.service;

import java.util.List;

import com.example.entity.PoseCHG;
import com.example.entity.VideoCHG;
import com.example.repository.PoseRepository;
import com.example.repository.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PoseServiceImpl implements PoseService{

    @Autowired PoseRepository pRepository;
    @Autowired VideoRepository vRepository;

    // 자세등록
    @Override
    public long poseInsert(PoseCHG pose) {
        try {
            PoseCHG pose1 =  pRepository.save(pose);

            return pose1.getPno();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 자세 1개 조회
    @Override
    public PoseCHG poseSelectOne(Long pno) {
        try {
            PoseCHG pose = pRepository.findById(pno).orElse(null);
            return pose;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 자세수정
    @Override
    public int poseUpdate(PoseCHG pose) {
        try {
            pRepository.save(pose);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 자세삭제(수정)
    @Override
    public int poseDelete(PoseCHG pose) {
        try {
            pRepository.save(pose);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 자세목록 (페이지네이션, 검색)
    @Override
    public List<PoseCHG> poseSelectList(int step, Pageable page, String title) {
        try {
            List<PoseCHG> list = pRepository.findByPstepEqualsAndPnameContainingOrderByPnoDesc(step, title, page);
            return list;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 자세 비디오 등록
    @Override
    public long poseVideoInsert(VideoCHG video) {
        try {
            vRepository.save(video);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 자세 비디오 조회
    @Override
    public VideoCHG poseVideoSelectOne(long vno) {
        try {
            VideoCHG video = vRepository.findById(vno).orElse(null);
            return video;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 자세 비디오 수정
    @Override
    public long poseVideoUpdate(VideoCHG video) {
        try {
            vRepository.save(video);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

    // 자세 비디오 삭제
    @Override
    public int poseVideoDelete(long vno) {
        try {
            vRepository.deleteById(vno);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 자세 수정 조회용
    @Override
    public PoseCHG poseSelectPrivate(String memail, long pno) {
        try {
            PoseCHG pose = pRepository.findByMemberchg_memailAndPnoEqualsOrderByPnoDesc(memail, pno);
            return pose; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
