package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Station 강남역;
    private Station 성수역;
    private Station 건대입구역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        성수역 = Station.of(2L, "성수역");
        건대입구역 = Station.of(3L, "건대입구역");
        신분당선 = 신분당선(강남역, 성수역);
    }

    private Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .id(1L)
                .name("2호선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .build();
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void addSection() {
        // when
        신분당선.registerSection(성수역, 건대입구역, 10);

        // then
        assertThat(신분당선.getStationIds()).containsExactly(강남역.getId(), 성수역.getId(), 건대입구역.getId());
    }

    @Test
    @DisplayName("구간을 조회한다.")
    void getStations() {
        // when
        신분당선.getStationIds();

        // then
        assertThat(신분당선.getStationIds()).containsExactly(강남역.getId(), 성수역.getId());
    }

    @Test
    @DisplayName("구간을 제거한다.")
    void removeSection() {
        // given
        신분당선.registerSection(성수역, 건대입구역, 10);

        // when
        신분당선.deleteSection(건대입구역);

        // then
        assertThat(신분당선.getStationIds()).containsExactly(강남역.getId(), 성수역.getId());
    }
}
