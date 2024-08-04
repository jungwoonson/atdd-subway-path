package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class PathAcceptanceTestFixture {
    static final String 신분당선 = "신분당선";
    static final String 분당선 = "분당선";
    static final String 경의선 = "경의선";
    static final String 중앙선 = "중앙선";
    static final String RED = "bg-red-600";
    static final String GREEN = "bg-green-600";
    static final String YELLOW = "bg-yellow-600";
    static final String BLUE = "bg-blue-600";
    static final String 분당역 = "분당역";
    static final String 홍대역 = "홍대역";
    static final String 강남역 = "강남역";
    static final String 성수역 = "성수역";
    static final Long 분당역_ID = createStation(분당역);
    static final Long 홍대역_ID = createStation(홍대역);
    static final Long 강남역_ID = createStation(강남역);
    static final Long 성수역_ID = createStation(성수역);
    static final Integer DEFAULT_DISTANCE = 10;

    static final Map<String, Object> 신분당선_PARAM = Map.of(
            "name", 신분당선,
            "color", RED,
            "upStationId", 분당역_ID,
            "downStationId", 홍대역_ID,
            "distance", DEFAULT_DISTANCE
    );

    static final Map<String, Object> 분당선_PARAM = Map.of(
            "name", 분당선,
            "color", GREEN,
            "upStationId", 분당역_ID,
            "downStationId", 강남역_ID,
            "distance", 4
    );

    static final Map<String, Object> 경의선_PARAM = Map.of(
            "name", 경의선,
            "color", YELLOW,
            "upStationId", 강남역_ID,
            "downStationId", 성수역_ID,
            "distance", 1
    );

    static final Map<String, Object> 중앙선_PARAM = Map.of(
            "name", 중앙선,
            "color", BLUE,
            "upStationId", 성수역_ID,
            "downStationId", 홍대역_ID,
            "distance", 8
    );

    static Long createStation(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");
    }
}
