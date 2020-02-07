package atdd.station;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class StationController {

    @PostMapping("/stations")
    public ResponseEntity createStation() {
        return ResponseEntity.created(URI.create("/station/1")).build();
    }
}