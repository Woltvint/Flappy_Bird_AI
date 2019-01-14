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
  boolean movePipe() 
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
  void drawPipe() 
  {
    if (s)
    {
      fill(0);
    } else
    {
      fill(#FF6347);
    }

    stroke(0);
    rect(x, y, w, h);
    rect(x, y+150+h, w, h+400);
  }
}
