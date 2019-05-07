package se.elfu.sportprojectbackend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${spring.security.client.id}")
    private String CLIEN_ID;
    @Value("${spring.security.client.secret}")
    private  String CLIENT_SECRET;
    @Value("${spring.security.access}")
    private int ACCESS_TOKEN_VALIDITY_SECONDS;
    @Value("${spring.security.refresh}")
    private int REFRESH_TOKEN_VALIDITY_SECONDS;
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String IMPLICIT = "implicit";
    private static final String SCOPE_READ = "preview";
    private static final String SCOPE_WRITE = "write";
    private static final String TRUST = "trust";


    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        String encodedClientSecret = passwordEncoder.encode(CLIENT_SECRET);
        configurer
                .inMemory()
                .withClient(CLIEN_ID)
                .secret(encodedClientSecret)
                .authorities("ROLE_USER", "ROLE_ADMIN")
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT ) //TODO authorization code
                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
    }
}
