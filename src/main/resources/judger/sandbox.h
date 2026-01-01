#ifndef SANDBOX_H
#define SANDBOX_H

#include <string>
#include "def.h"

Result run_task(
	const std :: string & exe_path,
	const std :: string & in_file,
	const std :: string & user_out_file,
	int time_limit_ms,
	int mem_limit_mb
);

#endif