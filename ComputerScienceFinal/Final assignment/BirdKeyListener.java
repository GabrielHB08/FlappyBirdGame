import java.awt.*;
import java.awt.event.*;

public class BirdKeyListener extends KeyAdapter {
   private Bird bird;
   private int velocity;

   /**
    * Constructs a KeyListener for the Bird
    * @param bird the bird object to control
    * @param velocityWrapper a one-element array to hold and mutate bird's velocity (since primitives are passed by value)
    */
   public BirdKeyListener(Bird bird) {
      this.bird = bird;
   }

   @Override
   public void keyPressed(KeyEvent event) {
      int code = event.getKeyCode();
      if (code == KeyEvent.VK_SPACE) {
         bird.jump();
      }
   }
}
