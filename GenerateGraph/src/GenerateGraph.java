import java.io.*;

public class GenerateGraph {

	public int n;
	public int m;
	public int num;
	public EC ec[];
	
	GenerateGraph(int n, int m){
		this.n = n;
		this.m = m;
		this.num = 0;
		ec = new EC[m];
		for(int i = 0; i < m; i++){
			ec[i] = new EC(0,0,0);
		}
	}
	
	public void WriteFile(String fileName, GenerateGraph gg){
		String s = "";
		s += gg.n + " " + gg.m + "\r\n";
		int ii = 0;
//		for(int i=0;i<gg.m;i++){
//			s += gg.ec[i].i + " " + gg.ec[i].j + " " + gg.ec[i].cost + "\r\n";
//		}
//		s = s.substring(0, s.length() - 1);
		File file = new File(fileName);
	    BufferedWriter writer = null;
	    try {
	        writer = new BufferedWriter(new FileWriter(file));
	        
	        for(int i=0;i<=gg.m/100;i++){
	        	if(i==0){
	        		writer.write(s);
	        		s = "";
	        	}
	        	else{
	        		s = "";
	        	}
	        	for(int j=0;j<100;j++){
	        		if((100*i+j)<gg.m){
	        			s += gg.ec[i*100+j].i + " " + gg.ec[i*100+j].j + " " + gg.ec[i*100+j].cost + "\r\n";
	        			ii ++;
	        		}
	        		else{
	        			break;
	        		}
	        	}
	        	System.out.println(i*100);
	        	writer.write(s);
	        }
	        
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (writer != null) {
	               try {
	                   writer.close();
	               } catch (IOException e1) {
	               }
	        }
	    }
	    System.out.println(ii + "rows");
	    System.out.println("Writing file is done!");
	}
	
	public void AddNode(int i, int j, int cost, GenerateGraph gg, int graph[][]){
		graph[i][j] = 1;
		graph[j][i] = 1;
		
		gg.ec[num].Add(i, j, cost);
		gg.num ++;
	}
	
	public void RandomMode(GenerateGraph gg){
		int graph[][] = new int[gg.n][];
		for(int i = 0; i < gg.n; i++){
			graph[i] = new int[gg.n];
		}
		for(int i = 0; i < gg.n; i++)
			for(int j = 0; j < gg.n; j++)
				graph[i][j] = 0;
		int k = 0;
		while(true){
			int i = (int)(gg.n * java.lang.Math.random());
			int j = (int)(gg.n * java.lang.Math.random());
			int cost = (int)(1000 * java.lang.Math.random()) + 2;
			
			if(i!=j && graph[i][j]!=1 && graph[j][i]!=1){
				gg.AddNode(i,j,cost,gg,graph);
				k++;
			}
			if(k == gg.m)
				break;
		}
	}
	
	public void UserInputMode(GenerateGraph gg){
		int graph[][] = new int[n][];
		for(int i = 0; i < n; i++){
			graph[i] = new int[n];
		}
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				graph[i][j] = 0;
		
		// 0 to any other edges is the smallest
//		for(int i=0;i<gg.n;i++){
//			if(i!=0){
//				gg.AddNode(0, i, 1, gg, graph);
//			}
//		}
			//	i to (i+1) is smallest
		for(int i=0;i<(gg.n-1);i++){
			gg.AddNode(i, i+1, 1, gg, graph);
		}
		
		int k = gg.num;
		while(true){
			int i = (int)(gg.n * java.lang.Math.random());
			int j = (int)(gg.n * java.lang.Math.random());
			int cost = (int)(1000 * java.lang.Math.random()) + 1;
			
			if(i!=j && graph[i][j]!=1 && graph[j][i]!=1){			
				gg.AddNode(i,j,cost,gg,graph);
				k++;
			}
			if(k == gg.m)
				break;
		}
	}
	
	public static void main(String[] args) {
		int n = 5000;
		int d = 100;
		int ed = ((n*(n-1)/2)*d)/100;
		
		GenerateGraph gg = new GenerateGraph(5000,10000);
			//	Random mode
//		gg.RandomMode(gg);
			//	User Input Mode
		gg.UserInputMode(gg);
		
		gg.WriteFile("File.txt", gg);

	}

}

final class EC {
	public int i;
	public int j;
	public int cost;
	
	EC(int i, int j, int cost){
		this.i = i;
		this.j = j;
		this.cost = cost;
	}
	
	public void Add(int i, int j, int cost){
		this.i = i;
		this.j = j;
		this.cost = cost;
	}
}