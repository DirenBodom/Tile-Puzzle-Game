import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

//Java Program to demonstrate adjacency list 
//representation of graphs 
class Graph {
	Tile[] tiles = new Tile[9];
	int skippedTile; // Tile to be skipped
	static ArrayList<Integer> adjListArray[]; // Adjacency list which contains the edges
	boolean lockOn;
	static HashMap<Integer, int[]> map; 
	/*
	 * This variable is used inside the calculateChildrenState function. To avoid an infinite loop,
	 * child state cannot contain its parent node as a child. Every time the calculateChildrenState is ran
	 * it should use this variable to know which neighbor to skip.
	 */
	private static int skipState = -1;
	
	// For this graph implementation, vertices are represented by tile objects
	Graph (int V) {
		// Instantiate the map
		map = new HashMap<>();
		lockOn = false;
		// The integer V represents how big you want the graph to be.
		adjListArray = new ArrayList[V];
		
		// Add values to the map
		setMapValues();
		
		// Initialize the LinkedList objects of the adjListArray list
		for (int i = 0; i < adjListArray.length; i++) {
			adjListArray[i] = new ArrayList<Integer>();
		}
	}
	public void setMapValues() {
		for (int i = 0; i < 3; i++) {
			// [row, column]
			int[] temp = {0, i};
			map.put(i, temp);
		}
		for (int i = 3; i < 6; i++) {
			// [row, column]
			int[] temp = {1, i - 3};
			map.put(i, temp);
		}
		for (int i = 6; i < 9; i++) {
			// [row, column]
			int[] temp = {2, i - 6};
			map.put(i, temp);
		}
	}
	// A star algorithm
	public State aStar() {
		/*
		 * G: Cost (Number of steps to current state)
		 * H: Estimated distance to goal
		 */
		
		// List of all the nodes we've evaluated an F cost for
		ArrayList<State> open = new ArrayList<State>();
		// Elements the have already been fully explored.
		ArrayList<State> closed = new ArrayList<State>();
		
		// Add the start node to the open nodes (states)
		State start = new State(this.tiles);
		// Set initial node's g cost to 0
		start.setGCost(0);
		
		open.add(start);
		
		// Return once you've reached the goal
		
		while(true) {
			// Find the open node with the lowest f cost
			int lowestIndex = lowestFCost(open);
			System.out.println("Lowest Index: " + lowestIndex);
			State current = open.get(lowestIndex);
			
			// Remove the current node from the open list and add it to the closed list
			closed.add(open.remove(lowestIndex));
			
			// If the target node has been reached, return
			if (current.getHCost() == 0) {
				return current;
			}
			
			// Otherwise, calculate all of the children states.
			ArrayList<State> children = calculateChildrenStates(current);
			// Expand each child node
			for (int i = 0; i < children.size(); i++) {
				// Check if this child is already closed
				if (closed.contains(children.get(i))) {
					// Skip to the next neighbor
					continue;
				}
				// If this is not already in the open node list, add it
				// Also check whether the new path is shorter to that node (smaller gCost)
				if (!open.contains(children.get(i)) || (children.get(i).getGCost() > current.getGCost() + 1)) {
					// Set the g cost of this child node
					children.get(i).setGCost(current.getGCost() + 1);
					
					// Set the parent to the current node
					children.get(i).setParent(current);
					
					// Add to list of nodes to explore
					open.add(children.get(i));
				}
			}
		}
	}
	public static int lowestFCost(ArrayList<State> s) {
		int min = 1000;
		int index = -1;
		for (int i = 0; i < s.size(); i++) {
			if (s.get(i).getFCost() < min) {
				// Found a new min
				min = s.get(i).getFCost();
				index = i;
			}
		}
		return index;
	}
	public ArrayList<State> calculateChildrenStates() {
		return Graph.calculateChildrenStates(new State(this.tiles));
	}
	public static ArrayList<State> calculateChildrenStates(State s) {
		Tile[] t = s.getTiles();
		ArrayList<State> result = new ArrayList<State>();
		int ignoredIndex = 0;
		// Find the index of the ignored tile
		for (int i = 0; i < t.length; i++) {
			if (t[i].getName().compareTo("Ignored tile") == 0) {
				ignoredIndex = i;
				System.out.println("Ignored Index: " + ignoredIndex);
				break;
			}
		}
		
		// Create new copies of the current tiles state to modify for the new states.
		Tile[][] p = new Tile[adjListArray[ignoredIndex].size()][];
		for (int i = 0; i < p.length; i++) {
			p[i] = new Tile[t.length];
			for (int j = 0; j < p[i].length; j++) {
				// Use the copy constructor to create a new tile with the same information.
				Tile temp = new Tile(t[j]);
				if (temp != null) {
					p[i][j] = temp;
				}
			}
		}
		
		// Print p
//		System.out.println("Printing initial state: ");
//		printVertices(p);
		// Get its neighbors and create a state with the ignored tile switch with that neighbor
		/**
		 * 2. Place the ignored tile at the neighbor's index
		 * 3. Place the neighbor at the ignored tile's index
		 */
		int neighborIndex;
		for (int i = 0; i < p.length; i++) {
			neighborIndex = adjListArray[ignoredIndex].get(i);
			
			// Find whether this neighbor belongs to the parent state
			//if (s.getParent() == null || (s.getParent() != null && s.getParentSkipped() != neighborIndex)) {
				Tile ignoredCopy = new Tile(p[i][ignoredIndex]);
				Tile neighborCopy = new Tile(p[i][neighborIndex]);
				//for (int j = 0; j < p[i].length; j++) {
					// Get this neighbor's index in the original list
					p[i][ignoredIndex] = neighborCopy;
					p[i][neighborIndex] = ignoredCopy;
				//}
				// Create a new state based on this order list of tiles
				result.add(new State(p[i]));
				// Print
				Graph.printVertices(result.get(i).getTiles());
			//}
		}
		
		return result;
	}
	public static int[] swapTiles(Tile[] t, int src, int dest) {
		int[] res = new int[t.length];
		
		for (int i = 0; i < t.length; i++) {
			res[i] = t[i].getIndex();
		}
		
		res[src] = t[dest].getIndex();
		res[dest] = t[src].getIndex();
		
		return res;
	}
	public void printState(int[] p) {
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				System.out.print(p[i] + "\n");
			} else {
				System.out.print(p[i] + " | ");
			}
		}
		for (int i = 3; i < 6; i++) {
			if (i == 5) {
				System.out.print(p[i] + "\n");
			} else {
				System.out.print(p[i] + " | ");
			}
		}
		for (int i = 6; i < 9; i++) {
			if (i == 8) {
				System.out.print(p[i] + "\n");
			} else {
				System.out.print(p[i] + " | ");
			}
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
					, positions[i][0], positions[i][1], indices.get(randName));
				// Add the mouse listener to the JLabel
				addTileMouseListener(i);
				} else {
					tiles[i] = new Tile("Ignored tile", 
								new ImageIcon("Tiles/" + imageNames[indices.get(randName)])
								, positions[i][0], positions[i][1], indices.get(randName));
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
			printVertices(tiles);
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
		
		printVertices(tiles);
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
	public static void printVertices(Tile[] t) {
		System.out.println("Displaying graph");
		
		System.out.println(t[0].label.getText() + "(" + t[0].getIndex() + ")		" + t[1].label.getText() + "(" + t[1].getIndex() + ") 	" + t[2].label.getText() + "(" + t[2].getIndex() + ")");
		System.out.println(t[3].label.getText() + "(" + t[3].getIndex() + ")	" + t[4].label.getText() + "(" + t[4].getIndex() + ") 	" + t[5].label.getText() + "(" + t[5].getIndex() + ")");
		System.out.println(t[6].label.getText() + "(" + t[6].getIndex() + ")		" + t[7].label.getText() + "(" + t[7].getIndex() + ") 	" + t[8].label.getText() + "(" + t[8].getIndex() + ")");
		System.out.println();
	}
	
}
class Tile{
	JLabel label;
	int x, y;
	private int tileIndex;
	Timer timer;
	int bound;	// Bound for animation
	private char direction;	// The direction represented as in 'n' for North
	
	// Copy constructor
	public Tile (Tile t) {
		this(t.getName(), new ImageIcon("Tiles/" + t.getName()), t.x, t.y, t.getIndex());
	}
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
	public int getIndex() {
		return tileIndex;
	}
	public String getName() {
		return this.label.getText();
	}
}