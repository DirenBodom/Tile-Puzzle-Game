import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
class Window extends JFrame {
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
		graph.printVertices();
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
}
//Java Program to demonstrate adjacency list 
//representation of graphs 
class Graph {
	Tile[] tiles = new Tile[9];
	int skippedTile; // Tile to be skipped
	ArrayList<Integer> adjListArray[]; // Adjacency list which contains the edges
	boolean lockOn;
	
	// For this graph implementation, vertices are represented by tile objects
	Graph (int V) {
		lockOn = false;
		// The integer V represents how big you want the graph to be.
		adjListArray = new ArrayList[V];
		
		// Initialize the LinkedList objects of the adjListArray list
		for (int i = 0; i < adjListArray.length; i++) {
			adjListArray[i] = new ArrayList<Integer>();
		}
	}
	// Creates vertices
	public void createVertexes () {
		String[] imageNames = {"TL.png", "TC.png", "TR.png",
				"ML.png", "MC.png", "MR.png",
				"BL.png", "BC.png","BR.png"};
		
		int[][] positions = {{5, 5}, {140, 5}, {275, 5},		// 0, 1, 2
				{5, 140}, {140, 140}, {275, 140},	// 3, 4, 5
				{5, 275}, {140, 275}, {275, 275}};  // 6, 7, 8
		
		// Selects a random tile to not display
		Random rand = new Random();
		skippedTile = rand.nextInt(9);
		// Selects a random name to use for the skipped tile
		int randName = rand.nextInt(9);
		
		// List of indices to index the Locations 
		ArrayList<Integer> indices = new ArrayList<Integer>();

		// Initializes the possible indices as a set of 0-9
		for (int i = 0; i < 9; i++) {
			indices.add(i);
		}
		// The order of which you're inserting the string names is random
		// The positions will always be inserted in the same order
		// You have one skipped name to take into account
		
		// Create tiles
		for (int i = 0; i < imageNames.length; i++) {
			randName = rand.nextInt(indices.size());
			// Create the tile, one to one correspondence between imageNamees and positions
			if (i != skippedTile) {
				tiles[i] = new Tile(imageNames[indices.get(randName)], 
					new ImageIcon("Tiles/" + imageNames[indices.get(randName)])
					, positions[i][0], positions[i][1], i);
				// Add the mouse listener to the JLabel
				addTileMouseListener(i);
				} else {
					tiles[i] = new Tile("Ignored tile", 
								new ImageIcon("Tiles/" + imageNames[indices.get(randName)])
								, positions[i][0], positions[i][1], i);
							tiles[i].label.setVisible(false); 	// Makes the ignored tile invisible
				}
			indices.remove(randName);
		}
	}
	/*
	 * As of now, clicking any tile swaps index 0 and 5.
	 * 
	 */
	// Implements the mouse listener the tile at the given index
	void addTileMouseListener(int index) {
		// Checks whether another tile is being animated, discredits the click if true
		/*
		if (lockOn) {
			System.out.println("Currently moving another tile!");
			return;
		}*/
		tiles[index].label.addMouseListener(new MouseListener() {
		// Places a lock to prevent any other click operations
		
		@Override
		public void mouseReleased(MouseEvent e) {
		// Since getSource returns a copy of the JLabel being clicked
		// Set the JLabel's text as the image's name
		// Then here using that text, create a method that searches the tiles
		// And returns an int corresponding with the image name index in tiles.
			
		
		JLabel temp = (JLabel)e.getSource();
		int labelIndex = findLabelIndex(temp.getText());
		boolean emptyNeighbor = false;		// Variable to keep track whether empty tile is a neighbor
		
		// Find out if the skipped tile is your neighbor
		for(int i = 0; i < adjListArray[labelIndex].size(); i++) {
			if (adjListArray[labelIndex].get(i) == skippedTile) {
				// Exit the loop as soon as you find out it is true
				emptyNeighbor = true;
				break;
			}
		}
		
		if (emptyNeighbor) {
			System.out.println("Empty node is a neighbor!\n");
			
			//TODO figure out where the empty tile is relative to the clicked tile
			//	   set the direction, set the timer, then swap in that order.
			
			// Get the clicked tile's position.
			int xClicked = tiles[labelIndex].x;
			int yClicked = tiles[labelIndex].y;
			
			// Get the clicked tile's position.
			int xIgnored = tiles[skippedTile].x;
			int yIgnored = tiles[skippedTile].y;
			
			// Case where the ignored tile is north
			if ((xClicked == xIgnored) && (yIgnored < yClicked)) {
				tiles[labelIndex].setDirection('n');
				tiles[labelIndex].bound = yIgnored;
			}
			// Case where the ignored tile is south
			else if ((xClicked == xIgnored) && (yIgnored > yClicked)) {
				tiles[labelIndex].setDirection('s');
				tiles[labelIndex].bound = yIgnored;
			}
			// Case where the ignored tile is west
			else if ((xClicked > xIgnored) && (yIgnored == yClicked)) {
				tiles[labelIndex].setDirection('w');
				tiles[labelIndex].bound = xIgnored;
			}
			// Case where the ignored tile is east
			else if ((xClicked < xIgnored) && (yIgnored == yClicked)) {
				tiles[labelIndex].setDirection('e');
				tiles[labelIndex].bound = xIgnored;
			}
			// Set ignored tile's position to the clicked tile's current position
			tiles[skippedTile].x = xClicked;
			tiles[skippedTile].y = yClicked;
			
			// Start the animation
			tiles[labelIndex].timer.start();
			
			swap(labelIndex, skippedTile);
			
			// Display graph information after swap
			printVertices();
			printCoordinates();
			printGraph();
			// Make sure the skippedTile is updated
			skippedTile = labelIndex;
		} else {
			System.out.println("Tile is unmovable.");
		}
		// Changes the locks status
		lockOn = false;
		
		// Check whether the ignored tile is your neighbor
		// If it is, determine where it is relative to the current tile
		// Set the direction for the timer to animate appropriately
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
									
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
									
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
									
		}
		public void mouseExited(MouseEvent e) {
		}
		});
	}
	// Traverses the vertices to find and return the index corresponding to the given label
	int findLabelIndex (String n) {
		// The n parameter is the label's image name: e.g "TL.png"
		int index = -1;	// In case the label is not found, return -1;
		
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].label.getText().equals(n) ) {
				index = i;
			}
		}
		
		return index;
	}
	// Adds the list an edge to the undirected graph
	public void addEdge(int source, int destination) {
		// Add an edge from source to destination.  
        this.adjListArray[source].add(destination); 
          
     // Add an edge from destination to source.  
        this.adjListArray[destination].add(source);
	}
	// Swaps 2 nodes in the graph, both their Tile and their neighbors. Note that this assumes src and dest are neighbors
	public void swap(int src, int dest) {
		System.out.println("Swapping " + tiles[src].label.getText() + " with " + tiles[dest].label.getText());
		// Swap in the vertex list
		Tile tempTile = tiles[src];
		tiles[src] = tiles[dest];
		tiles[dest] = tempTile;
		
		printVertices();
	}
	// Print vertex coordinates
	public void printCoordinates () {
		System.out.println("Vertex coordinates: ");
		for (int i = 0; i < tiles.length; i++) {
			System.out.println("Vertex " + tiles[i].label.getText() + ": (" + tiles[i].x + ", " + tiles[i].y + ")");
		}
		System.out.println();
	}
	// Display the Vertex with its edges
	public void printGraph () {
		System.out.println("Tile neighbors:");
		for (int i = 0; i < adjListArray.length; i++) {
			System.out.print(tiles[i].label.getText() + ": {");	// tiles[i].label.getText() is the label's name
			for (int j = 0; j < adjListArray[i].size(); j++) {
				if (j == adjListArray[i].size() - 1) {
					System.out.print(adjListArray[i].get(j));
				} else {
					System.out.print(adjListArray[i].get(j) + ", ");
				}
			}
			System.out.println("}");
		}
		System.out.println("\n");
	}
	// Prints out the vertexes as they would look like on the screen; showing what the graph structure looks like
	public void printVertices() {
		System.out.println("Displaying graph");
		
		System.out.println(tiles[0].label.getText() + "		" + tiles[1].label.getText() + " 	" + tiles[2].label.getText());
		System.out.println(tiles[3].label.getText() + "		" + tiles[4].label.getText() + " 	" + tiles[5].label.getText());
		System.out.println(tiles[6].label.getText() + "		" + tiles[7].label.getText() + " 	" + tiles[8].label.getText());
		System.out.println();
	}
	class Tile{
		JLabel label;
		int x, y;
		int tileIndex;
		Timer timer;
		int bound;	// Bound for animation
		private char direction;	// The direction represented as in 'n' for North
		
		public Tile (String n, ImageIcon i, int hor, int ver, int tIndex) {
			x = hor;
			y = ver;
			tileIndex = tIndex;
			// Set the JLabel bounds
			label = new JLabel();
			label.setText(n); 	// Set the label's text as the image name.
			label.setIcon(i);
			label.setBounds(hor, ver, i.getIconWidth(), i.getIconHeight());
			
			// Create timer
			
			timer = new Timer(10, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Check if the direction has been set
					if (direction != '\u0000') {
						//System.out.println("Direction: " + direction);
						switch (direction) {
							case 'n': if (y > bound) {
										label.setLocation(x, y);
										y--;
									} else {
										timer.stop();
										System.out.println("Final animation position: (" + x + ", " + y + ")");
									}
									break;
							case 's': if (y < bound) {
										label.setLocation(x, y);
										y++;
									  } else {
										timer.stop();
										System.out.println("Final animation position: (" + x + ", " + y + ")");
									  }
									break;
							case 'w': if (x > bound) {
										label.setLocation(x, y);
										x--;
							  		  } else {
							  			  timer.stop();
							  			System.out.println("Final animation position: (" + x + ", " + y + ")");
							  		  }
									  break;
							case 'e': if (x < bound) {
										label.setLocation(x, y);
										x++;
							  		  } else {
							  			  timer.stop();
							  			System.out.println("Final animation position: (" + x + ", " + y + ")");
							  		  }
									  break;
						}
					} else {
						System.out.println("Direction not set!");
						timer.stop();
					}
				}
			});
		}
		// Sets the direction
		public void setDirection (char d) {
			direction = d;
		}
		
	}
}