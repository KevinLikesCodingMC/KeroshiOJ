package org.keroshi.keroshioj.controller.advice;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
public class LocaleControllerAdvice {
	@ModelAttribute
	public void addLocaleAttributes(Model model) {
		Locale locale = LocaleContextHolder.getLocale();
		model.addAttribute("locale", locale.getLanguage());
	}
}
