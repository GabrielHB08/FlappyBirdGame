import java.util.*;
public class FlappyBird {
   public static void main(String [] args){
      Game game = new Game(500,500);
      Scanner console = new Scanner(System.in);
      game.startGame();
   }
}