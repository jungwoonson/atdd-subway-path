package nextstep.subway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import nextstep.subway.line.exception.*;
import nextstep.subway.station.exception.NotExistStationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotExistStationException.class, NotExistLineException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({
            NotDownStationException.class,
            LastOneSectionException.class,
            DuplicateStationException.class
    })
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}