package se.elfu.sportprojectbackend.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "sport_meet"; //TODO

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous().disable() // No anonymoususer
                .authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ADMIN')") // Have to admin to use these endpoints
                .antMatchers("/units", "/units/**").access("hasAnyRole('ADMIN', 'USER')") // can be user or admin
                .antMatchers("/users", "/units/**").access("hasAnyRole('ADMIN', 'USER')")
                .antMatchers("/requests", "/requests/**").access("hasAnyRole('ADMIN', 'USER')")
                .antMatchers("/events", "events/**").access("hasAnyRole('ADMIN', 'USER')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
