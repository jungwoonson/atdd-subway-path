package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.acceptance.line.LineAcceptanceTestFixture.*;
import static nextstep.subway.utils.AssertUtil.assertResponseCode;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 역 노선 관련 기능")
@ActiveProfiles("databaseCleanup")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this.getClass());
    }

    /**
     * Given 새로운 지하철 노선 정보를 입력하고
     * When 관리자가 노선을 생성하면
     * Then 해당 노선이 생성되고 노선 목록에 포함된다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        ExtractableResponse<Response> response = createLine(신분당선_PARAM);

        // then
        assertResponseCode(response, HttpStatus.CREATED);
        assertThat(findNames(lookUpLines())).containsExactlyInAnyOrder(신분당선);
    }

    /**
     * Given 여러 개의 지하철 노선이 등록되어 있고,
     * When 관리자가 지하철 노선 목록을 조회하면,
     * Then 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("노선 목록을 조회한다.")
    @Test
    void lookUpLinesTest() {
        // given
        createLine(신분당선_PARAM);
        createLine(분당선_PARAM);

        // when
        ExtractableResponse<Response> response = lookUpLines();

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(findNames(response)).containsExactlyInAnyOrder(신분당선, 분당선);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("노선을 조회한다.")
    @Test
    void lookUpLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = lookUpLine(findId(createdLineResponse));

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(findName(response)).isEqualTo(신분당선);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("노선을 수정한다.")
    @Test
    void modifyLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = modifyLine(findId(createdLineResponse), MODIFY_PARAM);

        // then
        assertResponseCode(response, HttpStatus.OK);
        ExtractableResponse<Response> lookedUpLine = lookUpLine(findId(createdLineResponse));
        assertThat(findName(lookedUpLine)).isEqualTo(분당선);
        assertThat(lookedUpLine.jsonPath().getString("color")).isEqualTo(GREEN);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = deleteLine(findId(createdLineResponse));

        // then
        assertResponseCode(response, HttpStatus.NO_CONTENT);
        assertThat(findNames(lookUpLines())).doesNotContain(신분당선);
    }

    /**
     * Given: 특정 구간이 등록되어 있고,
     * When: 구간을 추가하면,
     * Then: 노선 조회시 구간이 등록되어있다.
     */
    @DisplayName("노선에 새로운 구간을 추가한다.")
    @Test
    void addSectionTest() {
        List<Arguments> fixtures = addSectionFixtures();

        for (Arguments arguments : fixtures){
            // given
            ExtractableResponse<Response> createdLineResponse = createLine((Map<String, Object>) arguments.get()[0]);
            Map<String, Object> sectionParams = (Map<String, Object>) arguments.get()[1];
            List<Long> expectedStationIds = (List<Long>) arguments.get()[2];

            // when
            ExtractableResponse<Response> response = addSection(findId(createdLineResponse), sectionParams);

            // then
            assertResponseCode(response, HttpStatus.CREATED);
            List<Long> stationsIds = lookUpStationIds(findId(createdLineResponse));
            assertThat(stationsIds).isEqualTo(expectedStationIds);
        }
    }

    private List<Arguments> addSectionFixtures() {
        return List.of(
            Arguments.of(신분당선_PARAM, 홍대역_강남역_구간_PARAM, List.of(분당역_ID, 홍대역_ID, 강남역_ID)),
            Arguments.of(분당선_PARAM, 홍대역_분당역_구간_PARAM, List.of(홍대역_ID, 분당역_ID, 강남역_ID)),
            Arguments.of(신분당선_PARAM, 분당역_성수역_구간_PARAM, List.of(분당역_ID, 성수역_ID, 홍대역_ID))
        );
    }

    /**
     * When: 존재하지 않는 노선에 구간을 등록하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("존재하지 않는 노선에 구간을 등록하면 오류가 발생한다.")
    @Test
    void notExistLineExceptionTest() {
        // when
        ExtractableResponse<Response> response = addSection(1L, 홍대역_강남역_구간_PARAM);

        // then
        assertResponseCode(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 존재하지 않는 역이 포함된 구간을 등록하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("존재하지 않는 역이 포함된 구간을 노선에 등록하면 오류가 발생한다.")
    @Test
    void notExistStationExceptionTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = addSection(findId(createdLineResponse), 홍대역_서초역_구간_PARAM);

        // then
        assertResponseCode(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Given: 특정 노선에 구간이 2개 이상 등록되어 있고,
     * When: 지하철 역의 위치에 상관없이 구간을 제거하면,
     * Then: 노선을 조회했을 때 구간이 제거된다.
     */
    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteSectionTest() {
        List<Arguments> fixtures = deleteSectionFixtures();

        for (Arguments fixture : fixtures) {
            // given
            ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);
            addSection(findId(createdLineResponse), 홍대역_강남역_구간_PARAM);
            Long stationId = (Long) fixture.get()[0];
            List<Long> expectedStationIds = (List<Long>) fixture.get()[1];

            // when
            ExtractableResponse<Response> response = deleteSection(findId(createdLineResponse), stationId);

            // then
            assertResponseCode(response, HttpStatus.OK);
            List<Long> stationIds = lookUpStationIds(findId(createdLineResponse));
            assertThat(stationIds).isEqualTo(expectedStationIds);
        }
    }

    private static List<Arguments> deleteSectionFixtures() {
        return List.of(
                Arguments.of(분당역_ID, List.of(홍대역_ID, 강남역_ID)),
                Arguments.of(홍대역_ID, List.of(홍대역_ID, 강남역_ID)),
                Arguments.of(강남역_ID, List.of(분당역_ID, 홍대역_ID))
        );
    }

    /**
     * Given: 특정 노선에 구간이 1개 등록되어 있고,
     * When: 노선의 하행역 구간을 제거하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("지하철 노선에 구간이 1개인 경우 구간을 제거하려하면 오류가 발생한다.")
    @Test
    void deleteSectionOfOnlyOneSectionLineExceptionTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = deleteSection(findId(createdLineResponse), 홍대역_ID);

        // then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> lookUpLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> lookUpLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> modifyLine(Long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> addSection(Long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(String.format("/lines/%d/sections", id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteSection(Long id, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete(String.format("/lines/%s/sections?stationId=%s", id, stationId))
                .then().log().all()
                .extract();
    }

    private List<Long> lookUpStationIds(Long lindId) {
        return lookUpLine(lindId).jsonPath()
                .getList("stations.id", Long.class);
    }

    private List<String> findNames(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("name", String.class);
    }

    private static String findName(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

    private static long findId(ExtractableResponse<Response> createdLineResponse) {
        return createdLineResponse.jsonPath()
                .getLong("id");
    }
}
