package org.keroshi.keroshioj.controller;

import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.domain.Submission;
import org.keroshi.keroshioj.service.ProblemService;
import org.keroshi.keroshioj.service.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String submissions(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Sort sort = Sort.by(Sort.Direction.DESC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Submission> submissionPage = submissionService.getSubmissionByPage(pageable);
		model.addAttribute("submissions", submissionPage);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", submissionPage.getTotalPages());
		model.addAttribute("totalItems", submissionPage.getTotalElements());
		model.addAttribute("hasNext", submissionPage.hasNext());
		model.addAttribute("hasPrevious", submissionPage.hasPrevious());
		model.addAttribute("pageSize", pageable.getPageSize());

		List<Submission> submissions = submissionPage.getContent();
		List<Long> pids = submissions.stream()
				.map(Submission :: getProblem)
				.distinct()
				.toList();
		List<Problem> problems = problemService.getProblemsByIds(pids);
		Map<Long, Problem> problemMap = problems.stream()
				.collect(Collectors.toMap(Problem :: getId, p -> p));
		model.addAttribute("problemMap", problemMap);

		return "submission_list";
	}

	@RequestMapping("/submission/{id}")
	public String submission(@PathVariable long id, Model model) {
		Optional<Submission> submission = submissionService.getSubmissionById(id);
		if (submission.isEmpty()) {
			return "redirect:/submissions";
		}
		model.addAttribute("submission", submission.get());
		Optional<Problem> problem = problemService.getProblemById(submission.get().getProblem());
		model.addAttribute("problem", problem.orElseThrow());
		return "submission";
	}
}
