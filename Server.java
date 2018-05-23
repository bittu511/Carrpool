package carpool_master1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Server {
	public static Bin bins[][] = new Bin[31][22];
	static Route_hop rh = new Route_hop();
	public static ArrayList<Path> allPaths = new ArrayList<Path>();

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 31; i++) {
			for (int j = 0; j < 22; j++) {
				bins[i][j] = new Bin(i, j);
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
		d.p1 = P;
		d.dbin.a = P.ori.a;
		d.dbin.b = P.ori.b;

		ArrayList<BinNumber> binlist = new ArrayList<BinNumber>();
		binlist.add(d.p1.ori);
		binlist.add(d.p1.dest);

		ArrayList<Passenger> servicedPassengerlist = new ArrayList<Passenger>();
		ArrayList<Passenger> pickupPassengerlist = new ArrayList<Passenger>();

		routes(d, binlist, 0, servicedPassengerlist, pickupPassengerlist);
		for (int i = 0; i < allPaths.size(); i++) {

			System.out.println("path");
			for (int j = 0; j < allPaths.get(i).binno.size(); j++) {
				System.out.print(allPaths.get(i).binno.get(j).a + " " + allPaths.get(i).binno.get(j).b + "|");
			}

		}
		System.out.println(allPaths.size());
		GeneticAlgorithm g = new GeneticAlgorithm();
		g.population(allPaths);
	}

	static int arr_oriX[] = { 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 5, 6, 6, 6, 7, 7, 6 };
	static int arr_oriY[] = { 0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 5, 5, 5, 5, 5, 4, 6, 4, 6, 3, 6 };
	static int arr_destX[] = { 4, 4, 6, 6, 6, 5, 3, 2, 6, 5, 3, 5, 7, 7, 7, 7, 7, 3, 3, 2, 3 };
	static int arr_destY[] = { 2, 1, 1, 3, 3, 4, 4, 4, 5, 4, 6, 6, 5, 5, 2, 1, 4, 3, 3, 5, 2 };
	static String arr_name[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u" };

	static void passengerGeneration() throws IOException {
		Passenger[] plist = new Passenger[21];// Should be more than 100
		for (int i = 0; i < plist.length; i++) {
			plist[i] = new Passenger();
			plist[i].name = arr_name[i];
			plist[i].ori.a = arr_oriX[i];
			plist[i].ori.b = arr_oriY[i];
			plist[i].dest.a = arr_destX[i];
			plist[i].dest.b = arr_destY[i];
			bins[plist[i].ori.a][plist[i].ori.b].plist.add(plist[i]);
			bins[plist[i].ori.a][plist[i].ori.b].binDensity++;
		}

	}
	

	public static ArrayList<Passenger> neighbours(BinNumber bin) {
		ArrayList<Passenger> plist = new ArrayList<Passenger>();
		// receives a driver and generates its 9 neighbours
		// finds the max bins
		int max = bins[bin.a][bin.b].binDensity;
		for (int i = bin.a - 1; i < bin.a + 2; i++) {
			for (int j = bin.b - 1; j < bin.b + 2; j++) {
				if (i >= 0 && i < 31) {
					if (j >= 0 && j < 22) {
						if (i != bin.a || j != bin.b) {
							// System.out.print("bin" + i + " " + j);
							if (max < bins[i][j].binDensity) {
								max = bins[i][j].binDensity;
							}
						}
					}
				}
			}

		}
		// takes all the passengers from max bins and puts them in a arraylist of
		// passengers
		plist.addAll(bins[bin.a][bin.b].plist);

		for (int i = bin.a - 1; i < bin.a + 2; i++) {
			for (int j = bin.b - 1; j < bin.b + 2; j++) {
				if (i >= 0 && i < 31) {
					if (j >= 0 && j < 22) {

						if (max == bins[i][j].binDensity) {
							plist.addAll(bins[i][j].plist);
						}

					}
				}
			}

		}

		// returns the arraylist
		return plist;
	}

	

	

	public static ArrayList<BinNumber> optimize(int b, ArrayList<BinNumber> oldpath, Driver d) {
		ArrayList<BinNumber> newp = new ArrayList<BinNumber>();
		ArrayList<BinNumber> oldp = new ArrayList<BinNumber>();
		int i = 0;
		while (i <= b) {
			oldp.add(oldpath.get(i));
			i++;
		}

		while (i <= oldpath.size() - 1) {
			newp.add(oldpath.get(i));
			i++;
		}
		Route_hop hop = new Route_hop();
		ArrayList<BinNumber> fin = new ArrayList<BinNumber>();
		fin.addAll(hop.arrange(oldpath.get(b), newp));

		oldp.addAll(fin);
		return oldp;

	}

	public static void routes(Driver d, ArrayList<BinNumber> path, int cursor, ArrayList<Passenger> serviced,
			ArrayList<Passenger> pickup) {
		
		if (path.get(cursor).equals(d.p1.dest)) {
			Path p = new Path();
			p.binno.addAll(path);
			allPaths.add(p);
			return;
		} else {
			ArrayList<Passenger> pclone = (ArrayList<Passenger>) d.plist.clone();
			ArrayList<Passenger> pickupclone = (ArrayList<Passenger>) pickup.clone();
			for (Passenger p : pclone) {
				if (path.get(cursor).equals(p.dest)) {
					d.plist.remove(p);
				}
			}

			for (Passenger p : pickupclone) {
				if (path.get(cursor).equals(p.ori)) {
					d.plist.add(p);
					serviced.add(p);
					path.add(path.size() - 1, p.dest);
					path = optimize(cursor, path, d);
					pickup.remove(p);
				}
			}
			routes(d, path, cursor + 1, serviced, pickup);
			if (d.plist.size() < 3 && path.size() < 10) {
				ArrayList<Passenger> neighbourlist = new ArrayList<Passenger>();
				neighbourlist.addAll(neighbours(path.get(cursor)));
				neighbourlist.removeAll(serviced);
				for (int i = 0; i < 3; i++) {
					if (!neighbourlist.isEmpty()) {
						Passenger bestp = rh.bestPassenger(neighbourlist, d, path.get(cursor));
						neighbourlist.remove(bestp);
						ArrayList<BinNumber> pathclone = (ArrayList<BinNumber>) path.clone();
						ArrayList<Passenger> servicedclone = (ArrayList<Passenger>) serviced.clone();
						pathclone.add(path.size() - 1, bestp.ori);
						pathclone = optimize(cursor, pathclone, d);
						Driver dclone = new Driver(d);
						@SuppressWarnings("unchecked")
						ArrayList<Passenger> pickupcl = (ArrayList<Passenger>) pickup.clone();
						pickupcl.add(bestp);
						routes(dclone, pathclone, cursor, servicedclone, pickupcl);
					}
				}
			}

		}

	}
}
