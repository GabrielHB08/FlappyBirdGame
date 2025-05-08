import java.awt.image.*;
import java.util.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
public class Bird {
   private int xCoord;
   private int yCoord;
   private BufferedImage image;
   private int velocityY = 0;
   private static final int GRAVITY = 4;
   private static final int JUMP_STRENGTH = -18;
   private static final int BIRD_SIZE = 50;
   /**
   * Constructs a Bird object with two parameters
   * @param xCoord the initial x coordinate of the Bird
   * @param yCoord the initial y coordinate of the Bird
   */
   public Bird(int xCoord, int yCoord){
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      try{
         this.image = ImageIO.read(new File("FlappyBird.png"));
      }catch(Exception e){
         System.out.println("File not found");
      }
   }
   /**
   * Draws the Bird to the DrawingPanel
   * @param g the Graphics object so you can draw to the DrawingPanel
   * @param xCoord the given x coordinate of the Bird
   * @param yCoord the given y coordinate of the Bird
   */
   public void drawBird(Graphics g,int xCoord,int yCoord){
      Image resized = this.image.getScaledInstance(50,50, Image.SCALE_SMOOTH);
      g.drawImage(resized,xCoord,yCoord,null);
     
   }
   /**
   * Retrieves the x coordinate of the Bird
   * @return the x coordinate of the Bird
   */
   public int getX(){
      return this.xCoord;
   }
   /**
   * Retrieves the y coordinate of the Bird
   * @return the y coordinate of the Bird
   */
   public int getY(){
      return this.yCoord;
   }
   /**
   * Sets the x coordinate of the Bird
   * @param xCoord the new x coordinate of the Bird
   */
   public void setX(int xCoord){
      this.xCoord = xCoord;
   }
   /**
   * Sets the y coordinate of the Bird
   * @param yCoord the new y coordinate of the Bird
   */
   public void setY(int yCoord){
      this.yCoord = yCoord;
   }
   /**
   * Retrieves the bounding box of the Bird
   * @return the bounding box of the Bird
   */
   public Rectangle getBoundingBox(){
      return new Rectangle(xCoord,yCoord,30,30);
   }
   /**
   * Updates the Bird velocity and y coordinate
   */
   public void update(){
      velocityY += GRAVITY;
      yCoord += velocityY;
   }
   /**
   * Makes the Bird "jump"
   */
   public void jump(){
      velocityY += JUMP_STRENGTH;
   }
   /**
   * Gets the Bird size
   * @return the Bird size
   */
   public int getBirdSize(){
      return BIRD_SIZE;
   }
}
