package org.keroshi.keroshioj.controller;

import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.domain.Submission;
import org.keroshi.keroshioj.service.JudgeService;
import org.keroshi.keroshioj.service.ProblemService;
import org.keroshi.keroshioj.service.SubmissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProblemController {
	private final ProblemService problemService;
	private final SubmissionService submissionService;
	private final JudgeService judgeService;
	public ProblemController(ProblemService problemService,
							 SubmissionService submissionService,
							 JudgeService judgeService) {
		this.problemService = problemService;
		this.submissionService = submissionService;
		this.judgeService = judgeService;
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

	@RequestMapping("/problem/{id}/submit")
	public String problemSubmit(@PathVariable long id, @RequestParam Map<String,Object> map) {
		Optional<Problem> problem = problemService.getProblemById(id);
		if (problem.isEmpty()) {
			return "redirect:/problems";
		}
		String code = (String) map.get("code");

		Submission submission = new Submission();
		submission.setProblem(id);
		submission.setCode(code);
		submission.setUsername("Keroshi");
		submission.setStatus(7);
		submissionService.saveSubmission(submission);
		judgeService.executeJudge(submission.getId());

		return "redirect:/submissions";
	}
}
