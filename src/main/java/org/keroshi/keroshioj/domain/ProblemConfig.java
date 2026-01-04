package org.keroshi.keroshioj.domain;

import java.io.Serializable;

public class ProblemConfig implements Serializable {
	private Object value;
	private boolean visible;

	public ProblemConfig() {}
	public ProblemConfig(Object value, boolean visible) {
		this.value = value;
		this.visible = visible;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getFormattedValue(String key) {
		if (value == null) return "";
		return switch (key) {
			case "tl" -> "Time Limit: " + value.toString() + " ms";
			case "ml" -> "Memory Limit: " + value.toString() + " MB";
			default -> key + ": " + value.toString();
		};
	}
}
