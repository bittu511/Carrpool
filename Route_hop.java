package carpool;

import java.util.ArrayList;

public class Route_hop
{
	public static ArrayList <BinNumber> arr = new ArrayList <BinNumber>();
    static void permute(BinNumber[] a, int k, BinNumber b, BinNumber l) 
    {
        if (k == a.length) 
        {
        	ArrayList <BinNumber> p = new ArrayList <BinNumber>();
        	for (int i = 0; i < a.length; i++) 
            {
                p.add(a[i]);
            }
        	        	
        	if(length(b, p, l)>=length(b, arr, l)) {
        		arr.clear();
        		arr.addAll(p);
        	}
            System.out.println();
        } 
        else 
        {
            for (int i = k; i < a.length; i++) 
            {
            	BinNumber temp = a[k];
                a[k] = a[i];
                a[i] = temp;
 
                permute(a, k + 1, b, l);
 
                temp = a[k];
                a[k] = a[i];
                a[i] = temp;
            }
        }
    }
    
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
    	permute(pnew, 0, b, l);
    	ArrayList <BinNumber> fin = new ArrayList <BinNumber>();
    	fin.addAll(arr);
    	fin.remove(0);
    	arr.clear();
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
//    }
}