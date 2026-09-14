package com.devon90.lib.foo;

import java.time.LocalDateTime;

/**
 * Provides access to the current local date and time.
 * 
 * This class is a plain Java component and does not require a
 * Spring application context.
 */
public class CurrentTime {

    /**
     * Returns the current date and time using the system's default time zone.
     * @return the current local date and time
     */
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
