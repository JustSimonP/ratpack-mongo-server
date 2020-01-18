import java.time.LocalDateTime;

public class PlantEnv {


    public final String airTemperature;
    public final String airHumidity;
    public final String groundHumidity;
    public final String light;
    public final String dateTime;

    public PlantEnv(String airTemperature, String airHumidity, String groundHumidity, String light, String dateTime) {
        this.airTemperature = airTemperature;
        this.airHumidity = airHumidity;
        this.groundHumidity = groundHumidity;
        this.light = light;
        this.dateTime = dateTime;
    }


}
