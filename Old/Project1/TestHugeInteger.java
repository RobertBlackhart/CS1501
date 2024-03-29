///
/// Contents: Test driver for HugeInteger class.
/// Author:   John Aronis
/// Date:     December 2012
///

/// This program runs a battery of tests for the HugeInteger class and
/// compares results to Java's BigInteger class.

import java.math.BigInteger ;
import java.util.Random ;

public class TestHugeInteger {

  public static int START     = 1 ;
  public static int STOP      = 10000 ;
  public static int INCREMENT = 10 ;

  public static void main(String[] args) {
    String a, b ;
    BigInteger biga, bigb, bigc, bigd ;
    HugeInteger hugea, hugeb, hugec, huged ;
    boolean correct = true ;
    for (int digits=START ; digits<=STOP ; digits+=INCREMENT) {

      System.out.println("DIGITS = " + digits) ;

      // Create data:
      a = makeHugeInteger(digits) ;
      b = makeHugeInteger(digits) ;
      // a = "0" ;
      // b = "0" ;
      biga = new BigInteger(a,10) ;
      bigb = new BigInteger(b,10) ;
      hugea = new HugeInteger(a) ;
      hugeb = new HugeInteger(b) ;

      // Addition:
      HugeInteger.DIGIT_OPERATIONS = 0 ;
      bigc = biga.add(bigb) ;
      hugec = hugea.add(hugeb) ;
      if ( !bigc.toString().equals(hugec.toString()) ) correct=false ;
      System.out.println("  Addition operations/digits: " + HugeInteger.DIGIT_OPERATIONS/(float)digits) ;

      // Multiplication:
      HugeInteger.DIGIT_OPERATIONS = 0 ;
      bigc = biga.multiply(bigb) ;
      hugec = hugea.multiply(hugeb) ;
      if ( !bigc.toString().equals(hugec.toString()) ) correct=false ;
      System.out.println("  Multiplication operations/digits^2: " + HugeInteger.DIGIT_OPERATIONS/(float)(digits*digits)) ;

      // Fast Multiplication:
      HugeInteger.DIGIT_OPERATIONS = 0 ;
      bigc = biga.multiply(bigb) ;
      hugec = hugea.fastMultiply(hugeb) ;
      if ( !bigc.toString().equals(hugec.toString()) ) correct=false ;
      System.out.println("  Fast Multiplication operations/digits^2: " + HugeInteger.DIGIT_OPERATIONS/(float)(digits*digits)) ;
      System.out.println("  Fast Multiplication operations/digits^1.6: " + HugeInteger.DIGIT_OPERATIONS/Math.pow(digits,1.6f)) ;

      // Comparison:
      HugeInteger.DIGIT_OPERATIONS = 0 ;
      if ( biga.compareTo(bigb)!=hugea.compareTo(hugeb) ) correct=false ;
      System.out.println("  Comparison operations/digits: " + HugeInteger.DIGIT_OPERATIONS/(float)digits) ;

      // Subtraction:
      if (biga.compareTo(bigb)<0) { bigc=biga ; biga=bigb ; bigb=bigc ; hugec=hugea ; hugea=hugeb ; hugeb=hugec ; }
      HugeInteger.DIGIT_OPERATIONS = 0 ;
      bigc = biga.subtract(bigb) ;
      hugec = hugea.subtract(hugeb) ;
      if ( !bigc.toString().equals(hugec.toString()) ) correct=false ;
      System.out.println("  Subtraction operations/digits: " + HugeInteger.DIGIT_OPERATIONS/(float)digits) ;

      // Oops!
      if (!correct) { System.out.println("ERROR: " + a + " " + b) ; return ; }

    }

  }

  public static String makeHugeInteger(int numberOfDigits) {
    String result = "" ;
    Random R = new Random() ;
    for (int i=0 ; i<numberOfDigits ; i++) { result = result + R.nextInt(10) ; }
    return result ;
  }

}

/// End-of-File
