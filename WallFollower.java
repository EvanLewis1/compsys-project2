package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class WallFollower implements TraversalStrategy{
	

	
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 2;
	
	private boolean isFollowingWall = false; // This is initialised when the car sticks to a wall.
	private WorldSpatial.RelativeDirection lastTurnDirection = null; // Shows the last turn direction the car takes.
	private boolean isTurningLeft = false;
	private boolean isTurningRight = false; 
	private WorldSpatial.Direction previousState = null; // Keeps track of the previous state
	private MyAIController controller;
	
	// Car Speed to move at
	private final float CAR_SPEED = 3;
	
	// Offset used to differentiate between 0 and 360 degrees
	private int EAST_THRESHOLD = 3;
	
	public WallFollower(MyAIController controller){
	this.controller = controller;	
	
	}
	

	public void update(float delta) {
		
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = controller.getView();
		
		
		checkStateChange();


		// If you are not following a wall initially, find a wall to stick to!
		if(!isFollowingWall){
			if(controller.getVelocity() < CAR_SPEED){
				controller.applyForwardAcceleration();
			}
			// Turn towards the north
			if(!controller.getOrientation().equals(WorldSpatial.Direction.NORTH)){
				lastTurnDirection = WorldSpatial.RelativeDirection.LEFT;
				applyLeftTurn(controller.getOrientation(),delta);
			}
			if(controller.checkNorth(currentView)){
				// Turn right until we go back to east!
				if(!controller.getOrientation().equals(WorldSpatial.Direction.EAST)){
					lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
					applyRightTurn(controller.getOrientation(),delta);
				}
				else{
					isFollowingWall = true;
				}
			}
		}
		// Once the car is already stuck to a wall, apply the following logic
		else{
			
			// Readjust the car if it is misaligned.
			readjust(lastTurnDirection,delta);
			
			if(isTurningRight){
				applyRightTurn(controller.getOrientation(),delta);
			}
			else if(isTurningLeft){
				// Apply the left turn if you are not currently near a wall.
				if(!checkFollowingWall(controller.getOrientation(),currentView)){
					applyLeftTurn(controller.getOrientation(),delta);
				}
				else{
					isTurningLeft = false;
				}
			}
			// Try to determine whether or not the car is next to a wall.
			else if(checkFollowingWall(controller.getOrientation(),currentView)){
				// Maintain some velocity
				if(controller.getVelocity() < CAR_SPEED){
					controller.applyForwardAcceleration();
				}
				// If there is wall ahead, turn right!
				if(controller.checkWallAhead(controller.getOrientation(),currentView)){
					lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
					isTurningRight = true;				
					
				}

			}
			// This indicates that I can do a left turn if I am not turning right
			else{
				lastTurnDirection = WorldSpatial.RelativeDirection.LEFT;
				isTurningLeft = true;
			}
		}
		


	}
	
	/**
	 * Readjust the car to the orientation we are in.
	 * @param lastTurnDirection
	 * @param delta
	 */
	private void readjust(WorldSpatial.RelativeDirection lastTurnDirection, float delta) {
		if(lastTurnDirection != null){
			if(!isTurningRight && lastTurnDirection.equals(WorldSpatial.RelativeDirection.RIGHT)){
				adjustRight(controller.getOrientation(),delta);
			}
			else if(!isTurningLeft && lastTurnDirection.equals(WorldSpatial.RelativeDirection.LEFT)){
				adjustLeft(controller.getOrientation(),delta);
			}
		}
		
	}
	
	/**
	 * Try to orient myself to a degree that I was supposed to be at if I am
	 * misaligned.
	 */
	private void adjustLeft(WorldSpatial.Direction orientation, float delta) {
		
		switch(orientation){
		case EAST:
			if(controller.getAngle() > WorldSpatial.EAST_DEGREE_MIN+EAST_THRESHOLD){
				controller.turnRight(delta);
			}
			break;
		case NORTH:
			if(controller.getAngle() > WorldSpatial.NORTH_DEGREE){
				controller.turnRight(delta);
			}
			break;
		case SOUTH:
			if(controller.getAngle() > WorldSpatial.SOUTH_DEGREE){
				controller.turnRight(delta);
			}
			break;
		case WEST:
			if(controller.getAngle() > WorldSpatial.WEST_DEGREE){
				controller.turnRight(delta);
			}
			break;
			
		default:
			break;
		}
		
	}

	private void adjustRight(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(controller.getAngle() > WorldSpatial.SOUTH_DEGREE && controller.getAngle() < WorldSpatial.EAST_DEGREE_MAX){
				controller.turnLeft(delta);
			}
			break;
		case NORTH:
			if(controller.getAngle() < WorldSpatial.NORTH_DEGREE){
				controller.turnLeft(delta);
			}
			break;
		case SOUTH:
			if(controller.getAngle() < WorldSpatial.SOUTH_DEGREE){
				controller.turnLeft(delta);
			}
			break;
		case WEST:
			if(controller.getAngle() < WorldSpatial.WEST_DEGREE){
				controller.turnLeft(delta);
			}
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * Checks whether the car's state has changed or not, stops turning if it
	 *  already has.
	 */
	private void checkStateChange() {
		if(previousState == null){
			previousState = controller.getOrientation();
		}
		else{
			if(previousState != controller.getOrientation()){
				if(isTurningLeft){
					isTurningLeft = false;
				}
				if(isTurningRight){
					isTurningRight = false;
				}
				previousState = controller.getOrientation();
			}
		}
	}
	
	/**
	 * Turn the car counter clock wise (think of a compass going counter clock-wise)
	 */
	private void applyLeftTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.NORTH)){
				controller.turnLeft(delta);
			}
			break;
		case NORTH:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.WEST)){
				controller.turnLeft(delta);
			}
			break;
		case SOUTH:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.EAST)){
				controller.turnLeft(delta);
			}
			break;
		case WEST:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				controller.turnLeft(delta);
			}
			break;
		default:
			break;
		
		}
		
	}
	
	/**
	 * Turn the car clock wise (think of a compass going clock-wise)
	 */
	private void applyRightTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				controller.turnRight(delta);
			}
			break;
		case NORTH:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.EAST)){
				controller.turnRight(delta);
			}
			break;
		case SOUTH:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.WEST)){
				controller.turnRight(delta);
			}
			break;
		case WEST:
			if(!controller.getOrientation().equals(WorldSpatial.Direction.NORTH)){
				controller.turnRight(delta);
			}
			break;
		default:
			break;
		
		}
		
	}


	
	/**
	 * Check if the wall is on your left hand side given your orientation
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		
		switch(orientation){
		case EAST:
			return controller.checkNorth(currentView);
		case NORTH:
			return controller.checkWest(currentView);
		case SOUTH:
			return controller.checkEast(currentView);
		case WEST:
			return controller.checkSouth(currentView);
		default:
			return false;
		}
		
	}
	

}
	
	