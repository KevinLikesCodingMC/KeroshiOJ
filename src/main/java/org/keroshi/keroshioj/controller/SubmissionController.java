package org.keroshi.keroshioj.controller;

import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.domain.Submission;
import org.keroshi.keroshioj.service.ProblemService;
import org.keroshi.keroshioj.service.SubmissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SubmissionController {
	private final SubmissionService submissionService;
	private final ProblemService problemService;
	public SubmissionController(SubmissionService submissionService,
								ProblemService problemService) {
		this.submissionService = submissionService;
		this.problemService = problemService;
	}

	@RequestMapping("/submissions")
	public String submissions(Model model) {
		List<Submission> submissions = submissionService.getAllSubmissions();
		model.addAttribute("submissions", submissions);
		List<Long> pids = submissions.stream()
				.map(Submission :: getProblem)
				.distinct()
				.toList();
		List<Problem> problems = problemService.getProblemsByIds(pids);
		Map<Long, Problem> problemMap = problems.stream()
				.collect(Collectors.toMap(Problem::getId, p -> p));
		model.addAttribute("problemMap", problemMap);
		return "submission_list";
	}

	@RequestMapping("/submission/{id}")
	public String problem(@PathVariable long id, Model model) {
		Optional<Submission> submission = submissionService.getSubmissionById(id);
		if (submission.isEmpty()) {
			return "redirect:/submissions";
		}
		model.addAttribute("submission", submission.get());
		return "submission";
	}
}
