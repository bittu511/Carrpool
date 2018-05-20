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
			routes(d, path, cursor++, serviced, pickup);
			if (d.plist.size() < 3 && cursor < 10) {
				ArrayList<Passenger> neighbourlist = new ArrayList<Passenger>();
				neighbourlist.addAll(neighbours(path.get(cursor)));
				neighbourlist.removeAll(serviced);
				for (int i = 0; i < 3; i++) {
					if (!neighbourlist.isEmpty()) {
						Passenger bestp = bestPassenger(neighbourlist, d, path.get(cursor));
						neighbourlist.remove(bestp);
						ArrayList<BinNumber> pathclone = (ArrayList<BinNumber>) path.clone();
						ArrayList<Passenger> servicedclone = (ArrayList<Passenger>) serviced.clone();
						pathclone.add(path.size() - 1, bestp.ori);
						pathclone = optimize(cursor, pathclone, d);
						Driver dclone = new Driver(d);
						ArrayList<Passenger> pickupcl = (ArrayList<Passenger>) pickup.clone();
						pickupcl.add(bestp);
						routes(dclone, pathclone, cursor, servicedclone, pickupcl);
					}
				}
			}

		}

	}
