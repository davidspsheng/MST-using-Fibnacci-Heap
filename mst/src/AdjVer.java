
public class AdjVer {
	public int vertex;	// in this adjacency list, if this is a head, vertex means this list belongs to this vertex, otherwise vertex means this vertex has an edge with the node this adjacency list belongs to
	public int cost;	// cost of edge, other than in head node
	public AdjVer next;
	
	AdjVer(int ver, int c){
		this.vertex = ver;
		this.cost = c;
		this.next = null;
	}
}
