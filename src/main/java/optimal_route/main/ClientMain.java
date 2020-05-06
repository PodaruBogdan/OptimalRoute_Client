package optimal_route.main;

import optimal_route.contract.Account;
import optimal_route.contract.IAccountPersistency;
import optimal_route.contract.IStationNodePersistency;
import optimal_route.controller.EmployeeController;
import optimal_route.controller.TravelerController;
import optimal_route.controller.TravelerController2;
import optimal_route.view.EmployeeView;
import optimal_route.view.TravelerView;
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


           // Account account=stub.getById(1);
            //System.out.println(account.getUsername());

            InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;

            socket = new Socket(host.getHostName(), 9876);
            ObjectOutputStream request = null;
            ObjectInputStream response = null;

            request = new ObjectOutputStream(socket.getOutputStream());
            response = new ObjectInputStream(socket.getInputStream());

            IAccountPersistency stub = (IAccountPersistency) registry.lookup("optimal_route.contract.IAccountPersistency");
            IStationNodePersistency stubStationNodes = (IStationNodePersistency) registry.lookup("optimal_route.contract.IStationNodePersistency");
            //TravelerView travelerView = new TravelerView();
            //oos = new ObjectOutputStream(socket.getOutputStream());
            //System.out.println("Sending request to Socket Server");
            //oos.writeObject("Dijkstra_call");
            //ois = new ObjectInputStream(socket.getInputStream());
            //new TravelerController(travelerView, stub, stubStationNodes,response,request);
            new TravelerController2(new TravelerView2(stubStationNodes),stub,stubStationNodes,response,request);
            Scanner s = new Scanner(System.in);
            while (!s.next().equals("stop")) ;
           System.exit(0);


            //String message = (String) ois.readObject();
            //System.out.println("Message: " + message);
            //ois.close();
            //.close();


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


}