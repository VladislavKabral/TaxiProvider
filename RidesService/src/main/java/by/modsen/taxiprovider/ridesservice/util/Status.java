package by.modsen.taxiprovider.ridesservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Status {

    public final String RIDE_STATUS_WAITING = "WAITING";
    public final String RIDE_STATUS_COMPLETED = "COMPLETED";
    public final String RIDE_STATUS_CANCELLED = "CANCELLED";
    public final String RIDE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public final String RIDE_STATUS_PAID = "PAID";
    public final String DRIVER_STATUS_TAKEN = "TAKEN";
    public final String DRIVER_STATUS_FREE = "FREE";
}
