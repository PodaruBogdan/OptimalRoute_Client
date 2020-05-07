package optimal_route.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NodeTool2 extends JPanel {

    private JButton addLink;
    private JButton add;
    private JButton rmv;
    private JButton save;
    private JTextField textField;
    public NodeTool2(){

        addLink = new JButton("Add link");
        add = new JButton("Add node");
        rmv = new JButton("Remove node");
        save = new JButton("Save changes");
        textField=new JTextField(10);

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(new JLabel("Busline name: "));
        this.add(textField);
        this.add(add);
        this.add(rmv);
        this.add(addLink);
        this.add(Box.createRigidArea(new Dimension(10,100)));
        this.add(save);
        this.add(Box.createRigidArea(new Dimension(10,20)));
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();

        this.add(p2);
        this.add(p1);


    }

    public JTextField getTextField() {
        return textField;
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




}
