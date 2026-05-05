package dk.via.group1.urbanmicrofarm_backend.mlClient;

import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MLPredictionClient {

    private final RestClient restClient;
    private final String predictionUrl;

    public MLPredictionClient(RestClient restClient, @Value("${ml.prediction.url}") String predictionUrl) {
        this.restClient = restClient;
        this.predictionUrl = predictionUrl;
    }

    public WaterPredictionResponseDto predictWater(WaterPredictionRequestDto request) {
        return restClient.post()
                .uri(predictionUrl)
                .body(request)
                .retrieve()
                .body(WaterPredictionResponseDto.class);
    }
}