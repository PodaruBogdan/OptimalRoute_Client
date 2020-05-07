package optimal_route.view;

import optimal_route.contract.IStationNodePersistency;
import optimal_route.contract.StationNode;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MapArea extends JPanel implements MouseListener, MouseMotionListener {

    private int moveX  = 0;
    private int moveY  = 0;
    private int clickX = 0;
    private int clickY = 0;
    private String currentBus;
    private static boolean editable=false;

    private static boolean canEdit = false;
    private static boolean canAdd = false;
    private static boolean canRmv = false;
    private static boolean canAddLink = false;
    private static boolean canSave = false;
    private IStationNodePersistency stationNodePersistency;
    private List<StationNode> data;
    private List<StationNode> atStartUp;
    private static Point lastSelected;
    List<Point> clicks;
    HashSet<Point> set;

    private boolean wasSet=false;
    private String busName;

    public static void toggleEditable() {
        if(editable==false) {
            MapArea.editable = true;
        }else
            editable = false;
    }

    public List<StationNode> getData(){
        return data;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public static void toggleEdit() {
        MapArea.canEdit = true;
        MapArea.canAdd=false;
        MapArea.canRmv=false;
        MapArea.canAddLink=false;
        MapArea.canSave=false;
    }

    public static void toggleAdd() {
        MapArea.canEdit = false;
        MapArea.canAdd=true;
        MapArea.canRmv=false;
        MapArea.canAddLink=false;
        MapArea.canSave=false;
    }

    public static void toggleRmv() {
        MapArea.canEdit = false;
        MapArea.canAdd=false;
        MapArea.canRmv=true;
        MapArea.canAddLink=false;
        MapArea.canSave=false;
    }

    public static void toggleLink() {
        lastSelected=null;
        MapArea.canEdit = false;
        MapArea.canAdd=false;
        MapArea.canRmv=false;
        MapArea.canAddLink=true;
        MapArea.canSave=false;
    }
    public static void toggleSave() {
        MapArea.canEdit = false;
        MapArea.canAdd=false;
        MapArea.canRmv=false;
        MapArea.canAddLink=false;
        lastSelected = null;
        MapArea.canSave=true;
    }
    public MapArea(IStationNodePersistency stationNodePersistency) {
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(800, 600));
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.stationNodePersistency = stationNodePersistency;
        try {
            data = stationNodePersistency.getAll();
            atStartUp=new ArrayList<>(data);
            if(data==null){
                data = new ArrayList<>();

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        clicks = getListOfClicks(data);
            lastSelected = null;
            set=new HashSet<>();
        }

        public void updateData(){
            try {
                data = stationNodePersistency.getAll();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private String getCommonBuses(StationNode s1,StationNode s2){
            String msg="";
            List<String> bus1=s1.getBuslinesPassingThrough();
            List<String> bus2=s2.getBuslinesPassingThrough();
            Collections.sort(bus1);
            Collections.sort(bus2);
            List<String> visited=new ArrayList<>();
            for(String s:bus1){
                if(bus2.contains(s) && !visited.contains(s)){
                    msg+=s+",";
                    visited.add(s);
                }
            }
            return msg;
        }


        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            set=new HashSet<>();

            int opacity=255;

            g.drawString("x=" + moveX
                    + ", y=" + moveY , 10, 30);
            if(lastSelected!=null) {
                Point p = getOval(lastSelected.x, lastSelected.y);
                for (StationNode s : data) {
                    if (s.getApparentCoordinate().x == p.x && s.getApparentCoordinate().y == p.y) {
                        g.drawString("Last station selected:  " + s.getStationName(), 10, 40);
                    }
                }
            }

            for(StationNode n: data) {
                if(atStartUp.contains(n) && editable==true){
                    opacity=50;
                }else{
                    opacity=255;
                }
                Point c = n.getApparentCoordinate();
                g.setColor(new Color(0,0,0, opacity));
                g.drawOval((int)c.getX()-20, (int)c.getY()-20, 40, 40);
                g.fillOval((int)c.getX()-20, (int)c.getY()-20, 40, 40);
                //g.setColor(Color.GREEN);
                g.setColor(new Color(0,255,0, opacity));
                g.drawOval((int)c.getX()-16, (int)c.getY()-16, 32, 32);
                g.fillOval((int)c.getX()-16, (int)c.getY()-16, 32, 32);
                g.setColor(new Color(0,0,0, opacity));
                g.drawString(n.getStationName(),(int)c.getX()+25,(int)c.getY()+25);
            }

            for(StationNode stationNode:data) {
                if(atStartUp.contains(stationNode) && editable==true){
                    opacity=50;
                }else {
                    opacity=255;
                }
                Point c1 = stationNode.getApparentCoordinate();
                if(stationNode.getNeighbors()!=null) {
                    for (StationNode neighbor : stationNode.getNeighbors()) {
                        Point c2 = neighbor.getApparentCoordinate();
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(3));

                        if(currentBus!=null) {
                            if (stationNode.getBuslinesPassingThrough().contains(currentBus.substring(7)) && neighbor.getBuslinesPassingThrough().contains(currentBus.substring(7))) {
                                g.setColor(new Color(0,0,255, opacity));
                            } else {
                                g.setColor(new Color(255,0,0, opacity));
                            }
                        }else{
                            g.setColor(new Color(255,0,0, opacity));
                        }
                        if(editable==true && atStartUp.contains(stationNode) && atStartUp.contains(neighbor)){
                            opacity=50;
                            g.setColor(new Color(255,0,0, opacity));
                        }
                        g2.drawLine((int) c1.getX(), (int) c1.getY(), (int) c2.getX(), (int) c2.getY());
                        Point middle = new Point(((int) c1.getX() + (int) c2.getX()) / 2, ((int) c1.getY() + (int) c2.getY()) / 2);
                        if (!set.contains(middle)) {
                            String common = getCommonBuses(stationNode, neighbor);
                            if(busName!=null && !busName.equals("")&&common.contains(busName)){
                                opacity=255;
                            }else if(editable==true){
                                opacity=50;
                            }
                            g.setColor(new Color(0,0,0, opacity));
                            g2.drawString(common, (int) middle.getX(), (int) middle.getY());
                            set.add(middle);
                            try {
                                URL imageSrc = ((new File("busicon.png")).toURI()).toURL();
                                BufferedImage bi = ImageIO.read(imageSrc);

                                g.drawImage(bi, (int) middle.getX(), (int) middle.getY() + 10, 25, 25, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            this.repaint();
        }

    public String getCurrentBus() {
        return currentBus;
    }

    public void setCurrentBus(String currentBus) {
        this.currentBus = currentBus;
    }

    public static boolean checkInside(int x, int y, List<Point> list){
            for(Point c : list){
                if(x>= c.getX()-21 && x<=c.getX()+21 && y>= c.getY()-21 && y<=c.getY()+21)
                    return false;
            }
            return true;
        }
        public static List<Point> getListOfClicks(List<StationNode> data){
            List<Point> coordinates = new ArrayList<>();
            for(StationNode n:data)
                coordinates.add(n.getApparentCoordinate());
            return coordinates;
        }
        boolean perform_check(StationNode s1, StationNode s2){
            if(busName!=null && !busName.equals("")) {
                int count1=0;
                for (StationNode neighbor : s1.getNeighbors()) {
                    if (s1.getBuslinesPassingThrough().contains(busName)){
                        count1++;
                    }
                }
                if(count1>=2){
                    data.remove(s2);
                    JOptionPane.showMessageDialog(null,"Fork not allowed");
                    return false;
                }
                int count2=0;
                for (StationNode neighbor : s2.getNeighbors()) {
                    if (s2.getBuslinesPassingThrough().contains(busName)){
                        count2++;
                    }
                }
                if(count2>=2){
                    data.remove(s1);
                    JOptionPane.showMessageDialog(null,"Fork not allowed");
                    return false;
                }
            }
            return true;
        }
        public void mouseClicked(MouseEvent e) {
            clickX = e.getX();
            clickY = e.getY();
            clicks = getListOfClicks(data);
            if(checkInside(clickX,clickY,clicks) && canAdd) {
                new NodePopUp(data,clickX,clickY);
            }
            if(canAddLink){
                if(checkInside(clickX,clickY,clicks) == false) {
                    Point c = getOval(clickX, clickY);
                    if(lastSelected != null && c!=null && !lastSelected.equals(c)){
                        StationNode s1=null,s2=null;
                        for(StationNode stationNode : data){
                            if(stationNode.getApparentCoordinate().equals(c)){
                                s1 = stationNode;
                            }
                            if(stationNode.getApparentCoordinate().equals(lastSelected)){
                                s2 = stationNode;
                            }
                        }
                        if(s1!=null && s2!=null) {
                            boolean result=perform_check(s1,s2);
                            if(result==true) {
                                s1.addNeighbor(s2);
                                double xm = (c.getX() + lastSelected.getX()) / 2;
                                double ym = (c.getX() + lastSelected.getX()) / 2;
                                Point middle = new Point((int) xm, (int) ym);
                                //CREATION OF NODES AND NEIGHBORING RELATION
                                if (editable == true) {
                                    if (busName != null && !busName.equals("")) {
                                        s1.addBusline(busName);
                                        s2.addBusline(busName);
                                        wasSet = true;
                                        //update s1,s2 everywhere
                                    }
                                } else {
                                    new LinePopUp(s1, s2, (int) xm, (int) ym);
                                }
                            }
                        }
                    }
                    lastSelected = c;
                }
            }
            if(canRmv){
                Point c = getOval(clickX, clickY);
                if(c!=null) {
                    for (StationNode stationNode : data) {
                        if (stationNode.getApparentCoordinate().equals(c)){
                            stationNode.removeNeighbors();
                            data.remove(stationNode);
                            break;
                        }
                    }
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            moveX = e.getX();
            moveY = e.getY();
            this.repaint();
        }
        private double euclidianDistance(int x1,int x2,int y1,int y2){
            return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        }
        private Point getOval(int x,int y){
            double min=Double.MAX_VALUE;
            int minX=0, minY=0;
            clicks = getListOfClicks(data);
            for(Point c: clicks){
                if(min > euclidianDistance(x,(int)c.getX(),y,(int)c.getY())){
                    min = euclidianDistance(x,(int)c.getX(),y,(int)c.getY());
                    minX = (int)c.getX();
                    minY = (int)c.getY();
                }
            }
            return new Point(minX, minY);
        }


    public void mouseDragged (MouseEvent e) {}
        public void mouseEntered (MouseEvent e) {}
        public void mouseExited  (MouseEvent e) {}
        public void mousePressed (MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}

}
