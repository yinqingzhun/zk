package qs.config.quartz;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class FixdedDelayJobData implements Serializable {

    private static final long serialVersionUID = 1L;

    private long delay;
    private TimeUnit delayUnit;

    public FixdedDelayJobData(long delay, final TimeUnit delayUnit) {
        if (delay == 0) {
            throw new IllegalArgumentException("Delay cannot be zero");
        }
        if (delayUnit == null) {
            throw new IllegalArgumentException("Delay Unit should be provided");
        }
        this.delay = delay;
        this.delayUnit = delayUnit;
    }

    public Date getNextScheduleDate() {
        return new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delay, delayUnit));
    }

    @Override
    public String toString() {
        return "FixdedDelayJobData [delay=" + delay + ", delayUnit=" + delayUnit + "]";
    }
}