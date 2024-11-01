package com.devrace.domain.algorithm.comment.repository;

import com.devrace.domain.algorithm.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findBySolutionIdOrderByCreatedAtDesc(Long solutionId);
}
