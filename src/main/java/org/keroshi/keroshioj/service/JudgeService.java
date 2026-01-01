package org.keroshi.keroshioj.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface JudgeService {

	@Async("judgeExecutor")
	@Transactional
	void executeJudge(long id);
}
