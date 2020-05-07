package optimal_route.controller;

import com.opencsv.exceptions.CsvValidationException;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.contract.StationNode;
import optimal_route.view.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Timer;

public class EmployeeController2 {
    IStationNodePersistency persistency;
    EmployeeView2 view;
    TravelerView2 travelerView;
    private ObjectInputStream response;
    private ObjectOutputStream request;
    EditBusline editBusline;
    public EmployeeController2(TravelerView2 travelerView, EmployeeView2 view, IStationNodePersistency persistency,ObjectInputStream ois,ObjectOutputStream oos){
        this.response=ois;
        this.request=oos;
        this.view = view;
        this.travelerView = travelerView;
        this.persistency = persistency;
        update();
        view.getBusLinesListing2().addListListener(new CustomListListener());
        view.getBuslineTool().AddAddBusListener(new AddBusListener());
        view.getBuslineTool().AddRmvBusListener(new RmvBusListener());
        view.getBuslineTool().addEdtBusListener(new EdtBusListener());
        view.getBuslineTool().AddOptimalListener(new SearchOptimalListener());

    }

    void update(){
        List<StationNode> stationNodes = null;
        try {
            stationNodes = persistency.getAll();
            view.getBusLinesArea().setDrawData(stationNodes);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stationNodes!=null) {
            DefaultListModel model = (DefaultListModel) view.getBusLinesListing2().getList().getModel();
            model.clear();
            for (String s : getStationNames(stationNodes)) {
                if(!model.contains("Line : "+s))
                    model.addElement("Line : " + s);
            }
        }else{
            System.out.println("Model null!!!");
        }
        updateTraveler();
    }

    public void updateTraveler(){
        List<StationNode> stationNodes = null;
        try {
            stationNodes = persistency.getAll();
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



    class AddBusListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleEditable();
            new AddBusline(persistency,view,travelerView);

        }
    }
    class EdtBusListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<StationNode> data = persistency.getAll();
                if(!view.getBusLinesListing2().getList().isSelectionEmpty()) {
                    String busName = view.getBusLinesListing2().getList().getSelectedValue();
                    editBusline = new EditBusline(busName,data);
                    editBusline.AddSetListener(new SetListener());
                }else {
                    JOptionPane.showMessageDialog(null,"No selected busline");
                }
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }


        }
    }

    class SetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(editBusline!=null){
                try {
                    String newName = editBusline.getBusName().getText();
                    String oldName = view.getBusLinesListing2().getList().getSelectedValue().substring(7);
                    List<StationNode> dataToUpdate = editBusline.getBusLinesArea().getDrawData();
                    if(!newName.equals("") && dataToUpdate!=null) {
                        for (StationNode stationNode : dataToUpdate) {
                            StationNode stationNode1 = persistency.getById(stationNode.getId());
                            if(stationNode1!=null && stationNode1.getBuslinesPassingThrough().contains(oldName)) {
                                stationNode1.setApparentCoordinate(stationNode.getApparentCoordinate());
                                stationNode1.getBuslinesPassingThrough().remove(oldName);
                                stationNode1.getBuslinesPassingThrough().add(newName);
                                persistency.update(stationNode1);
                            }
                        }
                        update();
                        travelerView.getMapArea().updateData();
                    }
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
    }









    class RmvBusListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<StationNode> stationNodes = null;
            try {
                stationNodes = persistency.getAll();
                if(!view.getBusLinesListing2().getList().isSelectionEmpty()) {
                    String busName = view.getBusLinesListing2().getList().getSelectedValue().substring(7);
                    System.out.println(busName);
                    for (Iterator<StationNode>it = stationNodes.iterator();it.hasNext();) {
                        StationNode stationNode= it.next();
                        int numOfLines = stationNode.getBuslinesPassingThrough().size();
                        if (numOfLines <= 1 && stationNode.getBuslinesPassingThrough().contains(busName)) {
                            StationNode s = persistency.getById(stationNode.getId());
                            if(s!=null) {
                                persistency.delete(stationNode.getId());
                                it.remove();
                            }
                        } else if (numOfLines >1 && stationNode.getBuslinesPassingThrough().contains(busName)) {
                            StationNode stationNode1 = persistency.getById(stationNode.getId());
                            if(stationNode1!=null) {
                                stationNode1.getBuslinesPassingThrough().remove(busName);
                                persistency.update(stationNode1);
                            }
                        }
                    }
                    update();
                    travelerView.getMapArea().updateData();
                }else{
                    JOptionPane.showMessageDialog(null,"No busline was selected!");
                }
            } catch (RemoteException f) {
                f.printStackTrace();
            }
        }
    }




    class CustomListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            view.getBusLinesArea().setSelectedBus(view.getBusLinesListing2().getList().getSelectedValue());
        }
    }

    class SearchOptimalListener implements ActionListener {
        private Child getMyRoot(Stack<StationNode> stat, HashMap<StationNode,Integer> distances){
            Child root=new Child("optimal_route");
            root.setType("route_info");
            StationNode end=null;
            for(int i=0;i<stat.size();i++) {
                if (stat.get(i).getStationName().equals(view.getBuslineTool().getToField().getText())) {
                    end = stat.get(i);
                    break;
                }
            }
            Child cost=new Child(""+distances.get(end));
            cost.setType("cost");
            root.addChild(cost);
            while(!stat.isEmpty()){
                StationNode node=stat.pop();
                Child c=new Child(node.getStationName());
                c.setType("station");
                Child buses=new Child("buslines");
                buses.setType("buslines");
                HashSet<String> s=new HashSet(node.getBuslinesPassingThrough());
                for(String b:s){
                    Child t=new Child(b);
                    t.setType("busline");
                    buses.addChild(t);
                }
                Child neighbors=new Child("neighbors");
                neighbors.setType("neighbors");
                for(StationNode n:node.getNeighbors()){
                    Child t=new Child(n.getStationName());
                    t.setType("neighbor");
                    neighbors.addChild(t);
                }
                Child appC = new Child(node.getApparentCoordinate().toString());
                Child ID = new Child(String.valueOf(node.getId()));
                ID.setType("id");
                appC.setType("apparent_coordinate");
                c.addChild(ID);
                c.addChild(appC);
                c.addChild(neighbors);
                c.addChild(buses);
                root.addChild(c);
            }
            return root;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Pair<Stack<StationNode>, HashMap<StationNode,Integer>> list= null;
            try {
                String source = view.getBuslineTool().getFromField().getText();
                String destination = view.getBuslineTool().getToField().getText();
                Object[] msg = new Object[]{"dijkstra", 3, true, source, destination};
                request.writeObject(msg);
                list = (Pair<Stack<StationNode>, HashMap<StationNode, Integer>>) response.readObject();

            } catch (RemoteException ex) {
                ex.printStackTrace();
            } catch (UnknownHostException hostException) {
                hostException.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException notFoundException) {
                notFoundException.printStackTrace();
            }
            if(list!=null) {
                Stack<StationNode> stat = list.getKey();
                HashMap<StationNode, Integer> distances = list.getValue();
                Object[] options = {".csv",
                        ",json",
                        ".xml"};

                int result = JOptionPane.showOptionDialog(null,
                        "Save found route as",
                        "Save file",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        null);
                switch (result) {
                    case 0:
                        try {
                            StationNode end = null;
                            for (int i = 0; i < stat.size(); i++) {
                                if (stat.get(i).getStationName().equals(view.getBuslineTool().getToField().getText())) {
                                    end = stat.get(i);
                                    break;
                                }
                            }
                            String data = distances.get(end) + ",";
                            while (!stat.isEmpty()) {
                                StationNode s = stat.pop();
                                data += s.getStationName() + " " + s.getApparentCoordinate() + ",";
                            }
                            String[] line = CSVReport.convertToCSV(data, ',');
                            CSVReport.writeLine("optimal.csv", line, ',');
                        } catch (CsvValidationException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 1:
                        Child root = getMyRoot(stat, distances);
                        JSONReport.writeLine("optimal.json", root);
                        break;
                    case 2:
                        Child root2 = getMyRoot(stat, distances);
                        XMLReport.writeLine(root2, "optimal.xml");
                        break;
                    default:
                        break;
                }

            }
        }

    }




}
