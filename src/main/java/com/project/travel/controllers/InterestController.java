package com.project.travel.controllers;

import com.project.travel.models.Interest;
import com.project.travel.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/interest")
public class InterestController {
    @Autowired
    private InterestRepository interestRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<Interest> getAll() {
        return interestRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Interest> getInterestById(@PathVariable(value = "id") Integer id) {
        Interest tour = interestRepository.findById(id).orElse(null);
        if (tour == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(tour);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createInterest(@Valid @RequestBody Interest interest) {
        interestRepository.save(interest);
        return ResponseEntity.ok(interest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInterest(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody Interest interest
    ) {
        Interest updateInterest = interestRepository.findById(id)
                        .orElse(null);
        if(updateInterest != null) {
            updateInterest.setName(interest.getName());
            updateInterest.setKey(interest.getKey());
            interestRepository.save(updateInterest);
            return ResponseEntity.ok(updateInterest);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterest(@PathVariable(value = "id") Integer id) {
        Interest interest = interestRepository.findById(id).orElse(null);
        if (interest == null) {
            return ResponseEntity.notFound().build();
        }
        interestRepository.delete(interest);
        return ResponseEntity.noContent().build();
    };
}
