package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.domain.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemService {
	void saveProblem(Problem problem);

	Optional<Problem> getProblemById(long id);

	List<Problem> getProblemsByIds(List<Long> ids);

	List<Problem> getAllProblems();

	List<TestCasePair> getTestCases(long id);

	class TestCasePair {
		private final String inPath;
		private final String ansPath;
		public TestCasePair(String inPath, String ansPath) {
			this.inPath = inPath;
			this.ansPath = ansPath;
		}
		public String getInPath() {
			return inPath;
		}
		public String getAnsPath() {
			return ansPath;
		}
	}
}
