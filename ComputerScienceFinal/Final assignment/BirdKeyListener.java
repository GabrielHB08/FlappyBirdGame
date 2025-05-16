import java.awt.*;
import java.awt.event.*;

public class BirdKeyListener extends KeyAdapter {
   private Bird bird;
   private Game game;

   /**
    * Constructs a KeyListener for the Bird
    * @param bird the bird object to control
    * @param velocityWrapper a one-element array to hold and mutate bird's velocity (since primitives are passed by value)
    */
   public BirdKeyListener(Bird bird,Game game) {
      this.bird = bird;
      this.game = game;
   }

   @Override
   public void keyPressed(KeyEvent event) {
      int code = event.getKeyCode();
      if (code == KeyEvent.VK_SPACE) {
         bird.jump();
      }else if(code == KeyEvent.VK_R && !this.game.isGameOver()){
         game.resetGame();
      }
   }
   /**
   * Sets the bird so that restarts work
   * @param bird the new Bird object
   */
   public void setBird(Bird bird){
      this.bird = bird;
   }
}
