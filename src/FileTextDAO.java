import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTextDAO implements TextDAO {

	@Override
	public void create(String file) {
		

	}

	@Override
	public String read(String file) {
		byte[]datas=null;
		try {
			datas=Files.readAllBytes(Paths.get(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(FileTextDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return new String(datas);
	}

	@Override
	public void save(String file, String text) {
		

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() ->{
		new Jnote(new FileTextDAO()).setVisible(true);
			
		});

	}

}
