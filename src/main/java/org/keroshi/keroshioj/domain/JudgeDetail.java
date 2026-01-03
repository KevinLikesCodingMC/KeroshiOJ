package org.keroshi.keroshioj.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Embeddable
public class JudgeDetail {
	@Column(name = "status")
	private Integer status;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	@Column(name = "message")
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public JudgeDetail() {}

	public JudgeDetail(Integer status, Integer timeMs, Integer memoryKb, String message) {
		this.status = status;
		this.timeMs = timeMs;
		this.memoryKb = memoryKb;
		this.message = message;
	}

	public String getVerdictName() {
		return Verdict.fromCode(this.status).getFullname();
	}
	public String getVerdictColor() {
		return Verdict.fromCode(this.status).getDetColor();
	}
}
