# Resume AI Analyzer - Developer Checklist & Setup Guide

## ✅ Pre-Setup Checklist

### System Requirements
- [ ] Java 17 or higher installed
  ```bash
  java -version
  ```
- [ ] Maven 3.6+ installed
  ```bash
  mvn -version
  ```
- [ ] Git installed (optional, for version control)
- [ ] IDE installed (IntelliJ IDEA, Eclipse, or VS Code)
- [ ] 2GB+ RAM available
- [ ] Internet connection (for Maven dependencies)

### Development Environment
- [ ] JDK_HOME environment variable set
- [ ] MAVEN_HOME environment variable set (optional)
- [ ] IDE project imported successfully
- [ ] IDE indexing completed

## 📥 Installation Steps

### Step 1: Clone/Extract Project
```bash
cd your-projects-folder
# Extract Resume-AI-Analyzer.zip
unzip Resume-AI-Analyzer.zip
cd Resume-AI-Analyzer
```

### Step 2: Install Dependencies
```bash
# Full build
mvn clean install

# OR faster build (skip tests)
mvn clean install -DskipTests
```

### Step 3: Verify Build
```bash
# Check if JAR was created
ls target/*.jar

# Or on Windows
dir target\*.jar
```

### Step 4: Run Application
```bash
# Using Maven
mvn spring-boot:run

# OR using Java directly
java -jar target/Resume-AI-Analyzer-0.0.1-SNAPSHOT.jar
```

### Step 5: Verify Server Started
```bash
# Test health endpoint
curl http://localhost:8080/resume-analyzer/health

# Expected response:
# {"status":"UP","service":"Resume AI Analyzer"}
```

## 🛠️ IDE Setup (IntelliJ IDEA)

### Import Project
1. Open IntelliJ IDEA
2. File → Open → Select Resume-AI-Analyzer folder
3. Select "Trust Project" when prompted
4. Wait for Maven indexing to complete

### Configure Run Configuration
1. Run → Edit Configurations
2. Click "+" → Spring Boot
3. Set:
   - Main class: `com.resumeaianalyzer.resumeaianalyzer.ResumeAiAnalyzerApplication`
   - Working directory: `$PROJECT_DIR$`
4. Click Apply → OK

### Run Application
1. Click Run button (or Shift+F10)
2. Watch logs for "Started ResumeAiAnalyzerApplication"
3. Access at http://localhost:8080

## 📚 Project Structure Verification

After setup, verify this structure exists:

```
Resume-AI-Analyzer/
├── src/main/java/com/resumeaianalyzer/resumeaianalyzer/
│   ├── ResumeAiAnalyzerApplication.java              ✓
│   ├── bean/
│   │   ├── ExperienceProperties.java                 ✓
│   │   ├── MatchType.java                            ✓
│   │   ├── ResumeAnalyzerResponse.java                ✓
│   │   └── SkillResults.java                          ✓
│   ├── controller/
│   │   └── ResumePropertyController.java              ✓
│   ├── service/
│   │   └── ResumeAIAnalyzerService.java               ✓
│   └── util/
│       ├── TextProcessingUtil.java                    ✓
│       ├── ResumeMatcher.java                         ✓
│       ├── DocumentProcessorUtil.java                 ✓
│       ├── WebContentFetcher.java                     ✓
│       └── MLAlgorithmTester.java                     ✓
│
├── pom.xml                                             ✓
├── ML-DOCUMENTATION.md                                ✓
├── IMPLEMENTATION-GUIDE.md                            ✓
├── PROJECT-SUMMARY.md                                 ✓
├── QUICK-REFERENCE.md                                 ✓
├── ARCHITECTURE.md                                    ✓
└── DEVELOPER-CHECKLIST.md                             ✓
```

## 🧪 Testing Checklist

### Test 1: Compilation
```bash
mvn clean compile
```
**Expected**: BUILD SUCCESS

### Test 2: All Tests
```bash
mvn test
```
**Expected**: All tests pass (or n/a if no tests)

### Test 3: Package
```bash
mvn package
```
**Expected**: JAR file created in target/ folder

### Test 4: Start Application
```bash
mvn spring-boot:run
```
**Expected**: No errors, listening on port 8080

### Test 5: API Health Check
```bash
curl http://localhost:8080/resume-analyzer/health
```
**Expected Response**:
```json
{"status":"UP","service":"Resume AI Analyzer"}
```

### Test 6: Analyze Endpoint (Manual)
Use Postman or cURL:
```bash
curl -X POST http://localhost:8080/resume-analyzer/analyze \
  -F "resumeFile=@sample_resume.pdf" \
  -F "jobDescription=Senior Java Developer with 5+ years experience"
```

**Expected**: JSON response with resumeScore, matchType, skillResults, etc.

## 🔍 Common Setup Issues

### Issue 1: "command not found: mvn"
**Solution**: Add Maven to PATH
```bash
# Windows: Add MAVEN_BIN to System PATH
# Linux/Mac: export PATH=$PATH:/path/to/maven/bin
```

### Issue 2: "Java version not supported"
**Solution**: Update Java to 17+
```bash
java -version  # Check current version
# Install Java 17 if needed
```

### Issue 3: "Port 8080 already in use"
**Solution**: Change port in application.properties
```properties
server.port=8081
```

### Issue 4: Maven build fails with dependency errors
**Solution**: Clear cache and rebuild
```bash
mvn clean install -U
```

### Issue 5: IDE doesn't recognize Spring Boot
**Solution**: 
- Invalidate IDE cache
- Reimport Maven project
- Restart IDE

## 📝 Development Workflow

### Daily Development Cycle

```
1. START APPLICATION
   mvn spring-boot:run

2. MAKE CHANGES
   Edit Java files in src/main/java

3. LIVE RELOAD (if configured)
   Application auto-reloads on changes

4. TEST CHANGES
   Use Postman/cURL to test endpoints

5. VERIFY LOGS
   Check application logs for errors

6. COMMIT CHANGES (if using Git)
   git add .
   git commit -m "Feature description"

7. RUN TESTS
   mvn test

8. STOP APPLICATION
   Ctrl+C
```

### Adding New Features

Template for adding new feature:

```bash
# 1. Create new utility class (if needed)
touch src/main/java/com/resumeaianalyzer/resumeaianalyzer/util/NewUtil.java

# 2. Edit corresponding test (if needed)
touch src/test/java/com/resumeaianalyzer/resumeaianalyzer/util/NewUtilTest.java

# 3. Update service layer
vim src/main/java/com/resumeaianalyzer/resumeaianalyzer/service/ResumeAIAnalyzerService.java

# 4. Test locally
mvn test

# 5. Build and verify
mvn clean install
```

## 🚀 Deployment Checklist

Before deploying to production:

### Code Quality
- [ ] All tests pass: `mvn test`
- [ ] No compilation warnings: `mvn clean compile`
- [ ] Code formatted consistently
- [ ] Comments added for complex logic

### Security
- [ ] Input validation present
- [ ] No hardcoded credentials
- [ ] Error messages don't expose internals
- [ ] HTTPS enabled (if needed)

### Performance
- [ ] Application startup time acceptable
- [ ] Memory usage reasonable
- [ ] No memory leaks detected
- [ ] API response time < 5 seconds

### Documentation
- [ ] README.md updated
- [ ] API documentation complete
- [ ] Configuration documented
- [ ] Setup guide available

### Build & Package
- [ ] Final JAR builds successfully: `mvn clean package`
- [ ] JAR file size reasonable
- [ ] All dependencies included
- [ ] Version updated in pom.xml

## 📊 Performance Monitoring Commands

### Check Application Logs
```bash
# While running, watch logs
tail -f nohup.out

# Or redirect to file during startup
mvn spring-boot:run > app.log 2>&1 &
```

### Monitor Memory Usage
```bash
# Find Java process
ps aux | grep java

# Monitor with jstat
jstat -gc <PID> 1000
```

### Test API Performance
```bash
# Using Apache Bench
ab -n 100 -c 10 http://localhost:8080/resume-analyzer/health

# Using wrk
wrk -t4 -c100 -d30s http://localhost:8080/resume-analyzer/health
```

## 🔧 Configuration Management

### Local Development (application.properties)
```properties
# Server
server.port=8080
server.servlet.context-path=

# Logging
logging.level.root=INFO
logging.level.com.resumeaianalyzer=DEBUG

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Application
app.name=Resume AI Analyzer
app.version=1.0.0
```

### Production (application-prod.properties)
```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=WARN
logging.level.com.resumeaianalyzer=INFO
logging.file.name=/var/log/app.log

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Run with Specific Profile
```bash
# Development
java -jar app.jar --spring.profiles.active=dev

# Production
java -jar app.jar --spring.profiles.active=prod
```

## 📚 Documentation Checklist

Make sure you've read:

- [ ] **QUICK-REFERENCE.md** - Quick start and algorithm overview
- [ ] **ML-DOCUMENTATION.md** - Complete features and algorithms
- [ ] **IMPLEMENTATION-GUIDE.md** - Technical deep dive
- [ ] **ARCHITECTURE.md** - System architecture overview
- [ ] **PROJECT-SUMMARY.md** - Project structure and summary

## 🎓 Learning Path for New Developers

### Week 1: Basics
- [ ] Run application and verify startup
- [ ] Test API with sample data
- [ ] Read QUICK-REFERENCE.md
- [ ] Understand project structure

### Week 2: ML Algorithms
- [ ] Read IMPLEMENTATION-GUIDE.md
- [ ] Review TextProcessingUtil.java
- [ ] Review ResumeMatcher.java
- [ ] Understand each algorithm

### Week 3: Backend Architecture
- [ ] Read ARCHITECTURE.md
- [ ] Study Service layer design
- [ ] Review Controller implementation
- [ ] Understand data flow

### Week 4: Enhancement
- [ ] Make first code change
- [ ] Add test coverage
- [ ] Update documentation
- [ ] Submit for review

## 🆘 Getting Help

### Documentation
1. Check **QUICK-REFERENCE.md** for quick answers
2. Review **IMPLEMENTATION-GUIDE.md** for detailed info
3. Look at inline code comments and JavaDoc

### Debugging
```java
// Enable debug logging
logging.level.com.resumeaianalyzer=DEBUG

// Use breakpoints in IDE
// Right-click line number → Set Breakpoint
// Run in Debug mode (Shift+F9 in IntelliJ)
```

### Common Commands

```bash
# Clean build
mvn clean install -DskipTests

# Run with debug output
mvn spring-boot:run -X

# Run tests only
mvn test

# Generate project documentation
mvn javadoc:javadoc

# Check dependencies
mvn dependency:tree
```

## ✨ Best Practices

1. **Code Style**
   - Use consistent naming conventions
   - Follow Spring Boot conventions
   - Add meaningful comments
   - Keep methods focused

2. **Testing**
   - Write tests for new features
   - Test edge cases
   - Mock external dependencies
   - Aim for 80%+ code coverage

3. **Performance**
   - Avoid N+1 queries
   - Use caching where appropriate
   - Profile before optimizing
   - Monitor resource usage

4. **Security**
   - Validate all inputs
   - Use parameterized queries
   - Don't expose internal errors
   - Keep dependencies updated

5. **Maintenance**
   - Document complex logic
   - Keep README updated
   - Use meaningful commit messages
   - Review code before merging

## 📋 Pre-Commit Checklist

Before committing code:

```bash
# 1. Run all tests
mvn test

# 2. Check code quality
mvn clean compile

# 3. Run application
mvn spring-boot:run

# 4. Manual testing
# Test endpoints with Postman/cURL

# 5. Review changes
git diff

# 6. Commit with message
git commit -m "feature: Add new capability"

# 7. Push to repository
git push origin branch-name
```

## 🎯 Troubleshooting Quick Links

| Problem | Solution |
|---------|----------|
| JAR not found | Run `mvn clean package` |
| Port in use | Change `server.port` in properties |
| Dependencies missing | Run `mvn clean install -U` |
| Application won't start | Check `application.properties` |
| API returns 400 | Validate input parameters |
| API returns 500 | Check application logs |
| Slow performance | Check file size, review logs |

## 🎊 Conclusion

You're now ready to develop with the Resume AI Analyzer!

**Next Steps**:
1. ✅ Complete setup verification
2. ✅ Run first test
3. ✅ Read documentation
4. ✅ Make first code change
5. ✅ Deploy with confidence

**Questions?** Check the documentation files or review the inline code comments.

---

**Last Updated**: March 2026
**Maintained By**: Development Team
**Status**: ✅ Production Ready

