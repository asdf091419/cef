package tests.detailed;



import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class ModelFrame  extends JFrame  implements MouseMotionListener , MouseListener {

    private static  ModelFrame instance=null;
    public static  ModelFrame getInstance(){
        if(instance==null){
            instance=new ModelFrame();
        }
        return instance;
    }

    int x,y=0;

    Double rate_X=0D;
    Double rate_Y=0D;

    public void setRateX(Double rate) {
        this.rate_X = rate;
    }
    public void setRateY(Double rate) {
        this.rate_Y = rate;
    }

    public ModelFrame() throws HeadlessException {
        setUndecorated(true);
        setBackground(new Color(0,0,0, 1));
//        AWTUtilities.setWindowOpacity(this, 0.1f);
        panel.setBounds(0,0,this.getWidth(),this.getHeight());
        panel.setBackground(new Color(255, 255, 255, 1));
        add(panel);
    }
    Integer drawX=0,drawY=0;
    Integer lastX=0,lastY=0;

    List<Integer> pointX=null;
    List<Integer> pointY=null;
    public void drawLine(Integer X,Integer Y){
        pointX.add(X);
        pointY.add(Y);
    }

    JPanel panel=new JPanel(){

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d=(Graphics2D)g;
            if(pointX!=null) {
                int pX[]=new int[pointX.size()];
                for (int i = 0; i < pointX.size(); i++) {
                    pX[i] = pointX.get(i)*rate_X.intValue();

                }

                int py[] = new int[pointY.size()];

                for (int i = 0; i < pointY.size(); i++) {
                    py[i] = pointY.get(i)*rate_Y.intValue();//Integer.valueOf(""+pointY.get(i)*rate_Y);
                }
                BasicStroke bs=new BasicStroke(20,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
                g2d.setStroke(bs);
                g2d.setColor(new Color(239, 12, 12,255));
                g2d.drawPolyline(pX, py, pointX.size());
            }
        }
    };

    public void StartdrawLine(Integer X,Integer Y){
        pointX=new ArrayList<Integer>();
        pointX.add(X);
        pointY=new ArrayList<Integer>();
        pointY.add(Y);
    }

    public void EnddrawLine(Integer X,Integer Y){
        pointX.add(X);
        pointY.add(Y);
        panel.repaint();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        g.setColor(new Color(239, 12, 12));
//        g.drawLine(0,0,200,200);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dispose();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
