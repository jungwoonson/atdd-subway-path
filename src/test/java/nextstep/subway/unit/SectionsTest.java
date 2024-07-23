package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.Sections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    private Line 신분당선;
    private Section 강남역_양재역;
    private Section 양재역_교대역;
    private Section 강남역_홍대역;
    private Section 홍대역_양재역;

    @BeforeEach
    void setUp() {
        신분당선 = 신분당선(강남역, 양재역);
        강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE, true);
        양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE, false);
        강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, 강남역_홍대역_DISTANCE,  true);
        홍대역_양재역 = createSection(신분당선, 홍대역, 양재역, 홍대역_양재역_DISTANCE, false);
    }

    @Test
    @DisplayName("마지막 구간을 추가한다.")
    void addEndSection() {
        // given
        Sections expected = Sections.of(강남역_양재역, 양재역_교대역);

        // when
        신분당선.registerSection(양재역, 교대역, DEFAULT_DISTANCE);

        // then
        assertThat(신분당선.getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("가운데 구간을 추가한다.")
    void addMiddleSection() {
        // given
        Sections sections = Sections.of(강남역_양재역);

        // when
        sections.add(강남역_홍대역);

        // then
        assertThat(sections).isEqualTo(Sections.of(강남역_홍대역, 홍대역_양재역));
    }
}
