package nextstep.subway.unit.line.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("구간 단위 테스트")
class LineSectionTest {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();
  private final Station 판교역 = 판교역();

  @DisplayName("구간 앞에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canPrepend() {
    LineSection section = new LineSection(역삼역, 선릉역, 10);
    assertThat(section.canPrepend(new LineSection(강남역, 역삼역, 20))).isTrue();
    assertThat(section.canPrepend(new LineSection(강남역, 판교역, 30))).isFalse();
  }

  @DisplayName("구간 뒤에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canAppend() {
    LineSection section = new LineSection(강남역, 역삼역, 10);
    assertThat(section.canAppend(new LineSection(역삼역, 선릉역, 20))).isTrue();
    assertThat(section.canAppend(new LineSection(강남역, 판교역, 30))).isFalse();
  }

  @DisplayName("노선 상행역을 공통으로 가운데 새로운 구간을 추가할 수 있는지 확인한다.")
  @Test
  void canSplitUp() {
    LineSection section = new LineSection(강남역, 선릉역, 30);
    assertThat(section.canSplitUp(new LineSection(강남역, 역삼역, 10))).isTrue();
    assertThat(section.canSplitUp(new LineSection(역삼역, 선릉역, 10))).isFalse();
    assertThat(section.canSplitUp(new LineSection(강남역, 역삼역, 40))).isFalse();
    assertThat(section.canSplitUp(new LineSection(역삼역, 선릉역, 20))).isFalse();
  }

  @DisplayName("노선 하행역을 공통으로 가운데 새로운 구간을 추가할 수 있는지 확인한다.")
  @Test
  void canSplitDown() {
    LineSection section = new LineSection(강남역, 선릉역, 30);
    assertThat(section.canSplitDown(new LineSection(역삼역, 선릉역, 20))).isTrue();
    assertThat(section.canSplitDown(new LineSection(강남역, 역삼역, 10))).isFalse();
    assertThat(section.canSplitDown(new LineSection(역삼역, 선릉역, 40))).isFalse();
    assertThat(section.canSplitDown(new LineSection(강남역, 역삼역, 10))).isFalse();
  }

  @DisplayName("구간이 같은지 확인한다.")
  @Test
  void isSame() {
    LineSection section = LineSection.of(강남역, 역삼역, 10);
    assertThat(section.isSame(LineSection.of(강남역, 역삼역, 10))).isTrue();
    assertThat(section.isSame(LineSection.of(강남역, 선릉역, 10))).isFalse();
    assertThat(section.isSame(LineSection.of(선릉역, 역삼역, 10))).isFalse();
    assertThat(section.isSame(LineSection.of(강남역, 역삼역, 20))).isFalse();
  }

  @DisplayName("상행역이 같은 경우 구간을 추가하면 구간이 쪼개진다.")
  @Test
  void split_isSameUpStation() {
    LineSection section = LineSection.of(강남역, 선릉역, 30);

    List<LineSection> sections = section.split(LineSection.of(강남역, 역삼역, 10));

    assertThat(sections).hasSize(2);
    assertThat(sections.get(0).isSame(LineSection.of(강남역, 역삼역, 10))).isTrue();
    assertThat(sections.get(1).isSame(LineSection.of(역삼역, 선릉역, 20))).isTrue();
  }

  @DisplayName("하행역이 같은 경우 구간을 추가하면 구간이 쪼개진다.")
  @Test
  void split_isSameDownStation() {
    LineSection section = LineSection.of(강남역, 선릉역, 30);

    List<LineSection> sections = section.split(LineSection.of(역삼역, 선릉역, 10));

    assertThat(sections).hasSize(2);
    assertThat(sections.get(0).isSame(LineSection.of(강남역, 역삼역, 20))).isTrue();
    assertThat(sections.get(1).isSame(LineSection.of(역삼역, 선릉역, 10))).isTrue();
  }
}
