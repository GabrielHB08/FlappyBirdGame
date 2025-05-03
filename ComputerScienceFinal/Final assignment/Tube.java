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
   public void drawTube(Graphics g){
      //Top tube
      Image topTube = this.topImage.getScaledInstance(RECTWIDTH,rectLength,Image.SCALE_SMOOTH);
      g.drawImage(topTube,xCoord,0,null);
      //Bottom tube
      Image bottomTube = this.image.getScaledInstance(RECTWIDTH,(height-rectLength-gap),Image.SCALE_SMOOTH);
      g.drawImage(bottomTube,xCoord,rectLength+gap,null);
   }
   public int getX(){
      return this.xCoord;
   }
   public void setX(int xCoord){
      this.xCoord = xCoord;
   }
   public int getWidth(){
      return RECTWIDTH;
   }
   public Rectangle getTopBoundingBox(){
      return new Rectangle(xCoord,0,50,rectLength);
   }
   public Rectangle getBottomBoundingBox(){
      return new Rectangle(xCoord,rectLength+gap,RECTWIDTH,(height-rectLength));
   }
   public void update(){
      this.xCoord -= 5;
   }
   public boolean hasPassed(){
      return passed;
   }
   public void setPassed(boolean passed){
      this.passed = passed;
   }
   
   

}