//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files: GraphADT.java 
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
import java.util.List;
import java.util.Set;

/**
 * Filename:   GraphADT.java
 * Project:    Social Network
 *
 * 
 * A simple graph interface 
 */
public interface GraphADT {

	/**
	 * Add new Node of given Person to the graph
	 *
	 * If person is null or already exists, method ends without adding a vertex or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. person is non-null 2. person is not already in
	 * the graph
	 * @param name the Person to be added
	 * @return true if add user successfully, false otherwise
	 */
    public boolean addNode(Person name);

    
    /**
	 * Remove the Node associated with given Person from the graph 
	 * and all associated edges from the hash map.
	 * 
	 * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     * @param name the Person to be removed
	 * @return true if remove user successfully, false otherwise
	 */
    public boolean removeNode(Person name);

    
    /**
	 * Add the edge from name1 to name2
     * to this graph.  
     * 
     * If either Person does not exist,
     * PERSON VERTEX IS ADDED and then edge is created.
     * No exception is thrown.
     *
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
	 *Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
     * 
     * @param name1 the first Person to add Edge
     * @param name2 the second Person to add Edge
     * @return true if add a edge between these two people successfully,
	 *         false other wise. If the edge already existed, return false;
	 */
    public boolean addEdge(Person name1, Person name2);

    
    /**
	 * Remove the edge from name1 to name2
     * from this graph.  (edge is undirected and unweighted)
     * If either vertex does not exist,
     * or if an edge from name1 to name2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither Person vertex is null
     * 2. both Person vertices are in the graph 
     * 3. the edge from name1 to name2 is in the graph
     *  
     * @param name1 the first Person to remove Edge
     * @param name2 the second Person to remove Edge
     * @return true if remove a edge between these two people successfully,
	 *         false other wise. If both people exist, but the edge not
	 *         found, throw false;
	 */
    public boolean removeEdge(Person name1, Person name2);
    
        
    /**
	  * Get all Person nodes in the Graph
	  * 
	  * @return the Set<Person> of all Person nodes in the Graph
	  */
    public Set<Person> getAllNodes();
    
    /**
	  * Given the name of the Person, return the corresponding Person node
	  *
	  * @param name the name of the Person
	  * @return the Person Node of the corresponding name
	  */
    public Person getNode(String name);

    
    /**
	  * Get all neighbors of the specified person
	  * 
	  * @param name the target Person that we wish to find all neighbors
	  * @return the Set<Person> of all neighbors of the specified Person
	  */
    public Set<Person> getNeighbors(Person name);
    
    
}
