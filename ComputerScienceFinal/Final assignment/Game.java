import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
public class Game implements Runnable{
    private Thread gameThread;
    private static final int MAX_TUBES = 5;
    private boolean running;
    private static final int FPS = 60;
    private long targetTime;
    private Deque<Tube> tubes;
    private Bird bird;
    private int score;
    private static final int GAP_DECREASE_INTERVAL = 5;
    private int tubeCount;
    private int gap;
    private static final int MIN_GAP = 50;
    private int panelWidth;
    private int panelHeight;
    private DrawingPanel panel;
    private boolean gameOver;
    private int spacer;
    private Color lightBlue;
    /**
    * Constructs a Game object with two parameters
    * @param panelWidth the width of the panel
    * @param panelHeight the height of the panel
    */
    public Game(int panelWidth, int panelHeight){
        this.tubes = new LinkedList<Tube>();
        this.targetTime = 10000 / FPS;
        this.bird = new Bird(100, 300);
        this.score = 0;
        this.gap = 100;
        this.tubeCount = 0;
        this.panel = new DrawingPanel(panelWidth, panelHeight);
        this.lightBlue = new Color(145,215,216);
        panel.setBackground(lightBlue);
        this.panel.addKeyListener(new BirdKeyListener(this.bird)); 
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.gameOver = false;
        this.spacer = 200;
        for(int i = 0; i < 6; i++){
            this.tubes.add(new Tube(this.panelWidth+(this.spacer*i),this.panelHeight,this.gap));
        }
    }
    /**
    * Starts a Thread to run the game
    */
    public void startGame(){
        if(this.gameThread == null){
            this.gameThread = new Thread(this);
            running = true;
            gameThread.start();
        }
    }

    
    @Override
    public void run(){
        Graphics g = panel.getGraphics();
        while(running){
            g.setColor(this.lightBlue);
            for(Tube a: tubes){
               g.fillRect(a.getX(),0,a.getWidth(),this.panelHeight);
            }
            g.fillRect(bird.getX(),bird.getY(),bird.getBirdSize(),bird.getBirdSize());
            updateGame();
            renderGame(g); 
            try{
                Thread.sleep(this.targetTime);
            }catch(InterruptedException e){
                System.out.println("Uh oh");
            }
        }
    }
    /**
    * Stops the game
    */
    public void stopGame(){
      System.out.println("Game over!");
      System.out.println("Score: " + this.score);
      running = false;
      try{
         this.gameThread.join();
      }catch(Exception e){
         System.out.println("Uh oh");
      }
    }
    /**
    * Updates the game by checking if the game should still be running
    */
    public void updateGame(){
        bird.update();
        for(Tube tube : tubes){
            tube.update();
            if(bird.getBoundingBox().intersects(tube.getTopBoundingBox()) ||
                    bird.getBoundingBox().intersects(tube.getBottomBoundingBox()) ||
                    bird.getY() >= this.panelHeight){
                stopGame();
                return;
            }
            if(bird.getX() > tube.getX() + tube.getWidth() && !tube.hasPassed()){
                this.score++;
                tube.setPassed(true);
                this.tubeCount++;
                if(tubeCount % GAP_DECREASE_INTERVAL == 0 && gap > MIN_GAP){
                    this.gap -= 5;
                    this.spacer -= 20;
                }
            }
        }
        tubes.removeIf(t -> t.getX() + t.getWidth() < 0);
        if(tubes.size() < MAX_TUBES){
            addNewTube();
        }
    }
    /**
    * Adds a new tube to the ArrayList object
    */
    private void addNewTube(){
        Tube lastTube = this.tubes.peekLast();
        int lastXCoord = (lastTube != null) ? lastTube.getX() : panelWidth;
        tubes.add(new Tube(lastXCoord + this.spacer, this.panelHeight, this.gap));
    }
    /**
    * Renders the game by drawing objects to the DrawingPanel
    * @param g a Graphics object so you can draw
    */
    private void renderGame(Graphics g){
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
        bird.drawBird(g, bird.getX(), bird.getY());
        for(Tube tube : tubes){
            tube.drawTube(g);
        }
    }
}



