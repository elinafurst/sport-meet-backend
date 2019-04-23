package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.UserDto;
import se.elfu.sportprojectbackend.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("active")
    public ResponseEntity updateUser(@RequestBody UserDto userDto){
        log.info("PUT update active user: {}", userDto);
        userService.updateUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userNumber}")
    public ResponseEntity getUser(@PathVariable("userNumber") UUID userNumber){
        return ResponseEntity.ok(userService.getUser(userNumber));
    }

    @GetMapping("active/units")
    public ResponseEntity getGroupsForActiveUser(){
        return ResponseEntity.ok(userService.getGroupsForActiveUser());
    }


    @PutMapping("active/units/{unitNumber}")
    public ResponseEntity joinGroup(@PathVariable("unitNumber") UUID unitNumber) {
        userService.joinGroup(unitNumber);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("active/units/{unitNumber}")
    public ResponseEntity leaveGroup(@PathVariable("unitNumber") UUID unitNumber) {
        userService.leaveGroup(unitNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("active")
    public ResponseEntity getActiveUser() {
        return new ResponseEntity(userService.getActiveUser(), HttpStatus.OK);
    }

}