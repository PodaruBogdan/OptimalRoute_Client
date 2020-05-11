package optimal_route.controller;

import optimal_route.contract.Account;
import optimal_route.contract.IAccountPersistency;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.contract.StationNode;
import optimal_route.lang.ConcreteLangSubject;
import optimal_route.view.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class TravelerController {

    private TravelerView view;
    private IAccountPersistency accountsPersistency;
    private IStationNodePersistency stationsPersistency;
    private static Object fromServer;
    private ObjectOutputStream request ;
    private ObjectInputStream response ;
    private ConcreteLangSubject langSubject;

    public TravelerController(TravelerView view, IAccountPersistency accountsPersistency, IStationNodePersistency stationsPersistency, ObjectInputStream ois, ObjectOutputStream oos, ConcreteLangSubject langSubject){
        this.langSubject=langSubject;
        this.response=ois;
        this.request=oos;
        this.view=view;
        this.accountsPersistency=accountsPersistency;
        this.stationsPersistency=stationsPersistency;
        update();
        view.getBusLinesListing().addListListener(new CustomListListener());
        view.getBusLinesListing().addSearchListenr(new SearchListener());
        view.getLoginArea().addLoginListener(new LoginListener());
    }

    public void update(){
        List<StationNode> stationNodes = null;
        try {
            stationNodes = stationsPersistency.getAll();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(stationNodes!=null) {
            DefaultListModel model = (DefaultListModel) view.getBusLinesListing().getList().getModel();
            model.clear();
            for (String s : getStationNames(stationNodes)) {
                if(!model.contains("Line : "+s))
                    model.addElement("Line : " + s);
            }
        }else{
            System.out.println("Model null!!!");
        }
    }



    private List<String> getStationNames(List<StationNode> stations){
        if(stations==null){
            return null;
        }
        List<String> result=new ArrayList<>();
        for(StationNode node: stations){
            List<String> l = node.getBuslinesPassingThrough();
            if(l!=null) {
                for (String s : l) {
                    if (!result.contains(s)) {
                        result.add(s);
                    }
                }
            }else{
                System.out.println("NULLLLLLLLLLLLLLLL");
            }
        }
        return result;
    }
    class CustomListListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            view.getMapArea().setCurrentBus(view.getBusLinesListing().getList().getSelectedValue());
        }
    }
    class SearchListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String source = view.getBusLinesListing().getF1().getText();
                String destination = view.getBusLinesListing().getF2().getText();
                Object[] msg = new Object[]{"dijkstra", 3, false, source, destination};
                request.writeObject(msg);
                System.err.println("CLIENT.1");
                Object[] resp = (Object[]) response.readObject();
                System.err.println("CLIENT.2");
                JOptionPane.showMessageDialog(null, resp[0]);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
    }



    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Account> accountList= null;
            try {
                accountList = accountsPersistency.getAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            for(Account account:accountList){
                if(account.getUsername().equals(view.getLoginArea().getUsrField().getText()) && account.getPswd().equals(view.getLoginArea().getPswField().getText())){
                    if(account.getRole().equals("admin")) {
                        AdminView adminView = new AdminView();
                        langSubject.attachObserver(adminView);
                        adminView.setLangSubject(langSubject);
                        langSubject.notifyObservers();
                        new AdminController(adminView,accountsPersistency);
                    }else {
                        EmployeeView employeeView = new EmployeeView();
                        langSubject.attachObserver(employeeView);
                        employeeView.setLangSubject(langSubject);
                        langSubject.notifyObservers();
                        new EmployeeController(view, employeeView, stationsPersistency, response, request,langSubject);
                    }
                    break;
                }
            }
        }
    }


}
