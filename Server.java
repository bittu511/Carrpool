package carpool;

import java.io.IOException;
import java.util.ArrayList;

public class Server {
	public static Bin bins[][] = new Bin[31][22];
	
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
		d.a = P.oria;
		d.b = P.orib;
				
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
		//finding the neighbouring bins with max density
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
				int k = 0;
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
		int manhattanD =  manhattan(d.p1.desa, p.desa, d.p1.desb, p.desb);
		int manhattanO =  manhattan(d.a, p.oria, d.b, p.orib);
		
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
	
}