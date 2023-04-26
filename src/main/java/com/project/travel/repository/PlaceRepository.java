package com.project.travel.repository;

import com.project.travel.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Query("SELECT p FROM Place p WHERE p.province like %:province% or p.city like %:city%")
    List<Place> findByProvinceAndCity(@Param("province") String province, @Param("city") String city);

    @Query(
            "SELECT p FROM Place p " +
            "WHERE LOWER(CONCAT(p.name, ' ', p.city, ' ', p.province)) " +
            "LIKE %:query% " +
            "AND LOWER(p.city) LIKE %:city% "+
            "AND LOWER(p.province) LIKE %:province%"
    )
    List<Place> searchPlace(
            @Param("query") String query,
            @Param("city") String city,
            @Param("province") String province
    );
}