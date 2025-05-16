import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
public class Game implements Runnable{
    private Thread gameThread;
    private static final int MAX_TUBES = 5;
    private boolean running;
    private static final int FPS = 50;
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
    private static final Color lightBlue = new Color(145,215,216);
    private BirdKeyListener keyListener;
    private Graphics g;
    private static final Font scoreFont = new Font("Arial", Font.PLAIN,12);
    private static final Font gameOverFont = new Font("Arial", Font.BOLD, 50);
    private static final Font smallFont = new Font("Arial", Font.BOLD,25);
    private static final int MAX_VELOCITY = -50;
    private int bestScore;
    /**
    * Constructs a Game object with two parameters
    * @param panelWidth the width of the panel
    * @param panelHeight the height of the panel
    */
    public Game(int panelWidth, int panelHeight){
        this.bestScore = 0;
        this.tubes = new LinkedList<Tube>();
        this.targetTime = 1000 / FPS;
        this.bird = new Bird(100, 300);
        this.score = 0;
        this.gap = 150;
        this.tubeCount = 0;
        this.panel = new DrawingPanel(panelWidth, panelHeight);
        this.g = panel.getGraphics();
        panel.setBackground(lightBlue);
        this.keyListener = new BirdKeyListener(this.bird,this);
        this.panel.addKeyListener(this.keyListener); 
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.gameOver = false;
        this.spacer = 200;
        for(int i = 0; i < 6; ++i){
            this.tubes.add(new Tube(this.panelWidth+(this.spacer*i),this.panelHeight,this.gap));
        }
    }
    /**
    * Starts a Thread to run the game
    */
    public void startGame(){
        if(this.gameThread == null){
            this.gameThread = new Thread(this);
            this.running = true;
            this.gameThread.start();
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
    public void stopGame(Graphics g){
      if(this.bestScore < this.score){
         this.bestScore = this.score;
      }
      Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(new GradientPaint(150,250,Color.RED,450,250,Color.ORANGE));
      g2d.fillRect(0,0,panelWidth,panelHeight);
      g2d.setColor(Color.BLACK);
      g2d.setFont(gameOverFont);
      g2d.drawString("Game Over!",100,200);
      g.setFont(smallFont);
      g.drawString("Your score: " + this.score,150,250);
      g.drawString("Best score: " + this.bestScore,150,275);
      g.drawString("Press \"R\" to play again",100,300);
      running = false;
      try{
         this.gameThread.join();
      }catch(Exception e){
         System.out.println("Uh oh");
      }
    }
    public void resetGame(){
      panel.getGraphics().setColor(lightBlue);
      panel.getGraphics().fillRect(0,0,this.panelWidth,this.panelHeight);
      this.bird = new Bird(100,300);
      this.keyListener.setBird(this.bird);
      this.score = 0;
      this.gap = 150;
      this.tubeCount = 0;
      this.spacer = 200;
      this.tubes.clear();
      for(int i = 0; i < 6; ++i){
         this.tubes.add(new Tube(this.panelWidth + (this.spacer*i),this.panelHeight,this.gap));
      }
      this.gameOver = false;
      this.gameThread = new Thread(this);
      this.running = true;
      this.gameThread.start();
    }
    /**
    * Checks if the game is over
    * @return whether the game is over or not
    */
    public boolean isGameOver(){
      return this.gameOver;
    }
    /**
    * Updates the game by checking if the game should still be running
    */
    public void updateGame(){
        bird.update();
        Rectangle birdBoundingBox = bird.getBoundingBox();
        for(Tube tube : tubes){
            tube.update();
            if(birdBoundingBox.intersects(tube.getTopBoundingBox()) ||
                    birdBoundingBox.intersects(tube.getBottomBoundingBox()) ||
                    bird.getY() >= this.panelHeight){
                stopGame(this.g);
                return;
            }
            if(bird.getY() < 0){
               bird.setY(0);
               bird.setVelocityY(0);
            }
            if(bird.getVelocityY() < MAX_VELOCITY){
               bird.setVelocityY(MAX_VELOCITY);
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
        g.setFont(scoreFont);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
        bird.drawBird(g, bird.getX(), bird.getY());
        for(Tube tube : tubes){
            tube.drawTube(g);
        }
    }
}



