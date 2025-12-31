package org.keroshi.keroshioj.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.keroshi.keroshioj.config.OjProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;

@Controller
public class LoginController {
	private final OjProperties ojProperties;
	public LoginController(OjProperties ojProperties) {
		this.ojProperties = ojProperties;
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/login/check")
	public String adminLoginCheck(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		if (! Objects.equals(map.get("username"), ojProperties.getSuUsername())
				|| ! Objects.equals(map.get("password"), ojProperties.getSuPassword())) {
			return "redirect:/login";
		}
		request.getSession().setAttribute("user", "admin");
		return "redirect:/";
	}
}
