/*
  * World: 9 spaces, 1 empty.
 * We need a window that is squared in length.
 * We need a double array to represent the places on the board.
 * I need an image array
 */
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.util.ArrayList;
import java.util.Random;
public class Main
{
	public static void main(String[] args)
	{
		Window window = new Window("Tile Puzzle Game");
		window.run();
	}
}
class Window extends JFrame
{

	/**
	 * Generated code
	 */
	private static final long serialVersionUID = 1L;
	private boolean imagesLoaded;
	private Tile[] tiles;
	private Location[] locations;
	private JPanel contentPane;
	Tile myTile;
	Image  myImage;

	public Window(String s)
	{
		super(s);
		tiles = new Tile[9];
		imagesLoaded = false;
		
		contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
		
		locations = new Location[9];
		
		// Top 3 locations
		locations[0] = new Location(15, 40);
		locations[1] = new Location(150, 40);
		locations[2] = new Location(285, 40);
		
		// Middle 3 locations
		locations[3] = new Location(15, 175);
		locations[4] = new Location(150, 175);
		locations[5] = new Location(285, 175);
		// Middle 3 locations
		locations[6] = new Location(15, 310);
		locations[7] = new Location(150, 310);
		locations[8] = new Location(285, 310);
	}
	public void run() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(430, 455);
		
		try {
			loadImages();
			repaint();
		} catch (Exception e) {
			//
		}
	}
	public void paint(Graphics g){
		// Traverse through each image, check if it's set to visible
		if (imagesLoaded) {
			for (int i = 0; i < tiles.length; i++) {
				if (tiles[i].visible) {
					g.drawImage(tiles[i].image, tiles[i].x, tiles[i].y, null);
				}
			}
		}
	}
	// 
	public void loadImages() {
		
		String[] imageNames = {"Tiles/TL.png", "Tiles/TC.png", "Tiles/TR.png",
				"Tiles/ML.png", "Tiles/MC.png", "Tiles/MR.png",
				"Tiles/BL.png", "Tiles/BC.png","Tiles/BR.png"};
		
		Random rand = new Random();
		int randomLocation;
		int randomSkipped = rand.nextInt(9);
		
		// List of indices to index the Locations 
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		// Counter for the tile array
		int counter = 0;
		
		// Initializes the possible indices as a set of 0-9
		for (int i = 0; i < 9; i++) {
			indices.add(i);
		}
		while (!indices.isEmpty()) {
			// Gets a random index within the index set.
			randomLocation = rand.nextInt(indices.size());
			
			// Checks whether the randomly chosen empty spot is currently picked, sets it invisible
			if (randomSkipped == counter) {
				tiles[counter] = new Tile(imageNames[counter], locations[indices.get(randomLocation)].x, 
						locations[indices.get(randomLocation)].y, false);
				locations[indices.get(randomLocation)].setOccupied(false);
			} else {
				// Grabs on of the remaining open slots and marks it as occupied, sets the tile as visible
				tiles[counter] = new Tile(imageNames[counter], locations[indices.get(randomLocation)].x, 
						locations[indices.get(randomLocation)].y, true);
				locations[indices.get(randomLocation)].setOccupied(true);
			}
			counter++;
			indices.remove(randomLocation);
		}
		// Add the JLabels to the content pane
		for (Tile t: tiles) {
			contentPane.add(t.label);
		}
		imagesLoaded = true;
	}
	class Tile{
		Image image;
		int x, y;
		boolean visible;
		JLabel label;
		// String is the file name
		public Tile(String name, int hor, int ver, boolean visibility) {
			image = new ImageIcon(name).getImage();
			label = new JLabel(new ImageIcon(name));
			visible = visibility;
			
			x = hor;
			y = ver;
			
			// Adding mouse event listener
			label.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("Image: " + name + " clicked!");
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
	}
	private class Location {
		private boolean occupied;
		int x, y;
		
		Location (int hor, int ver) {
			x  = hor;
			y = ver;
		}
		public void setOccupied (boolean o) {
			occupied = o;
		}
	}
}