package nextstep.subway.line;

import nextstep.subway.line.exception.DuplicateStationException;
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
        section.changeToFirst();
        sections.add(section);
    }

    private Sections(Section... section) {
        sections = new ArrayList<>(Arrays.asList(section));
        sections.get(0).changeToFirst();
    }

    public static Sections of(Line line, Station upStation, Station downStation, Integer distance) {
        return new Sections(createSection(line, upStation, downStation, distance));
    }

    public static Sections of(Section... section) {
        return new Sections(section);
    }

    private static Section createSection(Line line, Station upStation, Station downStation, Integer distance) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    public void add(Section section) {
        if (findEndSection().sameDownStationAndUpStationOf(section)) {
            addEndSection(section);
            return;
        }
        if (findStartSection().sameUpStationAndDownStationOf(section)) {
            addStartSection(section);
            return;
        }
        addMiddleSection(section);
    }

    private void addEndSection(Section section) {
        validateDuplicate(section.getDownStation());

        sections.add(section);
    }

    private void addStartSection(Section section) {
        validateDuplicate(section.getUpStation());

        Section beforeStartSection = findStartSection();
        beforeStartSection.changeToNotFirst();
        section.changeToFirst();
        sections.add(section);
    }

    private void addMiddleSection(Section section) {
        validateDuplicate(section.getDownStation());

        OptionalInt indexOpt = findSameUpStationIndex(section);
        indexOpt.ifPresent(index -> dividedSection(index, section));
    }

    private void validateDuplicate(Station station) {
        Stations stations = getSortedStations();
        if (stations.contains(station)) {
            throw new DuplicateStationException(station.getName());
        }
    }

    private OptionalInt findSameUpStationIndex(Section section) {
        return IntStream.range(0, sections.size())
                .filter(i -> sections.get(i).sameUpStation(section))
                .findFirst();
    }

    private void dividedSection(int index, Section newSection) {
        Section existingSection = sections.get(index);
        Section dividedSection = existingSection.dividedSection(newSection);
        sections.add(dividedSection);
    }

    private Section findNextSection(Section section) {
        return sections.stream()
                .filter(it -> it.sameUpStation(section.getDownStation()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<Section> getSortedSections() {
        List<Section> sortedSections = new ArrayList<>();
        Section currentSection = findStartSection();

        sortedSections.add(currentSection);
        while (sections.size() > sortedSections.size()) {
            currentSection = findNextSection(currentSection);
            sortedSections.add(currentSection);
        }

        return sortedSections;
    }

    public List<Long> getSortedStationIds() {
        return getSortedStations().getStationIds();
    }

    private Stations getSortedStations() {
        Section startSection = findStartSection();
        Stations stations = Stations.of(startSection.getUpStation(), startSection.getDownStation());
        for (Section section : sections) {
            appendStations(stations, section);
        }
        return stations;
    }

    private Section findStartSection() {
        return sections.stream()
                .filter(Section::isFirst)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Section findEndSection() {
        List<Section> sortedSections = getSortedSections();
        return sortedSections.get(sortedSections.size() - 1);
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
        Section lastSection = findEndSection();
        if (!lastSection.isDownStation(station)) {
            throw new NotDownStationException();
        }
        sections.remove(lastSection);
    }

    private boolean hasLastOneSection() {
        return sections.size() == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(getSortedSections(), sections1.getSortedSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSortedSections());
    }
}
