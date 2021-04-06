
package com.example.report;

import javax.validation.Valid;
import javax.validation.constraints.*;

import javax.persistence.*;


@Entity
public class Report {

   

    public enum Status {
        DRAFT, INWORK, ACCEPTED, REJECTED
    }

    @Id
    @GeneratedValue
    private Long id;

    
    @Valid
    @NotNull(message = "message is a mandatory field")
    private String message;

    @Enumerated(EnumType.STRING)
    @Valid
    @NotNull(message = "status is a mandatory field")
    private Status status;

    public Report(String msg, Status stat, String creatoruser) {
        message = msg;
        status = stat;
        creator = creatoruser;
    }

    public String getMessage() { return message; }
    public void  setMessage(String m) {  message = m; }
    public Status getStatus() { return status; }
    public void  setStatus(Status s) {  status=s; }

    public Long getId() { return id; }

    public Report() {
        message="";
        status = Status.DRAFT;
        creator = "";
    }

    private String creator;
    public String getCreator() { return creator; }
    protected void setCreator(String c) { creator = c; }
    
    private String operator;
    public String getOperator() { return operator; }
    protected void setOperator(String o) { operator = o; }
    
}