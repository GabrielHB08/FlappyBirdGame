import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
public class Game implements Runnable{
    private Thread gameThread;
    /** this sets up the maximum amount of tubes that can be in the tubes Deque */
    public static final int MAX_TUBES = 5;
    private boolean running;
    /** this sets up the FPS of the game */
    public static final int FPS = 50;
    private long targetTime;
    private Deque<Tube> tubes;
    private Bird bird;
    private int score;
    /** this sets up the interval over which the gap between tubes will decrease */
    public static final int GAP_DECREASE_INTERVAL = 5;
    private int tubeCount;
    private int gap;
    /** this sets up the minimum gap between tubes to ensure user has a good time. You're welcome. */
    public static final int MIN_GAP = 55;
    /** this sets up the panel's width */
    public static final int PANELWIDTH = 500;
    /** this sets up the panel's height */
    public static final int PANELHEIGHT = 500;
    private DrawingPanel panel;
    private boolean gameOver;
    private int spacer;
    /** this sets up a light blue color */
    public static final Color LIGHT_BLUE = new Color(145,215,216);
    private BirdKeyListener keyListener;
    /** this gives the parameters to display the score once the game is over */
    public static final Font SCORE_FONT = new Font("Arial", Font.PLAIN,12);
    /** this gives the parameters to display the "Game Over" text once the game is over */
    public static final Font GAME_OVER_FONT = new Font("Arial", Font.BOLD, 50);
    /** this gives the parameters for the small text once the game is over */
    public static final Font SMALL_FONT = new Font("Arial", Font.BOLD,25);
    /** this sets up the high score file. This will always exist */
    public static final File HIGH_SCORE_FILE = new File("HighScore.txt");
    private int bestScore;
    /**
    * Constructs a Game object with two parameters
    */
    public Game(){
        loadHighScore();
        this.tubes = new ArrayDeque<Tube>();
        this.targetTime = 1000 / FPS;
        this.bird = new Bird(100, 300);
        this.score = 0;
        this.gap = 150;
        this.tubeCount = 0;
        this.panel = new DrawingPanel(PANELWIDTH, PANELHEIGHT);
        panel.setBackground(LIGHT_BLUE);
        this.keyListener = new BirdKeyListener(this.bird,this);
        this.panel.addKeyListener(this.keyListener); 
        this.gameOver = false;
        this.spacer = 250;
        for(int i = 0; i < 6; ++i){
            this.tubes.add(new Tube(this.PANELWIDTH+(this.spacer*i),this.PANELHEIGHT,this.gap));
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
        g.setColor(this.LIGHT_BLUE);
        while(running){
            for(Tube tube: tubes){
               g.fillRect(tube.getX(),0,tube.RECTWIDTH,this.PANELHEIGHT);
            }
            g.fillRect(bird.getX(),bird.getY(),bird.BIRD_SIZE,bird.BIRD_SIZE);
            updateGame(g);
            renderGame(g); 
            try{
                Thread.sleep(this.targetTime);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    /**
    * Stops the game
    * @param g the Graphics object
    */
    public void stopGame(Graphics g){
      if(this.bestScore < this.score){
         this.bestScore = this.score;
         saveHighScore(this.score);
      }
      Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(new GradientPaint(150,250,Color.RED,450,250,Color.ORANGE));
      g2d.fillRect(0,0,PANELWIDTH,PANELHEIGHT);
      g2d.setColor(Color.BLACK);
      g2d.setFont(GAME_OVER_FONT);
      g2d.drawString("Game Over!",100,200);
      g.setFont(SMALL_FONT);
      g.drawString("Your score: " + this.score,150,250);
      g.drawString("Best score: " + this.bestScore,150,275);
      g.drawString("Press \"R\" to play again",100,300);
      running = false;
      try{
         this.gameThread.join();
      }catch(Exception e){
         e.printStackTrace();
      }
    }
    /**
    * Resets the game so the user can play again
    */
    public void resetGame(){
      Graphics g = panel.getGraphics();
      g.setColor(LIGHT_BLUE);
      g.fillRect(0,0,this.PANELWIDTH,this.PANELHEIGHT);
      this.bird = new Bird(100,300);
      this.keyListener.setBird(this.bird);
      this.score = 0;
      this.gap = 150;
      this.tubeCount = 0;
      this.spacer = 200;
      this.tubes.clear();
      for(int i = 0; i < 6; ++i){
         this.tubes.add(new Tube(this.PANELWIDTH + (this.spacer*i),this.PANELHEIGHT,this.gap));
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
    * @param g a Graphics object
    */
    public void updateGame(Graphics g){
        bird.update();
        Rectangle birdBoundingBox = bird.getBoundingBox();
        for(Tube tube : tubes){
            tube.update();
            if(birdBoundingBox.intersects(tube.getTopBoundingBox()) ||
                    birdBoundingBox.intersects(tube.getBottomBoundingBox()) ||
                    bird.getY() >= this.PANELHEIGHT){
                stopGame(g);
                return;
            }
            if(bird.getX() > tube.getX() + tube.RECTWIDTH && !tube.hasPassed()){
                this.score++;
                tube.setPassed(true);
                this.tubeCount++;
                if(tubeCount % GAP_DECREASE_INTERVAL == 0 && gap > MIN_GAP){
                    this.gap -= 5;
                    this.spacer -= 20;
                }
            }
        }
        Tube firstTube = tubes.peekFirst();
        if(firstTube.getX() + firstTube.RECTWIDTH < 0){
            tubes.removeFirst();
        }
        if(tubes.size() < MAX_TUBES){
            addNewTube();
        }
    }
    /**
    * Adds a new tube to the Deque object
    */
    private void addNewTube(){
        Tube lastTube = this.tubes.peekLast();
        int lastXCoord = (lastTube != null) ? lastTube.getX() : PANELWIDTH;
        tubes.add(new Tube(lastXCoord + this.spacer, this.PANELHEIGHT, this.gap));
    }
    /**
    * Renders the game by drawing objects to the DrawingPanel
    * @param g a Graphics object so you can draw
    */
    private void renderGame(Graphics g){
        bird.drawBird(g, bird.getX(), bird.getY());
        for(Tube tube : tubes){
            tube.drawTube(g);
        }
        g.setFont(SCORE_FONT);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
    }
    /**
    * Reads in a high score variable from a .txt file
    * @param scanner a Scanner object to parse the file
    */
    public void loadHighScore(){
      try{
         Scanner scanner = new Scanner(HIGH_SCORE_FILE);
         if(scanner.hasNextInt()){
            this.bestScore = scanner.nextInt();
         }else{
            this.bestScore = 0;
         }
      }catch(Exception e){
         e.printStackTrace();
      }
    }
    /**
    * Writes the high score of a player to a .txt file to save it
    * @param highScore the high score of the player
    */
    public void saveHighScore(int highScore) {
        try(PrintStream ps = new PrintStream("HighScore.txt")) {
            ps.print(Integer.toString(highScore));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}



