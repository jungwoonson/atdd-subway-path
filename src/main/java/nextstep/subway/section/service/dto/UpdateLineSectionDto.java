package nextstep.subway.section.service.dto;

import nextstep.subway.section.domain.Section;
import nextstep.subway.station.service.StationDto;

import java.util.Objects;

public class UpdateLineSectionDto {

    private StationDto upStation;
    private StationDto downStation;
    private Integer distance;

    private UpdateLineSectionDto() {
    }

    private UpdateLineSectionDto(StationDto upStation, StationDto downStation, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static UpdateLineSectionDto from(Section section) {
        return new UpdateLineSectionDto(
                StationDto.from(section.getUpStation()),
                StationDto.from(section.getDownStation()),
                section.getDistance()
        );
    }

    public StationDto getUpStation() {
        return upStation;
    }

    public StationDto getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateLineSectionDto that = (UpdateLineSectionDto) o;
        return Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

}