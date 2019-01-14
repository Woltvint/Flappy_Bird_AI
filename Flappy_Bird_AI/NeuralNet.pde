class Neuron 
{
  //values inic
  float[] w = new float[4];
  float b;

  //information setting /dist x from pipe/dist y from the upper part/dist y from the lower part/vellocity 
  boolean[] con = new boolean[]{true, true, true, true};


  //network inic func
  void NetInic() 
  {
    for (int i = 0; i < w.length; i++) 
    {
      w[i] = 0;
    }
    b = 0;
  }

  //calculation of the network output
  boolean NetCalc(float distx, float distyUp, float ditsyDown, float vel) 
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
    if (output > 0.5) { 
      return true;
    } else
    { 
      return false;
    }
  }
  
  
  
  //sigmoid func
  float sigmoid(float x) 
  {
    return 1.0f / (1.0f + (float)Math.exp(-x));
  }


  //mutation func
  Neuron MutateNeuron() 
  {
    Neuron output = new Neuron();

    for (int i = 0; i < w.length; i++) 
    {
      if (random(0, 100) < 25)
      {
        output.w[i] = w[i] + random(-0.5, 0.5);
      } else
      {
        output.w[i] = w[i];
      }
    }

    if (random(0, 100) < 50)
    {
      output.b = b + random(-0.5, 0.5);
    } else
    {
      output.b = b;
    }
    return output;
  }
}
