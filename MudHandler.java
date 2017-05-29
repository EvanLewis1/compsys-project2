package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class MudHandler implements TrapHandler {
	static Coordinate dir;
	public static void setDir(double angle) {
		if (angle >= 45 || angle < 135) {
			// Right
			dir = new Coordinate(1,0);
		} else if (angle >= 135 && angle < 225) {
			// Down
			dir = new Coordinate(0,-1);
		} else if (angle >= 225 && angle < 315) {
			// Left
			dir = new Coordinate(-1,0);
		} else if (angle >= 315 || angle < 45) {
			// Up
			dir = new Coordinate(0,1);
		}
	}
	private static HashMap<Coordinate, MapTile> subsetCoord(HashMap<Coordinate, MapTile> view, Coordinate pos){
		HashMap<Coordinate, MapTile> returnMap = new HashMap<Coordinate, MapTile>();
		for (Coordinate tileCoord : view.keySet()) {
			System.out.println("");
			if (((tileCoord.x - pos.x) * dir.x) >= (tileCoord.x - pos.x) && ((tileCoord.y - pos.y) * dir.y) >= (tileCoord.y - pos.y)) {
				// Tile is in front of you.
				returnMap.put(tileCoord, view.get(tileCoord));
			}
		}
		return returnMap;
	}
	
	public static void update(MyAIController controller, float delta) {
		Coordinate pos = new Coordinate(controller.getPosition());
		HashMap<Coordinate, MapTile> currentView = controller.getView();
		HashMap<Coordinate, MapTile> forwardView = new HashMap<Coordinate, MapTile>();
		forwardView = subsetCoord(currentView, pos);
		float closestDist = Float.MAX_VALUE;
		MapTile closestTile = null;
		Coordinate closestCoord = null;
		for (Coordinate tileCoord : forwardView.keySet()) {
			if (!forwardView.get(tileCoord).getName().equals("Wall") && !forwardView.get(tileCoord).getName().equals("Trap")) {
				if (controller.tileDistance(tileCoord, pos) < closestDist && tileCoord.x != pos.x && tileCoord.y != pos.y) {
					closestCoord = tileCoord;
					closestTile = forwardView.get(tileCoord);
				}
			}
		}
		
		if (closestTile == null) {
			controller.wallFollower.update(delta);
		} else {
			double desiredAngle = ((Math.atan((double)(closestCoord.y - pos.y) / (double)(closestCoord.x - pos.x))) + (Math.PI/2.0)) * (180.0 / Math.PI);
			double currentAngle = controller.getAngle();
			WorldSpatial.RelativeDirection turnDir;
			if (currentAngle < desiredAngle) {
				if (Math.abs(currentAngle - desiredAngle)<180) {
					turnDir = WorldSpatial.RelativeDirection.LEFT;
				} else {
					turnDir = WorldSpatial.RelativeDirection.RIGHT;
				}
			} else {
				if (Math.abs(currentAngle - desiredAngle)<180) {
					turnDir = WorldSpatial.RelativeDirection.RIGHT;
				} else {
					turnDir = WorldSpatial.RelativeDirection.LEFT;
				}
			}
			System.out.println("Closest open tile = " + closestCoord.toString());
			if (!controller.peek(controller.getRawVelocity(), controller.getAngle(), turnDir, delta).getReachable()) {
				controller.wallFollower.update(delta);
			} else if (turnDir == WorldSpatial.RelativeDirection.LEFT) {
				controller.turnLeft(delta);
			} else {
				controller.turnRight(delta);
			}
		}
	}
}
