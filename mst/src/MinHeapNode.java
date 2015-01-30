
public class MinHeapNode {
	public int vertex;	//	this vertex has edges with at least one vertex added to the MST
	public int cost;	// the minimum cost of any edges connecting this vertex to the vertices added to the MST
	
	MinHeapNode(int v, int c){
		this.vertex = v;
		this.cost = c;
	}
}
