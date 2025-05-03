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
   private static final int GRAVITY = 3;
   private static final int JUMP_STRENGTH = -15;
   public Bird(int xCoord, int yCoord){
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      try{
         this.image = ImageIO.read(new File("FlappyBird.png"));
      }catch(Exception e){
         System.out.println("File not found");
      }
   }
   public void drawBird(Graphics g,int xCoord,int yCoord){
      Image resized = this.image.getScaledInstance(50,50, Image.SCALE_SMOOTH);
      g.drawImage(resized,xCoord,yCoord,null);
     
   }
   public int getX(){
      return this.xCoord;
   }
   public int getY(){
      return this.yCoord;
   }
   public void setX(int xCoord){
      this.xCoord = xCoord;
   }
   public void setY(int yCoord){
      this.yCoord = yCoord;
   }
   public Rectangle getBoundingBox(){
      return new Rectangle(xCoord,yCoord,50,50);
   }
   public void update(){
      velocityY += GRAVITY;
      yCoord += velocityY;
   }
   public void jump(){
      velocityY += JUMP_STRENGTH;
   }
}