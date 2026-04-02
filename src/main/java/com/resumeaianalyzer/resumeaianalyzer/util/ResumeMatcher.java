package com.resumeaianalyzer.resumeaianalyzer.util;

import com.resumeaianalyzer.resumeaianalyzer.bean.SkillResults;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ML-based algorithm for matching resume with job description
 * Implements multiple matching strategies for accurate analysis
 */
public class ResumeMatcher {

    // Weight configuration for scoring (adjustable for different use cases)
    private static final double SKILL_MATCH_WEIGHT = 0.40;
    private static final double EXPERIENCE_WEIGHT = 0.25;
    private static final double KEYWORD_MATCH_WEIGHT = 0.20;
    private static final double CONTENT_SIMILARITY_WEIGHT = 0.15;

/**
 * Calculate overall match score between resume and job description
 * Uses weighted ML algorithm with dynamic skill extraction
 * No hardcoded skill lists - compares resume and job description directly
 */
public static double calculateMatchScore(String resumeText, String jobDescription) {
    if (StringUtils.isEmpty(resumeText) || StringUtils.isEmpty(jobDescription)) {
        return 0.0;
    }

    // Use advanced ML matcher for dynamic comparison
    return AdvancedMLMatcher.calculateAdvancedMatchScore(resumeText, jobDescription);
}

    /**
     * Calculate skill match score using Jaccard Similarity (ML Algorithm)
     */
    private static double calculateSkillMatchScore(Set<String> resumeSkills, Set<String> jobSkills) {
        if (jobSkills.isEmpty()) {
            return resumeSkills.isEmpty() ? 100 : 50;
        }

        double jaccardSimilarity = TextProcessingUtil.calculateJaccardSimilarity(resumeSkills, jobSkills);

        // Calculate how many required skills are matched
        Set<String> matchedSkills = new HashSet<>(resumeSkills);
        matchedSkills.retainAll(jobSkills);

        // Bonus: Also consider fuzzy matches (skills that are similar but not exact)
        double fuzzyMatchBonus = calculateFuzzySkillMatches(resumeSkills, jobSkills);

        double score = (jaccardSimilarity * 70) + (fuzzyMatchBonus * 30);

        // Calculate coverage percentage
        double coveragePercentage = (matchedSkills.size() / (double) jobSkills.size()) * 100;

        // Apply coverage boost
        return Math.min(100, score * (1 + (coveragePercentage / 500)));
    }

    /**
     * Calculate fuzzy skill matches using Levenshtein distance
     */
    private static double calculateFuzzySkillMatches(Set<String> resumeSkills, Set<String> jobSkills) {
        double fuzzyMatchCount = 0;

        for (String jobSkill : jobSkills) {
            for (String resumeSkill : resumeSkills) {
                // If similarity > 0.75, consider it a match
                if (TextProcessingUtil.normalizedLevenshteinSimilarity(jobSkill, resumeSkill) > 0.75) {
                    fuzzyMatchCount += 0.5; // Partial credit for fuzzy matches
                    break;
                }
            }
        }

        if (jobSkills.isEmpty()) {
            return 0;
        }

        return (fuzzyMatchCount / jobSkills.size()) * 100;
    }

    /**
     * Calculate experience match score
     * Considers required years of experience vs candidate's experience
     */
    public static double calculateExperienceScore(String resumeText, String jobDescription) {
        Integer resumeYears = TextProcessingUtil.extractYearsOfExperience(resumeText);
        Integer requiredYears = TextProcessingUtil.extractYearsOfExperience(jobDescription);

        // If no experience info found in either, assume neutral
        if (resumeYears == null && requiredYears == null) {
            return 50.0;
        }

        if (resumeYears == null) {
            return 30.0; // No experience mentioned in resume
        }

        if (requiredYears == null) {
            return 60.0; // No requirement specified, partial match
        }

        // Calculate experience fit
        if (resumeYears >= requiredYears) {
            // Candidate has sufficient or more experience
            double bonus = Math.min(20, (resumeYears - requiredYears) * 5);
            return 100.0 - (Math.abs(resumeYears - requiredYears) > 10 ? -10 : 0) + bonus;
        } else {
            // Candidate has less experience
            double deficit = (double) (requiredYears - resumeYears) / requiredYears;
            return Math.max(20, 100.0 - (deficit * 100));
        }
    }

    /**
     * Calculate keyword match score
     * Extracts key terms and checks for matching
     */
    private static double calculateKeywordMatchScore(String resumeText, String jobDescription) {
        Set<String> jobKeywords = TextProcessingUtil.extractKeywords(jobDescription, 15);
        Set<String> resumeKeywords = TextProcessingUtil.extractKeywords(resumeText, 20);

        if (jobKeywords.isEmpty()) {
            return 50.0;
        }

        Set<String> matchedKeywords = new HashSet<>(jobKeywords);
        matchedKeywords.retainAll(resumeKeywords);

        double matchPercentage = (matchedKeywords.size() / (double) jobKeywords.size()) * 100;

        // Apply partial credit for substring matches
        for (String jobKeyword : jobKeywords) {
            if (!matchedKeywords.contains(jobKeyword)) {
                for (String resumeKeyword : resumeKeywords) {
                    if (resumeKeyword.contains(jobKeyword) || jobKeyword.contains(resumeKeyword)) {
                        matchPercentage += 5;
                        break;
                    }
                }
            }
        }

        return Math.min(100, matchPercentage);
    }

/**
 * Analyze skill gaps between resume and job description
 * Uses technical skills database for validation
 */
public static SkillResults analyzeSkillGaps(String resumeText, String jobDescription) {
    Set<String> skillGaps = AdvancedMLMatcher.analyzeSkillGapsDynamic(resumeText, jobDescription);

    // Limit to top 10 gaps for clarity
    List<String> topGaps = skillGaps.stream()
            .limit(10)
            .collect(Collectors.toList());

    return SkillResults.builder()
            .name("Skill Gaps")
            .values(topGaps)
            .build();
}

/**
 * Analyze matched skills between resume and job description
 * Uses technical skills database for validation
 */
public static SkillResults analyzeMatchedSkills(String resumeText, String jobDescription) {
    Set<String> matchedSkills = AdvancedMLMatcher.analyzeMatchedSkillsDynamic(resumeText, jobDescription);

    // Limit to top 15 matched skills for clarity
    List<String> topMatches = matchedSkills.stream()
            .limit(15)
            .collect(Collectors.toList());

    return SkillResults.builder()
            .name("Matched Skills")
            .values(topMatches)
            .build();
}

/**
 * Analyze present skills in resume
 * Uses technical skills database for validation
 */
public static SkillResults analyzePresentSkills(String resumeText) {
    // Extract only valid technical skills from resume
    Set<String> resumeSkills = TechnicalSkillsDatabase.extractValidSkills(resumeText);

    // Limit to top 20 skills for clarity
    List<String> topSkills = resumeSkills.stream()
            .limit(20)
            .collect(Collectors.toList());

    return SkillResults.builder()
            .name("Present Skills")
            .values(topSkills)
            .build();
}

/**
 * Generate recommendations based on ML analysis
 * Uses advanced ML matcher for detailed analysis
 */
public static List<String> generateRecommendations(String resumeText, String jobDescription,
                                                   double matchScore, Set<String> skillGaps) {
    return AdvancedMLMatcher.generateDetailedRecommendations(resumeText, jobDescription, matchScore, skillGaps);
}

    /**
     * Calculate soft skills match
     */
    public static double calculateSoftSkillsMatch(String resumeText, String jobDescription) {
        String resumeNorm = TextProcessingUtil.normalizeText(resumeText);
        String jobNorm = TextProcessingUtil.normalizeText(jobDescription);

        // Common soft skill keywords
        String[] softSkillKeywords = {
                "leadership", "communication", "teamwork", "problem-solving",
                "analytical", "creative", "organized", "detail-oriented",
                "collaborative", "adaptable", "initiative", "motivated"
        };

        int matchCount = 0;
        for (String keyword : softSkillKeywords) {
            if (resumeNorm.contains(keyword) && jobNorm.contains(keyword)) {
                matchCount++;
            }
        }

        return (matchCount / (double) softSkillKeywords.length) * 100;
    }
}


