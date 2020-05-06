package optimal_route.view;
import optimal_route.contract.StationNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinePopUp extends JFrame{
    private JTextField stationNames;
    private JLabel nameLabel;
    private JButton add;
    private StationNode n1,n2;
    public LinePopUp(StationNode n1,StationNode n2,int apx, int apy){
        this.n1=n1;
        this.n2=n2;
        stationNames = new JTextField(10);
        nameLabel=new JLabel("Bus names: ");
        add = new JButton("Set");
        this.setSize(new Dimension(40,40));
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        pane.add(nameLabel);
        pane.add(stationNames);
        pane.add(add);
        this.setLocation(new Point(apx,apy));
        this.setContentPane(pane);
        this.pack();
        this.setVisible(true);
        add.addActionListener(new MyCustomListener(this));
    }
    class MyCustomListener implements ActionListener {
        private JFrame frame;
        MyCustomListener(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.hide();
            String[] names = stationNames.getText().split(",");
           for(String s:names){
               n1.addBusline(s);
               n2.addBusline(s);
           }
        }
    }
}
