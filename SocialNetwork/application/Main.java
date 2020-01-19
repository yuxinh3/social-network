//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: A3 Social Network
// Files:Main.java 
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The main class of the social network that drive the GUI and all class
 * 
 * @author Boya Zeng, lecture2, and bzeng7@wisc.edu
 * @author Yuxin He, lecture1, and yhe242@wisc.edu
 * @author Zeyu Tan, lecture1, and ztan56@wisc.edu
 * @author Samuel Weng, lecture1, and sweng23@wisc.edu
 * @author Shouzhe Li, lecture1, and sli649@wisc.edu
 */
public class Main extends Application {
    private List<String> args;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final String APP_TITLE = "Social Network";
    private static final double CENTERX = 200;
    private static final double CENTERY = 200;
    private static final double RADIUS = 150;

    public SocialNetwork socialNetwork;
    public VBox signUpBox = new VBox();
    public VBox twoInputBox = new VBox();
    public VBox centerBox = new VBox();
    public VBox bottomBox = new VBox();
    public MenuBar menuBar = new MenuBar();

    private String centerUser;
    private Scene mainScene;
    private BorderPane root = new BorderPane();
    private Pane pane = new Pane();
    private int singleInput;
    private int twoInput;
    private List<Person> allPerson = new ArrayList<Person>();
    private File log = new File("log.txt");
    private String logContent;
    private VBox rightBox = new VBox();
    private Set<point> points = new HashSet<point>();
    private int edgeNum;
    private int nodeNum;
    private TextField tf1 = new TextField();
    private Text tx1 = new Text();
    private Text tx2 = new Text();
    private String status = "";

	/**
	 * Inner class that represent a point that stands for a node in the graph
	 *
	 */
    private class point {
        private double xCoor;
        private double yCoor;
        private Circle circle;
        private Text text;
        private String name;

        /**
		 * Constructor of a point
		 * 
		 * @param xCoor  x coordinate
		 * @param yCoor  y coordinate
		 * @param circle the shape that it shows in the GUI
		 * @param text   the text inside the circle
		 * @param name   name of the Person
		 */
        private point(double xCoor, double yCoor, Circle circle, Text text, String name) {
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.text = text;
            this.circle = circle;
            this.name = name;
        }

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
            if (Character.isLetterOrDigit(name.charAt(i)) || name.charAt(i) == '_'
                || name.charAt(i) == '\'') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    /**
	 * method to setup signUpBox (Single input box) set up a vbox that takes single
	 * input as argument
	 */
    public void setUpSignUpBox() {
        Button bt1 = new Button("Done");
        // TextField tf1 = new TextField();
        // tf1.setPromptText("Type here");

        signUpBox.getChildren().addAll(tx1, tf1, bt1);
        signUpBox.setVisible(false);

        bt1.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(false);
            if (singleInput == 1) {
                if (isValidName(tf1.getText())) {
                    if (socialNetwork.addUser(tf1.getText())) {
                        // add user log
                        logContent = "a " + tf1.getText() + "\n";
                        saveToFile(log);
                        logContent = null;
                    } else {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("User Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("User Duplicate: Unable to add");
                        alert.showAndWait();
                    }
                    try {
                        updateAllPerson();
                        showAll();
                    } catch (PersonNotFoundException e1) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("User Warning");
                        alert.setHeaderText(null);
                        alert
                            .setContentText("PersonNotFound Exception: Unable to show all persons");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The names may contain any letters {A-Z}, space, "
                        + "digits {0-9}, underscore {_}, or single-quote characte {'}");
                    alert.showAndWait();
                }

            } else if (singleInput == 2) {
                if (socialNetwork.removeUser(tf1.getText())) {
                    // remove user log
                    logContent = "r " + tf1.getText() + "\n";
                    saveToFile(log);
                    logContent = null;
                    if (centerUser != null) {
                        if (centerUser.equals(tf1.getText())) {
                            try {
                                updateAllPerson();
                                showAll();
                            } catch (PersonNotFoundException e1) {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("User Warning");
                                alert.setHeaderText(null);
                                alert.setContentText(
                                    "PersonNotFound Exception: Unable to show all persons");
                                alert.showAndWait();
                            }
                        } else {
                            try {
                                drawGraph();
                            } catch (PersonNotFoundException e1) {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("User Warning");
                                alert.setHeaderText(null);
                                alert.setContentText(
                                    "PersonNotFound Exception: Unable to draw graph");
                                alert.showAndWait();

                            }
                        }

                    } else {
                        try {
                            updateAllPerson();
                            showAll();
                        } catch (PersonNotFoundException e1) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("User Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Person Not Found: Unable to show all persons");
                            alert.showAndWait();
                        }
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("User Not Found: Unable to remove");
                    alert.showAndWait();
                }
            } else if (singleInput == 3) {
                File file = new File(tf1.getText());
                String errorMessage = "";
                try {
                   
                    socialNetwork.loadFromFile(file);
                    updateAllPerson();
                    showAll();
                } catch (IOException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("IOException: Unable to load file");
                    alert.showAndWait();
                } catch (ParseException e2) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("ParseException: Unable to load file");
                    alert.showAndWait();
                } catch (PersonNotFoundException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("ParseException: Unable to load file");
                    alert.showAndWait();
                } catch (PersonInvalidNameException e4) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    errorMessage = e4.getMessage();
                    alert.setContentText("Error:\n " + e4.getMessage());
                    alert.showAndWait();
                } finally {
                    if(!(CentralUser.getName()==null)&&!CentralUser.getName().equals(centerUser)) {
                        centerUser=CentralUser.getName();
                        boolean found = false;
                        for (int i = 0; i < allPerson.size(); i ++) {
                            if (allPerson.get(i).getName().equals(centerUser)) {
                                setSelectedUser(centerUser);
                                found = true;
                                try {
                                    drawGraph();
                                } catch (PersonNotFoundException e1) {
                                    Alert alert = new Alert(AlertType.WARNING);
                                    alert.setTitle("User Warning");
                                    alert.setHeaderText(null);
                                    alert.setContentText("PersonNotFoundException: Unable to draw graph");
                                    alert.showAndWait();
                            } 
                        }
                        }
                        if (!found) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("User Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Error:\n " + errorMessage);
                            alert.showAndWait();
                            showAll();
                           
                        }  
                        
                        
                    }
                }
                
            } else if (singleInput == 4) {
                try {
                    export(tf1.getText());
                } catch (IOException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("IOException: Unable to save file");
                    alert.showAndWait();
                }
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Goodbye!!");
                alert.showAndWait();
                Platform.exit();
            } else if (singleInput == 5) {
                try {
                    updateAllPerson();
                    for (int i = 0; i < allPerson.size(); i++) {
                        if (allPerson.get(i).getName().equals(tf1.getText())) {
                            setSelectedUser(tf1.getText());
                        }
                    }
                    throw new PersonNotFoundException();
                } catch (PersonNotFoundException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("PersonNotFound Exception: Unable to draw graph");
                    alert.showAndWait();
                }


                logContent = "s " + tf1.getText() + "\n";
                saveToFile(log);
                logContent = null;
            }
            setUpRightBox();
            if (centerUser != null) {
                try {
                    drawGraph();
                } catch (PersonNotFoundException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("PersonNotFound Exception: Unable to draw graph");
                    alert.showAndWait();
                }
            }
            singleInput = 0;
        });
    }


	/**
	 * method to setup twoinputBox Set up a vbox to access two argument
	 */
    public void setUpTwoInputBox() {
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        tf1.setPromptText("Type User here");
        tf2.setPromptText("Type User here");
        Button bt = new Button("Done");
        twoInputBox.setVisible(false);
        bt.setOnAction((ActionEvent e) -> {
            twoInputBox.setVisible(false);
            if (twoInput == 1) {
                if (isValidName(tf1.getText()) && isValidName(tf2.getText())) {
                    List<String> allPersonName = new ArrayList<String>();
                    for (Person ppl : allPerson) {
                        allPersonName.add(ppl.getName());
                    }
                    
                    if (allPersonName.contains(tf1.getText()) && !allPersonName.contains(tf2.getText())) {
                         Alert alert = new Alert(AlertType.WARNING);
                         alert.setTitle("User Warning");
                         alert.setHeaderText(null);
                         alert.setContentText("\"" + tf2.getText()+ "\" not exist, the system automatically "
                                + "add this person with the assigned friendship");
                         alert.showAndWait();
                    }
                    else  if (allPersonName.contains(tf2.getText()) && !allPersonName.contains(tf1.getText())) {
                         Alert alert = new Alert(AlertType.WARNING);
                         alert.setTitle("User Warning");
                         alert.setHeaderText(null);
                         alert.setContentText("\"" + tf1.getText()+"\" not exist, the system automatically "
                                + "add this person with the assigned friendship");
                         alert.showAndWait();
                    } 
                    
                    else if (!allPersonName.contains(tf1.getText()) && !allPersonName.contains(tf2.getText())) {
                     Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("User Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Both Person do not exist, the system automatically "
                                + "add two Person with the friendship");
                        alert.showAndWait();
                    }
                    
                    if (socialNetwork.addFriends(tf1.getText(), tf2.getText())) {
                        // add friends log
                        logContent = "a " + tf1.getText() + " " + tf2.getText() + "\n";
                        saveToFile(log);
                        logContent = null;
                        if (centerUser != null) {
                            try {
                                drawGraph();
                            } catch (PersonNotFoundException e1) {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("User Warning");
                                alert.setHeaderText(null);
                                alert.setContentText("PersonNotFound Exception: Unable to draw graph");
                                alert.showAndWait();
                            }
                        } else {
                            try {
                                updateAllPerson();
                                showAll();
                            } catch (PersonNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
        
                        }
                    } else {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("User Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("BothPersonsNotFound : Unable to add relationship");
                        alert.showAndWait();
                    }
                }
                else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The names may contain any letters {A-Z}, space, "
                            + "digits {0-9}, underscore {_}, or single-quote characte {'}");
                    alert.showAndWait();
                }
                
            } else if (twoInput == 2) {
                if (socialNetwork.removeFriends(tf1.getText(), tf2.getText())) {
                    // remove friends log
                    logContent = "r " + tf1.getText() + " " + tf2.getText() + "\n";
                    saveToFile(log);
                    logContent = null;
                    if (centerUser != null) {
                        try {
                            drawGraph();
                        } catch (PersonNotFoundException e1) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("User Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("PersonNotFound Exception: Unable to draw graph");
                            alert.showAndWait();
                        }
                    } else {
                        showAll();
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert
                        .setContentText("FriendsNotFound Exception: Unable to remove relationship");
                    alert.showAndWait();
                }
    
            } else if (twoInput == 3) {
                Set<Person> mutualFriends = new HashSet<Person>();
                try {
                    mutualFriends = socialNetwork.getMutualFriends(tf1.getText(), tf2.getText());
                } catch (PersonNotFoundException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("PersonNotFound Exception: Unable to get mutual friends");
                    alert.showAndWait();
                }
    
                pane.getChildren().clear();
                Person[] friendsArray = new Person[mutualFriends.size()];
                mutualFriends.toArray(friendsArray);
                int numOfFriends = friendsArray.length;
                for (int i = 0; i < numOfFriends; i++) {
                    double degree = 2 * Math.PI * i / numOfFriends;
                    double positionX = CENTERX + RADIUS * Math.cos(degree);
                    double positionY = CENTERY + RADIUS * Math.sin(degree);
                    drawNode(friendsArray[i].getName(), positionX, positionY);
                }
            } else if (twoInput == 4) {
                List<Person> shortestPath = new ArrayList<Person>();
    
                try {
                    shortestPath = socialNetwork.getShortestPath(tf1.getText(), tf2.getText());
                    if (shortestPath.isEmpty()) {
                        showAll();

                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("User Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("No shortest path between these two people");
                        alert.showAndWait();
                    }
                    else {

                        // draw a path
                        pane.getChildren().clear();
            
                        for (int i = 0; i < shortestPath.size(); i++) {
                            double positionX = CENTERX + i * RADIUS;
                            double positionY = CENTERY;
                            drawNode(shortestPath.get(i).getName(), positionX, positionY);
                        }
                        for (int i = 0; i < shortestPath.size() - 1; i++) {
                            double positionX1 = CENTERX + i * RADIUS;
                            double positionX2 = positionX1 + RADIUS;
                            double positionY = CENTERY;
                            drawEdge(positionX1, positionX2, positionY, positionY);
                        }
                        
                    }
                } catch (PersonNotFoundException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("PersonNotFound Exception: Unable to get shortest path");
                    alert.showAndWait();
                }
            }
            setUpRightBox();
    
        });
        twoInputBox.getChildren().addAll(tx2, tf1, tf2, bt);
    }

	/**
	 * method to setup center box
	 */
    public void setUpCenterBox() {
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(pane);

    }

	/**
	 * method to setup bottom box
	 */
    public void setUpBottomBox() {
        bottomBox.getChildren().clear();
        HBox hb = new HBox();
        setUpSignUpBox();
        setUpTwoInputBox();
        hb.getChildren().addAll(signUpBox, twoInputBox);
        bottomBox.getChildren().addAll(hb);
    }

	/**
	 * method to setup right box
	 */
    private void setUpRightBox() {
        try {
            updateAllPerson();
        } catch (PersonNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        rightBox.getChildren().clear();
        Label lb1 = new Label("Number of User(s) in the graph currently: : " + nodeNum);
        Label lb2 = new Label("Number of Friendship(s) in the graph currently: " + edgeNum);
        Set<Graph> connected = null;
        try {
            connected = socialNetwork.getConnectedComponents();
        } catch (PersonNotFoundException e1) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("User Warning");
            alert.setHeaderText(null);
            alert.setContentText("PersonNotFound Exception: Unable to get connected components");
            alert.showAndWait();
        }
        int num = connected.size();
        Label lb3 = new Label("Number of Connected Components: " + num);

        try (Scanner scanner = new Scanner(log)) {

            while (scanner.hasNextLine()) {

                status = scanner.nextLine().trim();

                String[] instr = status.split(" ");

                switch (instr[0]) {
                    // add
                    case "a":
                        // if a bbb
                        if (instr.length == 2)
                            status = "Add Person: " + instr[1];
                        // if a bbb bbb
                        else if (instr.length == 3)
                            status = "Add Friendship: " + instr[1] + " " + instr[2];

                        break;
                    // remove
                    case "r":
                        // if r bbb
                        if (instr.length == 2)
                            status = "Remove Person: " + instr[1];
                        // if r bbb bbb
                        else if (instr.length == 3)
                            status = "Remove Friendship: " + instr[1] + " " + instr[2];

                        break;
                    // set
                    case "s":
                        // if s bbb
                        if (instr.length == 2) {
                            status = "Set: " + instr[1] + " As Center User";
                        }

                    default:
                        continue;

                }


            }

            scanner.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        Label lb4 = new Label("Status(what was just done): " + status);


        Button bt = new Button("Clear All");
        bt.setOnAction((ActionEvent e) -> {
            for (int i = 0; i < allPerson.size(); i++) {
                socialNetwork.removeUser(allPerson.get(i).getName());
                logContent = "r " + allPerson.get(i).getName() + "\n";
                saveToFile(log);
                logContent = null;
            }
            pane.getChildren().clear();
            edgeNum = 0;
            nodeNum = 0;
            setUpRightBox();
        });
        rightBox.getChildren().addAll(lb1, lb2, lb3, lb4, bt);
    }

	/**
	 * method to setup menu box
	 */
    public void setUpMenuBox() {
        Menu file = new Menu("File");
        MenuItem file1 = new MenuItem("Load");
        MenuItem file2 = new MenuItem("Save and Exit");
        MenuItem file3 = new MenuItem("Exit without Saving");
        Menu action = new Menu("Action");
        MenuItem action1 = new MenuItem("Add Person");
        MenuItem action2 = new MenuItem("Remove Person");
        MenuItem action3 = new MenuItem("Add Relationship");
        MenuItem action4 = new MenuItem("Remove Relationship");
        MenuItem action5 = new MenuItem("Mutual Friends of Two Person");
        MenuItem action6 = new MenuItem("Shortest Friendship Path of Two Person");
        MenuItem action8 = new MenuItem("Set Centural User");
        Menu view = new Menu("View");
        MenuItem view1 = new MenuItem("Show All");

        file1.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(true);
            singleInput = 3;
            twoInputBox.setVisible(false);
            tx1.setText("Type file name here to load");
            tf1.setPromptText("Type file name here");
        });
        file2.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(true);
            singleInput = 4;
            twoInputBox.setVisible(false);
            tx1.setText("Type file name here to save");
            tf1.setPromptText("Type file name");
        });
        file3.setOnAction((ActionEvent e) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Goodbye!!");
            alert.showAndWait();
            Platform.exit();
        });

        view1.setOnAction((ActionEvent e) -> {
            try {
                updateAllPerson();
                showAll();
            } catch (PersonNotFoundException e1) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("User Warning");
                alert.setHeaderText(null);
                alert.setContentText("PersonNotFound Exception: Unable to show all person");
                alert.showAndWait();
            }


        });
        action1.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(true);
            singleInput = 1;
            twoInputBox.setVisible(false);
            tf1.setPromptText("Type user here");
            tx1.setText(
                "Type user name to add. The names\n may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action2.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(true);
            singleInput = 2;
            twoInputBox.setVisible(false);
            tf1.setPromptText("Type user here");
            tx1.setText(
                "Type user name to remove. The names\n may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action3.setOnAction((ActionEvent e) -> {
            twoInputBox.setVisible(true);
            twoInput = 1;
            signUpBox.setVisible(false);
            tx2.setText(
                "Type user names to add friendship. The names\n may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action4.setOnAction((ActionEvent e) -> {
            twoInputBox.setVisible(true);
            twoInput = 2;
            signUpBox.setVisible(false);
            tx2.setText(
                "Type user names to remove friendship. The\n names may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action5.setOnAction((ActionEvent e) -> {
            twoInputBox.setVisible(true);
            twoInput = 3;
            signUpBox.setVisible(false);
            tx2.setText(
                "Type user names to get their mutual friends. The\n names may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action6.setOnAction((ActionEvent e) -> {
            twoInputBox.setVisible(true);
            twoInput = 4;
            signUpBox.setVisible(false);
            tx2.setText(
                "Type user names to get their shortest path. The\n names may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });
        action8.setOnAction((ActionEvent e) -> {
            signUpBox.setVisible(true);
            singleInput = 5;
            twoInputBox.setVisible(false);
            tf1.setPromptText("Type user here");
            tx1.setText(
                "Type user name to set as center user. The\n names may contain any letters {A-Z},\n space, digits {0-9}, underscore {_}, \nor single-quote character.");
        });


        file.getItems().addAll(file1, file2, file3);
        action.getItems().addAll(action1, action2, action3, action4, action8, action5, action6);
        view.getItems().addAll(view1);

        menuBar.getMenus().addAll(file, action, view);
    }


    /**
	 * method to draw a graph with a center user
	 * 
	 * @throws PersonNotFoundException when the person is not found in the current
	 *                                 social network
	 */
    public void drawGraph() throws PersonNotFoundException {
        edgeNum = 0;
        nodeNum = 0;
        pane.getChildren().clear();
        // draw center user at center
        drawNode(centerUser, CENTERX, CENTERY, Color.PINK);
        nodeNum++;
        // draw all friends of center user
        Set<Person> friends = socialNetwork.getFriends(centerUser);
        Person[] friendsArray = new Person[friends.size()];
        friends.toArray(friendsArray);
        int numOfFriends = friendsArray.length;
        for (int i = 0; i < numOfFriends; i++) {
            double degree = 2 * Math.PI * i / numOfFriends;
            double positionX = CENTERX + RADIUS * Math.cos(degree);
            double positionY = CENTERY + RADIUS * Math.sin(degree);
            drawNode(friendsArray[i].getName(), positionX, positionY);
            drawEdge(CENTERX, positionX, CENTERY, positionY);
            edgeNum++;
            nodeNum++;
        }
        setUpRightBox();


    }


    /**
	 * method to draw a Node
	 * 
	 * @param name the name of the Person thats shows in the Node as text
	 * @param x    x-coordinate
	 * @param y    y-coordinate
	 */
    public void drawNode(String name, double x, double y) {
        Text text = new Text(name);
        text.setLayoutX(x - getWidth(text) / 2);
        text.setLayoutY(y);
        Circle circle = drawCircle(text, Color.WHITE);
        circle.setLayoutX(x);
        circle.setLayoutY(y - getHeight(text) / 4);
        pane.getChildren().addAll(circle, text);
    }

    /**
	 * method to draw a Node with color
	 * 
	 * @param name the name of the Person thats shows in the Node as text
	 * @param x    x-coordinate
	 * @param y    y-coordinate
	 * @param c    the color of Node
	 */
    private void drawNode(String name, double x, double y, Color c) {
        Text text = new Text(name);
        text.setLayoutX(x - getWidth(text) / 2);
        text.setLayoutY(y);
        Circle circle = drawCircle(text, c);
        circle.setLayoutX(x);
        circle.setLayoutY(y - getHeight(text) / 4);
        pane.getChildren().addAll(circle, text);
    }

	/**
	 * method to draw a circle
	 * 
	 * @param text the text showed in the circle
	 * @param c    the color of the circle
	 * @return
	 */
    private Circle drawCircle(Text text, Color c) {
        Circle circle = new Circle();
        circle.setFill(c);
        final double PADDING = 10;
        circle.setRadius(getWidth(text) / 2 + PADDING);
        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                setSelectedUser(text.getText());
                // set central user log
                logContent = "s " + text.getText() + "\n";
                saveToFile(log);
                logContent = null;
                try {
                    drawGraph();
                } catch (PersonNotFoundException e) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("PersonNotFound Exception: Unable to draw graph");
                    alert.showAndWait();
                }
            }
        });
        circle.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mainScene.setCursor(Cursor.HAND);
            }
        });
        circle.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mainScene.setCursor(Cursor.DEFAULT);
            }
        });

        return circle;
    }

    /**
	 * private helper method to get the width of the text so that we could choose
	 * the size of the circle
	 * 
	 * @param text the text would be shown in the circle
	 * @return the width of the circle
	 */
    private double getWidth(Text text) {
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getWidth();
    }

	/**
	 * private helper method to get the height of the text so that we could choose
	 * the size of the circle
	 * 
	 * @param text the text would be shown in the circle
	 * @return the width of the circle
	 */
    private double getHeight(Text text) {
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getHeight();
    }

	/**
	 * method to draw edge
	 * 
	 * @param x1 start x-coordinate
	 * @param x2 end x-coordinate
	 * @param y1 start y-coordinate
	 * @param y2 end y-coordinate
	 */
    public void drawEdge(double x1, double x2, double y1, double y2) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        pane.getChildren().addAll(line);
        line.toBack();
    }


    /**
	 * method to set the user as the center when user's mouse clicked on
	 * 
	 * @param str the string in the Node which user's mouse clicked on
	 */
    public void setSelectedUser(String str) {
        centerUser = str;
    }

	/**
	 * Start up all elements in the GUI
	 * 
	 * @param primaryStage the primary stage
	 * @throws PersonNotFoundException when the back-end trying to update all person
	 *                                 in the graph but some person not found
	 */
    public void start(Stage primaryStage) throws PersonNotFoundException {
        args = this.getParameters().getRaw();
        socialNetwork = new SocialNetwork();
        setUpMenuBox();
        setUpCenterBox();
        setUpRightBox();
        setUpBottomBox();
        root.setTop(menuBar);
        root.setRight(rightBox);
        root.setCenter(centerBox);
        root.setBottom(bottomBox);
        mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        updateAllPerson();
        showAll();

        primaryStage.setOnCloseRequest(event -> {
            TextInputDialog dialog = new TextInputDialog("logFile");
            dialog.setTitle("Comfirmation");
            dialog.setHeaderText("Do you want to save log file");
            dialog.setContentText("Enter file name of your log:");
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Save and Exit");
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Exit");
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    export(result.get());
                } catch (IOException e1) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("User Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("IOException: Unable to save file");
                    alert.showAndWait();
                }
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Goodbye!!");
                alert.showAndWait();
                Platform.exit();
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Goodbye!!");
            alert.showAndWait();
            Platform.exit();
        });

    }

	/**
	 * The private helper method that will update all person in the GUI
	 * 
	 * @throws PersonNotFoundException when some person not found in the graph
	 */
    private void updateAllPerson() throws PersonNotFoundException {
        allPerson = new ArrayList<Person>();
        List<Graph> graphs = new ArrayList<Graph>(socialNetwork.getConnectedComponents());

        for (int i = 0; i < graphs.size(); i++) {
            List<Person> temp = new ArrayList<Person>(graphs.get(i).getAllNodes());
            allPerson.addAll(temp);
        }
    }

	/**
	 * The private helper method that show all the Person and relationship which
	 * does not have a center user
	 */
    private void showAll() {
        pane.getChildren().clear();
        setSelectedUser(null);
        points = new HashSet<point>();
        nodeNum = 0;
        edgeNum = 0;
        for (int i = 0; i < allPerson.size(); i++) {
            double degree = 2 * Math.PI * i / allPerson.size();
            double positionX = CENTERX + RADIUS * Math.cos(degree);
            double positionY = CENTERY + RADIUS * Math.sin(degree);
            drawNode(allPerson.get(i).getName(), positionX, positionY);
            nodeNum++;
            points.add(new point(positionX, positionY, null, null, allPerson.get(i).getName()));
            List<Person> friendList = null;
            try {
                friendList =
                    new ArrayList<Person>(socialNetwork.getFriends(allPerson.get(i).getName()));
            } catch (PersonNotFoundException e) {
                e.printStackTrace();
            }
            List<point> allPoints = new ArrayList<point>(points);
            for (int j = 0; j < friendList.size(); j++) {
                for (int k = 0; k < allPoints.size(); k++) {
                    if (friendList.get(j).getName().equals(allPoints.get(k).name)) {
                        drawEdge(positionX, allPoints.get(k).xCoor, positionY,
                            allPoints.get(k).yCoor);
                        edgeNum++;
                    }

                }
            }
        }

        setUpRightBox();


    }
    
	/**
	 * Private helper method to allow users save their action log into txt file
	 * 
	 * @param fileName the name of the file to be saved
	 */
    private void saveToFile(File fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(logContent);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Export the log file into txt file
	 * 
	 * @param name name of the file to be exported
	 * @throws IOException when experience failure during output process
	 */
    private void export(String name) throws IOException {
        File export = new File(name + ".txt");
        if (!export.exists()) {
            export.createNewFile();
        }
        socialNetwork.saveToFile(export);
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
