import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/*
 * 
 * So right now the program works this way:
 * Main class creates an instance of a window, has inherited the properties of JFrame
 * 
 * The window creates a panel, then uses the graph class to create a graph data structure containning the
 * JLabels being added to the JPanel
 * 
 * The graph class, just like the graph data structure, contains vertices (The objects called tiles)
 * and the edges in the adjListArray in line 111
 * 
 * So within the Graph class, if i = 1, you can get the vertex by doing tiles[i] and
 * you can get the edges with adjListArray[i]
 * 
 * This brings me to the next point, Tiles (the vertices) extend JLabel, so within the graph class
 * I've added the mouseListener in a method in line 165
 * 
 * So, problem 1:
 * 
 * The mouse event works, but I don't have anyway of know which tile in tiles[] was clicked.
 * 
 * Problem 2:
 * Within the Tile class, I want to set the timer. Within the timer I want to use the use this.setLocation(...)
 * But since I can't reference the Tile object itself within the actionListener I can't do that.
 * 
 * I can fix this by simply adding a JLabel within the class, but I might have to change a few other things within
 * my project.
 */

class Main {
	public static void main (String[] args) {
		Window w = new Window("Tile Puzzle Game");
	}
}
class Window extends JFrame implements KeyListener{
	Graph graph;
	JPanel panel;
	public Window (String s) {
		super(s);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(415, 440);
		
		// Set up the panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    //panel.setOpaque(false);
	    
	    setLayout(new FlowLayout());
	    add(panel);
	    ((JPanel) getContentPane()).setOpaque(false);
	    
	    // Add key listener to listen for key events
	    addKeyListener(this);
	    
	    
		// Graph data structure containing the tiles
		graph = new Graph(9);
		// Populate the graph
		createGraph();
		
		
		// Now the labels are ready, so add them to the panel
		//getLayeredPane().add(graph.tiles[0], new Integer(Integer.MIN_VALUE));
		for (int i = 0; i < graph.tiles.length; i++) {
			getLayeredPane().add(graph.tiles[i].label, new Integer(Integer.MIN_VALUE));
		}
		
		// Display graph information after initial initialization.
		graph.printVertices(graph.tiles);
		graph.printCoordinates();
		graph.printGraph();
	}
	void createGraph() {
		// Add all the edges
		
		// 0, 1, 2
		// 3, 4, 5
		// 6, 7, 8
		
		//Top left vertex
		graph.addEdge(0, 1);
		graph.addEdge(0, 3);
		
		//Top center vertex
		graph.addEdge(1, 2);
		graph.addEdge(1, 4);
		
		//Top right vertex
		graph.addEdge(2, 5);
		
		//Middle left vertex
		graph.addEdge(3, 4);
		graph.addEdge(3, 6);
		
		//Middle center vertex
		graph.addEdge(4, 5);
		graph.addEdge(4, 7);
		
		//Middle right vertex
		graph.addEdge(5, 8);
		
		//Bottom left vertex
		graph.addEdge(6, 7);
		
		//Bottom center vertex
		graph.addEdge(7, 8);
		
		// Add vertices
		graph.createVertexes();
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// If the user presses 's', the game should self-resolve
		System.out.println("Key was pressed!: " + e);
		//graph.aStar();
		ArrayList<State> children = graph.calculateChildrenStates();
		
		for (State s : children)
		{
			System.out.println("|--------------------------|");
			Graph.printVertices(s.getTiles());
		}
	}
}

