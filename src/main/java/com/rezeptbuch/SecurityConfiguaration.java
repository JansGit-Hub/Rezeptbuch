package com.rezeptbuch;

import com.rezeptbuch.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

    @EnableWebSecurity
    @Configuration
    public class SecurityConfiguaration extends VaadinWebSecurity {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            setLoginView(http, LoginView.class);
        }



//    @Bean
//    UserDetailsManager userDetailsManager() {
//        return new InMemoryUserDetailsManager(
//                User.withUsername("admin")
//                        .password("{noop}adminpw")
//                        .roles("USER").build()
//        );
//    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/rezeptbuch")
                .username("postgres")
                .password("dbpass")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
