//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files: Graph.java 
// Course: CS 400, Fall 2019
//
// Team Members:
// 1. Boya Zeng, lecture2, and bzeng7@wisc.edu
// 2. Yuxin He, lecture1, and yhe242@wisc.edu
// 3. Zeyu Tan, lecture1, and ztan56@wisc.edu
// 4. Samuel Weng, lecture1, and sweng23@wisc.edu
// 5. Shouzhe Li, lecture1, and sli649@wisc.edu
// Lecturer's Name: Debra Deppeler
//
// Program description: Create interactive social network with a graphic 
// user interface (GUI) and network data structure for the backend.   
// Must be able to construct from information read from a file and also write 
// current network to a file (as a series of connections (edges).
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Graph class that make every person and each friends stored in graph data
 * structure
 *
 */
public class Graph implements GraphADT {
	private HashMap<Person, List<Person>> adjacencyList;

	/**
	 * default constructor
	 */
	public Graph() {
		adjacencyList = new HashMap<Person, List<Person>>();
	}

	/**
	 * Add new Node of given Person to the graph
	 *
	 * If person is null or already exists, method ends without adding a vertex or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. person is non-null 2. person is not already in
	 * the graph
	 * 
	 * @param name the Person to be added
	 * @return true if add user successfully, false otherwise
	 */
	@Override
	public boolean addNode(Person name) {
		if (name == null)
			return false;

		if (adjacencyList.containsKey(name))
			return false;

		adjacencyList.put(name, new LinkedList<Person>());
		return true;
	}

	/**
	 * Remove the Node associated with given Person from the graph and all
	 * associated edges from the hash map.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 * 
	 * @param name the Person to be removed
	 * @return true if remove user successfully, false otherwise
	 */
	@Override
	public boolean removeNode(Person name) {
		if (name == null)
			return false;

		List<Person> allNodes = new ArrayList<Person>(getAllNodes());

		// check validity
		if (!adjacencyList.containsKey(name))
			return false;

		// remove all the relationships
		for (int i = 0; i < allNodes.size(); i++) {
			removeEdge(name, allNodes.get(i));
		}

		// remove the node
		adjacencyList.remove(name);
		return true;
	}

	/**
	 * Add the edge from name1 to name2 to this graph.
	 * 
	 * If either Person does not exist, PERSON VERTEX IS ADDED and then edge is
	 * created. No exception is thrown.
	 *
	 * If the edge exists in the graph, no edge is added and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge is not in the graph
	 * 
	 * @param name1 the first Person to add Edge
	 * @param name2 the second Person to add Edge
	 * @return true if add a edge between these two people successfully, false other
	 *         wise. If the edge already existed, return false;
	 */
	@Override
	public boolean addEdge(Person person1, Person person2) {
		// If vertexes are null
		if (person1 == null || person2 == null)
			return false;

		// test if two Person are in the graph, 4 cases possible
		if (adjacencyList.containsKey(person1)) {
			// if person1 in graph

			if (adjacencyList.containsKey(person2)) {
				// case 1: person1 in, person2 in

				// if the friendship doesn't exist
				if (!adjacencyList.get(person1).contains(person2)) {
					adjacencyList.get(person1).add(person2);
					adjacencyList.get(person2).add(person1);

				} else {
					return false;
				}
				return true;
			} else {

				// case 2: person1 in; person 2 not in
				addNode(person2);
				adjacencyList.get(person1).add(person2);
				adjacencyList.get(person2).add(person1);
				return true;
			}
		} else {
			// if p1 is not in the graph

			if (adjacencyList.containsKey(person2)) {

				// case 3: if person1 not in, person2 in
				addNode(person1);
				adjacencyList.get(person1).add(person2);
				adjacencyList.get(person2).add(person1);
				return true;
			} else { // case 4: both not in
				addNode(person1);
				addNode(person2);
				adjacencyList.get(person1).add(person2);
				adjacencyList.get(person2).add(person1);
				return true;
			}

		}
	}

	/**
	 * Remove the edge from name1 to name2 from this graph. (edge is undirected and
	 * unweighted) If either vertex does not exist, or if an edge from name1 to
	 * name2 does not exist, no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither Person vertex is null 2. both Person
	 * vertices are in the graph 3. the edge from name1 to name2 is in the graph
	 * 
	 * @param name1 the first Person to remove Edge
	 * @param name2 the second Person to remove Edge
	 * @return true if remove a edge between these two people successfully, false
	 *         other wise. If both people exist, but the edge not found, throw
	 *         false;
	 */
	@Override
	public boolean removeEdge(Person name1, Person name2) {

		// If vertex is null
		if (name1 == null || name2 == null)
			return false;
		// If vertex does not exist,
		if (!adjacencyList.containsKey(name1) || !adjacencyList.containsKey(name2))
			return false;

		List<Person> friend1 = adjacencyList.get(name1);
		List<Person> friend2 = adjacencyList.get(name2);

		// if edge does not exit
		if (!friend1.contains(name2) && !friend2.contains(name1))
			return false;

		friend1.remove(name2);
		adjacencyList.replace(name1, friend1);
		friend2.remove(name1);
		adjacencyList.replace(name2, friend2);

		return true;

	}

	/**
	 * Get all Person nodes in the Graph
	 * 
	 * @return the Set<Person> of all Person nodes in the Graph
	 */
	@Override
	public Set<Person> getAllNodes() {
		return adjacencyList.keySet();
	}

	/**
	 * Given the name of the Person, return the corresponding Person node
	 *
	 * @param name the name of the Person
	 * @return the Person Node of the corresponding name
	 */
	@Override
	public Person getNode(String name) {
		for (Person person : getAllNodes()) {
			if (person.getName().equals(name)) {
				return person;
			}
		}
		return null;
	}

	/**
	 * Get all neighbors of the specified person
	 * 
	 * @param name the target Person that we wish to find all neighbors
	 * @return the Set<Person> of all neighbors of the specified Person
	 */
	@Override
	public Set<Person> getNeighbors(Person name) {
		List<Person> neighbor = adjacencyList.get(name);
		Set<Person> nblist = new HashSet<Person>(neighbor);
		return nblist;
	}

}