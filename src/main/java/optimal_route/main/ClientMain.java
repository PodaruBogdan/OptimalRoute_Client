package optimal_route.main;

import optimal_route.contract.IAccountPersistency;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.controller.TravelerController2;

import optimal_route.view.TravelerView2;

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

            new TravelerController2(new TravelerView2(stubStationNodes),stub,stubStationNodes,response,request);
            Scanner s = new Scanner(System.in);
            while (!s.next().equals("stop")) ;
           System.exit(0);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


}