package carpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Passenger {
	double oriX, oriY;
	double destX, destY;
	String name;
	int oria = 0; 
	int orib = 0;
	int desa = 0;
	int desb = 0;
	

void createPassenger() throws IOException {
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		name = br.readLine();
		oriX = Double.parseDouble(br.readLine());
		oriY = Double.parseDouble(br.readLine());
		destX = Double.parseDouble(br.readLine());
		destY = Double.parseDouble(br.readLine());
		
	}
BinNumber allocation() {
	
		BinNumber n = new BinNumber();
		if(oriX>22.454 && oriX<22.657 && oriY>88.281 && oriY<88.491) {
			n.a = (int) Math.floor((22.657-oriX)/0.00655);
			n.b = (int) Math.floor((88.491-oriY)/0.00954);
			oria = n.a;
			orib = n.b;
			
		}
		return n;
		
	}
}
