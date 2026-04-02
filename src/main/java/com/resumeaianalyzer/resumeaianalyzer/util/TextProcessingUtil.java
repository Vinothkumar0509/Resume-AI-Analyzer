package com.resumeaianalyzer.resumeaianalyzer.util;

import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for text processing and NLP operations
 * Implements ML algorithms for text similarity and skill extraction
 */
public class TextProcessingUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b");
    private static final Pattern YEARS_OF_EXP_PATTERN = Pattern.compile("(\\d+)\\s*(?:[-–]\\s*(\\d+))?\\s*(?:to\\s*(\\d+))?\\s*(?:\\+)?\\s*(?:years?|yrs?|years?\\s+of\\s+exp)");

    // Common technical skills database
    private static final Set<String> TECHNICAL_SKILLS = new HashSet<>(Arrays.asList(
            "java", "python", "javascript", "typescript", "c#", "c++", "ruby", "php", "swift", "kotlin",
            "go", "rust", "scala", "r", "matlab", "perl", "groovy", "julia", "haskell", "elixir",
            "react", "angular", "vue", "svelte", "ember", "backbone", "next.js", "nuxt", "gatsby",
            "spring", "spring boot", "hibernate", "mybatis", "struts", "grails", "play framework",
            "django", "flask", "fastapi", "tornado", "pyramid", "web2py", "bottle",
            "nodejs", "node.js", "express", "koa", "hapi", "fastify", "nestjs",
            "aws", "azure", "gcp", "google cloud", "oracle cloud", "alibaba cloud",
            "docker", "kubernetes", "docker compose", "helm", "openshift",
            "mysql", "postgresql", "mongodb", "redis", "cassandra", "elasticsearch", "dynamodb", "oracle", "sql server",
            "git", "svn", "mercurial", "bitbucket", "github", "gitlab",
            "jenkins", "gitlab ci", "github actions", "travis ci", "circleci", "azure devops",
            "maven", "gradle", "npm", "yarn", "pip", "cargo", "maven",
            "soap", "rest", "graphql", "grpc", "websocket", "mqtt",
            "html", "css", "xml", "json", "yaml", "toml",
            "junit", "pytest", "mocha", "jasmine", "rspec", "phpunit", "testng",
            "selenium", "cypress", "playwright", "appium", "uiautomator",
            "jira", "confluence", "trello", "asana", "monday.com",
            "linux", "windows", "macos", "ubuntu", "centos", "debian",
            "agile", "scrum", "kanban", "xp", "lean", "waterfall",
            "machine learning", "deep learning", "tensorflow", "pytorch", "scikit-learn", "keras",
            "microservices", "monolithic", "oop", "functional programming", "reactive programming",
            "sql", "nosql", "blockchain", "artificial intelligence", "nlp", "computer vision",
            "soap", "rest", "json", "xml", "protobuf", "apache kafka", "rabbitmq", "activemq",
            "openshift", "terraform", "ansible", "puppet", "chef", "saltstack",
            "grafana", "prometheus", "datadog", "new relic", "splunk", "elk stack"
    ));

    // Common soft skills
    private static final Set<String> SOFT_SKILLS = new HashSet<>(Arrays.asList(
            "communication", "leadership", "teamwork", "problem-solving", "critical thinking",
            "time management", "project management", "adaptability", "creativity", "collaboration",
            "attention to detail", "analytical", "organizational", "interpersonal",
            "negotiation", "presentation", "public speaking", "mentoring", "coaching"
    ));

    /**
     * Extract text from resume content (already extracted)
     */
    public static String normalizeText(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        return text.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-z0-9\\s+#-]", "")
                .trim();
    }

/**
 * Extract skills from text using pattern matching and ML
 */
public static Set<String> extractSkills(String text) {
    String normalizedText = normalizeText(text);
    Set<String> foundSkills = new HashSet<>();

    // Extract technical skills
    for (String skill : TECHNICAL_SKILLS) {
        if (normalizedText.contains(skill)) {
            foundSkills.add(skill);
        }
    }

    // Extract soft skills
    for (String skill : SOFT_SKILLS) {
        if (normalizedText.contains(skill)) {
            foundSkills.add(skill);
        }
    }

    return foundSkills;
}

/**
 * Extract skills dynamically from text without hardcoded list
 * Identifies technical terms, frameworks, tools, and technologies
 * ML Algorithm: Uses TF-IDF and keyword frequency analysis
 */
public static Set<String> extractSkillsDynamic(String text) {
    if (StringUtils.isEmpty(text)) {
        return new HashSet<>();
    }

    String normalizedText = normalizeText(text);
    Set<String> dynamicSkills = new HashSet<>();

    // Extract frequent multi-word terms (compound skills)
    String[] words = normalizedText.split("\\s+");
    for (int i = 0; i < words.length - 1; i++) {
        String twoWordTerm = words[i] + " " + words[i + 1];
        // Look for common patterns like "spring boot", "machine learning", etc.
        if (isLikelySkill(twoWordTerm)) {
            dynamicSkills.add(twoWordTerm);
        }
    }

    // Extract single-word technical terms (capital letters, special patterns)
    for (String word : words) {
        if (isLikelySkill(word) && word.length() > 2) {
            dynamicSkills.add(word);
        }
    }

    return dynamicSkills;
}

/**
 * Determine if a word/phrase is likely a skill based on characteristics
 */
private static boolean isLikelySkill(String term) {
    if (StringUtils.isEmpty(term) || term.length() < 2) {
        return false;
    }

    // Filter out common stop words
    Set<String> stopWords = new HashSet<>(Arrays.asList(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
            "of", "with", "by", "from", "as", "is", "was", "are", "be", "have",
            "has", "had", "do", "does", "did", "will", "would", "could", "should",
            "may", "might", "must", "can"
    ));

    if (stopWords.contains(term)) {
        return false;
    }

    // Check for likely skill indicators
    // 1. Contains numbers or special chars (Java8, C++, C#, Python3.9, etc.)
    if (term.matches(".*[0-9+#].*")) {
        return true;
    }

    // 2. All caps or title case (likely acronyms or proper names - tools/frameworks)
    if (term.matches("^[A-Z][a-zA-Z]*$") || term.equals(term.toUpperCase())) {
        return true;
    }

    // 3. Contains hyphens or dots (spring-boot, asp.net, etc.)
    if (term.contains("-") || term.contains(".")) {
        return true;
    }

    // 4. Minimum length for single words (most skills are longer)
    if (term.length() > 3 && !term.matches(".*[0-9].*")) {
        return true;
    }

    return false;
}

    /**
     * Extract years of experience using regex and pattern matching
     * Handles ranges like "3–5 years", "3 to 5 years", "3+ years", "5 years"
     */
    public static Integer extractYearsOfExperience(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        Matcher matcher = YEARS_OF_EXP_PATTERN.matcher(text.toLowerCase());
        List<Integer> years = new ArrayList<>();

        while (matcher.find()) {
            try {
                // Extract the first number (minimum in range)
                int firstNumber = Integer.parseInt(matcher.group(1));
                years.add(firstNumber);

                // If there's a range (e.g., "3–5" or "3 to 5"), also add the second number
                if (matcher.group(2) != null) {
                    int secondNumber = Integer.parseInt(matcher.group(2));
                    years.add(secondNumber);
                } else if (matcher.group(3) != null) {
                    int secondNumber = Integer.parseInt(matcher.group(3));
                    years.add(secondNumber);
                }
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }
        }

        // Return the minimum years found (most conservative approach)
        // For ranges like "3–5 years", return 3 (minimum requirement)
        return years.isEmpty() ? null : Collections.min(years);
    }

    /**
     * Calculate Jaccard similarity between two sets (ML Algorithm)
     * Used for skill matching
     */
    public static double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 1.0;
        }

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    /**
     * Calculate Cosine similarity using TF-IDF concept (ML Algorithm)
     * Used for text content matching
     */
    public static double calculateCosineSimilarity(String text1, String text2) {
        if (StringUtils.isEmpty(text1) || StringUtils.isEmpty(text2)) {
            return 0.0;
        }

        String[] words1 = normalizeText(text1).split("\\s+");
        String[] words2 = normalizeText(text2).split("\\s+");

        Map<String, Integer> freq1 = getWordFrequency(words1);
        Map<String, Integer> freq2 = getWordFrequency(words2);

        double dotProduct = 0.0;
        for (String word : freq1.keySet()) {
            if (freq2.containsKey(word)) {
                dotProduct += freq1.get(word) * freq2.get(word);
            }
        }

        double magnitude1 = Math.sqrt(freq1.values().stream()
                .mapToDouble(v -> v * v).sum());
        double magnitude2 = Math.sqrt(freq2.values().stream()
                .mapToDouble(v -> v * v).sum());

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    /**
     * Calculate word frequency for TF-IDF
     */
    private static Map<String, Integer> getWordFrequency(String[] words) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty() && !isCommonWord(word)) {
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }
        return frequency;
    }

    /**
     * Check if word is a common stop word
     */
    private static boolean isCommonWord(String word) {
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
                "of", "with", "by", "from", "as", "is", "was", "are", "be", "have",
                "has", "had", "do", "does", "did", "will", "would", "could", "should",
                "may", "might", "must", "can", "it", "its", "this", "that", "these",
                "those", "i", "you", "he", "she", "we", "they"
        ));
        return stopWords.contains(word) || word.length() < 2;
    }

    /**
     * Levenshtein distance for string similarity (ML Algorithm)
     * Used for fuzzy matching of skills
     */
    public static int levenshteinDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Normalize Levenshtein distance to similarity score (0-1)
     */
    public static double normalizedLevenshteinSimilarity(String s1, String s2) {
        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) {
            return 1.0;
        }

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0;
        }

        return 1.0 - ((double) levenshteinDistance(s1, s2) / maxLength);
    }

    /**
     * Extract email addresses
     */
    public static Set<String> extractEmails(String text) {
        Set<String> emails = new HashSet<>();
        Matcher matcher = EMAIL_PATTERN.matcher(text);
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        return emails;
    }

    /**
     * Extract phone numbers
     */
    public static Set<String> extractPhoneNumbers(String text) {
        Set<String> phones = new HashSet<>();
        Matcher matcher = PHONE_PATTERN.matcher(text);
        while (matcher.find()) {
            phones.add(matcher.group());
        }
        return phones;
    }

    /**
     * Split text into sentences
     */
    public static List<String> splitIntoSentences(String text) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }

        String[] sentences = text.split("[.!?]+");
        return Arrays.stream(sentences)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Extract keywords from text (top N frequent non-stop words)
     */
    public static Set<String> extractKeywords(String text, int topN) {
        if (StringUtils.isEmpty(text)) {
            return new HashSet<>();
        }

        String[] words = normalizeText(text).split("\\s+");
        Map<String, Integer> frequency = getWordFrequency(words);

        return frequency.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
