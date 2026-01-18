package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.config.OjProperties;
import org.keroshi.keroshioj.domain.JudgeDetail;
import org.keroshi.keroshioj.domain.Problem;
import org.keroshi.keroshioj.domain.ProblemConfig;
import org.keroshi.keroshioj.domain.Submission;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JudgeServiceImpl implements JudgeService {
	private final SubmissionService submissionService;
	private final ProblemService problemService;
	private final OjProperties ojProperties;
	public JudgeServiceImpl(SubmissionService submissionService,
							ProblemService problemService,
							OjProperties ojProperties) {
		this.submissionService = submissionService;
		this.problemService = problemService;
		this.ojProperties = ojProperties;
	}

	@Async("judgeExecutor")
	@Override
	public void executeJudge(long id) {
		Submission submission = submissionService.getSubmissionById(id).orElseThrow();
		submission.setStatus(8);
		submission.getDetails().clear();
		submissionService.saveSubmission(submission);

		String workDirPath = ojProperties.getDataPath() + File.separator + "temp" + File.separator + id;
		File workDir = new File(workDirPath);
		if (! workDir.exists()) workDir.mkdirs();

		int maxTime = - 1;
		int maxMem = - 1;

		try {
			String userBin = compile(submission, workDirPath);
			if (userBin == null) {
				submissionService.saveSubmission(submission);
				return;
			}

			long pid = submission.getProblem();
			Problem problem = problemService.getProblemById(pid).orElseThrow();
			Map<String, ProblemConfig> info = problem.getInfo();

			String[] requiredKeys = {"tl", "ml", "judger", "checker"};
			for (String key : requiredKeys) {
				if (info == null || ! info.containsKey(key)) {
					submission.setStatus(6);
					submission.setCompileLog("System Error: Missing required config field: " + key);
					return;
				}
				if (info.get(key).getValue() == null) {
					submission.setStatus(6);
					submission.setCompileLog("System Error: Config field [" + key + "] has no value");
					return;
				}
			}
			int timeLimit = Integer.parseInt(info.get("tl").getValue().toString());
			int memoryLimit = Integer.parseInt(info.get("ml").getValue().toString());
			String testcaseName = info.get("judger").getValue().toString();
			String checkerName = info.get("checker").getValue().toString();

			String testcasePath = ojProperties.getDataPath() + File.separator + "bin" + File.separator + testcaseName;
			String checkerPath = ojProperties.getDataPath() + File.separator + "bin" + File.separator + checkerName;
			File testcaseFile = new File(testcasePath);
			File checkerFile = new File(checkerPath);
			if (! testcaseFile.exists()) {
				submission.setStatus(6);
				submission.setCompileLog("System Error: testcase binary not found at " + testcasePath);
				return;
			}
			if (! checkerFile.exists()) {
				submission.setStatus(6);
				submission.setCompileLog("System Error: checker not found at " + checkerPath);
				return;
			}

			List<ProblemService.TestCasePair> cases =
					problemService.getTestCases(submission.getProblem());
			if (cases.isEmpty()) {
				submission.setStatus(6);
				submission.setCompileLog("System Error: No test cases found.");
				submissionService.saveSubmission(submission);
				return;
			}

			boolean failed = false;

			for (ProblemService.TestCasePair cp : cases) {
				JudgeDetail detail = runSingleTest(
						userBin,
						cp,
						timeLimit,
						memoryLimit,
						testcasePath,
						checkerPath
				);

				submission.getDetails().add(detail);
				maxTime = Math.max(maxTime, detail.getTimeMs());
				maxMem = Math.max(maxMem, detail.getMemoryKb());

				if (detail.getStatus() != 0) {
					submission.setStatus(detail.getStatus());
					failed = true;
					break;
				}
//				submission.setTimeMs(maxTime);
//				submission.setMemoryKb(maxMem);
//				submissionService.saveSubmission(submission);
			}
			if (! failed) {
				submission.setStatus(0);
			}
		} catch (Exception e) {
			submission.setStatus(6);
			submission.setCompileLog("System Error: " + e.getMessage());
		} finally {
			submission.setTimeMs(maxTime);
			submission.setMemoryKb(maxMem);
			submissionService.saveSubmission(submission);
		}
	}

	private String compile(Submission submission, String workDir) throws Exception {
		String sourcePath = workDir + File.separator + "main.cpp";
		String binPath = workDir + File.separator + "main";
		Files.writeString(Path.of(sourcePath), submission.getCode());
		ProcessBuilder pb = new ProcessBuilder("g++", sourcePath, "-o", binPath, "-O2", "-std=c++14");
		pb.redirectErrorStream(true);
		Process p = pb.start();
		String output = new String(p.getInputStream().readAllBytes());
		boolean finished = p.waitFor(20, TimeUnit.SECONDS);
		boolean success = finished && p.exitValue() == 0;
		submission.setCompileLog(output);
		if (! success) {
			if (! finished) p.destroyForcibly();
			submission.setStatus(5);
			return null;
		}
		return binPath;
	}

	private JudgeDetail runSingleTest(
			String userBin,
			ProblemService.TestCasePair cp,
			int timeLimit,
			int memoryLimit,
			String testcasePath,
			String checkerPath
	) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(
				testcasePath,
				userBin,
				cp.getInPath(),
				cp.getAnsPath(),
				String.valueOf(timeLimit),
				String.valueOf(memoryLimit),
				checkerPath
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
