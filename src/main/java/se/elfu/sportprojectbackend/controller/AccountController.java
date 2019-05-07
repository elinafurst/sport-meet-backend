package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.users.UserCreationDto;
import se.elfu.sportprojectbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("account")
@Slf4j
public class AccountController {

    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @Autowired
    private HttpServletRequest request;
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("signout")
    public ResponseEntity revokeToken() {
        log.info("Sign out");

        String authorization = request.getHeader(AUTHORIZATION);

        if (authorization != null && authorization.contains(BEARER)){
            String tokenId = authorization.substring(BEARER.length() + 1);
            consumerTokenServices.revokeToken(tokenId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("signup")
    public ResponseEntity createAccount(@Valid @RequestBody UserCreationDto userCreationDto) {
        log.info("POST Create account ");
        return new ResponseEntity(userService.registerUser(userCreationDto), HttpStatus.CREATED);
    }

    @PostMapping("password/reset")
    public String resetPassword( @RequestBody String test) {
        log.info("POST Create account ");
        return "INNE";
    }
    }

