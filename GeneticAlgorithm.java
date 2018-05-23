package carpool_master1;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {

	void population(ArrayList<Path> allpath) {
		ArrayList<Path> pathlist = new ArrayList<Path>();
		for (Path p : allpath) {
			if (pathcost(p.binno) > 260)
				pathlist.add(p);
		}
		for (int i = 0; i < pathlist.size(); i++) {

			System.out.println("path");
			for (int j = 0; j < pathlist.get(i).binno.size(); j++) {
				System.out.print(pathlist.get(i).binno.get(j).a + " " + pathlist.get(i).binno.get(j).b + "|");
			}
			System.out.println(pathcost(pathlist.get(i).binno));
		}
		System.out.println(pathlist.size());
		bestpath(pathlist);
		call(pathlist);
	}

	public static double pathcost(ArrayList<BinNumber> route) {
		double cost = 0;
		for (int i = 0; i < route.size() - 1; i++) {
			cost += cost(route.get(i), route.get(i + 1));
		}
		return cost;
	}

	public static int manhattan(BinNumber x, BinNumber y) {
		int manh = Math.abs(x.a - y.a) + Math.abs(x.b - y.b);
		return manh;

	}

	public static int cost(BinNumber x, BinNumber y) {
		int c = manhattan(x, y);
		if (c == 0)
			return 5;
		else
			return c * 10;
	}

	public static double normcost(ArrayList<BinNumber> route) {
		double cost = pathcost(route);
		double newcost = (cost - 100) / (500 - 100);
		int p = route.size() / 2;
		double normp = (p - 0) / (4 - 0);
		return (newcost * 0.5) - (normp * 0.5);
	}

	public static Path bestpath(ArrayList<Path> allpath) {
		Path bestp = new Path();
		bestp = allpath.get(0);
		for (Path p : allpath) {
			if (normcost(p.binno) < normcost(bestp.binno))
				bestp = p;
		}
		for (int j = 0; j < bestp.binno.size(); j++) {
			System.out.print(bestp.binno.get(j).a + " " + bestp.binno.get(j).b + "|");
		}
		System.out.println(normcost(bestp.binno));
		return bestp;
	}

	public static Path Cross(ArrayList<BinNumber> father, ArrayList<BinNumber> mother) {
		Path al = new Path();
		ArrayList<BinNumber> f = (ArrayList<BinNumber>) father.clone();
		ArrayList<BinNumber> m = (ArrayList<BinNumber>) mother.clone();
		ArrayList<BinNumber> child1 = new ArrayList<BinNumber>();
		ArrayList<BinNumber> child2 = new ArrayList<BinNumber>();
		ArrayList<BinNumber> child3 = new ArrayList<BinNumber>();

		for (int i = 0; i < f.size(); i++) {
			if (i < f.size() / 2)
				child1.add(f.get(i));
			else
				child3.add(f.get(i));
		}
		for (int i = 0; i < f.size(); i++) {
			if (i < m.size() / 2)
				child2.add(m.get(i));
			else
				child1.add(f.get(i));
		}
		child2.addAll(child3);

		if (normcost(child1) > normcost(child2))
			al.binno.addAll(child2);
		else
			al.binno.addAll(child1);
		return al;
	}

	public static ArrayList<BinNumber> mutation(ArrayList<BinNumber> element) {
		Random rand = new Random();
		if (rand.nextInt(100) == 50) {
			int size = element.size();
			int position = rand.nextInt(size);
			BinNumber bin = element.get(position);
			bin.a++;
			bin.b++;
			element.set(position, bin);
		}
		return element;
	}

	public static void call(ArrayList<Path> p) {
		Random rand = new Random();
		ArrayList<Path> crossPath = new ArrayList<Path>();
		crossPath.add(p.get(rand.nextInt(p.size())));
		for (int i = 1; i <= 10; i++) {
			System.out.println("grneration" + i);
			for (int j = 1; j < 10; j++) {
				System.out.println(rand.nextInt(p.size()));
				crossPath.add(Cross(p.get(rand.nextInt(p.size())).binno, p.get(rand.nextInt(p.size())).binno));

			}

			for (Path m : crossPath) {
				for (BinNumber b : m.binno) {
					System.out.print("|" + b.a + " " + b.b);
				}
				System.out.println();
			}
			Path bestp = new Path();
			// bestp.binno = mutation(bestp.binno);
			bestp = bestpath(crossPath);
			crossPath.clear();
			crossPath.add(bestp);

		}

	}

}

