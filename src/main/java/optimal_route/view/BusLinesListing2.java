package optimal_route.view;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;


public class BusLinesListing2 extends JPanel {
    private JList<String> list;

    private JTextField searchLine;
    private DefaultListModel model;
    public BusLinesListing2() {
        searchLine=new JTextField(10);

        JPanel p1=new JPanel();
        JPanel p2=new JPanel();

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectedIndex(0);
        list.setSelectionMode(0);
        JScrollPane pane = new JScrollPane(list);
        this.add(pane);
        JPanel p = new JPanel();
        p.add(new JLabel("Search: "));
        p.add(searchLine);
        this.add(p);

        searchLine.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e) { filter();}
            @Override public void removeUpdate(DocumentEvent e) { filter();}
            @Override public void changedUpdate(DocumentEvent e) {}
            private void filter() {
                String filter = searchLine.getText();
                filterList(list, filter);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(p1);
        panel.add(p2);
        this.add(panel);
        this.add(Box.createRigidArea(new Dimension(10,20)));
        //this.setPreferredSize(new Dimension(100, 400));
    }
    public JList<String> getList() {
        return list;
    }
    public void addListListener(ListSelectionListener listener){
        list.addListSelectionListener(listener);
    }


    private void filterList(JList<String> list, String filter) {
        DefaultListModel<String> model2 = new DefaultListModel<>();
        if(filter.isEmpty()){
            list.setModel(model);
        }else {
            for (int i=0;i<list.getModel().getSize();i++) {
                String s=list.getModel().getElementAt(i);
                if (!s.contains(filter)) {
                    if (model2.contains(s)) {
                        model2.removeElement(s);
                    }
                } else {
                    if (!model2.contains(s)) {
                        model2.addElement(s);
                    }
                }
            }
            list.setModel(model2);
        }
    }



    public void setList(JList<String> list) {
        this.list = list;
    }

}
