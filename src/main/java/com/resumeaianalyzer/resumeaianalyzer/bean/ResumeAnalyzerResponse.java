package com.resumeaianalyzer.resumeaianalyzer.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeAnalyzerResponse {

    private Double resumeScore;
    private String matchType;
    private String description;
    private List<SkillResults> skillResults;
    private ExperienceProperties experienceProperties;
}
