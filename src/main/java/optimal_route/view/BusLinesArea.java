package optimal_route.view;
import optimal_route.contract.StationNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusLinesArea extends JPanel implements MouseMotionListener, MouseListener {
    private List<StationNode> drawData;
    private String currentBus;
    int x=0,y=0;
    private int X = 0;
    private int Y = 0;
    private static boolean canDrag = false;

    private Map<StationNode,Boolean> nodeToDrag;
    public void toggleCanDrag(){
        canDrag=true;
    }

    public BusLinesArea() {
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(800, 600));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        nodeToDrag=new HashMap<>();
    }

    public void setSelectedBus(String value){
        currentBus = value;
    }

    public void setDrawData(List<StationNode> drawData) {
        this.drawData = drawData;
        for(StationNode stationNode:drawData){
            nodeToDrag.put(stationNode,false);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(currentBus!=null) {
            for(StationNode station: drawData){
                if(station.getBuslinesPassingThrough().contains(currentBus.substring(7))) {
                    Point c = null;
                    if(nodeToDrag.get(station)==true){
                        System.err.println("Finally true");
                        c = new Point(X,Y);
                    }else {
                        c = station.getApparentCoordinate();
                    }
                    g.setColor(Color.BLACK);
                    g.drawOval((int)c.getX() - 20, (int)c.getY() - 20, 40, 40);
                    g.fillOval((int)c.getX() - 20, (int)c.getY() - 20, 40, 40);
                    g.setColor(Color.GREEN);
                    g.drawOval((int)c.getX() - 16, (int)c.getY() - 16, 32, 32);
                    g.fillOval((int)c.getX() - 16, (int)c.getY() - 16, 32, 32);
                    g.setColor(Color.BLACK);
                    g.drawString(station.getStationName(), (int)c.getX() + 25, (int)c.getY() + 25);
                }
            }
            g.setColor(Color.RED);
            for(StationNode stationNode: drawData) {
                Point2D c1 = stationNode.getApparentCoordinate();
                if(stationNode.getNeighbors()!=null) {
                    for (StationNode neighbor : stationNode.getNeighbors()) {
                        Point2D c2 = neighbor.getApparentCoordinate();
                        if (stationNode.getBuslinesPassingThrough().contains(currentBus.substring(7)) && neighbor.getBuslinesPassingThrough().contains(currentBus.substring(7))) {
                            g.setColor(Color.RED);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setStroke(new BasicStroke(3));
                            g2.drawLine((int) c1.getX(), (int) c1.getY(), (int) c2.getX(), (int) c2.getY());
                        }
                    }
                }
            }
        }
        this.repaint();
    }


    private double euclidianDistance(int x1,int x2,int y1,int y2){
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
    private Point getOval(int x,int y,List<Point> clicks){
        double min=Double.MAX_VALUE;
        int minX=0, minY=0;
        for(Point c: clicks){
            if(min > euclidianDistance(x,(int)c.getX(),y,(int)c.getY())){
                min = euclidianDistance(x,(int)c.getX(),y,(int)c.getY());
                minX = (int)c.getX();
                minY = (int)c.getY();
            }
        }
        return new Point(minX, minY);
    }



    @Override
    public void mouseDragged(MouseEvent e) {
        this.repaint();
        X = e.getX();
        Y = e.getY();
        if(canDrag) {
            X = e.getX();
            Y = e.getY();
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(drawData!=null){
            for(StationNode stationNode:drawData){
                nodeToDrag.put(stationNode,false);
            }
            List<Point> clicks = MapArea.getListOfClicks(drawData);
            Point p = getOval(e.getX(), e.getY(),clicks);
            System.err.println("Mouse was pressed at :"+p);
            for(StationNode stationNode:drawData) {
                if(stationNode.getApparentCoordinate()==p){
                    nodeToDrag.replace(stationNode,true);
                }
            }
        }else{
            System.err.println("DRAW DATA IS NULL");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.err.println("Mouse was released");
        if(drawData!=null) {
            for (StationNode stationNode : drawData) {
                nodeToDrag.replace(stationNode, false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

