package com.resumeaianalyzer.resumeaianalyzer.util;

import com.resumeaianalyzer.resumeaianalyzer.bean.MatchType;
import com.resumeaianalyzer.resumeaianalyzer.bean.ResumeAnalyzerResponse;
import com.resumeaianalyzer.resumeaianalyzer.bean.SkillResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Sample Resume and Job Description for testing the ML algorithms
 * This class demonstrates the capabilities of the Resume AI Analyzer
 */
public class MLAlgorithmTester {

    private static final Logger logger = LoggerFactory.getLogger(MLAlgorithmTester.class);

    /**
     * Test the ML algorithms with sample data
     */
    public static void main(String[] args) {
        logger.info("=== Resume AI Analyzer - ML Algorithm Test ===\n");

        String sampleResume = getSampleResume();
        String sampleJobDescription = getSampleJobDescription();

        logger.info("Testing ML Algorithms...\n");

        // Test 1: Skill Extraction
        logger.info("1. Testing Skill Extraction Algorithm");
        Set<String> resumeSkills = TextProcessingUtil.extractSkills(sampleResume);
        Set<String> jobSkills = TextProcessingUtil.extractSkills(sampleJobDescription);
        logger.info("   Resume Skills Found: {}", resumeSkills);
        logger.info("   Job Skills Required: {}\n", jobSkills);

        // Test 2: Jaccard Similarity
        logger.info("2. Testing Jaccard Similarity (Skill Matching)");
        double jaccardScore = TextProcessingUtil.calculateJaccardSimilarity(resumeSkills, jobSkills);
        logger.info("   Jaccard Similarity Score: {}\n", jaccardScore);

        // Test 3: Cosine Similarity
        logger.info("3. Testing Cosine Similarity (Content Matching)");
        double cosineScore = TextProcessingUtil.calculateCosineSimilarity(sampleResume, sampleJobDescription);
        logger.info("   Cosine Similarity Score: {}\n", cosineScore);

        // Test 4: Levenshtein Distance (Fuzzy Matching)
        logger.info("4. Testing Levenshtein Distance (Fuzzy Matching)");
        String skill1 = "React";
        String skill2 = "React Native";
        int distance = TextProcessingUtil.levenshteinDistance(skill1, skill2);
        double similarity = TextProcessingUtil.normalizedLevenshteinSimilarity(skill1, skill2);
        logger.info("   Distance between '{}' and '{}': {}", skill1, skill2, distance);
        logger.info("   Normalized Similarity: {}\n", similarity);

        // Test 5: Experience Extraction
        logger.info("5. Testing Experience Extraction");
        Double resumeYears = TextProcessingUtil.extractYearsOfExperience(sampleResume);
        Double jobYears = TextProcessingUtil.extractYearsOfExperience(sampleJobDescription);
        logger.info("   Resume Years of Experience: {}", resumeYears);
        logger.info("   Required Years of Experience: {}\n", jobYears);

        // Test 6: Match Score Calculation
        logger.info("6. Testing Overall Match Score Calculation");
        double matchScore = ResumeMatcher.calculateMatchScore(sampleResume, sampleJobDescription);
        logger.info("   Overall Match Score: {}", matchScore);
        MatchType matchType = MatchType.fromScore((int) Math.round(matchScore));
        logger.info("   Match Type: {}", matchType.getLabel());
        logger.info("   Description: {}\n", matchType.getDescription());

        // Test 7: Skill Gap Analysis
        logger.info("7. Testing Skill Gap Analysis");
        SkillResults skillGaps = ResumeMatcher.analyzeSkillGaps(sampleResume, sampleJobDescription);
        logger.info("   Missing Skills: {}\n", skillGaps.getValues());

        // Test 8: Matched Skills Analysis
        logger.info("8. Testing Matched Skills Analysis");
        SkillResults matchedSkills = ResumeMatcher.analyzeMatchedSkills(sampleResume, sampleJobDescription);
        logger.info("   Matched Skills: {}\n", matchedSkills.getValues());

        // Test 9: Keyword Extraction
        logger.info("9. Testing Keyword Extraction");
        Set<String> keywords = TextProcessingUtil.extractKeywords(sampleJobDescription, 10);
        logger.info("   Top Keywords in Job Description: {}\n", keywords);

        // Test 10: Soft Skills Matching
        logger.info("10. Testing Soft Skills Matching");
        double softSkillsScore = ResumeMatcher.calculateSoftSkillsMatch(sampleResume, sampleJobDescription);
        logger.info("   Soft Skills Match Score: {}\n", softSkillsScore);

        logger.info("=== All Algorithm Tests Completed Successfully ===");
    }

    private static String getSampleResume() {
        return "SENIOR JAVA DEVELOPER\n" +
                "\n" +
                "Professional Summary\n" +
                "Experienced Senior Java Developer with 6 years of software development experience. " +
                "Strong expertise in building scalable microservices using Spring Boot and Docker. " +
                "Proficient in full-stack development with React and JavaScript. " +
                "Proven track record in leading development teams and mentoring junior developers.\n" +
                "\n" +
                "Technical Skills\n" +
                "Languages: Java, Python, JavaScript, TypeScript, SQL\n" +
                "Frameworks: Spring Boot, Spring Cloud, Spring Data JPA, Hibernate\n" +
                "Frontend: React, Angular, HTML5, CSS3\n" +
                "Cloud & DevOps: AWS, Docker, Kubernetes, Jenkins, GitLab CI\n" +
                "Databases: MySQL, PostgreSQL, MongoDB, Redis\n" +
                "Tools: Git, Maven, Gradle, JIRA\n" +
                "Methodologies: Agile, Scrum, Kanban\n" +
                "\n" +
                "Professional Experience\n" +
                "\n" +
                "Senior Developer at Tech Solutions Inc. (2021 - Present)\n" +
                "- Designed and implemented microservices architecture using Spring Boot and Docker\n" +
                "- Led team of 5 developers in building payment processing system\n" +
                "- Improved system performance by 40% through database optimization\n" +
                "- Mentored junior developers in Java best practices and design patterns\n" +
                "\n" +
                "Java Developer at Digital Innovations Ltd. (2018 - 2021)\n" +
                "- Developed RESTful APIs using Spring Boot for e-commerce platform\n" +
                "- Implemented containerization using Docker for deployment\n" +
                "- Collaborated with cross-functional teams in Agile environment\n" +
                "\n" +
                "Education\n" +
                "Bachelor's in Computer Science, University of Technology\n" +
                "\n" +
                "Certifications\n" +
                "- AWS Solutions Architect Associate\n" +
                "- Certified Kubernetes Administrator (CKA)\n" +
                "\n" +
                "Contact: john.developer@email.com | LinkedIn: /in/johndeveloper";
    }

    private static String getSampleJobDescription() {
        return "Senior Java Developer\n" +
                "\n" +
                "Company: Global Tech Corp\n" +
                "Location: San Francisco, CA (Remote Eligible)\n" +
                "Salary: $120,000 - $160,000\n" +
                "\n" +
                "Job Description\n" +
                "We are looking for a Senior Java Developer to join our growing engineering team. " +
                "You will work on building scalable microservices and cloud-native applications.\n" +
                "\n" +
                "Requirements\n" +
                "- 5+ years of professional Java development experience\n" +
                "- Strong expertise in Spring Boot and Spring Framework\n" +
                "- Experience with Docker and Kubernetes containerization\n" +
                "- Proficiency in building RESTful APIs\n" +
                "- Experience with relational databases (MySQL, PostgreSQL)\n" +
                "- Knowledge of cloud platforms (AWS or Azure)\n" +
                "- Understanding of microservices architecture\n" +
                "- Experience with CI/CD pipelines (Jenkins, GitLab CI)\n" +
                "- Bachelor's degree in Computer Science or related field\n" +
                "\n" +
                "Nice to Have\n" +
                "- Experience with GraphQL\n" +
                "- Knowledge of Kubernetes orchestration\n" +
                "- AWS certification\n" +
                "- Experience with Apache Kafka\n" +
                "- Frontend development experience (React, Angular)\n" +
                "- Open source contributions\n" +
                "\n" +
                "Responsibilities\n" +
                "- Design and implement high-performance microservices\n" +
                "- Collaborate with cross-functional teams\n" +
                "- Mentor junior developers\n" +
                "- Review code and ensure quality standards\n" +
                "- Participate in architecture design discussions\n" +
                "- Optimize application performance\n" +
                "\n" +
                "What We Offer\n" +
                "- Competitive salary and benefits\n" +
                "- Flexible work arrangements\n" +
                "- Professional development opportunities\n" +
                "- Collaborative team environment";
    }
}

