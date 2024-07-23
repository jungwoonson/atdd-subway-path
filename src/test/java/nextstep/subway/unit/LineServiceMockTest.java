package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.LineResponse;
import nextstep.subway.line.LineService;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    @Test
    void addSection() {
        // given
        LineService lineService = new LineService(lineRepository, stationRepository);

        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        Line 신분당선 = 신분당선(강남역, 양재역);
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));
        when(lineRepository.save(신분당선)).thenReturn(신분당선);

        // when
        LineResponse lineResponse = lineService.registerSections(1L, createSectionRequest(양재역.getId(), 교대역.getId()));

        // then
        assertThat(lineResponse.getStations()).isEqualTo(createStationResponse(강남역, 양재역, 교대역));
    }
}
