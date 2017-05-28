package mycontroller;


import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAIController extends CarController{
	WallFollower wallFollower;
	public int loop = 1;
	public String startPosition;
	public String prevPosition;
	public String state;
	private int wallSensitivity = 2;

	public MyAIController(Car car) {
		super(car);
		this.prevPosition  = car.getPosition();
		this.startPosition = car.getPosition();
		this.state = "wallFollower";
		
		this.wallFollower = new WallFollower(this);
	}

	@Override
	public void update(float delta) {
		if (loopFinished()){
			loop++;
			System.out.println(loop);
			
		}
		
		//resetstate();
		
		if(state.equals("wallFollower")){
			wallFollower.update(delta);
		}
		else if (state.equals("Reverse")){
			Reverse.update(this, delta);
		}
		else if(state.equals("3PointTurn")){
			ThreePointTurn.update(this, delta);
		}
		else if(state.equals("UTurn")){
			UTurn.update(this, delta);
		}
		else if (state.equals("Lava")){
			LavaHandler.update(this, delta);
		}
		else if(state.equals("Mud")){
			MudHandler.update(this, delta);
		}
		else if(state.equals("Grass")){
			GrassHandler.update(this, delta);
		}
		
	}

	/*private void resetstate() {
		
		
		HashMap<Coordinate, MapTile> currentView = getView();
		Coordinate currentPosition = new Coordinate(getPosition());
				for(int i = 0; i <= 2; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
					if(tile.getName().equals("Wall")|| loop == 1 && tile.getName().equals("Trap")){
						
		
		
		if currentView.get(key)
		
	}*/
	
	/**
	 * Check if you have a wall in front of you!
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	public boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
		switch(orientation){
		case EAST:
			return checkEast(currentView);
		case NORTH:
			return checkNorth(currentView);
		case SOUTH:
			return checkSouth(currentView);
		case WEST:
			return checkWest(currentView);
		default:
			return false;
		
		}
	}

	private boolean loopFinished() {
		if(!this.prevPosition.equals(this.getPosition())){
			if(this.startPosition.equals(this.getPosition())){
				this.prevPosition = this.getPosition();
				return true;
			}
			this.prevPosition = this.getPosition();	
		}
		
		return false;
	}
	

	/**
	 * Method below just iterates through the list and check in the correct coordinates.
	 * i.e. Given your current position is 10,10
	 * checkEast will check up to wallSensitivity amount of tiles to the right.
	 * checkWest will check up to wallSensitivity amount of tiles to the left.
	 * checkNorth will check up to wallSensitivity amount of tiles to the top.
	 * checkSouth will check up to wallSensitivity amount of tiles below.
	 */
	public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
		//	System.out.println(tile.getName());
			if(tile.getName().equals("Wall") || loop == 1 && tile.getName().equals("Trap")){// || loop == 1 && tile.){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			if(tile.getName().equals("Wall") || loop == 1 && tile.getName().equals("Trap")){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			if(tile.getName().equals("Wall")|| (loop == 1 && tile.getName().equals("Trap"))){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			if(tile.getName().equals("Wall")|| loop == 1 && tile.getName().equals("Trap")){
				
				return true;
			}
		}
		return false;
	}
	

}
