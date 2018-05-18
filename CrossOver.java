package carpool_master1;

import java.io.IOException;
import java.util.ArrayList;

public class CrossOver {
	public static ArrayList<int[]> Cross(int[] father, int[] mother) {
		ArrayList<int[]> al = new ArrayList<int[]>();
		  int [] f = null;
		  int [] m = null;
		  if ( father.length < mother.length) {
			  f = father.clone();
		      m = mother.clone();
		  }
		  else {
			  m = mother.clone();
			  f = father.clone();
		  }
		  int [] child1 = new int [f.length/2 + m.length/2];
		  int [] child2 = new int [(f.length - f.length/2) + (m.length - m.length/2)];
		  int j = m.length - m.length/2;
		  for ( int i = 0 ; i < child1.length ; i++ ) {
			  
			  if( i < f.length / 2) 
				  child1[i] = f[i];  
			  else {
				  child1[i] = m[j];
			  	  j++;
			  }
		  }
		  j = f.length - f.length/2;
		  for ( int i = 0 ; i < child2.length ; i++) {
			  
			  if( i < (m.length - m.length / 2))
				  child2[i] = m[i];
			  else {
				  child2[i] = f[j];
				  j++;
			  }
				  
			  
		  }
		  
		  al.add(child1);
		  al.add(child2);
		  return al;
	}
  public static void main(String [] args)throws IOException{
	  int [] f = new int[] {1,4,6,9,10,16};
	  int [] m = new int[] {5,8,4,6,7,15,18,16,17,20};
	  ArrayList<int[]> al = new ArrayList<int[]>();
	  al = Cross(f,m);
	  for( int i = 0 ; i < al.size() ; i++ ) {
		  int [] a = al.get(i);
		  for( int j = 0 ; j < a.length ; j++ ) {
			  System.out.println(a[j] + " ");
		  }
		  System.out.println("\n");
	  }
  }
	  
  }

