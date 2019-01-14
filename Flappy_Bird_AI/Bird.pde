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
  void birdSim() 
  {
    if (vel > -2) 
    {
      vel -= 0.4;
    }

    y -= vel;
  }

  //drawing func
  void drawBird() 
  {
    stroke(0);
    fill(#FFFF00);
    ellipse(x, y, w, h);
  }

  //collision checking func
  void colBird(int px, int pw, int ph) 
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
  void jump() 
  {
    if (vel <= -1.8) 
    {
      vel = 7;
    }
  }
}
