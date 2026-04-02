# Resume AI Analyzer - Code Examples & API Reference

## API Endpoints

### Analyze Resume

**Endpoint**: `POST /resume-analyzer/analyze`

**Parameters**:
- `resumeFile` (multipart file): Resume file (PDF, DOCX, or TXT)
- `jobDescription` (string): Job description text (direct paragraph, no URL required)

**Request Example**:
```bash
curl -X POST "http://localhost:8080/resume-analyzer/analyze" \
  -F "resumeFile=@resume.pdf" \
  -F "jobDescription=Senior Java Developer with 5+ years experience..."
```

**Response (200 OK)**:
```json
{
  "resumeScore": 78.5,
  "matchType": "Strong Match",
  "description": "Candidate has strong technical background with most required skills",
  "skillResults": [
    {
      "name": "Matched Skills",
      "values": ["Java", "Spring Boot", "REST API", "Microservices", "Docker"]
    },
    {
      "name": "Skill Gaps",
      "values": ["Kubernetes", "Machine Learning"]
    },
    {
      "name": "Present Skills",
      "values": ["Java", "Python", "JavaScript", "Spring Boot", "Maven", "Git"]
    }
  ],
  "experienceProperties": {
    "experienceAndLocations": {
      "Candidate Experience": "6 years",
      "Required Experience": "5 years",
      "Experience Match": "92.5%",
      "Skill Coverage": "82.3%",
      "Soft Skills Match": "75.0%",
      "Contact": "Provided",
      "Status": "Recommended"
    },
    "recommendations": [
      "Strong match - minor improvements needed",
      "Priority skills to develop: Kubernetes, Docker orchestration",
      "Consider adding metrics and achievements to strengthen impact",
      "Keep resume updated with recent projects and tools used",
      "Include specific achievements with measurable outcomes"
    ]
  }
}
```

**Error Responses**:
```json
// 400 Bad Request
{
  "error": "Validation Error",
  "message": "Resume file is required and cannot be empty",
  "timestamp": 1617849600000
}

// 500 Internal Server Error
{
  "error": "Processing Error",
  "message": "Error processing resume file: Invalid PDF structure",
  "timestamp": 1617849600000
}
```

---

## Code Examples

### Example 1: Using the Service Directly

```java
import com.resumeaianalyzer.resumeaianalyzer.service.ResumeAIAnalyzerService;
import com.resumeaianalyzer.resumeaianalyzer.bean.ResumeAnalyzerResponse;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MyController {
    
    private final ResumeAIAnalyzerService analyzer;
    
    @PostMapping("/custom-analyze")
    public ResponseEntity<ResumeAnalyzerResponse> analyzeWithCustomLogic(
            @RequestParam MultipartFile resume,
            @RequestParam String jobDesc) {
        
        // Use the service directly
        ResumeAnalyzerResponse response = analyzer.analyzeResume(resume, jobDesc);
        
        // Add custom logic
        if (response.getResumeScore() > 80) {
            // Send notification email
            sendRecommendationEmail(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    private void sendRecommendationEmail(ResumeAnalyzerResponse response) {
        // Your custom logic here
    }
}
```

---

### Example 2: Using Advanced ML Matcher Directly

```java
import com.resumeaianalyzer.resumeaianalyzer.util.AdvancedMLMatcher;

// Calculate comprehensive match score
double score = AdvancedMLMatcher.calculateAdvancedMatchScore(
    resumeText,
    jobDescriptionText
);
System.out.println("Match Score: " + score + "%");

// Get dynamic skill gaps
Set<String> gaps = AdvancedMLMatcher.analyzeSkillGapsDynamic(
    resumeText,
    jobDescriptionText
);
System.out.println("Skills needed: " + gaps);

// Get matched skills
Set<String> matched = AdvancedMLMatcher.analyzeMatchedSkillsDynamic(
    resumeText,
    jobDescriptionText
);
System.out.println("Skills you have: " + matched);

// Calculate skill coverage percentage
double coverage = AdvancedMLMatcher.calculateSkillCoveragePercentage(
    resumeText,
    jobDescriptionText
);
System.out.println("Skill Coverage: " + coverage + "%");

// Get detailed recommendations
List<String> recommendations = AdvancedMLMatcher.generateDetailedRecommendations(
    resumeText,
    jobDescriptionText,
    score,
    gaps
);
recommendations.forEach(r -> System.out.println("- " + r));
```

---

### Example 3: Custom Batch Analysis

```java
import com.resumeaianalyzer.resumeaianalyzer.util.AdvancedMLMatcher;
import java.util.*;

public class BatchAnalyzer {
    
    public static void analyzeMultipleResumes(
            List<String> resumeTexts,
            String jobDescription) {
        
        // Score all resumes
        List<ResumeScore> scores = resumeTexts.stream()
            .map(resume -> new ResumeScore(
                resume.substring(0, 30) + "...",  // ID
                AdvancedMLMatcher.calculateAdvancedMatchScore(resume, jobDescription)
            ))
            .sorted(Comparator.comparingDouble(ResumeScore::getScore).reversed())
            .collect(Collectors.toList());
        
        // Print ranked results
        System.out.println("Top Candidates:");
        scores.forEach(rs -> System.out.println(
            rs.getId() + " -> " + String.format("%.1f%%", rs.getScore())
        ));
        
        // Analyze top 3
        scores.stream()
            .limit(3)
            .forEach(topCandidate -> {
                Set<String> gaps = AdvancedMLMatcher.analyzeSkillGapsDynamic(
                    topCandidate.getId(),
                    jobDescription
                );
                System.out.println("Gaps for " + topCandidate.getId() + ": " + gaps);
            });
    }
    
    static class ResumeScore {
        private String id;
        private double score;
        
        ResumeScore(String id, double score) {
            this.id = id;
            this.score = score;
        }
        
        double getScore() { return score; }
        String getId() { return id; }
    }
}
```

---

### Example 4: Extract Terms with Weights

```java
import com.resumeaianalyzer.resumeaianalyzer.util.AdvancedMLMatcher;
import java.util.Map;

// Extract important terms from job description with TF-IDF weights
Map<String, Double> importantTerms = AdvancedMLMatcher.extractTermsWithWeights(
    jobDescription
);

// Sort by importance
importantTerms.entrySet().stream()
    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
    .limit(10)
    .forEach(entry -> System.out.println(
        entry.getKey() + " (importance: " + String.format("%.3f", entry.getValue()) + ")"
    ));

// Output example:
// spring boot (importance: 0.045)
// microservices (importance: 0.038)
// rest api (importance: 0.035)
// docker (importance: 0.032)
```

---

### Example 5: Skills with Context

```java
import com.resumeaianalyzer.resumeaianalyzer.util.AdvancedMLMatcher;
import java.util.Map;

// Extract skills with context sentences
Map<String, String> skillContext = AdvancedMLMatcher.extractSkillsWithContext(
    jobDescription
);

skillContext.forEach((skill, context) -> {
    System.out.println("Skill: " + skill);
    System.out.println("Context: " + context);
    System.out.println("---");
});

// Output example:
// Skill: spring boot
// Context: We need expert-level Spring Boot and microservices experience
// ---
// Skill: kubernetes
// Context: Kubernetes deployment and container orchestration required
// ---
```

---

### Example 6: Fuzzy Skill Matching

```java
import com.resumeaianalyzer.resumeaianalyzer.util.TextProcessingUtil;

// Check if two skills are similar
String skill1 = "Java Script";
String skill2 = "JavaScript";

double similarity = TextProcessingUtil.normalizedLevenshteinSimilarity(
    skill1,
    skill2
);

System.out.println("Similarity: " + (similarity * 100) + "%");
// Output: Similarity: 93.3%

// Check multiple skills
String[] resumeSkills = {"JavaScript", "TypeScript", "React.js"};
String jobSkill = "typescript";

for (String skill : resumeSkills) {
    double sim = TextProcessingUtil.normalizedLevenshteinSimilarity(
        skill.toLowerCase(),
        jobSkill.toLowerCase()
    );
    if (sim > 0.8) {
        System.out.println(skill + " matches " + jobSkill);
    }
}

// Output:
// TypeScript matches typescript
```

---

### Example 7: Text Similarity Analysis

```java
import com.resumeaianalyzer.resumeaianalyzer.util.TextProcessingUtil;

String resumeText = "5 years experience with Java, Spring Boot, and REST APIs";
String jobText = "Seeking 5+ years Java and Spring Boot developer";

// Cosine Similarity (0-1 scale)
double cosine = TextProcessingUtil.calculateCosineSimilarity(
    resumeText,
    jobText
);
System.out.println("Cosine Similarity: " + (cosine * 100) + "%");
// Output: Cosine Similarity: 85.7%

// Jaccard Similarity (0-1 scale)
Set<String> resumeWords = new HashSet<>(
    Arrays.asList(resumeText.split("\\s+"))
);
Set<String> jobWords = new HashSet<>(
    Arrays.asList(jobText.split("\\s+"))
);
double jaccard = TextProcessingUtil.calculateJaccardSimilarity(
    resumeWords,
    jobWords
);
System.out.println("Jaccard Similarity: " + (jaccard * 100) + "%");
// Output: Jaccard Similarity: 56.2%
```

---

### Example 8: Years of Experience Extraction

```java
import com.resumeaianalyzer.resumeaianalyzer.util.TextProcessingUtil;

String resumeText = "I have 7 years of experience in software development";
String jobText = "Seeking candidates with 5+ years experience";

Integer resumeYears = TextProcessingUtil.extractYearsOfExperience(resumeText);
Integer requiredYears = TextProcessingUtil.extractYearsOfExperience(jobText);

System.out.println("Resume Years: " + resumeYears);           // Output: 7
System.out.println("Required Years: " + requiredYears);       // Output: 5
System.out.println("Meets requirement: " + (resumeYears >= requiredYears));
// Output: Meets requirement: true
```

---

### Example 9: Email Extraction

```java
import com.resumeaianalyzer.resumeaianalyzer.util.TextProcessingUtil;
import java.util.Set;

String resumeText = "Contact me at john.doe@example.com or jane.smith@company.com";

Set<String> emails = TextProcessingUtil.extractEmails(resumeText);
emails.forEach(email -> System.out.println("Email: " + email));

// Output:
// Email: john.doe@example.com
// Email: jane.smith@company.com
```

---

### Example 10: Keywords Extraction

```java
import com.resumeaianalyzer.resumeaianalyzer.util.TextProcessingUtil;
import java.util.Set;

String jobDescription = "We seek a Senior Java Developer with expertise in Spring Boot, " +
    "microservices architecture, REST APIs, and Docker containerization. " +
    "Experience with CI/CD pipelines, Git version control, and Agile methodology required.";

// Get top 10 keywords
Set<String> topKeywords = TextProcessingUtil.extractKeywords(jobDescription, 10);

topKeywords.forEach(keyword -> System.out.println("- " + keyword));

// Output:
// - java
// - developer
// - spring
// - boot
// - microservices
// - rest
// - docker
// - experience
// - pipeline
// - git
```

---

## Response Status Codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Analysis successful |
| 400 | Bad Request | Invalid resume file or empty job description |
| 500 | Internal Server Error | File processing error or unexpected exception |

---

## Field Descriptions

### ResumeAnalyzerResponse

| Field | Type | Description |
|-------|------|-------------|
| resumeScore | Double | Overall match score (0-100) |
| matchType | String | Classification (Excellent/Strong/Moderate/Weak Match) |
| description | String | Human-readable match summary |
| skillResults | List<SkillResults> | Array of matched, gap, and present skills |
| experienceProperties | ExperienceProperties | Experience, coverage, and recommendations |

### SkillResults

| Field | Type | Description |
|-------|------|-------------|
| name | String | Category (Matched Skills, Skill Gaps, Present Skills) |
| values | List<String> | List of skills in this category |

### ExperienceProperties

| Field | Type | Description |
|-------|------|-------------|
| experienceAndLocations | Map | Key-value pairs (Experience, Location, etc.) |
| recommendations | List<String> | Personalized recommendations for improvement |

---

## Integration with Other Systems

### Database Integration

```java
@Entity
@Table(name = "analysis_results")
public class AnalysisResult {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String resumeFileName;
    private String jobDescription;
    private Double matchScore;
    private String matchType;
    
    @ElementCollection
    private List<String> matchedSkills;
    
    @ElementCollection
    private List<String> skillGaps;
    
    @Column(columnDefinition = "TEXT")
    private String fullResponse;  // JSON serialized
    
    private LocalDateTime analyzedAt;
}

// Usage
@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisResult, Long> {}

// Save results
@Service
public class AnalysisService {
    
    @Autowired
    private AnalysisRepository repository;
    
    @Autowired
    private ResumeAIAnalyzerService analyzerService;
    
    public AnalysisResult analyzeAndSave(MultipartFile resume, String jobDesc) {
        ResumeAnalyzerResponse response = analyzerService.analyzeResume(resume, jobDesc);
        
        AnalysisResult result = new AnalysisResult();
        result.setResumeFileName(resume.getOriginalFilename());
        result.setJobDescription(jobDesc);
        result.setMatchScore(response.getResumeScore());
        result.setMatchType(response.getMatchType());
        result.setAnalyzedAt(LocalDateTime.now());
        // ... set other fields
        
        return repository.save(result);
    }
}
```

---

### Async Processing

```java
@Service
public class AsyncAnalysisService {
    
    @Autowired
    private ResumeAIAnalyzerService analyzerService;
    
    @Async
    public CompletableFuture<ResumeAnalyzerResponse> analyzeAsync(
            MultipartFile resume,
            String jobDescription) {
        
        return CompletableFuture.supplyAsync(() -> 
            analyzerService.analyzeResume(resume, jobDescription)
        );
    }
    
    // Usage
    public ResponseEntity<?> submitForAnalysis(
            @RequestParam MultipartFile resume,
            @RequestParam String jobDescription) {
        
        CompletableFuture<ResumeAnalyzerResponse> future = 
            asyncAnalysisService.analyzeAsync(resume, jobDescription);
        
        return ResponseEntity.accepted()
            .body(Map.of("message", "Analysis submitted", "status", "processing"));
    }
}
```

---

## Testing Examples

### Unit Test

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdvancedMLMatcherTest {
    
    @Test
    public void testPerfectMatch() {
        String resume = "5 years Java developer with Spring Boot";
        String job = "Seeking Java developer with Spring Boot experience";
        
        double score = AdvancedMLMatcher.calculateAdvancedMatchScore(
            resume,
            job
        );
        
        assertTrue(score > 80, "Score should be > 80 for perfect match");
    }
    
    @Test
    public void testNoMatch() {
        String resume = "Graphic designer with Adobe Suite";
        String job = "Java developer position";
        
        double score = AdvancedMLMatcher.calculateAdvancedMatchScore(
            resume,
            job
        );
        
        assertTrue(score < 40, "Score should be < 40 for no match");
    }
    
    @Test
    public void testFuzzyMatch() {
        Set<String> gaps = AdvancedMLMatcher.analyzeSkillGapsDynamic(
            "javascript and react developer",
            "JavaScript and React.js required"
        );
        
        assertTrue(gaps.isEmpty() || gaps.size() < 2, 
            "Fuzzy matching should find similar skills");
    }
}
```

---

## Performance Tips

### 1. Cache Results

```java
@Service
public class CachedAnalysisService {
    
    private final Map<String, ResumeAnalyzerResponse> cache = 
        new ConcurrentHashMap<>();
    
    public ResumeAnalyzerResponse analyzeWithCache(
            String resumeHash,
            String jobDescHash,
            MultipartFile resume,
            String jobDescription) {
        
        String cacheKey = resumeHash + "_" + jobDescHash;
        
        return cache.computeIfAbsent(cacheKey, key ->
            analyzerService.analyzeResume(resume, jobDescription)
        );
    }
}
```

### 2. Batch Processing

```java
@Service
public class BatchAnalysisService {
    
    public List<ResumeAnalyzerResponse> analyzeBatch(
            List<MultipartFile> resumes,
            String jobDescription) {
        
        return resumes.parallelStream()
            .map(resume -> analyzerService.analyzeResume(resume, jobDescription))
            .collect(Collectors.toList());
    }
}
```

---

This comprehensive guide covers all aspects of the Resume AI Analyzer API and its ML algorithms!

