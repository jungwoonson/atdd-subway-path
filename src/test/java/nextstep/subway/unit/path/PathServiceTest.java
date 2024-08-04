package nextstep.subway.unit.path;

import nextstep.subway.line.LineRepository;
import nextstep.subway.path.PathService;
import nextstep.subway.line.PathsResponse;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.unit.UnitTestFixture;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.unit.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테스트DB 사용한 지하철 경로 서비스 테스트")
@SpringBootTest
@Transactional
@ActiveProfiles("databaseCleanup")
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private PathService pathService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute(getClass());
    }

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
        // given
        Station 강남역 = stationRepository.save(UnitTestFixture.강남역);
        Station 양재역 = stationRepository.save(UnitTestFixture.양재역);
        Station 교대역 = stationRepository.save(UnitTestFixture.교대역);
        Station 홍대역 = stationRepository.save(UnitTestFixture.홍대역);

        lineRepository.save(신분당선(강남역, 양재역));
        lineRepository.save(분당선(양재역, 교대역));
        lineRepository.save(중앙선(교대역, 홍대역));
        lineRepository.save(경의선(홍대역, 강남역));

        // when
        PathsResponse pathsResponse = pathService.findShortestPaths(강남역.getId(), 교대역.getId());

        // then
        assertThat(pathsResponse.getStations()).isEqualTo(createStationResponse(강남역, 홍대역, 교대역));
        assertThat(pathsResponse.getDistance()).isEqualTo(13);
    }
}
