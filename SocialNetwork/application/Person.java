//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files: Person.java 
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

/**
 * This class represents a Person, which functions a vertex in Graph. 
 * 
 * @author Boya Zeng, lecture2, and bzeng7@wisc.edu
 * @author Yuxin He, lecture1, and yhe242@wisc.edu
 * @author Zeyu Tan, lecture1, and ztan56@wisc.edu
 * @author Samuel Weng, lecture1, and sweng23@wisc.edu
 * @author Shouzhe Li, lecture1, and sli649@wisc.edu
 *
 */
class Person {
 //the name of the person
 private String name;

 /**
  * The constructor of the Person with the given name
  * 
  * @param name the given name of the Person
  */
 Person(String name) {
  this.name = name;
 }

 /**
  * Getter of the name of the Person
  * 
  * @return the name of the Person
  */
 String getName() {
  return this.name;
 }
}