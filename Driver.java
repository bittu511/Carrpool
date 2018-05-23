package carpool_master1;

import java.util.ArrayList;

public class Driver {
	Passenger p1;
	ArrayList<Passenger> plist = new ArrayList<Passenger>();
	int pcount = 0;

	BinNumber dbin = new BinNumber();

	// driver current bin
	Driver() {

	}

	Driver(Driver d) {
		p1 = d.p1;
		plist = (ArrayList<Passenger>) d.plist.clone();
		pcount = d.pcount;
	}
}
