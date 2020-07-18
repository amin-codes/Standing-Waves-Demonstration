class TF
{
  static String doubleToString(double paramDouble, int paramInt, String paramString)
  {
    String str = paramDouble >= 0.0D ? "" : "-";
    long l2;
    if (paramDouble < 0.0D) paramDouble = -paramDouble;
    long l3 = 0L; for (l2 = 1L; l3 < paramInt; l3 += 1L) l2 *= 10L;
    long l1 = Math.round(paramDouble * l2);
    str = str + l1 / l2; if (paramInt > 0) str = str + paramString;
    l1 %= l2; for (l3 = 0L; l3 < paramInt; l3 += 1L) { l2 /= 10L;str = str + l1 / l2;l1 %= l2; }
    return str;
  }
  




  static String doubleToString2(double paramDouble, int paramInt, String paramString)
  {
    double d = Math.abs(paramDouble);
    int i = (int)Math.floor(Math.log(d) / Math.log(10.0D));
    String str = paramDouble >= 0.0D ? "" : "-";
    return str + doubleToString(d, paramInt - 1 - i, paramString);
  }
  

  static double stringToDouble(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    
    for (int i = 0; i < localStringBuffer.length(); i++) if (localStringBuffer.charAt(i) == ',')
        localStringBuffer.setCharAt(i, '.');
    double d; try { d = Double.valueOf(localStringBuffer.toString()).doubleValue();
    } catch (NumberFormatException localNumberFormatException) { d = 0.0D; }
    return d;
  }
  
  TF() {}
}
