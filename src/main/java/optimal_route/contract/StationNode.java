package optimal_route.contract;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class StationNode implements Serializable {
    private int id;
    private String stationName;
    private Point realCoordinates;
    private Point apparentCoordinate;
    private List<String> buslinesPassingThrough;
    private List<StationNode> neighbors;
    public StationNode(){}

    public StationNode(String stationName, Point apparentCoordinate) {
        this.stationName = stationName;
        this.apparentCoordinate = apparentCoordinate;
        neighbors=new ArrayList<StationNode>();
        buslinesPassingThrough=new ArrayList<String>();
    }
    public StationNode(String stationName, Point apparentCoordinate, List<StationNode> neighbors) {
        this.stationName = stationName;
        this.apparentCoordinate = apparentCoordinate;
        this.neighbors = neighbors;
        buslinesPassingThrough=new ArrayList<String>();
    }

    public void addBusline(String busline){
        if(!buslinesPassingThrough.contains(busline))
            buslinesPassingThrough.add(busline);

    }

    public void addNeighbor(StationNode neighbor){
        boolean found = false;
        for(StationNode n : neighbors){
            if(n.getStationName().equals(neighbor.stationName)){
                found=true;
            }
        }
        if(found==false){
            neighbors.add(neighbor);
        }
       found=false;
        for(StationNode n:neighbor.getNeighbors()){
            if(n.getStationName().equals(stationName)){
                found=true;
            }
        }
        if(found==false){
            neighbor.addNeighbor(this);
        }
    }

    public void removeNeighbors(){
        List<StationNode> neighbors = getNeighbors();

        for(StationNode neighbor: neighbors){
            List<StationNode> recNeighbors = new ArrayList<>(neighbor.getNeighbors());
            recNeighbors.remove(this);
            neighbor.setNeighbors(recNeighbors);
        }
        this.neighbors = new ArrayList<>();

    }

    public List<StationNode> getNeighbors(){
        return neighbors;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Point2D getRealCoordinates() {
        return realCoordinates;
    }

    public void setRealCoordinates(Point realCoordinates) {
        this.realCoordinates = realCoordinates;
    }

    public Point getApparentCoordinate() {
        return apparentCoordinate;
    }

    public void setApparentCoordinate(Point apparentCoordinate) {
        this.apparentCoordinate = apparentCoordinate;
    }
    public List<String> getBuslinesPassingThrough() {
        return buslinesPassingThrough;
    }

    public void setBuslinesPassingThrough(List<String> busLines) {
        this.buslinesPassingThrough = busLines;
    }

    public void setNeighbors(List<StationNode> neighbors) {
        this.neighbors = neighbors;
    }

    public String toString(){
        String neighbors = "";
        for(StationNode s:this.getNeighbors()){
            neighbors+= s.getStationName()+";";
        }
        String buses="";
        for(String s:buslinesPassingThrough){
            buses+=s+";";
        }
        return id+","+stationName+","+apparentCoordinate+","+neighbors+","+buses;
    }



}

