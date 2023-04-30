package com.project.travel.controllers;

import com.project.travel.enums.ERole;
import com.project.travel.enums.ETour;
import com.project.travel.models.Interest;
import com.project.travel.models.Role;
import com.project.travel.models.User;
import com.project.travel.payload.request.SignupRequest;
import com.project.travel.payload.response.MessageResponse;
import com.project.travel.repository.InterestRepository;
import com.project.travel.repository.RoleRepository;
import com.project.travel.repository.UserRepository;
import com.project.travel.services.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    @Value("${app.fileURL}")
    private String fileURL;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);


    @GetMapping
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> registerUser(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody SignupRequest signUpRequest
    ) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("UserID is not exist!"));
        }

        User user = optionalUser.get();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        Set<String> strInterests = signUpRequest.getInterests();
        Set<Interest> interests = new HashSet<>();

        if (strInterests != null) {
            strInterests.forEach(item -> {
                Interest interest = interestRepository.findByKey(ETour.valueOf(item))
                        .orElseThrow(() -> new RuntimeException("Error:::Interest is not found"));
                interests.add(interest);
            });
        }

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setInterests(interests);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @PostMapping("/upload-avatar/{id}")
    public ResponseEntity<?> uploadsAvatar(
            @PathVariable(value = "id") Long id,
            @RequestParam("file") MultipartFile file
    ) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("UserID is not exist!"));
        }

        User user = optionalUser.get();
        //upload avatar image
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!filename.equals("")){
            user.setAvatar(fileURL + filename);
            try{
                storageService.save(file);
            }catch(Exception e){
                logger.error(e.getMessage());
                return ResponseEntity.badRequest().body(new MessageResponse("UserID is not exist!"));
            }
        }

        return ResponseEntity.ok().body(new MessageResponse("Avatar updated successfully!"));
    }
}
