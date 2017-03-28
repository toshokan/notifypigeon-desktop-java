import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import org.gnome.gtk.*;
import org.gnome.notify.*;

public class NotifyPigeonListener{
	
	private static int aCounter = 0;
	private static Notification[] nArr = new Notification[10];
	private static long[] hashes = new long[10];

	public static void main(String[] args){
		Gtk.init(args);
		Notify.init("Hello World!");
		try{
			ServerSocket rx = new ServerSocket(1973);
			while(true){
				Socket tx = rx.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(tx.getInputStream()));
				String fromSocket = in.readLine();
				System.out.println(fromSocket);
				if(fromSocket.charAt(0) == 's'){
					spawnNotification(fromSocket);
				} else if (fromSocket.charAt(0) == 'u'){
					destroyNotification(fromSocket);
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public static void spawnNotification(String s){
		String[] tokens = s.split("" +(char) 31);
		long currentHash = tokens[2].hashCode(); 
		try{
			System.out.println("Here you go :) ");
			nArr[aCounter] = new Notification(tokens[1], tokens[2], "");
			hashes[aCounter] = currentHash;
			nArr[aCounter].show();
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
		String[] tokens = s.split("" + (char) 31);
		long currentHash = tokens[2].hashCode();

		int i;
		for(i = 0; i<9; i++){
			if(hashes[i] == currentHash){
				System.out.println("Found it! " + i);
				break;
			}
			if(i == 9 && hashes[i] != currentHash){
				i = -1;
				break;
			}
		}
		if(i != -1){
			System.out.println("Closing " + i);
			nArr[i].close();
			hashes[i] = -1;
		}
	}

}
