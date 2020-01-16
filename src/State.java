
class State {
	private Tile[] positions;
	private int gCost, hCost;
	// Reference to parent
	private State parent;
	
	public State (Tile[] p) {
		positions = p;
	}
	public void setGCost(int c) {
		gCost = c;
	}
	public void setHCost(int c) {
		gCost = c;
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
	}
	public State getParent() {
		return parent;
	}
	public Tile[] getTiles() {
		return positions;
	}
}