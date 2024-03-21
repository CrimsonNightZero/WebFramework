package org.web.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.web.infrastructure.dto.UserQueriesDTO;
import org.web.infrastructure.dto.UserQueryDTO;

public class DomainController {
    private final DomainService domainService;
    private final Map<String, User> tokens;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
        this.tokens = new HashMap<>();
    }

    public HTTPResponse registerUser(HTTPRegisterRequest httpPOSTRequest) {
        User user;
        try {
            user = domainService.registerUser(httpPOSTRequest);
        } catch (IncorrectFormatOfEmailException exception) {
            throw new IncorrectFormatOfEmailException("Registration's format incorrect.");
        }

        return new RegisterUserDTO(user).response();
    }

    public UserLoginDTO login(HTTPLoginRequest httpLoginRequest) {
        User user;
        try {
            user = domainService.login(httpLoginRequest);
        } catch (IncorrectFormatOfEmailException exception) {
            throw new IncorrectFormatOfEmailException("Login's format incorrect.");
        }

        String token = String.valueOf(UUID.randomUUID());
        tokens.put(token, user);

        return new UserLoginDTO(user, token);
    }

    public HTTPResponse rename(HTTPRenameRequest httpRenameRequest, HTTPRequest httpRequest) {
        String authorization = httpRequest.getHttpHeaders().get("authorization");
        Map<String, Object> pathVariable = httpRequest.getHttpPath().getPathVariable();
        int userId = Integer.parseInt(pathVariable.get("userId").toString());
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

    public List<UserQueryDTO> userQuery(HTTPRequest httpRequest) {
        String authorization = httpRequest.getHttpHeaders().get("authorization");
        validToken(authorization);
        Map<String, Object> httpQueryVariable = httpRequest.getHttpQueryVariable();
        List<User> users;
        if (httpQueryVariable.containsKey("keyword")) {
            users = domainService.userQuery((String) httpQueryVariable.get("keyword"));
        } else {
            users = domainService.userQuery();
        }

        return new UserQueriesDTO(users).response();
    }
}
