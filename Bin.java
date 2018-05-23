package carpool_master1;

import java.util.ArrayList;


public class Bin {

	public ArrayList <Passenger> plist = new ArrayList <Passenger>();
	int binDensity = 0;
	int xaxis = 0;
	int yaxis = 0;
	public Bin(int a, int b) {
		xaxis = a;
		yaxis = b;
	}
}
