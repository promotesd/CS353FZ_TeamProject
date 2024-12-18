package com.project.app.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryDTO {

    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s6;
    private List<Integer> s5;
    private Long limit;
    private boolean index;

}
