package com.project.travel.repository;

import com.project.travel.enums.ETour;
import com.project.travel.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Optional<Interest> findByKey(ETour key);
}
