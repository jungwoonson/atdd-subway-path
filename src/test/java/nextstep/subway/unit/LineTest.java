package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.Sections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Line 신분당선;
    private Section 강남역_양재역;
    private Section 양재역_교대역;

    @BeforeEach
    void setUp() {
        신분당선 = 신분당선(강남역, 양재역);
        강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void addSection() {
        // given
        Sections expected = Sections.of(강남역_양재역, 양재역_교대역);

        // when
        신분당선.addSection(양재역, 교대역, DEFAULT_DISTANCE);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("구간을 조회한다.")
    void getStations() {
        //given
        Sections expected = Sections.of(강남역_양재역);

        // when
        신분당선.getStationIds();

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("구간을 제거한다.")
    void removeSection() {
        // given
        Sections expected = Sections.of(강남역_양재역);
        신분당선.addSection(양재역, 교대역, DEFAULT_DISTANCE);

        // when
        신분당선.deleteSection(교대역);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }
}
