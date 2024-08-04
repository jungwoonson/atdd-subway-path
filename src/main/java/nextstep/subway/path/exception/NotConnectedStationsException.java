package nextstep.subway.path.exception;

public class NotConnectedStationsException extends IllegalStateException {
    private static final String MESSAGE = "출발역과 도착역이 연결되어있지 않습니다.";

    public NotConnectedStationsException() {
        super(MESSAGE);
    }
}
