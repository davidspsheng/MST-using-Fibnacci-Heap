import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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