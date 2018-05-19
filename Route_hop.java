package carpool;

import java.util.ArrayList;
import java.util.Arrays;

public class Route_hop
{

    public static int length(BinNumber b, ArrayList <BinNumber> p, BinNumber l) {
    	ArrayList <BinNumber> n = new ArrayList <BinNumber>();
    	n.add(b);
    	n.addAll(p);
    	n.add(l);
    	int len = 0;
    	for (int i = 0; i<n.size() - 1; i++) {
    		len = len + manhattan(n.get(i),n.get(i));
    	}
    	return len;
    	
    }
    public static int manhattan (BinNumber p1, BinNumber p2) {
		int manh =  Math.abs(p1.a - p2.a) + Math.abs(p1.b - p2.b);
		
		return manh;
		
	}
    ArrayList <BinNumber> arrange(BinNumber b, ArrayList <BinNumber> p, BinNumber l) {
    	BinNumber [] pnew = p.toArray(new BinNumber[p.size()]);
    	for(int i = 0; i < pnew.length - 1; i++) {
    		if(manhattan(b, pnew[0]) > manhattan(b, pnew[i]) ) {
    			BinNumber temp = pnew[i];
    			pnew[i] = pnew[0];
    			pnew[0] = temp;
      		}
//    		if(pnew[i] == l) {
//    			BinNumber temp = pnew[i];
//    			pnew[i] = pnew[pnew.length - 1];
//    			pnew[pnew.length - 1] = temp;
//      		}
  	    }
    	ArrayList <BinNumber> fin = new ArrayList <BinNumber>(Arrays.asList(pnew));
    	return fin;
    	
    	
    }
        
    
 
//    public static void main(String args[]) 
//    {
//        int[] sequence = new int[3];
// 
//        for (int i = 0; i < 3; i++)
//            sequence[i] = i;
// 
//        
// 
//        System.out.println("\nThe permuted sequences are: ");
//        permute(sequence, 0);
//        int n=0;
//        for(int l =0; l<arr.size(); l++) {
//    		n = n*10 + arr.get(l);
//    		System.out.print(arr.get(l));
//    	}
//    
    	
}
