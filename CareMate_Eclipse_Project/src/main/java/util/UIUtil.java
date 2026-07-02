package util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.*;
import java.time.format.*;

public class UIUtil {
    public static final Color BG = new Color(248, 244, 237);
    public static final Color CARD = Color.WHITE;
    public static final Color CREAM = new Color(255, 251, 244);
    public static final Color PRIMARY = new Color(238, 147, 88);
    public static final Color PRIMARY_DARK = new Color(202, 111, 54);
    public static final Color GREEN = new Color(74, 151, 120);
    public static final Color BLUE = new Color(80, 137, 191);
    public static final Color YELLOW = new Color(241, 184, 82);
    public static final Color RED = new Color(224, 82, 82);
    public static final Color DARK = new Color(50, 50, 50);
    public static final Color MUTED = new Color(132, 127, 118);
    public static final Color LINE = new Color(235, 226, 215);
    public static final Color NAV_BG = new Color(246,238,228);

    public static Font titleFont(){ return font(Font.BOLD, 30); }
    public static Font h2(){ return font(Font.BOLD, 22); }
    public static Font subTitleFont(){ return h2(); }
    public static Font bigFont(){ return h3(); }
    public static Font h3(){ return font(Font.BOLD, 18); }
    public static Font bodyFont(){ return font(Font.PLAIN, 15); }
    public static Font smallFont(){ return font(Font.PLAIN, 13); }
    public static Font tabFont(){ return font(Font.BOLD, 11); }
    public static Font font(int style, int size){ return new Font("Microsoft JhengHei", style, size); }

    public static class RoundPanel extends JPanel {
        private int radius; private Color bg; private boolean shadow;
        public RoundPanel(int radius, Color bg){ this(radius,bg,true); }
        public RoundPanel(int radius, Color bg, boolean shadow){ super(null); this.radius=radius; this.bg=bg; this.shadow=shadow; setOpaque(false); }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(shadow){ g2.setColor(new Color(0,0,0,16)); g2.fillRoundRect(5,7,getWidth()-8,getHeight()-10,radius,radius); }
            g2.setColor(bg); g2.fillRoundRect(0,0,getWidth()-8,getHeight()-10,radius,radius);
            g2.dispose(); super.paintComponent(g);
        }
    }
    public static class RoundButton extends JButton {
        private Color normal, hover, press; private int radius=22;
        public RoundButton(String text, Color bg, Color fg){ super(text); normal=bg; hover=blend(bg, Color.WHITE, .18f); press=bg.darker(); setBackground(bg); setForeground(fg); setFont(h3()); setFocusPainted(false); setBorder(new EmptyBorder(8,16,8,16)); setContentAreaFilled(false); setOpaque(false); setCursor(new Cursor(Cursor.HAND_CURSOR)); addMouseListener(new MouseAdapter(){ public void mouseEntered(MouseEvent e){setBackground(hover);} public void mouseExited(MouseEvent e){setBackground(normal);} public void mousePressed(MouseEvent e){setBackground(press);} public void mouseReleased(MouseEvent e){setBackground(hover);} }); }
        protected void paintComponent(Graphics g){ Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius); g2.dispose(); super.paintComponent(g); }
    }

    public static class LineIcon implements Icon {
        private final String type; private final int size; private final Color color;
        public LineIcon(String type, int size, Color color){ this.type=type; this.size=size; this.color=color; }
        public int getIconWidth(){ return size; }
        public int getIconHeight(){ return size; }
        public void paintIcon(Component c, Graphics g, int x, int y){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(Math.max(2f,size/16f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(color);
            double s=size, ox=x, oy=y;
            switch(type){
                case "health": drawHeart(g2,ox,oy,s); break;
                case "medicine": drawCapsule(g2,ox,oy,s); break;
                case "meal": drawMeal(g2,ox,oy,s); break;
                case "mood": drawMood(g2,ox,oy,s); break;
                case "task": drawTask(g2,ox,oy,s); break;
                case "report": drawReport(g2,ox,oy,s); break;
                case "contact": drawContact(g2,ox,oy,s); break;
                case "account": drawUser(g2,ox,oy,s); break;
                case "sos": drawSOS(g2,ox,oy,s); break;
                case "translate": drawTranslate(g2,ox,oy,s); break;
                case "call": drawCall(g2,ox,oy,s); break;
                case "log": drawLog(g2,ox,oy,s); break;
                case "home": drawHome(g2,ox,oy,s); break;
                case "plus": drawPlus(g2,ox,oy,s); break;
                case "bell": drawBell(g2,ox,oy,s); break;
                case "back": drawBack(g2,ox,oy,s); break;
                case "menu": drawMenu(g2,ox,oy,s); break;
                default: drawSpark(g2,ox,oy,s); break;
            }
            g2.dispose();
        }
        private void drawHeart(Graphics2D g,double x,double y,double s){ Path2D p=new Path2D.Double(); p.moveTo(x+s*.50,y+s*.78); p.curveTo(x+s*.12,y+s*.52,x+s*.10,y+s*.28,x+s*.31,y+s*.22); p.curveTo(x+s*.43,y+s*.18,x+s*.50,y+s*.28,x+s*.50,y+s*.34); p.curveTo(x+s*.50,y+s*.28,x+s*.57,y+s*.18,x+s*.69,y+s*.22); p.curveTo(x+s*.90,y+s*.28,x+s*.88,y+s*.52,x+s*.50,y+s*.78); g.draw(p); }
        private void drawCapsule(Graphics2D g,double x,double y,double s){ g.rotate(-Math.PI/4,x+s/2,y+s/2); RoundRectangle2D r=new RoundRectangle2D.Double(x+s*.22,y+s*.34,s*.56,s*.28,s*.28,s*.28); g.draw(r); g.drawLine((int)(x+s*.50),(int)(y+s*.34),(int)(x+s*.50),(int)(y+s*.62)); g.rotate(Math.PI/4,x+s/2,y+s/2); }
        private void drawMeal(Graphics2D g,double x,double y,double s){ g.drawLine((int)(x+s*.28),(int)(y+s*.18),(int)(x+s*.28),(int)(y+s*.78)); g.drawLine((int)(x+s*.22),(int)(y+s*.18),(int)(x+s*.22),(int)(y+s*.38)); g.drawLine((int)(x+s*.34),(int)(y+s*.18),(int)(x+s*.34),(int)(y+s*.38)); g.drawArc((int)(x+s*.18),(int)(y+s*.32),(int)(s*.20),(int)(s*.18),180,180); g.drawArc((int)(x+s*.55),(int)(y+s*.18),(int)(s*.18),(int)(s*.42),90,180); g.drawLine((int)(x+s*.64),(int)(y+s*.58),(int)(x+s*.64),(int)(y+s*.78)); }
        private void drawMood(Graphics2D g,double x,double y,double s){ g.drawOval((int)(x+s*.20),(int)(y+s*.20),(int)(s*.60),(int)(s*.60)); g.fillOval((int)(x+s*.38),(int)(y+s*.42),(int)(s*.05),(int)(s*.05)); g.fillOval((int)(x+s*.58),(int)(y+s*.42),(int)(s*.05),(int)(s*.05)); g.drawArc((int)(x+s*.38),(int)(y+s*.48),(int)(s*.24),(int)(s*.18),200,140); }
        private void drawTask(Graphics2D g,double x,double y,double s){ g.drawRoundRect((int)(x+s*.25),(int)(y+s*.18),(int)(s*.50),(int)(s*.64),(int)(s*.08),(int)(s*.08)); g.drawLine((int)(x+s*.36),(int)(y+s*.36),(int)(x+s*.64),(int)(y+s*.36)); g.drawLine((int)(x+s*.36),(int)(y+s*.50),(int)(x+s*.64),(int)(y+s*.50)); g.drawLine((int)(x+s*.36),(int)(y+s*.64),(int)(x+s*.56),(int)(y+s*.64)); }
        private void drawReport(Graphics2D g,double x,double y,double s){ g.drawLine((int)(x+s*.22),(int)(y+s*.76),(int)(x+s*.78),(int)(y+s*.76)); g.drawLine((int)(x+s*.26),(int)(y+s*.66),(int)(x+s*.26),(int)(y+s*.50)); g.drawLine((int)(x+s*.44),(int)(y+s*.66),(int)(x+s*.44),(int)(y+s*.36)); g.drawLine((int)(x+s*.62),(int)(y+s*.66),(int)(x+s*.62),(int)(y+s*.24)); }
        private void drawContact(Graphics2D g,double x,double y,double s){ drawUser(g,x,y,s); g.drawArc((int)(x+s*.14),(int)(y+s*.32),(int)(s*.72),(int)(s*.48),215,110); }
        private void drawUser(Graphics2D g,double x,double y,double s){ g.drawOval((int)(x+s*.37),(int)(y+s*.20),(int)(s*.26),(int)(s*.26)); g.drawArc((int)(x+s*.25),(int)(y+s*.48),(int)(s*.50),(int)(s*.40),20,140); }
        private void drawSOS(Graphics2D g,double x,double y,double s){ g.drawOval((int)(x+s*.18),(int)(y+s*.18),(int)(s*.64),(int)(s*.64)); g.drawLine((int)(x+s*.50),(int)(y+s*.32),(int)(x+s*.50),(int)(y+s*.56)); g.fillOval((int)(x+s*.47),(int)(y+s*.64),(int)(s*.06),(int)(s*.06)); }
        private void drawTranslate(Graphics2D g,double x,double y,double s){ g.drawRoundRect((int)(x+s*.18),(int)(y+s*.24),(int)(s*.44),(int)(s*.34),(int)(s*.08),(int)(s*.08)); g.drawRoundRect((int)(x+s*.38),(int)(y+s*.42),(int)(s*.44),(int)(s*.34),(int)(s*.08),(int)(s*.08)); g.drawLine((int)(x+s*.30),(int)(y+s*.42),(int)(x+s*.50),(int)(y+s*.42)); g.drawLine((int)(x+s*.40),(int)(y+s*.32),(int)(x+s*.40),(int)(y+s*.52)); }
        private void drawCall(Graphics2D g,double x,double y,double s){ Path2D p=new Path2D.Double(); p.moveTo(x+s*.28,y+s*.24); p.curveTo(x+s*.18,y+s*.36,x+s*.28,y+s*.58,x+s*.48,y+s*.72); p.curveTo(x+s*.62,y+s*.82,x+s*.74,y+s*.72,x+s*.76,y+s*.62); p.lineTo(x+s*.62,y+s*.54); p.lineTo(x+s*.54,y+s*.64); p.curveTo(x+s*.44,y+s*.58,x+s*.38,y+s*.50,x+s*.34,y+s*.40); p.lineTo(x+s*.44,y+s*.32); p.closePath(); g.draw(p); }
        private void drawLog(Graphics2D g,double x,double y,double s){ g.drawRoundRect((int)(x+s*.24),(int)(y+s*.18),(int)(s*.52),(int)(s*.64),(int)(s*.08),(int)(s*.08)); g.drawLine((int)(x+s*.36),(int)(y+s*.34),(int)(x+s*.64),(int)(y+s*.34)); g.drawLine((int)(x+s*.36),(int)(y+s*.48),(int)(x+s*.64),(int)(y+s*.48)); g.drawLine((int)(x+s*.36),(int)(y+s*.62),(int)(x+s*.58),(int)(y+s*.62)); }
        private void drawHome(Graphics2D g,double x,double y,double s){ Path2D p=new Path2D.Double(); p.moveTo(x+s*.22,y+s*.48); p.lineTo(x+s*.50,y+s*.24); p.lineTo(x+s*.78,y+s*.48); p.moveTo(x+s*.30,y+s*.46); p.lineTo(x+s*.30,y+s*.76); p.lineTo(x+s*.70,y+s*.76); p.lineTo(x+s*.70,y+s*.46); g.draw(p); }
        private void drawPlus(Graphics2D g,double x,double y,double s){ g.drawLine((int)(x+s*.50),(int)(y+s*.24),(int)(x+s*.50),(int)(y+s*.76)); g.drawLine((int)(x+s*.24),(int)(y+s*.50),(int)(x+s*.76),(int)(y+s*.50)); }
        private void drawBell(Graphics2D g,double x,double y,double s){ g.drawArc((int)(x+s*.30),(int)(y+s*.22),(int)(s*.40),(int)(s*.50),0,180); g.drawLine((int)(x+s*.30),(int)(y+s*.48),(int)(x+s*.26),(int)(y+s*.66)); g.drawLine((int)(x+s*.70),(int)(y+s*.48),(int)(x+s*.74),(int)(y+s*.66)); g.drawLine((int)(x+s*.26),(int)(y+s*.66),(int)(x+s*.74),(int)(y+s*.66)); g.drawArc((int)(x+s*.42),(int)(y+s*.66),(int)(s*.16),(int)(s*.12),180,180); }
        private void drawBack(Graphics2D g,double x,double y,double s){ g.drawLine((int)(x+s*.62),(int)(y+s*.24),(int)(x+s*.34),(int)(y+s*.50)); g.drawLine((int)(x+s*.34),(int)(y+s*.50),(int)(x+s*.62),(int)(y+s*.76)); }
        private void drawMenu(Graphics2D g,double x,double y,double s){ g.fillOval((int)(x+s*.24),(int)(y+s*.47),(int)(s*.07),(int)(s*.07)); g.fillOval((int)(x+s*.47),(int)(y+s*.47),(int)(s*.07),(int)(s*.07)); g.fillOval((int)(x+s*.70),(int)(y+s*.47),(int)(s*.07),(int)(s*.07)); }
        private void drawSpark(Graphics2D g,double x,double y,double s){ g.drawOval((int)(x+s*.28),(int)(y+s*.28),(int)(s*.44),(int)(s*.44)); g.drawLine((int)(x+s*.50),(int)(y+s*.20),(int)(x+s*.50),(int)(y+s*.80)); }
    }

    public static JLabel icon(String type, int size, Color color){ return new JLabel(new LineIcon(type,size,color), SwingConstants.CENTER); }
    public static JButton iconGhost(String type, int size){ JButton b=ghostBtn(""); b.setIcon(new LineIcon(type,size,DARK)); return b; }
    public static Color blend(Color a, Color b, float ratio){ int r=(int)(a.getRed()*(1-ratio)+b.getRed()*ratio); int g=(int)(a.getGreen()*(1-ratio)+b.getGreen()*ratio); int bl=(int)(a.getBlue()*(1-ratio)+b.getBlue()*ratio); return new Color(r,g,bl); }

    public static JPanel card(){ return new RoundPanel(28, CARD); }
    public static JPanel softCard(){ return new RoundPanel(30, CREAM); }
    public static JPanel navCard(){ return new RoundPanel(18, NAV_BG, false); }
    public static JButton btn(String text){ return new RoundButton(text, PRIMARY, Color.WHITE); }
    public static JButton outlineBtn(String text){ RoundButton b=new RoundButton(text, Color.WHITE, PRIMARY_DARK); b.setBorder(new CompoundBorder(new LineBorder(PRIMARY,1,true), new EmptyBorder(8,16,8,16))); return b; }
    public static JButton dangerBtn(String text){ return new RoundButton(text, RED, Color.WHITE); }
    public static JButton ghostBtn(String text){ RoundButton b=new RoundButton(text, NAV_BG, DARK); b.setFont(bodyFont()); return b; }

    public static JLabel title(String t){ JLabel l=new JLabel(t); l.setFont(titleFont()); l.setForeground(DARK); return l; }
    public static JLabel label(String t){ return h3Label(t); }
    public static JLabel h2Label(String t){ JLabel l=new JLabel(t); l.setFont(h2()); l.setForeground(DARK); return l; }
    public static JLabel h3Label(String t){ JLabel l=new JLabel(t); l.setFont(h3()); l.setForeground(DARK); return l; }
    public static JLabel hint(String t){ JLabel l=new JLabel(t); l.setFont(smallFont()); l.setForeground(MUTED); return l; }

    public static JTextField input(){ JTextField t=new JTextField(); t.setFont(bodyFont()); t.setForeground(DARK); t.setBackground(Color.WHITE); t.setBorder(new CompoundBorder(new LineBorder(LINE,1,true), new EmptyBorder(8,12,8,12))); return t; }
    public static JPasswordField passwordInput(){ JPasswordField t=new JPasswordField(); t.setFont(bodyFont()); t.setBorder(new CompoundBorder(new LineBorder(LINE,1,true), new EmptyBorder(8,12,8,12))); return t; }
    public static void styleCombo(JComboBox<?> box){ box.setFont(bodyFont()); box.setBackground(Color.WHITE); box.setBorder(new LineBorder(LINE,1,true)); }
    public static void styleTable(JTable table){ table.setFont(bodyFont()); table.setRowHeight(38); table.setShowVerticalLines(false); table.setGridColor(new Color(244,238,230)); table.setSelectionBackground(new Color(255,233,214)); table.setSelectionForeground(DARK); JTableHeader h=table.getTableHeader(); h.setFont(h3()); h.setBackground(new Color(255,243,232)); h.setForeground(DARK); }
    public static JLabel clockLabel(){ JLabel l=hint(""); Timer t=new Timer(1000,e->l.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")))); t.start(); return l; }
    public static void error(Component c, Exception e){ JOptionPane.showMessageDialog(c, e.getMessage(), "CareMate 提醒", JOptionPane.ERROR_MESSAGE); }
    public static void info(Component c, String m){ JOptionPane.showMessageDialog(c, m, "CareMate", JOptionPane.INFORMATION_MESSAGE); }
}
