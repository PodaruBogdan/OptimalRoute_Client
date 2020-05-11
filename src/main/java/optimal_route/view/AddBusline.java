package optimal_route.view;


import optimal_route.contract.IStationNodePersistency;
import optimal_route.contract.StationNode;
import optimal_route.lang.ConcreteLangSubject;
import optimal_route.lang.LangObserver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddBusline extends JFrame implements LangObserver {
    private MapArea mapArea;
    private NodeTool2 nodeTool2;
    private IStationNodePersistency persistency;
    private EmployeeView employeeView;
    private TravelerView travelerView;
    private ConcreteLangSubject langSubject;
    public AddBusline(IStationNodePersistency persistency, EmployeeView view, TravelerView travelerView){
        this.employeeView=view;
        this.travelerView=travelerView;
        this.persistency=persistency;
        this.setTitle("Content creation");
        mapArea=new MapArea(persistency);
        nodeTool2=new NodeTool2();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(new DualView(mapArea,nodeTool2));
        this.pack();
        this.setVisible(true);
        nodeTool2.AddSaveListener(new SaveListener());
        nodeTool2.AddAddLinkListener(new AddLinkListener());
        nodeTool2.AddAddListener(new AddListener());
        nodeTool2.AddRemoveListener(new RemoveListener());
        nodeTool2.getTextField().getDocument().addDocumentListener(new MyTextListener());
    }
    public MapArea getMapArea() {
        return mapArea;
    }
    public NodeTool2 getNodeTool() {
        return nodeTool2;
    }
    private List<String> getStationNames(List<StationNode> stations) {
        List<String> result = new ArrayList<>();
        for (StationNode node : stations) {
            List<String> l = node.getBuslinesPassingThrough();
            for (String s : l) {
                if (!result.contains(s)) {
                    result.add(s);
                }
            }
        }
        return result;
    }
    public void setLangSubject(ConcreteLangSubject langSubject) {
        this.langSubject = langSubject;
    }


    void updateEmployeeView(){
        List<StationNode> stationNodes = null;
        try {
            stationNodes = persistency.getAll();
            employeeView.getBusLinesArea().setDrawData(stationNodes);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stationNodes!=null) {
            DefaultListModel model = (DefaultListModel) employeeView.getBusLinesListing2().getList().getModel();
            model.clear();
            for (String s : getStationNames(stationNodes)) {
                if(!model.contains("Line : "+s))
                    model.addElement("Line : " + s);
            }
        }else{
            System.out.println("Model null!!!");
        }
        updateTravelerView();
    }
    void updateTravelerView(){
        List<StationNode> stationNodes = null;
        try {
            stationNodes = persistency.getAll();
            travelerView.getMapArea().updateData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stationNodes!=null) {
            DefaultListModel model = (DefaultListModel) travelerView.getBusLinesListing().getList().getModel();
            model.clear();
            for (String s : getStationNames(stationNodes)) {
                if(!model.contains("Line : "+s))
                    model.addElement("Line : " + s);
            }
        }else{
            System.out.println("Model null!!!");
        }
    }

    @Override
    public void update() {
        ResourceBundle resourceBundle = langSubject.getResourceBundle(langSubject.getState());
        nodeTool2.getAdd().setText(resourceBundle.getString("emp_add_addButton"));
        nodeTool2.getRmv().setText(resourceBundle.getString("emp_add_removeButton"));
        nodeTool2.getAddLink().setText(resourceBundle.getString("emp_add_addLinkButton"));
        nodeTool2.getSave().setText(resourceBundle.getString("emp_add_saveButton"));
        nodeTool2.getBusName().setText(resourceBundle.getString("emp_add_busNameLabel"));
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<StationNode> list = mapArea.getData();

            try {
                if(list!=null) {
                    persistency.writeAll(list);
                    updateEmployeeView();
                    MapArea.toggleEditable();
                }
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            //travelerView.getBusLinesArea().setDrawData(list);

           // DefaultListModel<String> model = new DefaultListModel<>();
            //for (String s : getStationNames(list)) {
                //model.addElement("Line : " + s);
           // }
            //travelerView.getBusLinesListing().getList().setModel(model);
            MapArea.toggleSave();
        }
    }
    class AddLinkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleLink();
        }
    }
    class MyTextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            System.err.println(nodeTool2.getTextField().getText());
            mapArea.setBusName(nodeTool2.getTextField().getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleAdd();
        }
    }
    class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleRmv();
        }
    }



}