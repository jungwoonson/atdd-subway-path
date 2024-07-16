package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class RemoveLastLineSectionException extends ApiException {
  public RemoveLastLineSectionException() {
    super(ErrorCode.REMOVE_LAST_LINE_SECTION);
  }
}
