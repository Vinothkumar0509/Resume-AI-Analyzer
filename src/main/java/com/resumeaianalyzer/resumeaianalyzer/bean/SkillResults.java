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
public class SkillResults {

    private String name;
    private List<String> values;
}
