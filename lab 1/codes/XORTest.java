
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;


public class XORTest {
    
    public static void main ( String [] args ) throws Exception {
        
        // The system configuration
        Configuration conf = new Configuration(); 
        
        // Get an instance of the Filesystem
        FileSystem fs = FileSystem.get(conf);
        
        String path_name = "/user/shuowang/lab1/XORChecksumTestFile";
        
        Path path = new Path(path_name);
                
		// Open File for reading
		FSDataInputStream in = fs.open(path);
		
		// Create buffer to store data
		byte[] buffer = new byte[100];
		
        // Read all bytes whose offsets range from a till b from File into buffer.
        int bytesRead = in.read(0L,buffer,0,100);
		
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
		
		
		// show what we read
        System.out.println("Number of bytes read: "+bytesRead);
         System.out.print("Bytes read: ");
         
		 char c;
		 int i;
         // for each byte in buffer
         for(i = 0; i < bytesRead; i++)
         {
            // converts byte to character
            c=(char)buffer[i];
            
            // print
            System.out.println(c);
         }   
		
		
		// Close the file and the file system instance
        in.close(); 		
        fs.close();
        
    }
}