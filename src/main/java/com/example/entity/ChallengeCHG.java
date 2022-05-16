package com.example.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

//첼린지 인증 엔티티
@Data
@Entity
@Table(name = "CHALLENGECHG")
@SequenceGenerator(name="SEQ_CHG_NO",
		sequenceName = "SEQ_CHG_NO", 
		allocationSize = 1, 
		initialValue = 1)

// native작성
@NamedNativeQuery(
	name  = "challenge_dto",
	query = "SELECT * FROM "
			+ " (SELECT ROW_NUMBER() OVER (ORDER BY CHGNO ASC) "
			+ " CHGNO, CHGLIKE, CHGLEVEL, CHGTITLE, CHGINTRO, CHGCONTENT, "
			+ " CHGSTART, CHGEND, RECRUITSTART, RECRUITEND, "
			+ " RECSTATE, CHGCNT, CHGFEE, "
			+ " CHGIMAGE, CHGISIZE, CHGITYPE, CHGINAME "
			+ " FROM "
			+ " CHALLENGECHG) "
			+ " WHERE CHGNO BETWEEN 1 AND 9 ORDER BY CHGLIKE DESC",



	resultSetMapping = "resultmap_challenge_dto"
)
@SqlResultSetMapping(
	name = "resultmap_challenge_dto",
	classes = @ConstructorResult(
		targetClass = ChallengeDTO.class,
		columns = {
			@ColumnResult(name = "CHGNO", type = Long.class),
			@ColumnResult(name = "CHGLIKE", type = Long.class),
			@ColumnResult(name = "CHGLEVEL", type = Long.class),
			@ColumnResult(name = "CHGTITLE", type = String.class),
			@ColumnResult(name = "CHGINTRO", type = String.class),
			@ColumnResult(name = "CHGCONTENT", type = String.class),
			@ColumnResult(name = "CHGSTART", type = Timestamp.class),
			@ColumnResult(name = "CHGEND", type = Timestamp.class),
			@ColumnResult(name = "RECRUITSTART", type = Timestamp.class),
			@ColumnResult(name = "RECRUITEND", type = Timestamp.class),

			// 오류 : Could not locate appropriate constructor on class
			@ColumnResult(name = "RECSTATE", type = int.class),
			@ColumnResult(name = "CHGCNT", type = Long.class),
			@ColumnResult(name = "CHGFEE", type = Long.class),
			@ColumnResult(name = "CHGIMAGE", type = byte[].class),
			@ColumnResult(name = "CHGISIZE", type = Long.class),
			@ColumnResult(name = "CHGITYPE", type = String.class),
			@ColumnResult(name = "CHGINAME", type = String.class)
		}
	)
)
public class ChallengeCHG {
	

	// 챌린지번호
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHG_NO")
	private Long chgno;
	  
	// 제목
	private String chgtitle;
	  
	// 소개
	private String chgintro;
	  
	// 내용
	@Lob
	private String chgcontent;
	  
	// 챌린지 시작
	@Column(nullable = false)
	private Timestamp chgstart;
	  
	// 챌린지 종료
	@Column(nullable = false)
	private Timestamp chgend;
	  
	// 모집 시작
	@Column(nullable = false)
	private Timestamp recruitstart;
	  
	// 모집 마감
	@Column(nullable = false)
	private Timestamp recruitend;
	
	// 모집 상태
	private int recstate = 1;
	  
	// 인원수
	private Long chgcnt = 1L;
	  
	// 참여비
	private Long chgfee;

	// 이미지
	@Lob
	private byte[] chgimage;

	// 이미지크기
	private Long chgisize;

	// 이미지타입
	private String chgitype;

	// 이미지명
	private String chginame;
	  
	// 생성일자
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
	@CreationTimestamp	// CURRENT_DATE
	@Column(name = "CHGREGDATE")
	private Date chgregdate;
	  
	// 좋아요개수저장
	private Long chglike = 0L;
	  
	// 난이도
	private Long chglevel = 1L;

	// 루틴
	private Long chgroutine;
	
	// 첼린지 생성한 사람
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
	
}
