package nextstep.subway.line;

import nextstep.subway.line.exception.NotLessThanExistingDistanceException;
import nextstep.subway.station.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineId", referencedColumnName = "id")
    private Line line;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upStationId", referencedColumnName = "id")
    private Station upStation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downStationId", referencedColumnName = "id")
    private Station downStation;
    @Column(nullable = false)
    private Integer distance;
    @Column(nullable = false)
    private boolean isFirst;

    public Section() {
    }

    private Section(Builder builder) {
        this.line = builder.line;
        this.upStation = builder.upStation;
        this.downStation = builder.downStation;
        this.distance = builder.distance;
        this.isFirst = builder.isFirst;
    }

    public Section dividedSection(Section section) {
        Section dividedSection = createDividedSection(section);
        downStation = section.downStation;
        distance = section.getDistance();
        return dividedSection;
    }

    private Section createDividedSection(Section section) {
        if (distance <= section.distance) {
            throw new NotLessThanExistingDistanceException();
        }
        return new Builder()
                .line(line)
                .upStation(section.downStation)
                .downStation(downStation)
                .distance(distance - section.distance)
                .isFirst(false)
                .build();
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }

    public boolean sameUpStation(Section station) {
        return upStation.equals(station.upStation);
    }

    public boolean sameDownStationAndUpStationOf(Section station) {
        return downStation.equals(station.upStation);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return Station.from(upStation);
    }

    public Station getDownStation() {
        return Station.from(downStation);
    }

    public Integer getDistance() {
        return distance;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private Integer distance;
        private boolean isFirst;

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public Builder isFirst(boolean isFirst) {
            this.isFirst = isFirst;
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return isFirst == section.isFirst
                && Objects.equals(line.getId(), section.line.getId())
                && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line.getId(), upStation, downStation, distance, isFirst);
    }
}
