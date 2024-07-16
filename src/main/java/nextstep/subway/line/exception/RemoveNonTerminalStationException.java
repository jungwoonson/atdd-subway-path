package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class RemoveNonTerminalStationException extends ApiException {
  public RemoveNonTerminalStationException() {
    super(ErrorCode.REMOVE_NON_TERMINAL_STATION);
  }
}
