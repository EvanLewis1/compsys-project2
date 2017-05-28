package mycontroller;


import controller.CarController;
import world.Car;

public class MyAIController extends CarController{
	WallFollower wallFollower;
	public int loop = 1;
	public String startPosition;
	public String prevPosition;
	public String state;

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
		
		if(state.equals("wallFollower")){
			wallFollower.update(delta);
		}
		else if (state.equals("Reverse")){
			Reverse.update(this);
		}
		else if(state.equals("3PointTurn")){
			3PointTurn.update(this);
		}
		else if(state.equals("UTurn")){
			UTurn.update();
		}
		else if (state.equals("Lava")){
			LavaHandler.update(this);
		}
		else if(state.equals("Mud")){
			MudHandler.update(this);
		}
		else if(state.equals("Grass")){
			GrassHandler.update();
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

}
