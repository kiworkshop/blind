package org.kiworkshop.blind.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;
    private HashMap<String, String> jsonRequest;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String passwordParameter = super.getPasswordParameter();
        if (request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return jsonRequest.get(passwordParameter);
        }
        return request.getParameter(passwordParameter);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String usernameParameter = super.getUsernameParameter();
        if (request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return jsonRequest.get(usernameParameter);
        }
        return request.getParameter(usernameParameter);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
        }
        if (request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.jsonRequest = mapper.readValue(request.getReader().lines().collect(Collectors.joining()), new TypeReference<HashMap<String, String>>(){});
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request content-type parsing error");
            }
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("[Login request] email: {}, password: ****", username);
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
            username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
