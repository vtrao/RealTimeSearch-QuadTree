import java.util.*;
import java.io.*;
import java.awt.*;


class BTree extends TreeSet
 {
 	BTree(Comparator comp)
 	{
 		super(comp);
 	}
 	object search(object obj_to_search)
 	{
 		if(this.contains(obj_to_search))
 		{
 	    	QuadTree q1=obj_to_search.objptr_to_quad;
 		    for(int i=0;i<q1.obj_count;i++)
		    {
			    object temp=(object)q1.obj.elementAt(i);
			    if(obj_to_search.identifier==temp.identifier)
			    {
			        return temp;
			    }
		    }
		    return null;
		}
		else
		{
			return null;
		}
	}		
 	
}
class rectangle
{
	 int xmin,xmax,ymin,ymax;
	 rectangle(int xmin,int xmax,int ymin,int ymax)
	 {
	 	this.xmin=xmin;
	 	this.xmax=xmax;
	 	this.ymin=ymin;
	 	this.ymax=ymax;
	 }
	 
	 
	boolean ispoint_in_rect(int xval,int yval)
	{
		if(xval<=xmax && xval>xmin)
		{
			if(yval<=ymax && yval>ymin)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			return false;
		}
		
	} 
	boolean isoverlapping(rectangle rect)
	{
	    if(this.ispoint_in_rect (rect.xmin,rect.ymin))
			return true;
		if(this.ispoint_in_rect (rect.xmax,rect.ymin))
			return true;
		if(this.ispoint_in_rect (rect.xmin, rect.ymax))
			return true;
		if(this.ispoint_in_rect (rect.xmax, rect.ymax))
			return true;
		
		if (rect.ispoint_in_rect (this.xmin, this.ymin))
			return true;
		if (rect.ispoint_in_rect (this.xmax, this.ymin))
			return true;
		if (rect.ispoint_in_rect (this.xmin , this.ymax))
			return true;
		if (rect.ispoint_in_rect (this.xmax, this.ymax))
			return true;
			
		return false;
	}
	boolean isembedded(rectangle rect)
	{
		if (rect.xmin<xmin && this.ymin<ymin)
		{
			if (rect.xmax<xmax && this.ymax<ymax)
				return true;
		}
		return false;
	}	 
}
class object
{ // class to store the details of person 
	int xval,yval;// cordinates of object
    long identifier;
    QuadTree objptr_to_quad;
    int type;
    object(int x,int y, long id)
    {
    	xval = x;
    	yval = y;
    	identifier = id;
    }
    
     boolean update(object new_obj) throws Exception
    {
    	this.xval = new_obj.xval;
    	this.yval = new_obj.yval;
    	this.objptr_to_quad.update(this);
    	return true;
    }
}


class MyComparator implements Comparator
{

	public int compare (Object obj1, Object obj2)
	{
		object new_obj1 = (object)obj1;
		object new_obj2 = (object)obj2;
		if (new_obj1.identifier < new_obj2.identifier)
			return -1;
		if (new_obj1.identifier > new_obj2.identifier)
			return 1;
		
		new_obj1.objptr_to_quad=new_obj2.objptr_to_quad;		
		return 0;
	}
}


class QuadTree
{
	QuadTree parent;
	rectangle rect; // for storing region(rectangular type) 
	static int MAX_OBJ=10;     //Try out for different values 
	int obj_count;
	Vector obj=new Vector(MAX_OBJ); 
	QuadTree child[];
	boolean isleaf;
	
	BTree btree;
	
	static MyFrame f=new MyFrame ("Hello");
	
	
 QuadTree(rectangle r,QuadTree parentptr)
 	{
 	
 		if (parentptr==null)
 		{	
 			f.setSize (500,500);
 			f.setVisible (true);
 			f.setTitle ("Hello");
 			f.msg = "hello nipun";
 			//f.repaint();
 		}
 		parent=parentptr;
    	isleaf=true;
    	child=new QuadTree[4];
    	rect=r;
    	obj_count=0;
    	btree = new BTree (new MyComparator());         	 	 
	}


 //Performs a bottom up search for objects location in a region around a particular object	
 boolean  search_object_insquare(rectangle region,int type,QuadTree kid ,boolean parent,Vector result ) //given an identifier it will return set of objects around that object
 {                                                                                   // if boolean parent is false do not call parent.search and child indicates the child object which called the search 
 	 if(isleaf==true)
 	 { 
 	     for(int i=0;i<obj_count;i++)
 	     {
 	         if(region.ispoint_in_rect(((object)obj.elementAt(i)).xval,((object)obj.elementAt(i)).yval))
 	         {
 	         	result.add(obj.elementAt(i));
 	         	
 	         }	
 	         
 	     }
 	   /*  if(parent==true)
 	     {
 	     	this.parent.search_object_insquare(region,type,this,true,result);
 	     }
 	     return true;	    
 	     */
 	 }
 	 else
 	 {
 	 	for(int i=0;i<4;i++)
 	 	{
 	 		if(child[i]!=null)
 	 		{
 	 			if(child[i]!=kid)
 	 			{
 	 				if(region.isoverlapping(child[i].rect))
 	 				{
 	 					child[i].search_object_insquare(region,type,this,false,result);
 	 				}	
 	 			}	
 	 		}	
 	 	}
 	/* 	 if(parent==true)
 	     {
 	     	this.parent.search_object_insquare(region,type,this,true,result);
 	     }	
 	 */	
 	 }	
 	 
   // call	 isembedded();
     if(!region.isembedded(rect))
     {
     	if(parent==true && this.parent!=null)
     	{
   		    this.parent.search_object_insquare(region,type,this,true,result);	     	
     	}
     }	
 	 
 	return true; 	   	  
 }

 boolean insert_update (object new_obj) throws Exception
 {	
 	object btree_obj;
 	
 	if ((btree_obj=btree.search (new_obj))!=null)
 	{
 		//object btree_obj=btree.search(new_obj); //to find the object in btree  	
 		btree_obj.xval=new_obj.xval;
 		btree_obj.yval=new_obj.yval;
 		btree_obj.objptr_to_quad.update(btree_obj);
 	}
 	else
 	{
 		btree.add(new_obj);
 		insert(new_obj);
 	}	
 	return true;
 }



 boolean insert(object new_obj) throws Exception
 {
    
    if(!isleaf)  // traversing to leaf node
    { 
    	obj_count++;
 	  	child[quadrant(new_obj)].insert(new_obj);
 	}
 	else  // splitting and inserting
 	{  	     
 	   if(obj_count<MAX_OBJ)
 	   {
 	   	   new_obj.objptr_to_quad = this;
 		   obj.addElement(new_obj);
 		   obj_count++;
     	}
 	    else
      	{
 	    	split();
 	    	obj_count++;
 		    child[quadrant(new_obj)].insert(new_obj);
 		    try
 		    {
            for(int i=0;i<MAX_OBJ;i++)
            {
                 child[quadrant((object)obj.elementAt(i))].insert((object)obj.elementAt(i));
            }
            obj.removeAllElements();
        	}catch (Exception e)
        	{
        	System.out.println("error" + e);
        	
        	
        	}	
            //obj=null; //remove all elements from non leaf node	 			
 	    }	
     }
     return true;
  }
 		
 void split()
{
   
 	isleaf=false;
 	int splitx=(rect.xmin+rect.xmax)/2;
	int splity=(rect.ymin+rect.ymax)/2;
//	System.out.println(rect.xmin);
//	System.out.println(rect.xmax);
//	System.out.println("splitx=" + splitx);
	child[0]=new QuadTree(new rectangle(rect.xmin,splitx,rect.ymin,splity),this);
	child[1]=new QuadTree(new rectangle(rect.xmin,splitx,splity,rect.ymax),this);
	child[2]=new QuadTree(new rectangle(splitx,rect.xmax,splity,rect.ymax),this);
	child[3]=new QuadTree(new rectangle(splitx,rect.xmax,rect.ymin,splity),this);
}


// function to find the quadrant in which object is present
 int quadrant(object obj){
 	int splitx=(rect.xmin+rect.xmax)/2;
	int splity=(rect.ymin+rect.ymax)/2;
 	
 	if(obj.xval<=splitx)
 	{
 		if(obj.yval<=splity)
 		{
 			return 0;
 		}
 		else
 		{
 			return 1;
 		}
 	}	
 	else
 	{
 		if(obj.yval<=splity)
 		{
 			return 3;
 		}
 		else
 		{
 			return 2;
 		}
 	}
} 	

	static int level;
 	void traverse()
 	{
 		
 		if(!isleaf)
 		{
 			level++;
 			for(int i=0;i<4;i++)
	 		{
				//System.out.println(child[i].obj_count + " " + child[i].rect.xmin + " " +child[i].rect.xmax+ " " +child[i].rect.ymin + " " +child[i].rect.ymax); 
				f.addRect (child[i].rect.xmin,child[i].rect.ymin,child[i].rect.xmax-child[i].rect.xmin,child[i].rect.ymax-child[i].rect.ymin);
				child[i].traverse(); 			
	 		}
	 	}
	 	else
	 	{
	 		//System.out.println ("Rectangles: "+rect.xmin+" "+rect.xmax+" "+rect.ymin+" "+rect.ymax);
	 		for(int i=0;i<obj_count;i++)
	 		{
	 			object temp=(object)obj.elementAt(i);
	 			f.addPoint (temp.xval,temp.yval);
	 			//System.out.println("xval="+temp.xval+"yval="+temp.yval+"identifier"+temp.identifier);
	 		}
	 		//System.out.println (level);
	 		level=0;
	 	}
	 		
 	}
 	
 	
 	boolean update(object new_obj) throws Exception 
	{
	/*	for(int i=0;i<obj_count;i++)
		{
			object temp=(object)obj.elementAt(i);
			if(new_obj.identifier==temp.identifier)
			{
				temp.xval=new_obj.xval;
				temp.yval=new_obj.yval;
				new_obj=temp;
			}
		}*/
			
	    if(!rect.ispoint_in_rect(new_obj.xval,new_obj.yval))
	    {
	    	obj.removeElement(new_obj);
	    	obj_count--;
	    	QuadTree temp=this.parent;
	    	temp.obj_count--;
	    	
	    	//System.out.println (new_obj.xval + " " + new_obj.yval);
	    	try
	    	{  // decrement the count in parent also
	    		while(!temp.rect.ispoint_in_rect(new_obj.xval,new_obj.yval))
	    		{
	    			
	    			if(temp.obj_count<=MAX_OBJ)
	    			{
	    			     temp.merge();
	    			    
	    			}
	    		//System.out.println ("Temp Parent"+temp.rect.xmin+ " " +temp.rect.xmax + " " + temp.rect.ymin + " "+ temp.rect.ymax + " " +temp.obj_count);	
	    		temp=temp.parent;
	    		//System.out.println ("Temp"+temp.rect.xmin+ " " +temp.rect.xmax + " " + temp.rect.ymin + " "+ temp.rect.ymax + " " +temp.obj_count);
	    		temp.obj_count--;
	    		//System.out.println ("Temp Out "+new_obj.xval + " " + new_obj.yval);
	    		}
	    	temp.insert(new_obj);
	    	}catch(NullPointerException e)
	    	{
	    		System.out.println("Exception "+e.getCause()); e.printStackTrace();
	    	}
	    }	
	    return true;
	    	
	}
	
	void merge() throws Exception
	{
		int i=0,j=0;
		this.isleaf=true;
		
		try
		{
			
		for(i=0;i<4;i++)
		{
 			//try{
 			for(j=0;j<child[i].obj_count;j++)
 			{
 				//System.out.println("Merge :"+ i + " " + j + " " + child[i].obj_count);
 				try
 				{
 				object temp_obj = (object)child[i].obj.elementAt(j);
 				temp_obj.objptr_to_quad = this;
 				this.obj.addElement(temp_obj);
 			    }catch(Exception e){System.out.println("inside merge");}
 			}
 			//}catch (Exception e ){System.out.println ("i="+i+"j="+j);}
 			child[i].obj.removeAllElements();
 			child[i] = null;
		}
		}catch (NullPointerException e ){System.out.println ("Merge :"+e.getCause());
		                                  e.printStackTrace();} 
	}
	
	
	public Vector query_around_object (object obj1, int radius, int type)
	{
		if (!btree.contains(obj1))
			return null;
		object obj=btree.search (obj1);
		rectangle rect = new rectangle (obj.xval-radius, obj.xval+radius, obj.yval-radius, obj.yval+radius);
 		Vector result = new Vector();
 		
// 		System.out.println ("Calling Search");
 		System.out.println ("Rectangle "+rect.xmin+ " "+ rect.ymin + " "+rect.xmax + " "+rect.ymax);
 		obj.objptr_to_quad.search_object_insquare (rect,type,null,true,result);
		return result;
	}		

 	
 	public static void main(String[] args)
 	{
 		
 		QuadTree root= new QuadTree(new rectangle(0,800,0,700), null);
 		object objArray[] = new object[100000];
 		TreeSet tree = new TreeSet  (new MyComparator());
 		try 
 		{
 			
 		Random r = new Random();
 		Random r1 = new Random();
 		
 		Date date=new Date();
 		long start=date.getTime();
 
 		
 		//Used to Insert inital number of objects into the system.
 		for(int i=0;i<100000;i++)
 		{
 			objArray[i] = new object(r.nextInt(800),r1.nextInt (700),i);
 		//	tree.add (objArray[i]);
 			root.insert_update(objArray[i]);
 		//	System.out.println(objArray[i].xval +" "+ objArray[i].yval);
 		}

 		Date date1=new Date();
 		long stop=date1.getTime();
 		System.out.println("1 lakh inserts completed");
 		long time = stop-start;
 		System.out.println ("Start Time :"+start);
 		System.out.println ("Stop Time :"+stop);
 		System.out.println("Time taken to Insert :"+time);


 		
 		//System.out.println("palani and gokul are traversing");
 		//root.traverse();
 		//root.f.repaint();
 	    //System.out.println("they stopped");
 	    
 	    //Used to do the updates to the objects in the quadtree
 	    //Change the number of updates and check the time taken
 	    for(int i=0;i<30;i++)
 		{
 			objArray[i] = new object(r.nextInt(500),r1.nextInt (500),i);
 		//	tree.add (objArray[i]);
 			root.insert_update(objArray[i]);
 			//System.out.println(objArray[i].xval +" "+ objArray[i].yval);
 		}
 		
 		//System.out.println ("Traverse OVER");

 		
 		System.out.println ("Search for Objects around an Object");
 		
 		date = new Date();
 		start = date.getTime();
 		
 		object obj1 = new object (1,10,1);
 		root.btree.contains (obj1);
 		object obj = root.btree.search (obj1);
 		
 		//System.out.println ("\n\n\nCreating Rect");
 		
 		//rectangle rect = new rectangle (obj.xval-20, obj.xval+20, obj.yval-20, obj.yval+20);
 		System.out.println ("Search around :1 in rectangle :"+ (obj.xval-2)+" "+(obj.yval-2)+" "+(obj.xval+2)+" "+(obj.yval+2));
 		Vector result = new Vector();
 		
 		System.out.println ("Calling Search");
 		
 		//obj.objptr_to_quad.search_object_insquare (rect,101,null,true,result);
 		result = root.query_around_object (obj1,2,101);
 		//System.out.println ();
 		
 		//System.out.println ("Rectangle "+rect.xmin+ " "+ rect.ymin + " "+rect.xmax + " "+rect.ymax);
 		
 		date1 = new Date();
 		stop = date1.getTime();
 		System.out.println ("Query Execution Complete");
 		System.out.println ("Results of Query\n");
 		time=stop-start;

 		
 		for (int i=0;i<result.size();i++)
 		{
 			object o = (object)result.elementAt (i);
 			System.out.println (o.identifier +" "+ o.xval + " "+ o.yval);
 		}
 		System.out.println ("Time taken for Query :"+time);
 		/*BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
 		String str = "nipin";
 		
 		try{
 		while (!str.equals ("stop")){
 			str=br.readLine();
 			String s[] = str.split (" ");
 			int i1 = Integer.parseInt(s[0]);
 			int i2 = Integer.parseInt (s[1]);
 			int i3 = Integer.parseInt (s[2]);
 			
 			object ob = new object (i1,i2,i3);
 			
 			root.insert_update (ob); 
 			root.traverse();
 		}
 		}catch (Exception e){}
 		*/
 		//root.traverse();
 		
 		
 				
 
 	    
 	    }catch(Exception e)
 	    {
 	    	e.printStackTrace();
 	    }
 	    	
 		//root.traverse();
/* 		Iterator it = tree.iterator();
 		
 		
 		while (it.hasNext())
 		{
 			object obj = (object) it.next();
 			System.out.println (obj.identifier+" "+obj.xval+" "+obj.yval);
 		}
 		
 		
 		object o = new object (50,50,100);
 		if (tree.contains (o))
 		{
 			System.out.println (o.identifier +" "+o.xval+" "+o.yval);
 		}
 	*/
 	}
 
 
}



class MyFrame extends Frame
{
	public String msg;
	Vector rect = new Vector ();
	Vector point = new Vector ();	
	MyFrame (String name)
	{
		super (name);
		setBackground (Color.cyan);
		setForeground (Color.red);
		repaint();
	}
	
	
	void addRect (int x,int y,int width,int height)
	{
		rect.add (x);
		rect.add (y);
		rect.add (width);
		rect.add (height);
	}
	
	void addPoint (int x,int y)
	{
		point.add (x);
		point.add(y);
	}
	
	public void paint (Graphics g)
	{
		for (int i=0;i<rect.size();i+=4)
			g.drawRect (((Integer)rect.elementAt (i)).intValue(),((Integer)rect.elementAt (i+1)).intValue()+20, ((Integer)rect.elementAt (i+2)).intValue(),((Integer)rect.elementAt (i+3)).intValue());
			
		for (int i=0;i<point.size();i+=2)
			g.fillOval (((Integer)point.elementAt (i)).intValue(),((Integer)point.elementAt (i+1)).intValue()+20,2,2);
		
	
		//rect=null;
		//point=null;
		
		//rect=new Vector();
		//point=new Vector();
	
	}
	
}