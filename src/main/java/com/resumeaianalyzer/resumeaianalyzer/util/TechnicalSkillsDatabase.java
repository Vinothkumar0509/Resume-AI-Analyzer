package com.resumeaianalyzer.resumeaianalyzer.util;

import java.util.*;

/**
 * Technical Skills Database
 * Comprehensive list of valid technical skills organized by category
 */
public class TechnicalSkillsDatabase {

    // Programming Languages
    private static final Set<String> PROGRAMMING_LANGUAGES = new HashSet<>(Arrays.asList(
            "java", "python", "javascript", "typescript", "c#", "c++", "go", "rust", "kotlin",
            "scala", "groovy", "clojure", "elixir", "erlang", "haskell", "r", "matlab",
            "php", "ruby", "perl", "swift", "objective-c", "dart", "lua", "sql"
    ));

    // Frontend Frameworks & Libraries
    private static final Set<String> FRONTEND_TECHNOLOGIES = new HashSet<>(Arrays.asList(
            "react", "angular", "vue", "jquery", "bootstrap", "tailwind", "material-ui",
            "webpack", "gulp", "grunt", "babel", "npm", "yarn", "html", "css", "scss", "sass",
            "less", "next.js", "gatsby", "nuxt", "ember", "backbone", "knockout"
    ));

    // Backend Frameworks
    private static final Set<String> BACKEND_FRAMEWORKS = new HashSet<>(Arrays.asList(
            "spring", "spring boot", "spring cloud", "hibernate", "jpa", "mybatis",
            "django", "flask", "fastapi", "tornado", "pyramid",
            "express", "nest.js", "koa", "hapi", "fastify",
            "rails", "sinatra", "hanami",
            "laravel", "symfony", "yii", "slim", "zend",
            "asp.net", "asp.net core", "entity framework", "linq",
            "gin", "beego", "iris",
            "actix", "rocket", "axum",
            "phoenix", "ecto"
    ));

    // Databases
    private static final Set<String> DATABASES = new HashSet<>(Arrays.asList(
            "mysql", "postgresql", "oracle", "sql server", "mariadb", "sqlite",
            "mongodb", "cassandra", "couchdb", "dynamodb", "firestore", "redis",
            "elasticsearch", "solr", "neo4j", "influxdb", "timescaledb",
            "h2", "db2", "teradata", "snowflake", "redshift", "bigquery",
            "hbase", "druid", "cockroachdb"
    ));

    // Cloud Platforms & Services
    private static final Set<String> CLOUD_PLATFORMS = new HashSet<>(Arrays.asList(
            "aws", "azure", "gcp", "google cloud", "heroku", "digitalocean",
            "linode", "vultr", "ibm cloud", "oracle cloud", "alibaba cloud",
            "ec2", "s3", "lambda", "rds", "dynamodb", "sqs", "sns", "cloudformation",
            "appservice", "cosmos db", "azure functions", "sql azure",
            "cloud run", "app engine", "cloud functions", "firestore",
            "cloudflare", "cdn"
    ));

    // DevOps & Infrastructure
    private static final Set<String> DEVOPS_TOOLS = new HashSet<>(Arrays.asList(
            "docker", "kubernetes", "jenkins", "gitlab-ci", "github actions", "circleci",
            "travis ci", "bamboo", "terraform", "ansible", "puppet", "chef",
            "prometheus", "grafana", "elk stack", "datadog", "new relic",
            "splunk", "sumo logic", "logstash", "fluentd",
            "nginx", "apache", "haproxy", "load balancer",
            "git", "svn", "mercurial", "perforce"
    ));

    // Testing Tools & Frameworks
    private static final Set<String> TESTING_TOOLS = new HashSet<>(Arrays.asList(
            "junit", "testng", "mockito", "powermock",
            "jest", "mocha", "jasmine", "chai", "enzyme",
            "pytest", "unittest", "nose", "behave",
            "rspec", "cucumber",
            "phpunit", "pestphp",
            "selenium", "cypress", "playwright", "webdriver", "appium",
            "sonarqube", "jacoco", "cobertura"
    ));

    // Big Data & Analytics
    private static final Set<String> BIG_DATA_TOOLS = new HashSet<>(Arrays.asList(
            "hadoop", "spark", "kafka", "hive", "pig", "flume", "sqoop",
            "flink", "storm", "airflow", "nifi",
            "presto", "trino", "impala",
            "luigi", "oozie", "azkban"
    ));

    // Machine Learning & AI
    private static final Set<String> ML_TOOLS = new HashSet<>(Arrays.asList(
            "tensorflow", "pytorch", "keras", "scikit-learn", "xgboost",
            "catboost", "lightgbm", "pandas", "numpy", "scipy",
            "matplotlib", "seaborn", "plotly", "ggplot",
            "jupyter", "anaconda", "conda",
            "mlflow", "wandb", "mlops", "kubeflow"
    ));

    // Message Queues & Streaming
    private static final Set<String> MESSAGE_QUEUES = new HashSet<>(Arrays.asList(
            "kafka", "rabbitmq", "activemq", "zeromq", "mqtt",
            "kinesis", "sqs", "sns", "pubsub", "nats"
    ));

    // API & Web Services
    private static final Set<String> API_TECHNOLOGIES = new HashSet<>(Arrays.asList(
            "rest", "rest api", "graphql", "soap", "grpc", "websocket",
            "swagger", "openapi", "postman", "insomnia", "apigee"
    ));

    // Build Tools & Package Managers
    private static final Set<String> BUILD_TOOLS = new HashSet<>(Arrays.asList(
            "maven", "gradle", "ant", "npm", "yarn", "pip", "gems",
            "cargo", "go modules", "composer", "nuget",
            "sbt", "lein", "mix"
    ));

    // Security
    private static final Set<String> SECURITY_TOOLS = new HashSet<>(Arrays.asList(
            "ssl", "tls", "oauth", "jwt", "kerberos", "ldap",
            "pki", "certificate", "encryption", "aes", "rsa",
            "bcrypt", "argon2", "scrypt",
            "owasp", "burp suite", "zaproxy", "nessus"
    ));

    // Soft Skills (exclude these as they're not technical)
    private static final Set<String> SOFT_SKILLS = new HashSet<>(Arrays.asList(
            "communication", "teamwork", "leadership", "problem solving", "analytical",
            "creativity", "organization", "detail oriented", "collaborative", "adaptability",
            "initiative", "motivated", "responsible", "reliable", "professional",
            "excellent", "strong", "good", "experienced", "proficient"
    ));

    // Common non-skills to filter
    private static final Set<String> NON_SKILLS = new HashSet<>(Arrays.asList(
            "projects", "experience", "years", "skills", "features", "stack",
            "scalable", "efficient", "robust", "design", "develop", "maintain",
            "collaborate", "programming", "knowledge", "testing", "unit", "exposure",
            "visual", "seamless", "product", "images", "web", "application",
            "data", "software", "solutions", "system", "development",
            "management", "process", "implementation", "maintenance", "deployment",
            "training", "documentation", "support", "optimization", "improvement",
            "design patterns", "architecture", "model", "view", "controller",
            "business", "domain", "logic", "flow", "cycle", "pipeline",
            "introduced", "implemented", "developed", "created", "built",
            "frontend", "backend", "full stack", "end to end", "end-to-end"
    ));

    // Combine all technical skills
    private static final Set<String> ALL_TECHNICAL_SKILLS = new HashSet<>();

    static {
        ALL_TECHNICAL_SKILLS.addAll(PROGRAMMING_LANGUAGES);
        ALL_TECHNICAL_SKILLS.addAll(FRONTEND_TECHNOLOGIES);
        ALL_TECHNICAL_SKILLS.addAll(BACKEND_FRAMEWORKS);
        ALL_TECHNICAL_SKILLS.addAll(DATABASES);
        ALL_TECHNICAL_SKILLS.addAll(CLOUD_PLATFORMS);
        ALL_TECHNICAL_SKILLS.addAll(DEVOPS_TOOLS);
        ALL_TECHNICAL_SKILLS.addAll(TESTING_TOOLS);
        ALL_TECHNICAL_SKILLS.addAll(BIG_DATA_TOOLS);
        ALL_TECHNICAL_SKILLS.addAll(ML_TOOLS);
        ALL_TECHNICAL_SKILLS.addAll(MESSAGE_QUEUES);
        ALL_TECHNICAL_SKILLS.addAll(API_TECHNOLOGIES);
        ALL_TECHNICAL_SKILLS.addAll(BUILD_TOOLS);
        ALL_TECHNICAL_SKILLS.addAll(SECURITY_TOOLS);
    }

    /**
     * Check if a term is a valid technical skill
     */
    public static boolean isValidTechnicalSkill(String term) {
        if (term == null || term.isEmpty()) {
            return false;
        }

        String normalized = term.toLowerCase().trim();

        // Check against non-skills list first
        if (NON_SKILLS.contains(normalized)) {
            return false;
        }

        // Check against soft skills
        if (SOFT_SKILLS.contains(normalized)) {
            return false;
        }

        // Check if it's in the comprehensive technical skills database
        if (ALL_TECHNICAL_SKILLS.contains(normalized)) {
            return true;
        }

        // Check for partial matches (e.g., "Spring Boot" contains "Spring")
        for (String skill : ALL_TECHNICAL_SKILLS) {
            if (normalized.contains(skill) || skill.contains(normalized)) {
                // Make sure it's a meaningful match
                if (normalized.length() > 3 && skill.length() > 3) {
                    return true;
                }
            }
        }

        // Filter out very generic terms
        if (normalized.length() < 3) {
            return false;
        }

        // Check for common patterns that indicate a skill
        // Version numbers: Java8, Python3.9, etc.
        if (normalized.matches(".*\\d.*") && normalized.length() > 2) {
            // Must be a known language/tool with version
            String baseSkill = normalized.replaceAll("[0-9.]", "");
            return ALL_TECHNICAL_SKILLS.contains(baseSkill);
        }

        return false;
    }

    /**
     * Get all valid technical skills
     */
    public static Set<String> getAllTechnicalSkills() {
        return new HashSet<>(ALL_TECHNICAL_SKILLS);
    }

    /**
     * Get programming languages
     */
    public static Set<String> getProgrammingLanguages() {
        return new HashSet<>(PROGRAMMING_LANGUAGES);
    }

    /**
     * Get frontend technologies
     */
    public static Set<String> getFrontendTechnologies() {
        return new HashSet<>(FRONTEND_TECHNOLOGIES);
    }

    /**
     * Get backend frameworks
     */
    public static Set<String> getBackendFrameworks() {
        return new HashSet<>(BACKEND_FRAMEWORKS);
    }

    /**
     * Get databases
     */
    public static Set<String> getDatabases() {
        return new HashSet<>(DATABASES);
    }

    /**
     * Get cloud platforms
     */
    public static Set<String> getCloudPlatforms() {
        return new HashSet<>(CLOUD_PLATFORMS);
    }

    /**
     * Get DevOps tools
     */
    public static Set<String> getDevOpsTools() {
        return new HashSet<>(DEVOPS_TOOLS);
    }

    /**
     * Get testing tools
     */
    public static Set<String> getTestingTools() {
        return new HashSet<>(TESTING_TOOLS);
    }

    /**
     * Get big data tools
     */
    public static Set<String> getBigDataTools() {
        return new HashSet<>(BIG_DATA_TOOLS);
    }

    /**
     * Get ML tools
     */
    public static Set<String> getMLTools() {
        return new HashSet<>(ML_TOOLS);
    }

    /**
     * Extract valid skills from text
     */
    public static Set<String> extractValidSkills(String text) {
        if (text == null || text.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> validSkills = new HashSet<>();
        String normalized = text.toLowerCase();

        // Check each technical skill
        for (String skill : ALL_TECHNICAL_SKILLS) {
            if (normalized.contains(skill)) {
                validSkills.add(skill);
            }
        }

        return validSkills;
    }
}

