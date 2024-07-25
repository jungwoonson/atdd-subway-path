package nextstep.subway.unit;

import nextstep.subway.line.*;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSectionTest() {
        // given
        Station 강남역 = stationRepository.save(LineTestFixture.강남역);
        Station 양재역 = stationRepository.save(LineTestFixture.양재역);
        Station 교대역 = stationRepository.save(LineTestFixture.교대역);
        Line line = lineRepository.save(신분당선(강남역, 양재역));

        // when
        LineResponse lineResponse = lineService.addSections(line.getId(), createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }

    @Test
    void findShortestPathsTest() {
        // given
        Station 강남역 = stationRepository.save(LineTestFixture.강남역);
        Station 양재역 = stationRepository.save(LineTestFixture.양재역);
        Station 교대역 = stationRepository.save(LineTestFixture.교대역);
        Station 홍대역 = stationRepository.save(LineTestFixture.홍대역);

        Line 신분당선 = lineRepository.save(신분당선(강남역, 양재역));
        Line 분당선 = lineRepository.save(분당선(양재역, 교대역));
        Line 중앙선 = lineRepository.save(중앙선(교대역, 홍대역));
        Line 경의선 = lineRepository.save(경의선(홍대역, 강남역));

        // when
        PathsResponse pathsResponse = lineService.findShortestPaths(강남역.getId(), 교대역.getId());

        // then
        assertThat(pathsResponse.getStations()).isEqualTo(createStationResponse(강남역, 홍대역, 교대역));
        assertThat(pathsResponse.getDistance()).isEqualTo(13);
    }
}
