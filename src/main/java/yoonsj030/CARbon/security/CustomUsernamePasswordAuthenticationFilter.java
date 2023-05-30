package yoonsj030.CARbon.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken;

        if (request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            // json request
            try {
                // read request body and mapping to login dto class by object mapper
                LoginDto loginDto = objectMapper.readValue(
                        request.getReader().lines().collect(Collectors.joining()), LoginDto.class);

                authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password);
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type(application/json) Parsing Error");
            }
        } else {
            // form-request
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        }

        this.setDetails(request, authenticationToken);

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Data
    static public class LoginDto {
        private String username;
        private String password;

        @JsonCreator
        public LoginDto(@JsonProperty("username") String username, @JsonProperty("password") String password) {
            this.username = username;
            this.password = password;
        }


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}