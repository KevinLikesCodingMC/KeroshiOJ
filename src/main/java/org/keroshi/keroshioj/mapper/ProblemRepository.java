package org.keroshi.keroshioj.mapper;

import org.keroshi.keroshioj.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
	List<Problem> findAllByIdIn(Collection<Long> ids);
}
