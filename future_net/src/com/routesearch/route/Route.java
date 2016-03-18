/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

public final class Route
{
	private final static int INFINITY = Integer.MAX_VALUE;
	
    /**
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition)
    {
    	System.out.println("------------------------------");
    	System.out.println(graphContent);
    	System.out.println("------------------------------");
    	initGraph(graphContent);
        return "hello world!";
    }
    
    private static void initGraph(String graphContent)
    {
    	EdgeBean[][] graphMatrix;
    	int rows = graphContent.length() / 8;
    	int[][] temp = new int[4][rows];
    	int index = 0;
    	int max = 0;
    	
    	// 每一行的格式为 0,0,1,2（LinkID,SourceID,DestinationID,Cost）
    	// temp第 0 行存储编号，第 1 行存储源点，第 2 行存储汇点，第 3 行存储权重
    	for (int i = 0; i < rows; i++)
    	{
    		// 每一行的起始地址
    		index = i * 8;
    		
    		temp[0][i] = Integer.parseInt(graphContent.substring(index, index+1));
    		temp[1][i] = Integer.parseInt(graphContent.substring(index + 2, index + 3));
    		temp[2][i] = Integer.parseInt(graphContent.substring(index + 4, index + 5));
    		temp[3][i] = Integer.parseInt(graphContent.substring(index + 6, index + 7));
    		
    		// 求出最大节点编号
    		max = Integer.max(max, temp[1][i]);
    	}
    	
    	// 数组初始化，边的权重为无穷大，编号为-1
    	// 因为节点编号从0开始，所以 +1
    	graphMatrix = new EdgeBean[max+1][max+1];
    	for (int i = 0; i <= max; i++)
    	{
    		for (int j = 0; j <= max; j++)
    		{
    			graphMatrix[i][j] = new EdgeBean();
    		}
    	}
    	
    	// 为边赋值
    	for (int i = 0; i < temp[0].length; i++)
    	{    		 
    		int src = temp[1][i];
    		int des = temp[2][i];
    		
    		graphMatrix[src][des].setWeight(temp[3][i]);
    		graphMatrix[src][des].setNumber(temp[0][i]);
    	}
    	
    	for (int i = 0; i <= max; i++)
    	{
    		for (int j = 0; j <= max; j++)
    		{
    			System.out.print(graphMatrix[i][j].weight + "  ");
    		}
    		
    		System.out.println();
    	} 
    	    	
    }    

}

class EdgeBean
{
	public int weight;
	public int no;
	
	public EdgeBean() 
	{
		weight = Integer.MAX_VALUE;
		no = -1;
	}
	
	public void setWeight(int w) 
	{
		this.weight = w;
	}
	
	public int getWeight()
	{
		return this.weight;
	}
	
	public void setNumber(int n) 
	{
		this.no = n;
	}
	
	public int getNumber()
	{
		return this.no;
	}
}