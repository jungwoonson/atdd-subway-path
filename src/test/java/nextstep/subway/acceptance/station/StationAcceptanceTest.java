package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.utils.AssertUtil.assertResponseCode;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 인수 테스트")
@ActiveProfiles("databaseCleanup")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private static final String STATION_NAME_1 = "강남역";
    private static final String STATION_NAME_2 = "역삼역";

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this.getClass());
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성 요청은, 치하철 역 정보를 입력하여 요청하면 지하철역 목록을 조회했을 때 해당 역이 포함된다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStation(STATION_NAME_1);

        // then
        assertResponseCode(response, HttpStatus.CREATED);

        // then
        assertThat(findNames(lookUpStations())).containsAnyOf(STATION_NAME_1);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역 목록 조회 요청은, 지하철 역 조회 요청을 하면 전체 지하철 역이 조회된다.")
    @Test
    void lookUpStation() {
        // given
        createStation(STATION_NAME_1);
        createStation(STATION_NAME_2);

        // when
        ExtractableResponse<Response> response = lookUpStations();

        // then
        assertResponseCode(response, HttpStatus.OK);

        // then
        assertThat(findNames(response).size()).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역 삭제 요청은, 지하철 역 삭제 요청 후 지하철 역 목록을 조회하면 해당역이 제외된다.")
    @Test
    void deleteStation() {
        // given
        Integer id = createStation(STATION_NAME_1).jsonPath()
                .get("id");

        // when
        ExtractableResponse<Response> response = deleteStation(id);

        // then
        assertResponseCode(response, HttpStatus.NO_CONTENT);

        // then
        assertThat(findNames(lookUpStations())).doesNotContain(STATION_NAME_1);
    }

    private static ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> lookUpStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private static List<String> findNames(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("name", String.class);
    }

    private static ExtractableResponse<Response> deleteStation(Integer id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }
}
