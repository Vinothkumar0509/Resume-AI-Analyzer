package com.resumeaianalyzer.resumeaianalyzer.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for fetching and processing web content
 * Extracts text from URLs and cleans HTML tags
 */
public class WebContentFetcher {

    private static final int TIMEOUT = 10000; // 10 seconds
    private static final int MAX_CONTENT_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SCRIPT_STYLE_PATTERN = Pattern.compile("<(script|style)[^>]*>.*?</\\1>", Pattern.DOTALL);

    /**
     * Fetch HTML content from URL
     */
    public static String fetchWebContent(String urlString) throws Exception {
        if (StringUtils.isEmpty(urlString)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        // Validate URL format
        if (!isValidUrl(urlString)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            // Set User-Agent to avoid being blocked
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

            StringBuilder content = new StringBuilder();
            int contentLength = 0;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null && contentLength < MAX_CONTENT_SIZE) {
                    content.append(line).append("\n");
                    contentLength += line.length();
                }
            }

            if (contentLength >= MAX_CONTENT_SIZE) {
                throw new IllegalArgumentException("Content exceeds maximum allowed size");
            }

            return content.toString();
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + e.getMessage());
        } catch (java.net.ConnectException e) {
            throw new Exception("Unable to connect to URL: " + e.getMessage());
        }
    }

    /**
     * Extract text from HTML content by removing tags
     */
    public static String extractTextFromHtml(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return "";
        }

        String content = htmlContent;

        // Remove script and style elements
        content = SCRIPT_STYLE_PATTERN.matcher(content).replaceAll(" ");

        // Remove HTML tags
        content = HTML_TAG_PATTERN.matcher(content).replaceAll(" ");

        // Decode HTML entities
        content = decodeHtmlEntities(content);

        // Clean up whitespace
        content = content.replaceAll("\\s+", " ").trim();

        return content;
    }

    /**
     * Decode HTML entities
     */
    private static String decodeHtmlEntities(String text) {
        return text
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ")
                .replace("&copy;", "©")
                .replace("&reg;", "®");
    }

    /**
     * Validate URL format
     */
    private static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return url.startsWith("http://") || url.startsWith("https://");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract job description from HTML content
     */
    public static String extractJobDescription(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return "";
        }

        // Try to find common job description tags/classes
        String jobDesc = extractByPattern(htmlContent,
                "(job.?description|job.?details|description|requirements|about.?job)",
                1000);

        if (!jobDesc.isEmpty()) {
            return jobDesc;
        }

        // Fallback: extract all text and try to identify key sections
        String text = extractTextFromHtml(htmlContent);

        // Remove common header/footer patterns
        text = text.replaceAll("(?i)(apply now|login|sign up|register|home|about|contact|careers).*", "");

        return text.length() > 500 ? text.substring(0, Math.min(2000, text.length())) : text;
    }

    /**
     * Extract content by regex pattern
     */
    private static String extractByPattern(String html, String pattern, int maxLength) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);

        if (m.find()) {
            int start = m.start();
            int end = Math.min(start + maxLength, html.length());
            String content = html.substring(start, end);
            return extractTextFromHtml(content);
        }

        return "";
    }

    /**
     * Get domain from URL
     */
    public static String getDomainFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (Exception e) {
            return "unknown";
        }
    }
}

