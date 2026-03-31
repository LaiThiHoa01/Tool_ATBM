import View.View;
import controller.CryptController;

public class Tool {
	public static void main(String[] args) {
		View view= new View();
		CryptController controller = new CryptController(view);
		view.setVisible(true);
	}

}
