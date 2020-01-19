//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files: SocialNetworkADT.java 
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
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * Defines methods required for social network visualization
 */
public interface SocialNetworkADT {

	/**
	 * Add the relationship between two people with given names
	 * 
	 * @param user   the name of the user that a friend would be added
	 * @param friend the name of the friends that would be added to this people
	 * @return true if add a relationship between these two people successfully,
	 *         false other wise. If the relationship already existed, return false;
	 */
	boolean addFriends(String user, String friend);

	/**
	 * Remove the relationship between two people with given names
	 * 
	 * @param user   the name of the user that a friend would be removed
	 * @param friend the name of the friends that would be removed from this people
	 * @return true if remove a relationship between these two people successfully,
	 *         false other wise. If both people exist, but the relationship not
	 *         found, throw false;
	 * @throws PersonNotFoundException if try to remove a friend who does not exist
	 */
	boolean removeFriends(String user, String friend) throws PersonNotFoundException;

	/**
	 * Add new user to the graph with the given name.
	 * 
	 * @param user the name of the user to be added
	 * @return true if add user successfully, false otherwise
	 * @throws IllegalArgumentException if the user is null
	 * @throws DuplicatePersonException if the user already existed in the Social
	 *                                  Network
	 */
	boolean addUser(String user) throws IllegalArgumentException, DuplicatePersonException;

	/**
	 * Remove new user to the graph with the given name.
	 * 
	 * @param user the name of the user to be removed
	 * @return true if remove user successfully, false otherwise
	 * @throws IllegalArgumentException if the user is null
	 * @throws DuplicatePersonException if the user does not exist in the Social
	 *                                  Network
	 */
	boolean removeUser(String user) throws IllegalArgumentException, PersonNotFoundException;

	/**
	 * Given the user, get all friends set of this user
	 * 
	 * @param user the name of the target user whom we want to find all friends
	 * @return the set of friends of this user
	 * @throws IllegalArgumentExceptio if the user is null
	 * @throws PersonNotFoundException if user is not exist in the social network
	 */
	Set<Person> getFriends(String user) throws IllegalArgumentException, PersonNotFoundException;

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
	Set<Person> getMutualFriends(String user1, String user2) throws IllegalArgumentException, PersonNotFoundException;

	/**
	 * Find the sequence of friends that is the shortest path between given two users 
	 * within a connected component
	 * 
	 * @param user1 the name of the target user1 to get shortest path between two
	 *              person
	 * @param user2 the name of the target user2 to get shortest path between two
	 *              person
	 * @return shortest friends set path between of two users
	 * @throws IllegalArgumentExceptio if the user is null
	 * @throws PersonNotFoundException if user is not exist in the social network
	 */
	List<Person> getShortestPath(String user1, String user2) throws IllegalArgumentException, PersonNotFoundException;

	/**
	 * To get individual Connected Components of the Social Network as a set
	 * 
	 * @return the set of disconnected graph created by Social Network
	 */
	Set<Graph> getConnectedComponents() throws PersonNotFoundException;

	/**
	 * Parse the file fileName into the graph
	 * 
	 * @param fileName The file to get initial action from
	 * @throws FileNotFoundException throws exceptions when parsed file do not exits
	 * @throws IOException           if the give file cannot be read
	 * @throws ParseException        if the given file cannot be parsed
	 * @throws PersonInvalidNameException 
	 * @throws PersonNotFoundException 
	 */
	void loadFromFile(File fileName) throws FileNotFoundException, IOException, ParseException, SetException, PersonInvalidNameException;

	/**
	 * save the log of the user
	 * 
	 * @param fileName The file to save the log to
	 * @throws IOException exceptions produced by failed or interrupted I/O
	 *                     operations.
	 */
	void saveToFile(File fileName) throws IOException;
}