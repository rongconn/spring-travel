package com.project.travel.controllers;

import com.project.travel.models.Tour;
import com.project.travel.repository.PlaceRepository;
import com.project.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("api/search")
@RestController
public class SearchController {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    PlaceRepository placeRepository;

    @GetMapping
    public ResponseEntity<?> searchPlace(@RequestParam("query") String query) {
        System.out.println("alo alo alo alo:::"+query);
        List<Tour> tours = tourRepository.searchTour(query.toLowerCase());
        return ResponseEntity.ok().body(tours);
    }
}
