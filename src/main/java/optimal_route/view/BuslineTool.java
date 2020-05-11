package optimal_route.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BuslineTool extends JPanel {

    private JButton addBus;
    private JButton rmvBus;
    private JButton edtBus;



    private JLabel from;
    private JLabel to;
    private JTextField fromField;
    private JTextField toField;
    //private JButton saveMap;
    private JButton searchOptimal;




    public BuslineTool(){
        from=new JLabel("FROM : ");
        to=new JLabel("to : ");
        toField=new JTextField(10);
        fromField=new JTextField(10);
        searchOptimal=new JButton("Generate report");
        addBus = new JButton("Add bus");
        rmvBus = new JButton("Remove bus");
        edtBus = new JButton("Edit bus");
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(addBus);
        this.add(rmvBus);
        this.add(edtBus);
        this.add(Box.createRigidArea(new Dimension(10,100)));
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        p1.add(to);
        p1.add(toField);
        p2.add(from);
        p2.add(fromField);
        this.add(p2);
        this.add(p1);
        this.add(searchOptimal);

    }

    public JButton getAddBus() {
        return addBus;
    }

    public JButton getRmvBus() {
        return rmvBus;
    }

    public JButton getEdtBus() {
        return edtBus;
    }

    public JLabel getFrom() {
        return from;
    }

    public JLabel getTo() {
        return to;
    }

    public void AddAddBusListener(ActionListener listener){
        addBus.addActionListener(listener);
    }
    public void AddRmvBusListener(ActionListener listener){
        rmvBus.addActionListener(listener);
    }

    public JTextField getFromField() {
        return fromField;
    }

    public void setFromField(JTextField fromField) {
        this.fromField = fromField;
    }

    public JTextField getToField() {
        return toField;
    }

    public void setToField(JTextField toField) {
        this.toField = toField;
    }

    public JButton getSearchOptimal() {
        return searchOptimal;
    }

    public void AddOptimalListener(ActionListener listener){
        searchOptimal.addActionListener(listener);
    }

    public void addEdtBusListener (ActionListener listener){
        edtBus.addActionListener(listener);
    }


}
