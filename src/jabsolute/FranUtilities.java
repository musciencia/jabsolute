package jabsolute;

public class FranUtilities {
     public static boolean StringContains (String s1, String s2 ) {
          boolean rv = false;
          if ( (s1.length() >= s2.length()) && (s2.length()>0) ) {
               for (int i = 0 ; i <= (s1.length() - s2.length()); i ++) {
                    rv = s1.regionMatches(true, i, s2, 0, s2.length());
                    if ( rv ) {break;}
               }
          }
          return rv;
     }
     
     public static void pauseOneSecond(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.err.println(ie);
            }            

     }

}