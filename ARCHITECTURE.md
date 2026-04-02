# Resume AI Analyzer - Architecture Overview

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
│  (Web Browser, Mobile App, Desktop Client)                      │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼ HTTP/REST
┌─────────────────────────────────────────────────────────────────┐
│                   API LAYER (Controller)                         │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  ResumePropertyController                               │    │
│  │  - POST /resume-analyzer/analyze                        │    │
│  │  - GET /resume-analyzer/health                          │    │
│  │  - Error handling & response mapping                    │    │
│  └─────────────────────────────────────────────────────────┘    │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                  SERVICE LAYER (Business Logic)                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  ResumeAIAnalyzerService                                │    │
│  │  - Orchestrates analysis workflow                       │    │
│  │  - Coordinates between utilities                        │    │
│  │  - Exception handling                                   │    │
│  └─────────────────────────────────────────────────────────┘    │
└────────────────┬────────────────────────────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
┌────────┐  ┌────────┐  ┌────────┐
│Document│  │Resume  │  │WebContent
│Parser  │  │Matcher │  │Fetcher
│Util    │  │(ML)    │  │Util
│        │  │        │  │
├────────┤  ├────────┤  ├────────┤
│Extract │  │Calculate
│text    │  │scores  │  │Fetch
│from    │  │        │  │HTML
│PDFs,   │  │Analyze │  │
│DOCX    │  │gaps    │  │Extract
│etc.    │  │        │  │text
│        │  │Generate│  │
└────────┘  │recs.   │  └────────┘
            │        │
            └────────┘
                │
                ▼
        ┌────────────────┐
        │Text Processing│
        │Util            │
        │────────────────│
        │- Normalize text│
        │- Extract       │
        │  skills (219+) │
        │- Extract exp.  │
        │- Levenshtein   │
        │- Jaccard sim.  │
        │- Cosine sim.   │
        │- TF-IDF        │
        └────────────────┘
             │
             ▼
   ┌─────────────────┐
   │ ML Algorithms   │
   │ (5 major)       │
   └─────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    DATA LAYER (DTOs/Models)                      │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Bean Classes                                           │    │
│  │  - ResumeAnalyzerResponse                              │    │
│  │  - SkillResults                                        │    │
│  │  - ExperienceProperties                                │    │
│  │  - MatchType (Enum)                                    │    │
│  └─────────────────────────────────────────────────────────┘    │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼ JSON
        ┌─────────────────┐
        │ HTTP Response   │
        │ (200/400/500)   │
        └─────────────────┘
```

## 📦 Component Interaction Diagram

```
┌─────────────────┐
│ Request Input   │
│ Resume + JobDesc│
└────────┬────────┘
         │
         ▼
┌───────────────────────────────────────────────────────────────┐
│ ResumeAIAnalyzerService.analyzeResume()                       │
├───────────────────────────────────────────────────────────────┤
│ 1. validateInputs(resumeFile, jobDescription)                 │
│    ↓                                                           │
│ 2. extractResumeText(resumeFile)                              │
│    └─> DocumentProcessorUtil.extractTextFromFile()            │
│        └─> Apache Tika extracts text                          │
│    ↓                                                           │
│ 3. getJobDescription(jobDescriptionInput)                     │
│    ├─> If URL: WebContentFetcher.fetchWebContent()           │
│    │   └─> WebContentFetcher.extractTextFromHtml()           │
│    └─> If text: Use directly                                 │
│    ↓                                                           │
│ 4. ResumeMatcher.calculateMatchScore()                        │
│    └─> ML Analysis Pipeline:                                 │
│        ├─ TextProcessingUtil.extractSkills() [Resume]         │
│        ├─ TextProcessingUtil.extractSkills() [Job]            │
│        ├─ ResumeMatcher.calculateSkillMatchScore()            │
│        │  └─ Jaccard Similarity                              │
│        │  └─ Fuzzy Matching (Levenshtein)                    │
│        ├─ ResumeMatcher.calculateExperienceScore()            │
│        │  └─ Extract & Compare years                         │
│        ├─ ResumeMatcher.calculateKeywordMatchScore()          │
│        │  └─ TF-IDF based matching                           │
│        └─ TextProcessingUtil.calculateCosineSimilarity()      │
│           └─ Vector-based content similarity                 │
│    ↓                                                           │
│ 5. Calculate Final Score = Σ(Component × Weight)              │
│    ↓                                                           │
│ 6. MatchType.fromScore() → Determine category                 │
│    ↓                                                           │
│ 7. ResumeMatcher.analyzeSkillGaps()                           │
│    └─> Missing skills with fuzzy match filtering             │
│    ↓                                                           │
│ 8. ResumeMatcher.analyzeMatchedSkills()                       │
│    └─> Direct skill matches                                  │
│    ↓                                                           │
│ 9. ResumeMatcher.analyzePresentSkills()                       │
│    └─> All skills found in resume                            │
│    ↓                                                           │
│ 10. analyzeExperienceAndLocation()                            │
│     ├─ Extract years from both                               │
│     ├─ Calculate skill coverage %                            │
│     ├─ Calculate soft skills match %                         │
│     └─ Determine status                                      │
│    ↓                                                           │
│ 11. ResumeMatcher.generateRecommendations()                   │
│     ├─ Experience-based recommendations                       │
│     ├─ Skill gap recommendations                             │
│     ├─ Score-based recommendations                           │
│     └─ General best practices                                │
│    ↓                                                           │
│ 12. Build ResumeAnalyzerResponse object                       │
└────────────────────┬──────────────────────────────────────────┘
                     │
                     ▼
        ┌─────────────────────────────┐
        │ ResumeAnalyzerResponse       │
        │ {                            │
        │   resumeScore: 78.5          │
        │   matchType: "Good Match"    │
        │   skillResults: [...]        │
        │   experienceProperties: {...}│
        │ }                            │
        └─────────────────────────────┘
                     │
                     ▼
        ┌─────────────────────────────┐
        │ JSON Response (HTTP 200)    │
        │ Sent to Client              │
        └─────────────────────────────┘
```

## 🧠 ML Pipeline Architecture

```
┌──────────────────────────────────────────────────────────────┐
│            TEXT PROCESSING & NLP LAYER                       │
│              (TextProcessingUtil.java)                       │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Input: Resume Text & Job Description Text                 │
│  ▼                                                           │
│  ┌─────────────────────────────────────────────────┐        │
│  │ NORMALIZATION STAGE                            │        │
│  │ - Lowercase conversion                         │        │
│  │ - Whitespace standardization                   │        │
│  │ - Special character removal                    │        │
│  │ - Stop word filtering                          │        │
│  └─────────────────────────────────────────────────┘        │
│  ▼                                                           │
│  ┌─────────────────────────────────────────────────┐        │
│  │ FEATURE EXTRACTION STAGE                        │        │
│  │ ├─ extractSkills()                             │        │
│  │ │  └─ Match against 219+ skill database        │        │
│  │ ├─ extractYearsOfExperience()                  │        │
│  │ │  └─ Regex pattern matching                   │        │
│  │ ├─ extractKeywords()                           │        │
│  │ │  └─ TF-IDF weighting                         │        │
│  │ ├─ extractEmails()                             │        │
│  │ └─ extractPhoneNumbers()                       │        │
│  └─────────────────────────────────────────────────┘        │
│  ▼                                                           │
│  ┌─────────────────────────────────────────────────┐        │
│  │ SIMILARITY CALCULATION STAGE                    │        │
│  │ ├─ calculateJaccardSimilarity()                │        │
│  │ ├─ calculateCosineSimilarity()                 │        │
│  │ ├─ levenshteinDistance()                       │        │
│  │ └─ normalizedLevenshteinSimilarity()           │        │
│  └─────────────────────────────────────────────────┘        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────────────────────┐
│        ML MATCHING & SCORING LAYER                          │
│        (ResumeMatcher.java)                                 │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ INDIVIDUAL SCORE CALCULATION                        │   │
│  │                                                      │   │
│  │ 1. Skill Score (Component 1)                        │   │
│  │    ├─ Jaccard Similarity (Set-based)               │   │
│  │    ├─ Fuzzy Matching Bonus (Levenshtein)          │   │
│  │    └─ Coverage Percentage Boost                    │   │
│  │    Formula: (jaccardScore × 0.70 +                │   │
│  │             fuzzyBonus × 0.30) × coverage_boost   │   │
│  │    Result: Score0 (0-100)                          │   │
│  │                                                      │   │
│  │ 2. Experience Score (Component 2)                   │   │
│  │    ├─ Extract years from resume                     │   │
│  │    ├─ Extract years from job description           │   │
│  │    ├─ Compare and match                            │   │
│  │    └─ Apply experience fit formula                 │   │
│  │    Formula: Based on years differential            │   │
│  │    Result: Score1 (0-100)                          │   │
│  │                                                      │   │
│  │ 3. Keyword Score (Component 3)                      │   │
│  │    ├─ Extract job keywords (top 15)                │   │
│  │    ├─ Match against resume keywords                │   │
│  │    ├─ Calculate match percentage                   │   │
│  │    └─ Add partial credit for substring matches    │   │
│  │    Formula: (matched_keywords / total) × 100       │   │
│  │    Result: Score2 (0-100)                          │   │
│  │                                                      │   │
│  │ 4. Content Score (Component 4)                      │   │
│  │    ├─ Calculate Cosine Similarity (TF-IDF)        │   │
│  │    ├─ Normalize to percentage                      │   │
│  │    └─ Overall document relevance                   │   │
│  │    Formula: Cosine(Doc1, Doc2) × 100               │   │
│  │    Result: Score3 (0-100)                          │   │
│  └──────────────────────────────────────────────────────┘   │
│         │         │         │         │                     │
│         ▼         ▼         ▼         ▼                     │
│      Score0    Score1    Score2    Score3                  │
│       (0-100)  (0-100)   (0-100)   (0-100)                 │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ WEIGHTED COMPOSITE SCORE                            │   │
│  │                                                      │   │
│  │ FinalScore = (Score0 × 0.40) +                      │   │
│  │              (Score1 × 0.25) +                      │   │
│  │              (Score2 × 0.20) +                      │   │
│  │              (Score3 × 0.15)                        │   │
│  │                                                      │   │
│  │ FinalScore = min(100, max(0, FinalScore))          │   │
│  │                                                      │   │
│  │ Output: Score (0-100)                              │   │
│  └──────────────────────────────────────────────────────┘   │
│         │                                                    │
│         ▼                                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ MATCH TYPE DETERMINATION                            │   │
│  │                                                      │   │
│  │ IF Score >= 80:    "Strong Match"                   │   │
│  │ ELSE IF Score >= 65: "Good Match"                   │   │
│  │ ELSE IF Score >= 50: "Moderate Match"               │   │
│  │ ELSE IF Score >= 30: "Weak Match"                   │   │
│  │ ELSE:               "No Match"                      │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
└──────────────────────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────────────────────┐
│        ANALYSIS & RECOMMENDATIONS LAYER                      │
│        (ResumeMatcher.java + Service.java)                  │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  1. analyzeSkillGaps()                                       │
│     Job Skills - Resume Skills = Missing Skills            │
│     Apply fuzzy matching to filter false gaps              │
│                                                              │
│  2. analyzeMatchedSkills()                                   │
│     Resume Skills ∩ Job Skills = Matched Skills            │
│                                                              │
│  3. analyzePresentSkills()                                   │
│     All skills in resume                                   │
│                                                              │
│  4. analyzeExperienceAndLocation()                           │
│     ├─ Years comparison                                     │
│     ├─ Skill coverage percentage                           │
│     ├─ Soft skills match                                   │
│     └─ Status determination                                │
│                                                              │
│  5. generateRecommendations()                                │
│     ├─ Experience-based suggestions                        │
│     ├─ Skill gap remediation                               │
│     ├─ Score-based guidance                                │
│     └─ Best practice tips                                  │
│                                                              │
└──────────────────────────────────────────────────────────────┘
         │
         ▼
    ┌──────────────────┐
    │ Final Response   │
    │ Object with all  │
    │ analysis data    │
    └──────────────────┘
```

## 🔐 Data Flow Security

```
Client Request
    ↓
Input Validation
    ├─ File not null/empty
    ├─ File format valid
    ├─ File size ≤ 10MB
    ├─ Job description not empty
    └─ Job description min 50 chars
    ↓
Secured Processing
    ├─ File isolation
    ├─ Text extraction isolation
    ├─ No persistent storage of inputs
    └─ No sensitive data logging
    ↓
Result Generation
    └─ Scored analysis (no raw data exposure)
    ↓
JSON Response
    └─ Serialized and returned
```

## 🔄 Request-Response Lifecycle

```
1. CLIENT SENDS REQUEST
   POST /resume-analyzer/analyze
   Headers: Content-Type: multipart/form-data
   Body:
   - resumeFile: [Binary PDF/DOCX]
   - jobDescription: "Text or URL"

2. CONTROLLER RECEIVES REQUEST
   ↓
   Parameter Extraction
   ↓
   Call Service Layer

3. SERVICE ORCHESTRATION
   ├─ Input Validation
   ├─ File Processing
   ├─ Job Description Handling
   ├─ ML Analysis
   ├─ Result Compilation
   └─ Response Building

4. RESPONSE GENERATION
   ResumeAnalyzerResponse object
   ↓
   JSON Serialization
   ↓
   HTTP 200 OK + Body

5. CLIENT RECEIVES RESPONSE
   Parse JSON
   Display Results
   Render Score & Recommendations
```

## 📊 Database Schema (if needed)

```sql
-- Optional: For storing analysis history
CREATE TABLE resume_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_name VARCHAR(255),
    resume_score DECIMAL(5,2),
    match_type VARCHAR(50),
    job_title VARCHAR(255),
    analysis_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    matched_skills TEXT,
    skill_gaps TEXT,
    experience_match DECIMAL(5,2),
    recommendations TEXT
);

CREATE INDEX idx_score ON resume_analysis(resume_score);
CREATE INDEX idx_date ON resume_analysis(analysis_date);
```

## 🚀 Scalability Considerations

### Current Configuration:
- Single instance deployment
- In-memory processing
- No database persistence (stateless)

### For Enterprise Scale:

```
┌─────────────┐
│ Load        │
│ Balancer    │
└──────┬──────┘
       │
    ┌──┴──┬──────┬──────┐
    ▼     ▼      ▼      ▼
  ┌───┐┌───┐┌───┐┌───┐
  │ S ││ S ││ S ││ S │
  │ E ││ E ││ E ││ E │
  │ R ││ R ││ R ││ R │
  │ V ││ V ││ V ││ V │
  └───┘└───┘└───┘└───┘
    │     │      │      │
    └──┬──┴──┬───┴──┬───┘
       ▼     ▼      ▼
  ┌─────────────────────┐
  │  Cache Layer       │
  │  (Redis)           │
  └─────────────────────┘
       │
       ▼
  ┌─────────────────────┐
  │ Database            │
  │ (PostgreSQL)        │
  └─────────────────────┘
```

### Optimization Techniques:
- Implement caching layer (Redis)
- Database persistence for history
- Asynchronous processing for large batches
- Microservices decomposition
- API rate limiting
- Request queuing

## 🎯 Summary

The Resume AI Analyzer is built with a **3-tier architecture**:

1. **Presentation Layer** (Controller)
   - Handles HTTP requests
   - Parameter validation
   - Response formatting

2. **Business Logic Layer** (Service + Utilities)
   - Orchestrates workflow
   - Implements ML algorithms
   - Performs analysis

3. **Data Model Layer** (DTOs/Beans)
   - Structured response format
   - Type safety
   - Easy serialization

**Key Strengths**:
✅ Stateless (easily scalable)
✅ Separation of concerns
✅ Easy to test
✅ Modular design
✅ Clean architecture principles

---

**Architecture Version**: 1.0
**Last Updated**: March 2026

