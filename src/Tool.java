import View.View;
import controller.CryptController;
import controller.SymmetricController;

public class Tool {
	public static void main(String[] args) {
		View view= new View();
		CryptController controller = new CryptController(view);
        SymmetricController symmetricController = new SymmetricController(view);
		view.setVisible(true);
	}

}
