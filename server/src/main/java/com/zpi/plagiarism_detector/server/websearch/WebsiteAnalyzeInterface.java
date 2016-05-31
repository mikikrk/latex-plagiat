package com.zpi.plagiarism_detector.server.websearch;

import com.zpi.plagiarism_detector.commons.protocol.DocumentData;

import java.util.List;
import java.util.Set;

public interface WebsiteAnalyzeInterface {
    List<DocumentData> analyze(Set<String> keywords);
}
