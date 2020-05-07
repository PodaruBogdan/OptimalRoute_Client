package optimal_route.view;


import optimal_route.contract.IStationNodePersistency;

import javax.swing.*;
import java.awt.*;


public class TravelerView extends JFrame {
    private BusLinesListing busLinesListing;
    private LoginArea loginArea;
    private MapArea mapArea;
    private IStationNodePersistency stationNodePersistency;
    public TravelerView(IStationNodePersistency stationNodePersistency){
        this.stationNodePersistency=stationNodePersistency;
        ImageIcon img = new ImageIcon("bus.jpg");
        this.setIconImage(img.getImage());
        this.setTitle("Busline App");
        mapArea = new MapArea(stationNodePersistency);
        busLinesListing = new BusLinesListing();
        loginArea = new LoginArea();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(busLinesListing);
        panel.add(Box.createRigidArea(new Dimension(20,80)));
        panel.add(loginArea);
        this.setContentPane(new DualView(mapArea, panel));
        this.pack();
        this.setVisible(true);
    }

    public MapArea getMapArea() {
        return mapArea;
    }

    public BusLinesListing getBusLinesListing() {
        return busLinesListing;
    }

    public LoginArea getLoginArea() {
        return loginArea;
    }
}
