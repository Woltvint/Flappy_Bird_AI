//////////////////////////////////////////////////////// //<>// //<>//
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

void setup () 
{
  size(384, 512);
  frameRate(120);

  //pipes
  inicPipes();

  //bird
  inicBirds();
}

//inic funcs
void inicBirds() 
{
  for (int i = 0; i < birds.length; i++) 
  {
    birds[i] = new Bird();
    birds[i].x = 160;
    birds[i].y = 200;
    birds[i].n.NetInic();
  }
}

void inicPipes () 
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
void draw () 
{
  background(#00BFFF);   //backy the back

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
    float max = abs(birb.n.w[0]) + abs(birb.n.w[1]) + abs(birb.n.w[2]) + abs(birb.n.w[3]) + 0.0000001;

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
void mutateNets() 
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
void mouseClicked() {
  if (mouseButton == RIGHT) {
    frameRate(frameRate*2); //fastah
  }
  if (mouseButton == LEFT) {
    frameRate(frameRate/2); //slowah
  }
}

void keyPressed() 
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
