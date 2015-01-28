import java.io.*;

/**
 * @author Xuan Sheng UFID:1381-8475
 * 
 * @param args[0]: -r random a graph; 
 * 				   -s simple scheme of MST; 
 * 				   -f run the f-heap scheme of MST
 * 
 * 		  args[1]: under -r mode: number of the vertices; 
 * 				   under -s -f mode: the file name
 * 
 * 		  args[2]: under -r mode: density of the graph
 */	


public class mst {
	public static int MaxVal = 9999;
	public int n;
	public int d;
	public int vFrom[];
	public int vKey[];
	public int vInMst[];	// flag whether vertex is in the mst or not
	
	mst(int a, int b) {
		n = a;
		d = b;
		vFrom = new int[n];
		vKey = new int[n];
		vInMst = new int[n];
		for(int i=0;i<this.n;i++){
			vInMst[i] = 0;
		}
	}
	
	mst(){
	}
	
	public void Initial(int a, int b){
		n = a;
		d = b;
		vFrom = new int[n];
		vKey = new int[n];
		vInMst = new int[n];
		for(int i=0;i<this.n;i++){
			vInMst[i] = 0;
		}
	}
	
	public void initialFib(Graph g, FibonacciHeap f, int start){
		vKey[start] = 0;
		vInMst[start] = 1;
		AdjVer p = g.v[start].next;
		while(p!=null){
			this.vFrom[p.vertex] = start;
			this.vKey[p.vertex] = p.cost;
			f.AddNode(p.cost, p.vertex, g);
			
			p = p.next;
		}
	}
	
	public int MST_Prim_FibonacciHeap(Graph g, int start, FibonacciHeap f){
		//initial tracing list
		for(int i = 0;i < this.n;i++){
			vFrom[i] = -1;
			vKey[i] = MaxVal;
			vInMst[i] = 0;
		}
		
		this.initialFib(g, f, start);	// initial Fibonacci Heap based on the Start vertex
		int k = 1;
		int totalCost = 0;
		while(k < n){
			HeapNode h = f.Extract_Min();	//	h---u: Extract-Min
			this.vInMst[h.vertex] = 1;
			
			AdjVer p = g.v[h.vertex].next;	//	p---v: v in the AdjList of u
			while(p!=null){
				if(this.vInMst[p.vertex]==0 && p.cost<this.vKey[p.vertex]){	// p is not in MST and should decrease key of p
					this.vFrom[p.vertex] = h.vertex;
					this.vKey[p.vertex] = p.cost;
					
					//this key changes from infinite, just add node into Fibonacci Heap
					if(f.vAdd[p.vertex] == null){	// this node is not added into Fibanacci Heap
						f.AddNode(p.cost, p.vertex, g);
					}	
					else{	// this node has already in the Fibonacci Heap, so decrease the key
						f.vAdd[p.vertex].cost = p.cost;	// based on the pointer stored in vAdd[] we can find the node in Fibonacci Heap
						f.Decrease_Key(f.vAdd[p.vertex]);
					}
				}
				p = p.next;
			}
				// output the edges in the mst
//			System.out.println(this.vFrom[h.vertex] + " " + h.vertex + " " + this.vKey[h.vertex] + "---" + k);
			totalCost += this.vKey[h.vertex];
			
			k++;
		}
//		System.out.println("The total cost of the mst is " + totalCost);
		return totalCost;
	}
	
	public void ReadFile(String fileName, mst m, Graph g){
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int k = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] Array = tempString.trim().split(" ");
            	if(k == 0){
            		int n = Integer.parseInt(Array[0].trim());
            		int d = Integer.parseInt(Array[1].trim());
            		m.Initial(n, d);
            		g.Initial(n, d);
            	}
            	else{
            		int i = Integer.parseInt(Array[0].trim());
            		int j = Integer.parseInt(Array[1].trim());
            		int cost = Integer.parseInt(Array[2].trim());
            		AdjVer a1 = new AdjVer(j, cost);
            		a1.next = g.v[i].next;
            		g.v[i].next = a1;
            		
            		AdjVer a2 = new AdjVer(i, cost);
            		a2.next = g.v[j].next;
            		g.v[j].next = a2;
            	}
            	
            	k++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

	}
	
	public void OutputMST(int sum){
		System.out.println(sum);
		for(int i = 0; i < this.n; i++){
			if(i != 0){
				System.out.println(this.vFrom[i] + " " + i);
			}
		}
	}
	
	public void InitialMinHeap(Graph g, int start, MinHeap m){
		vKey[start] = 0;
		vInMst[start] = 1;
		AdjVer p = g.v[start].next;
		while(p!=null){
			this.vFrom[p.vertex] = start;
			this.vKey[p.vertex] = p.cost;
			m.Decrease_key(m.vAdd[p.vertex], p.cost);
			
			p = p.next;
		}
	}
	
	public int MST_Prim_MinHeap(Graph g, int start, MinHeap m){
		//initial tracing list
		for(int i = 0;i < this.n;i++){
			vFrom[i] = -1;
			vKey[i] = MaxVal;
			vInMst[i] = 0;
		}
		this.InitialMinHeap(g, start, m);
		int k = 1;
		int totalCost = 0;
		while(k < n){
			MinHeapNode h = m.Extract_Min();	//	h---u: Extract-Min
			this.vInMst[h.vertex] = 1;
			
			AdjVer p = g.v[h.vertex].next;	//	p---v: v in the AdjList of u
			while(p!=null){
				if(this.vInMst[p.vertex]==0 && p.cost<this.vKey[p.vertex]){
					this.vFrom[p.vertex] = h.vertex;
					this.vKey[p.vertex] = p.cost;
					
					m.Decrease_key(m.vAdd[p.vertex], p.cost);
				}
				p = p.next;
			}
				// output the edges in the mst
//			System.out.println(this.vFrom[h.vertex] + " " + h.vertex + " " + this.vKey[h.vertex] + "---" + k);
			totalCost += this.vKey[h.vertex];
			
			k++;
		}
		System.out.println("The total cost of the mst is " + totalCost);
		return totalCost;
	}
	
	public void InitialArray(Graph g, int start, Array a){
		vKey[start] = 0;
		vInMst[start] = 1;
		AdjVer p = g.v[start].next;
		while(p!=null){
			this.vFrom[p.vertex] = start;
			this.vKey[p.vertex] = p.cost;
	//		m.Decrease_key(m.vAdd[p.vertex], p.cost);
			a.Decrease_Key(p.vertex, p.cost);
			
			p = p.next;
		}
	}
	
	public int MST_Prim_Array(Graph g, int start, Array a, mst m){
		for(int i = 0;i < this.n;i++){
			vFrom[i] = -1;
			vKey[i] = MaxVal;
			vInMst[i] = 0;
		}
		this.InitialArray(g, start, a);
		int k = 1;
		int totalCost = 0;
		while(k < n){
			MinHeapNode h = a.Extract_Min(m);	//	h---u: Extract-Min
			this.vInMst[h.vertex] = 1;
			
			AdjVer p = g.v[h.vertex].next;	//	p---v: v in the AdjList of u
			while(p!=null){
				if(this.vInMst[p.vertex]==0 && p.cost<this.vKey[p.vertex]){	// p is not in MST and should decrease key of p
					this.vFrom[p.vertex] = h.vertex;
					this.vKey[p.vertex] = p.cost;
					
					a.Decrease_Key(p.vertex, p.cost);
				}
				p = p.next;
			}
				// output the edges in the mst
//			System.out.println(this.vFrom[h.vertex] + " " + h.vertex + " " + this.vKey[h.vertex] + "---" + k);
			totalCost += this.vKey[h.vertex];
			
			k++;
		}
//		System.out.println("The total cost of the mst is " + totalCost);
		return totalCost;
	}
	
	public static void main(String[] args) {
		int n = 3000;
		int d = 50;
		long start = 0;
		long stop = 0;
		int flag = 1;
		int sum = 0;
		String fFile = "";
		String sFile = "";
		
		mst m = new mst();
		Graph g = new Graph();
		
		String com = args[0];
		if(com.equals("-r")){	// Random Mode
			n = Integer.parseInt(args[1]);
			d = Integer.parseInt(args[2]);
				// Random Mode
			m.Initial(n, d);
			g.Initial(m.n,m.d);
			g.CreateGraph();
			
				// Fibonacci Heap
			FibonacciHeap f = new FibonacciHeap(m.n);
			start = System.currentTimeMillis();
			sum = m.MST_Prim_FibonacciHeap(g, 0, f);
			stop = System.currentTimeMillis();
			System.out.println("The total cost of the mst is " + sum + " with Fibonacci Heap.");
			System.out.println("The running time of FibonacciHeap scheme is " + (stop - start));
//			m.OutputMST(sum);
				// end Fibonacci Heap
			
				// Min Heap
//			MinHeap mh = new MinHeap(m.n, m);
//			start = System.currentTimeMillis();
//			sum = m.MST_Prim_MinHeap(g, 0, mh);
//			stop = System.currentTimeMillis();
//			System.out.println("MinHeap time is " + (stop - start));
//			m.OutputMST(sum);
				// end Min Heap
			
				//	Array
			Array a = new Array(m.n, m);
			start = System.currentTimeMillis();
			sum = m.MST_Prim_Array(g, 0, a, m);
			stop = System.currentTimeMillis();
			System.out.println("The total cost of the mst is " + sum + " with Array.");
			System.out.println("The running time of Array scheme is " + (stop - start));
//			m.OutputMST(sum);
				//	end Array
			
		}
		else if(com.equals("-s")){	//	User Input Mode - Array
			fFile = args[1];
				// User Input Mode
			m.ReadFile(fFile, m, g);
			if(!g.DFS()){
				flag = 0;
				System.out.println("Unconnected graph is given!");
			}
			
			if(flag == 1){
					//	Array
				Array a = new Array(m.n, m);
				start = System.currentTimeMillis();
				sum = m.MST_Prim_Array(g, 0, a, m);
				stop = System.currentTimeMillis();
				System.out.println("The running time of Array scheme is " + (stop - start));
				m.OutputMST(sum);
					//	end Array
			}
		}
		else if(com.equals("-f")){	// User Input Mode - Fibnacci heap
			sFile = args[1];
				// User Input Mode
			m.ReadFile(sFile, m, g);
			if(!g.DFS()){
				flag = 0;
				System.out.println("Unconnected graph is given!");
			}
			
			if(flag == 1){
					// Fibonacci Heap
				FibonacciHeap f = new FibonacciHeap(m.n);
				start = System.currentTimeMillis();
				sum = m.MST_Prim_FibonacciHeap(g, 0, f);
				stop = System.currentTimeMillis();
				System.out.println("The running time of FibonacciHeap scheme is " + (stop - start));
				m.OutputMST(sum);
					// end Fibonacci Heap
			}
		}
		else{}	
		
	}

}

final class Graph {
	public int n;	//	the number of vertices in graph
	public int d;	//	density of edges in graph
	public int ed;	//	total edges in graph
	public AdjVer[] v;	//	adjacency lists
	
	Graph(int n, int d){
		this.n = n;
		this.d = d;
		v = new AdjVer[n];
		for(int i = 0; i < n; i++){
			v[i] = new AdjVer(i,0);
		}
	}
	
	Graph(){
	}
	
	public void Initial(int n, int d){	//	initial function
		this.n = n;
		this.d = d;
		v = new AdjVer[n];
		for(int i = 0; i < n; i++){
			v[i] = new AdjVer(i,0);
		}
	}
	
	public void CreateGraph(){	//	create graph
		this.TryCreateGraph();
		while(!this.DFS())	//	if graph generated is not connected, generate it again
			this.TryCreateGraph();
	}
	
	public void TryCreateGraph(){	// generate graph
		int numOfEdges = n*(n-1)/2;
		this.ed = (int)(numOfEdges*d/100);
		
		int graph[][] = new int[n][];
		for(int i = 0; i < n; i++){
			graph[i] = new int[n];
		}
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				graph[i][j] = 0;
		
		int k = 0;
		while(true){	//	add edges one by one
			int i = (int)(n * java.lang.Math.random());
			int j = (int)(n * java.lang.Math.random());
			int cost = (int)(1000 * java.lang.Math.random()) + 1;
			
			if(i!=j && graph[i][j]!=1 && graph[j][i]!=1){
				graph[i][j] = 1;
				graph[j][i] = 1;
				Add(i,j,cost);
				k++;
			}
			if(k==ed)
				break;
		}
		
	}
	
	public void Add(int i,int j,int cost){	//	add an edge to graph
		AdjVer av1 = new AdjVer(j,cost);
		av1.next = v[i].next;
		v[i].next = av1;
		
		AdjVer av2 = new AdjVer(i,cost);
		av2.next = v[j].next;
		v[j].next = av2;
	}
	
	public void DFS_Visit(int u, int color[]){	// the function go deep through the vertex unexplored
		color[u] = 1;
		AdjVer p = this.v[u].next;
		while(p!=null){
			if(color[p.vertex]==0){
				DFS_Visit(p.vertex,color);
			}
			p = p.next;
		}
	}
	
	public boolean DFS(){	// depth-first search to check whether graph is connected
		int color[] = new int[this.n];
		for(int i=0;i<this.n;i++){
			color[i] = 0;	// color of unexplored vertex is 0; color of explored vertex is 1
		}
		color[0] = 1;
		AdjVer p = v[0].next;
		while(p!=null){
			if(color[p.vertex]==0){
				DFS_Visit(p.vertex,color);
			}
			p = p.next;
		}
		int j;
		for(j=0;j<this.n;j++){
			if(color[j]==0)
				break;
		}
		if(j<this.n)
			return false;
		else
			return true;
	}
	
	public void print(){	//	print all edges in graph
		long sum = 0;
		for(int i=0;i<n;i++){
//			System.out.print(i + "   ");
			AdjVer t = v[i].next;
			while(t!=null){
//				System.out.print("(" + i + "," + t.vertex + "," + t.cost + ")");
				//sum += t.vertex;
				sum += 1;
				t = t.next;
			}
//			System.out.println();
		}
		System.out.println(sum + " " + this.ed*2);
	}
}

final class AdjVer {	//	adjacency lists
	public int vertex;	// in this adjacency list, if this is a head, vertex means this list belongs to this vertex, otherwise vertex means this vertex has an edge with the node this adjacency list belongs to
	public int cost;	// cost of edge, other than in head node
	public AdjVer next;
	
	AdjVer(int ver, int c){
		this.vertex = ver;
		this.cost = c;
		this.next = null;
	}

}

final class HeapNode {	//	the unit node in Fibonacci Heap
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

final class FibonacciHeap {	//	Fibonacci Heap
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

final class MinHeapNode {	//	nodes with simple structure
	public int vertex;	//	this vertex has edges with at least one vertex added to the MST
	public int cost;	// the minimum cost of any edges connecting this vertex to the vertices added to the MST
	
	MinHeapNode(int v, int c){
		this.vertex = v;
		this.cost = c;
	}
}

final class MinHeap {
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

final class Array {	//	Array used to implement MST_Prim
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



