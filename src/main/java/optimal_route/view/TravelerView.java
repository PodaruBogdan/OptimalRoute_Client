package optimal_route.view;


import optimal_route.contract.IStationNodePersistency;
import optimal_route.lang.ConcreteLangSubject;
import optimal_route.lang.LangObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


public class TravelerView extends JFrame implements LangObserver {
    private BusLinesListing busLinesListing;
    private LoginArea loginArea;
    private MapArea mapArea;
    private IStationNodePersistency stationNodePersistency;
    private ConcreteLangSubject langSubject;
    private JComboBox langList;
    public TravelerView(IStationNodePersistency stationNodePersistency){
        this.stationNodePersistency=stationNodePersistency;
        ImageIcon img = new ImageIcon("bus.jpg");
        this.setIconImage(img.getImage());
        this.setTitle("Busline App");
        mapArea = new MapArea(stationNodePersistency);
        busLinesListing = new BusLinesListing();
        loginArea = new LoginArea();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel languagePanel = new JPanel();
        String[] langs = { "English","Romana","Deutsch" };
        langList = new JComboBox(langs);
        langList.setSelectedIndex(0);
        ImageIcon lan = new ImageIcon("lan.png");
        Image image = lan.getImage();
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
        lan = new ImageIcon(newimg);
        languagePanel.add(new JLabel(lan));
        languagePanel.add(langList);
        panel.add(languagePanel);
        panel.add(Box.createRigidArea(new Dimension(40,20)));
        panel.add(busLinesListing);
        panel.add(Box.createRigidArea(new Dimension(20,60)));
        panel.add(loginArea);
        this.setContentPane(new DualView(mapArea, panel));
        this.pack();
        this.setVisible(true);
    }

    public MapArea getMapArea() {
        return mapArea;
    }

    public BusLinesListing getBusLinesListing() {
        return busLinesListing;
    }

    public LoginArea getLoginArea() {
        return loginArea;
    }

    @Override
    public void update() {
        ResourceBundle resourceBundle = langSubject.getResourceBundle(langSubject.getState());
        busLinesListing.getArrivalLabel().setText(resourceBundle.getString("trav_arrivalLabel"));
        busLinesListing.getDepartureLabel().setText(resourceBundle.getString("trav_departureLabel"));
        busLinesListing.getSearch().setText(resourceBundle.getString("trav_searchButton"));
        busLinesListing.getBusLineLabel().setText(resourceBundle.getString("trav_buslineLabel"));
        loginArea.getPassword().setText(resourceBundle.getString("trav_passwordLabel"));
        loginArea.getUsername().setText(resourceBundle.getString("trav_usernameLabel"));
        loginArea.getLogin().setText(resourceBundle.getString("trav_loginButton"));
    }

    public void setLangSubject(ConcreteLangSubject langSubject) {
        this.langSubject = langSubject;
    }
    public void setLangListener(ActionListener langListener){langList.addActionListener(langListener);}

    public JComboBox getLangList() {
        return langList;
    }
}
