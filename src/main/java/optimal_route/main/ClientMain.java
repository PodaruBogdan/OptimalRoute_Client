package optimal_route.main;

import optimal_route.contract.IAccountPersistency;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.controller.TravelerController;

import optimal_route.lang.ConcreteLangSubject;
import optimal_route.view.TravelerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(null,2020);

            InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;

            socket = new Socket(host.getHostName(), 9876);
            ObjectOutputStream request = null;
            ObjectInputStream response = null;

            request = new ObjectOutputStream(socket.getOutputStream());
            response = new ObjectInputStream(socket.getInputStream());

            IAccountPersistency stub = (IAccountPersistency) registry.lookup("optimal_route.contract.IAccountPersistency");
            IStationNodePersistency stubStationNodes = (IStationNodePersistency) registry.lookup("optimal_route.contract.IStationNodePersistency");
            ConcreteLangSubject langSubject = new ConcreteLangSubject();
            TravelerView travelerView = new TravelerView(stubStationNodes);
            langSubject.attachObserver(travelerView);
            travelerView.setLangSubject(langSubject);
            LangListener langListener = new LangListener(langSubject,travelerView.getLangList());
            travelerView.setLangListener(langListener);
            new TravelerController(travelerView,stub,stubStationNodes,response,request,langSubject);
            Scanner s = new Scanner(System.in);
            while (!s.next().equals("stop")) ;
           System.exit(0);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }



}
