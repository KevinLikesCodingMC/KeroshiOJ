package org.keroshi.keroshioj.domain;

public enum Verdict {
	AC(0, "AC", "text-success"),
	TL(1, "TL", "text-warning"),
	ML(2, "ML", "text-warning"),
	RE(3, "RE", "text-warning"),
	WA(4, "WA", "text-danger"),
	CE(5, "CE", "text-secondary"),
	SE(6, "SE", "text-danger"),
	WAITING(7, "Waiting", "text-info"),
	JUDGING(8, "Judging", "text-primary");
	private final int code;
	private final String name;
	private final String colorClass;
	Verdict(int code, String name, String colorClass) {
		this.code = code;
		this.name = name;
		this.colorClass = colorClass;
	}
	public static Verdict fromCode(int code) {
		for (Verdict v : Verdict.values()) {
			if (v.code == code) return v;
		}
		return SE;
	}
	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getColorClass() {
		return colorClass;
	}
}
