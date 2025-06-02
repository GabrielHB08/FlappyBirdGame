import java.awt.image.*;
import java.util.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
public class Bird {
   private int xCoord;
   private int yCoord;
   private static BufferedImage image;
   private double velocityY = 0;
   /** this sets up the gravity for the game */
   public static final double GRAVITY = 0.8;
   /** this sets up the jump strength of the bird */
   public static final int JUMP_STRENGTH = -16;
   /** this sets up the bird size */
   public static final int BIRD_SIZE = 50;
   private Rectangle boundingBox;
   private Image scaledImage;
   /** this sets up the max velocity that the bird can go to */
   public static final int MAX_VELOCITY = -15;
   static {
      try {
         image = ImageIO.read(new File("FlappyBird.png"));
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   /**
   * Constructs a Bird object with two parameters
   * @param xCoord the initial x coordinate of the Bird
   * @param yCoord the initial y coordinate of the Bird
   */
   public Bird(int xCoord, int yCoord){
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      this.scaledImage = image.getScaledInstance(BIRD_SIZE,BIRD_SIZE, Image.SCALE_SMOOTH);
      //Bounding box is slightly smaller so the user has an easier time navigating the game. You're welcome.
      this.boundingBox = new Rectangle(xCoord,yCoord,BIRD_SIZE-10,BIRD_SIZE-10);
   }
   /**
   * Draws the Bird to the DrawingPanel
   * @param g the Graphics object so you can draw to the DrawingPanel
   * @param xCoord the given x coordinate of the Bird
   * @param yCoord the given y coordinate of the Bird
   */
   public void drawBird(Graphics g,int xCoord,int yCoord){
      g.drawImage(scaledImage,xCoord,yCoord,null);
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
      return this.boundingBox;
   }
   /**
   * Updates the bounding box to the bird's current coordinates
   */
   public void updateBoundingBox(){
      this.boundingBox.setLocation(xCoord,yCoord);
   }
   /**
   * Updates the Bird velocity and y coordinate
   */
   public void update(){
      velocityY += GRAVITY;
      if(velocityY < MAX_VELOCITY){
         velocityY = MAX_VELOCITY;
      }
      yCoord += velocityY;
      updateBoundingBox();
   }
   /**
   * Makes the Bird "jump"
   */
   public void jump(){
      velocityY += JUMP_STRENGTH;
   }
   /**
   * Gets velocityY
   * @return the y velocity of the Bird
   */
   public double getVelocityY(){
      return this.velocityY;
   }
   /**
   * Sets the y velocity of the Bird
   * @param velocityY the y velocity of the bird
   */
   public void setVelocityY(int velocityY){
      this.velocityY = velocityY;
   }
}
