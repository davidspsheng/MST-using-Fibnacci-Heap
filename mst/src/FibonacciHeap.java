
public class FibonacciHeap {
	public HeapNode min;	//	min node pointer
	public int tableSize;	//	size of table used in pairwise combine
	public HeapNode table[];	// table used in the pairwise combination
	public HeapNode vAdd[];	// keep the address of the vertex in the Fibonacci Heap if it is added into the heap
	
	FibonacciHeap(int n){
		min = new HeapNode(0,-1,-1);
		tableSize = (int)(java.lang.Math.log(n)/java.lang.Math.log(2)) + 2;
		table = new HeapNode[tableSize];
		for(int i = 0; i < tableSize; i++){
			table[i] = null;
		}
		vAdd = new HeapNode[n];
		for(int i=0;i<n;i++){
			vAdd[i] = null;
		}
	}
	
	public void AddNode(int cost, int v, Graph g){	// add node into Fibonacci Heap
		HeapNode h = new HeapNode(0, cost, v);
		
		this.vAdd[v] = h;
		
		if(this.min.cost == -1){	// Fibonacci Heap is empty
			this.min = h;
			this.min.lSibling = this.min;
			this.min.rSibling = this.min;
		}
		else{
			h.rSibling = this.min.rSibling;
			h.lSibling = this.min;
			this.min.rSibling.lSibling = h;
			this.min.rSibling = h;
			if(h.cost < this.min.cost)
				this.min = h;
		}
	}
	
	public void PairwiseComb(){	//	pairwise combine
		if(this.min.cost == -1)
			return;
		
			// combination part
		HeapNode h = this.min;
		HeapNode p = h.rSibling;
		HeapNode flag = this.min.lSibling;	//	flag--whether pointer scan all items in top-level list
		int a = 0;
		int maxIndex = -1;	
		while(a == 0){
			if(h == flag){	// flag is the last min heap in the path we scan, so if h points to flag, this will the last time combine
				a = 1;
			}
			while(true){	// loop goes until no entry in table has the same degree with h, and then insert h into table
				int de = h.degree;
				if(de > maxIndex){
					maxIndex = de;
				}
				if(table[de] == null){	//	no entry in table has the same degree with h, and loop terminates
					table[de] = h;
					break;
				}
				else{	//	combine h and table[de]
					if(h.cost < table[de].cost){	//	h.child = table[de]
						table[de].parent = h;
						if(h.child == null){	//	h has no child, its child pointer points to table[de]
							h.child = table[de];
								//remove table[de] from the top-level list
							table[de].lSibling.rSibling = table[de].rSibling;
							table[de].rSibling.lSibling = table[de].lSibling;
								// insert table[de] into h's child circular list
							table[de].lSibling = table[de];
							table[de].rSibling = table[de];
						}
						else{	// h has children, insert table[de] into its children list
								//remove table[de] from the top-level list
							table[de].lSibling.rSibling = table[de].rSibling;
							table[de].rSibling.lSibling = table[de].lSibling;
								// insert table[de] into h's child circular list
							table[de].rSibling = h.child.rSibling;
							table[de].lSibling = h.child;
							h.child.rSibling.lSibling = table[de];
							h.child.rSibling = table[de];
						}
						
						h.degree += 1;
						table[de].childCut = false;
						
						table[de] = null;
						//h = h;	//	keep the combine pointer h, and in this case no need to update pointer h
						
					}
					else{							//	table[de].child = h
						h.parent = table[de];
						if(table[de].child == null){
							table[de].child = h;
								//remove h from the top-level list
							h.lSibling.rSibling = h.rSibling;
							h.rSibling.lSibling = h.lSibling;
								// insert h into table[de]'s child circular list
							h.lSibling = h;
							h.rSibling = h;
						}
						else{
								//remove h from the top-level list
							h.lSibling.rSibling = h.rSibling;
							h.rSibling.lSibling = h.lSibling;
								// insert h into table[de]'s child circular list
							h.rSibling = table[de].child.rSibling;
							h.lSibling = table[de].child;
							table[de].child.rSibling.lSibling = h;
							table[de].child.rSibling = h;
						}
						
						table[de].degree += 1;
						h.childCut = false;
						
						h = table[de];	// h always points to the root of the two combined heap trees
						table[de] = null;
						
					}
				}
			}
			
			h = p;
			p = p.rSibling;
		}
		
			// set table to null and reset min pointer part
		int j = 0;
		for(j = 0; j<= maxIndex; j++){	// to increase efficiency, we just reset table with maxIndex, which is the max index used in this combine
			if(table[j] != null){
				this.min = table[j];
				break;
			}
		}
		for(int i = j;i <= maxIndex;i++){
			if(table[i] != null){
				if(this.min.cost > table[i].cost){
					this.min = table[i];
				}
				table[i] = null;
			}
		}
	}
	
	public HeapNode Extract_Min(){	//	Extract min
		// remove the min node from the top circular list
		HeapNode m = this.min;
		if(this.min.lSibling!=this.min && this.min.rSibling!=this.min){	//there are more than one node in the top circular list
			if(this.min.child == null){	//	no need to reinsert the subtrees of min into the top-level list
					//	remove min node from the top-level list
				this.min.lSibling.rSibling = this.min.rSibling;
				this.min.rSibling.lSibling = this.min.lSibling;
					// set min point to the right sibling of the min temporarily, pairwise combine will update min pointer
				this.min = this.min.rSibling;
			}
			else{
					//	reinsert subtrees of min node
				HeapNode from = this.min.child;
				HeapNode to = this.min.child.lSibling;
				
				this.min.rSibling.lSibling = this.min.child.lSibling;
				this.min.child.lSibling.rSibling = this.min.rSibling;
				this.min.lSibling.rSibling = this.min.child;
				this.min.child.lSibling = this.min.lSibling;
					//	set the parent fields of  the children of min node to null
				HeapNode p = from;
				while(true){
					p.parent = null;
					if(p == to)
						break;
					else
						p = p.rSibling;
				}
					// set min point to the child of the min temporarily, pairwise combine will update the min pointer
				this.min = this.min.child;
			}
		}
		else{	// there is only one node in the top circular list
			if(this.min.child == null){	//	there is no child of min node
				this.min.cost = -1;
			}
			else{
					//	set the parent fields of the children of min node to null
				HeapNode from = this.min.child;
				HeapNode to = this.min.child.lSibling;
				HeapNode p = from;
				
				while(true){
					p.parent = null;
					if(p == to)
						break;
					else
						p = p.rSibling;
				}
					// set min point to the child of the min temporarily, pairwise combine will update the min pointer
				this.min = this.min.child;
			}
		}
		
			// pairwise combine and find the new min	
		this.PairwiseComb();
	
		return m;
	}
	
	public void Decrease_Key(HeapNode hNode){	//	Decrease key of hNode
		if(hNode.parent == null)	// this node is in the top-level list, so decrease-key will not trigger more work
			return;
		
		if(hNode.cost < hNode.parent.cost){	// violate the property, more work is needed
				//	remove this node from the circular list and insert into top-level list
			if(hNode.parent.child == hNode){	//if hNode's parent's child-pointer points to hNode, then change parent's child-pointer
				if(hNode.parent.degree == 1){	//if hNode's parent has only one child, set its child-pointer null
					hNode.parent.child = null;
				}
				else{	// set hNode's parent's child-pointer the right sibling of hNode
					hNode.parent.child = hNode.rSibling;
				}
			}
				// remove hNode from the child list
			hNode.lSibling.rSibling = hNode.rSibling;
			hNode.rSibling.lSibling = hNode.lSibling;
				//insert into the top-level list
			hNode.lSibling = this.min;
			hNode.rSibling = this.min.rSibling;
			this.min.rSibling.lSibling = hNode;
			this.min.rSibling = hNode;
			
				// update min-pointer if necessary
			if(hNode.cost < this.min.cost)
				this.min = hNode;
			
			hNode.parent.degree -= 1;
			HeapNode p = hNode.parent;
			hNode.parent = null;
			
				// CascadingCut
			this.CascadingCut(p);
		}
	}
	
	public void CascadingCut(HeapNode h){	//	CascadingCut of HeapNode h, this is a recursive procedure
		if(!h.childCut){
			h.childCut = true;
			return;
		}
		else{
				// remove this node from the circular list and insert into the top-level list
			if(h.parent == null)
				return;
			if(h.parent.child == h){	// the parent's child-pointer points to h, we should update this child-pointer
				if(h.parent.degree == 1){	// the parent only has one child -- h
					h.parent.child = null;
				}
				else
					h.parent.child = h.rSibling;
			}
				// remove h from the children list
			h.lSibling.rSibling = h.rSibling;
			h.rSibling.lSibling = h.lSibling;
				//insert into the top-level list
			h.lSibling = this.min;
			h.rSibling = this.min.rSibling;
			this.min.rSibling.lSibling = h;
			this.min.rSibling = h;
			
			// update min-pointer if necessary
//			if(h.cost < this.min.cost)
//				this.min = h;
			
			h.parent.degree -= 1;
			HeapNode p = h.parent;
			h.parent = null;
			
				// CascadingCut recursively
			this.CascadingCut(p);
		}
	}
}
