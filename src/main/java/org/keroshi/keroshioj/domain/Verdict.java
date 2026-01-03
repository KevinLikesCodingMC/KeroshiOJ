package org.keroshi.keroshioj.domain;

public enum Verdict {
	AC(0, "AC", "Accepted", "v-ac", "vd-ac"),
	TL(1, "TL", "Time Limit Exceeded", "v-tl", "vd-tl"),
	ML(2, "ML", "Memory Limit Exceeded", "v-ml", "vd-ml"),
	RE(3, "RE", "Runtime Error", "v-re", "vd-re"),
	WA(4, "WA", "Wrong Answer", "v-wa", "vd-wa"),
	CE(5, "CE", "Compile Error", "v-ce", "vd-ce"),
	SE(6, "SE", "System Error", "v-se", "vd-se"),
	WAITING(7, "Waiting", "Pending", "v-waiting", "vd-waiting"),
	JUDGING(8, "Judging", "Running", "v-judging", "vd-judging");
	private final int code;
	private final String name;
	private final String fullname;
	private final String subColor;
	private final String detColor;
	Verdict(int code, String name, String fullname, String subColor, String detColor) {
		this.code = code;
		this.name = name;
		this.fullname = fullname;
		this.subColor = subColor;
		this.detColor = detColor;
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
	public String getFullname() {
		return fullname;
	}
	public String getSubColor() {
		return subColor;
	}
	public String getDetColor() {
		return detColor;
	}
}
