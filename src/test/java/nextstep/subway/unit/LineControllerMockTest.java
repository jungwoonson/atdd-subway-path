package nextstep.subway.unit;

import nextstep.subway.line.LineService;
import nextstep.subway.line.SectionRequest;
import nextstep.subway.line.exception.DuplicateStationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static nextstep.subway.unit.LineTestFixture.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LineControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @DisplayName("DuplicateStationException이 발생하면 400 에러를 응답한다.")
    @Test
    void addSectionsTest() throws Exception {
        // given
        Long lineId = 1L;
        when(lineService.addSections(lineId, new SectionRequest())).thenThrow(DuplicateStationException.class);

        // when & then
        mockMvc.perform(post(String.format("/lines/%s/sections", lineId)))
                .andExpect(status().isBadRequest());
    }
}
