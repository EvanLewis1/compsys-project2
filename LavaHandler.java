package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class LavaHandler implements TrapHandler{

	public static void update(MyAIController controller, float delta) {
		
		WorldSpatial.Direction orientation = controller.getOrientation();
		HashMap<Coordinate, MapTile> view = controller.getView();
		Coordinate nextTile = new Coordinate(controller.getPosition());
		Coordinate nextLeftTile = new Coordinate(controller.getPosition());
		Coordinate nextRightTile = new Coordinate(controller.getPosition());
		Coordinate LeftTile = new Coordinate(controller.getPosition());
		Coordinate RightTile = new Coordinate(controller.getPosition());
		
		
		switch(orientation){
		
		case EAST:
			nextTile.x+=2;
			
			nextLeftTile.x+=2;
			nextLeftTile.y++;
			
			LeftTile.y++;
			
			nextRightTile.x+=2;
			nextRightTile.y--;
			
			RightTile.y--;
			
			break;
		case WEST:
			nextTile.x-=2;
			
			nextLeftTile.x-=2;
			nextLeftTile.y--;
			
			LeftTile.y--;
			
			nextRightTile.x-=2;
			nextRightTile.y++;
			
			RightTile.y++;
			break;
		case NORTH:
			nextTile.y+=2;
			
			nextLeftTile.x--;
			nextLeftTile.y+=2;
			
			LeftTile.x--;
			
			nextRightTile.x++;
			nextRightTile.y+=2;
			
			RightTile.x++;
			break;
		case SOUTH:
			nextTile.y-=2;
			
			nextLeftTile.x++;
			nextLeftTile.y-=2;
			
			LeftTile.x++;
			
			nextRightTile.x--;
			nextRightTile.y-=2;
			
			RightTile.x--;
			break;
		default:
			break;
		}
			
		if (view.get(nextTile).getName().equals("Road")){
			controller.applyForwardAcceleration();			
		
		
		}
		else if (view.get(nextLeftTile).getName().equals("Road") || view.get(LeftTile).getName().equals("Road") ){
			controller.turnLeft(delta);
			
		}
		else if (view.get(nextRightTile).getName().equals("Road") || view.get(RightTile).getName().equals("Road")){
			controller.turnRight(delta);
			
		}
		else{
		controller.applyForwardAcceleration();	
		}
		}
		
		
		
	}


