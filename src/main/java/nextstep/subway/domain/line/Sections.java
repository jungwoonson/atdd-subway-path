package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;
import nextstep.subway.exception.SectionDeleteMinSizeException;
import nextstep.subway.exception.SectionExistException;
import nextstep.subway.exception.SectionNotExistException;
import nextstep.subway.exception.SectionNotFoundException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Embeddable
public class Sections {

    private static final int SECTIONS_MIN_SIZE = 1;

    @OneToMany(fetch = LAZY, mappedBy = "line", cascade = ALL, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, Station upStation, Station downStation, int distance) {
        values.add(new Section(line, upStation, downStation, distance));
    }

    public void add(Section addSection) {
        if (isExistedAllStation(addSection)) {
            throw new SectionExistException();
        }
        if (isNotExistedAllStation(addSection)) {
            throw new SectionNotExistException();
        }
        modifyExistedSection(addSection);
        values.add(addSection);
    }

    public void remove(Section section) {
        if (values.size() == SECTIONS_MIN_SIZE) {
            throw new SectionDeleteMinSizeException();
        }
        values.remove(section);
    }

    public List<Station> getStations(Station station) {
        List<Station> stations = new ArrayList<>(Arrays.asList(station));
        while (hasNext(station)) {
            Section section = next(station);
            stations.add(section.getDownStation());
            station = section.getDownStation();
        }
        return stations;
    }

    public Section findSectionByUpStation(Station station) {
        return values.stream()
                .filter(section -> section.isUp(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);
    }

    public Section findSectionByDownStation(Station station) {
        return values.stream()
                .filter(section -> section.isDown(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);
    }

    private boolean hasNext(Station station) {
        return values.stream().anyMatch(section -> section.isUp(station));
    }

    private Section next(Station station) {
        return values.stream()
                .filter(section -> section.isUp(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);
    }

    private boolean isNotExistedAllStation(Section addSection) {
        return !isExistedUpStation(addSection) && !isExistedDownStation(addSection);
    }

    private boolean isExistedAllStation(Section addSection) {
        return isExistedUpStation(addSection) && isExistedDownStation(addSection);
    }

    private boolean isExistedDownStation(Section addSection) {
        return values.stream().anyMatch(section -> section.isExistedStation(addSection.getDownStation()));
    }

    private boolean isExistedUpStation(Section addSection) {
        return values.stream().anyMatch(section -> section.isExistedStation(addSection.getUpStation()));
    }

    private boolean isExistedUpStationSameSection(Section addSection) {
        return values.stream().anyMatch(section -> section.isUp(addSection.getUpStation()));
    }

    private boolean isExistedDownStationSameSection(Section addSection) {
        return values.stream().anyMatch(section -> section.isDown(addSection.getDownStation()));
    }

    private void modifyExistedSection(Section addSection) {
        if (isExistedUpStationSameSection(addSection)) {
            modifyUpStationSameSection(addSection);
        }
        if (isExistedDownStationSameSection(addSection)) {
            modifyDownStationSameSection(addSection);
        }
    }

    private void modifyUpStationSameSection(Section addSection) {
        Section section = findSectionByUpStation(addSection.getUpStation());
        section.modifyDistance(addSection);
        section.modifyUpStation(addSection.getDownStation());
    }

    private void modifyDownStationSameSection(Section addSection) {
        Section section = findSectionByDownStation(addSection.getDownStation());
        section.modifyDistance(addSection);
        section.modifyDownStation(addSection.getUpStation());
    }

}