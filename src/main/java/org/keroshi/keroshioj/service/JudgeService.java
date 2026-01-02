package org.keroshi.keroshioj.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface JudgeService {

	@Async("judgeExecutor")
	void executeJudge(long id);
}
