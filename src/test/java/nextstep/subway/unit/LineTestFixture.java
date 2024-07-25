package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.Section;
import nextstep.subway.line.SectionRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineTestFixture {
    public static final String RED = "bg-red-600";
    public static final int DEFAULT_DISTANCE = 10;
    public static final int DISTANCE_4 = 4;
    public static final int DISTANCE_6 = 6;
    public static final int DISTANCE_7 = 7;
    public static final Station 강남역 = Station.from("강남역");
    public static final Station 양재역 = Station.from("양재역");
    public static final Station 교대역 = Station.from("교대역");
    public static final Station 홍대역 = Station.from("홍대역");

    public static Line 신분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("신분당선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DEFAULT_DISTANCE)
                .build();
    }

    public static Line 분당선(Station upStation, Station downStation) {
        return Line.builder()
                .name("분당선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_4)
                .build();
    }

    public static Line 중앙선(Station upStation, Station downStation) {
        return Line.builder()
                .name("중앙선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_6)
                .build();
    }

    public static Line 경의선(Station upStation, Station downStation) {
        return Line.builder()
                .name("경의선")
                .color(RED)
                .upStation(upStation)
                .downStation(downStation)
                .distance(DISTANCE_7)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
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
