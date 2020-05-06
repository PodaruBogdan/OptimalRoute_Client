package optimal_route.view;

import optimal_route.contract.StationNode;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NodePopUp extends JFrame{
    JTextField name;
    JLabel nameLabel;
    JButton add;
    private int x,y;
    List<StationNode> map;
    public NodePopUp(List<StationNode> map, int x, int y){
        this.map = map;
        this.x = x;
        this.y = y;
        name = new JTextField(10);
        nameLabel=new JLabel("Station name");
        add = new JButton("Set");
        this.setSize(new Dimension(40,40));
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        pane.add(nameLabel);
        pane.add(name);
        pane.add(add);
        this.setLocation(new Point(x,y));
        this.setContentPane(pane);
        this.pack();
        this.setVisible(true);
        add.addActionListener(new MyCustomListener(this));
    }
    class MyCustomListener implements ActionListener{
        private JFrame frame;
        MyCustomListener(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.hide();
            String stationName = name.getText();
            StationNode node = new StationNode(stationName, new Point(x,y));
            System.out.println(node.getStationName());
            map.add(node);
        }
    }

}
