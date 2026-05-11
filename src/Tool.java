import View.View;
import controller.AsymmetricController;
import controller.ClassicalController;
import controller.HashController;
import controller.SymmetricController;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
public class Tool {
	public static void main(String[] args) {
		View view= new View();
        Security.addProvider(new BouncyCastleProvider());
		ClassicalController controller = new ClassicalController(view);
        SymmetricController symmetricController = new SymmetricController(view);
        AsymmetricController  asymmetricController = new AsymmetricController(view);
        HashController hashController = new HashController(view);

		view.setVisible(true);
	}

}
