package optimal_route.contract;
import java.util.ArrayList;
import java.util.List;

public class Busline {
    private int id;
    private String buslineName;
    private List<StationNode> stations;

    public Busline(int id, String buslineName, List<StationNode> stations) {
        this.id = id;
        this.buslineName = buslineName;
        this.stations = stations;
    }

    public Busline(int id, String buslineName) {
        this.id = id;
        this.buslineName = buslineName;
        stations=new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuslineName() {
        return buslineName;
    }

    public void setBuslineName(String buslineName) {
        this.buslineName = buslineName;
    }

    public List<StationNode> getStations() {
        return stations;
    }

    public void setStations(List<StationNode> stations) {
        this.stations = stations;
    }
}
