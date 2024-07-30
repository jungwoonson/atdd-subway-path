package nextstep.subway.unit;

import nextstep.subway.line.LineService;
import nextstep.subway.line.SectionRequest;
import nextstep.subway.line.exception.DuplicateStationException;
import nextstep.subway.line.exception.LastOneSectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LineControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @DisplayName("구간 추가 중 DuplicateStationException이 발생하면 400 에러를 응답한다.")
    @Test
    void addSectionsExceptionTest() throws Exception {
        // given
        Long lineId = 1L;
        when(lineService.addSections(lineId, new SectionRequest())).thenThrow(DuplicateStationException.class);

        // when & then
        mockMvc.perform(post(String.format("/lines/%s/sections", lineId)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("구간 제거 중 LastOneSectionException이 발생하면 400 에러를 응답한다.")
    @Test
    void deleteSectionsExceptionTest() throws Exception {
        // given
        Long lineId = 1L;
        Long stationId = 2L;
        when(lineService.deleteSection(lineId, stationId)).thenThrow(LastOneSectionException.class);

        // when & then
        mockMvc.perform(post(String.format("/lines/%s/sections", lineId)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("출발역과 도착역이 같을 경우 400 에러를 응답한다.")
    @Test
    void sameSourceAndTargetException() throws Exception {
        // given
        String stationId = "1";

        // when & then
        mockMvc.perform(get("/paths")
                        .param("source", stationId)
                        .param("target", stationId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("출발역과 도착역은 달라야합니다."));
    }
}
