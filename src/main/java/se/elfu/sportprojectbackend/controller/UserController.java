package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.users.UserDto;
import se.elfu.sportprojectbackend.service.AccountService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("active")
    public ResponseEntity updateUser(@Valid @RequestBody UserDto userDto){
        log.info("Update active user: {}", userDto);
        accountService.updateUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userNumber}")
    public ResponseEntity getUser(@PathVariable("userNumber") UUID userNumber){
        log.info("Get user: {}", userNumber);

        return ResponseEntity.ok(accountService.getUser(userNumber));
    }

    @GetMapping("active/units")
    public ResponseEntity getUnitsForActiveUser(){
        log.info("Get units for acitve user");

        return ResponseEntity.ok(accountService.getUnitssForActiveUser());
    }


    @GetMapping("active")
    public ResponseEntity getActiveUser() {
        log.info("Get acitve user");
        return new ResponseEntity(accountService.getActiveUser(), HttpStatus.OK);
    }

}
