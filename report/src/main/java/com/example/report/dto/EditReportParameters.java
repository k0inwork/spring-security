package com.example.report.dto;
public class EditReportParameters {
    private String text;
    private Long id;

    public EditReportParameters() {}

    public Long getId() { return id;} 
    public String getText() { return text;}
    
    public void setid(Long i) { id=i;}
    public void setText(String o) { text = o;}
}
