package com.project.travel.controllers;

import com.project.travel.models.Place;
import com.project.travel.payload.request.PlaceRequest;
import com.project.travel.repository.PlaceRepository;
import com.project.travel.services.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    FilesStorageService storageService;

    @Value("${app.fileURL}")
    private String fileURL;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<Place> getAllPlace() {
        return placeRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable(value = "id") Integer id) {
        Place place = placeRepository.findById(id).orElse(null);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPlace(
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute PlaceRequest placeRequest
    ){
        Place place = new Place();
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!filename.equals("")){
            place.setImage(fileURL+filename);
            try{
                storageService.save(file);
            }catch(Exception e){
                logger.error(e.getMessage());
            }
        }

        place.setName(placeRequest.getName());
        place.setCity(placeRequest.getCity());
        place.setProvince(placeRequest.getProvince());
        place.setMapLink(placeRequest.getMapLink());

        placeRepository.save(place);
        return ResponseEntity.ok(place);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute PlaceRequest placeRequest
    ) {
        Place place = placeRepository.findById(id).orElse(null);
        if (place != null) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (!filename.equals("")){
                place.setImage(fileURL+filename);
                try{
                    storageService.save(file);
                }catch(Exception e){
                    logger.error(e.getMessage());
                }
            }

            place.setName(placeRequest.getName());
            place.setCity(placeRequest.getCity());
            place.setProvince(placeRequest.getProvince());
            place.setMapLink(placeRequest.getMapLink());

            placeRepository.save(place);
            return ResponseEntity.ok(place);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable(value = "id") Integer id) {
        Place place = placeRepository.findById(id).orElse(null);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        placeRepository.delete(place);
        return ResponseEntity.noContent().build();
    };

}
