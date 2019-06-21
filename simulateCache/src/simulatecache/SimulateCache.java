package simulatecache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rm631
 */
public class SimulateCache {
    
    private static Map<Integer,String> map = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        File dir = new File("cache-tests");
        Scanner read = null;
        BufferedWriter writer = null;
        File[] files = dir.listFiles();
        if(files != null) {
            for(File file : files) {
                System.out.println(file);
                try {
                    String output = "";
                    read = new Scanner(file);
                    int i = file.getName().lastIndexOf(".");
                    String fileName = file.getName().substring(0,i); // filename without extension
                    writer = new BufferedWriter(new FileWriter("cache-tests/outputs/" + fileName + ".out"));
                    read.useDelimiter("\\n| ");
                    int line = 0; // we have some things to do only for the first line. Line will also act as our key
                    while(read.hasNext()) {
                        if(line == 0) {
                            // cast the 4 integers to variables W,C,B and k
                            int W = Integer.parseInt(read.next()); // bits in an address
                            int C = Integer.parseInt(read.next()); // bytes in cache
                            int B = Integer.parseInt(read.next()); // 1 block has B bytes
                            int k = Integer.parseInt(read.next().trim()); // k lines in 1 block, trim removes the \n char
                            
                            // each block contains k lines, so the simulation cannot go above size k
                            map = new LRULinkedHashMap<>(k, .75f, true);
                            
                            line++;
                        } else {
                            String dec = read.next();
                            String binary = "";
                            if(!dec.trim().isEmpty()) {
                                try {
                                    long decimal = Long.parseLong(dec.trim());
                                    binary = Long.toBinaryString(decimal); // this is the binary value of the mem address
                                } catch (NumberFormatException ex) {
                                    // this should catch the BIG numbers in the big files and avoid breaking everything else
                                    
                                    BigInteger decimal = new BigInteger(dec.trim());
                                    binary = decimal.toString(2); // string in base 2
                                }
                                if(searchForValue(binary)) {
                                    output += "C";
                                    map.put(line, binary);
                                } else {
                                    output += "M";
                                    map.put(line, binary);
                                }
                            }
                            line++;
                        }
                    }
                    
                    writer.write(output);
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if(read != null) {read.close();}
                        if(writer != null) {writer.close();} // catch this
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * Since each block contains B=64 bytes, there are C/B=1024/64=16 blocks. Since 16=24, we can identify blocks using a 4-bit index.
     * We seem to need to identify the block indexs as they act as separate map
     */
    private static void setup() {
        
    }
    
    /**
     * This method traverses the map and tries to find the binary string..
     * @param binary to find in map
     * @return true if value was in 'cache'
     */
    private static boolean searchForValue(String binary) {
        for(Map.Entry<Integer,String> entry : map.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            // if we find the value in the map..
            if(binary.equals(value)) {
                return true;
            }
        }
        return false; 
    }    
}
