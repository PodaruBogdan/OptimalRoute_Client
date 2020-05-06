package optimal_route.view;

import javax.swing.*;
import java.awt.*;

public class DualView extends JPanel {
    private JPanel right;
    private JPanel left;
    public DualView(JPanel right, JPanel left) {
        this.left = left;
        this.right = right;
        this.setLayout(new FlowLayout());
        this.add(right);
        this.add(left);
    }
}
