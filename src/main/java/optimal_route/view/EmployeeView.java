package optimal_route.view;



import optimal_route.lang.ConcreteLangSubject;
import optimal_route.lang.LangObserver;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class EmployeeView extends JFrame implements LangObserver {
    private BuslineTool buslineTool;
    private BusLinesArea busLineArea;
    private BusLinesListing2 busLinesListing2;
    private ConcreteLangSubject langSubject;

    public EmployeeView() {

        this.setTitle("Content creation");
        busLineArea = new BusLinesArea();
        buslineTool = new BuslineTool();

        busLinesListing2 = new BusLinesListing2();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(busLinesListing2);
        panel.add(Box.createRigidArea(new Dimension(20, 50)));
        panel.add(buslineTool);


        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(new DualView(busLineArea, panel));
        this.pack();
        this.setVisible(true);

    }

    public BusLinesListing2 getBusLinesListing2() {
        return busLinesListing2;
    }

    public BusLinesArea getBusLinesArea() {
        return busLineArea;
    }

    public BuslineTool getBuslineTool() {
        return buslineTool;
    }

    public void setLangSubject(ConcreteLangSubject langSubject) {
        this.langSubject = langSubject;
    }

    @Override
    public void update() {
        ResourceBundle resourceBundle = langSubject.getResourceBundle(langSubject.getState());
        busLinesListing2.getSearchLabel().setText(resourceBundle.getString("emp_searchLabel"));
        buslineTool.getTo().setText(resourceBundle.getString("emp_toLabel"));
        buslineTool.getFrom().setText(resourceBundle.getString("emp_fromLabel"));
        buslineTool.getSearchOptimal().setText(resourceBundle.getString("emp_reportButton"));
        buslineTool.getAddBus().setText(resourceBundle.getString("emp_addButton"));
        buslineTool.getRmvBus().setText(resourceBundle.getString("emp_removeButton"));
        buslineTool.getEdtBus().setText(resourceBundle.getString("emp_updateButton"));
    }
}
