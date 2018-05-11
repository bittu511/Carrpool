package carpool;

import java.io.IOException;
import java.util.ArrayList;

public class Server {
	public static Bin bins[][] = new Bin[31][22];
	
	
	public static ArrayList <Path> allPaths = new ArrayList <Path>();
	
	
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
	public static int manhattan (double x, double y) {
		int manh =0 ;
//		needs to finish
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
			for( int x = 0; x < plist.size(); x++) {
				if (d.dbin == plist.get(x).ori) {
					d.plist.add(plist.get(x));
					path.add(plist.get(x).dest);
					
				}
					
					
				if (d.dbin == plist.get(x).dest) {
					d.plist.remove(plist.get(x));
					d.pcount--;
				}
			}
		if(d.pcount == 3) {
			path.add(d.p1.dest);
			//finds the current index of the driver on the path
			int i = path.indexOf(d.dbin);
			
			//	path optimize function to be added	
			path = optimize(i, path, d.p1.dest, d);
			//if car is full then continue on path
			i = path.indexOf(d.dbin);
			d.dbin = path.get(i+1);
			routes(path, d, plist);
		}
		else {
			//if there'are vacant seats then find new passengers
				ArrayList <Passenger> neighbourlist = neighbours(d);
				neighbourlist.removeAll(plist);   //passengers who already served in route must be overlooked
				int k = 0;
				while(k < 3) {
					Passenger bestp = bestPassenger(neighbourlist, d);
					neighbourlist.remove(bestp);
					
					path.add(bestp.ori);
					
					//finds the current index of the driver on the path
					int i = path.indexOf(d.dbin);
					
					//a function needed to organize " ArrayList <BinNumber> path "
					
					path = optimize(i, path, d.p1.dest, d);
					plist.add(bestp);
					//as the path is organized next index will lead to the final destination
					d.dbin = path.get(i+1);
					d.pcount++;
					routes(path, d, plist);
					d.pcount--;
					path.remove(bestp.ori);
					plist.remove(bestp);
					
					k++;
					
				}
			}
		}
	}
	public static ArrayList <Passenger> neighbours(Driver d) {
		ArrayList <Passenger> plist = new ArrayList <Passenger>();
		//receives a driver and generates its 9 neighbours
		//finds the max bins
		int max = bins[d.dbin.a][d.dbin.b].binDensity;
		for(int i=d.dbin.a-1 ; i<d.dbin.a+2;i++) {
			for(int j=d.dbin.b-1 ; j<d.dbin.b+2;j++) {
				if(i>=0 || i<31) {
					if(j>=0 || j<22) {
						if(max < bins[i][j].binDensity) {
							max = bins[i][j].binDensity;
						}
					}
				}
			}
											
		}
		//takes all the passengers from max bins and puts them in a arraylist of passengers
		for(int i=d.dbin.a-1 ; i<d.dbin.a+2;i++) {
			for(int j=d.dbin.b-1 ; j<d.dbin.b+2;j++) {
				if(i>=0 || i<31) {
					if(j>=0 || j<22) {
						if(max == bins[i][j].binDensity) {
							 plist.addAll(bins[i][j].plist);
						}
					}
				}
			}
											
		}
		//returns the arraylist
		return plist;
	}
	public static Passenger bestPassenger(ArrayList <Passenger> plist, Driver d) {
		Passenger p = new Passenger();
		//selects the best passenger for driver d from plist
		ArrayList <Passenger> list = new ArrayList <Passenger>();
		list = plist;
		double dis = distortion( d , list.get(0) );//cost for distortion
		double backtrack = BacktrackCost( d , list.get(0));//cost for backtrack
		double lcost = dis + backtrack;
		p = list.get(0);
		for( int i = 0 ; i < list.size() ; i++){
		 dis = distortion( d , list.get(i) );
		 backtrack = BacktrackCost( d , list.get(0));
		 double cost = dis + backtrack;
		 if( lcost < cost  ) {
			 lcost = cost;
			 p = list.get(i);//return best passenger
		 }
		}
		return p;
	}
	public static double BacktrackCost( Driver d , Passenger p ){
		    //cost calculation if the car backtracks
		    double globalx = d.p1.dest.a - d.p1.ori.a;
		    double globaly = d.p1.dest.b - d.p1.ori.b;
		    double localx = p.ori.a - d.dbin.a;
		    double localy = p.ori.b - d.dbin.b;
		    double currentx = d.p1.dest.a - d.dbin.a;
		    double currenty = d.p1.dest.b - d.dbin.b;
		    double valuex , valuey;
		    if( globalx * localx <= 0 ) {
		    	valuex = Math.abs( localx ); //if backtrack occurs along in X direction
		    }
		    else
			valuex = 0; //if no backtrack occurs along in X direction
		    if( globaly * localy <= 0 ){
			    valuey = Math.abs( localy );//if backtrack occurs along in Y direction
		    }
		    else
	               valuey = 0;// if no backtrack occurs along in Y direction
		    double backtrack = valuex + valuey;
		    double r1 = ( 1 + 0.25 );
		    double r2 = 1 + (0.25 * manhattan( currentx , currenty ) < manhattan( globalx , globaly ) ? (1 - manhattan( currentx , currenty ) / manhattan( globalx , globaly )) : 0);
		    return Math.pow( backtrack , (r1 * r2) );//cost will increase exponent wise
		    }
    
	public static ArrayList <BinNumber>  optimize(int b, ArrayList <BinNumber> oldpath, BinNumber l, Driver d) {
		ArrayList <BinNumber> newp = new ArrayList <BinNumber>();
		ArrayList <BinNumber> oldp = new ArrayList <BinNumber>();
		int i=0;
		while(b != i) {
			oldp.add(oldpath.get(i));
			i++;
		}
		oldp.add(oldpath.get(i));
		
		while(i != oldpath.size()-1) {
			newp.add(oldpath.get(i));
			i++;
		}
		newp = arrange(oldp.get(oldp.size()-1), newp, l);
		oldp.addAll(newp);
		return oldp;
		
	}
	
	public static ArrayList <BinNumber>  arrange(BinNumber b, ArrayList <BinNumber> p, BinNumber l) {
		Route_hop hop = new Route_hop();
		ArrayList <BinNumber> fin = new ArrayList <BinNumber>();
    	fin.addAll(hop.arrange(b, p, l));
		return fin;
	}
}
