package optimal_route.view;



import javax.swing.*;
import java.awt.*;

public class EmployeeView extends JFrame {
    private BuslineTool buslineTool;
    private BusLinesArea busLineArea;
    private BusLinesListing2 busLinesListing2;
    public EmployeeView() {

        this.setTitle("Content creation");
        busLineArea = new BusLinesArea();
        buslineTool = new BuslineTool();

        busLinesListing2 = new BusLinesListing2();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(busLinesListing2);
        panel.add(Box.createRigidArea(new Dimension(20, 50)));
        panel.add(buslineTool);


        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(new DualView(busLineArea, panel));
        this.pack();
        this.setVisible(true);

    }

    public BusLinesListing2 getBusLinesListing2() {
        return busLinesListing2;
    }

    public BusLinesArea getBusLinesArea() {
        return busLineArea;
    }

    public BuslineTool getBuslineTool() {
        return buslineTool;
    }
}
