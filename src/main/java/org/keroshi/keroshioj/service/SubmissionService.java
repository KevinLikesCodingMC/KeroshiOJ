package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.domain.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionService {
	void saveSubmission(Submission submission);

	Optional<Submission> getSubmissionById(long id);

	List<Submission> getAllSubmissions();
}
