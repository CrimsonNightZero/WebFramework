package org.web.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.web.application.DomainService;
import org.web.application.HTTPLoginRequest;
import org.web.application.HTTPRegisterRequest;
import org.web.application.HTTPRenameRequest;
import org.web.domain.User;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.domain.exceptions.ForbiddenException;
import org.web.domain.exceptions.IllegalAuthenticationException;
import org.web.domain.exceptions.IncorrectFormatOfEmailException;
import org.web.domain.exceptions.InvalidNameFormatException;
import org.web.infrastructure.dto.RegisterUserDTO;
import org.web.infrastructure.dto.UserLoginDTO;
import org.web.infrastructure.dto.UserQueryDTO;

public class DomainController {
    public DomainService domainService;
    public Map<String, User> tokens;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
        this.tokens = new HashMap<>();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public DomainService getDomainService() {
        return domainService;
    }

    public Map<String, User> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, User> tokens) {
        this.tokens = tokens;
    }

    public HTTPResponse registerUser(HTTPRequest httpRequest) {
        HTTPRegisterRequest httpPOSTRequest = httpRequest.readBodyAsObject(HTTPRegisterRequest.class);

        User user;
        try {
            user = domainService.registerUser(httpPOSTRequest);
        } catch (IncorrectFormatOfEmailException exception) {
            throw new IncorrectFormatOfEmailException("Registration's format incorrect.");
        }

        HTTPResponse httpResponse = new HTTPResponse(201);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new RegisterUserDTO(user.getId(), user.getEmail(), user.getName()));
        return httpResponse;
    }

    public HTTPResponse login(HTTPRequest httpRequest) {
        HTTPLoginRequest httpLoginRequest = httpRequest.readBodyAsObject(HTTPLoginRequest.class);

        User user;
        try {
            user = domainService.login(httpLoginRequest);
        } catch (IncorrectFormatOfEmailException exception) {
            throw new IncorrectFormatOfEmailException("Login's format incorrect.");
        }

        String token = String.valueOf(UUID.randomUUID());
        tokens.put(token, user);

        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new UserLoginDTO(user.getId(), user.getEmail(), user.getName(), token));
        return httpResponse;
    }

    public HTTPResponse rename(HTTPRequest httpRequest) {
        String authorization = httpRequest.getHttpHeaders().get("Authorization");
        Map<String, Object> pathVariable = httpRequest.getHttpPath().getPathVariable();
        int userId = Integer.parseInt(pathVariable.get("userId").toString());
        HTTPRenameRequest httpRenameRequest = httpRequest.readBodyAsObject(HTTPRenameRequest.class);
        httpRenameRequest.id = userId;
        validToken(authorization);
        validPermission(authorization, userId);

        try {
            domainService.rename(httpRenameRequest);
        } catch (InvalidNameFormatException exception) {
            throw new InvalidNameFormatException("Name's format invalid.");
        }

        return new HTTPResponse(204);
    }

    private void validToken(String authorization) {
        String token = parseToken(authorization);
        if (!tokens.containsKey(token)) {
            throw new IllegalAuthenticationException("Can't authenticate who you are.");
        }
    }

    private void validPermission(String authorization, int userId) {
        User user = getUserByAuthorization(authorization);
        if (user.getId() != userId) {
            throw new ForbiddenException("Forbidden");
        }
    }

    private User getUserByAuthorization(String authorization) {
        return tokens.get(parseToken(authorization));
    }

    private String parseToken(String authorization) {
        return authorization.replace("Bearer ", "").strip();
    }

    public HTTPResponse userQuery(HTTPRequest httpRequest) {
        String authorization = httpRequest.getHttpHeaders().get("Authorization");
        validToken(authorization);
        Map<String, Object> httpQueryVariable = httpRequest.getHttpQueryVariable();
        List<User> users;
        if (httpQueryVariable.containsKey("keyword")) {
            users = domainService.userQuery((String) httpQueryVariable.get("keyword"));
        } else {
            users = domainService.userQuery();
        }

        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(users.stream().map(UserQueryDTO::new).collect(Collectors.toList()));
        return httpResponse;
    }
}
