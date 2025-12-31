package org.keroshi.keroshioj.controller.advice;

import org.keroshi.keroshioj.config.OjProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class OjControllerAdvice {
	private final OjProperties ojProperties;
	public OjControllerAdvice(OjProperties ojProperties) {
		this.ojProperties = ojProperties;
	}

	@ModelAttribute
	public void addOjAttributes(Model model) {
		model.addAttribute("oj", ojProperties);
	}
}
