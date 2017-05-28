package mycontroller;

import controller.CarController;
import world.Car;

public class MyAIController extends CarController{
	WallFollower wallFollower;

	public MyAIController(Car car) {
		super(car);
		this.wallFollower = new WallFollower(this);
	}

	@Override
	public void update(float delta) {
		wallFollower.update(delta);
		
	}

}
