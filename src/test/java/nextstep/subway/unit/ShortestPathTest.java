package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.ShortestPath;
import nextstep.subway.station.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.unit.LineTestFixture.*;
import static nextstep.subway.unit.LineTestFixture.createSection;
import static org.assertj.core.api.Assertions.assertThat;

public class ShortestPathTest {

    private Line 신분당선 = 신분당선(강남역, 양재역);
    private Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
    private Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DISTANCE_4);
    private Section 교대역_홍대역 = createSection(신분당선, 교대역, 홍대역, DISTANCE_6);
    private Section 홍대역_강남역 = createSection(신분당선, 홍대역, 강남역, DISTANCE_7);
    List<Section> sections = List.of(강남역_양재역, 양재역_교대역, 교대역_홍대역, 홍대역_강남역);

    @DisplayName("지하철역 목록 조회는, 가장 짧은 지하철 역 목록을 반환한다.")
    @Test
    void getStations() {
        // given
        ShortestPath shortestPath = new ShortestPath(sections);

        // when
        List<Station> actual =  shortestPath.getStations(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(List.of(강남역, 홍대역, 교대역));
    }

    @DisplayName("거리 조회 함수는, 가장 짧은 거리를 반환한다.")
    @Test
    void getDistance() {
        // given
        ShortestPath shortestPath = new ShortestPath(sections);

        // when
        int actual =  shortestPath.getDistance(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(DISTANCE_6 + DISTANCE_7);
    }
}
