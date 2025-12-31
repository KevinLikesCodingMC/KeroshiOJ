package org.keroshi.keroshioj.controller;

import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.service.ProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ProblemController {
	private final ProblemService problemService;
	public ProblemController(ProblemService problemService) {
		this.problemService = problemService;
	}

	@RequestMapping("/problems")
	public String problems(Model model) {
		List<Problem> problems = problemService.getAllProblems();
		model.addAttribute("problems", problems);
		return "problem_list";
	}

	@RequestMapping("/problems/new")
	public String newProblem() {
		Problem problem = new Problem();
		problem.setPid("P0");
		problem.setName("New Problem");
		problemService.saveProblem(problem);
		return "redirect:/problems";
	}

	@RequestMapping("/problem/{id}")
	public String problem(@PathVariable long id, Model model) {
		Optional<Problem> problem = problemService.getProblemById(id);
		if (problem.isEmpty()) {
			return "redirect:/problems";
		}
		model.addAttribute("problem", problem.get());
		return "problem";
	}
}
