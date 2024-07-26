package nextstep.subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nextstep.subway.line.exception.NotExistLineException;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import nextstep.subway.station.exception.NotExistStationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
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
                .stations(createStationResponses(line.getStationIds()))
                .build();
    }

    private List<StationResponse> createStationResponses(List<Long> stationIds) {
        return stationIds.stream()
                .map(this::createStation)
                .collect(Collectors.toList());
    }

    private StationResponse createStation(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(NotExistStationException::new);
        return new StationResponse(stationId, station.getName());
    }

    private Station lookUpStationBy(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotExistStationException::new);
    }

    public PathsResponse findShortestPaths(Long source, Long target) {
        return null;
    }
}
