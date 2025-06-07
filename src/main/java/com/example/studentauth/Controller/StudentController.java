package com.example.studentauth.Controller;

import com.example.studentauth.DTOs.UserDTO;
import com.example.studentauth.DTOs.ValidateTokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private RestTemplate restTemplate;

    private final String OAUTH_BASE_URL = "http://localhost:8080/auth";

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody UserDTO userDTO)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                OAUTH_BASE_URL + "/register", request, String.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestBody UserDTO userDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);

            // Use String.class or Map.class if you update AuthController to return both user and token
            ResponseEntity<String> response = restTemplate.postForEntity(
                    OAUTH_BASE_URL + "/login", request, String.class);

            return new ResponseEntity<>(response.getBody(), response.getHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateStudent(@RequestBody ValidateTokenDto validateTokenDto) {
        boolean result = Boolean.TRUE.equals(restTemplate.postForObject(OAUTH_BASE_URL + "/validate", validateTokenDto, Boolean.class));

        if(result){
            return new ResponseEntity<>("Validate successful", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Not Successful", HttpStatus.UNAUTHORIZED);
        }

    }
}
