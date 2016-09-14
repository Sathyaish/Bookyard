import bookyard.client.LoginDialogEventLoop
import javax.swing.SwingUtilities;

public class Program {

    companion object {
        @JvmStatic public fun main(args: Array<String>) {

            SwingUtilities.invokeLater(LoginDialogEventLoop());

        }
    }
}