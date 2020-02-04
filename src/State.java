
class State {
	private static Tile[] positions;
	private int gCost;
	private static int hCost;
	// Reference to parent
	private State parent;
	private int skipped; // skipped for this state
	private int parentSkipped;
	
	public State (Tile[] p) {
		// manually set the tiles to avoid referencing the same Tiles from the source.
		positions = p;
		// Set which tile is empty in this state
		skipped = setSkipped(p);
		// Set the H cost
		setHCost();
		// Set the G cost to infinity (represented with a large number)
		gCost = 1000;
	}
	public void setGCost(int c) {
		gCost = c;
	}
	private static void setHCost() {
		// Calculate the H cost using the Euclidean distance based on the values from Graph.map
		int hTotal = 0;
		for (int i = 0; i < positions.length; i++) {
			// Test if you're already at the h designated spot
			if (i == positions[i].getIndex()) {
				// Nothing is added to the hTotal
				//System.out.println("Tile " + positions[i].getName() + " at i : " + i + " the h cost is " + 0);
			} else {
				int horizontalDiff = Math.abs(Graph.map.get(i)[0] - Graph.map.get(positions[i].getIndex())[0]);
				int verticalDiff = Math.abs(Graph.map.get(i)[1] - Graph.map.get(positions[i].getIndex())[1]); 
				//System.out.println("Tile " + positions[i].getName() + " at i : " + i + " the h cost is " + sum);
				hTotal += horizontalDiff + verticalDiff;
			}
		}
		hCost = hTotal;
	}
	public int getGCost() {
		return gCost;
	}
	public int getHCost() {
		return hCost;
	}
	public int getFCost() {
		return gCost + hCost;
	}
	public void setParent(State p) {
		parent = p;
		parentSkipped = p.getSkipped();
	}
	public State getParent() {
		return parent;
	}
	private static int setSkipped(Tile[] input) {
		int ignoredIndex = -1;
		// Find the index of the ignored tile
		for (int i = 0; i < input.length; i++) {
			if (input[i].getName().compareTo("Ignored tile") == 0) {
				ignoredIndex = i;
			}
		}
		return ignoredIndex;
	}
	public int getSkipped() {
		return skipped;
	}
	public int getParentSkipped() {
		return parentSkipped;
	}
	public Tile[] getTiles() {
		return positions;
	}
	public void printState() {
		Graph.printVertices(this.getTiles());
	}
	@Override
    public boolean equals(Object o) {
		// If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        }
        /* Check if o is an instance of Complex or not 
        "null instanceof [type]" also returns false */
        if (!(o instanceof State)) { 
        	return false; 
        }
     // typecast o to State so that we can compare data members 
        State s = (State)o;
        
        // Test whether all the tiles of both states match in name and index
        for (int i = 0; i < s.getTiles().length; i++) {
        	if (this.getSkipped() != s.getSkipped() || this.getHCost() != s.getHCost()) {
        		return false;
        	}
        }
        
        return true;
	}
}