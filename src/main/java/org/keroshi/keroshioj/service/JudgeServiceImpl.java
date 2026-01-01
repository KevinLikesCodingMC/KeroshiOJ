package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.config.OjProperties;
import org.keroshi.keroshioj.domain.JudgeDetail;
import org.keroshi.keroshioj.domain.Submission;
import org.keroshi.keroshioj.mapper.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JudgeServiceImpl implements JudgeService {
	private final SubmissionRepository submissionRepository;
	private final ProblemService problemService;
	private final OjProperties ojProperties;
	public JudgeServiceImpl(SubmissionRepository submissionRepository,
							ProblemService problemService,
							OjProperties ojProperties) {
		this.submissionRepository = submissionRepository;
		this.problemService = problemService;
		this.ojProperties = ojProperties;
	}

	@Async("judgeExecutor")
	@Transactional
	@Override
	public void executeJudge(long id) {
		Submission sub = submissionRepository.findById(id).orElseThrow();
		sub.setStatus(8);
		submissionRepository.save(sub);

		String workDirPath = ojProperties.getDataPath() + File.separator + "temp" + File.separator + id;
		File workDir = new File(workDirPath);
		if (! workDir.exists()) workDir.mkdirs();

		int maxTime = - 1;
		int maxMem = - 1;

		try {
			String userBin = compile(sub, workDirPath);
			if (userBin == null) {
				submissionRepository.save(sub);
				return;
			}

			List<ProblemService.TestCasePair> cases =
					problemService.getTestCases(sub.getProblem());
			if (cases.isEmpty()) {
				sub.setStatus(6);
				sub.setCompileLog("System Error: No test cases found.");
				submissionRepository.save(sub);
				return;
			}

			boolean failed = false;

			for (ProblemService.TestCasePair cp : cases) {
				JudgeDetail detail = runSingleTest(userBin, cp);
				sub.getDetails().add(detail);

				maxTime = Math.max(maxTime, detail.getTimeMs());
				maxMem = Math.max(maxMem, detail.getMemoryKb());

				if (detail.getStatus() != 0) {
					sub.setStatus(detail.getStatus());
					failed = true;
					break;
				}
				sub.setTimeMs(maxTime);
				sub.setMemoryKb(maxMem);
				submissionRepository.save(sub);
			}

			if (! failed) {
				sub.setStatus(0);
			}
		} catch (Exception e) {
			sub.setStatus(6);
			sub.setCompileLog("System Error: " + e.getMessage());
		} finally {
			sub.setTimeMs(maxTime);
			sub.setMemoryKb(maxMem);
			submissionRepository.save(sub);
		}
	}

	private String compile(Submission sub, String workDir) throws Exception {
		String sourcePath = workDir + File.separator + "main.cpp";
		String binPath = workDir + File.separator + "main";
		Files.writeString(Path.of(sourcePath), sub.getCode());
		ProcessBuilder pb = new ProcessBuilder("g++", sourcePath, "-o", binPath, "-O2", "-std=c++14");
		pb.redirectErrorStream(true);
		Process p = pb.start();
		String output = new String(p.getInputStream().readAllBytes());
		boolean finished = p.waitFor(20, TimeUnit.SECONDS);
		boolean success = finished && p.exitValue() == 0;
		sub.setCompileLog(output);
		if (! success) {
			if (! finished) p.destroyForcibly();
			sub.setStatus(5);
			return null;
		}
		return binPath;
	}

	private JudgeDetail runSingleTest(String userBin, ProblemService.TestCasePair cp) throws Exception {
		String testcasePath = ojProperties.getDataPath() + File.separator + "bin" + File.separator + "testcase";
		String ncmpPath = ojProperties.getDataPath() + File.separator + "bin" + File.separator + "ncmp";

		ProcessBuilder pb = new ProcessBuilder(
				testcasePath,
				userBin,
				cp.getInPath(),
				cp.getAnsPath(),
				"1000",
				"256",
				ncmpPath
		);

		Process p = pb.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line = reader.readLine();
			boolean finished = p.waitFor(5, TimeUnit.SECONDS);

			if (finished && line != null && !line.isBlank()) {
				String[] parts = line.trim().split("\\s+", 4);
				if (parts.length >= 3) {
					return new JudgeDetail(
							Integer.parseInt(parts[0]),
							Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]),
							(parts.length == 4) ? parts[3] : ""
					);
				}
			}
		} finally {
			if (p.isAlive()) p.destroyForcibly();
		}

		return new JudgeDetail(6, 0, 0, "Sandbox Internal Error");
	}
}
