package com.PRS.DB;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.PRS.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {
    long countByRequestNumberStartingWith(String prefix);
    @Query("SELECT r FROM Request r WHERE r.status = 'REVIEW' AND r.userId != :userId")
    List<Request> findRequestsForReview(@Param("userId") Integer userId);}


