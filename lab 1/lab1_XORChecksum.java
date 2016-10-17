
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class XORChecksum  {
    
    public static void main ( String [] args ) throws Exception {
        
        // The system configuration
        Configuration conf = new Configuration(); 
        
        // Get an instance of the Filesystem
        FileSystem fs = FileSystem.get(conf);
        
		String path_name = "/class/s15419x/lab1/bigdata";
		
        Path path = new Path(path_name);
        
		// Open File for reading
		FSDataInputStream in = fs.open(path);
		
		// Create buffer to store data
		byte[] buffer = new byte[1000];
		
        // Read all bytes whose offsets range from 5000000000 till 5000000999 from File into buffer.
        long location = 5000000000L;
		int offset = 0;
		int length = 1000;
				
		int bytesRead = in.read(location,buffer,offset,length);
		
		System.out.println("start byte #: " + Long.toString(location));		
		System.out.println("Number of bytes read: " + bytesRead);
		
		// Initiate xorChecksum
		byte xorChecksum = 0;
		
		// Create loop to compute xor byte by byte
		for (byte b:buffer)
		{
			xorChecksum ^= b;
		}
		
		// Display the byte XORChecksum and the 8-digit XORChecksum 
		System.out.println("byte XOR Checksum: " + xorChecksum);
		System.out.println("8-digit XOR Checksum: " + String.format("%8s", Integer.toBinaryString(xorChecksum & 0xFF)).replace(' ', '0'));
				
        // Close the file and the file system instance
        in.close(); 		
        fs.close();
        
    }
    
}
