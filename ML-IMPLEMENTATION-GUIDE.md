# Resume AI Analyzer - Implementation & Configuration Guide

## Quick Start

### 1. Basic Usage

```bash
POST /resume-analyzer/analyze
Content-Type: multipart/form-data

resumeFile: [PDF/DOCX/TXT file]
jobDescription: "Senior Java Developer with 5+ years experience in Spring Boot and microservices..."
```

### 2. Expected Response

```json
{
  "resumeScore": 78.5,
  "matchType": "Strong Match",
  "description": "Candidate shows strong alignment with job requirements",
  "skillResults": [
    {
      "name": "Matched Skills",
      "values": ["Java", "Spring Boot", "Microservices", "REST API", "Docker"]
    },
    {
      "name": "Skill Gaps",
      "values": ["Kubernetes", "Machine Learning", "AWS Lambda"]
    },
    {
      "name": "Present Skills",
      "values": ["Java", "Python", "JavaScript", "Spring Boot", "Maven", ...]
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
      "Strong match for the position",
      "Priority skills to develop: Kubernetes, Docker orchestration, Cloud deployment",
      "Strong match - refine documentation of skills and experience",
      "Emphasize leadership and complex project involvement",
      "Keep resume updated with recent projects and tools used",
      "Include specific achievements with measurable outcomes"
    ]
  }
}
```

## How It Works

### Step 1: Resume Text Extraction
```
Input: Resume file (PDF/DOCX/TXT)
↓
Process: Apache Tika extracts raw text
↓
Output: Cleaned resume text
```

### Step 2: Text Normalization
```
Input: "I have expertise in Java, Spring-Boot, and REST APIs"
↓
Process: Lowercase, remove special chars, normalize whitespace
↓
Output: "i have expertise in java spring boot and rest apis"
```

### Step 3: Dynamic Term Extraction (TF-IDF)
```
Input: Job description and resume text
↓
Process: 
  1. Calculate term frequency (how often each term appears)
  2. Calculate inverse document frequency (how unique the term is)
  3. Compute TF-IDF score for each term
↓
Output: Weighted terms with importance scores
{
  "java": 0.045,
  "spring boot": 0.038,
  "rest api": 0.035,
  "microservices": 0.032,
  ...
}
```

### Step 4: Multi-Algorithm Comparison

```
Algorithm 1: Dynamic Skill Matching
├─ Extract skills from job description
├─ Extract skills from resume
├─ Find exact matches
└─ Find fuzzy matches (80%+ similarity)
Result: Skill match percentage

Algorithm 2: Content Similarity
├─ Cosine similarity (60% weight)
└─ Jaccard similarity (40% weight)
Result: Overall content similarity score

Algorithm 3: Experience Matching
├─ Extract years from resume
├─ Extract years from job description
└─ Calculate experience fit
Result: Experience compatibility score

Algorithm 4: Keyword Frequency Analysis
├─ Extract top 25 job keywords
├─ Extract top 30 resume keywords
├─ Find matches with fuzzy matching
└─ Calculate keyword coverage
Result: Keyword match percentage

Algorithm 5: Soft Skills Analysis
├─ Check for soft skill keywords
├─ Match between documents
└─ Calculate soft skills coverage
Result: Soft skills match percentage
```

### Step 5: Weighted Composite Score
```
Final Score = 
  (Skill Match × 0.35) +
  (Content Similarity × 0.30) +
  (Experience × 0.20) +
  (Keyword Frequency × 0.15)

Result: 0-100 score
```

### Step 6: Match Type Classification
```
Score 80-100  → "Excellent Match"
Score 65-79   → "Strong Match"
Score 50-64   → "Moderate Match"
Score 0-49    → "Weak Match"
```

### Step 7: Skill Gap Analysis
```
Input: Job required skills, Resume skills
↓
Process:
  1. Find skills in job description not in resume
  2. Filter out fuzzy matches (80%+ similar)
  3. Weight gaps by importance in job description
  4. Sort by importance
↓
Output: Ranked list of missing skills
```

## Algorithms Explained

### TF-IDF (Term Frequency - Inverse Document Frequency)

**What it does**: Identifies important, unique terms in documents

**Formula**:
```
TF-IDF = (Term Count / Total Terms) × log(Total Documents / Documents with Term)
```

**Example**:
- "the" appears 50 times in 10-page document → Low TF-IDF (common word)
- "microservices" appears 5 times → High TF-IDF (specific skill)

**Why it matters**: Finds domain-specific skills, not common words

---

### Cosine Similarity

**What it does**: Measures angle between two text vectors (0° = identical, 90° = unrelated)

**How it works**:
```
1. Create vector of word frequencies for each document
2. Calculate dot product of vectors
3. Divide by magnitude of each vector
4. Result: 0 (completely different) to 1 (identical)
```

**Example**:
- Resume: "Java developer with Spring Boot experience"
- Job: "Spring Boot and Java developer needed"
- Cosine Similarity: 0.92 (very similar)

---

### Jaccard Similarity

**What it does**: Measures set overlap percentage

**Formula**:
```
Jaccard = |Intersection| / |Union|
```

**Example**:
- Resume skills: {Java, Python, Spring}
- Job skills: {Java, Spring, Docker}
- Intersection: {Java, Spring}
- Union: {Java, Python, Spring, Docker}
- Jaccard: 2/4 = 0.50 (50%)

---

### Levenshtein Distance (Fuzzy Matching)

**What it does**: Measures string similarity by counting minimum edits

**Example**:
```
"JavaScript" vs "Java Script"
Distance: 1 (remove space)
Normalized Similarity: 0.90 (90%)
```

**Used for**:
- Typo tolerance: "Sprig Boot" → "Spring Boot"
- Variant matching: "JS" → "JavaScript"
- Abbreviation matching: "DB" → "Database"

---

## Advanced Configuration

### 1. Adjust ML Weights

Edit `AdvancedMLMatcher.java`:

```java
// More emphasis on exact skills
private static final double DYNAMIC_SKILL_WEIGHT = 0.45;      // was 0.35
private static final double CONTENT_SIMILARITY_WEIGHT = 0.25; // was 0.30
private static final double EXPERIENCE_WEIGHT = 0.15;         // was 0.20
private static final double KEYWORD_FREQUENCY_WEIGHT = 0.15;  // was 0.15
```

### 2. Adjust Fuzzy Match Threshold

Current threshold: **0.80** (80% similarity)

```java
// Make fuzzy matching stricter (higher threshold = fewer matches)
if (similarity > 0.85) {  // was 0.80
    matchCount++;
}

// Or make it more lenient
if (similarity > 0.75) {  // was 0.80
    matchCount++;
}
```

### 3. Adjust Result Limits

Current limits:
- Skill Gaps: Top 10
- Matched Skills: Top 15
- Present Skills: Top 20

```java
// Increase matched skills shown
.limit(20)  // was 15
```

### 4. Add Custom Stop Words

In `AdvancedMLMatcher.java`, method `isRelevantTerm()`:

```java
Set<String> stopWords = new HashSet<>(Arrays.asList(
    // ... existing stop words ...
    "client", "service", "company"  // Add custom words
));
```

## Customization Examples

### Example 1: Stricter Scoring

For roles where exact skill match is critical (Specialized Positions):

```java
// In AdvancedMLMatcher.java
private static final double DYNAMIC_SKILL_WEIGHT = 0.50;      // +15%
private static final double CONTENT_SIMILARITY_WEIGHT = 0.20; // -10%
private static final double EXPERIENCE_WEIGHT = 0.15;         // -5%
private static final double KEYWORD_FREQUENCY_WEIGHT = 0.15;  // unchanged
```

### Example 2: Looser Scoring

For roles where transferable skills matter (Entry-level Positions):

```java
// In AdvancedMLMatcher.java
private static final double DYNAMIC_SKILL_WEIGHT = 0.30;      // -5%
private static final double CONTENT_SIMILARITY_WEIGHT = 0.35; // +5%
private static final double EXPERIENCE_WEIGHT = 0.25;         // +5%
private static final double KEYWORD_FREQUENCY_WEIGHT = 0.10;  // -5%
```

### Example 3: Experience-Focused

For senior positions where experience is critical:

```java
// In AdvancedMLMatcher.java
private static final double DYNAMIC_SKILL_WEIGHT = 0.30;
private static final double CONTENT_SIMILARITY_WEIGHT = 0.20;
private static final double EXPERIENCE_WEIGHT = 0.40;         // +20%
private static final double KEYWORD_FREQUENCY_WEIGHT = 0.10;
```

## Debugging & Troubleshooting

### Issue 1: Score seems too high/low

**Check**: ML Weights
```java
// Print scores for each component
System.out.println("Dynamic Skill Score: " + dynamicSkillScore);
System.out.println("Content Similarity: " + contentScore);
System.out.println("Experience Score: " + experienceScore);
System.out.println("Keyword Score: " + keywordScore);
```

**Solution**: Adjust weights in `AdvancedMLMatcher.java`

### Issue 2: Not finding obvious skills

**Check**: Skill extraction
```java
// Get actual extracted skills
Map<String, Double> terms = extractTermsWithWeights(jobDescription);
System.out.println("Job Terms: " + terms);
```

**Possible causes**:
- Skill filtered as stop word → Add to custom list
- Skill in list but formatted differently → Use fuzzy matching (0.75 threshold)
- Skill too short → Minimum 2 characters required

### Issue 3: Fuzzy matching too aggressive/conservative

**Check**: Threshold
```java
// Current: 0.80
if (similarity > 0.80) {  // Try 0.75 or 0.85
    matchCount++;
}
```

### Issue 4: Slow performance

**Profile code**:
```java
long start = System.currentTimeMillis();
double score = calculateAdvancedMatchScore(resume, job);
long duration = System.currentTimeMillis() - start;
System.out.println("Analysis took: " + duration + "ms");
```

**Optimization**:
- Cache extracted terms
- Use parallel streams for large documents
- Limit keyword extraction to top N

## Performance Benchmarks

| Document Size | Time | Notes |
|---|---|---|
| 1-2 pages (PDF) | 100-200ms | Fast processing |
| 3-5 pages (DOCX) | 200-400ms | Normal |
| 10+ pages (TXT) | 400-800ms | Larger analysis |

## Integration Testing

### Test 1: Perfect Match
```
Resume: "Senior Java Developer with 5 years Spring Boot experience"
Job: "Seeking Senior Java Developer with Spring Boot expertise"
Expected Score: 85-95
```

### Test 2: No Skills Match
```
Resume: "Graphic Designer with Adobe Creative Suite experience"
Job: "Senior Java Developer needed"
Expected Score: 10-25
```

### Test 3: Partial Match
```
Resume: "Python developer, learning Java"
Job: "Java Developer with 3+ years experience"
Expected Score: 45-60
```

### Test 4: Fuzzy Match
```
Resume: "Experience with JavaScript, React.js, Node.js"
Job: "Need expertise in JavaScript, React, Node"
Expected Score: 75-85
```

## Monitoring & Metrics

### Key Metrics to Track

1. **Average Match Score**: Should vary (not consistently 50)
2. **Score Distribution**: Should follow bell curve
3. **Skill Gap Count**: Varies by role
4. **Processing Time**: Should be < 1 second

### Log Analysis

Check logs for patterns:
- Too many "no skills found" → Check extraction
- All scores >80 → May be too lenient
- All scores <40 → May be too strict

## Support & Questions

For issues or questions:

1. Check `ML-ALGORITHMS-DOCUMENTATION.md` for algorithm details
2. Review logs in `target/` folder
3. Verify job description format (plain text, not corrupted)
4. Test with known good examples first

## Version History

- v1.0 (Current): Dynamic ML-based matching without hardcoded skills
- v0.9: Hardcoded skill list matching

