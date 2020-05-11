package optimal_route.view;
import optimal_route.contract.StationNode;
import optimal_route.lang.ConcreteLangSubject;
import optimal_route.lang.LangObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

public class EditBusline extends JFrame implements LangObserver {
    private BusLinesArea busLinesArea;
    private NodeTool2 nodeTool2;
    private JTextField busName;
    private JButton set;
    String name;
    JTextArea textArea;
    JLabel editBusName;
    private ConcreteLangSubject langSubject;
    public EditBusline(String name, List<StationNode> data){
        this.name=name;
        set=new JButton("Set");
        busName = new JTextField(10);
        this.setTitle("Content creation");
        busLinesArea = new BusLinesArea();
        busLinesArea.setDrawData(data);
        busLinesArea.setSelectedBus(name);
        busLinesArea.toggleCanDrag();
        nodeTool2=new NodeTool2();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(30,100));
        editBusName = new JLabel("Edit busline name: ");
        panel.add(editBusName);
        busName.setText(name.substring(7));
        panel.add(busName);
        panel.add(set);
        textArea = new JTextArea(4, 10);
        textArea.setEditable(false);
        textArea.setText("Info:\n Adjust each node by selecting it.\n Click to select.\n Drag to move.");
        panel.add(textArea);
        this.setContentPane(new DualView(busLinesArea,panel));
        this.pack();
        this.setVisible(true);

    }

    public JButton getSet() {
        return set;
    }

    public JTextField getBusName() {
        return busName;
    }

    public void setBusName(JTextField busName) {
        this.busName = busName;
    }

    public BusLinesArea getBusLinesArea() {
        return busLinesArea;
    }

    public NodeTool2 getNodeTool() {
        return nodeTool2;
    }
    public void AddSetListener(ActionListener listener){set.addActionListener(listener);}

    public void setLangSubject(ConcreteLangSubject langSubject) {
        this.langSubject = langSubject;
    }
    @Override
    public void update() {
        ResourceBundle resourceBundle = langSubject.getResourceBundle(langSubject.getState());
        set.setText(resourceBundle.getString("emp_update_setButton"));
        editBusName.setText(resourceBundle.getString("emp_update_editBusNameLabel"));
        textArea.setText(resourceBundle.getString("emp_update_textArea"));
    }
}