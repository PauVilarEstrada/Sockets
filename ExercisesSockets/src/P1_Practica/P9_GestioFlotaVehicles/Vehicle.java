package P9_GestioFlotaVehicles;

public class Vehicle {
    private String id;
    private String type;
    private double speed;
    private double latitude;
    private double longitude;
    private String status;

    public Vehicle(String id, String type, double speed, double latitude, double longitude, String status) {
        this.id = id;
        this.type = type;
        this.speed = speed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", speed=" + speed +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", status='" + status + '\'' +
                '}';
    }
}
