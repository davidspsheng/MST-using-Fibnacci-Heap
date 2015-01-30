
public class HeapNode {
	public int vertex;	//	this vertex has edges with at least one vertex added to the MST
	public int cost;	// the minimum cost of any edges connecting this vertex to the vertices added to the MST
	public HeapNode lSibling;	// left sibling in the circular list
	public HeapNode rSibling;	//	right sibling in the circular list
	public int degree;	//	degree of this vertex
	public HeapNode child;	//	child pointer field
	public HeapNode parent;	//	parent pointer field
	public boolean childCut;	//	ChildCut flag
	
	HeapNode(int de, int cost, int v){
		this.degree = de;
		child = null;
		parent = null;
		lSibling = null;
		rSibling = null;
		childCut = false;
		this.cost = cost;
		this.vertex = v;
	}
}
