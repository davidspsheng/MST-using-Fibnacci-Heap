
public class MinHeap {
	public int heapSize;
	public MinHeapNode node[];
	public int vAdd[];	// vAdd[i] = j; means node i is in the position of index j in the MinHeap
	
	MinHeap(int n, mst m){
		heapSize = n - 1;
		node = new MinHeapNode[n];
		vAdd = new int[n];
		
		for(int i = 0; i < n; i++){
			node[i] = new MinHeapNode(i, m.MaxVal);
			vAdd[i] = i;
		}
	}
	
	public int Left(int i){
		return (2*i + 1);
	}
	
	public int Right(int i){
		return (2*i + 2);
	}
	
	public int Parent(int i){
		return (i - 1)/2;
	}
	
	public void Exchange(int i, int j){
			// swap the node in the MinHeap
		MinHeapNode a = this.node[i];
		MinHeapNode b = this.node[j];
		this.node[i] = b;
		this.node[j] = a;
			// swap index in the vAdd
		this.vAdd[b.vertex] = i;
		this.vAdd[a.vertex] = j;
	}
	
	public void Min_Heapify(int i){
		int l = this.Left(i);
		int r = this.Right(i);
		int smallest = 0;
		if(l<=this.heapSize && this.node[l].cost<this.node[i].cost)
			smallest = l;
		else
			smallest = i;
		if(r<=this.heapSize && this.node[r].cost<this.node[smallest].cost)
			smallest = r;
		if(smallest != i){
			this.Exchange(i, smallest);
			this.Min_Heapify(smallest);
		}
	}
	
	public MinHeapNode Extract_Min(){
		if(this.heapSize < 1)
			return null;
		MinHeapNode min = this.node[0];
		this.Exchange(0, this.heapSize);
		this.heapSize -= 1;
		this.Min_Heapify(0);
		
		return min;
	}
	
	public void Decrease_key(int i, int cost){
		this.node[i].cost = cost;
		while(i>0 && this.node[this.Parent(i)].cost>this.node[i].cost){
			this.Exchange(i, this.Parent(i));
			i = this.Parent(i);
		}
	}
}
