package org.keroshi.keroshioj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oj")
@PropertySource(value = {
		"classpath:oj.properties",
		"file:oj.properties"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class OjProperties {
	private String dataPath;
	public String getDataPath() {
		return dataPath;
	}
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	private String SuUserName;
	public String getSuUserName() {
		return SuUserName;
	}
	public void setSuUserName(String SuUserName) {
		this.SuUserName = SuUserName;
	}

	private String SuPassword;
	public String getSuPassword() {
		return SuPassword;
	}
	public void setSuPassword(String SuPassword) {
		this.SuPassword = SuPassword;
	}
}
