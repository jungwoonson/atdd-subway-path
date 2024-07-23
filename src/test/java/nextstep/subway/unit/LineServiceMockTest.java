package nextstep.subway.unit;

import nextstep.subway.line.*;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {

    private static final int DEFAULT_DISTANCE = 10;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    @Test
    void addSection() {
        // given
        LineService lineService = new LineService(lineRepository, stationRepository);
        Station 강남역 = Station.of(1L, "강남역");
        Station 양재역 = Station.of(2L, "양재역");
        Station 교대역 = Station.of(3L, "교대역");
        Line 신분당선 = 신분당선(강남역, 양재역);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));
        when(lineRepository.save(신분당선)).thenReturn(신분당선);

        // when
        LineResponse lineResponse = lineService.registerSections(1L, createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }

    private Line 신분당선(Station upStation, Station downStation) {
        Line 신분당선 = Line.builder()
                .id(1L)
                .name("신분당선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .distance(10)
                .build();
        return 신분당선;
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
