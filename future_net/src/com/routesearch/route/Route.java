/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import java.util.LinkedList;
import java.util.List;

public final class Route
{
	private final static int INFINITY = Integer.MAX_VALUE;         // 无穷大
	private static int nodes = 0;                                    // 节点数
	private static int source = -1;                                  // 起点
	private static int destination = -1;                             // 汇点
	private List<Integer> includingSet = new LinkedList<Integer>();  // 中间点集合
	private EdgeBean[][] graphMatrix;                                 // 邻接矩阵
	private List<Integer> result = new LinkedList<Integer>();
	
	
	
    /**
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition)
    {
    	Route route = new Route();
    	
    	// test code
    	System.out.println("------------------------------");
    	System.out.println(graphContent);
    	System.out.println(condition);
    	System.out.println("------------------------------");
    	
    	nodes = route.initGraph(graphContent);
    	
    	route.handleCondition(condition);
    	
    	route.Dijkstra(source, destination);
    	
        return route.Dijkstra(source, destination);
    }
    
    /** 
     * 初始化邻接矩阵
     * @param grapnContent read from files
     * @return nodes of graph
     */
    private int initGraph(String graphContent)
    {
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
    	
    	// test code
    	for (int i = 0; i <= max; i++)
    	{
    		for (int j = 0; j <= max; j++)
    		{
    			System.out.print(graphMatrix[i][j].weight + "  ");
    		}
    		
    		System.out.println();
    	} 
    	
    	return (max + 1);    	    	
    }    
    
    /**
     * @param graph
     * @param n count of node
     * @param condition contain start node, end node, including set
     */
    private String Dijkstra(int src, int des)
    {
    	int begin = src;
    	int end = des;
    	int[] distance = new int[nodes]; // 记录从源点到其他点的距离
    	int[] path = new int[nodes];     // 记录路径上对应节点的前驱节点
    	int[] s = new int[nodes];        // 记录已经走过的点, 1表示已经走过，0表示未走过
    	List<Integer> passedSet = new LinkedList<Integer>();  // 记录已经走过的中间点
    	int minDis = INFINITY;
    	int cntOfgoThrough = includingSet.size();
    	int u;
    	boolean tag;
    	
    	u = -1;
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		distance[i] = graphMatrix[source][i].getWeight();
    		s[i] = 0;
    		
    		if (distance[i] < INFINITY)
    		{
    			path[i] = begin;
    		}
    		else
    		{
    			path[i] = -1;
    		}
    	}
    	
    	s[begin] = 1;
    	path[begin] = -2;
    	
    	for (int l = 0; l < nodes; l++)
    	{
    		graphMatrix[l][begin].setWeight(INFINITY);
    	}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		minDis = INFINITY;
    		u = -1;
    		
    		// 首先判断中间节点可不可达，可达则另 u 为中间节点
    		// 不可达选取不在 s 中且具有最小距离的顶点 u
    		tag = true;
    		for (int j = 0; j < includingSet.size(); j++)
    		{
    			if (s[includingSet.get(j)] == 0 && 
    					distance[includingSet.get(j)] < minDis)
    			{
    				u = includingSet.get(j);
    				minDis = distance[includingSet.get(j)];
    				tag = false;
    				passedSet.add(includingSet.get(j));
    				includingSet.remove(j);
    				break;
    			}
    		}
    		
    		if (tag)
    		{
    			for (int j = 0; j < nodes; j++)
        		{
        			if (cntOfgoThrough != passedSet.size() && s[j] == 0 && 
        					distance[j] < minDis && j != end)
        			{
        				u = j;
        				minDis = distance[j];
        			}
        			else if (cntOfgoThrough == passedSet.size() && s[j] == 0 && 
        					 	distance[j] < minDis)
        			{
        				u = j;
        				minDis = distance[j];
        			}
        		}    			
    		}    		
    		
    		// 顶点 u 加入 s 中
    		s[u] = 1;
    		
    		// 对邻接矩阵进行修改
    		for (int i1 = 0; i1 < nodes; i1++)
    		{
    			graphMatrix[i1][u].setWeight(INFINITY);
    		}
    		
    		// 修改不在 s 中的顶点的距离
    		for (int j = 0; j < nodes; j++)
    		{
    			if (s[j] == 0)
    			{
    				if ( (graphMatrix[u][j].getWeight() < INFINITY) &&
    						(distance[u] + graphMatrix[u][j].getWeight() < distance[j]) )
    				{
    					distance[j] = distance[u] + graphMatrix[u][j].getWeight();
    					path[j] = u;
    				}
    			}
    		}
    		
    		if (u == end)
    		{
    			break;
    		}
    		
    	}
    	
    	return getPath(distance, path, s, src);
    }
    
    private String getPath(int dis[], int path[], int s[], int src)
    {
    	if (s[destination] == 0)
    		return null;
    	Ppath(path, destination, src);
    	
    	String track;
    	
    	if (result.isEmpty())
    		return null;
    	
    	track = "" + graphMatrix[src][result.get(0)].getNumber() + "|";
    	
    	
    	for (int i = 0; i < result.size() - 1; i++)
    	{
    		track += graphMatrix[result.get(i)][result.get(i + 1)].getNumber() + "|";
    	}
    	
    	track += graphMatrix[result.get(result.size() - 1)][destination].getNumber();
    	
    	return track;
    }
    
    private void Ppath(int path[], int i, int src)
    {
    	int k;
    	
    	k = path[i];
    	if (k == src)
    	{
    		return ;
    	}
    	
    	Ppath(path, k, src);
    	
    	result.add(k);
    }
    
    private void handleCondition(String condition)
    {
    	// 处理 condition
    	String[] subStr = condition.split(","); 
    	String[] passVertex = subStr[2].split("\\|");
    	source = Integer.parseInt(subStr[0]);
    	destination = Integer.parseInt(subStr[1]);    	
//    	includingSet = new int[passVertex.length];
    	
    	for (int i = 0; i < passVertex.length; i++)
    	{    		
    		if (i == passVertex.length - 1)
    		{
    	    	// 因为passVertex中最后一个字符串的结束符是'\n'
    	    	includingSet.add(Integer.parseInt(passVertex[i].substring(0, passVertex.length - 1)));
    		}
    		else
    		{
    			includingSet.add(Integer.parseInt(passVertex[i]));
    		}
    	}
    	
    	return ;
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