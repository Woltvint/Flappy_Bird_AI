import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Flappy_Bird_AI extends PApplet {

//////////////////////////////////////////////////////// //<>//
//                                                    //
//  <---------------------------------------------->  //
//  |         Flappy Bird AI by Woltvint           |  //
//  |          usually better than you             |  //
//  |           in just 6 generations              |  //
//  |                   enjoy!!                    |  //
//  <---------------------------------------------->  //
//                                                    //
////////////////////////////////////////////////////////


//pipes
Pipe[] pipes = new Pipe[2];
int pipeStart = 500; //how far do the pipes start from the bird
int pipeSpace = 350; //space between pipes

//birds
Bird[] birds = new Bird[10];

//bar control
boolean showMainBar = true;
boolean showValueBar = false;
boolean showGraph = false;

//bar info
int score = 0;
int gen = 1; //so what gen 0 looked stupid

public void setup () 
{
  
  frameRate(120);

  //pipes
  inicPipes();

  //bird
  inicBirds();
}

//inic funcs
public void inicBirds() 
{
  for (int i = 0; i < birds.length; i++) 
  {
    birds[i] = new Bird();
    birds[i].x = 160;
    birds[i].y = 200;
    birds[i].n.NetInic();
  }
}

public void inicPipes () 
{
  for (int i = 0; i < pipes.length; i++) 
  {
    pipes[i] = new Pipe();
    pipes[i].x = pipeStart + i * pipeSpace;
    pipes[i].w = 80;
    pipes[i].h = (int)random(50, 300);
    pipes[i].pipeSpace = pipeSpace*pipes.length;
  }
}

//main loop
public void draw () 
{
  background(0xff00BFFF);   //backy the back

  //control values
  boolean anyoneAlive = false;
  Pipe cp = pipes[0];

  //the bird used for the stats in the bars his name is birb
  Bird birb = new Bird();

  //pipe movement
  for (int i = 0; i < pipes.length; i++) 
  {
    pipes[i].s = false;
    if ((pipes[i].x < cp.x) && pipes[i].x + pipes[i].w > birds[0].x || cp.x < birds[0].x - cp.w - birds[0].w/2 ) {
      cp = pipes[i];
    }
    if (pipes[i].movePipe()) { 
      score++;
    }
  }

  //coloration of the pipe in focus
  cp.s = true;


  //bird control
  for (int i = 0; i < birds.length; i++) 
  {
    if (birds[i].alive)
    {
      birds[i].birdSim();

      birds[i].colBird(cp.x, cp.w, cp.h);
      birds[i].score++;

      if (birds[i].n.NetCalc(abs(birds[i].x - cp.x), cp.h-birds[i].y, cp.h+150-birds[i].y, birds[i].vel)) {
        birds[i].jump();
      }

      //yes the last bird alive in the array becomes the new birb ... too lazy to do anything else
      birb = birds[i];
    }

    //is anyone there ?
    anyoneAlive = anyoneAlive || birds[i].alive;
  }

  //if all are dead mutate start repeat
  if (!anyoneAlive) {
    mutateNets();
  }

  //rendering part
  for (int i = 0; i < birds.length; i++) 
  {
    if (birds[i].alive)
    {
      birds[i].drawBird();
    }
  }

  for (int i = 0; i < pipes.length; i++) 
  {
    pipes[i].drawPipe();
  }




  //main bar
  if (showMainBar) {
    stroke(0);
    fill(255);
    rect(0, 0, 70, 30);
    rect(70, 0, 60, 30);
    rect(130, 0, 254, 30);

    fill(0);
    text("Score:"+score, 5, 20);
    text("Gen:"+gen, 75, 20);
    text("FPS:"+frameRate, 145, 20);
  }

  //network values
  if (showValueBar) {
    stroke(0);
    fill(255);
    rect(0, height-30, 128, 30);
    rect(128, height-30, 128, 30);
    rect(0, height-60, 128, 30);
    rect(128, height-60, 128, 30);
    rect(256, height-30, 128, 30);

    fill(0);
    text("w1: "+birb.n.w[0], 5, height-40);
    text("w2: "+birb.n.w[1], 133, height-40);
    text("w3: "+birb.n.w[2], 5, height-10);
    text("w4: "+birb.n.w[3], 133, height-10);
    text("b: "+birb.n.b, 261, height-10);
  }

  //network graph
  if (showGraph)
  {
    float max = abs(birb.n.w[0]) + abs(birb.n.w[1]) + abs(birb.n.w[2]) + abs(birb.n.w[3]) + 0.0000001f;

    stroke(0);

    strokeWeight(abs((birb.n.w[0]/max)*5));
    line(width-100, height-120, width-10, height-80);
    strokeWeight(abs((birb.n.w[1]/max)*5));
    line(width-100, height-95, width-10, height-80);
    strokeWeight(abs((birb.n.w[2]/max)*5));
    line(width-100, height-70, width-10, height-80);
    strokeWeight(abs((birb.n.w[3]/max)*5));
    line(width-100, height-45, width-10, height-80);

    strokeWeight(1);
    fill(255);
    ellipse(width-10, height-80, 20, 20);
    ellipse(width-100, height-120, 20, 20);
    ellipse(width-100, height-95, 20, 20);
    ellipse(width-100, height-70, 20, 20);
    ellipse(width-100, height-45, 20, 20);

    fill(0);
    stroke(0);
    text("i1", width-104, height-115);
    text("i2", width-104, height-90);
    text("i3", width-104, height-65);
    text("i4", width-104, height-40);
    text("N", width-14, height-75);
  }
}


//mutation func
public void mutateNets() 
{
  Bird[] newBirds = new Bird[birds.length];

  //new birds setup loop
  for (int i = 0; i < newBirds.length; i++) 
  {
    newBirds[i] = new Bird();
    newBirds[i].x = 160;
    newBirds[i].y = 200;
  }

  //array for sorting
  Bird[] ar = birds;

  //sorting loop
  for (int i = 0; i < ar.length-1; i++)
  {
    int min = i;
    for (int j = i+1; j < ar.length; j++)
      if (ar[j].score < ar[min].score) min = j;
    Bird temp = ar[i];
    ar[i] = ar[min];
    ar[min] = temp;
  }

  //control values for bird replication
  int i = 0;
  int scoremax = 0;

  //score addition
  for (int a = 0; a < birds.length; a++) {
    scoremax += birds[a].score;
  }

  //replication loop
  while (i < newBirds.length) 
  {
    int b = ar.length-1 - (int)random(0, birds.length/2);

    if (random(0, scoremax) < ar[b].score) 
    {
      newBirds[i].n = ar[b].n.MutateNeuron();
      i++;
    }
  }

  //updating values
  birds = newBirds;
  inicPipes();
  gen++;
  score = 0;
}

//user control
public void mouseClicked() {
  if (mouseButton == RIGHT) {
    frameRate(frameRate*2); //fastah
  }
  if (mouseButton == LEFT) {
    frameRate(frameRate/2); //slowah
  }
}

public void keyPressed() 
{
  switch (key) 
  {
  case 'm':
    showMainBar = !showMainBar;
    break;

  case 'v':
    showValueBar = !showValueBar;
    break;

  case 'g':
    showGraph = !showGraph;
    break;
  }
}
class Bird 
{
  boolean alive = true;
  float vel = 0;
  float x;
  float y;
  int score = 0;
  int w = 40;
  int h = 40;

  Neuron n = new Neuron();

  //simulation func
  public void birdSim() 
  {
    if (vel > -2) 
    {
      vel -= 0.4f;
    }

    y -= vel;
  }

  //drawing func
  public void drawBird() 
  {
    stroke(0);
    fill(0xffFFFF00);
    ellipse(x, y, w, h);
  }

  //collision checking func
  public void colBird(int px, int pw, int ph) 
  {
    //collision check
    if (px <= x+w/2 && px + pw >= x-w/2) 
    {
      if (y-h/2 <= ph) {
        alive = false;
      }
      if (y + h/2 >= ph+150) {
        alive = false;
      }
    }

    //world borders check
    if (y <= 0) { 
      alive = false;
    }
    if (y >= 512) { 
      alive = false;
    }
  }

  //jumping func
  public void jump() 
  {
    if (vel <= -1.8f) 
    {
      vel = 7;
    }
  }
}
class Neuron 
{
  //values inic
  float[] w = new float[4];
  float b;

  //information setting /dist x from pipe/dist y from the upper part/dist y from the lower part/vellocity 
  boolean[] con = new boolean[]{true, true, true, true};


  //network inic func
  public void NetInic() 
  {
    for (int i = 0; i < w.length; i++) 
    {
      w[i] = 0;
    }
    b = 0;
  }

  //calculation of the network output
  public boolean NetCalc(float distx, float distyUp, float ditsyDown, float vel) 
  {
    float input = 0;

    //the first great ifing or just simple math
    if (con[0]) 
    {
      input += w[0] * distx;
    }

    if (con[1]) 
    {
      input += w[1] * distyUp;
    }

    if (con[2]) 
    {
      input += w[2] * ditsyDown;
    }

    if (con[3]) 
    {
      input += w[3] * vel;
    }

    //fitting the output between 0 and 1
    float output = sigmoid(input + b);

    //to jump or not to jump that is the question
    if (output > 0.5f) { 
      return true;
    } else
    { 
      return false;
    }
  }
  
  
  
  //sigmoid func
  public float sigmoid(float x) 
  {
    return 1.0f / (1.0f + (float)Math.exp(-x));
  }


  //mutation func
  public Neuron MutateNeuron() 
  {
    Neuron output = new Neuron();

    for (int i = 0; i < w.length; i++) 
    {
      if (random(0, 100) < 25)
      {
        output.w[i] = w[i] + random(-0.5f, 0.5f);
      } else
      {
        output.w[i] = w[i];
      }
    }

    if (random(0, 100) < 50)
    {
      output.b = b + random(-0.5f, 0.5f);
    } else
    {
      output.b = b;
    }
    return output;
  }
}
class Pipe //its a pipe
{
  //is focus 
  boolean s = false;

  //hyperspace coordinates
  int x;
  int y = 0;
  int h;
  int w;
  int pipeSpace;


  //pipe moving func
  public boolean movePipe() 
  {
    if (x+w > 0) 
    {
      x -= 2;
      return false;
    } else
    {
      x = x + pipeSpace;
      h = (int)random(50, 300);
      return true;
    }
  }

  //pipe render func(tomato red)
  public void drawPipe() 
  {
    if (s)
    {
      fill(0);
    } else
    {
      fill(0xffFF6347);
    }

    stroke(0);
    rect(x, y, w, h);
    rect(x, y+150+h, w, h+400);
  }
}
  public void settings() {  size(384, 512); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Flappy_Bird_AI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
