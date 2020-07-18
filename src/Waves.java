import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
  TODO Remove code in @paint method
 */
public class Waves extends Frame implements Runnable, java.awt.event.ItemListener, java.awt.event.ActionListener, ChangeListener {
  int width;
  int height;

  int finalDisplacement = 30;
  int sizeMultiplier = 2;

  double speedOfTime = 300.0D;
  JSlider slider;
  int sliderMin = 1;
  int sliderMax = 200;
  int sliderStart = sliderMax-30;

  java.awt.Image i2;
  java.awt.Font fH;
  Graphics g1;
  Graphics g2;
  final java.awt.Color BG;
  final java.awt.Color PAN;
  GBLPanel p;
  java.awt.Label lOsc;
  java.awt.Label lLambda;
  java.awt.Label lNy;

  public Waves()
  {
    setSize(700, 320);
    setTitle("Standing Waves Demo");
    setVisible(true);

    init();
    start();
    BG = Color.lightGray;
    PAN = Color.gray;

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        stop();
        dispose();
        System.exit(0);
      }
    });
  }

  public static void main(String[] args)
  {
    new Waves();
  }

  void calculation() {

    //d= # of nodes or antinodes, depending on standing wave selected
    double d = type != 1 ? 2.0D / (nrOsc + 1) : 4.0D / (2 * nrOsc + 1);//Is it not set on a one-end-closed tube?
    // If so, d=2/(nrOsc+1).
    //else d=4/(2*(nrOsc+1);

    //wavelength just for mathematical output purposes
    lambdaReal = (d * lengthReal);


    lambda = (d * 240.0D);

    //omega=angular frequency
    omega = (2000.0D / (lambda * 2.0D * 3.141592653589793D));// wave speed/wavelength * 2 * Pi -> frequency * 2 *pi

    //k=angular wavenumber
    k = (6.283185307179586D / lambda); // 2*PI / wavelength -> # of wavelengths per distance, rad / m

    //nyReal=frequency
    //f=v/lambda
    nyReal = (343.5D / lambdaReal); // speed of sound/wavelength

    //display output
    tfLength.setText(TF.doubleToString2(lengthReal, 3, pt));
    lLambda.setText(TF.doubleToString2(lambdaReal, 3, pt));
    lNy.setText(TF.doubleToString2(nyReal, 3, pt));

    //aMax = max amplitude
    aMax = (lambda / 18.0D);
  }
  
  public void init()
  {
    width = getSize().width;
    height = getSize().height;
    fH = new java.awt.Font("Helvetica", 1, 12);
    g1 = getGraphics();
    i2 = createImage(width, height);
    g2 = i2.getGraphics();
    setLayout(null);
  }
  
  public void start()
  {
    text = english;

    //"."
    pt = text[0];
    p = new GBLPanel();
    p.setBounds(330*sizeMultiplier+40, finalDisplacement+10, width - 380, height);

    //"Form of Tube"
    p.add(new java.awt.Label(text[1]), PAN, 0, 0, 3, 10, 10, 0, 10);

    cbg = new java.awt.CheckboxGroup();
    //"both sides open"
    cbO = new java.awt.Checkbox(text[2], cbg, true);
    p.add(cbO, PAN, 0, 1, 3, 0, 10, 0, 10);
    //"one side open"
    cbHO = new java.awt.Checkbox(text[3], cbg, false);
    p.add(cbHO, PAN, 0, 2, 3, 0, 10, 0, 10);
    //"both sides closed"
    cbG = new java.awt.Checkbox(text[4], cbg, false);
    p.add(cbG, PAN, 0, 3, 3, 0, 10, 0, 10);

    //"Vibrational Mode"
    p.add(new java.awt.Label(text[5]), PAN, 0, 4, 1, 10, 10, 0, 5);

    //"Fundamental"
    lOsc = new java.awt.Label(text[6]);
    lOsc.setForeground(java.awt.Color.red);
    p.add(lOsc, PAN, 1, 4, 1, 10, 0, 0, 10);

    //"Higher", "Lower"
    bHigher = new java.awt.Button(text[12]);
    bLower = new java.awt.Button(text[13]);
    p.add(bLower, Color.yellow, 0, 6, 3, 10, 10, 5, 10);
    p.add(bHigher, Color.cyan, 0, 7, 3, 5, 10, 0, 10);

    //Length of Tube
    p.add(new java.awt.Label(text[14]), PAN, 0, 8, 1, 10, 10, 10, 10);

    tfLength = new java.awt.TextField(5);
    p.add(tfLength, java.awt.Color.white, 1, 8, 1, 10, 0, 0, 5);

    //meters
    p.add(new java.awt.Label("m"), PAN, 2, 8, 1, 10, 0, 0, 10);

    //Wavelength
    p.add(new java.awt.Label(text[15]), PAN, 0, 9, 1, 5, 10, 0, 0);

    lLambda = new java.awt.Label();
    p.add(lLambda, PAN, 1, 9, 1, 5, 0, 0, 5);

    //meters
    p.add(new java.awt.Label("m"), PAN, 2, 9, 1, 5, 0, 0, 10);

    //Frequency
    p.add(new java.awt.Label(text[16]), PAN, 0, 10, 1, 5, 10, 0, 0);

    lNy = new java.awt.Label();
    p.add(lNy, PAN, 1, 10, 1, 5, 0, 0, 5);

    //Hertz
    p.add(new java.awt.Label("Hz"), PAN, 2, 10, 1, 5, 0, 0, 10);

    //Graph
    p.add(new java.awt.Label("Graph Type:"), PAN, 0, 11, 1, 10, 10, 0, 10);

    //p.add(new Label("Displacement"), PAN, 1, 11, 1, 10, 0, 0, 10);


    cbgp = new java.awt.CheckboxGroup();
    //Displacement
    cbD = new java.awt.Checkbox(text[21], cbgp, true);
    p.add(cbD, PAN, 0, 12, 3, 0, 10, 0, 10);

    //Pressure
    cbP = new java.awt.Checkbox(text[22], cbgp, false);
    p.add(cbP, PAN, 0, 13, 3, 0, 10, 0, 10);


    //Speed Adjuster
    slider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderStart);
    slider.setMajorTickSpacing(50);
    slider.setPaintTicks(true);
    p.add(new Label("Speed"), PAN, 0, 14, 1, 10, 10, 0, 10);
    p.add(slider, PAN, 1, 14, 1, 10, 0, 0, 10);

    //Name
    p.add(new java.awt.Label(text[19]), PAN, 0, 15, 3, 10, 10, 10, 10);

    bLower.setEnabled(false);
    bHigher.setEnabled(true);

    cbO.setEnabled(true);
    cbHO.setEnabled(true);
    cbG.setEnabled(true);

    slider.setEnabled(true);

    cbD.setEnabled(true);

    //Pressure graph
    cbP.setEnabled(true);

    tfLength.setEnabled(true);

    add(p);
    p.repaint();
    cbO.addItemListener(this);
    cbHO.addItemListener(this);
    cbG.addItemListener(this);

    //Pressure Graph
    cbP.addItemListener(this);
    cbD.addItemListener(this);


    bLower.addActionListener(this);
    bHigher.addActionListener(this);
    tfLength.addActionListener(this);
    slider.addChangeListener(this);
    t = 0.0D;
    type = (this.nrOsc = 0);
    lengthReal = 1.0D;
    calculation();
    thr = new Thread(this);
    thr.start();
  }
  
  public void stop()
  {
    if (thr != null)
      thr.suspend();
    thr = null;
    removeAll();
  }
  
  public void run()
  {
    long l1 = System.currentTimeMillis();
    for (;;)
    {
      paint(g2);
      g1.drawImage(i2, 0, 0, this);
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException localInterruptedException) {}
      long l2 = System.currentTimeMillis();
      t += (l2 - l1) / (0.25+speedOfTime); //how often to change states, lower the denominator -> the faster events occur
      l1 = l2;
    }
  }

  //Axis Labels
  void drawMarks(boolean paramBoolean, int paramInt)
  {
    int i;
    int j;
    if (pressureGraph) {
      i = 18;j = 17;
    } else {
      i = 17;j = 18;
    }
    g2.setColor(java.awt.Color.black);
    for (int m = 0; m * lambda / 4.0D <= 240.001D; m++)
    {
      int n = (int)Math.round(50.0D + m * lambda / 4.0D);
      g2.drawLine(n*sizeMultiplier, paramInt - 2 + finalDisplacement, n*sizeMultiplier, paramInt + 2 + finalDisplacement);
      if (paramBoolean) {
        if (((type >= 1) && (m % 2 == 0)) || ((type == 0) && (m % 2 == 1))) {
          g2.drawString(text[i], n*sizeMultiplier - 3, paramInt + 15 + finalDisplacement);
        } else {
          g2.drawString(text[j], n*sizeMultiplier - 3, paramInt + 15 + finalDisplacement);
        }
      }
    }
  }
  
  void drawTube() {

    if (type==2)
    {
      cos = Math.cos(omega * t);

      cbP.setEnabled(false);
      cbP.setState(false);
      cbD.setState(true);
      pressureGraph = false;

      g2.setColor(java.awt.Color.black);

      int graphX = 50 * sizeMultiplier - 2;
      int graphY = 120 + finalDisplacement;
      //draw bottom
      g2.fillRect(graphX, 120 + finalDisplacement, 240 * sizeMultiplier + 10, 3);
      //close left end
      g2.fillRect(47 * sizeMultiplier + 1, 17 + finalDisplacement, 3, 106);
      g2.drawString("Energy", 47*sizeMultiplier + 1 - 10, 16 + finalDisplacement);
      g2.setColor(Color.blue);
      g2.drawString("Blue", 47 * 2 * sizeMultiplier + 1, 16 + finalDisplacement);
      g2.setColor(Color.black);
      g2.drawString("         = Kinetic |", 47 * 2 * sizeMultiplier + 1, 16 + finalDisplacement);
      g2.setColor(Color.RED);
      g2.drawString("                              Red", 47 * 2 * sizeMultiplier + 1, 16 + finalDisplacement);
      g2.setColor(Color.black);
      g2.drawString("                                       = Potential | ", 47 * 2 * sizeMultiplier + 1, 16 + finalDisplacement);
      g2.setColor(Color.YELLOW);
      g2.drawString(" Yellow",47 * 4 * sizeMultiplier + 1, 16 + finalDisplacement);
      g2.setColor(Color.black);
      g2.drawString("              = Total", 47 * 4 * sizeMultiplier + 1, 16 + finalDisplacement);


      drawEnergy(50.0D, graphY-30, lambda, cos * graphX, 50.0D, 290.0D, animatedGraphColor);

      //do label nodes/antinodes below air molecules graph
      drawMarks(true, 122);
    }
    else {
      if (!cbP.isEnabled())
      {
        cbP.setEnabled(true);
      }
      //cos=displacement or position
      cos = Math.cos(omega * t);

      //g2 = air molecules graph
      g2.setColor(java.awt.Color.black);
      g2.fillRect(50 * sizeMultiplier - 2, 17 + finalDisplacement, 240 * sizeMultiplier + 10, 3);
      g2.fillRect(50 * sizeMultiplier - 2, 120 + finalDisplacement, 240 * sizeMultiplier + 10, 3);

      //if selected tube = one-end-closed or both-ends-closed
      if (type >= 1)
        //close left end
        g2.fillRect(47 * sizeMultiplier + 1, 17 + finalDisplacement, 3, 106);

      g2.setColor(java.awt.Color.blue);

      double d1;
      double d2;
      int j;
      int m;
      int n;
      for (int i = 50; i <= 290; i += 20) {
        d1 = (i - 50) * k;

        //if open at both ends
        if (type == 0) {
          //d2 = cosine graph
          d2 = aMax * Math.cos(d1);
        } else {
          //d2 = sine graph
          d2 = aMax * Math.sin(d1);
        }
        j = (int) Math.round(d2 * cos);
        //m=x location of molecules
        m = i + j - 2;
        for (n = 40; n <= 100; n += 20) {
          //draw molecules
          g2.fillOval(m * sizeMultiplier, n - 2 + finalDisplacement, 5 * sizeMultiplier, 5 * sizeMultiplier);
        }
      }

      //same as loop above but just different molecules that are displaced more so there is more molecules
      for (int i = 60; i <= 280; i += 20) {
        d1 = (i - 50) * k;

        if (type == 0) {
          d2 = aMax * Math.cos(d1);
        } else
          d2 = aMax * Math.sin(d1);
        j = (int) Math.round(d2 * cos);
        m = i + j - 2;
        for (n = 30; n <= 110; n += 20) {
          g2.fillOval(m * sizeMultiplier, n - 2 + finalDisplacement, 5 * sizeMultiplier, 5 * sizeMultiplier);
        }
      }


      //dont label nodes/antinodes on top of air molecules graph
      drawMarks(false, 17);

      //do label nodes/antinodes below air molecules graph
      drawMarks(true, 122);
    }
  }

  /*
  @paramint3 x placement of graph
  @paramint2 y placement of the graph
  @paramint4 modifies the width of line

  paramint4 > paramint3
   */
  void drawXAxis(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    g2.setColor(java.awt.Color.black);
    g2.drawLine(paramInt3, paramInt2 + finalDisplacement, paramInt4, paramInt2 + finalDisplacement);
    g2.drawLine(paramInt4 - 10, paramInt2 - 3 + finalDisplacement, paramInt4, paramInt2 + finalDisplacement);
    g2.drawLine(paramInt4 - 10, paramInt2 + 3 + finalDisplacement, paramInt4, paramInt2 + finalDisplacement);
  }

  /*
  @paramInt1 x placement of line
  @paramInt3 maxY of line
  @paramInt4 minY of line

  paramInt3 > paramInt4
   */
  void drawYAxis(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    g2.setColor(java.awt.Color.black);

    //the actual line
    g2.drawLine(paramInt1, paramInt3 + finalDisplacement, paramInt1, paramInt4 + finalDisplacement);

    //a vector
    g2.drawLine(paramInt1 - 3, paramInt4 + 10 + finalDisplacement, paramInt1, paramInt4 + finalDisplacement);
    //another vector
    g2.drawLine(paramInt1 + 3, paramInt4 + 10 + finalDisplacement, paramInt1, paramInt4 + finalDisplacement);
  }

  /*
  @paramDouble5 x-axis
  @paramDouble3 lambda/wavelength
   */
  void drawSinus(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, java.awt.Color paramColor)
  {
    double d = 6.283185307179586D / paramDouble3;// 2pi/wavelength
    g2.setColor(paramColor);
    int i = (int)Math.ceil(paramDouble5);
    int j;
    //System.out.println("i: " + String.valueOf(i) + ", paramDouble6: " + String.valueOf(paramDouble6));
    for (int m = (int)Math.round(paramDouble2 + finalDisplacement - paramDouble4 * Math.sin(d * (i - paramDouble1))); i < paramDouble6; m = j)
    {
      int n = i + 1;
      j = (int)Math.round(paramDouble2 + finalDisplacement - paramDouble4 * Math.sin(d * (n - paramDouble1)));
      g2.drawLine(i*sizeMultiplier, m, n*sizeMultiplier, j);
      //System.out.println("X1: " + String.valueOf(i*sizeMultiplier) +", Y1: " + String.valueOf(m) + ", X2: " + String.valueOf(n*sizeMultiplier) + ", Y2: " + String.valueOf(j));
      i = n;
    }
  }

  /*
  @paramDouble4 x-axis
  @paramDouble2 y-axis
  @paramDouble3 lambda/wavelength
   */
  void drawEnergy(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, java.awt.Color paramColor)
  {
    double d = 6.283185307179586D / paramDouble3;// 2pi/wavelength
    int maxAmplitude = 50 * sizeMultiplier - 2;
    g2.setColor(paramColor);
    int i = (int)Math.ceil(paramDouble5);
    int i2 = (int)Math.ceil(paramDouble5);
    int j;
    int jp;


    int j1;
    //Potential Energy 2.0 -> GOOD
    for (int m = (int)Math.round(paramDouble2 + finalDisplacement - paramDouble4 * Math.sin(d * (i - paramDouble1) )), m1; i < paramDouble6; m = j)
    {
      int n = i + 1;
      //yAxis - xAxis * sin(
      double maxDisplacement = paramDouble4;
      j = (int)Math.round(paramDouble2 + finalDisplacement - maxDisplacement * Math.sin(d * (n - paramDouble1) ));

      int Loss = maxAmplitude - (int)Math.abs(maxDisplacement);
      m1 = (int)Math.round(paramDouble2 + finalDisplacement - Loss * Math.sin(d * (i - paramDouble1)));
      j1 = (int)Math.round(paramDouble2 + finalDisplacement - Loss * Math.sin(d * (n - paramDouble1)));

      //m1 and j1 are for kinetic energy

      //System.out.println("Max Displacement: " + String.valueOf(maxDisplacement));



      //m=y coordinate, j = y coordinate


      if (m >= paramDouble2 + 30)
      {
        m = (int)Math.round(paramDouble2 + finalDisplacement + paramDouble4 * Math.sin(d * (i - paramDouble1)));
      }
      if (j >= paramDouble2 + 30)
      {
        j = (int)Math.round(paramDouble2 + finalDisplacement + maxDisplacement * Math.sin(d * (n - paramDouble1)));
      }



      if (m1 >= paramDouble2 + 30)
      {
        m1 = (int)Math.round(paramDouble2 + finalDisplacement + Loss * Math.sin(d * (i - paramDouble1)));
      }
      if (j1 >= paramDouble2 + 30)
      {
        j1 = (int)Math.round(paramDouble2 + finalDisplacement + Loss * Math.sin(d * (n - paramDouble1)));
      }

      g2.setColor(Color.YELLOW);
      drawDashedLine(g2, 100,(int)paramDouble2+30-maxAmplitude-1, 590, (int)paramDouble2+30-maxAmplitude-1);


      /*
      if (m1 > paramDouble2 + 30)
      {
        m1 = (int)(paramDouble2 + 30) - mLoss;
      }

      if (j1 > paramDouble2 + 30)
      {
        if (jLoss < 0)
          jLoss += (j1-(paramDouble2+30));
        j1 = (int)(paramDouble2+30) - jLoss;
      }
      */


      g2.setColor(Color.RED);
      g2.drawLine(i*sizeMultiplier, m, n*sizeMultiplier, j);
      g2.setColor(Color.BLUE);
      g2.drawLine(i*sizeMultiplier, m1, n*sizeMultiplier, j1);
      //System.out.println("X1: " + String.valueOf(i*sizeMultiplier) +", Y1: " + String.valueOf(m) + ", X2: " + String.valueOf(n*sizeMultiplier) + ", Y2: " + String.valueOf(j));
      i = n;
    }
  }
  

  void drawDiagram()
  {
    //displacement or position at point
    cos = Math.cos(omega * t);

    //param1 not used
    drawXAxis(50, 240, 40, 310*sizeMultiplier);
    //param2 not used
    drawYAxis(50*sizeMultiplier, 240, 300, 170);

    //Draw both open
    if (type == 0)
    {

      if (pressureGraph)
      {
        drawSinus(50.0D, 240.0D, lambda, 40.0D, 50.0D, 290.0D, maxGraphColor);
        drawSinus(50.0D, 240.0D, lambda, -40.0D, 50.0D, 290.0D, maxGraphColor);
        drawSinus(50.0D, 240.0D, lambda, cos * 40.0D, 50.0D, 290.0D, animatedGraphColor);
      }
      else
      {
        drawSinus(50.0D - lambda / 4.0D, 240.0D, lambda, 40.0D, 50.0D, 290.0D, maxGraphColor);
        drawSinus(50.0D - lambda / 4.0D, 240.0D, lambda, -40.0D, 50.0D, 290.0D, maxGraphColor);
        drawSinus(50.0D - lambda / 4.0D, 240.0D, lambda, cos * 40.0D, 50.0D, 290.0D, animatedGraphColor);
      }
      

    }
    else if (pressureGraph)
    {
      drawSinus(50.0D + lambda / 4.0D, 240.0D, lambda, 40.0D, 50.0D, 290.0D, maxGraphColor);
      drawSinus(50.0D + lambda / 4.0D, 240.0D, lambda, -40.0D, 50.0D, 290.0D, maxGraphColor);
      drawSinus(50.0D + lambda / 4.0D, 240.0D, lambda, cos * 40.0D, 50.0D, 290.0D, animatedGraphColor);
    }
    else
    {
      //50,y,lambda,x,50,290,maxGraphColor
      drawSinus(50.0D, 240.0D, lambda, 40.0D, 50.0D, 290.0D, maxGraphColor);
      drawSinus(50.0D, 240.0D, lambda, -40.0D, 50.0D, 290.0D, maxGraphColor);
      drawSinus(50.0D, 240.0D, lambda, cos * 40.0D, 50.0D, 290.0D, animatedGraphColor);
    }
    


    drawMarks(true, 240);
  }

  public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){

    //creates a copy of the Graphics instance
    Graphics2D g2d = (Graphics2D) g.create();

    //set the stroke of the copy, not the original
    Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    g2d.setStroke(dashed);
    g2d.drawLine(x1, y1, x2, y2);

    //gets rid of the copy
    g2d.dispose();
  }

  @Override
  public void paint(Graphics paramGraphics)
  {
    paramGraphics.setFont(fH);
    paramGraphics.setColor(BG);
    paramGraphics.fillRect(0, finalDisplacement, width*sizeMultiplier, height*sizeMultiplier+finalDisplacement);
    drawTube();
    drawDiagram();
  }

  //Updates text about overtone and enables/disables buttons
  void newOscillation()
  {
    lOsc.setText(text[(6 + nrOsc)]);
    bLower.setEnabled(nrOsc > 0);
    bHigher.setEnabled(nrOsc < 5);
  }

  @Override
  public void itemStateChanged(java.awt.event.ItemEvent paramItemEvent)
  {
    java.awt.Checkbox localCheckbox = cbg.getSelectedCheckbox();
    if (localCheckbox.equals(cbO)) {
      type = 0;
    }
    else if (localCheckbox.equals(cbHO)) {
      type = 1;
    }
    else if (localCheckbox.equals(cbG)) {
      type = 2;
    }


    localCheckbox = cbgp.getSelectedCheckbox();
    if (localCheckbox.equals(cbD)) {
      pressureGraph = false;
    }
    else if (localCheckbox.equals(cbP)) {
      pressureGraph = true;
    }

    calculation();
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent paramActionEvent)
  {
    Object localObject = paramActionEvent.getSource();

    if ((localObject == bLower) && (nrOsc > 0))
    {
      nrOsc -= 1;
      newOscillation();
    }
    else if ((localObject == bHigher) && (nrOsc < 5))
    {
      nrOsc += 1;
      newOscillation();
    }
    else if ((localObject instanceof java.awt.TextField))
    {
      lengthReal = TF.stringToDouble(tfLength.getText());
      if (lengthReal > 10.0D)
        lengthReal = 10.0D;
      if (lengthReal < 0.1D)
        lengthReal = 0.1D;
    }
    calculation();
  }

  java.awt.Button bHigher;

  java.awt.Button bLower;

  java.awt.TextField tfLength;

  java.awt.Checkbox cbO;

  java.awt.Checkbox cbHO;

  java.awt.Checkbox cbG;

  java.awt.CheckboxGroup cbg;

  java.awt.CheckboxGroup cbgp;

  java.awt.Checkbox cbP;

  java.awt.Checkbox cbD;
  Thread thr;
  double t;
  int type;
  boolean pressureGraph = false;
  int nrOsc = 0;
  final double cReal = 343.5D;
  final double c = 2000.0D;
  double lengthReal;
  final int length = 240;
  final int left = 50;
  double lambdaReal;
  double lambda;
  double nyReal;
  double omega;
  double k;
  double aMax;
  double cos;
  java.awt.Color animatedGraphColor = java.awt.Color.black;
  java.awt.Color maxGraphColor = java.awt.Color.lightGray;

  String lang;
  String[] text;
  String pt;
  String[] english = {
          ".", //0
          "Form of tube:",
          "Both sides open",
          "One side open",
          "String fixed at two ends",
          "Vibrational mode:",//5
          "Fundamental",
          "1st overtone",
          "2nd overtone",
          "3rd overtone",
          "4th overtone",//10
          "5th overtone",
          "Higher",
          "Lower",
          "Length of tube:",
          "Wavelength:",//15
          "Frequency:",
          "N",
          "A",
          "Amin was here :D", //19
          "Graph:",
          "Displacement",
          "Pressure",
          "Kinetic Energy" //23
  };

  @Override
  public void stateChanged(ChangeEvent e) {
    Object local = e.getSource();
    if (local instanceof JSlider)
    {
      speedOfTime = (sliderMax-slider.getValue())*10;
    }
  }
}
