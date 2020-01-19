//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files: SocialNetwork.java 
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * Defines methods required for social network visualization
 */
public class SocialNetwork implements SocialNetworkADT {

	/**
	 * Inner class that represent a person in the social network
	 *
	 */
	private class Node {
		private Person name;
		private List<Person> pre;
		private boolean visit = false;

		/**
		 * Constructor with the given person name
		 * 
		 * @param name name of the Person
		 */
		private Node(Person name) {
			pre = new ArrayList<Person>();
			visit = false;
			this.name = name;
		}
	}

	private String fileName;
	private Graph graph;
	private String centerUser;
	private File log;
	private String logContent;
	public List<Node> nodeC;
	public String errorPart = "";

	/**
	 * Constructor of SocialNetwork with load file
	 * 
	 * @param fileName the name of the loaded file
	 * @throws SetException               when trying to set a center user in the
	 *                                    last line
	 * @throws PersonInvalidNameException when person name has invalid element
	 */
	public SocialNetwork(String fileName) throws SetException, PersonInvalidNameException {
		fileName = this.fileName;
		graph = new Graph();
		File name = new File(fileName);
		try {
			if (!name.exists()) {
				name.createNewFile();
			}

			loadFromFile(name);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of SocialNetwork
	 */
	public SocialNetwork() {
		graph = new Graph();
		centerUser = null;
		log = new File("log.txt");
		if (!log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Add the relationship between two people with given names
	 * 
	 * @param user   the name of the user that a friend would be added
	 * @param friend the name of the friends that would be added to this people
	 * @return true if add a relationship between these two people successfully,
	 *         false other wise. If the relationship already existed, return false;
	 */
	@Override
	public boolean addFriends(String person1, String person2) {
		// check Strings
		if (person1 == null || person2 == null)
			throw new IllegalArgumentException();

		// get the specific person
		Person p1 = graph.getNode(person1);// p1 can be null or Person
		Person p2 = graph.getNode(person2);

		if (p1 != null) {

			// case 1: p1 not null | p2 not null
			if (p2 != null) {
				return graph.addEdge(p1, p2);
			}

			// case 2: p1 not null | p2 null
			if (p2 == null) {
				p2 = new Person(person2);
				return graph.addEdge(p1, p2);
			}
		} else {

			// case 3: p1 null | p2 nut null
			if (p2 != null) {
				p1 = new Person(person1);
				return graph.addEdge(p1, p2);
			}

			// case 4: both null
			if (p2 == null)
				return graph.addEdge(new Person(person1), new Person(person2));
			// false;
		}
		return false;
	}

	/**
	 * Remove the relationship between two people with given names
	 * 
	 * @param user   the name of the user that a friend would be removed
	 * @param friend the name of the friends that would be removed from this people
	 * @return true if remove a relationship between these two people successfully,
	 *         false other wise. If both people exist, but the relationship not
	 *         found, throw false;
	 * @throws IllegalArgumentException if try to remove a friendship with null
	 *                                  people included
	 */
	@Override
	public boolean removeFriends(String person1, String person2) {
		// check Strings
		if (person1 == null || person2 == null)
			throw new IllegalArgumentException();

		// get all the Person
		Person p1 = graph.getNode(person1);
		Person p2 = graph.getNode(person2);

		// 4 cases for removing
		if (p1 != null) {

			// case 1: p1 not null | p2 not null
			if (p2 != null) {
				return graph.removeEdge(p1, p2);
			}

			// case 2: p1 not null | p2 null
			if (p2 == null) {
				return false;
			}
		} else {

			// case 3: p1 null | p2 nut null
			if (p2 != null) {
				return false;
			}

			// case 4: both null
			if (p2 == null)
				return false;
		}
		return false;
	}

	/**
	 * Add new user to the graph with the given name.
	 * 
	 * @param user the name of the user to be added
	 * @return true if add user successfully, false otherwise
	 * @throws IllegalArgumentException if the user is null
	 */
	@Override
	public boolean addUser(String person) {
		// check String
		if (person == null)
			throw new IllegalArgumentException();
		// check valid
		if (!this.isValidName(person)) {
			return false;
		}
		// get the node
		Person p = graph.getNode(person);

		// check if dup happens
		if (p == null) {
			// if no dup, add
			p = new Person(person);
			return graph.addNode(p);
		} else {
			// if dup, false
			return graph.addNode(p);
		}

	}

	/**
	 * Remove new user to the graph with the given name.
	 * 
	 * @param user the name of the user to be removed
	 * @return true if remove user successfully, false otherwise
	 * @throws IllegalArgumentException if the user is null
	 */
	@Override
	public boolean removeUser(String person) {
		// check String
		if (person == null)
			throw new IllegalArgumentException();

		// get node
		Person p = graph.getNode(person);

		// check if person exists
		if (p == null) {
			return false;
		} else {
			return graph.removeNode(p);
		}
	}

	/**
	 * Given the user, get all friends set of this user
	 * 
	 * @param user the name of the target user whom we want to find all friends
	 * @return the set of friends of this user
	 * @throws IllegalArgumentExceptio if the user is null
	 * @throws PersonNotFoundException if user is not exist in the social network
	 */
	@Override
	public Set<Person> getFriends(String user) throws PersonNotFoundException {
		// handle null
		if (user == null)
			throw new IllegalArgumentException("user is null");

		// handle person not found
		if (graph.getNode(user) == null)
			throw new PersonNotFoundException();

		return graph.getNeighbors(graph.getNode(user));
	}

	/**
	 * Given two users, get all mutual friends of two users
	 * 
	 * @param user1 the name of the target user1 whom we want to find mutual friends
	 *              between
	 * @param user2 the name of the target user2 whom we want to find mutual friends
	 *              between
	 * @return the set of mutual friends of two users
	 * @throws IllegalArgumentExceptio if the user is null
	 * @throws PersonNotFoundException if user is not exist in the social network
	 */
	@Override
	public Set<Person> getMutualFriends(String user1, String user2) throws PersonNotFoundException {
		// handle null
		if (user1 == null || user2 == null)
			throw new IllegalArgumentException("user name is not legal");

		// handle personNotFound
		if (graph.getNode(user1) == null || graph.getNode(user2) == null)
			throw new PersonNotFoundException();

		Set<Person> user1Friends = graph.getNeighbors(graph.getNode(user1));
		Set<Person> user2Friends = graph.getNeighbors(graph.getNode(user2));

		// get intersection
		user1Friends.retainAll(user2Friends);

		return user1Friends;
	}

	/**
	 * Find the sequence of friends that is the shortest path between given two
	 * users within a connected component
	 * 
	 * @param user1 the name of the target user1 to get shortest path between two
	 *              person
	 * @param user2 the name of the target user2 to get shortest path between two
	 *              person
	 * @return shortest friends set path between of two users
	 * @throws IllegalArgumentExceptio if the user is null
	 * @throws PersonNotFoundException if user is not exist in the social network
	 */
	@Override
	public List<Person> getShortestPath(String person1, String person2) throws PersonNotFoundException {
		// handle null
		if (person1 == null || person2 == null) {
			throw new IllegalArgumentException("user is null");
		}

		Queue<Node> queue = new LinkedList<Node>();
		Person p1 = graph.getNode(person1);
		Person p2 = graph.getNode(person2);

		// handle person not found
		if (p1 == null || p2 == null)
			throw new PersonNotFoundException();

		// initiate the nodes(preList and visited)
		ini();
		printC();

		// add the first node to the queue
		queue.add(pN(p1));

		printQ(queue);

		// loop to get the result
		while (!queue.isEmpty()) {
			// remove the min Node
			queue = firstMin(queue);
			Node previous = queue.remove();
			previous.visit = true;
			printQ(queue);
			List<Person> friends = new ArrayList<Person>(getFriends(previous.name.getName()));

			// get the path of the previous node
			List<Person> prePath = new ArrayList<Person>(previous.pre);
			prePath.add(previous.name);

			for (int i = 0; i < friends.size(); i++) {
				Node tempN = new Node(friends.get(i));
				Node existNode = pN(friends.get(i));
				tempN.pre = prePath;

				System.out.println("temp node:  ");
				printNode(tempN);
				System.out.print("\n");

				System.out.println("existing node:  ");
				printNode(existNode);
				System.out.print("\n");

				// if the node is not visited, change the pre list if there is a shorter path
				if (!existNode.visit) {
					// special case if the node show up the first time, change pre list manually
					if (existNode.pre.size() == 0) {
						existNode.pre = new ArrayList<Person>();
						for (int j = 0; j < tempN.pre.size(); j++) {
							existNode.pre.add(tempN.pre.get(j));
						}
						queue.add(existNode);
						continue;
					}

					// compare to see if the length is reduced
					if (tempN.pre.size() < existNode.pre.size()) {
						existNode.pre = new ArrayList<Person>();
						for (int j = 0; j < tempN.pre.size(); j++) {
							existNode.pre.add(tempN.pre.get(j));
						}
						queue.add(existNode);
					} else {
						queue.add(existNode);
					}
				}
			}
			printC();

		}

		// if no connection, directly return
		if (pN(p2).pre.size() == 0) {
			return pN(p2).pre;
		}

		// return a list that includes person2
		List<Person> a = pN(p2).pre;
		a.add(p2);
		return a;

	}

	/**
	 * To get individual Connected Components of the Social Network as a set
	 * 
	 * @throws PersonNotFoundException when trying to get friends from a person who
	 *                                 does not exist
	 * @return the set of disconnected graph created by Social Network
	 */
	@Override
	public Set<Graph> getConnectedComponents() throws PersonNotFoundException {
		// get all the person in the graph first
		List<Person> allPerson = new ArrayList<Person>(graph.getAllNodes());
		ini();
		List<List<Person>> group = new ArrayList<List<Person>>();
		Set<Graph> assemble = new HashSet<Graph>();
		Queue<Node> queue = new LinkedList<Node>();

		// if no perosn in the graph, return empty set
		if (allPerson.size() == 0)
			return assemble;

		while (!allPerson.isEmpty()) {
			Set<Person> connectedPerson = new HashSet<Person>();

			// set a start person for the loop
			queue.add(pN(allPerson.get(0)));
			connectedPerson.add(pN(allPerson.get(0)).name);

			// start looping!
			while (!queue.isEmpty()) {
				Node current = queue.remove();
				current.visit = true;
				List<Person> currentFriends = new ArrayList<Person>(getFriends(current.name.getName()));
				for (int i = 0; i < currentFriends.size(); i++) {
					Node temp = pN(currentFriends.get(i));
					if (!temp.visit) {
						queue.add(temp);
						connectedPerson.add(temp.name);
					}
				}
			}

			// add the teams to a larger group so that graphs can be created later
			List<Person> temp = new ArrayList<Person>(connectedPerson);
			group.add(temp);

			// remove so that the outer while loop will end
			List<Person> removeList = new ArrayList<Person>(connectedPerson);// remove the nodes in one graph
			allPerson.removeAll(removeList);
		}

		for (int i = 0; i < group.size(); i++) {
			assemble.add(painter(group.get(i)));
		}

		return assemble;

	}

	/**
	 * The private helper method that detects if the Person name has the correct
	 * format
	 * 
	 * @param name the name of the Person
	 * @return true if in correct format, false otherwise
	 */
	private boolean isValidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (Character.isLetterOrDigit(name.charAt(i)) || name.charAt(i) == '_' || name.charAt(i) == '\'') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Parse the file fileName into the graph
	 * 
	 * @param fileName The file to get initial action from
	 * @throws FileNotFoundException      throws exceptions when parsed file do not
	 *                                    exits
	 * @throws IOException                if the give file cannot be read
	 * @throws ParseException             if the given file cannot be parsed
	 * @throws PersonInvalidNameException if the person name contains invalid
	 *                                    element
	 */
	@Override
	public void loadFromFile(File fileName) throws IOException, ParseException, PersonInvalidNameException {
		// if file do not exist, throw filenotfound exception
		if (!fileName.exists())
			throw new FileNotFoundException();

		// if file cannot read throw IOException
		if (!fileName.canRead())
			throw new IOException();

		// use scanner to read file
		Scanner sc = null;
		String[] instr;

		// read the file

		sc = new Scanner(fileName);
		// log into log.txt in right form
		StringBuilder fileContents = new StringBuilder((int) fileName.length());
		// Check if there is another line of input
		while (sc.hasNextLine()) {
			String str = sc.nextLine().trim();
			instr = str.split(" ");
			// handle instr to complete instruction
			// if str is null
			if (instr == null || instr.length == 0) {
				System.out.println("instr is null");
				throw new ParseException(str, 0);

			}
			// start handling the different instructions
			switch (instr[0]) {
			// add
			case "a":
				// if a bbb
				if (instr.length == 2)
					if (isValidName(instr[1])) {
						if (this.addUser(instr[1])) {
							fileContents.append(str + System.lineSeparator());
						}
						else {
							errorPart = errorPart + str + "\n";
						}
					} else {
						errorPart = errorPart + str + "\n";
					}
				// if a bbb bbb
				else if (instr.length == 3)
					if (isValidName(instr[1]) && isValidName(instr[2])) {
						if (this.addFriends(instr[1], instr[2]))
							fileContents.append(str + System.lineSeparator());
						else errorPart = errorPart + str + "\n";
					} else {
						errorPart = errorPart + str + "\n";
					}
				// else throw exception
				else {
					System.out.println("a sb");
					throw new ParseException(str, 0);
				}
				break;
			// remove
			case "r":
				// if r bbb
				if (instr.length == 2) {
					if (this.removeUser(instr[1]))
						fileContents.append(str + System.lineSeparator());
					else {
						errorPart = errorPart + str + "\n";
					}
					
					// if r bbb bbb
				}else if (instr.length == 3) {
						if (removeFriends(instr[1], instr[2])) 
							fileContents.append(str + System.lineSeparator()); 
						else {
							errorPart = errorPart + str + "\n";
						}
						// else throw exception
				}else {
							System.out.println("r sb");
							throw new ParseException(str, 0);
						}
				break;
			// set
			case "s":
				// if s bbb
				if (instr.length == 2) {
					this.centerUser = instr[1];
					List<Person> persons = new ArrayList<Person>(this.graph.getAllNodes());
					boolean found = false;
					for (int i = 0; i < persons.size(); i++) {
						if (persons.get(i).getName().equals(centerUser)) {
							CentralUser.setName(centerUser);
							fileContents.append(str + System.lineSeparator());
							found = true;
						}
					}
					if (!found) {
						CentralUser.setName(centerUser);
						errorPart = errorPart + str + "\n";
					}
				}
				// else throw exception
				else {
					System.out.println("s sb");
					throw new ParseException(str, 0);
				}
				break;

			default:
				continue;

			}

		}
		logContent = fileContents.toString();
		// save to file after load
		saveToFileAtFirst();
		if (!errorPart.equals("")) {
			throw new PersonInvalidNameException(errorPart);
		}
		sc.close();

	}

	/**
	 * save the log of the user
	 * 
	 * @param fileName The file to save the log to
	 * @throws IOException exceptions produced by failed or interrupted I/O
	 *                     operations.
	 */
	@Override
	public void saveToFile(File fileName) {
		try {

			if (!fileName.exists()) {
				fileName.createNewFile();
			}
			Scanner sc = new Scanner(new File("log.txt"));

			String content;
			while (sc.hasNextLine()) {
				content = sc.nextLine();
				FileWriter fw = new FileWriter(fileName.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content + "\n");
				bw.close();

			}

			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * private helper method to save loaded files instructions to log.txt
	 */
	private void saveToFileAtFirst() {
		try {
			if (!log.exists()) {
				log.createNewFile();
			}
			FileWriter fw = new FileWriter(log, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(logContent);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////// public for test, change to private after
	////////////////////////// test///////////////////
	// Initialize pre list and visited
	public void ini() {
		// get all the nodes in the graph
		List<Person> all = new ArrayList<Person>(graph.getAllNodes());

		// create container for Units
		nodeC = new ArrayList<Node>();

		// create unit for every Person and add to the container
		for (int i = 0; i < all.size(); i++) {
			nodeC.add(new Node(all.get(i)));
		}
	}

	// helper method for the getShortestPath
	public Node pN(Person person) {
		Node output = null;
		for (int i = 0; i < nodeC.size(); i++) {
			if (nodeC.get(i).name.equals(person)) {
				output = nodeC.get(i);
			}
		}

		return output;
	}

	// remove the node that has the least pre size in the queue
	public Queue<Node> firstMin(Queue<Node> in) {
		// if the queue is empty
		if (in.size() == 0) {
			return in;
		}

		Node min;
		Queue<Node> out = new LinkedList<Node>();
		List<Node> temp = new ArrayList<Node>(in);
		min = temp.get(0);

		// compare the pre size of each node, get the min
		int i;
		for (i = 0; i < temp.size(); i++) {
			if (temp.get(i).pre.size() < min.pre.size()) {
				min = temp.get(i);
			}
		}

		temp.remove(min);

		// create a queue starts with a unit that has the shortest path
		out.add(min);
		for (int t = 0; t < temp.size(); t++) {
			out.add(temp.get(t));
		}
		return out;
	}

	public Graph painter(List<Person> input) throws PersonNotFoundException {
		Graph a = new Graph();

		// add the points to the graph
		for (int i = 0; i < input.size(); i++) {
			a.addNode(input.get(i));
		}

		// add the friendship to the graph
		for (int i = 0; i < input.size(); i++) {
			// get the friendships
			List<Person> friendShips = new ArrayList<Person>(getFriends(input.get(i).getName()));

			for (int j = 0; j < friendShips.size(); j++) {
				a.addEdge(input.get(i), friendShips.get(j));
			}
		}

		return a;
	}

//////////////////////shouldn't exist, exists only for test///////////////////////////////
	public void printC() {
		System.out.println("* Container content *");
		for (int i = 0; i < nodeC.size(); i++) {
			System.out.print("node name: " + nodeC.get(i).name.getName());
			System.out.print(" | node pre size: " + nodeC.get(i).pre.size());
			System.out.println(" | node visit status: " + nodeC.get(i).visit);
		}
	}

	public void printNode(Node u) {
		System.out.println("* Node content *");
		System.out.print("Node name: " + u.name.getName());
		System.out.print(" | unit pre content: ");
		for (int i = 0; i < u.pre.size(); i++) {
			if (i != (u.pre.size() - 1)) {
				System.out.print(u.pre.get(i).getName() + "->");
			} else {
				System.out.print(u.pre.get(i).getName());
			}
		}
		System.out.println(" | node visit status: " + u.visit);
	}

	public void printQ(Queue<Node> a) {
		Queue temp = a;
		System.out.println("* queue content *");
		System.out.println("queue size: " + a.size());
		for (Node u : a) {
			printNode(u);
		}
	}

	public void printPersonList(List<Person> a) {
		System.out.println("* The content of the list *");
		for (int i = 0; i < a.size(); i++) {
			System.out.print(" [ " + a.get(i).getName() + " ] ");
		}
	}

	public Graph getGraph() {
		return this.graph;
	}
//////////////////////////////////////////////////////////////////////////////////////////  

}
