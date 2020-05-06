package optimal_route.controller;

import com.opencsv.exceptions.CsvValidationException;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.contract.StationNode;
import optimal_route.view.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;

public class EmployeeController {
    IStationNodePersistency persistency;
    EmployeeView view;
    TravelerView travelerView;
    private ObjectInputStream response;
    private ObjectOutputStream request;
    public EmployeeController(TravelerView travelerView, EmployeeView view, IStationNodePersistency persistency,ObjectInputStream ois,ObjectOutputStream oos) {
        this.response=ois;
        this.request=oos;
        this.view = view;
        this.travelerView = travelerView;
        this.persistency = persistency;
        view.getNodeTool().AddSaveListener(new SaveListener());
        view.getNodeTool().AddAddLinkListener(new AddLinkListener());
        view.getNodeTool().AddAddListener(new AddListener());
        view.getNodeTool().AddRemoveListener(new RemoveListener());
        view.getNodeTool().AddOptimalListener(new SearchOptimalListener());

        //view.getNodeTool().AddSaveMapListener(new SaveMapListener());
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

    public EmployeeView getView() {
        return view;
    }

    class AddLinkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleLink();
        }
    }

    class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleAdd();
        }
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<StationNode> list = view.getMapArea().getData();

            try {
                if(list!=null) {
                    persistency.writeAll(list);
                }
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            travelerView.getBusLinesArea().setDrawData(list);
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String s : getStationNames(list)) {
                model.addElement("Line : " + s);
            }
            travelerView.getBusLinesListing().getList().setModel(model);
            MapArea.toggleSave();
        }
    }

    class SearchOptimalListener implements ActionListener {
        private Child getMyRoot(Stack<StationNode> stat, HashMap<StationNode,Integer> distances){
            Child root=new Child("optimal_route");
            root.setType("route_info");
            StationNode end=null;
            for(int i=0;i<stat.size();i++) {
                if (stat.get(i).getStationName().equals(view.getNodeTool().getToField().getText())) {
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
                String source = view.getNodeTool().getFromField().getText();
                String destination = view.getNodeTool().getToField().getText();
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
                                if (stat.get(i).getStationName().equals(view.getNodeTool().getToField().getText())) {
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

    class SaveMapListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MapArea.toggleRmv();
        }
    }

}
