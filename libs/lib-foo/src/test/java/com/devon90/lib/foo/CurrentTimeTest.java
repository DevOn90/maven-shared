package com.devon90.lib.foo;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrentTimeTest {

    private final CurrentTime currentTime = new CurrentTime();

    @Test 
    void shouldReturnCurrentTime() {
        LocalDateTime result = currentTime.getCurrentTime();

        assertNotNull(result);
    }
}
