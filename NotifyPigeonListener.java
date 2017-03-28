import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import org.gnome.gtk.*;
import org.gnome.notify.*;

public class NotifyPigeonListener{
	
	// Create some global variables FIXME
	private static int aCounter = 0;
	private static Notification[] nArr = new Notification[10];
	private static long[] hashes = new long[10];

	public static void main(String[] args){
		// libnotify requirements
		Gtk.init(args);
		Notify.init("Hello World!");
		try{
			// Create a listener on a socket
			ServerSocket rx = new ServerSocket(1973);
			while(true){
				// Continuously read data sent to the socket
				Socket tx = rx.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(tx.getInputStream()));
				// Store received data
				String fromSocket = in.readLine();
				// Print some debug information FIXME
				System.out.println(fromSocket);
				if(fromSocket.charAt(0) == 's'){
					// If the command character is "s" for "show", show a notification.
					spawnNotification(fromSocket);
				} else if (fromSocket.charAt(0) == 'u'){
					// If the command character is "u" for "unshow", remove a notification.
					destroyNotification(fromSocket);
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public static void spawnNotification(String s){
		// Split the message assuming ASCII 31 Unit Separator delimieters
		String[] tokens = s.split("" +(char) 31);
		// Hash the text portion of the message
		long currentHash = tokens[2].hashCode(); 
		try{
			// Print debug information FIXME
			System.out.println("Here you go :) ");
			// Create a new notification at the next position in the array and
			// set it to have the correct information from the received message
			nArr[aCounter] = new Notification(tokens[1], tokens[2], "");
			// Also store the hash in the hash array in the same position
			hashes[aCounter] = currentHash;
			// Display the notification on screen (send to daemon)
			nArr[aCounter].show();
			// Loop back to position 0 in the array once we reach the end.
			// This means we only are only allowed to dismiss the last
			// 10 notifications. This is not a limitation as we would be
			// unlikely to receive more than 10 notifications within the 5
			// second default display time after which closing is pointless
			if(aCounter == 9){
				aCounter = 0;
			} else {
				aCounter++;
			}
			
		}catch(Exception e){
			//
		}
	}

	public static void destroyNotification(String s){
		// Split the message assuming ASCII 31 Unit Separator delimiters
		String[] tokens = s.split("" + (char) 31);
		// Hash the text portion of the message
		long currentHash = tokens[2].hashCode();

		// Do not destroy i after loop
		int i;
		for(i = 0; i<9; i++){
			// Loop through available positions in array
			if(hashes[i] == currentHash){
				// Compare the message to delete's hash to those stored in
				// the hash array.
				// Print debug information FIXME
				System.out.println("Found it! " + i);
				// Stop incrementing i once we've found it
				break;
			}
			if(i == 9 && hashes[i] != currentHash){
				// If we haven't found it, mark failure
				i = -1;
				break;
			}
		}
		if(i != -1){
			// If we haven't failed to find it, close it.
			// Print debug information FIXME
			System.out.println("Closing " + i);
			nArr[i].close();
			// Set the hash to an inaccessible value (for good measure, even
			// though a collision is very unlikely
			hashes[i] = -1;
		}
	}

}
