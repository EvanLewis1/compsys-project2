package mycontroller;

public class GrassHandler  implements TrapHandler{

	public static void update(MyAIController controller, float delta) {
		controller.applyForwardAcceleration();
		
	}
	
}
