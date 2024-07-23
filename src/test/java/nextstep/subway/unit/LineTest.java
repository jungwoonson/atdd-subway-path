package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.Sections;
import nextstep.subway.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private static final int DEFAULT_DISTANCE = 10;
    private Station 강남역;
    private Station 성수역;
    private Station 건대입구역;
    private Line 신분당선;
    private Section 강남역_성수역;
    private Section 성수역_건대입구역;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        성수역 = Station.of(2L, "성수역");
        건대입구역 = Station.of(3L, "건대입구역");
        신분당선 = 신분당선(강남역, 성수역);
        강남역_성수역 = createSection(신분당선, 강남역, 성수역, true);
        성수역_건대입구역 = createSection(신분당선, 성수역, 건대입구역, false);
    }

    private Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .id(1L)
                .name("2호선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .build();
    }

    private Section createSection(Line line, Station upStation, Station downStation, boolean isFirst) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .isFirst(isFirst)
                .build();
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void addSection() {
        // given
        Sections expected = Sections.of(강남역_성수역, 성수역_건대입구역);

        // when
        신분당선.registerSection(성수역, 건대입구역, DEFAULT_DISTANCE);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("구간을 조회한다.")
    void getStations() {
        //given
        Sections expected = Sections.of(강남역_성수역);

        // when
        신분당선.getStationIds();

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("구간을 제거한다.")
    void removeSection() {
        // given
        Sections expected = Sections.of(강남역_성수역);
        신분당선.registerSection(성수역, 건대입구역, DEFAULT_DISTANCE);

        // when
        신분당선.deleteSection(건대입구역);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }
}
