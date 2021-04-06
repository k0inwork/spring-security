

package com.example.report.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.jdbcAuthentication().dataSource(dataSource)
        
        .usersByUsernameQuery("select username, password, enabled"
            + " from users where username=?")
        .authoritiesByUsernameQuery("select username, authority "
            + "from authorities where username=?");   
        }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/tooperator").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/userlist").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/edit").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/accept").hasRole("OPERATOR")
                .antMatchers(HttpMethod.POST, "/reject").hasRole("OPERATOR")
                .antMatchers(HttpMethod.POST, "/sendtooperator/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/reports").hasRole("USER")

                .antMatchers(HttpMethod.GET, "/myconsole").permitAll()
                .and().csrf().ignoringAntMatchers("/myconsole/**")
                .and().headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .formLogin().disable();

        
    }

    

    @Bean (name="passswordEncdcode")
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager()
	{
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		
		return jdbcUserDetailsManager;
	}

    
}
