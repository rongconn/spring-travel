package com.project.travel.controllers;

import com.project.travel.enums.ECate;
import com.project.travel.models.Category;
import com.project.travel.models.Place;
import com.project.travel.models.Tour;
import com.project.travel.payload.request.TourRequest;
import com.project.travel.repository.CategoryRepository;
import com.project.travel.repository.PlaceRepository;
import com.project.travel.repository.TourRepository;
import com.project.travel.services.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/tours")
public class TourController {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryRepository categoryRepositoryRepository;
    @Autowired
    FilesStorageService storageService;

    @Value("${app.fileURL}")
    private String fileURL;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);

    // Get all tours
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    // Create a new tour
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Tour> createTour(
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute TourRequest tourRequest
    ) {
        Tour tour = new Tour();
        Set<Category> categories = new HashSet<>();
        Set<Place> places = new HashSet<>();
        Set<String> categoriesString = tourRequest.getCategories();
        Set<String> placeString = tourRequest.getPlaces();

        if (!placeString.isEmpty()) {
            placeString.forEach(id -> {
                placeRepository.findById(Integer.parseInt(id)).ifPresent(places::add);
            });
        }

        if (!categoriesString.isEmpty()) {
            categoriesString.forEach(key -> {
                categoryRepositoryRepository.findByKey(ECate.valueOf(key)).ifPresent(categories::add);
            });
        }

        //upload tour image
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!filename.equals("")){
            try{
                storageService.save(file);
            }catch(Exception e){
                logger.error(e.getMessage());
            }
            tour.setImage(fileURL + filename);
        }

        tour.setPlaces(places);
        tour.setCategories(categories);
        tour.setDuration(tourRequest.getDuration());
        tour.setCity(tourRequest.getCity());
        tour.setName(tourRequest.getName());
        tour.setPrice(tourRequest.getPrice());
        tour.setProvince(tourRequest.getProvince());
        tour.setType(tourRequest.getType());
        tour.setDescription(tourRequest.getDescription());
        tour.setRating(tourRequest.getRating());

        tourRepository.save(tour);
        return new ResponseEntity<>(tour, HttpStatus.CREATED);
    }

    // Get a tour by id
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable(value = "id") Integer id) {
        Tour tour = tourRepository.findById(id).orElse(null);
        if (tour == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(tour);
    }
    // Get a tour by id
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/category")
    public ResponseEntity<List<Tour>> getTourByCategoryId(@RequestParam("id") Integer id) {
        List<Tour> tour = tourRepository.findByCategoryId(id).orElse(null);
        if (tour == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(tour);
    }

    // Update a tour
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute TourRequest tourRequest
    ) {
        Tour tour = tourRepository.findById(id).orElse(null);
        if(tour != null){
            Set<Category> categories = new HashSet<>();
            Set<Place> places = new HashSet<>();
            Set<String> categoriesString = tourRequest.getCategories();
            Set<String> placeString = tourRequest.getPlaces();

            if (!placeString.isEmpty()) {
                placeString.forEach(item -> {
                    placeRepository.findById(Integer.parseInt(item)).ifPresent(places::add);
                });
            }

            if (!categoriesString.isEmpty()) {
                categoriesString.forEach(key -> {
                    categoryRepositoryRepository.findByKey(ECate.valueOf(key)).ifPresent(categories::add);
                });
            }

            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (!filename.equals("")){
                try{
                    storageService.save(file);
                }catch(Exception e){
                    logger.error(e.getMessage());
                }
                tour.setImage(fileURL + filename);
            }

            tour.setPlaces(places);
            tour.setCategories(categories);
            tour.setDuration(tourRequest.getDuration());
            tour.setCity(tourRequest.getCity());
            tour.setName(tourRequest.getName());
            tour.setPrice(tourRequest.getPrice());
            tour.setProvince(tourRequest.getProvince());
            tour.setType(tourRequest.getType());
            tour.setDescription(tourRequest.getDescription());
            tour.setRating(tourRequest.getRating());

            tourRepository.save(tour);
            return ResponseEntity.ok().body(tour);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a tour
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable(value = "id") Integer id) {
        Tour tour = tourRepository.findById(id).orElse(null);
        if (tour == null) {
            return ResponseEntity.notFound().build();
        }
        tourRepository.delete(tour);
        return ResponseEntity.noContent().build();
    };
}
