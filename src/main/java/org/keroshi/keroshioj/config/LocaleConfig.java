package org.keroshi.keroshioj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {
	private final OjProperties ojProperties;
	public LocaleConfig(OjProperties ojProperties) {
		this.ojProperties = ojProperties;
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		Locale defaultLocale = getLocaleFromLang(ojProperties.getLang());
		resolver.setDefaultLocale(defaultLocale);
		return resolver;
	}

	private Locale getLocaleFromLang(String lang) {
		if(lang == null || lang.isEmpty()) {
			return Locale.ENGLISH;
		}
		return switch (lang.toLowerCase()) {
			case "zh", "zh_cn", "zh-cn" -> Locale.CHINA;
			default -> Locale.ENGLISH;
		};
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
