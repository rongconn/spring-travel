package com.project.travel.controllers;

import com.project.travel.dto.JwtPayload;
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
import com.project.travel.security.jwt.JwtUtils;
import com.project.travel.services.FilesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private InterestRepository interestRepository;

    @Autowired
    private FilesStorageService storageService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.fileURL}")
    private String fileURL;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUser() {
        return userRepository.findAll();
    }



    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
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

        if (strRoles != null) {
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
                }
            });
        }
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        }

        if(!interests.isEmpty()) {
            user.setInterests(interests);
        }
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/upload-avatar/{id}")
    public ResponseEntity<?> uploadsAvatar(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable(value = "id") Long id,
            @RequestParam("file") MultipartFile file
    ) {
        String token = authorizationHeader.substring(7);
        JwtPayload jwtPayload = jwtUtils.getJwtPayloadFromToken(token, jwtSecret);

        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();

        if(!Objects.equals(jwtPayload.getUserId(), id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("UserID is not exist!"));
        }

        //upload avatar image
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!filename.equals("")){
            try{
                storageService.save(file);
            }catch(Exception e){
                logger.error(e.getMessage());
            }
            user.setAvatar(fileURL + filename);
        }

        userRepository.save(user);

        return ResponseEntity.ok().body(new MessageResponse("Avatar updated successfully!"));
    }
}
