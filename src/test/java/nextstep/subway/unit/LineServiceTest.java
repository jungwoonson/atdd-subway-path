package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.LineResponse;
import nextstep.subway.line.LineService;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테스트DB 사용한 지하철 노선 서비스 테스트")
@SpringBootTest
@Transactional
public class LineServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @DisplayName("구간을 추가 함수는, 특정 노선에 구간을 추가하면 해당 구간이 추가된 노선 정보가 반환된다.")
    @Test
    void addSection() {
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
}
