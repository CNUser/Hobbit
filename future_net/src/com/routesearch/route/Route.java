/**
 * ʵ�ִ����ļ�
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
	private final static int INFINITY = Integer.MAX_VALUE;          // �����
	private static int nodes = 0;                                     // �ڵ���
	private static int source = -1;                                   // ���
	private static int destination = -1;                              // ���
	private List<Integer> includingSet = new LinkedList<Integer>();   // �м�㼯��
	private EdgeBean[][] graphMatrix;                                 // �ڽӾ���
<<<<<<< HEAD
	private EdgeBean[][] replicaOfgraphMatrix;
	private List<Integer> result = new LinkedList<Integer>();
	private int[] path = null;                            // ��¼·���϶�Ӧ�ڵ��ǰ���ڵ�
	private int[] s = null;        // ��¼�Ѿ��߹��ĵ�, 1��ʾ�Ѿ��߹���0��ʾδ�߹�
	private List<Integer> passedSet = new LinkedList<Integer>();  // ��¼�Ѿ��߹����м��
	private int[] distance = null; // ��¼��Դ�㵽������ľ���	
	private static int lowcost = 0;
	private String goodPath = null;
=======
	private EdgeBean[][] replicaOfgraphMatrix;                        // �ڽӾ��󸱱�������Lowcost���õ�
	private List<Integer> result = new LinkedList<Integer>();         // ÿ��Ѱ��·��ʱ���澭���ĵ�
	private int[] path = null;                                        // ��¼·���϶�Ӧ�ڵ��ǰ���ڵ�
	private int[] s = null;                                           // ��¼�Ѿ��߹��ĵ�, 1��ʾ�Ѿ��߹���0��ʾδ�߹�
	private List<Integer> passedSet = new LinkedList<Integer>();      // ��¼�Ѿ��߹����м��
	private int[] distance = null;               // ��¼��Դ�㵽������ľ���	
	private static int lowcost = 0;              // ��¼��СȨ��
	private String goodPath = null;               // ��¼���Ž�
	private int[] goodPathOfNodes = null;
>>>>>>> origin/master
	
	
	
    /**
     * ����Ҫ��ɹ��ܵ����
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
    	
    	
    	
    	
    	
    	// ��ʼ���������ڽӾ��󣬷��ؽڵ���
    	nodes = route.initGraph(graphContent);
    	
    	// ���������ַ���
    	route.handleCondition(condition);
    	
    	route.distance = new int[nodes];
    	route.path = new int[nodes];
    	route.s = new int[nodes];
<<<<<<< HEAD
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		route.path[i] = -1;
    		route.s[i] = 0;
    	}
    	
    	route.path[route.source] = -2;
    	
    	String edgetrace = null;
		try {
			edgetrace = route.Dijkstra(route.graphMatrix, route.s, source, destination, route.includingSet, route.passedSet);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println(lowcost);
    	
    	String nodetrace = edgetrace;
    	
    	nodetrace += "\n";
    	for (Integer i : route.result)
    		nodetrace += "" + i + " ";
    	
    	System.out.println(route.goodPath);
    	
        return edgetrace;
=======
    	
    	// path�ĳ�ʼ����s�ĳ�ʼ��������Dijkstra�ݹ��ԭ��
    	// ������path��sֻ�ڳ�ʼ�����ã���Ϊ�ݹ鲻ϣ��������ȫ�ֱ����Ĵ���
    	for (int i = 0; i < nodes; i++)
    	{
    		route.path[i] = -1;
    		route.s[i] = 0;
    	}
    	
    	// Դ��
    	route.path[route.source] = -2;
    	
    	// Ѱ�ߣ���Ϊ�˷��������� goodPath���ȫ�ֱ�������¼���Ž⣬
    	// Dijkstra�������ز������Ž⣬���ǵݹ����ϲ�Ľ�
    	// ��ΪEdgeBeanʵ���˿�¡�ӿ�
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
    	
    	// nodetrace��¼�߹��ıߣ��ͽڵ�
    	nodetrace += "\n";
    	
    	if (route.goodPathOfNodes != null)
    	{
    		for (int i = 0; i < route.goodPathOfNodes.length; i++)
        		nodetrace += "" + route.goodPathOfNodes[i] + " ";
    	}
    	
    	
    	System.out.println("route: " + nodetrace);
    	/**************************************************************/
    	
    	
        return route.goodPath;
>>>>>>> origin/master
    }
    
    /** 
     * ��ʼ���ڽӾ���
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
    	
    	// ÿһ�еĸ�ʽΪ 0,0,1,2��LinkID,SourceID,DestinationID,Cost��
    	// temp�� 0 �д洢��ţ��� 1 �д洢Դ�㣬�� 2 �д洢��㣬�� 3 �д洢Ȩ��
    	for (int i = 0; i < rows; i++)
    	{
    		String[] value = eachLine[i].split(",");
    		temp[0][i] = Integer.parseInt(value[0]);
    		temp[1][i] = Integer.parseInt(value[1]);
    		temp[2][i] = Integer.parseInt(value[2]);
    		temp[3][i] = Integer.parseInt(value[3]);
    		
    		// ������ڵ���
    		max = Integer.max(max, temp[1][i]);
    	}
    	
    	// �����ʼ�����ߵ�Ȩ��Ϊ����󣬱��Ϊ-1
    	// ��Ϊ�ڵ��Ŵ�0��ʼ������ +1
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
    	
    	// Ϊ�߸�ֵ
    	for (int i = 0; i < temp[0].length; i++)
    	{    		 
    		int src = temp[1][i];
    		int des = temp[2][i];
    		
<<<<<<< HEAD
    		graphMatrix[src][des].setWeight(temp[3][i]);
    		graphMatrix[src][des].setNumber(temp[0][i]);
    		
    		replicaOfgraphMatrix[src][des].setWeight(temp[3][i]);
    		replicaOfgraphMatrix[src][des].setNumber(temp[0][i]);
    	}
    	
    	// test code
//    	for (int i = 0; i <= max; i++)
//    	{
//    		for (int j = 0; j <= max; j++)
//    		{
//    			System.out.print(graphMatrix[i][j].weight + "  ");
//    		}
//    		
//    		System.out.println();
//    	} 
=======
    		// ���ǵ�����֮������ж����ߣ���ֻ����̵ı�
    		if (graphMatrix[src][des].getWeight() > temp[3][i])
    		{
    			graphMatrix[src][des].setWeight(temp[3][i]);
        		graphMatrix[src][des].setNumber(temp[0][i]);
        		
        		replicaOfgraphMatrix[src][des].setWeight(temp[3][i]);
        		replicaOfgraphMatrix[src][des].setNumber(temp[0][i]);
    		}
    		
    	}
>>>>>>> origin/master
    	
    	return (max + 1);    	    	
    }    
    
    /**
     * �Ͻ�˹�������Σ����þֲ��������ҽ�
     * @param graph
     * @param n count of node
     * @param condition contain start node, end node, including set
<<<<<<< HEAD
     * @throws CloneNotSupportedException 
     */
    private String Dijkstra(EdgeBean[][] graph, int[] s, int src, int des, List<Integer> including, List<Integer> passed) 
    		throws CloneNotSupportedException
    {
    	
    	EdgeBean[][] graphMatrix = new EdgeBean[nodes][nodes];
    	List<Integer> includingList = new LinkedList<Integer>();
    	List<Integer> passedList = new LinkedList<Integer>();
    	int[] store = new int[nodes];
    	
=======
     * @param s preserve node has visited
     * @param p p��һ�����飬�����ǽڵ��������ӦԪ�ر�ʾ��Ӧ�ڵ�·���ϵ�ǰ���ڵ�
     * @throws CloneNotSupportedException 
     */
    private String Dijkstra(EdgeBean[][] graph, int[] s, int[] p, int src, int des, List<Integer> including, List<Integer> passed) 
    		throws CloneNotSupportedException
    {
    	// �������¡
    	EdgeBean[][] graphMatrix = new EdgeBean[nodes][nodes];
    	List<Integer> includingList = new LinkedList<Integer>();
    	List<Integer> passedList = new LinkedList<Integer>();    	
    	int[] store = new int[nodes];
    	int[] path = new int[nodes];
    	
    	// hasVisited���ڼ�¼�Ѿ����ʹ����м�ڵ㣬����ݹ鷵��ʱ�ظ�����ĳһ�ڵ�
    	List<Integer> hasVisited = new LinkedList<Integer>();
>>>>>>> origin/master
    	int[] distance = new int[nodes];
    	int begin = src;
    	int end = des;
    	
    	int minDis = INFINITY;
<<<<<<< HEAD
//    	int cntOfgoThrough = passedSet.size();
=======
>>>>>>> origin/master
    	int u;
    	boolean tag = true;   
    	
    	
    	// ���¡
    	for (int i = 0; i < nodes; i++)
    		for (int j = 0; j < nodes; j++)
    		{
    			graphMatrix[i][j] = (EdgeBean) graph[i][j].clone();
    		}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		store[i] = s[i];
    	}
    	
<<<<<<< HEAD
=======
    	for (int i = 0; i < nodes; i++)
    	{
    		path[i] = p[i];
    	}
    	
>>>>>>> origin/master
    	for (int i = 0; i < including.size(); i++)
    		includingList.add(including.get(i));
    	
    	for (int i = 0; i < passed.size(); i++)
    		passedList.add(passed.get(i));
    	
    	u = -1;
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		distance[i] = graphMatrix[begin][i].getWeight();
<<<<<<< HEAD
//    		s[i] = 0;
=======
>>>>>>> origin/master
    		
    		if (distance[i] < INFINITY)
    		{
    			path[i] = begin;
    		}
<<<<<<< HEAD
//    		else if (path[i] != -1)
    //		{
    //			path[i] = -1;
   // 		}
    	}
    	
    	store[begin] = 1;
//    	path[begin] = -2;
    	
    	// ��ȸ�Ϊ 0
=======
    	}
    	
    	store[begin] = 1;
    	
    	// ��ȸ�Ϊ 0����ֹ��
>>>>>>> origin/master
    	for (int l = 0; l < nodes; l++)
    	{
    		graphMatrix[l][begin].setWeight(INFINITY);
    	}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		minDis = INFINITY;
    		u = -1;
    		
    		// �����ж��м�ڵ�ɲ��ɴ�ɴ����� u Ϊ�м�ڵ�
<<<<<<< HEAD
    		// ���ɴ�ѡȡ���� s ���Ҿ�����С����Ķ��� u
    		
     		for (int j = 0; j < includingList.size(); j++)
    		{
    			tag = true;
    			if (store[includingList.get(j)] == 0 && 
    					distance[includingList.get(j)] < minDis)
    			{
//    				u = includingList.get(j);
//    				minDis = distance[includingList.get(j)];
    				tag = false;
    				passedList.add(includingList.get(j));
    				includingList.remove(j);
    				Dijkstra(graphMatrix, store, passedList.get(passedList.size() - 1), des, includingList, passedList);
=======
    		// ���ɴ�ѡȡ���� s ���Ҿ�����С����Ķ��� u    		
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
>>>>>>> origin/master
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
    				// δ���������м�ڵ�ʱ�������߻��
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
    		
<<<<<<< HEAD
//    		if (!result.isEmpty())
//    			break;
    		if (u == -1)
    			continue;
    		// ���� u ���� s ��
    		store[u] = 1;   		
    		
=======
    		if (u == -1)
    			continue;
    		
    		store[u] = 1;   		
>>>>>>> origin/master
    		
    		
    		// ���ڽӾ�������޸ģ����⻷
    		for (int i1 = 0; i1 < nodes; i1++)
    		{
    			graphMatrix[i1][u].setWeight(INFINITY);
    		}
    		
    		// �޸Ĳ��� store �еĶ���ľ���
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
    		
<<<<<<< HEAD
//    		if (u == end)
//    		{
//    			break;
//    		}
    		
    	}
    	
=======
    	}
    	
    	if (terminate && includingList.size() > 0 && goodPath == null)
    		return null;
    	
>>>>>>> origin/master
    	return getPath(distance, path, store, src);
    }
    
    private String getPath(int dis[], int path[], int s[], int src)
    {
    	int tempcost = 0;
    	
    	if (s[destination] == 0)
    		return null;
    	
    	result.clear();
    	Ppath(path, destination);
<<<<<<< HEAD
    	
    	
    	
    	String track = "";
=======
>>>>>>> origin/master
    	
    	
<<<<<<< HEAD
//    	track = "" + graphMatrix[src][result.get(0)].getNumber() + "|";
=======
    	
    	String track = "";
    	
    	if (result.isEmpty())
    		return null;
>>>>>>> origin/master
    	
    	for (int i = 0; i < result.size() - 1; i++)
    	{
    		track += graphMatrix[result.get(i)][result.get(i + 1)].getNumber() + "|";
    		tempcost += replicaOfgraphMatrix[result.get(i)][result.get(i + 1)].getWeight();
    	}
    	tempcost += replicaOfgraphMatrix[result.get(result.size() - 1)][destination].getWeight();
    	track += graphMatrix[result.get(result.size() - 1)][destination].getNumber();
    	
<<<<<<< HEAD
    	if (lowcost == 0)
    	{
    		goodPath = track;
    		lowcost = tempcost;
    	}
    	else if (lowcost != 0 && lowcost > tempcost)
    	{
    		goodPath = track;
    		lowcost = tempcost;
    	}
=======
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
>>>>>>> origin/master
    	
    	return track;
    }
    
<<<<<<< HEAD
=======
    // �ݹ�Ѱ�Ҿ����ĵ�
>>>>>>> origin/master
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
    	// ���� condition
    	String[] subStr = condition.split(","); 
    	String[] passVertex = subStr[2].split("\\|");
    	source = Integer.parseInt(subStr[0]);
    	destination = Integer.parseInt(subStr[1]);  
    	
    	for (int i = 0; i < passVertex.length; i++)
    	{    		
    		if (i == passVertex.length - 1)
    		{
    	    	// ��ΪpassVertex�����һ���ַ����Ľ�������'\n'
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

<<<<<<< HEAD
=======
// �ߵ���Ϣ
>>>>>>> origin/master
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