package optimal_route.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NodeTool extends JPanel {

    private JButton addLink;
    private JButton add;
    private JButton rmv;
    private JButton save;
    private JLabel from;
    private JLabel to;
    private JTextField fromField;
    private JTextField toField;
    //private JButton saveMap;
    private JButton searchOptimal;
    public NodeTool(){
        from=new JLabel("FROM : ");
        to=new JLabel("to : ");
        toField=new JTextField(10);
        fromField=new JTextField(10);
        searchOptimal=new JButton("Search optimal");
        addLink = new JButton("Add link");
        add = new JButton("Add node");
        rmv = new JButton("Remove node");
        save = new JButton("Save changes");
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(add);
        this.add(rmv);
        this.add(addLink);
        this.add(Box.createRigidArea(new Dimension(10,100)));
        this.add(save);
        this.add(Box.createRigidArea(new Dimension(10,20)));
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

    public void setSearchOptimal(JButton searchOptimal) {
        this.searchOptimal = searchOptimal;
    }

    public void AddAddListener(ActionListener listener){
        add.addActionListener(listener);
    }
    public void AddRemoveListener(ActionListener listener){
        rmv.addActionListener(listener);
    }
    public void AddSaveListener(ActionListener listener){
        save.addActionListener(listener);
    }
    public void AddAddLinkListener(ActionListener listener){
        addLink.addActionListener(listener);
    }
    public void AddOptimalListener(ActionListener listener){
        searchOptimal.addActionListener(listener);
    }




}
