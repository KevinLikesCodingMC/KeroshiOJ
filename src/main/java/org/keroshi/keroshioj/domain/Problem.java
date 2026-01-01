package org.keroshi.keroshioj.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "problem")
public class Problem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	public Long getId() {
		return id;
	}

	@Column(name = "pid")
	private String pid;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

	@Column(name = "name")
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@JdbcTypeCode(SqlTypes.LONG32VARCHAR)
	@Column(name = "statement")
	private String statement;
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "info")
	private Map<String, Object> info;
	public Map<String, Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}
}
