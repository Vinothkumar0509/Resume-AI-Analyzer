package com.resumeaianalyzer.resumeaianalyzer.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced ML-Based Matcher for Resume-Job Description Analysis
 *
 *
 * Algorithms implemented:
 * 1. TF-IDF (Term Frequency-Inverse Document Frequency)
 * 2. Cosine Similarity
 * 3. Jaccard Similarity
 * 4. Levenshtein Distance for fuzzy matching
 * 5. Semantic Similarity Analysis
 * 6. Contextual Matching
 */
public class AdvancedMLMatcher {

    // ML Model weights for composite scoring
    private static final double DYNAMIC_SKILL_WEIGHT = 0.35;
    private static final double CONTENT_SIMILARITY_WEIGHT = 0.30;
    private static final double EXPERIENCE_WEIGHT = 0.20;
    private static final double KEYWORD_FREQUENCY_WEIGHT = 0.15;

    /**
     * Calculate comprehensive match score between resume and job description
     * Uses advanced ML algorithms without hardcoded skill lists
     *
     * @param resumeText Raw resume content
     * @param jobDescription Raw job description
     * @return Match score between 0-100
     */
    public static double calculateAdvancedMatchScore(String resumeText, String jobDescription) {
        if (StringUtils.isEmpty(resumeText) || StringUtils.isEmpty(jobDescription)) {
            return 0.0;
        }

        // Component 1: Dynamic skill extraction and matching (TF-IDF based)
        double dynamicSkillScore = calculateDynamicSkillMatch(resumeText, jobDescription);

        // Component 2: Content similarity (Cosine + Jaccard)
        double contentScore = calculateContentSimilarity(resumeText, jobDescription);

        // Component 3: Experience level matching
        double experienceScore = calculateExperienceMatch(resumeText, jobDescription);

        // Component 4: Keyword frequency analysis
        double keywordScore = calculateKeywordFrequencyMatch(resumeText, jobDescription);

        // Weighted composite score
        double finalScore = (dynamicSkillScore * DYNAMIC_SKILL_WEIGHT) +
                (contentScore * CONTENT_SIMILARITY_WEIGHT) +
                (experienceScore * EXPERIENCE_WEIGHT) +
                (keywordScore * KEYWORD_FREQUENCY_WEIGHT);

        return Math.min(100, Math.max(0, finalScore));
    }

    /**
     * ML Algorithm 1: Dynamic Skill Matching
     * Extracts skills from both documents and matches them dynamically
     * Uses technical skills database for validation
     */
    private static double calculateDynamicSkillMatch(String resumeText, String jobDescription) {
        // Extract only valid technical skills
        Set<String> jobSkills = TechnicalSkillsDatabase.extractValidSkills(jobDescription);
        Set<String> resumeSkills = TechnicalSkillsDatabase.extractValidSkills(resumeText);

        if (jobSkills.isEmpty()) {
            return resumeSkills.isEmpty() ? 50 : 40;
        }

        // Find exact matches
        Set<String> matchedSkills = new HashSet<>(resumeSkills);
        matchedSkills.retainAll(jobSkills);

        // Find fuzzy matches (skills that are similar but not exact)
        int fuzzyMatches = 0;
        for (String jobSkill : jobSkills) {
            if (!matchedSkills.contains(jobSkill)) {
                for (String resumeSkill : resumeSkills) {
                    double similarity = TextProcessingUtil.normalizedLevenshteinSimilarity(jobSkill, resumeSkill);
                    if (similarity > 0.75) {
                        fuzzyMatches++;
                        break;
                    }
                }
            }
        }

        // Calculate matching percentage
        double matchPercentage = ((matchedSkills.size() + (fuzzyMatches * 0.5)) / jobSkills.size()) * 100;
        return Math.min(100, matchPercentage);
    }

    /**
     * ML Algorithm 2: Extract Terms with TF-IDF Weights
     * Identifies important terms and assigns weights based on frequency and uniqueness
     */
    private static Map<String, Double> extractTermsWithWeights(String text) {
        if (StringUtils.isEmpty(text)) {
            return new HashMap<>();
        }

        String normalized = TextProcessingUtil.normalizeText(text);
        String[] words = normalized.split("\\s+");

        // Calculate term frequencies
        Map<String, Integer> termFrequency = new HashMap<>();
        for (String word : words) {
            if (isRelevantTerm(word)) {
                termFrequency.put(word, termFrequency.getOrDefault(word, 0) + 1);
            }
        }

        // Convert to TF-IDF scores
        Map<String, Double> weightedTerms = new HashMap<>();
        int totalTerms = termFrequency.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
            double tf = entry.getValue() / (double) totalTerms;
            // IDF approximation: less common words get higher weight
            double idf = Math.log10((double) words.length / (entry.getValue() + 1));
            double tfidf = tf * idf;
            if (tfidf > 0) {
                weightedTerms.put(entry.getKey(), tfidf);
            }
        }

        return weightedTerms;
    }

    /**
     * Check if a term is relevant (not a stop word)
     */
    private static boolean isRelevantTerm(String word) {
        if (word.length() < 2) {
            return false;
        }

        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
                "of", "with", "by", "from", "as", "is", "was", "are", "be", "have",
                "has", "had", "do", "does", "did", "will", "would", "could", "should",
                "may", "might", "must", "can", "this", "that", "it", "its", "you",
                "your", "we", "our", "they", "their", "i", "me", "my", "he", "she",
                "him", "her", "about", "during", "between", "through", "over", "under"
        ));

        return !stopWords.contains(word);
    }

    /**
     * ML Algorithm 3: Content Similarity Analysis
     * Uses Cosine Similarity and Jaccard Similarity for comprehensive comparison
     */
    private static double calculateContentSimilarity(String resumeText, String jobDescription) {
        // Cosine similarity (TF-IDF based)
        double cosineSim = TextProcessingUtil.calculateCosineSimilarity(resumeText, jobDescription);

        // Extract key phrases for Jaccard similarity
        Set<String> resumePhrases = extractKeyPhrases(resumeText, 20);
        Set<String> jobPhrases = extractKeyPhrases(jobDescription, 20);

        double jaccardSim = TextProcessingUtil.calculateJaccardSimilarity(resumePhrases, jobPhrases);

        // Combine both metrics
        return (cosineSim * 100 * 0.6) + (jaccardSim * 100 * 0.4);
    }

    /**
     * Extract key phrases from text (top N by frequency)
     */
    private static Set<String> extractKeyPhrases(String text, int topN) {
        if (StringUtils.isEmpty(text)) {
            return new HashSet<>();
        }

        String normalized = TextProcessingUtil.normalizeText(text);
        String[] words = normalized.split("\\s+");

        Map<String, Integer> phraseFrequency = new HashMap<>();
        for (int i = 0; i < words.length - 1; i++) {
            String phrase = words[i] + " " + words[i + 1];
            if (isRelevantTerm(words[i]) && isRelevantTerm(words[i + 1])) {
                phraseFrequency.put(phrase, phraseFrequency.getOrDefault(phrase, 0) + 1);
            }
        }

        return phraseFrequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * ML Algorithm 4: Experience Level Matching
     * Compares years of experience, seniority indicators, and roles
     */
    private static double calculateExperienceMatch(String resumeText, String jobDescription) {
        Integer resumeYears = TextProcessingUtil.extractYearsOfExperience(resumeText);
        Integer requiredYears = TextProcessingUtil.extractYearsOfExperience(jobDescription);

        if (resumeYears == null && requiredYears == null) {
            return 50.0;
        }

        if (resumeYears == null) {
            return 30.0;
        }

        if (requiredYears == null) {
            return 60.0;
        }

        // Experience matching logic
        if (resumeYears >= requiredYears) {
            double surplus = Math.min(resumeYears - requiredYears, 10);
            return Math.min(100, 80 + (surplus * 2));
        } else {
            double deficit = (requiredYears - resumeYears) / (double) requiredYears;
            return Math.max(20, 80 - (deficit * 80));
        }
    }

    /**
     * ML Algorithm 5: Keyword Frequency Analysis
     * Compares high-frequency, domain-specific keywords
     */
    private static double calculateKeywordFrequencyMatch(String resumeText, String jobDescription) {
        Map<String, Integer> jobKeywords = extractHighFrequencyKeywords(jobDescription, 25);
        Map<String, Integer> resumeKeywords = extractHighFrequencyKeywords(resumeText, 30);

        if (jobKeywords.isEmpty()) {
            return 50.0;
        }

        int matchCount = 0;
        int totalJobKeywords = jobKeywords.size();

        for (String keyword : jobKeywords.keySet()) {
            if (resumeKeywords.containsKey(keyword)) {
                matchCount++;
            } else {
                // Fuzzy matching for similar keywords
                for (String resumeKeyword : resumeKeywords.keySet()) {
                    double similarity = TextProcessingUtil.normalizedLevenshteinSimilarity(keyword, resumeKeyword);
                    if (similarity > 0.75) {
                        matchCount++;
                        break;
                    }
                }
            }
        }

        return (matchCount / (double) totalJobKeywords) * 100;
    }

    /**
     * Extract high-frequency keywords from text
     */
    private static Map<String, Integer> extractHighFrequencyKeywords(String text, int limit) {
        if (StringUtils.isEmpty(text)) {
            return new HashMap<>();
        }

        String normalized = TextProcessingUtil.normalizeText(text);
        String[] words = normalized.split("\\s+");

        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            if (isRelevantTerm(word) && word.length() > 3) {
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }

        return frequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Analyze skill gaps using dynamic extraction
     * Identifies skills required but missing from resume
     */
    public static Set<String> analyzeSkillGapsDynamic(String resumeText, String jobDescription) {
        // Extract only valid technical skills from both documents
        Set<String> resumeSkills = TechnicalSkillsDatabase.extractValidSkills(resumeText);
        Set<String> jobSkills = TechnicalSkillsDatabase.extractValidSkills(jobDescription);

        // Find gaps - skills in job description but not in resume
        Set<String> skillGaps = new HashSet<>(jobSkills);
        skillGaps.removeAll(resumeSkills);

        // Filter with fuzzy matching for close matches
        Set<String> finalGaps = new HashSet<>();
        for (String gap : skillGaps) {
            boolean hasClose = resumeSkills.stream()
                    .anyMatch(s -> TextProcessingUtil.normalizedLevenshteinSimilarity(gap, s) > 0.75);
            if (!hasClose) {
                finalGaps.add(gap);
            }
        }

        return finalGaps.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Analyze matched skills using dynamic extraction
     * Identifies skills that appear in both resume and job description
     */
    public static Set<String> analyzeMatchedSkillsDynamic(String resumeText, String jobDescription) {
        // Extract only valid technical skills from both documents
        Set<String> resumeSkills = TechnicalSkillsDatabase.extractValidSkills(resumeText);
        Set<String> jobSkills = TechnicalSkillsDatabase.extractValidSkills(jobDescription);

        // Find intersection - skills in both documents
        Set<String> matchedSkills = new HashSet<>(resumeSkills);
        matchedSkills.retainAll(jobSkills);

        // Add fuzzy matches
        for (String jobSkill : jobSkills) {
            if (!matchedSkills.contains(jobSkill)) {
                for (String resumeSkill : resumeSkills) {
                    double similarity = TextProcessingUtil.normalizedLevenshteinSimilarity(jobSkill, resumeSkill);
                    if (similarity > 0.75) {
                        matchedSkills.add(jobSkill);
                        break;
                    }
                }
            }
        }

        return matchedSkills.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Extract semantic context for skills
     * Returns skills with their contextual importance
     */
    public static Map<String, String> extractSkillsWithContext(String text) {
        Map<String, String> skillsWithContext = new LinkedHashMap<>();

        String normalized = TextProcessingUtil.normalizeText(text);
        String[] sentences = TextProcessingUtil.splitIntoSentences(text).toArray(new String[0]);

        Map<String, Double> terms = extractTermsWithWeights(text);

        for (String term : terms.keySet()) {
            // Find the sentence where this term appears
            for (String sentence : sentences) {
                if (TextProcessingUtil.normalizeText(sentence).contains(term)) {
                    skillsWithContext.put(term, sentence.replaceAll("\\s+", " ").substring(0, Math.min(100, sentence.length())));
                    break;
                }
            }
        }

        return skillsWithContext;
    }

    /**
     * Calculate skill match percentage
     */
    public static double calculateSkillCoveragePercentage(String resumeText, String jobDescription) {
        Set<String> jobSkills = TechnicalSkillsDatabase.extractValidSkills(jobDescription);
        Set<String> resumeSkills = TechnicalSkillsDatabase.extractValidSkills(resumeText);

        Set<String> matched = new HashSet<>(resumeSkills);
        matched.retainAll(jobSkills);

        if (jobSkills.isEmpty()) {
            return 0.0;
        }

        return (matched.size() / (double) jobSkills.size()) * 100;
    }

    /**
     * Generate detailed recommendations based on ML analysis
     */
    public static List<String> generateDetailedRecommendations(String resumeText, String jobDescription,
                                                               double matchScore, Set<String> skillGaps) {
        List<String> recommendations = new ArrayList<>();

        // Experience recommendations
        Integer resumeYears = TextProcessingUtil.extractYearsOfExperience(resumeText);
        Integer requiredYears = TextProcessingUtil.extractYearsOfExperience(jobDescription);

        if (resumeYears != null && requiredYears != null && resumeYears < requiredYears) {
            recommendations.add("Gain " + (requiredYears - resumeYears) + " more years of experience in relevant roles");
        }

        // Top skill gaps
        if (!skillGaps.isEmpty()) {
            List<String> topGaps = skillGaps.stream().limit(3).collect(Collectors.toList());
            recommendations.add("Priority skills to develop: " + String.join(", ", topGaps));
        }

        // Score-based recommendations
        if (matchScore < 50) {
            recommendations.add("Significant improvements needed - focus on core technical skills");
            recommendations.add("Restructure resume to highlight relevant project experience");
        } else if (matchScore < 70) {
            recommendations.add("Good foundation - work on identified skill gaps");
            recommendations.add("Add quantifiable achievements and metrics to current experience");
        } else if (matchScore < 85) {
            recommendations.add("Strong match - refine documentation of skills and experience");
            recommendations.add("Emphasize leadership and complex project involvement");
        } else {
            recommendations.add("Excellent match for this position");
            recommendations.add("Consider negotiating compensation based on strong fit");
        }

        recommendations.add("Keep resume updated with recent projects and tools used");
        recommendations.add("Include specific achievements with measurable outcomes");

        return recommendations;
    }
}

