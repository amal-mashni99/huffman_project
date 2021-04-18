package application;


public class Heap {
	   private static final int CAPACITY = 1;

	
	Node[] elt;
	int size;
	
	public Heap ( ) // Constructor: default size
	{
	setup(CAPACITY);
	}
	
	private void setup ( int maxNumber ) // Called by constructors only
	{
	size = 0;
	elt = new Node[maxNumber + 1];

	}

	public Heap ( int maxNumber ) // Constructor: specific size
	{
	setup(maxNumber);
	}
	
	public Node[] getElt() {
		Node[] ret = new Node[size + 1];
		for (int i = 0; i < size + 1; i++)
			ret[i] = elt[i];
		return ret;
	}
	
	public Node deleteMin() {
		int child, i;
		Node last, min = null;
		if (size != 0) {
			min = elt[1];
			last = elt[size--];
			for (i = 1; i * 2 <= size; i = child) {
				child = i * 2;
				if (child < size
						&& elt[child].getCount()> elt[child + 1].getCount())
					child++;
				if (last.getCount() > elt[child].getCount())
					elt[i] = elt[child];
				else
					break;
			}
			elt[i] = last;
		}
		return min;
	}
	
	public void addElt(Node element) {
		int i = ++size;
		while ((i > 1) && elt[i / 2].getCount() > element.getCount()) {
			elt[i] = elt[i / 2];
			i /= 2;
		}
		elt[i] = element;
	}
	
	   public void print() 
	    { 
	        for (int i = 1; i <= size / 2; i++) { 
	            System.out.println(" PARENT : " + elt[i] + " LEFT CHILD : " + 
	                      elt[2 * i] + " RIGHT CHILD :" + elt[2 * i + 1]); 
	            System.out.println(); 
	        } 
	    }
}
