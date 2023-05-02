package com.project.travel.repository;

import com.project.travel.enums.ETour;
import com.project.travel.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    @Query(
            "SELECT t FROM Tour t " +
            "WHERE LOWER(CONCAT(t.name, ' ', t.duration, ' ', t.province, ' ', t.city)) " +
            "LIKE %:keyword% " +
            "AND LOWER(t.city) LIKE %:city% "+
            "AND LOWER(t.province) LIKE %:province% "+
            "AND LOWER(t.type) IN (:interests)"
    )
    List<Tour> searchTour(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("province") String province,
            @Param("interests") List<String> interests
    );

    @Query("SELECT t FROM Tour t JOIN t.categories c WHERE c.id = :categoryId")
    Optional<List<Tour>> findByCategoryId(Integer categoryId);
}