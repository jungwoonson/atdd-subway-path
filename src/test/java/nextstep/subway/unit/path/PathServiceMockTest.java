package nextstep.subway.unit.path;

import nextstep.subway.line.*;
import nextstep.subway.path.PathService;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.unit.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Mock을 활용한 지하철 경로 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationRepository stationRepository;

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
        // given
        PathService pathService = new PathService(sectionRepository, stationRepository);
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        when(sectionRepository.findAll()).thenReturn(연결된구간);

        // when
        PathsResponse pathsResponse = pathService.findShortestPaths(강남역.getId(), 교대역.getId());

        // then
        assertThat(pathsResponse.getStations()).isEqualTo(createStationResponse(강남역, 홍대역, 교대역));
        assertThat(pathsResponse.getDistance()).isEqualTo(13);
    }
}
