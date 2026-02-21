#include "sandbox.h"
#include <unistd.h>
#include <fcntl.h>
#include <signal.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <sys/resource.h>

long get_ms() {
	struct timeval tv;
	gettimeofday(& tv, NULL);
	return tv.tv_sec * 1000 + tv.tv_usec / 1000;
}

Result run_task(
	const std :: string & exe_path,
	const std :: string & in_file,
	const std :: string & out_file,
	int time_limit_ms,
	int mem_limit_mb
) {
	Result res = {AC, 0, 0};
	long start_wall_time = get_ms();

	pid_t pid = fork();
	if (pid == - 1) {
		res.status = RE;
		return res;
	}
	if (pid == 0) {
		int fd_in = open(in_file.c_str(), O_RDONLY);
		int fd_out = open(out_file.c_str(), O_WRONLY | O_CREAT | O_TRUNC, 0644);
		if (fd_in < 0 || fd_out < 0) {
			perror("Open files failed");
			exit(255);
		}
		dup2(fd_in, STDIN_FILENO);
		dup2(fd_out, STDOUT_FILENO);
		close(fd_in);
		close(fd_out);

		struct rlimit rl;
		rl.rlim_cur = rl.rlim_max = (time_limit_ms / 1000) + 1;
		setrlimit(RLIMIT_CPU, & rl);
		rl.rlim_cur = rl.rlim_max = (rlim_t) (mem_limit_mb + 10) * 1024 * 1024;
		setrlimit(RLIMIT_AS, & rl);
		rl.rlim_cur = rl.rlim_max = (rlim_t) (mem_limit_mb + 10) * 1024 * 1024;
		setrlimit(RLIMIT_STACK, & rl);
		rl.rlim_cur = rl.rlim_max = 64 * 1024 * 1024;
		setrlimit(RLIMIT_FSIZE, & rl);

		execl(exe_path.c_str(), exe_path.c_str(), (char *) NULL);
		perror("Exec failed");
		exit(255);
	}
	else {
		int status;
		struct rusage usage;

		alarm((time_limit_ms * 2 / 1000) + 1);
		if (wait4(pid, & status, 0, & usage) == - 1) {
			res.status = RE;
			return res;
		}
		alarm(0);

		res.time_ms = usage.ru_utime.tv_sec * 1000 + usage.ru_utime.tv_usec / 1000;
		res.memory_kb = usage.ru_maxrss;

		long end_wall_time = get_ms();
		long wall_time_passed = end_wall_time - start_wall_time;
		
		if (WIFSIGNALED(status)) {
			int sig = WTERMSIG(status);
			if (sig == SIGXCPU || sig == SIGALRM
				|| (sig == SIGKILL && res.time_ms >= time_limit_ms - 10)) {
				res.status = TLE;
			}
			else if (sig == SIGKILL || sig == SIGSEGV) {
				if (res.memory_kb >= mem_limit_mb * 1024 - 1024) res.status = MLE;
				else res.status = RE;
			}
			else if (sig == SIGXFSZ) {
				res.status = RE;
			}
			else {
				res.status = RE;
			}
		}
		else {
			if (res.time_ms > time_limit_ms || wall_time_passed > time_limit_ms * 2) {
				res.status = TLE;
			}
			else if (res.memory_kb > mem_limit_mb * 1024) {
				res.status = MLE;
			}
			else if (WEXITSTATUS(status) != 0) {
				res.status = RE;
			}
			else {
				res.status = AC;
			}
		}
	}
	return res;
}