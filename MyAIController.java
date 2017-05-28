package mycontroller;


import controller.CarController;
import world.Car;

public class MyAIController extends CarController{
	WallFollower wallFollower;
	public int loop = 1;
	public String startPosition;
	public String prevPosition;

	public MyAIController(Car car) {
		super(car);
		this.prevPosition  = car.getPosition();
		this.startPosition = car.getPosition();
		
		this.wallFollower = new WallFollower(this);
	}

	@Override
	public void update(float delta) {
		if (loopFinished()){
			loop++;
			System.out.println(loop);
			
		}
		
		wallFollower.update(delta);
		
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
