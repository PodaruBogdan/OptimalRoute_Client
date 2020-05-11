package optimal_route.view;
import optimal_route.lang.ConcreteLangSubject;
import optimal_route.lang.LangObserver;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class AdminView extends JFrame implements LangObserver {
    private JButton add;
    private JButton rmv;
    private JButton upd;

    private JLabel l1,l2,l3,l4,l5;
    private JTextField t1,t2,t3,t4,t5;
    private JList<String> list;
    private ConcreteLangSubject langSubject;
    public AdminView() {
        this.setTitle("Employee administration");
        add = new JButton("Add new employee");
        rmv = new JButton("Remove employee");
        upd = new JButton("Update employee info");
        l1 = new JLabel("role: ");
        l2 = new JLabel("name: ");
        l3 = new JLabel("email: ");
        l4 = new JLabel("username: ");
        l5 = new JLabel("password: ");
        t1 = new JTextField(20);
        t2 = new JTextField(20);
        t3 = new JTextField(30);
        t4 = new JTextField(20);
        t5 = new JTextField(20);
        DefaultListModel model = new DefaultListModel();
        list = new JList<>(model);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(20, 200));
        panel.add(scrollPane);
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        p1.add(l1);
        p1.add(t1);
        p2.add(l2);
        p2.add(t2);
        p3.add(l3);
        p3.add(t3);
        p4.add(l4);
        p4.add(t4);
        p5.add(l5);
        p5.add(t5);
        panel.add(p1);
        panel.add(p2);
        panel.add(p3);
        panel.add(p4);
        panel.add(p5);
        panel.add(add);
        panel.add(rmv);
        panel.add(upd);
        panel.add(Box.createRigidArea(new Dimension(10,10)));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);

    }

    public JButton getUpd() {
        return upd;
    }

    public void setUpd(JButton upd) {
        this.upd = upd;
    }

    public JButton getAdd() {
        return add;
    }

    public void setAdd(JButton add) {
        this.add = add;
    }

    public JButton getRmv() {
        return rmv;
    }

    public void setRmv(JButton rmv) {
        this.rmv = rmv;
    }

    public JLabel getL1() {
        return l1;
    }

    public void setL1(JLabel l1) {
        this.l1 = l1;
    }

    public JLabel getL2() {
        return l2;
    }

    public void setL2(JLabel l2) {
        this.l2 = l2;
    }

    public JLabel getL3() {
        return l3;
    }

    public void setL3(JLabel l3) {
        this.l3 = l3;
    }

    public JLabel getL4() {
        return l4;
    }

    public void setL4(JLabel l4) {
        this.l4 = l4;
    }

    public JLabel getL5() {
        return l5;
    }

    public void setL5(JLabel l5) {
        this.l5 = l5;
    }

    public JTextField getT1() {
        return t1;
    }

    public void setT1(JTextField t1) {
        this.t1 = t1;
    }

    public JTextField getT2() {
        return t2;
    }

    public void setT2(JTextField t2) {
        this.t2 = t2;
    }

    public JTextField getT3() {
        return t3;
    }

    public void setT3(JTextField t3) {
        this.t3 = t3;
    }

    public JTextField getT4() {
        return t4;
    }

    public void setT4(JTextField t4) {
        this.t4 = t4;
    }

    public JTextField getT5() {
        return t5;
    }

    public void setT5(JTextField t5) {
        this.t5 = t5;
    }

    public JList<String> getList() {
        return list;
    }

    public void setList(JList<String> list) {
        this.list = list;
    }

    public void addRemoveListener(ActionListener listener){
        rmv.addActionListener(listener);
    }
    public void addAddListener(ActionListener listener){
        add.addActionListener(listener);
    }
    public void addUpdateListener(ActionListener listener){
        upd.addActionListener(listener);
    }
    public void addListListener(ListSelectionListener listener){
        list.addListSelectionListener(listener);
    }

    public void setLangSubject(ConcreteLangSubject langSubject) {
        this.langSubject = langSubject;
    }

    @Override
    public void update() {
        ResourceBundle resourceBundle = langSubject.getResourceBundle(langSubject.getState());
        add.setText(resourceBundle.getString("adm_addButton"));
        rmv.setText(resourceBundle.getString("adm_removeButton"));
        upd.setText(resourceBundle.getString("adm_updateButton"));
        l1.setText(resourceBundle.getString("adm_roleLabel"));
        l2.setText(resourceBundle.getString("adm_nameLabel"));
        l3.setText(resourceBundle.getString("adm_emailLabel"));
        l4.setText(resourceBundle.getString("adm_usernameLabel"));
        l5.setText(resourceBundle.getString("adm_passwordLabel"));
    }
}
