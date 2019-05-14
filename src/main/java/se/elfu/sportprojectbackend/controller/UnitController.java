package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.units.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.params.Param;
import se.elfu.sportprojectbackend.service.UnitService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("units")
@Slf4j
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity createUnit(@Valid @RequestBody UnitCreationDto unitCreationDto) {
        log.info("Create unit {} ", unitCreationDto);
        return new ResponseEntity(unitService.createUnit(unitCreationDto), HttpStatus.CREATED);
    }

    @GetMapping("{unitNumber}")
    public ResponseEntity getUnit(@PathVariable("unitNumber") UUID unitNumber) {
        log.info("Get unit {}", unitNumber);
        return new ResponseEntity(unitService.getUnit(unitNumber), HttpStatus.CREATED);
    }

    @GetMapping("admins")
    public ResponseEntity getUnitsActiveUserIsAdminOf(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                      @RequestParam(name = "size", defaultValue = "18", required = false) int size,
                                                      @RequestParam(name="keyvalue") boolean keyvalue) {
        if(keyvalue) {
            log.info("GET Units active user is admin of keypairs");
            return  ResponseEntity.ok(unitService.getUnitsActiveUserIsAdminOfKeyPairs());
        }
        Param param = Param.builder()
                .size(18)
                .page(0)
                .build();
        log.info("GET Units active user is admin of, params {}", param);

        return  ResponseEntity.ok(unitService.getUnitsActiveUserIsAdminOf(param));
    }

    @GetMapping("{unitNumber}/events")
    public ResponseEntity getEventsForUnit(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                           @RequestParam(name = "size", defaultValue = "18", required = false) int size,
                                            @PathVariable("unitNumber") UUID unitNumber) {
        Param param = Param.builder()
                .size(size)
                .page(page)
                .build();

        log.info("GET events for unit: {}, params: {}", unitNumber, param);

        return ResponseEntity.ok(unitService.getEventsForUnit(unitNumber, param));
    }

    @GetMapping("{unitNumber}/joiner")
    public ResponseEntity joinGroup(@PathVariable("unitNumber") UUID unitNumber) {
        log.info("Join unit {}", unitNumber);
        unitService.joinGroup(unitNumber);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{unitNumber}/joiner")
    public ResponseEntity leaveGroup(@PathVariable("unitNumber") UUID unitNumber) {
        log.info("Leave unit {}", unitNumber);
        unitService.leaveGroup(unitNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity getUnits(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "size", defaultValue = "6", required = false) int size) {
        Param param = Param.builder()
                .page(page)
                .size(size)
                .build();
        log.info("Geet units, params: {}", param);

        return ResponseEntity.ok(unitService.getUnits(param));
    }
}
