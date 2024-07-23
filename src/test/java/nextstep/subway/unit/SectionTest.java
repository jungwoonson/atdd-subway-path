package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {

    private Line 신분당선;

    @BeforeEach
    void setUp() {
        // given
        신분당선 = 신분당선(강남역, 양재역);
    }

    @Test
    @DisplayName("주어진 구간의 하행 역과 현재 구간의 하행 역이 연결된 새로운 구간을 생성한다.")
    void dividedSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE, true);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, 강남역_홍대역_DISTANCE, false);
        Section 홍대역_양재역 = createSection(신분당선, 홍대역, 양재역, 홍대역_양재역_DISTANCE, false);

        // when
        Section actual = 강남역_양재역.dividedSection(강남역_홍대역);

        // then
        assertThat(actual).isEqualTo(홍대역_양재역);
    }

    @Test
    @DisplayName("두 구간의 상행역이 같은지 확인한다.")
    void sameUpStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE, true);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE, false);

        // when & then
        assertThat(강남역_양재역.sameUpStation(강남역_홍대역)).isTrue();
    }

    @Test
    @DisplayName("현재 구간의 하행역과 주어진 구간의 상행역이 같은지 확인한다.")
    void isSameUpStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE, true);
        Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE, false);

        // when & then
        assertThat(강남역_양재역.sameDownStationAndUpStationOf(양재역_교대역)).isTrue();
    }
}
