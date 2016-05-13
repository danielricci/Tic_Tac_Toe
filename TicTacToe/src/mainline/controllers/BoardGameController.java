package mainline.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

import mainline.GameInstance;
import mainline.models.PlayerModel;
import mainline.models.PlayerModel.Team;
import mainline.views.BoardGameView;
import mainline.views.BoardGameView.BoardPosition;

/**
 * Represents the controller that holds actions for serving the board game related 
 * queries 
 * 
 * @author Daniel Ricci<thedanny09@gmail.com>
 * 
 */
public class BoardGameController implements ActionListener {
	
	private ArrayList<PlayerModel> _players = new ArrayList<PlayerModel>();	
	private Queue<PlayerModel> _turns = new LinkedList<PlayerModel>();
	
	private boolean _isGameOver = false;

	private BoardGameView _view = null;
	
	private int _gridSize = 0;
		
	/**
	 * Constructs a new object of this class
	 */
	private BoardGameController() {
		registerController();
		addView(new BoardGameView());
	}
	
	/**
	 * Construct a new object of this class
	 * 
	 * @param source The component to attach this controllers view to 
	 */
	public BoardGameController(JPanel source) {
		this();
		source.add(_view);
	}
	
	/**
	 * Registers this controller
	 */
	// TODO - remove once we have factory (and all other ones, i wont put a comment in the others unless it cant be done and
	// just in this one)
	private void registerController() {
		GameInstance.getInstance().registerController(this);
	}
	
	public void createPlayers() {
		// TODO - this could be read from config XML
		_players.clear();
		_players.add(new PlayerModel("", Team.PlayerX));
		_players.add(new PlayerModel("", Team.PlayerY));
		
		// TODO - X goes first?
		for(PlayerModel player : _players) {
			_turns.add(player);
		}

		_gridSize = 3;	
	}
	
	/**
	 * Registers the views that wish to listen to the player models
	 * 
	 * @param controller The controllers we wish to use to register, we grab their views
	 */
	// TODO - can't we just pass an IView once it is created
	public void registerListener(ScoreBoardController controller) {
		for(PlayerModel model : _players) {
			model.addObserver(controller.getView());
		}	
	}
	
	/**
	 * Gets the list of available board positions
	 * 
	 * @param root The root of our panels
	 * 
	 * @return The list of available board positions
	 */
	public ArrayList<BoardPosition> getAvailableBoardPositions(JPanel root) {
		return null;
		/*ArrayList<BoardPosition> positions = new ArrayList<BoardPosition>();
		Component[] components = root.getComponents();
		
		for(Component component : components) {
			if(component instanceof BoardPosition) {
				populateNeighbouringPositions(_turns.peek().getPlayerConfiguration(), (BoardPosition)component, positions);
			}
		}
		
		return positions;*/
	}
	
	/**
	 * Gets the list of direct path associated neighbors based on a board position recursively 
	 * 
	 * @param configuration The configuration to go against such as a row or column based schema
	 * @param currentPosition The current position we are at
	 * @param allPositions The list of all the positions we are filling up
	 */
	/*
	private void populateNeighbouringPositions(Configuration configuration, BoardPosition currentPosition, ArrayList<BoardPosition> allPositions) {
		
		// If the currentPosition is not valid, is locked, or has already been looked at 
		// then stop processing
		if(currentPosition == null || currentPosition.isLocked() || allPositions.contains(currentPosition)) {
			return;
		}
		
		// Based on the configuration try and get the two neighboring 
		// positions and try to add them
		if(configuration == Configuration.ROW) {
			BoardPosition east = currentPosition.getNeighbourRight();
			BoardPosition west = currentPosition.getNeighbourLeft();
			if((east != null && !east.isLocked()) || (west != null && !west.isLocked())) {
				
				// Add our position
				allPositions.add(currentPosition);
				
				populateNeighbouringPositions(configuration, east, allPositions);
				populateNeighbouringPositions(configuration, west, allPositions);	
			}
		} else if(configuration == Configuration.COLUMN) {
			BoardPosition north = currentPosition.getNeighbourTop();
			BoardPosition south = currentPosition.getNeighbourBottom();
			if((north != null && !north.isLocked()) || (south != null && !south.isLocked())) {
				
				// Add our position
				allPositions.add(currentPosition);
				
				populateNeighbouringPositions(configuration, north, allPositions);
				populateNeighbouringPositions(configuration, south, allPositions);	
			}
		}
	}
*/
		
	/**
	 * Validates if a position specified is valid for use 
	 * 
	 * @param child The position inside of our root
	 *
	 * @return If the position is valid
	 */
	public boolean isValidPosition(BoardGameView.BoardPosition child) {
		if(getIsGameOver()) {
			return false;
		}
		
		System.out.println("TODO - implement BoardGameController::isValidPosition");
		return true;
		
		/*
		// Get the player playing
		PlayerModel player = _turns.peek();
		if(player != null) {
			
				boolean isValid = false;
				
				Configuration configuration = player.getPlayerConfiguration();
				if(configuration == Configuration.COLUMN) {
					BoardGameView.BoardPosition north = child.getNeighbourTop();
					BoardGameView.BoardPosition south = child.getNeighbourBottom();
					
					if(north != null && !north.isLocked() && north.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					} 
					
					if(south != null && !south.isLocked() && south.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					}
					
				} else if(configuration == Configuration.ROW) {
					BoardGameView.BoardPosition east = child.getNeighbourRight();
					BoardGameView.BoardPosition west = child.getNeighbourLeft();
	
					if(east != null && !east.isLocked() && east.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					} 
					
					if(west != null && !west.isLocked() && west.getOwner() == getCurrentPlayerIdentification()) {
						isValid = true;
					}
				}
				return isValid;
			}	
		}
		
		GameInstance.getInstance().addLog("Invalid token position...");
		return false;
*/
	}
	
	/**
	 * Gets the path of the players token, used for rendering
	 * 
	 * @return The players token path
	 */
	public String getPlayerToken() {
		return _turns.peek()._tokenPath;
	}
	
	/**
	 * Cycles the players
	 */
	private void nextPlayer() {
		_turns.add(_turns.poll());
	}
	
	/**
	 * Initiates the dialog component for setting up the players
	 *
	 * @return The result of the dialog box
	 */
	public boolean startPlayerSetup() {	
		boolean done = _view.showPlayerDialog();
		if(done) {
			_view.render();
		}
		return done;
	}
		
	/**
	 * Gets the size of the grid, easier than getting all the components and 
	 * getting its size.  If the size changes dynamically then just remove this 
	 * and get the list of components
	 * 
	 * @return The size of the grid in one dimension
	 */
	public int getGridSize() {
		return _gridSize;
	}
	
	/**
	 * Adds a view to this controller
	 * 
	 * @param view The view to add
	 */
	private void addView(BoardGameView view) {
		if(_view == null) {
			_view = view;
			_view.addController(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) { }

	/**
	 * Performs a move based on the tokens that the player has entered
	 * 
	 * @param root The game panel
	 */
	public void performMove(JPanel root) {
		
		// Make sure the game isn't finished before proceeding
		if(getIsGameOver()) {
			return;
		}
	    
		// Swap to the next player
		nextPlayer();
		
	    /*
			    ArrayList<BoardPosition> positions = getAvailableBoardPositions(root);
			    if(positions.size() == 0) {
			    	GameInstance.getInstance().addLog("Player " + _turns.peek().getName() + " has lost the game because there are no more moves left");
			    	GameInstance.getInstance().addLog("Player " + player.getName() + " has won the game");
			    	JOptionPane.showOptionDialog(
			    			_view, 
	    	    		 	"Player " + player.getName() + " has won the game",
			    			"Game Over", 
			    			JOptionPane.PLAIN_MESSAGE, 
			    			JOptionPane.INFORMATION_MESSAGE, 
			    			null, 
			    			new Object[]{"OK"}, 
			    			null);
					_isGameOver = true;
			    }
			    
			    _turns.peek().refresh();
			}
		}
		*/
	}
	
	/**
	 * Gets if the game is over
	 * @return
	 */
	private boolean getIsGameOver() {
		return _isGameOver;
	}
}