package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.mapper.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemServiceImpl implements ProblemService {
	private final ProblemRepository repository;
	public ProblemServiceImpl(ProblemRepository repository) {
		this.repository = repository;
	}

	@Override
	public void saveProblem(Problem problem) {
		repository.save(problem);
	}

	@Override
	public Optional<Problem> getProblemById(long id) {
		return repository.findById(id);
	}

	@Override
	public List<Problem> getAllProblems() {
		return repository.findAll();
	}

}
