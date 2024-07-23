package nextstep.subway.unit;

import nextstep.subway.line.*;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {
    private static final int DEFAULT_DISTANCE = 10;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        Station 강남역 = stationRepository.save(Station.from("강남역"));
        Station 양재역 = stationRepository.save(Station.from("양재역"));
        Station 교대역 = stationRepository.save(Station.from("교대역"));
        Line line = lineRepository.save(신분당선(강남역, 양재역));

        // when
        LineResponse lineResponse = lineService.registerSections(line.getId(), createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }

    private Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("신분당선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .distance(10)
                .build();
    }

    private SectionRequest createSectionRequest(Long upStationId, Long downStationId) {
        return new SectionRequest(upStationId, downStationId, DEFAULT_DISTANCE);
    }

    private static List<StationResponse> createStationResponse(Station ...station) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station s : station) {
            stationResponses.add(new StationResponse(s.getId(), s.getName()));
        }
        return stationResponses;
    }
}
