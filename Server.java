package carpool;

import java.io.IOException;
import java.util.ArrayList;

public class Server {
	public static Bin bins[][] = new Bin[31][22];
	
	
	public static ArrayList <Path> allPaths;
	
	
	public static void main(String[] args) throws IOException {
		for(int i=0;i<31;i++) {
			for(int j=0;j<22;j++) {
				bins[i][j] = new Bin(i,j);
			}
		}
		passengerGeneration();
		Driver d = new Driver();
		Passenger P = new Passenger();
		P.createPassenger();
		d.p1 = P;
		d.x = P.oriX;
		d.y = P.oriY;
		d.dbin.a = P.ori.a;
		d.dbin.b = P.ori.b;
				
	}
	
	static void passengerGeneration() throws IOException {
		Passenger[] plist = new Passenger[1];	//Should be more than 100
		for(int i = 0; i < plist.length; i++) {
			plist[i] = new Passenger();
			plist[i].createPassenger();
			BinNumber n = plist[i].allocation();
			if(plist[i].oriX>22.454 && plist[i].oriX<22.657 && plist[i].oriY>88.281 && plist[i].oriY<88.491) {
				bins[n.a][n.b].plist.add(plist[i]);
				bins[n.a][n.b].binDensity++;
			}
			else
				System.out.println("Sorry! No Service");
			
		}
		
	}
	public static ArrayList<Bin> Maximun_Density_Bins(Driver d) { 
		//finding the neighboring bins with max density
		ArrayList <Bin> list = new ArrayList <Bin>();
		int dx = (int) Math.floor((22.657-d.x)/0.00655);
		int dy = (int) Math.floor((88.491-d.y)/0.00954);
		int max = bins[dx][dy].binDensity;
		for(int i=dx-1 ; i<dx+2;i++) {
			for(int j=dy-1 ; j<dy+2;i++) {
				if(i>=0 || i<31) {
					if(j>=0 || j<22) {
						if(max < bins[i][j].binDensity) {
							max = bins[i][j].binDensity;
						}
					}
				}
			}
											
		}
		for(int i=dx-1 ; i<dx+2;i++) {
			for(int j=dy-1 ; j<dy+2;i++) {
				if(i>=0 || i<31) {
					if(j>=0 || j<22) {
						if(max == bins[i][j].binDensity) {
							 list.add(bins[i][j]);
						}
					}
				}
			}
											
		}
		return list;
		
	}
	
	public static int distortion (Driver d , Passenger p) {
		int manhattanD =  manhattan(d.p1.dest.a, p.dest.a, d.p1.dest.b, p.dest.b);
		int manhattanO =  manhattan(d.dbin.a, p.ori.a, d.dbin.b, p.ori.b);
		
		return manhattanD + manhattanO;
		
	}
	
	public static int cost (int ox, int dx, int oy, int dy) {
		int c = manhattan(ox, dx, oy, dy);
		if(c == 0)
			return 5;
		else 
			return c*10;
	}
	public static int manhattan (int ox, int dx, int oy, int dy) {
		int manh =  Math.abs(ox - dx) + Math.abs(oy - dy);
		
		return manh;
		
	}
	public static void routes(ArrayList <BinNumber> path, Driver d, ArrayList <Passenger> plist) {
		if((path.get(path.size() - 1).a == d.p1.dest.a) && (path.get(path.size() - 1).b == d.p1.dest.b)){
			Path p = new Path();
			p.binno.addAll(path);
			allPaths.add(p);
			//if destination matches to final one then make it a route and store
		}
		else {
			//first the current bin is to be checked for passenger pick and/or drop
			//that procedure remains
			ArrayList <Passenger> neighbourlist = neighbours(d);
			int k = 0;
			while(k < 3) {
				Passenger bestp = bestPassenger(neighbourlist, d);
				neighbourlist.remove(bestp);
				
				path.add(bestp.ori);
				path.add(bestp.dest);
				//a function needed to organize " ArrayList <BinNumber> path "
				plist.add(bestp);
				int i = path.indexOf(d.dbin);
				//finds the current index of the driver on the path and
				//as the path is organized next index will lead to the final destination
				d.dbin = path.get(i+1);
				routes(path, d, plist);
				
				path.remove(bestp.ori);
				path.remove(bestp.dest);
				plist.remove(bestp);
				
				k++;
				
			}
		}
		
	}
	public static ArrayList <Passenger> neighbours(Driver d) {
		ArrayList <Passenger> plist = new ArrayList <Passenger>();
		//receives a driver and generates its 9 neighbours
		//finds the max bins
		//takes all the passengers from max bins and puts them in a arraylist of passengers
		//returns the arraylist
		return plist;
	}
	public static Passenger bestPassenger(ArrayList <Passenger> plist, Driver d) {
		Passenger p = new Passenger();
		//selects the best passenger for driver d from plist
		return p;
	}
	
}
