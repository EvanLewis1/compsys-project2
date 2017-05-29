package mycontroller;

public class MudHandler implements TrapHandler {

	public static void update(MyAIController controller, float delta) {
		controller.applyForwardAcceleration();
		
	}

}
