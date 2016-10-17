
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class BigDataRead  {
    
    public static void main ( String [] args ) throws Exception {
        
        // The system configuration
        Configuration conf = new Configuration(); 
        
        // Get an instance of the Filesystem
        FileSystem fs = FileSystem.get(conf);
        
			
        String inFile_path_name = "/class/s15419x/lab1/bigdata";
		//String outFile_path_name = "/user/shuowang/lab1/bigdataread";
        
        Path inFile = new Path(inFile_path_name);
        //Path outFile = new Path(outFile_path_name);
		
		//FSDataOutputStream file = fs.create(outFile);
		
		// Open inFile for reading
		FSDataInputStream in = fs.open(inFile);
		
		// Open outFile for writing
		//FSDataOutputStream out = fs.create(outFile);
		
		byte[] buffer = new byte[1000];
		
        // Read from input stream and write to output stream until EOF.
        int bytesRead = in.read(5000000000L,buffer,0,1000);
		//out.write(buffer, 0, bytesRead);
		
		byte xorChecksum = 0;
		
		for (byte b:bs)
		{
			xorChecksum ^= b;
		}
		System.out.println("XOR Checksum in byte is : " + xorChecksum);
		System.out.println("XOR Checksum in binary array is : " + Integer.toBinaryString(xorChecksum));
		
		            
        // Close the file and the file system instance
        in.close(); 
		out.close(); 
        fs.close();
        
    }
    
}
