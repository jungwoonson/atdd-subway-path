package nextstep.subway.line.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.CannotAddLineSectionException;
import nextstep.subway.line.exception.LineSectionAlreadyExistsException;
import nextstep.subway.line.exception.RemoveLastLineSectionException;
import nextstep.subway.line.exception.RemoveNonTerminalStationException;
import nextstep.subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineSections {
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "line_id")
  private final List<LineSection> sections = new ArrayList<>();

  @Builder
  public LineSections(List<LineSection> lineSections) {
    this.sections.addAll(lineSections);
  }

  public LineSections(LineSection... lineSections) {
    this.sections.addAll(Arrays.asList(lineSections));
  }

  public LineSections(Station upStation, Station downStation, int distance) {
    this(LineSection.of(upStation, downStation, distance));
  }

  public static LineSections of(Station upStation, Station downStation, int distance) {
    return new LineSections(upStation, downStation, distance);
  }

  public int size() {
    return sections.size();
  }

  public boolean isEmpty() {
    return sections.isEmpty();
  }

  public LineSection getFirst() {
    return sections.get(0);
  }

  public LineSection getLast() {
    return sections.get(sections.size() - 1);
  }

  public void add(LineSection lineSection) {
    validateAdd(lineSection);
    if (isEmpty()) {
      sections.add(lineSection);
      return;
    }
    if (isPrepend(lineSection)) {
      sections.add(0, lineSection);
      return;
    }
    if (isAppend(lineSection)) {
      sections.add(lineSection);
      return;
    }
    if (insertUp(lineSection)) {
      return;
    }
    if (insertDown(lineSection)) {
      return;
    }
    throw new CannotAddLineSectionException();
  }

  private boolean insertUp(LineSection lineSection) {
    OptionalInt optionalIndex = indexOfSplitUp(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private boolean insertDown(LineSection lineSection) {
    OptionalInt optionalIndex = indexOfSplitDown(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private void insert(LineSection lineSection, int index) {
    LineSection splitTargetSection = sections.remove(index);
    List<LineSection> splitSections = splitTargetSection.split(lineSection);
    sections.addAll(index, splitSections);
  }

  private OptionalInt indexOfSplitUp(LineSection lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitUp(lineSection))
        .findFirst();
  }

  private OptionalInt indexOfSplitDown(LineSection lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitDown(lineSection))
        .findFirst();
  }

  private boolean isPrepend(LineSection lineSection) {
    return getFirst().canPrepend(lineSection);
  }

  private boolean isAppend(LineSection lineSection) {
    return getLast().canAppend(lineSection);
  }

  private void validateAdd(LineSection lineSection) {
    if (containsBothStations(lineSection)) {
      throw new LineSectionAlreadyExistsException();
    }
  }

  private boolean containsBothStations(LineSection lineSection) {
    List<Station> stations = getStations();
    return stations.stream().anyMatch(it -> it.isSame(lineSection.getUpStation()))
        && stations.stream().anyMatch(it -> it.isSame(lineSection.getDownStation()));
  }

  public void addAll(LineSections lineSections) {
    lineSections.sections.forEach(this::add);
  }

  public List<Station> getStations() {
    if (sections.isEmpty()) {
      return Collections.emptyList();
    }
    List<Station> stations =
        sections.stream().map(LineSection::getUpStation).collect(Collectors.toList());
    stations.add(getLast().getDownStation());
    return Collections.unmodifiableList(stations);
  }

  public void removeLast(Station station) {
    validateRemove(station);
    sections.remove(sections.size() - 1);
  }

  private void validateRemove(Station station) {
    if (!isTerminalStation(station)) {
      throw new RemoveNonTerminalStationException();
    }
    if (size() <= 1) {
      throw new RemoveLastLineSectionException();
    }
  }

  private boolean isTerminalStation(Station station) {
    return getLast().getDownStation().isSame(station);
  }
}
