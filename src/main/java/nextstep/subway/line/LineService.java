package nextstep.subway.line;

import nextstep.subway.line.exception.NotExistLineException;
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
public class LineService {

    private LineRepository lineRepository;

    private StationRepository stationRepository;

    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse lineSave(LineRequest lineRequest) {
        Line line = lineRepository.save(createLine(lineRequest));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse lookUpLine(Long id) {
        return createLineResponse(lookUpLineBy(id));
    }

    @Transactional
    public LineResponse modifyLine(Long id, LineRequest lineRequest) {
        Line line = lookUpLineBy(id);
        line.modify(lineRequest.getName(), lineRequest.getColor());
        return createLineResponse(lineRepository.save(line));
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSections(Long id, SectionRequest sectionRequest) {
        Station upStation = lookUpStationBy(sectionRequest.getUpStationId());
        Station downStation = lookUpStationBy(sectionRequest.getDownStationId());
        Line line = lookUpLineBy(id);
        line.addSection(upStation, downStation, sectionRequest.getDistance());
        return createLineResponse(lineRepository.save(line));
    }

    private Line lookUpLineBy(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NotExistLineException::new);
    }

    @Transactional
    public LineResponse deleteSection(Long lineId, Long stationId) {
        Line line = lookUpLineBy(lineId);
        Station downStation = lookUpStationBy(stationId);
        line.deleteSection(downStation);
        return createLineResponse(lineRepository.save(line));
    }

    public PathsResponse findShortestPaths(Long source, Long target) {
        Station start = lookUpStationBy(source);
        Station end = lookUpStationBy(target);

        List<Section> sections = sectionRepository.findAll();
        ShortestPath shortestPath = new ShortestPath(sections);

        return new PathsResponse(shortestPath.getDistance(start, end), createStationResponses(shortestPath.getStations(start, end)));
    }

    private Line createLine(LineRequest lineRequest) {
        return Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStation(lookUpStationBy(lineRequest.getUpStationId()))
                .downStation(lookUpStationBy(lineRequest.getDownStationId()))
                .distance(lineRequest.getDistance())
                .build();
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(createStationResponsesByIds(line.getStationIds()))
                .build();
    }

    private List<StationResponse> createStationResponsesByIds(List<Long> stationIds) {
        List<Station> stations = stationIds.stream()
                .map(this::lookUpStationBy)
                .collect(Collectors.toList());
        return createStationResponses(stations);
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(LineService::createStationResponse)
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
