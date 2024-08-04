package nextstep.subway.path;

import nextstep.subway.line.PathsResponse;
import nextstep.subway.line.Section;
import nextstep.subway.line.SectionRepository;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import nextstep.subway.station.exception.NotExistStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
    private StationRepository stationRepository;

    private SectionRepository sectionRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathsResponse findShortestPaths(Long source, Long target) {
        Station start = lookUpStationBy(source);
        Station end = lookUpStationBy(target);

        List<Section> sections = sectionRepository.findAll();
        ShortestPath shortestPath = new ShortestPath(sections);

        return new PathsResponse(shortestPath.getDistance(start, end), createStationResponses(shortestPath.getStations(start, end)));
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(PathService::createStationResponse)
                .collect(Collectors.toList());
    }

    private static StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    private Station lookUpStationBy(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotExistStationException::new);
    }
}
