package by.modsen.taxiprovider.ridesservice.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Status {

    public final String RIDE_STATUS_WAITING = "WAITING";
    public final String RIDE_STATUS_COMPLETED = "COMPLETED";
    public final String RIDE_STATUS_CANCELLED = "CANCELLED";
    public final String RIDE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public final String RIDE_STATUS_PAID = "PAID";
    public final String DRIVER_STATUS_TAKEN = "TAKEN";
    public final String DRIVER_STATUS_FREE = "FREE";

    public List<String> getRideStatuses() {
        return List.of(RIDE_STATUS_IN_PROGRESS,
                RIDE_STATUS_COMPLETED,
                RIDE_STATUS_CANCELLED,
                RIDE_STATUS_WAITING,
                RIDE_STATUS_PAID);
    }
}
