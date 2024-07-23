package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.SectionRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineTestFixture {
    public static final int DEFAULT_DISTANCE = 10;
    public static final int 강남역_홍대역_DISTANCE = 4;
    public static final int 홍대역_양재역_DISTANCE = 6;
    public static final Station 강남역 = Station.of(1L, "강남역");
    public static final Station 양재역 = Station.of(2L, "양재역");
    public static final Station 교대역 = Station.of(3L, "교대역");
    public static final Station 홍대역 = Station.of(4L, "홍대역");

    public static Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .id(1L)
                .name("2호선")
                .color("bg-red-600")
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance, boolean isFirst) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .isFirst(isFirst)
                .build();
    }

    public static SectionRequest createSectionRequest(Long upStationId, Long downStationId) {
        return new SectionRequest(upStationId, downStationId, DEFAULT_DISTANCE);
    }

    public static List<StationResponse> createStationResponse(Station ...station) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station s : station) {
            stationResponses.add(new StationResponse(s.getId(), s.getName()));
        }
        return stationResponses;
    }
}
