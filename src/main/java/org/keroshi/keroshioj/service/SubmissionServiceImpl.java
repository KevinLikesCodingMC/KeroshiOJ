package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.domain.Submission;
import org.keroshi.keroshioj.mapper.SubmissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {
	private final SubmissionRepository submissionRepository;
	public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
		this.submissionRepository = submissionRepository;
	}

	@Override
	public void saveSubmission(Submission submission) {
		submissionRepository.save(submission);
	}

	@Override
	public Optional<Submission> getSubmissionById(long id) {
		return submissionRepository.findById(id);
	}

	@Override
	public List<Submission> getAllSubmissions() {
		return submissionRepository.findAll();
	}

	@Override
	public Page<Submission> getSubmissionByPage(Pageable page) {
		return  submissionRepository.findAll(page);
	}
}
