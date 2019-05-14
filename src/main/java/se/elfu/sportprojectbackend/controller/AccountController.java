package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.users.UserCreationDto;
import se.elfu.sportprojectbackend.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

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
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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
        log.info("Create account {} ", userCreationDto);
        return new ResponseEntity(accountService.registerUser(userCreationDto), HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity deleteAccount() {
        log.info("Delete Account");
        accountService.deleteAccount();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("password/reset")
    public ResponseEntity resetPassword(@RequestBody String email) {
        log.info("Reset password account for {} ", email);
        accountService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("password/update")
    public ResponseEntity validatePasswordToken(@RequestParam("token") UUID token){
        log.info("Validate password token");

        accountService.validatePasswordToken(token);
        return ResponseEntity.ok().build();
    }
    @GetMapping("password/save")
    public ResponseEntity savePassword(@RequestParam(name ="token") UUID token, @RequestParam(name ="password") String password){
        log.info("Password save ");
        accountService.saveNewPassword(password, token);
        return ResponseEntity.ok().build();
    }
}

