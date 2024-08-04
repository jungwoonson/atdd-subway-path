package nextstep.subway.unit.path;

import nextstep.subway.line.Section;
import nextstep.subway.path.ShortestPath;
import nextstep.subway.line.exception.NotAddedStationsToSectionException;
import nextstep.subway.line.exception.NotConnectedStationsException;
import nextstep.subway.station.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.unit.line.LineTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {
    private static final List<Section> 열결되지않은구간 = List.of(강남역_양재역, 교대역_홍대역);
    private static final Station 구간에없는역 = Station.of(100L, "구간에없는역");

    @DisplayName("지하철역 조회 함수는, 가장 짧은 지하철 역 목록을 반환한다.")
    @Test
    void getStations() {
        // given
        ShortestPath shortestPath = new ShortestPath(연결된구간);

        // when
        List<Station> actual =  shortestPath.getStations(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(List.of(강남역, 홍대역, 교대역));
    }

    @DisplayName("지하철역 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getStationsNotConnectedStationsExceptionTest() {
        // given
        ShortestPath shortestPath = new ShortestPath(열결되지않은구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getStations(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedStationsException.class);
    }

    @DisplayName("지하철역 조회 함수는, 구간에 등록되지 않은 역이 출발역 또는 도착역인 경우 예외를 발생한다.")
    @ParameterizedTest
    @MethodSource("notAddedStationsToSectionParams")
    void getStationsNotAddedStationsToSectionExceptionTest(Station start, Station end, String expectedMessage) {
        // given
        ShortestPath shortestPath = new ShortestPath(연결된구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getStations(start, end);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToSectionException.class)
                .hasMessageContaining(expectedMessage);
    }

    @DisplayName("거리 조회 함수는, 가장 짧은 거리를 반환한다.")
    @Test
    void getDistance() {
        // given
        ShortestPath shortestPath = new ShortestPath(연결된구간);

        // when
        int actual =  shortestPath.getDistance(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(DISTANCE_6 + DISTANCE_7);
    }

    @DisplayName("거리 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getDistanceNotConnectedStationsExceptionTest() {
        // given
        ShortestPath shortestPath = new ShortestPath(열결되지않은구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getDistance(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedStationsException.class);
    }

    @DisplayName("거리 조회 함수는, 구간에 등록되지 않은 역이 출발역 또는 도착역인 경우 예외를 발생한다.")
    @ParameterizedTest
    @MethodSource("notAddedStationsToSectionParams")
    void getDistanceNotAddedStationsToSectionExceptionTest(Station start, Station end, String expectedMessage) {
        // given
        ShortestPath shortestPath = new ShortestPath(연결된구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getDistance(start, end);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToSectionException.class)
                .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> notAddedStationsToSectionParams() {
        return Stream.of(
                Arguments.of(구간에없는역, 교대역, String.format("출발역(%s)", 구간에없는역.getName())),
                Arguments.of(강남역, 구간에없는역, String.format("도착역(%s)", 구간에없는역.getName()))
        );
    }
}
