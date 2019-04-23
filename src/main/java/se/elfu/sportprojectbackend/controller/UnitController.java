package se.elfu.sportprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.elfu.sportprojectbackend.controller.model.UnitCreationDto;
import se.elfu.sportprojectbackend.controller.parm.Param;
import se.elfu.sportprojectbackend.service.UnitService;

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
    public ResponseEntity createUnit(@RequestBody UnitCreationDto unitCreationDto) {
        log.info("CREATE unit {} ", unitCreationDto);
        return new ResponseEntity(unitService.createUnit(unitCreationDto), HttpStatus.CREATED);
    }

    @GetMapping("{unitNumber}")
    public ResponseEntity getUnit(@PathVariable("unitNumber") UUID unitNumber) {
        log.info("GET unit {}", unitNumber);
        return new ResponseEntity(unitService.getUnit(unitNumber), HttpStatus.CREATED);
    }

    @GetMapping("admin")
    public ResponseEntity getUnitsActiveUserIsAdminOf() {
        log.info("GET Units active user is admin of {}");
        return ResponseEntity.ok(unitService.getUnitsActiveUserIsAdminOf());
    }

    @GetMapping("{unitNumber}/events")
    public ResponseEntity getEventsForUnit(@PathVariable("unitNumber") UUID unitNumber) {
        log.info("GET units {}", unitNumber);
        Param param = Param.builder()
                .size(18)
                .page(0)
                .build();
        return ResponseEntity.ok(unitService.getEventsForUnit(unitNumber, param));
    }

    @GetMapping
    public ResponseEntity getUnits(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "size", defaultValue = "18", required = false) int size) {
        log.info("GET units");
        Param param = Param.builder()
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(unitService.getUnits(param));
    }
}
