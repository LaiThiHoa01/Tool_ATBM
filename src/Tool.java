import View.View;
import controller.AsymmetricController;
import controller.CryptController;
import controller.SymmetricController;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
public class Tool {
	public static void main(String[] args) {
		View view= new View();
        Security.addProvider(new BouncyCastleProvider());
		CryptController controller = new CryptController(view);
        SymmetricController symmetricController = new SymmetricController(view);
        AsymmetricController  asymmetricController = new AsymmetricController(view);

		view.setVisible(true);
	}

}
