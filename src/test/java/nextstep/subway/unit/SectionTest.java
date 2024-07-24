package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.exception.NotLessThanExistingDistanceException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, 강남역_홍대역_DISTANCE);
        Section 홍대역_양재역 = createSection(신분당선, 홍대역, 양재역, 홍대역_양재역_DISTANCE);

        // when
        Section actual = 강남역_양재역.dividedSection(강남역_홍대역);

        // then
        assertThat(actual).isEqualTo(홍대역_양재역);
    }

    @Test
    @DisplayName("현재 구간의 상행 역과 주어진 구간의 하행역이 연결된다.")
    void mergeSectionTest() {
        // given
        Section frontSection = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section backSection = createSection(신분당선, 양재역, 홍대역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE + DEFAULT_DISTANCE);

        // when
        frontSection.mergeSection(backSection);

        // then
        assertThat(frontSection).isEqualTo(강남역_홍대역);
    }

    @Test
    @DisplayName("구간 분리 시 새로운 구간의 거리가 기존 구간의 거리보다 크거나 같으면 예외를 발생시킨다.")
    void GraterOrEqualExistingDistanceExceptionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE);

        // when
        ThrowingCallable actual = () -> 강남역_양재역.dividedSection(강남역_홍대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotLessThanExistingDistanceException.class);
    }

    @Test
    @DisplayName("두 구간의 상행 역이 같은지 확인한다.")
    void sameUpStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 강남역_홍대역 = createSection(신분당선, 강남역, 홍대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameUpStation(강남역_홍대역)).isTrue();
    }

    @Test
    @DisplayName("주어진 역과 상행 역이 같은지 확인한다.")
    void sameUpStationByStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameUpStation(강남역)).isTrue();
    }

    @Test
    @DisplayName("주어진 역과 하행 역이 같은지 확인한다.")
    void sameDownStationByStationTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameDownStation(양재역)).isTrue();
    }

    @Test
    @DisplayName("현재 구간의 하행역과 주어진 구간의 상행역이 같은지 확인한다.")
    void sameDownStationAndUpStationOfNewSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(강남역_양재역.sameDownStationAndUpStationOf(양재역_교대역)).isTrue();
    }

    @Test
    @DisplayName("현재 구간의 상행역과 주어진 구간의 하행역이 같은지 확인한다.")
    void sameUpStationAndDownStationOfNewSectionTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
        Section 양재역_교대역 = createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);

        // when & then
        assertThat(양재역_교대역.sameUpStationAndDownStationOf(강남역_양재역)).isTrue();
    }

    @Test
    @DisplayName("구간의 첫 번째를 판단하는 상태 값을 true로 바꾼다.")
    void changeToFirstTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when
        강남역_양재역.changeToFirst();

        // then
        assertThat(강남역_양재역.isFirst()).isTrue();
    }

    @Test
    @DisplayName("구간의 첫 번째를 판단하는 상태 값을 false로 바꾼다.")
    void changeToNotFirstTest() {
        // given
        Section 강남역_양재역 = createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);

        // when
        강남역_양재역.changeToNotFirst();

        // then
        assertThat(강남역_양재역.isFirst()).isFalse();
    }
}
