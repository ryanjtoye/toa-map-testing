import javax.imageio.*;
import java.io.*;
import java.awt.Image.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GenerateMap
{   
   static int mapSize = 100;
   static Random r = new Random();
   
   public static void main(String[] args) {
      String[][] map = new String[mapSize][mapSize];
      map = populateWithRivers(map);
      map = populateWithMountains(map);
      map = populateWithForests(map);
      map = populateRest(map);
      HashMap<String, ArrayList> HMap = MapHash(map);
      writeImage(map);
       writeHashImage(HMap);
    
    //  ArrayList zero = HMap.get("0,0");
    //  String zerotile = zero.get(0).toString();
    //  ArrayList test = HMap.get("10,30");
    //  String testtile = test.get(0).toString();
   
   }
   
   public static int adjacentCount(String[][] map, int x, int y, String tileType) {
      int count = 0;
      for(int xMod = -1; xMod <= 1; xMod++) {
         for(int yMod = -1; yMod <= 1; yMod++) {
            if(!(x + xMod < 0 || x + xMod >= mapSize || y + yMod < 0 || y + yMod >= mapSize)) {
               if(map[x + xMod][y + yMod] != null) {
                  if(map[x + xMod][y + yMod].equals(tileType)) {
                     count++;
                  }
               }
            }
         }
      }
      return count;
   }

   public static String[][] populatePOI(String[][] map) {
      int count = 200 + r.nextInt(10);
      for(int i = 0; i < count; i++) {
         int x = r.nextInt(mapSize);
         int y = r.nextInt(mapSize);
         if(adjacentCount(map, x, y, "mountain") > 7 || (map[x][y] != null && map[x][y].equals("water"))) {
            i--;
         } else {
            map[x][y] = "poi";
         }
      }
      return map;
   }

   public static String[][] populateWithMountains(String[][] map) {
      OpenSimplexNoise noise = new OpenSimplexNoise(10 + (new Date()).getTime());
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            double val = noise.eval(x / 10.0,y / 10.0);
            if(map[x][y] == null && val > .6f) {
               map[x][y] = "mountain";
            }
         }
      }
      return map;
   }

   public static String[][] populateWithForests(String[][] map) {
      OpenSimplexNoise noise = new OpenSimplexNoise(1000 + (new Date()).getTime());
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            double val = noise.eval(x / 9.0,y / 9.0);
            if(map[x][y] == null && val > .2f) {
               map[x][y] = "forest";
            }
         }
      }
      return map;
   }

   public static String[][] populateWithRivers(String[][] map) {
      OpenSimplexNoise noise = new OpenSimplexNoise(100 + (new Date()).getTime());
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            double val = noise.eval(x / 60.0,y / 60.0);
            if(map[x][y] == null && (val > -0.06f && val < 0.06f)) {
               map[x][y] = "water";
            }
         }
      }
      return map;
   }

   public static String[][] populateRest(String[][] map) {
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            if(map[x][y] == null) {
               map[x][y] = "plains";
            }
         }
      }
      return map;
   }
   
   public static void writeImage(String[][] map) {
      BufferedImage bi = new BufferedImage(mapSize,mapSize,BufferedImage.TYPE_INT_ARGB);
      File outputfile = new File("saved.png");
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            if(map[x][y] == null) {
               bi.setRGB(x,y, new Color(0, 0, 0).getRGB());
            } else {
               switch(map[x][y]) {
                  case "water": 
                     {
                        bi.setRGB(x,y, new Color(24,95,173).getRGB());
                        break;
                     }
                  case "plains": 
                     {
                        bi.setRGB(x,y, new Color(230,204,29).getRGB());
                        break;
                     }
                  case "forest": 
                     {
                        bi.setRGB(x,y, new Color(24,158,73).getRGB());
                        break;
                     }
                  case "mountain": 
                     {
                        bi.setRGB(x,y, new Color(111, 121, 137).getRGB());
                        break;
                     }
                  case "poi": 
                     {
                        bi.setRGB(x,y, new Color(255, 0, 0).getRGB());
                        break;
                     }
               }
            }
         }
      }
      try{
         ImageIO.write(bi, "png", outputfile);
      } catch (Exception e) {
         
      }
   }
   
  
   
   //return structure will be a hashmap of { Coordinate String : [Tile type, Reasource, Reasource #, Structure) } 
   public static HashMap<String, ArrayList> MapHash(String[][] map) {
      HashMap<String, ArrayList> HMap = new HashMap<String, ArrayList>();  
      for(int x = 0; x < mapSize; x++) {
         for(int y = 0; y < mapSize; y++) {
            if(map[x][y] == null) {
            
            } else {
               switch(map[x][y]) {
                  case "water": 
                     {
                        ArrayList MapItems = new ArrayList();
                        MapItems.add("water");  //Tile type
                        Random rand0 = new Random();
                        Random rand1 = new Random();
                        int  randomR = rand1.nextInt(4) + 1;            
                        int  randomRN = rand1.nextInt(10) + 1;
                        MapItems.add(randomR); //What type of reasource is found
                        MapItems.add(randomRN); //How much of a reasource is found
                        MapItems.add("structure"); //structure that is found
                        StringBuilder sb = new StringBuilder();
                     
                        String coX = new Integer(x).toString();
                        String coY = new Integer(y).toString();
                        sb.append(coX);
                        sb.append(",");
                        sb.append(coY);
                     
                        HMap.put(sb.toString(), MapItems); 
                        break;
                     }
                  case "plains": 
                     {
                        ArrayList MapItems = new ArrayList();
                        MapItems.add("plains");  //Tile type
                        Random rand0 = new Random();
                        Random rand1 = new Random();
                        int  randomR = rand1.nextInt(4) + 1;            
                        int  randomRN = rand1.nextInt(10) + 1;
                        MapItems.add(randomR); //What type of reasource is found
                        MapItems.add(randomRN); //How much of a reasource is found
                        MapItems.add("structure"); //structure that is found
                        StringBuilder sb = new StringBuilder();
                     
                        String coX = new Integer(x).toString();
                        String coY = new Integer(y).toString();
                        sb.append(coX);
                        sb.append(",");
                        sb.append(coY);
                     
                        HMap.put(sb.toString(), MapItems); 
                        break;
                     }
                  case "forest": 
                     {
                        ArrayList MapItems = new ArrayList();
                        MapItems.add("forest");  //Tile type
                        Random rand0 = new Random();
                        Random rand1 = new Random();
                        int  randomR = rand1.nextInt(4) + 1;            
                        int  randomRN = rand1.nextInt(10) + 1;
                        MapItems.add(randomR); //What type of reasource is found
                        MapItems.add(randomRN); //How much of a reasource is found
                        MapItems.add("structure"); //structure that is found
                        StringBuilder sb = new StringBuilder();
                     
                        String coX = new Integer(x).toString();
                        String coY = new Integer(y).toString();
                        sb.append(coX);
                        sb.append(",");
                        sb.append(coY);
                     
                        HMap.put(sb.toString(), MapItems); 
                        break;
                     }
                  case "mountain": 
                     {
                        ArrayList MapItems = new ArrayList();
                        MapItems.add("mountain");  //Tile type
                        Random rand0 = new Random();
                        Random rand1 = new Random();
                        int  randomR = rand1.nextInt(4) + 1;            
                        int  randomRN = rand1.nextInt(10) + 1;
                        MapItems.add(randomR); //What type of reasource is found
                        MapItems.add(randomRN); //How much of a reasource is found
                        MapItems.add("structure"); //structure that is found
                        StringBuilder sb = new StringBuilder();
                     
                        String coX = new Integer(x).toString();
                        String coY = new Integer(y).toString();
                        sb.append(coX);
                        sb.append(",");
                        sb.append(coY);
                     
                        HMap.put(sb.toString(), MapItems); 
                        break;
                     }
               }
            }
         }
      }
      return HMap;
   }

   public static void writeHashImage(HashMap<String, ArrayList> HMap) {
      BufferedImage bi = new BufferedImage(mapSize,mapSize,BufferedImage.TYPE_INT_ARGB);
      File outputfile = new File("Hashsaved.png");
   
      Iterator HMapit = HMap.entrySet().iterator();
      while (HMapit.hasNext()) {
         Map.Entry pair = (Map.Entry)HMapit.next();
      //   int coo = Integer.parseInt(pair.getKey().toString());
         String coS = pair.getKey().toString();
        
         String array1[]= coS.split(",");
         int x = Integer.parseInt(array1[0]);
         int y = Integer.parseInt(array1[1]);
         String type = HMap.get(coS).get(0).toString();
         switch(type) {
            case "water": 
               {
                  bi.setRGB(x,y, new Color(24,95,173).getRGB());
                  break;
               }
            case "plains": 
               {
                  bi.setRGB(x,y, new Color(230,204,29).getRGB());
                  break;
               }
            case "forest": 
               {
                  bi.setRGB(x,y, new Color(24,158,73).getRGB());
                  break;
               }
            case "mountain": 
               {
                  bi.setRGB(x,y, new Color(111, 121, 137).getRGB());
                  break;
               }
            case "poi": 
               {
                  bi.setRGB(x,y, new Color(255, 0, 0).getRGB());
                  break;
               }
         }
      
      }
         try{
         ImageIO.write(bi, "png", outputfile);
      } catch (Exception e) {
         
      }

   }

}
