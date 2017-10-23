package com.thoersch.seeds.persistence.issues;

import com.thoersch.seeds.representations.issues.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuesRepository extends JpaRepository<Issue, Long> {
}