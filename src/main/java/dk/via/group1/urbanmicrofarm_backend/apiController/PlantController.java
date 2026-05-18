package dk.via.group1.urbanmicrofarm_backend.apiController;

import dk.via.group1.urbanmicrofarm_backend.application.services.plant_service.PlantService;
import dk.via.group1.urbanmicrofarm_backend.database.entities.PlantEntity;
import dk.via.group1.urbanmicrofarm_backend.dto.CreatePlantRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.PlantResponseDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper.PlantApiMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlantController {

    private final PlantService plantService;
    private final PlantApiMapper plantApiMapper;

    public PlantController(
            PlantService plantService,
            PlantApiMapper plantApiMapper
    ) {
        this.plantService = plantService;
        this.plantApiMapper = plantApiMapper;
    }

    @GetMapping("/growingsetups/{setupId}/plants")
    public List<PlantResponseDto> getPlantsBySetup(
            @PathVariable Long setupId
    ) {

        return plantService.getPlantsBySetup(setupId)
                .stream()
                .map(plantApiMapper::toResponseDto)
                .toList();
    }

    @GetMapping("/plants/{plantId}")
    public PlantResponseDto getPlant(
            @PathVariable Long plantId
    ) {

        PlantEntity plantEntity = plantService.getPlant(plantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Plant not found"
                ));

        return plantApiMapper.toResponseDto(plantEntity);
    }

    @GetMapping("/sensors/{sensorId}/plant")
    public PlantResponseDto getPlantBySensor(
            @PathVariable Long sensorId
    ) {

        PlantEntity plantEntity = plantService.getPlantBySensor(sensorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Plant not found"
                ));

        return plantApiMapper.toResponseDto(plantEntity);
    }

    @PostMapping("/plants")
    @ResponseStatus(HttpStatus.CREATED)
    public PlantResponseDto addPlant(
            @RequestBody CreatePlantRequestDto dto
    ) {

        PlantEntity plantEntity =
                plantApiMapper.fromCreateRequestDto(dto);

        return plantApiMapper.toResponseDto(
                plantService.addPlant(plantEntity)
        );
    }

    @PatchMapping("/plants/{plantId}")
    public PlantResponseDto updatePlant(
            @PathVariable Long plantId,
            @RequestBody CreatePlantRequestDto dto
    ) {

        PlantEntity updatedPlant =
                plantApiMapper.fromCreateRequestDto(dto);

        return plantApiMapper.toResponseDto(
                plantService.updatePlant(plantId, updatedPlant)
        );
    }

    @DeleteMapping("/plants/{plantId}")
    public String removePlant(
            @PathVariable Long plantId
    ) {

        plantService.removePlant(plantId);

        return "Plant removed successfully";
    }
}