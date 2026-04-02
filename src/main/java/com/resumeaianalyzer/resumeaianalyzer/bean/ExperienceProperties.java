package com.resumeaianalyzer.resumeaianalyzer.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperienceProperties {

    private Map<String, String> experienceAndLocations;
    private List<String> recommendations;
}
