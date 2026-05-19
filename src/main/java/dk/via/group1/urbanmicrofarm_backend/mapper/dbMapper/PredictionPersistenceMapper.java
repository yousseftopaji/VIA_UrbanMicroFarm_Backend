package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.Prediction;
import dk.via.group1.urbanmicrofarm_backend.database.entities.PredictionEntity;
import org.springframework.stereotype.Component;

@Component
public class PredictionPersistenceMapper {

    public Prediction toDomain(PredictionEntity entity) {

        if (entity == null) {
            return null;
        }

        Prediction prediction = new Prediction();

        prediction.setPredictionId(entity.getPredictionId());
        prediction.setPredictedValue(entity.getPredictedValue());
        prediction.setCreatedAt(entity.getCreatedAt());
        prediction.setPlantName(entity.getPlantName());

        return prediction;
    }

    public PredictionEntity toEntity(Prediction prediction) {

        if (prediction == null) {
            return null;
        }

        PredictionEntity entity = new PredictionEntity();

        entity.setPredictionId(prediction.getPredictionId());
        entity.setPredictedValue(prediction.getPredictedValue());
        entity.setCreatedAt(prediction.getCreatedAt());
        entity.setPlantName(prediction.getPlantName());

        return entity;
    }
}