package com.project.travel.repository;

import com.project.travel.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    @Query("SELECT t FROM Tour t WHERE LOWER(CONCAT(t.name, ' ', t.duration, ' ', t.province, ' ', t.city)) LIKE %:keyword%")
    List<Tour> searchTour(String keyword);
}