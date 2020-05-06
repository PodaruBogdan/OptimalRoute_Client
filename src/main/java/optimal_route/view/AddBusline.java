package optimal_route.view;


import optimal_route.contract.IStationNodePersistency;

import javax.swing.*;

public class AddBusline extends JFrame {
    private MapArea mapArea;
    private NodeTool2 nodeTool2;
    public AddBusline(IStationNodePersistency persistency){
        this.setTitle("Content creation");
        mapArea=new MapArea(persistency);
        nodeTool2=new NodeTool2();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(new DualView(mapArea,nodeTool2));
        this.pack();
        this.setVisible(true);

    }
    public MapArea getMapArea() {
        return mapArea;
    }
    public NodeTool2 getNodeTool() {
        return nodeTool2;
    }
}