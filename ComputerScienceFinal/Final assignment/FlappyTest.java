import java.awt.*;
public class FlappyTest {
   public static void main(String [] args){
      Bird bird = new Bird(100,100);
      Tube tube = new Tube(200);
      DrawingPanel panel = new DrawingPanel(500,500);
      Graphics g = panel.getGraphics();
      bird.drawBird(g,bird.getX(),bird.getY());
      tube.drawTube(500,g,200);
   }
}