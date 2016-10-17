/* Generated by Streams Studio: April 15, 2015 10:09:59 PM EDT */
package application;


import org.apache.log4j.Logger;

import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamingData.Punctuation;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.StreamingOutput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.model.InputPortSet;
import com.ibm.streams.operator.model.InputPortSet.WindowMode;
import com.ibm.streams.operator.model.InputPortSet.WindowPunctuationInputMode;
import com.ibm.streams.operator.model.InputPorts;
import com.ibm.streams.operator.model.OutputPortSet;
import com.ibm.streams.operator.model.OutputPortSet.WindowPunctuationOutputMode;
import com.ibm.streams.operator.model.OutputPorts;
import com.ibm.streams.operator.model.PrimitiveOperator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * Class for an operator that receives a tuple and then optionally submits a tuple. 
 * This pattern supports one or more input streams and one or more output streams. 
 * <P>
 * The following event methods from the Operator interface can be called:
 * </p>
 * <ul>
 * <li><code>initialize()</code> to perform operator initialization</li>
 * <li>allPortsReady() notification indicates the operator's ports are ready to process and submit tuples</li> 
 * <li>process() handles a tuple arriving on an input port 
 * <li>processPuncuation() handles a punctuation mark arriving on an input port 
 * <li>shutdown() to shutdown the operator. A shutdown request may occur at any time, 
 * such as a request to stop a PE or cancel a job. 
 * Thus the shutdown() may occur while the operator is processing tuples, punctuation marks, 
 * or even during port ready notification.</li>
 * </ul>
 * <p>With the exception of operator initialization, all the other events may occur concurrently with each other, 
 * which lead to these methods being called concurrently by different threads.</p> 
 */
@PrimitiveOperator(name="task4", namespace="application",
description="Java Operator task4")
@InputPorts({@InputPortSet(description="Port that ingests tuples", cardinality=1, optional=false, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious), @InputPortSet(description="Optional input ports", optional=true, windowingMode=WindowMode.NonWindowed, windowPunctuationInputMode=WindowPunctuationInputMode.Oblivious)})
@OutputPorts({@OutputPortSet(description="Port that produces tuples", cardinality=1, optional=false, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating), @OutputPortSet(description="Optional output ports", optional=true, windowPunctuationOutputMode=WindowPunctuationOutputMode.Generating)})
public class task4 extends AbstractOperator {
	
	// reserve 4 global variables
	// Rooms stores the children visited each room during last 5 min and weather they are still in the room or not
	// currenttime keeps recording the current system time
	// starttime is the time when the operator starts
	// df is the timestamp format we use here, which matches with the timestamp in the streams
	// the IBM infosphere streams system time is one hour ahead of our time
	// and we can not change it since we don't know the root password
	// so use the system time - 1 hour to get the real time.
	public Map<String, HashMap<String,String>> Rooms; 
	public long starttime;
	public long currenttime;
	public DateFormat df = new SimpleDateFormat("HH:mm:ss:sss");  
	
    /**
     * Initialize this operator. Called once before any tuples are processed.
     * @param context OperatorContext for this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
	@Override
	public synchronized void initialize(OperatorContext context)
			throws Exception {
    	// Must call super.initialize(context) to correctly setup an operator.
		super.initialize(context);
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " initializing in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
        
        // Initialization of the global variables
		     
    	java.util.Date date= new java.util.Date();
    	starttime = date.getTime()-60*60*1000;
    	currenttime = starttime;
    	
    	Rooms = new HashMap<String, HashMap<String,String>>();
    	for (int i=0; i<27; i++)
    	{
    		Rooms.put(Integer.toString(i), new HashMap<String,String>());
    	}

        
        
	}

    /**
     * Notification that initialization is complete and all input and output ports 
     * are connected and ready to receive and submit tuples.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public synchronized void allPortsReady() throws Exception {
    	// This method is commonly used by source operators. 
    	// Operators that process incoming tuples generally do not need this notification. 
        OperatorContext context = getOperatorContext();
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " all ports are ready in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
    }

    /**
     * Process an incoming tuple that arrived on the specified port.
     * <P>
     * Copy the incoming tuple to a new output tuple and submit to the output port. 
     * </P>
     * @param inputStream Port the tuple is arriving on.
     * @param tuple Object representing the incoming tuple.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public final void process(StreamingInput<Tuple> inputStream, Tuple tuple)
            throws Exception {
    	// Input tuple will have the following attributes.
    	// 1) String TimeStamp
    	// 2) String SensorID
    	// 3) String ChildID
    	// 4) String FromRoom
    	// 5) String ToRoom
		
		// update the current time
    	java.util.Date date= new java.util.Date();
    	currenttime=date.getTime()-60*60*1000;
    	
		// add child to his entered room with the status "1" 	
    	HashMap<String,String> b1 = Rooms.get(tuple.getString("ToRoom"));
    	b1.put(tuple.getString("ChildID"),"1");
    	Rooms.put(tuple.getString("ToRoom"),b1);
    	// change the child's status in his left room to "0"
    	HashMap<String,String> b2 = Rooms.get(tuple.getString("FromRoom"));
    	if (b2.containsKey(tuple.getString("ChildID")))
    	{
    		b2.put(tuple.getString("ChildID"),"0");
    		Rooms.put(tuple.getString("FromRoom"),b2);
    	}
    	
    	// ouput every 5 min (300000 milliseconds)
    	if (currenttime>starttime+300000)
    	{
    	starttime=starttime+300000;    	
    	  	
    	// iterate though each room
    	for (Map.Entry<String, HashMap<String,String>> entry : Rooms.entrySet())
    	{
    	 
    	// Create a new tuple for output port 0
        StreamingOutput<OutputTuple> outStream = getOutput(0);
        OutputTuple outTuple = outStream.newTuple();

        HashMap<String,String> x = entry.getValue();
        
        outTuple.setString("contents", df.format(currenttime)+", "+entry.getKey()+", "+x.size());
        
        // Submit new tuple to output port 0
        outStream.submit(outTuple);
    	}
    	
		// re-set the room info, remove all the children with status "0" (not currently in the room)
    	for (Map.Entry<String, HashMap<String,String>> entry : Rooms.entrySet())
    	{
    		HashMap<String,String> x = entry.getValue();
    		x.values().removeAll(Collections.singleton("0"));
    		Rooms.put(entry.getKey(), x);
    	}
    	}
    }
    
    /**
     * Process an incoming punctuation that arrived on the specified port.
     * @param stream Port the punctuation is arriving on.
     * @param mark The punctuation mark
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public void processPunctuation(StreamingInput<Tuple> stream,
    		Punctuation mark) throws Exception {
    	// For window markers, punctuate all output ports 
    	super.processPunctuation(stream, mark);
    }

    /**
     * Shutdown this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    public synchronized void shutdown() throws Exception {
        OperatorContext context = getOperatorContext();
        Logger.getLogger(this.getClass()).trace("Operator " + context.getName() + " shutting down in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );
        
        // TODO: If needed, close connections or release resources related to any external system or data store.

        // Must call super.shutdown()
        super.shutdown();
    }
}
