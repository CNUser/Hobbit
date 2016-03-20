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

public final class Route
{
	private final static int INFINITY = Integer.MAX_VALUE;         // �����
	private static int nodes = 0;                                    // �ڵ���
	private static int source = -1;                                  // ���
	private static int destination = -1;                             // ���
	private List<Integer> includingSet = new LinkedList<Integer>();  // �м�㼯��
	private EdgeBean[][] graphMatrix;                                 // �ڽӾ���
	private EdgeBean[][] replicaOfgraphMatrix;
	private List<Integer> result = new LinkedList<Integer>();
	private int[] path = null;                            // ��¼·���϶�Ӧ�ڵ��ǰ���ڵ�
	private int[] s = null;        // ��¼�Ѿ��߹��ĵ�, 1��ʾ�Ѿ��߹���0��ʾδ�߹�
	private List<Integer> passedSet = new LinkedList<Integer>();  // ��¼�Ѿ��߹����м��
	private int[] distance = null; // ��¼��Դ�㵽������ľ���	
	private static int lowcost = 0;
	private String goodPath = null;
	
	
	
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
    	
    	// test code
    	System.out.println("------------------------------");
    	System.out.println(graphContent);
    	System.out.println(condition);
    	System.out.println("------------------------------");
    	
    	nodes = route.initGraph(graphContent);
    	
    	route.handleCondition(condition);
    	
    	route.distance = new int[nodes];
    	route.path = new int[nodes];
    	route.s = new int[nodes];
    	
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
    	
    	return (max + 1);    	    	
    }    
    
    /**
     * @param graph
     * @param n count of node
     * @param condition contain start node, end node, including set
     * @throws CloneNotSupportedException 
     */
    private String Dijkstra(EdgeBean[][] graph, int[] s, int src, int des, List<Integer> including, List<Integer> passed) 
    		throws CloneNotSupportedException
    {
    	
    	EdgeBean[][] graphMatrix = new EdgeBean[nodes][nodes];
    	List<Integer> includingList = new LinkedList<Integer>();
    	List<Integer> passedList = new LinkedList<Integer>();
    	int[] store = new int[nodes];
    	
    	int[] distance = new int[nodes];
    	int begin = src;
    	int end = des;
    	
    	int minDis = INFINITY;
//    	int cntOfgoThrough = passedSet.size();
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
    	
    	for (int i = 0; i < including.size(); i++)
    		includingList.add(including.get(i));
    	
    	for (int i = 0; i < passed.size(); i++)
    		passedList.add(passed.get(i));
    	
    	u = -1;
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		distance[i] = graphMatrix[begin][i].getWeight();
//    		s[i] = 0;
    		
    		if (distance[i] < INFINITY)
    		{
    			path[i] = begin;
    		}
//    		else if (path[i] != -1)
    //		{
    //			path[i] = -1;
   // 		}
    	}
    	
    	store[begin] = 1;
//    	path[begin] = -2;
    	
    	// ��ȸ�Ϊ 0
    	for (int l = 0; l < nodes; l++)
    	{
    		graphMatrix[l][begin].setWeight(INFINITY);
    	}
    	
    	for (int i = 0; i < nodes; i++)
    	{
    		minDis = INFINITY;
    		u = -1;
    		
    		// �����ж��м�ڵ�ɲ��ɴ�ɴ����� u Ϊ�м�ڵ�
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
    			}
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
    		
//    		if (!result.isEmpty())
//    			break;
    		if (u == -1)
    			continue;
    		// ���� u ���� s ��
    		store[u] = 1;   		
    		
    		
    		// ���ڽӾ�������޸�
    		for (int i1 = 0; i1 < nodes; i1++)
    		{
    			graphMatrix[i1][u].setWeight(INFINITY);
    		}
    		
    		// �޸Ĳ��� s �еĶ���ľ���
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
    		
//    		if (u == end)
//    		{
//    			break;
//    		}
    		
    	}
    	
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
    	
//    	track = "" + graphMatrix[src][result.get(0)].getNumber() + "|";
    	
    	for (int i = 0; i < result.size() - 1; i++)
    	{
    		track += graphMatrix[result.get(i)][result.get(i + 1)].getNumber() + "|";
    		tempcost += replicaOfgraphMatrix[result.get(i)][result.get(i + 1)].getWeight();
    	}
    	tempcost += replicaOfgraphMatrix[result.get(result.size() - 1)][destination].getWeight();
    	track += graphMatrix[result.get(result.size() - 1)][destination].getNumber();
    	
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
    	
    	return track;
    }
    
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
//    	includingSet = new int[passVertex.length];
    	
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