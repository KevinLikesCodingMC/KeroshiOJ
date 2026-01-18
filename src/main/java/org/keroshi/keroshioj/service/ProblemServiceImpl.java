package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.config.OjProperties;
import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.mapper.ProblemRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemServiceImpl implements ProblemService {
	private final ProblemRepository problemRepository;
	private final OjProperties ojProperties;
	public ProblemServiceImpl(ProblemRepository problemRepository, OjProperties ojProperties) {
		this.problemRepository = problemRepository;
		this.ojProperties = ojProperties;
	}

	@Override
	public void saveProblem(Problem problem) {
		problemRepository.save(problem);
	}

	@Override
	public Optional<Problem> getProblemById(long id) {
		return problemRepository.findById(id);
	}

	@Override
	public List<Problem> getProblemsByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}
		return problemRepository.findAllByIdIn(ids);
	}

	@Override
	public List<Problem> getAllProblems() {
		return problemRepository.findAll();
	}

	@Override
	public List<TestCasePair> getTestCases(long id) {
		List<TestCasePair> testCasePairs = new ArrayList<>();
		String problemDataPath = ojProperties.getDataPath() + File.separator + "problem" + File.separator + id;
		File dir = new File(problemDataPath);
		if (! dir.exists() || ! dir.isDirectory()) {
			return testCasePairs;
		}
		File [] inFiles = dir.listFiles((d, name) -> name.endsWith(".in"));
		if (inFiles == null || inFiles.length == 0) {
			return testCasePairs;
		}
		Arrays.sort(inFiles, (f1, f2) -> {
			int num1 = extractNumber(f1.getName());
			int num2 = extractNumber(f2.getName());
			return Integer.compare(num1, num2);
		});
		for (File inFile : inFiles) {
			String fileName = inFile.getName();
			String baseName = fileName.substring(0, fileName.lastIndexOf(".in"));

			File ansFile = new File(dir, baseName + ".ans");
			File outFile = new File(dir, baseName + ".out");

			File targetFile = null;
			if (ansFile.exists()) {
				targetFile = ansFile;
			} else if (outFile.exists()) {
				targetFile = outFile;
			}

			if (targetFile == null) continue;
			testCasePairs.add(new TestCasePair(
					inFile.getAbsolutePath(),
					targetFile.getAbsolutePath()
			));
		}
		return testCasePairs;
	}

	private int extractNumber(String name) {
		try {
			String number = name.replaceAll("[^0-9]", "");
			return number.isEmpty() ? 0 : Integer.parseInt(number);
		} catch (Exception e) {
			return 0;
		}
	}
}
