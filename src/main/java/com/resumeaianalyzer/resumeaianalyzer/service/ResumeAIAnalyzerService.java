package com.resumeaianalyzer.resumeaianalyzer.service;

import com.resumeaianalyzer.resumeaianalyzer.bean.ExperienceProperties;
import com.resumeaianalyzer.resumeaianalyzer.bean.MatchType;
import com.resumeaianalyzer.resumeaianalyzer.bean.ResumeAnalyzerResponse;
import com.resumeaianalyzer.resumeaianalyzer.bean.SkillResults;
import com.resumeaianalyzer.resumeaianalyzer.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * ML-Based Resume AI Analyzer Service
 *
 * This service implements advanced machine learning algorithms for:
 * 1. Skill extraction and matching (Jaccard Similarity)
 * 2. Text similarity analysis (Cosine Similarity, TF-IDF)
 * 3. Experience level matching
 * 4. Comprehensive resume-to-job-description analysis
 * 5. Fuzzy string matching (Levenshtein Distance)
 * 6. Recommendations generation
 */
@Service
public class ResumeAIAnalyzerService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeAIAnalyzerService.class);
    private static final long MAX_FILE_SIZE = 10L; // 10 MB

    /**
     * Analyze resume against job description
     *
     * @param resumeFile The uploaded resume file (PDF, DOCX, TXT)
     * @param jobDescriptionInput Can be either a URL or raw job description text
     * @return ResumeAnalyzerResponse with detailed analysis
     */
    public ResumeAnalyzerResponse analyzeResume(MultipartFile resumeFile, String jobDescriptionInput) {
        try {
            logger.info("Starting resume analysis...");

            // Validate inputs
            validateInputs(resumeFile, jobDescriptionInput);

            // Step 1: Extract text from resume file
            String resumeText = extractResumeText(resumeFile);
            logger.debug("Resume text extracted successfully. Length: {}", resumeText.length());

            // Step 2: Get job description (from URL or direct text)
            String jobDescription = getJobDescription(jobDescriptionInput);
            logger.debug("Job description obtained successfully. Length: {}", jobDescription.length());

            // Step 3: Run ML-based matching algorithm
            double matchScore = ResumeMatcher.calculateMatchScore(resumeText, jobDescription);
            logger.info("Match score calculated: {}", matchScore);

            // Step 4: Determine match type
            MatchType matchType = MatchType.fromScore((int) Math.round(matchScore));
            logger.info("Match type determined: {}", matchType.getLabel());

            // Step 5: Analyze skill gaps
            SkillResults skillGaps = ResumeMatcher.analyzeSkillGaps(resumeText, jobDescription);
            logger.debug("Skill gaps analyzed: {} gaps found", skillGaps.getValues().size());

            // Step 6: Analyze matched skills
            SkillResults matchedSkills = ResumeMatcher.analyzeMatchedSkills(resumeText, jobDescription);
            logger.debug("Matched skills analyzed: {} matches", matchedSkills.getValues().size());

            // Step 7: Analyze present skills
            SkillResults presentSkills = ResumeMatcher.analyzePresentSkills(resumeText);
            logger.debug("Present skills analyzed: {} skills", presentSkills.getValues().size());

            // Step 8: Analyze experience and location
            ExperienceProperties experienceProps = analyzeExperienceAndLocation(resumeText, jobDescription,
                    new HashSet<>(skillGaps.getValues()), matchScore);
            logger.debug("Experience and location analyzed");

            // Step 9: Generate recommendations
            List<String> recommendations = ResumeMatcher.generateRecommendations(
                    resumeText, jobDescription, matchScore, new HashSet<>(skillGaps.getValues()));
            experienceProps.setRecommendations(recommendations);

            // Step 10: Build response
            ResumeAnalyzerResponse response = ResumeAnalyzerResponse.builder()
                    .resumeScore(Math.round(matchScore * 100.0) / 100.0) // Round to 2 decimals
                    .matchType(matchType.getLabel())
                    .description(matchType.getDescription())
                    .skillResults(Arrays.asList(matchedSkills, skillGaps, presentSkills))
                    .experienceProperties(experienceProps)
                    .build();

            logger.info("Resume analysis completed successfully");
            return response;

        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (IOException | TikaException e) {
            logger.error("Error processing file: {}", e.getMessage());
            throw new RuntimeException("Error processing resume file: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during analysis: {}", e.getMessage(), e);
            throw new RuntimeException("Error analyzing resume: " + e.getMessage());
        }
    }

    /**
     * Extract text from resume file
     */
    private String extractResumeText(MultipartFile resumeFile) throws IOException, TikaException {
        // Validate file size
        DocumentProcessorUtil.validateFileSize(resumeFile, MAX_FILE_SIZE);

        // Extract text using Apache Tika
        String rawText = DocumentProcessorUtil.extractTextFromFile(resumeFile);

        if (StringUtils.isEmpty(rawText)) {
            throw new IllegalArgumentException("Could not extract text from resume file");
        }

        return rawText;
    }

    /**
     * Get job description from provided text paragraph
     *
     * @param jobDescriptionInput Job description as direct text paragraph (no URL support)
     * @return Validated job description text
     */
    private String getJobDescription(String jobDescriptionInput) {
        if (StringUtils.isEmpty(jobDescriptionInput)) {
            throw new IllegalArgumentException("Job description cannot be empty");
        }

        logger.info("Received job description as text paragraph. Length: {}", jobDescriptionInput.length());

        // Validate that it's meaningful content (not just a URL)
        if (jobDescriptionInput.toLowerCase().startsWith("http://") ||
            jobDescriptionInput.toLowerCase().startsWith("https://")) {
            throw new IllegalArgumentException("URL input not supported. Please provide job description as direct text paragraph.");
        }

        // Return as plain text
        return jobDescriptionInput;
    }

    /**
     * Analyze experience and location
     */
    private ExperienceProperties analyzeExperienceAndLocation(String resumeText, String jobDescription,
                                                             Set<String> skillGaps, double matchScore) {
        Map<String, String> experienceAndLocations = new HashMap<>();

        // Extract years of experience
        Double resumeYears = TextProcessingUtil.extractYearsOfExperience(resumeText);
        Double requiredYears = TextProcessingUtil.extractYearsOfExperience(jobDescription);

        if (resumeYears != null) {
            experienceAndLocations.put("Candidate Experience", resumeYears + " years");
        } else {
            experienceAndLocations.put("Candidate Experience", "Not specified");
        }

        if (requiredYears != null) {
            experienceAndLocations.put("Required Experience", requiredYears + " years");
        } else {
            experienceAndLocations.put("Required Experience", "Not specified");
        }

        // Calculate experience match
        double experienceScore = ResumeMatcher.calculateExperienceScore(resumeText, jobDescription);
        experienceAndLocations.put("Experience Match", String.format("%.1f%%", experienceScore));

        // Extract emails and contact info
        Set<String> emails = TextProcessingUtil.extractEmails(resumeText);
        if (!emails.isEmpty()) {
            experienceAndLocations.put("Contact", "Provided");
        }

        // Calculate dynamic skill coverage percentage
        double skillCoverage = AdvancedMLMatcher.calculateSkillCoveragePercentage(resumeText, jobDescription);
        experienceAndLocations.put("Skill Coverage", String.format("%.1f%%", skillCoverage));

        // Extract soft skills analysis
        double softSkillsScore = ResumeMatcher.calculateSoftSkillsMatch(resumeText, jobDescription);
        experienceAndLocations.put("Soft Skills Match", String.format("%.1f%%", softSkillsScore));

        // Status based on match score
        String status = determineStatus(matchScore);
        experienceAndLocations.put("Status", status);

        return ExperienceProperties.builder()
                .experienceAndLocations(experienceAndLocations)
                .build();
    }

    /**
     * Determine candidate status based on match score
     */
    private String determineStatus(double matchScore) {
        if (matchScore >= 80) {
            return "Highly Recommended";
        } else if (matchScore >= 65) {
            return "Recommended";
        } else if (matchScore >= 50) {
            return "Consider";
        } else {
            return "Not Recommended";
        }
    }

    /**
     * Validate input parameters
     */
    private void validateInputs(MultipartFile resumeFile, String jobDescription) {
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required and cannot be empty");
        }

        if (StringUtils.isEmpty(jobDescription)) {
            throw new IllegalArgumentException("Job description is required and cannot be empty");
        }
    }
}
