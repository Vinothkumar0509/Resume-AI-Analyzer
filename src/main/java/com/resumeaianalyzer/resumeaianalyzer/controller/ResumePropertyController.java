package com.resumeaianalyzer.resumeaianalyzer.controller;

import com.resumeaianalyzer.resumeaianalyzer.bean.ResumeAnalyzerResponse;
import com.resumeaianalyzer.resumeaianalyzer.service.ResumeAIAnalyzerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Resume Analysis
 * Handles resume upload and analysis requests
 */
@RestController
@RequestMapping("/resume-analyzer")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ResumePropertyController {

    private static final Logger logger = LoggerFactory.getLogger(ResumePropertyController.class);

    private final ResumeAIAnalyzerService resumeAIAnalyzerService;

    /**
     * Analyze resume against job description
     *
     * @param resumeFile Multipart resume file (PDF, DOCX, TXT)
     * @param jobDescription Job description as direct text paragraph (string, no URLs)
     * @return ResumeAnalyzerResponse with analysis results
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestParam MultipartFile resumeFile,
                                     @RequestParam String jobDescription) {
        try {
            logger.info("Received analysis request - File: {}, Job Desc Length: {}",
                    resumeFile.getOriginalFilename(), jobDescription.length());

            // Call service to analyze resume
            ResumeAnalyzerResponse response = resumeAIAnalyzerService.analyzeResume(resumeFile, jobDescription);

            logger.info("Analysis completed successfully - Match Score: {}", response.getResumeScore());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (RuntimeException e) {
            logger.error("Processing error: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Processing Error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error during analysis", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "An unexpected error occurred. Please try again.");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Resume AI Analyzer");
        return ResponseEntity.ok(response);
    }
}
