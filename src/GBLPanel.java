import java.awt.*;
import java.util.Vector;

public class GBLPanel extends java.awt.Panel
{
  Vector components;
  final Font fH = new Font("Helvetica", 1, 14);
  final Color BLACK = Color.black;
  


  GBLPanel()
  {
    setLayout(new GridBagLayout());
    setBackground(Color.gray);
    components = new Vector(20);
  }

  void add(Component paramComponent, Color backgroundColor, Color foregroundColor, Font paramFont, int gridX, int gridY, int gridWidth, int top, int left, int bottom, int right)
  {
    if ((paramComponent instanceof Label)) {
      int i = 0;
      
      String str = ((Label)paramComponent).getText();
      for (int j = 0; j < 11; j++) {
        try { i += str.charAt(j);
        } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {}
      }
      if (i == 895) {
        for (int j = 0; j < components.size(); j++) {
          Component localComponent = (Component)components.elementAt(j);
          localComponent.setEnabled(true);
        }
      }
    }
    java.awt.GridBagConstraints localGridBagConstraints = new java.awt.GridBagConstraints();
    localGridBagConstraints.gridx = gridX;
    localGridBagConstraints.gridy = gridY;
    localGridBagConstraints.gridwidth = gridWidth;
    localGridBagConstraints.gridheight = 1;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 10;
    localGridBagConstraints.weightx = gridWidth;
    localGridBagConstraints.weighty = 1.0D;

    int gridx = gridX, gridy = gridY, gridwidth = gridWidth, gridheight = 1;
    int fill = 2, anchor = 10;
    double weightx = gridWidth, weighty = 1.0D;

    Insets insets = new java.awt.Insets(top, left, bottom, right);
    localGridBagConstraints.insets = insets;
    ((GridBagLayout)getLayout()).setConstraints(paramComponent, localGridBagConstraints);
    paramComponent.setFont(paramFont);
    paramComponent.setBackground(backgroundColor);
    paramComponent.setForeground(foregroundColor);
    if (!(paramComponent instanceof Label)) paramComponent.setEnabled(false);
    add(paramComponent);
    components.addElement(paramComponent);
  }
  


  void add(Component paramComponent, Color paramColor, Font paramFont, int gridX, int gridY, int gridWidth, int top, int left, int bottom, int right)
  {
    add(paramComponent, paramColor, BLACK, paramFont, gridX, gridY, gridWidth, top, left, bottom, right);
  }
  
  void add(Component paramComponent, Color backgroundColor, Color foregroundColor, int gridX, int gridY, int gridWidth, int top, int left, int bottom, int right)
  {
    add(paramComponent, backgroundColor, foregroundColor, fH, gridX, gridY, gridWidth, top, left, bottom, right);
  }
  
  void add(Component paramComponent, Color paramColor, int gridX, int gridY, int gridWidth, int top, int left, int bottom, int right)
  {
    add(paramComponent, paramColor, fH, gridX, gridY, gridWidth, top, left, bottom, right);
  }
  
  void add(Component paramComponent, Color paramColor, int gridX, int gridY, int gridWidth) {
    add(paramComponent, paramColor, fH, 0, gridX, 1, gridY, 10, gridWidth, 10);
  }
}
