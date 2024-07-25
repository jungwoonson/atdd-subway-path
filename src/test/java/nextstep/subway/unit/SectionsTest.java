package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.Sections;
import nextstep.subway.line.exception.DuplicateStationException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    private static Line 신분당선 = 신분당선(강남역, 양재역);

    @DisplayName("처음, 가운데, 마지막 구간을 추가한다.")
    @ParameterizedTest
    @MethodSource("addSectionParameters")
    void addSectionTest(Section section, Section newSection, Sections expected) {
        // given
        Sections sections = Sections.from(section);

        // when
        sections.add(newSection);

        // then
        assertThat(sections).isEqualTo(expected);
    }

    @DisplayName("중복된 구간을 추가하면 오류가 발생한다.")
    @ParameterizedTest
    @MethodSource("duplicateStationParameters")
    void duplicateStationExceptionTest(Sections sections, Section newSection, String expectedDuplicated) {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> sections.add(newSection);

        // then
        assertThatThrownBy(actual).isInstanceOf(DuplicateStationException.class)
                .message().contains(expectedDuplicated);
    }

    private static Stream<Arguments> addSectionParameters() {
        return Stream.of(
                Arguments.of(양재역_교대역(), 강남역_양재역(), Sections.of(강남역_양재역(), 양재역_교대역())),
                Arguments.of(강남역_양재역(), 강남역_홍대역(), Sections.of(강남역_홍대역(), 홍대역_양재역())),
                Arguments.of(양재역_교대역(), 강남역_양재역(), Sections.of(강남역_양재역(), 양재역_교대역()))
        );
    }

    private static Stream<Arguments> duplicateStationParameters() {
        return Stream.of(
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 양재역_강남역(), 양재역.getName()),
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 강남역_양재역(), 양재역.getName()),
                Arguments.of(Sections.of(강남역_양재역(), 양재역_교대역()), 교대역_양재역(), 양재역.getName())
        );
    }

    private static Section 양재역_교대역() {
        return createSection(신분당선, 양재역, 교대역, DEFAULT_DISTANCE);
    }

    private static Section 강남역_양재역() {
        return createSection(신분당선, 강남역, 양재역, DEFAULT_DISTANCE);
    }

    private static Section 강남역_홍대역() {
        return createSection(신분당선, 강남역, 홍대역, 강남역_홍대역_DISTANCE);
    }

    private static Section 홍대역_양재역() {
        return createSection(신분당선, 홍대역, 양재역, 홍대역_양재역_DISTANCE);
    }

    private static Section 양재역_강남역() {
        return createSection(신분당선, 양재역, 강남역, DEFAULT_DISTANCE);
    }

    private static Section 교대역_양재역() {
        return createSection(신분당선, 교대역, 양재역, DEFAULT_DISTANCE);
    }
}
