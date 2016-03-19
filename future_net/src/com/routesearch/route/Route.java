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
import com.filetool.util.*;

public final class Route
{
	private final static long startTime = System.currentTimeMillis();
	private boolean terminate = false;
	private final static int INFINITY = Integer.MAX_VALUE;          // 无穷大
	private static int nodes = 0;                                     // 节点数
	private static int source = -1;                                   // 起点
	private static int destination = -1;                              // 汇点
	private List<Integer> includingSet = new LinkedList<Integer>();   // 中间点集合
	private EdgeBean[][] graphMatrix;                                 // 邻接矩阵
	private EdgeBean[][] replicaOfgraphMatrix;                        // 邻接矩阵副本，计算Lowcost会用到
	private List<Integer> result = new LinkedList<Integer>();         // 每次寻找路径时保存经过的点
	private int[] path = null;                                        // 记录路径上对应节点的前驱节点
	private int[] s = null;                                           // 记录已经走过的点, 1表示已经走过，0表示未走过
	private List<Integer> passedSet = new LinkedList<Integer>();      // 记录已经走过的中间点
	private int[] distance = null;               // 记录从源点到其他点的距离	
	private static int lowcost = 0;              // 记录最小权重
	private String goodPath = null;               // 记录最优解
	private int[] goodPathOfNodes = null;
	
	
	
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

    	/**********************test code********************************/
    	System.out.println("------------------------------");
//    	System.out.println(graphContent);
    	System.out.println(condition);
    	System.out.println("------------------------------");
    	/**************************************************************/
    	
    	
    	
    	
    	
    	// 初始化，生成邻接矩阵，返回节点数
    	nodes = route.initGraph(graphContent);
    	
    	// 处理条件字符串
    	route.handleCondition(condition);
    	
    	route.distance = new int[nodes];
    	route.path = new int[nodes];
    	route.s = new int[nodes];
    	
    	// path的初始化，s的初始化，由于Dijkstra递归的原因
    	// 在这里path和s只在初始化有用，因为递归不希望有这种全局变量的存在
    	for (int i = 0; i < nodes; i++)
    	{
    		route.path[i] = -1;
    		route.s[i] = 0;
    	}
    	
    	// 源点
    	route.path[route.source] = -2;
    	
    	// 寻边，但为了方便我们用 goodPath这个全局变量来记录最优解，
    	// Dijkstra函数返回并非最优解，而是递归最上层的解
    	// 因为EdgeBean实现了克隆接口
    	String edgetrace = null;
		try {
			edgetrace = route.Dijkstra(route.graphMatrix, route.s, route.path, source, destination, route.includingSet, route.passedSet);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
    	
		/**********************test code********************************/
    	System.out.println("cost: " + lowcost);
    	
    	String nodetrace = edgetrace;
    	
    	// nodetrace记录走过的边，和节点
    	nodetrace += "\n";
    	
    	if (route.goodPathOfNodes != null)
    	{
    		for (int i = 0; i < route.goodPathOfNodes.length; i++)
        		nodetrace += "" + route.goodPathOfNodes[i] + " ";
    	}
    	
    	
    	System.out.println("route: " + nodetrace);
    	/**************************************************************/
    	
    	
        return route.goodPath;
    }
    
    /** 
     * 初始化邻接矩阵
     * @param grapnContent read from files
     * @return nodes of graph
     */
    private int initGraph(String graphContent)
    {
    	String[] eachLine = graphContent.split("\n");
    	int rows = eachLine.length;
    	
    	int[][] temp = new int[4][rows];
    	int index = 0;
    	int max = 0;
    	
    	// 每一行的格式为 0,0,1,2（LinkID,SourceID,DestinationID,Cost）
    	// temp第 0 行存储编号，第 1 行存储源点，第 2 行存储汇点，第 3 行存储权重
    	for (int i = 0; i < rows; i++)
    	{
    		String[] value = eachLine[i].split(",");
    		temp[0][i] = Integer.parseInt(value[0]);
    		temp[1][i] = Integer.parseInt(value[1]);
    		temp[2][i] = Integer.parseInt(value[2]);
    		temp[3][i] = Integer.parseInt(value[3]);
    		
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
    	
    	replicaOfgraphMatrix = new EdgeBean[max+1][max+1];
    	for (int i = 0; i <= max; i++)
    	{
    		for (int j = 0; j <= max; j++)
    		{
    			replicaOfgraphMatrix[i][j] = new EdgeBean();
    		}
    	}
    	
    	// 为边赋值
    	for (int i = 0; i < temp[0].length; i++)
    	{    		 
    		int src = temp[1][i];
    		int des = temp[2][i];
    		
    		// 考虑到两点之间可能有多条边，但只存最短的边
    		if (graphMatrix[src][des].getWeight() > temp[3][i])
    		{
    			graphMatrix[src][des].setWeight(temp[3][i]);
        		graphMatrix[src][des].setNumber(temp[0][i]);
        		
        		replicaOfgraphMatrix[src][des].setWeight(temp[3][i]);
        		replicaOfgraphMatrix[src][des].setNumber(temp[0][i]);
    		}
    		
    	}
    	
    	return (max + 1);    	    	
    }    
    
    /**
     * 迪杰斯特拉变形，利用局部最优来找解
     * @param graph
     * @param n count of node
     * @param condition contain start node, end node, including set
     * @param s preserve node has visited
     * @param p p是一个数组，长度是节点个数，对应元素表示对应节点路径上的前驱节点
     * @throws CloneNotSupportedException 
     */
    private String Dijkstra(EdgeBean[][] graph, int[] s, int[] p, int src, int des, List<Integer> including, List<Integer> passed) 
    		throws CloneNotSupportedException
    {
    	// 用来深克隆
    	EdgeBean[][] graphMatrix = new EdgeBean[nodes][nodes];
    	List<Integer> includingList = new LinkedList<Integer>();
    	List<Integer> passedList = new LinkedList<Integer>();    	
    	int[] store = new int[nodes];
    	int[] path = new int[nodes];
    	
    	// hasVisited用于记录已经访问过的中间节点，避免递归返回时重复访问某一节点
    	List<Integer> hasVisited = new LinkedList<Integer>();
    	int[] distance = new int[nodes];
    	int begin = src;
    	int end = des;
    	
    	int minDis = INFINITY;
    	int u;
    	boolean tag = true;   
    	
    	
    	// 深克隆
    	for (int i = 0; i < nodes; i++)
    		for (int j = 0; j < nodes; j++)
    		{
    			graphMatrix[i][j] = (EdgeBean) graph[i][j].clone();
    		}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		store[i] = s[i];
    	}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		path[i] = p[i];
    	}
    	
    	for (int i = 0; i < including.size(); i++)
    		includingList.add(including.get(i));
    	
    	for (int i = 0; i < passed.size(); i++)
    		passedList.add(passed.get(i));
    	
    	u = -1;
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		distance[i] = graphMatrix[begin][i].getWeight();
    		
    		if (distance[i] < INFINITY)
    		{
    			path[i] = begin;
    		}
    	}
    	
    	store[begin] = 1;
    	
    	// 入度改为 0，防止环
    	for (int l = 0; l < nodes; l++)
    	{
    		graphMatrix[l][begin].setWeight(INFINITY);
    	}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		minDis = INFINITY;
    		u = -1;
    		
    		// 首先判断中间节点可不可达，可达则令 u 为中间节点
    		// 不可达选取不在 s 中且具有最小距离的顶点 u    		
     		for (int j = 0; j < includingList.size(); j++)
    		{
    			tag = true;
    			
    			if ( store[includingList.get(j)] == 0 && 
    					distance[includingList.get(j)] < minDis &&
    					!hasVisited.contains(includingList.get(j)) )
    			{
    				tag = false;
    				passedList.add(includingList.get(j));
    				includingList.remove(j);
    				
    				long endTime = System.currentTimeMillis();
    				if ( endTime - startTime >= 9000)
    				{
    					terminate = true;
    					break;
    				}
    				
    				Dijkstra(graphMatrix, store, path, passedList.get(passedList.size() - 1), des, includingList, passedList);
    				if (!passedList.isEmpty())
    				{
    					includingList.add(passedList.get(passedList.size() - 1));
        				hasVisited.add(passedList.get(passedList.size() - 1));
        				passedList.remove(passedList.get(passedList.size() - 1));
        				j = -1;
    				}    				
    			}
    		}
     		
     		if (terminate)
     		{
     			break;
     		}
    		
    		if (true)
    		{
    			for (int j = 0; j < nodes; j++)
        		{
    				// 未经过所有中间节点时不能先走汇点
        			if (includingList.size() > 0 && store[j] == 0 && 
        					distance[j] < minDis && j != end)
        			{
        				u = j;
        				minDis = distance[j];
        			}
        			else if (includingList.size() == 0 && store[j] == 0 && distance[j] < minDis)
        			{
        				u = j;
        				minDis = distance[j];
        			}
    				
    				
        		}    			
    		}    		
    		
    		if (u == -1)
    			continue;
    		
    		store[u] = 1;   		
    		
    		
    		// 对邻接矩阵进行修改，避免环
    		for (int i1 = 0; i1 < nodes; i1++)
    		{
    			graphMatrix[i1][u].setWeight(INFINITY);
    		}
    		
    		// 修改不在 store 中的顶点的距离
    		for (int j = 0; j < nodes; j++)
    		{
    			if (store[j] == 0)
    			{
    				if ( (graphMatrix[u][j].getWeight() < INFINITY) &&
    						(distance[u] + graphMatrix[u][j].getWeight() < distance[j]) )
    				{
    					distance[j] = distance[u] + graphMatrix[u][j].getWeight();
    					path[j] = u;
    				}
    			}
    		}
    		
    	}
    	
    	if (terminate && includingList.size() > 0 && goodPath == null)
    		return null;
    	
    	return getPath(distance, path, store, src);
    }
    
    private String getPath(int dis[], int path[], int s[], int src)
    {
    	int tempcost = 0;
    	
    	if (s[destination] == 0)
    		return null;
    	
    	result.clear();
    	Ppath(path, destination);
    	
    	
    	
    	String track = "";
    	
    	if (result.isEmpty())
    		return null;
    	
    	for (int i = 0; i < result.size() - 1; i++)
    	{
    		track += graphMatrix[result.get(i)][result.get(i + 1)].getNumber() + "|";
    		tempcost += replicaOfgraphMatrix[result.get(i)][result.get(i + 1)].getWeight();
    	}
    	tempcost += replicaOfgraphMatrix[result.get(result.size() - 1)][destination].getWeight();
    	track += graphMatrix[result.get(result.size() - 1)][destination].getNumber();
    	
    	if (lowcost == 0 || lowcost > tempcost)
    	{
    		goodPath = track;    		
    		lowcost = tempcost;
    		
    		goodPathOfNodes = new int[result.size() + 1];
    		
    		for (int i = 0; i < result.size(); i++)
    		{
    			goodPathOfNodes[i] = result.get(i);
    		}    		
    		goodPathOfNodes[result.size()] = destination;
    	}
//    	else if (lowcost != 0 && lowcost > tempcost)
//    	{
//    		goodPath = track;
//    		lowcost = tempcost;
//    	}
    	
    	return track;
    }
    
    // 递归寻找经过的点
    private void Ppath(int path[], int des)
    {
    	int k;
    	
    	k = path[des];
    	if (k == -2)
    	{
    		return ;
    	}
    	
    	Ppath(path, k);
    	
    	result.add(k);
    }
    
    private void handleCondition(String condition)
    {
    	// 处理 condition
    	String[] subStr = condition.split(","); 
    	String[] passVertex = subStr[2].split("\\|");
    	source = Integer.parseInt(subStr[0]);
    	destination = Integer.parseInt(subStr[1]);  
    	
    	for (int i = 0; i < passVertex.length; i++)
    	{    		
    		if (i == passVertex.length - 1)
    		{
    	    	// 因为passVertex中最后一个字符串的结束符是'\n'
    	    	includingSet.add(Integer.parseInt(passVertex[i].substring(0, passVertex[i].length() - 1)));
    		}
    		else
    		{
    			includingSet.add(Integer.parseInt(passVertex[i]));
    		}
    	}
    	
    	return ;
    }

}

// 边的信息
class EdgeBean implements Cloneable
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
	
	public Object clone() throws CloneNotSupportedException
	{
		EdgeBean obj = new EdgeBean();
		
		obj.setNumber(this.no);
		obj.setWeight(this.weight);
		
		return obj;		
	}
}