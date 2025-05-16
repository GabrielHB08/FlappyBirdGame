import java.util.*;
public class FlappyBird {
   /**
   * Runs the FlappyBird Game 
   * @param args console input
   */
   public static void main(String [] args){
      Game game = new Game(500,500); 
      game.startGame();
   }
}