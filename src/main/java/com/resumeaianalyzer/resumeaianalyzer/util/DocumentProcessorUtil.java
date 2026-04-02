package com.resumeaianalyzer.resumeaianalyzer.util;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for document processing
 * Extracts text from various file formats (PDF, DOCX, TXT, etc.)
 */
public class DocumentProcessorUtil {

    private static final Tika tika = new Tika();
    private static final String[] SUPPORTED_FORMATS = {
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
            "application/msword", // .doc
            "text/plain",
            "text/rtf"
    };

    /**
     * Extract text from uploaded file
     */
    public static String extractTextFromFile(MultipartFile file) throws IOException, TikaException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String contentType = file.getContentType();
        if (!isSupportedFormat(contentType)) {
            throw new IllegalArgumentException(
                    "Unsupported file format. Supported formats: PDF, DOCX, DOC, TXT, RTF");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String text = tika.parseToString(inputStream);
            return text != null ? text : "";
        }
    }

    /**
     * Check if file format is supported
     */
    private static boolean isSupportedFormat(String contentType) {
        if (contentType == null) {
            return false;
        }

        for (String format : SUPPORTED_FORMATS) {
            if (contentType.equals(format) || contentType.contains(format)) {
                return true;
            }
        }

        // Also check by file extension as fallback
        return contentType.contains("pdf") || contentType.contains("word") ||
               contentType.contains("text") || contentType.contains("rtf") ||
               contentType.contains("document");
    }

    /**
     * Validate file size (max 10MB)
     */
    public static void validateFileSize(MultipartFile file, long maxSizeInMB) throws IOException {
        if (file.getSize() > (maxSizeInMB * 1024 * 1024)) {
            throw new IllegalArgumentException(
                    "File size exceeds the maximum allowed size of " + maxSizeInMB + "MB");
        }
    }
}

