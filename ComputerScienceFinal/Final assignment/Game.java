import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Game implements Runnable {
    private Thread gameThread;
    private boolean running;
    private final int FPS = 60;
    private long targetTime = 1000 / FPS;
    private ArrayList<Tube> tubes;
    private Bird bird;
    private int score = 0;
    private static final int GAP_DECREASE_INTERVAL = 5;
    private int tubeCount = 0;
    private int gap = 100;
    private static final int MIN_GAP = 50;
    private int panelWidth;
    private int panelHeight;
    private DrawingPanel panel;
    private Scanner console;
    private boolean gameOver = false;

    public Game(int panelWidth, int panelHeight) {
        this.tubes = new ArrayList<Tube>();
        this.bird = new Bird(100, 300);
        this.panel = new DrawingPanel(panelWidth, panelHeight);
        Color lightBlue = new Color(145,215,216);
        panel.setBackground(lightBlue);
        this.panel.addKeyListener(new BirdKeyListener(this.bird)); 
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.console = new Scanner(System.in);
        for(int i = 0; i < 6; i++){
            int spacer = 200;
            this.tubes.add(new Tube(this.panelWidth+(spacer*i),this.panelHeight,this.gap));
        }
    }

    public void startGame() {
        if (this.gameThread == null) {
            this.gameThread = new Thread(this);
            running = true;
            gameThread.start();
        }
    }

    @Override
    public void run() {
        long startTime;
        long elapsedTime;
        long waitTime;
        while (running) {
            startTime = System.nanoTime();
            Graphics g = panel.getGraphics();
            g.setColor(new Color(145, 214, 235));
            for(Tube a: tubes){
               g.fillRect(a.getX(),0,a.getWidth(),this.panelHeight);
            }
            g.fillRect(bird.getX(),bird.getY(),(int)bird.getBoundingBox().getWidth(),(int)bird.getBoundingBox().getHeight());
            updateGame();
            renderGame(g); 
            elapsedTime = System.nanoTime() - startTime;
            waitTime = targetTime - elapsedTime / 1000000; 
            if (waitTime < 0) {
                waitTime = 5;
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                System.out.println("Uh oh");
            }
        }
    }

    public void stopGame(){
        if(!gameOver){
            System.out.println("Game Over!!");
            System.out.println("Score: " + this.score);
            gameOver = true;
        }
        System.out.print("Do you want to play again? y/n");
        String input = this.console.next();
        console.nextLine();
        if(input.equalsIgnoreCase("n")){
            running = false;
            try{
               this.gameThread.join();
            }catch (Exception e){
               System.out.println("Uh oh");
            }
        }else if(input.equalsIgnoreCase("y")){
            running = false;
            try{
               this.gameThread.join();
            }catch (Exception e){
               System.out.println("Uh oh");
            }
            this.gameThread = null;
            resetGame();
            startGame();
        }else{
            System.out.println("Invalid input detected");
            stopGame();
        }
    }
    private void resetGame() {
      this.bird = new Bird(100, 300);
      this.tubes.clear();
      for (int i = 0; i < 6; i++) {
            int spacer = 200;
            this.tubes.add(new Tube(this.panelWidth + (spacer * i), this.panelHeight, this.gap));
      }
      this.score = 0;
      this.gap = 100;
      this.tubeCount = 0;
      this.gameOver = false;
    }

    public void updateGame() {
        bird.update();
        for (Tube tube : tubes) {
            tube.update();
            if (bird.getBoundingBox().intersects(tube.getTopBoundingBox()) ||
                    bird.getBoundingBox().intersects(tube.getBottomBoundingBox()) ||
                    bird.getY() >= this.panelHeight) {
                stopGame();
                return;
            }
            if (bird.getX() > tube.getX() + tube.getWidth() && !tube.hasPassed()) {
                this.score++;
                tube.setPassed(true);
                this.tubeCount++;
                if (tubeCount % GAP_DECREASE_INTERVAL == 0 && gap > MIN_GAP) {
                    gap -= 5;
                }
            }
        }
        tubes.removeIf(t -> t.getX() + t.getWidth() < 0);
        if(tubes.size() < 5){
            addNewTube();
        }
    }

    private void addNewTube() {
        int lastXCoord = this.tubes.get(this.tubes.size()-1).getX();
        int spacing = 200;
        tubes.add(new Tube(lastXCoord + spacing, this.panelHeight, this.gap));
    }

    private void renderGame(Graphics g) {
        bird.drawBird(g, bird.getX(), bird.getY());
        for (Tube tube : tubes) {
            tube.drawTube(g);
        }
        // Redraw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 10);
    }
}

