package org.keroshi.keroshioj.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "submission")
public class Submission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	public Long getId() {
		return id;
	}

	@Column(name = "username")
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}

	@Column(name = "problem")
	private long problem;
	public long getProblem() {
		return problem;
	}
	public void setProblem(long problem) {
		this.problem = problem;
	}

	@JdbcTypeCode(SqlTypes.LONG32VARCHAR)
	@Column(name = "code")
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "status")
	private Integer status;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "submit_time")
	@org.hibernate.annotations.CreationTimestamp
	private LocalDateTime submitTime;
	public LocalDateTime getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(LocalDateTime submitTime) {
		this.submitTime = submitTime;
	}

	@Column(name = "time_ms")
	private Integer timeMs;
	public Integer getTimeMs() {
		return timeMs;
	}
	public void setTimeMs(Integer timeMs) {
		this.timeMs = timeMs;
	}

	@Column(name = "memory_kb")
	private Integer memoryKb;
	public Integer getMemoryKb() {
		return memoryKb;
	}
	public void setMemoryKb(Integer memoryKb) {
		this.memoryKb = memoryKb;
	}

	@JdbcTypeCode(SqlTypes.LONG32VARCHAR)
	@Column(name = "compile_log")
	private String compileLog;
	public String getCompileLog() {
		return compileLog;
	}
	public void setCompileLog(String compileLog) {
		this.compileLog = compileLog;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "submission_detail",
			joinColumns = @JoinColumn(name = "submission_id")
	)
	@OrderColumn(name = "detail_index")
	private List<JudgeDetail> details = new java.util.ArrayList<>();
	public List<JudgeDetail> getDetails() {
		return details;
	}
	public void setDetails(List<JudgeDetail> details) {
		this.details = details;
	}

	public String getVerdictName() {
		return Verdict.fromCode(this.status).getName();
	}

	public String getVerdictColor() {
		return Verdict.fromCode(this.status).getColorClass();
	}
}
