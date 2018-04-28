	public static int rangepickup(int x1,int x2,int y1,int y2,int x3,int x4,int y3,int y4)
	{
		    //x1,y1 origin x2,y2 destination of the route
		    //x3 y3 driver urrent position x4 y4 passeger current position
	    	int v1 = manhattan(x1,x2,y1,y2);
	    	int v = 1;//if car is forward
	    	int v2 = manhattan(x3,x4,y3,y4);
	    	if( v1 <  0)
	    	{
	    		if(v2 > = 0)
	    		{
	    		System.out.println("");
	    		v = -1;
	    		}//if car goes backward
	    	}
	    	else if(v1 > = 0)
	    	{
	    		if(v2 < 0)
	    		{
	    			v = -1;//if car goes backward
	    		}
	    	}
	    	return v;
	}
	
}
