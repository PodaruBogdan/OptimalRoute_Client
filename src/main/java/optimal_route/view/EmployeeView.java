package optimal_route.view;


import optimal_route.contract.IStationNodePersistency;

import javax.swing.*;
import java.awt.*;

public class EmployeeView extends JFrame {
    private MapArea mapArea;
    private NodeTool nodeTool;
    public EmployeeView(IStationNodePersistency persistency){
        this.setTitle("Content creation");
        mapArea=new MapArea(persistency);
        nodeTool=new NodeTool();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(new DualView(mapArea,nodeTool));
        this.pack();
        this.setVisible(true);

    }
    public MapArea getMapArea() {
        return mapArea;
    }
    public NodeTool getNodeTool() {
        return nodeTool;
    }
}
