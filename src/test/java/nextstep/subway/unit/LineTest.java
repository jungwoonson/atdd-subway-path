package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.station.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @Test
    @DisplayName("구간을 추가한다.")
    void addSection() {
        Station 강남역 = Station.of(1L, "강남역");
        Station 성수역 = Station.of(2L, "성수역");
        Station 건대입구역 = Station.of(3L, "건대입구역");

        Line line = Line.builder()
                .id(1L)
                .name("2호선")
                .color("bg-red-600")
                .upStation(강남역)
                .downStation(성수역)
                .build();

        line.registerSection(성수역, 건대입구역, 10);

        assertThat(line.getStationIds()).containsExactly(강남역.getId(), 성수역.getId(), 건대입구역.getId());
    }

    @Test
    @DisplayName("구간을 조회한다.")
    void getStations() {
    }

    @Test
    @DisplayName("구간을 제거한다.")
    void removeSection() {
    }
}
