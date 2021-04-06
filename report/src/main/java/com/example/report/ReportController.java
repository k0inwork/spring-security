package com.example.report;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.http.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import javax.validation.*;
import javax.validation.constraints.*;

import com.example.report.dto.*;

import org.springframework.validation.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.ArrayList;

@Validated
@RestController
public class ReportController {
    
    @Autowired
    DataSource dataSource;

    private ArrayList getAuthorities() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArrayList<String> roles = new ArrayList<String>();        
        if (user == null || !User.class.isInstance(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong user");
        }
        String userName = ((User)user).getUsername();

        for (GrantedAuthority ga : ((User)user).getAuthorities()) {
            roles.add(ga.getAuthority());
        }
        return roles;        
    }

    @GetMapping("/reports")
    public List<Report> index() {

        ArrayList<Report> ret = new ArrayList<Report>();
        
        ArrayList roles = getAuthorities();
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((User)user).getUsername();


        if (roles.contains("ROLE_OPERATOR")) {

            for (Report r:repository.findByOperatorAndStatus(userName,Report.Status.INWORK)) {
                String m = r.getMessage();

                if (m.length()>1) {
                    StringBuffer sbb= new StringBuffer();
                    char[] str = m.toCharArray();                    

                    for (int i=0;i<m.length()-1;i++) {
                        sbb.append(str[i]);sbb.append('-');
                    }
                    sbb.append(str[m.length()-1]);
                    m = sbb.toString();
                }
                r.setMessage(m);
                ret.add(r);
            }
        }
        if (roles.contains("ROLE_USER")) {
            ret.addAll(repository.findByCreator(userName));
        }

        return ret;

    }

    @Autowired
    JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private ReportRepository repository;

    @PostMapping("/reports")
    Report newReport(@Valid @RequestBody Report newReport) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null && User.class.isInstance(user))
            newReport.setCreator(((User)user).getUsername());

        newReport.setStatus(Report.Status.DRAFT);
            
        return repository.save(newReport);
    }

    @PostMapping(path = "/edit", consumes = "application/json")
    public Report editReport(@RequestBody EditReportParameters params) {
        String text = params.getText();
        Long id = params.getId();

        if (id<=0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No or wrong id given");

        if (text==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No text given");

        Optional<Report> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong id.");
        }

        Report r = opt.get();

        ArrayList roles = getAuthorities();
        if (!roles.contains("ROLE_USER"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only role=user may edit reports.");
        if (r.getStatus()!=Report.Status.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only DRAFT reports are editable.");
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!r.getCreator().equals(((User)user).getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User may edit report only crated by himself");


        r.setMessage(text);

        return repository.save(r);
    }

    @PostMapping(path = "/accept", consumes = "application/json")
    public Report acceptReport(@RequestBody StatusParameters params) {

        Long id = params.getId();

        if (id<=0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No or wrong id given");

        Optional<Report> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong id.");
        }

        Report r = opt.get();

        ArrayList roles = getAuthorities();
        if (!roles.contains("ROLE_OPERATOR"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only role=operator may change status.");
        if (r.getStatus()!=Report.Status.INWORK)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only INWORK reports are acceptable.");
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!r.getOperator().equals(((User)user).getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operator may chnage status only of assigned reports");


        r.setStatus(Report.Status.ACCEPTED);

        return repository.save(r);
    }

    @PostMapping(path = "/reject", consumes = "application/json")
    public Report rejectReport(@RequestBody StatusParameters params) {

        Long id = params.getId();

        if (id<=0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No or wrong id given");

        Optional<Report> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong id.");
        }

        Report r = opt.get();

        ArrayList roles = getAuthorities();
        if (!roles.contains("ROLE_OPERATOR"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only role=operator may change status.");
        if (r.getStatus()!=Report.Status.INWORK)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only INWORK reports are rejected.");
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!r.getOperator().equals(((User)user).getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operator may chnage status only of assigned reports");


        r.setStatus(Report.Status.REJECTED);

        return repository.save(r);
    }


    @PostMapping(path = "/sendtooperator", consumes = "application/json")
    public Report sendToOperator(@RequestBody SendToOperatorParameters params) {

        String operator = params.getOperator();
        Long id = params.getId();

        if (id<=0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No or wrong id given");

        if (operator == null || operator.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No operator given");


        Optional<Report> opt = repository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong id.");
        }

        Report r = opt.get();

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null && User.class.isInstance(user))
            if (r.getCreator().equals(((User)user).getUsername())) {


                Object op = jdbcUserDetailsManager.loadUserByUsername(operator);
                if (op == null || !User.class.isInstance(op))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No operator provided");
                else {
                    for (GrantedAuthority ga : ((User)op).getAuthorities()) {
                        if (ga.getAuthority().equals("ROLE_OPERATOR")) {

                            r.setOperator(operator);
                            r.setStatus(Report.Status.INWORK);
                            return repository.save(r);
                        }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"operator has no operaror rights");
                    }
                }

            } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Current user is Not an owner of the report.");

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong request.");
            
    }
    

    @PostMapping(path = "/tooperator", consumes = "application/json")
    public Map toOperator(@RequestBody TooperatorParameters username) {
        Object op = jdbcUserDetailsManager.loadUserByUsername(username.username);
        if (op == null || !User.class.isInstance(op))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No given user");

        User user = (User)op;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
                jdbcTemplate.execute("insert into authorities(username,authority) values('"+user.getUsername()+"','ROLE_OPERATOR')");
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something went wrong");
        }

        HashMap<String,String> h = new HashMap<String,String>();
        h.put("status", "OK");
        return h;
    }
    @GetMapping(path = "/userlist", consumes = "application/json")
    public List<com.example.report.dto.User> userlist() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
                jdbcTemplate.execute("select * from users");
                List<com.example.report.dto.User> results = jdbcTemplate.query(
                    "select * from users", 
                    new RowMapper<com.example.report.dto.User>() {
                        @Override
                        public com.example.report.dto.User mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new com.example.report.dto.User(rs.getString("username"));
                        }
                    });       
                    return results; 
            } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something went wrong");
        }

    }

    @ExceptionHandler(Exception.class)
    public ExceptionMessage handleException(Exception ex) {
        ExceptionMessage em = new ExceptionMessage();
        em.message = ex.getMessage();
        em.status = HttpStatus.BAD_REQUEST;
        return em;
    }

}