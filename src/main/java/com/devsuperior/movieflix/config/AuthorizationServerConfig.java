package com.devsuperior.movieflix.config;

import com.devsuperior.movieflix.components.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${jwt.duration}")
    private Integer duration;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtAccessTokenConverter accessTokenConverter;

    private final JwtTokenStore tokenStore;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenEnhancer tokenEnhancer;

    private final UserDetailsService userDetailsService;

    public AuthorizationServerConfig(BCryptPasswordEncoder passwordEncoder, JwtAccessTokenConverter accessTokenConverter, JwtTokenStore tokenStore, AuthenticationManager authenticationManager, JwtTokenEnhancer tokenEnhancer, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.tokenEnhancer = tokenEnhancer;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")//permitindo o acesso a chave public usada para validar o token
                .checkTokenAccess("isAthenticated()");//configurando o acesso ao endpoint que verifica a validade dos tokens.
        // isAthenticated -> permite o acesso apenas para user autenticados

        // deixando essas configurações publicas, permitimos uma maior facilidade na integração de outras aplicações
        // geralmente se é negado o acesso a chave public quando aplicações demanda uma altissima segurança
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //definimos a nossa autenticação e quais serão os dados do cliente
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(duration)
                .refreshTokenValiditySeconds(duration);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer));

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(chain)
                .userDetailsService(userDetailsService);
    }

}
