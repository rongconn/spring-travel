package com.project.travel.controllers;

import com.project.travel.models.Place;
import com.project.travel.models.Tour;
import com.project.travel.payload.response.SearchTourAndPlaceResponse;
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
    public ResponseEntity<?> searchPlace(
            @RequestParam("query") String query,
            @RequestParam("city") String city,
            @RequestParam("province") String province,
            @RequestParam("interests") List<String> interests
    ) {
        List<Place> places = placeRepository.searchPlace(
                query.toLowerCase(),
                city.toLowerCase(),
                province.toLowerCase()
        );
        List<Tour> tours = tourRepository.searchTour(
                query.toLowerCase(),
                city.toLowerCase(),
                province.toLowerCase(),
                interests
        );
        SearchTourAndPlaceResponse response = new SearchTourAndPlaceResponse(tours, places);

        return ResponseEntity.ok().body(response);
    }
}
