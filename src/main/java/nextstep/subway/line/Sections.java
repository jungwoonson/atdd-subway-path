package nextstep.subway.line;

import nextstep.subway.line.exception.LastOneSectionException;
import nextstep.subway.line.exception.NotDownStationException;
import nextstep.subway.station.Station;
import nextstep.subway.station.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.IntStream;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "line", orphanRemoval = true)
    private final List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    private Sections(Section section) {
        sections = new ArrayList<>();
        sections.add(section);
    }

    private Sections(Section ...section) {
        sections = new ArrayList<>(Arrays.asList(section));
    }

    public static Sections of(Line line, Station upStation, Station downStation, Integer distance) {
        return new Sections(createFirstSection(line, upStation, downStation, distance));
    }

    public static Sections of(Section ...section) {
        return new Sections(section);
    }

    private static Section createFirstSection(Line line, Station upStation, Station downStation, Integer distance) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .isFirst(true)
                .build();
    }

    public void add(Section section) {
        if (findLastSection().sameDownStationAndUpStationOf(section)) {
            sections.add(section);
            return;
        }
        addMiddleSection(section);
    }

    private void addMiddleSection(Section section) {
        OptionalInt indexOpt = findSameUpStationIndex(section);
        indexOpt.ifPresent(index -> dividedSection(index, section));
    }

    private OptionalInt findSameUpStationIndex(Section section) {
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i).sameUpStation(section))
                .findFirst();
    }

    private void dividedSection(int index, Section newSection) {
        Section existingSection = sections.get(index);
        Section dividedSection = existingSection.dividedSection(newSection);
        sections.add(index + 1, dividedSection);
    }

    private Section findLastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Long> getStationIds() {
        return extractStations().getStationIds();
    }

    private Stations extractStations() {
        Section firstSection = findFirstSection();
        Stations stations = Stations.of(firstSection.getUpStation(), firstSection.getDownStation());
        for (Section section : sections) {
            appendStations(stations, section);
        }
        return stations;
    }

    private Section findFirstSection() {
        return sections.stream()
                .filter(Section::isFirst)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private void appendStations(Stations stations, Section section) {
        Station lastStation = stations.lastStation();
        if (lastStation.equals(section.getUpStation())) {
            stations.add(section.getDownStation());
        }
    }

    public void delete(Station station) {
        if (hasLastOneSection()) {
            throw new LastOneSectionException();
        }
        Section lastSection = findLastSection();
        if (!lastSection.isDownStation(station)) {
            throw new NotDownStationException();
        }
        sections.remove(lastSection);
    }

    private boolean hasLastOneSection() {
        return sections.size() == 1;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
