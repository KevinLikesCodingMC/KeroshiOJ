package org.keroshi.keroshioj.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

public class GlobalErrorController implements ErrorController {
	@RequestMapping("/error")
	public String HandleError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if (status != null) {
			httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));
		}

		model.addAttribute("status", httpStatus.value());
		model.addAttribute("error", httpStatus.getReasonPhrase());
		model.addAttribute("timestamp", new Date());

		return "error";
	}
}
