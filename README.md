# 🚀 Resume AI Analyzer

An AI-powered Resume Analyzer built using Java Spring Boot and Machine Learning algorithms to evaluate resumes, calculate ATS score, and provide improvement suggestions.

---

## 📌 Overview

Resume AI Analyzer is a backend application that analyzes resume content and generates an ATS (Applicant Tracking System) score based on keyword matching and skill relevance. It helps job seekers improve their resumes and increase their chances of getting shortlisted.

---

## ✨ Features

* 📊 ATS Score Calculation (0–100)
* 🧠 Keyword & Skill Matching
* 🎯 Resume Improvement Suggestions
* ⚡ REST API-based Architecture
* 🧪 API Testing using Postman
* 🗄️ Data Storage using PostgreSQL

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Database:** PostgreSQL
* **Machine Learning:** TF-IDF, Cosine Similarity
* **Tools:** IntelliJ IDEA, Postman, Git, GitHub

---

## ⚙️ How It Works

1. Resume content is provided as input
2. System processes and analyzes the text
3. ML algorithms extract keywords and skills
4. Resume content is compared with expected job-related keywords
5. ATS score is calculated
6. Suggestions are generated for improvement

---

## 📡 API Example

### 🔹 Endpoint

POST /analyzeResume

### 🔹 Request

```json
{
  "resumeText": "Java Spring Boot developer with REST API experience"
}
```

### 🔹 Response

```json
{
  "atsScore": 78,
  "suggestions": [
    "Add more backend-related keywords",
    "Include project details",
    "Improve technical skills section"
  ]
}
```

---

## 📁 Project Structure

```
Resume-AI-Analyzer/
│-- controller/
│-- service/
│-- repository/
│-- model/
│-- utils/
│-- config/
│-- README.md
```

---


## ⚙️ Database Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## 🎯 Use Cases

* Freshers improving resume quality
* Backend + ML project demonstration
* ATS-based resume evaluation system

---

## 🔮 Future Improvements

* Resume PDF Upload Support
* Frontend Integration
* Advanced ML Models
* Cloud Deployment

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
