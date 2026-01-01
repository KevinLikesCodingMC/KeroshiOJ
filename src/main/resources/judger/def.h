#ifndef DEF_H
#define DEF_H

enum Status {
	AC = 0,
	TLE,
	MLE,
	RE,
	WA,
	CE
};

struct Result {
	Status status;
	long time_ms;
	long memory_kb;
};

#endif