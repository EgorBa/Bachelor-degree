package clock;

import java.time.Instant;

public class StableClock implements Clock {

    private Instant now;

    public StableClock(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }

    public void setInstant(Instant now) {
        this.now = now;
    }

}
