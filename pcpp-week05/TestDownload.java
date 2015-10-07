// For week 5
// sestoft@itu.dk * 2014-09-19

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestDownload {

  private static final ExecutorService executor = Executors.newWorkStealingPool();

  private static final String[] urls = 
  { "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
    "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
    "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
    "http://www.ing.dk", "http://www.dtu.dk", "http://www.eb.dk", 
    "http://www.nytimes.com", "http://www.guardian.co.uk", "http://www.lemonde.fr",   
    "http://www.welt.de", "http://www.dn.se", "http://www.heise.de", "http://www.wsj.com", 
    "http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov" 
  };

  public static void main(String[] args) throws IOException {
    // String url = "https://www.wikipedia.org/";
    // String page = getPage(url, 10);
    // System.out.printf("%-30s%n%s%n", url, page);
    System.out.println("Loading...");

    for (int i = 0; i < 5; i++) {
      Timer t = new Timer();
      Set<Map.Entry<String,String>> s = getPagesParallel(urls, 200).entrySet();
      System.out.println(t.check());
      for (Map.Entry<String, String> entry : s) {
        String out = String.format("%s, %d", entry.getKey(), entry.getValue().length());
        System.out.println(out);
      }
    }
  }

  public static String getPage(String url, int maxLines) throws IOException {
    // This will close the streams after use (JLS 8 para 14.20.3):
    try (BufferedReader in 
         = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      StringBuilder sb = new StringBuilder();
      for (int i=0; i<maxLines; i++) {
        String inputLine = in.readLine();
        if (inputLine == null)
          break;
        else
          sb.append(inputLine).append("\n");
      }
      return sb.toString();
    }
  }

  public static Map<String,String> getPages(String[] urls, int maxLines) throws IOException {
    Map<String, String> map = new HashMap<>();
    for (String url : urls) {
      try (BufferedReader in 
           = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<maxLines; i++) {
          String inputLine = in.readLine();
          if (inputLine == null)
            break;
          else
            sb.append(inputLine).append("\n");
        }
        map.put(url, sb.toString()) ;
      }
    }
    return map;
  }

  public static Map<String, String> getPagesParallel(String[] urls, int maxLines) throws IOException {
    List<Future<?>> futures = new ArrayList<Future<?>>();
    Map<String, String> map = new HashMap<>();
    for (String url : urls){
      futures.add(executor.submit(() -> {
        try{
          map.put(url, getPage(url, maxLines));
        } catch(Exception e){
          System.out.print(e.getMessage());
        }
      }));
    }
    try {
      for (Future<?> fut : futures)
        fut.get();
    } catch (InterruptedException exn) { 
      System.out.println("Interrupted: " + exn);
    } catch (ExecutionException exn) { 
      throw new RuntimeException(exn.getCause()); 
    }
    return map;


  }
}

