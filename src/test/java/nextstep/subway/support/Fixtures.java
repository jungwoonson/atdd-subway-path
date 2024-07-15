package nextstep.subway.support;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Line.LineBuilder;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;

public class Fixtures {
  private Fixtures() {}

  public static Station 강남역() {
    return Station.builder().id(1L).name("강남역").build();
  }

  public static Station 역삼역() {
    return Station.builder().id(2L).name("역삼역").build();
  }

  public static Station 선릉역() {
    return Station.builder().id(3L).name("선릉역").build();
  }

  public static Station 판교역() {
    return Station.builder().id(4L).name("판교역").build();
  }

  public static LineBuilder aLine() {
    return Line.builder().id(1L).name("2호선").color("bg-green-600");
  }

  public static Line 이호선() {
    return Line.builder()
        .id(1L)
        .name("2호선")
        .color("bg-green-600")
        .lineSections(new LineSections(강남_역삼_구간()))
        .build();
  }

  public static Line 신분당선() {
    return Line.builder()
        .id(2L)
        .name("신분당선")
        .color("bg-red-600")
        .lineSections(new LineSections(강남_판교_구간()))
        .build();
  }

  public static LineSection 강남_역삼_구간() {
    return LineSection.builder().id(1L).upStation(강남역()).downStation(역삼역()).distance(10).build();
  }

  public static LineSection 역삼_선릉_구간() {
    return LineSection.builder().id(2L).upStation(역삼역()).downStation(선릉역()).distance(20).build();
  }

  public static LineSection 강남_판교_구간() {
    return LineSection.builder().id(3L).upStation(강남역()).downStation(판교역()).distance(20).build();
  }
}
