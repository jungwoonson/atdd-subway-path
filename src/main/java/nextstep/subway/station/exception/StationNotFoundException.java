package nextstep.subway.station.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class StationNotFoundException extends ApiException {
  public StationNotFoundException(Long id) {
    super(ErrorCode.NOT_FOUND, "역 #" + id + "이 존재하지 않습니다.");
  }
}
