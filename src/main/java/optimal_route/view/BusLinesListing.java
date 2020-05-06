package optimal_route.view;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;


public class BusLinesListing extends JPanel {
    private JList<String> list;
    private JLabel s1;
    private JLabel s2;
    private JTextField f1;
    private JTextField f2;
    private JButton search;
    private JTextField searchLine;
    private DefaultListModel model;
    public BusLinesListing() {
        searchLine=new JTextField(20);
        search=new JButton("Search");
        s1=new JLabel("Departure");
        s2=new JLabel("       Arrival");
        f1 = new JTextField(20);
        f2 = new JTextField(20);
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        p1.add(s1);
        p1.add(f1);
        p2.add(s2);
        p2.add(f2);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectedIndex(0);
        list.setSelectionMode(0);
        JScrollPane pane = new JScrollPane(list);
        this.add(pane);
        JPanel p = new JPanel();
        p.add(new JLabel("Busline: "));
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
        panel.add(search);
        this.add(panel);
        this.add(Box.createRigidArea(new Dimension(10,200)));
        this.setPreferredSize(new Dimension(100, 400));
    }
    public JList<String> getList() {
        return list;
    }
    public void addSearchListenr(ActionListener listener){
        search.addActionListener(listener);
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

    public JLabel getS1() {
        return s1;
    }

    public void setS1(JLabel s1) {
        this.s1 = s1;
    }

    public JLabel getS2() {
        return s2;
    }

    public void setS2(JLabel s2) {
        this.s2 = s2;
    }

    public JTextField getF1() {
        return f1;
    }

    public void setF1(JTextField f1) {
        this.f1 = f1;
    }

    public JTextField getF2() {
        return f2;
    }

    public void setF2(JTextField f2) {
        this.f2 = f2;
    }

    public JButton getSearch() {
        return search;
    }

    public void setSearch(JButton search) {
        this.search = search;
    }
}
