package mycontroller;

public class UTurn  implements TurnMethod{

	public static void update(MyAIController controller, float delta) {
		controller.turnRight(delta);
		if (controller.getVelocity() > 2){
			controller.applyReverseAcceleration();
			
		}
		
	}

}
