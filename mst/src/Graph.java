
public class Graph {
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
