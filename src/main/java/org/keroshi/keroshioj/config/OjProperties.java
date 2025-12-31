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
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String lang;
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	private String dataPath;
	public String getDataPath() {
		return dataPath;
	}
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	private String SuUsername;
	public String getSuUsername() {
		return SuUsername;
	}
	public void setSuUsername(String SuUsername) {
		this.SuUsername = SuUsername;
	}

	private String SuPassword;
	public String getSuPassword() {
		return SuPassword;
	}
	public void setSuPassword(String SuPassword) {
		this.SuPassword = SuPassword;
	}
}
