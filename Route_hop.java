package carpool_master1;

import java.util.ArrayList;
import java.util.Arrays;

public class Route_hop {

	public static int length(BinNumber b, ArrayList<BinNumber> p, BinNumber l) {
		ArrayList<BinNumber> n = new ArrayList<BinNumber>();
		n.add(b);
		n.addAll(p);
		n.add(l);
		int len = 0;
		for (int i = 0; i < n.size() - 1; i++) {
			len = len + manhattan(n.get(i), n.get(i));
		}
		return len;

	}

	public static int manhattan(BinNumber p1, BinNumber p2) {
		int manh = Math.abs(p1.a - p2.a) + Math.abs(p1.b - p2.b);

		return manh;

	}

	ArrayList<BinNumber> arrange(BinNumber b, ArrayList<BinNumber> p) {
		BinNumber[] pnew = p.toArray(new BinNumber[p.size()]);
		for (int i = 0; i < pnew.length - 1; i++) {
			if (manhattan(b, pnew[0]) > manhattan(b, pnew[i])) {
				BinNumber temp = pnew[i];
				pnew[i] = pnew[0];
				pnew[0] = temp;
			}
		}
		ArrayList<BinNumber> fin = new ArrayList<BinNumber>(Arrays.asList(pnew));
		return fin;

	}
	
	public static int distortion(BinNumber bin, Passenger p, Driver d) {
		int manhattanD = manhattan(d.p1.dest.a, p.dest.a, d.p1.dest.b, p.dest.b);
		int manhattanO = manhattan(bin.a, p.ori.a, bin.b, p.ori.b);

		return manhattanD + manhattanO;

	}
	public static int manhattan(int ox, int dx, int oy, int dy) {
		int manh = Math.abs(ox - dx) + Math.abs(oy - dy);

		return manh;

	}

	public static double manhattan(double x, double y) {
		double manh = 0;
		manh = x + y;
		return manh;

	}
	public static Passenger bestPassenger(ArrayList<Passenger> plist, Driver d, BinNumber bin) {
		Passenger p = new Passenger();
		// selects the best passenger for driver d from plist
		ArrayList<Passenger> list = new ArrayList<Passenger>();
		list.addAll(plist);
		double dis = distortion(bin, list.get(0), d);// cost for distortion
		double backtrack = BacktrackCost(d, list.get(0), bin);// cost for backtrack
		double lcost = dis + backtrack;
		p = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			dis = distortion(bin, list.get(i), d);
			backtrack = BacktrackCost(d, list.get(0), bin);
			double cost = dis + backtrack;
			if (lcost >= cost) {
				lcost = cost;
				p = list.get(i);// return best passenger
			}
		}
		return p;
	}
	public static double BacktrackCost(Driver d, Passenger p, BinNumber bin) {
		// cost calculation if the car backtracks
		double globalx = d.p1.dest.a - d.p1.ori.a;
		double globaly = d.p1.dest.b - d.p1.ori.b;
		double localx = p.ori.a - bin.a;
		double localy = p.ori.b - bin.b;
		double currentx = d.p1.dest.a - bin.a;
		double currenty = d.p1.dest.b - bin.b;
		double valuex, valuey;
		if (globalx * localx <= 0) {
			valuex = Math.abs(localx); // if backtrack occurs along in X direction
		} else
			valuex = 0; // if no backtrack occurs along in X direction
		if (globaly * localy <= 0) {
			valuey = Math.abs(localy);// if backtrack occurs along in Y direction
		} else
			valuey = 0;// if no backtrack occurs along in Y direction
		double backtrack = valuex + valuey;
		double r1 = (1 + 0.25);
		double r2 = 1 + (0.25 * manhattan(currentx, currenty) < manhattan(globalx, globaly)
				? (1 - manhattan(currentx, currenty) / manhattan(globalx, globaly))
				: 0);
		return Math.pow(backtrack, (r1 * r2));// cost will increase exponent wise
	}
}
