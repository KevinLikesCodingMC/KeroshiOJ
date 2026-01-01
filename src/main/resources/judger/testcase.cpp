#include "sandbox.h"
#include <iostream>
#include <fstream>
#include <string>
#include <unistd.h>
#include <sys/wait.h>

std :: string run_checker(
	const std :: string & checker_path, 
	const std :: string & in,
	const std :: string & out,
	const std :: string & ans,
	bool & is_ac
) {
	std :: string msg_file = "msg_" + std :: to_string(getpid()) + ".txt";
	std :: string cmd = checker_path + " " + in + " " + out + " " + ans
		+ " 2> " + msg_file + " > /dev/null";
	int exit_code = system(cmd.c_str());
	is_ac = (WIFEXITED(exit_code) && WEXITSTATUS(exit_code) == 0);
	std :: ifstream ifs(msg_file);
	std :: string msg;
	if (ifs.is_open()) {
		std :: string line;
		while (std :: getline(ifs, line)) {
			msg += line + " ";
		}
		ifs.close();
	}
	unlink(msg_file.c_str());
	for (auto & ch : msg) if (ch == '\r' || ch == '\n') ch = ' ';
	return msg.empty() ? "No message" : msg;
}

int main(int argc, char * argv []) {
	if (argc < 7) return 1;
	
	std :: string exe_path = argv[1];
	std :: string in_file = argv[2];
	std :: string ans_file = argv[3];
	int time_limit_ms = std :: stoi(argv[4]);
	int mem_limit_mb = std :: stoi(argv[5]);
	std :: string checker_path = argv[6];
	std :: string out_file = "user.out";

	Result result = run_task(
		exe_path,
		in_file,
		out_file,
		time_limit_ms,
		mem_limit_mb
	);

	std :: string checker_msg = "";

	if (result.status == AC) {
		bool is_ac = false;
		checker_msg = run_checker(
			checker_path,
			in_file,
			out_file,
			ans_file,
			is_ac
		);
		if (!is_ac) {
			result.status = WA;
		}
	} else {
		if (result.status == TLE) checker_msg = "Time Limit Exceeded";
		else if (result.status == MLE) checker_msg = "Memory Limit Exceeded";
		else checker_msg = "Runtime Error";
	}

	std :: cout << result.status << " "
		<< result.time_ms << " "
		<< result.memory_kb << " "
		<< checker_msg << std :: endl;

	unlink(out_file.c_str());
	return 0;
}
