package nextstep.subway.line;

import nextstep.subway.station.StationResponse;

import java.util.List;

public class PathsResponse {
    private List<StationResponse> stations;
    private int distance;

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
