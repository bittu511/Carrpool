package carpool_master1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
		P.name = "z";
		P.ori.a = 5;
		P.ori.b = 1;
		P.dest.a = 5;
		P.dest.b = 7;
//		P.createPassenger();
		d.p1 = P;
//		d.x = P.oriX;
//		d.y = P.oriY;
		d.dbin.a = P.ori.a;
		d.dbin.b = P.ori.b;
		
		ArrayList <BinNumber> binlist = new ArrayList <BinNumber>();
		binlist.add(d.dbin);
		ArrayList <Passenger> passengerlist = new ArrayList <Passenger>();
		
		routes(binlist, d, passengerlist);
				
	}
	static int arr_oriX[] = {5,5,5,5,5,5,5,5,4,4,4,4,4,4,5,6,6,6,7,7,6};
    static int arr_oriY[] = {0,0,1,1,2,2,2,2,3,3,5,5,5,5,5,4,6,4,6,3,3};
    static int arr_destX[] = {4,4,6,6,6,5,3,2,6,5,3,5,7,7,7,7,7,3,3,2,3};
    static int arr_destY[] = {2,1,1,3,3,4,4,4,5,4,6,6,5,5,2,1,4,3,3,5,2};
    static String arr_name[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u"};
	static void passengerGeneration() throws IOException {
		Passenger[] plist = new Passenger[21];//Should be more than 100
		for(int i = 0; i < plist.length; i++) {
			plist[i] = new Passenger();
			plist[i].name = arr_name[i];
			plist[i].ori.a = arr_oriX[i];
			plist[i].ori.b = arr_oriY[i];
			plist[i].dest.a = arr_destX[i];
			plist[i].dest.b = arr_destY[i];
	//		plist[i].createPassenger();
			//BinNumber n = plist[i].allocation();
			//if(plist[i].ori.a>22.454 && plist[i].dest.a<22.657 && plist[i].oriY>88.281 && plist[i].oriY<88.491) {
				bins[plist[i].ori.a][plist[i].ori.b].plist.add(plist[i]);
				bins[plist[i].ori.a][plist[i].ori.b].binDensity++;
//			}
//			else
//				System.out.println("Sorry! No Service");
//			
		}
		
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
	public static double manhattan (double x, double y) {
		double manh = 0;
		manh = x + y;
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
			    ArrayList <Passenger> neighbourlist = new ArrayList <Passenger>();
				neighbourlist.addAll(neighbours(d));
//				neighbourlist.removeAll(plist);//passengers who already served in route must be overlooked
				int k = 0;
				while(k < 3) {
					
					Passenger bestp = bestPassenger(neighbourlist, d);
					neighbourlist.remove(bestp);
					
					path.add(bestp.ori);
					for(int i = 0; i < path.size() ; i++ ) {
						System.out.print(path.get(i).a + path.get(i).b);
					}
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
						if(i != d.dbin.a && j != d.dbin.b) {
							if(max < bins[i][j].binDensity) {
								max = bins[i][j].binDensity;
							}
						}
					}
				}
			}
											
		}
		//takes all the passengers from max bins and puts them in a arraylist of passengers
		plist.addAll(bins[d.dbin.a][d.dbin.b].plist);
		
		
		for(int i=d.dbin.a-1 ; i<d.dbin.a+2;i++) {
			for(int j=d.dbin.b-1 ; j<d.dbin.b+2;j++) {
				if(i>=0 && i<31) {
					if(j>=0 && j<22) {
							
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
		newp.addAll(arrange(oldp.get(oldp.size()-1), newp, l));
		oldp.addAll(newp);
		return oldp;
		
	}
	
	public static ArrayList <BinNumber>  arrange(BinNumber b, ArrayList <BinNumber> p, BinNumber l) {
		Route_hop hop = new Route_hop();
		ArrayList <BinNumber> fin = new ArrayList <BinNumber>();
    	fin.addAll(hop.arrange(b, p, l));
		return fin;
	}
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	public static ArrayList <ArrayList> intialPopulation(ArrayList <BinNumber> p){
		ArrayList <ArrayList> newlist = new ArrayList <ArrayList>();
		for (int i = 0 ; i < 6 ; i++ ) {
			Collections.shuffle(p);
			if ( newlist.size() >= 1 ) {
				for(int j = 0 ; j < newlist.size() ; j++ ) {
					if(Arrays.equals(newlist.get(j).toArray(),p.toArray())) {
					newlist.add(p);
					}
			else
				newlist.add(p);
						
			}
		}
			
				
	}
		
		return newlist;
	
}
	
}
