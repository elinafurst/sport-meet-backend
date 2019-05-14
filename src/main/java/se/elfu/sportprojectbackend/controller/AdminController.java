package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.elfu.sportprojectbackend.controller.model.events.locations.LocationDto;
import se.elfu.sportprojectbackend.service.AdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("sports")
    public ResponseEntity createSport(@Valid @RequestBody String sport){
        log.info("Create sport {} ", sport);
        return new ResponseEntity(adminService.createSport(sport), HttpStatus.CREATED);
    }

    @PostMapping("locations")
    public ResponseEntity createLocation(@Valid @RequestBody LocationDto locationDto){
        log.info("Create locations {} ", locationDto);
        adminService.createLocation(locationDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
