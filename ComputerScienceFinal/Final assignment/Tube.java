import java.awt.image.*;
import java.util.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
public class Tube {
   private int xCoord;
   private BufferedImage image;
   private BufferedImage topImage;
   private static final int RECTWIDTH = 50;
   private int rectLength;
   private int gap;
   private int height;
   private boolean passed = false;
   /**
   * This constructs a Tube class
   * @param xCoord the xCoord of the tube
   * @param height the height of the tube
   * @param gap the gap between the tubes
   */
   public Tube(int xCoord,int height,int gap){
      this.xCoord = xCoord;
      this.height = height;
      this.gap = gap;
      this.rectLength = new Random().nextInt(height/2)+1;
      try{
         this.image = ImageIO.read(new File("FlappyTube.png"));
         this.topImage = ImageIO.read(new File("FlappyTubeTop.png"));
      }catch(Exception e){
         System.out.println("File not found");
      }
   }
   /**
   * Draws the tube in the DrawingPanel
   * @param g the Graphics object
   */
   public void drawTube(Graphics g){
      //Top tube
      Image topTube = this.topImage.getScaledInstance(RECTWIDTH,rectLength,Image.SCALE_SMOOTH);
      g.drawImage(topTube,xCoord,0,null);
      //Bottom tube
      Image bottomTube = this.image.getScaledInstance(RECTWIDTH,(height-rectLength-gap),Image.SCALE_SMOOTH);
      g.drawImage(bottomTube,xCoord,rectLength+gap,null);
   }
   /**
   * Gets a given x coordinate of the tube
   * @return the x coordinate of the tube
   */
   public int getX(){
      return this.xCoord;
   }
   /**
   * Sets the x coordinate of the tube
   * @param xCoord the new x coordinate of the tube
   */
   public void setX(int xCoord){
      this.xCoord = xCoord;
   }
   /**
   * Gets the width of the tube
   * @return the width of the tube
   */
   public int getWidth(){
      return RECTWIDTH;
   }
   /**
   * Gets the bounding box of the top tube
   * @return the Rectangle of the top bounding box
   */
   public Rectangle getTopBoundingBox(){
      return new Rectangle(xCoord,0,50,rectLength);
   }
   /**
   * Gets the bounding box of the bottom tube
   * @return the Rectangle of the bottom bounding box
   */
   public Rectangle getBottomBoundingBox(){
      return new Rectangle(xCoord,rectLength+gap,RECTWIDTH,(height-rectLength+10));
   }
   /**
   * Updates the tubes position
   */
   public void update(){
      this.xCoord -= 5;
   }
   /**
   * Returns whether the tube has passed the Bird
   * @return the passed boolean
   */
   public boolean hasPassed(){
      return passed;
   }
   /**
   * Sets the passed variable of the tube
   * @param passed the boolean of whether the tube has passed or not
   */
   public void setPassed(boolean passed){
      this.passed = passed;
   }
   
   

}
