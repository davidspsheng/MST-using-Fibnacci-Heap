
public class Array {
	public int n;	//	number of items in this array
	public MinHeapNode node[];	//	nodes stored in the array
	
	Array(int n, mst m){
		this.n = n;
		node = new MinHeapNode[n];
		for(int i = 0; i < this.n; i++){
			node[i] = new MinHeapNode(i, m.MaxVal);
		}
	}
	
	public MinHeapNode Extract_Min(mst m){	//	Extract min
		MinHeapNode p = null;
		
		int j = 0;
		for(j = 0; j < this.n; j++){
			if(m.vInMst[j]==0 && this.node[j].cost!=m.MaxVal){
				p = this.node[j];
				break;
			}
		}
		
		for(int i = j; i < this.n; i++){
			if(this.node[i].cost < p.cost && m.vInMst[i]==0){
				p = this.node[i];
			}
		}
		return p;
	}
	
	public void Decrease_Key(int i, int cost){	//	Decrease Key of node i to cost
		this.node[i].cost = cost;
	}
}
