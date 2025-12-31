package org.keroshi.keroshioj.config;

import org.keroshi.keroshioj.intercepter.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminConfig implements WebMvcConfigurer {
	private final AdminInterceptor adminInterceptor;
	public AdminConfig(AdminInterceptor adminInterceptor) {
		this.adminInterceptor = adminInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/error",
						"/login",
						"/login/check",
						"/lib/**"
				);
	}
}
