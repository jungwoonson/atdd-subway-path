package nextstep.subway.path.exception;

public class NotAddedStartToSectionException extends NotAddedStationsToSectionException {
    private static final String MESSAGE = "출발역(%s)";

    public NotAddedStartToSectionException(String name) {
        super(String.format(MESSAGE, name));
    }
}
