package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.domain.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemService {
	void saveProblem(Problem problem);

	Optional<Problem> getProblemById(long id);

	List<Problem> getAllProblems();
}
