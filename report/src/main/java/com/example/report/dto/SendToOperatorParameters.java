package com.example.report.dto;
public class SendToOperatorParameters {
    private String operator;
    private Long id;

    public SendToOperatorParameters() {}

    public Long getId() { return id;} 
    public String getOperator() { return operator;}
    
    public void setid(Long i) { id=i;}
    public void setOperator(String o) { operator = o;}
}
