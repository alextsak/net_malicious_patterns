package com.packetMem.shared;

import java.io.IOException;
import java.util.ArrayList;

public class ActiveNodes {

	ArrayList<NodeData> Nodes = null;
	private static ActiveNodes ActiveNodesinstance = null;
	private Object Nodeslock;
	
	private ActiveNodes(){
		Nodeslock = new Object();
	}
	
	public static ActiveNodes getInstance(){
		if(ActiveNodesinstance == null){
			ActiveNodesinstance = new ActiveNodes();
		}
		return ActiveNodesinstance;
	}
	
	public void CreateNodesMemory() throws IOException{
		Nodes = new ArrayList<NodeData>();
		System.out.printf("Creating Active Nodes Memory Done.\n");
	}
	
	public ArrayList<NodeData> getNodes() {
		return Nodes;
	}

	public void setNodes(ArrayList<NodeData> nodes) {
		synchronized (Nodeslock){
			Nodes = nodes;
		}
	}
	
	public int searchActiveNodes(String nodeID) { //if nodeID is found returns node's position, else -1
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					return Nodes.indexOf(temp);
				}
			}
		}
		return -1;
	}
	
	public void insertActiveNode(String nodeID){ //Inserts node with nodeID in Nodes
		synchronized (Nodeslock){
			NodeData temp = new NodeData();
			temp.setUID(nodeID);
			temp.setIPpos(-1);
			temp.setPatternpos(-1);
			Nodes.add(temp);
		}
	}
	
	public void removeNode(String nodeID){ //Removes ,if found, the node with nodeID from Nodes
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					Nodes.remove(temp);
					return;
				}
			}
		}
	}
	
	public void updateActiveNode(String nodeID, int ippos, int patpos){ //Updates position of patterns and IPs that have been sent to client with nodeID
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					temp.setIPpos(ippos);
					temp.setPatternpos(patpos);
					return;
				}
			}
		}
	}
	
	public int getNodeDataIPpos(String nodeID){
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					return temp.getIPpos();
				}
			}
		}
		return -1;
	}
	
	public int getNodeDataPatternpos(String nodeID){
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					return temp.getPatternpos();
				}
			}
		}
		return -1;
	}
	
	public void setNodeDataIPpos(String nodeID, int pos){
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					temp.setIPpos(pos);
					return;
				}
			}
		}
	}
	
	public void setNodeDataPatternpos(String nodeID, int pos){
		synchronized (Nodeslock){
			for(NodeData temp : Nodes){
				if (temp.getUID().equals(nodeID)){
					temp.setPatternpos(pos);
					return;
				}
			}
		}
	}
	
	public String getNodeDataUID(int index){
		return Nodes.get(index).getUID();
	}

	class NodeData {
		String UID = null;
		int IPpos = -1;
		int Patternpos = -1;
		
		public String getUID() {
			return UID;
		}
		public void setUID(String uID) {
			UID = uID;
		}
		public int getIPpos() {
			return IPpos;
		}
		public void setIPpos(int iPpos) {
			IPpos = iPpos;
		}
		public int getPatternpos() {
			return Patternpos;
		}
		public void setPatternpos(int patternpos) {
			Patternpos = patternpos;
		}
	}
	
}

