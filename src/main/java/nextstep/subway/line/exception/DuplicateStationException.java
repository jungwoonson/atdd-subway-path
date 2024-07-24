package nextstep.subway.line.exception;

public class DuplicateStationException extends IllegalArgumentException {
    private static final String message = "중복된 역이 존재합니다. 중복된 역 이름: %s";

    public DuplicateStationException(String stationName) {
        super(String.format(message, stationName));
    }
}
