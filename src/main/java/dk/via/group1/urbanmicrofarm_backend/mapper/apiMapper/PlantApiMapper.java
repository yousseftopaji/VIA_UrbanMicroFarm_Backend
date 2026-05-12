package dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper;

import dk.via.group1.urbanmicrofarm_backend.database.entities.PlantEntity;
import dk.via.group1.urbanmicrofarm_backend.dto.PlantResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.CreatePlantRequestDto;

import org.springframework.stereotype.Component;

@Component
public class PlantApiMapper {

    public PlantResponseDto toResponseDto(PlantEntity plantEntity) {
        PlantResponseDto dto = new PlantResponseDto();

        dto.setId(plantEntity.getId());
        dto.setSensorId(plantEntity.getSensorId());
        dto.setName(plantEntity.getName());
        dto.setDescription(plantEntity.getDescription());
        dto.setType(plantEntity.getType());

        if (plantEntity.getDatePlanted() != null) {
            dto.setDatePlanted(
                    plantEntity.getDatePlanted().toString()
            );
        }

        dto.setStatus(plantEntity.getStatus());

        return dto;
    }

    public PlantEntity fromCreateRequestDto(CreatePlantRequestDto dto) {
        PlantEntity plantEntity = new PlantEntity();

        plantEntity.setSensorId(dto.getSensorId());
        plantEntity.setName(dto.getName());
        plantEntity.setDescription(dto.getDescription());
        plantEntity.setType(dto.getType());

        return plantEntity;
    }
}