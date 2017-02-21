
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


public class Jnote extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar; // �˵���

	private TextDAO textDAO;
	private JFileChooser fileChooser;

	private JMenu fileMenu; // �ĵ��˵�
	private JMenuItem menuOpen; // ���ĵ����˵���Ŀ��
	private JMenuItem menuSave;
	private JMenuItem menuSaveAs;
	private JMenuItem menuClose;

	private JMenu editMenu; // �ĵ��˵�
	private JMenuItem menuCut; // ���ĵ����˵���Ŀ��
	private JMenuItem menuCopy;
	private JMenuItem menuPaste;

	private JMenu aboutMenu; // �ĵ��˵�
	private JMenuItem menuAbout; // ���ĵ����˵���Ŀ��

	private JTextArea textArea;
	private JLabel stareBar;
	private JPopupMenu PopupMenu;

	public Jnote(TextDAO textDAO) {
		this();
		this.textDAO = textDAO;

	}

	protected Jnote() {
		initComponents();
		initEventListeners();
	}

	private void initComponents() {
		setTitle("�������ı��ĵ�");
		setSize(400, 300);
		initFileMenu();
		initEditMenu();
		initAboutMenu();
		initMenuBar();
		initTextArea();
		initStateBar();
		// ��ʾ�˵�
		PopupMenu = editMenu.getPopupMenu();
		// �����ĵ��Ի���
		fileChooser = new JFileChooser();
	}

	private void initTextArea() {
		// ���ֱ༭����
		textArea = new JTextArea();
		textArea.setFont(new Font("����", Font.PLAIN, 16));
		textArea.setLineWrap(true);// �Զ�����
		JScrollPane pane1 = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane1, BorderLayout.CENTER);
	}

	private void initStateBar() {
		// ״̬��
		stareBar = new JLabel("δ�޸�");
		stareBar.setHorizontalAlignment(SwingConstants.LEFT);
		stareBar.setBorder(BorderFactory.createLoweredBevelBorder());// ����״̬�����
		getContentPane().add(stareBar, BorderLayout.SOUTH);
	}

	private void initFileMenu() {
		// �����ĵ��˵�
		fileMenu = new JMenu("�ĵ�");
		menuOpen = new JMenuItem("�����ĵ�");
		menuSave = new JMenuItem("�����ĵ�");
		menuSaveAs = new JMenuItem("���Ϊ��");
		menuClose = new JMenuItem("�ر�");

		fileMenu.add(menuOpen);
		fileMenu.addSeparator();// �ָ���
		fileMenu.add(menuSave);
		fileMenu.add(menuSaveAs);
		fileMenu.addSeparator();// �ָ���
		fileMenu.add(menuClose);
	}

	private void initEditMenu() {
		// ���ñ༭�˵�
		editMenu = new JMenu("�༭");
		menuCut = new JMenuItem("����");
		menuCopy = new JMenuItem("����");
		menuPaste = new JMenuItem("ճ��");

		editMenu.add(menuCut);
		editMenu.add(menuCopy);
		editMenu.add(menuPaste);

	}

	private void initAboutMenu() {
		// ���ù��ڲ˵�
		aboutMenu = new JMenu("����");
		menuAbout = new JMenuItem("����Note");
		aboutMenu.add(menuAbout);

	}

	private void initMenuBar() {
		// �˵���
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		// ���ò˵���
		setJMenuBar(menuBar);

	}

	private void initEventListeners() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initAccelerator();
		// ���´��ڹرհ�ť�¼�����
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				closeWindow(event);
			}

		});
		initMenuListener();

		// �༭�������¼�
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				jtextAreaActionperformed(event);
			}
		});

		// �༭������¼�
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) {
					PopupMenu.show(editMenu, event.getX(), event.getY());
				}
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					PopupMenu.setVisible(false);
				}
			}

		}

		);

	}

	private void initMenuListener() {
		menuOpen.addActionListener(this::openFile);
		menuSave.addActionListener(this::saveFile);
		menuSaveAs.addActionListener(this::saveFileAs);
		menuClose.addActionListener(this::closeFile);
		menuCut.addActionListener(this::cut);
		menuCopy.addActionListener(this::copy);
		menuPaste.addActionListener(this::paste);
		menuAbout.addActionListener(event -> {
			JOptionPane.showOptionDialog(null, "Note 1.0  С��Ƽ���Ʒ", "���� Note", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,

					null, null, null);
		});

	}

	private void closeWindow(WindowEvent event) {
		closeFile(new ActionEvent(event.getSource(), event.getID(), "windowsClosing"));
	}

	private void openFile(ActionEvent event) {
		if (stareBar.getText().equals("δ�޸�")) {
			showfileDialog();
		} else {
			int option = JOptionPane.showConfirmDialog(null, "�ĵ����޸ģ��Ƿ�洢", "�洢�ĵ�", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null);
			switch (option) {
			case JOptionPane.YES_OPTION:
				saveFile(event);
				break;

			case JOptionPane.NO_OPTION:
				showfileDialog();
				break;
			}
		}
	}

	private void showfileDialog() {
		int option = fileChooser.showDialog(null, null);// �ĵ�ѡȡ�Ի���
		// �û�����ȷ�ϼ�

		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				setTitle(fileChooser.getSelectedFile().toString());
				textArea.setText("");
				stareBar.setText("δ�޸�");
				String text = textDAO.read(fileChooser.getSelectedFile().toString());
				textArea.setText(text);
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(null, e.toString(), "�ĵ���ʧ��", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void saveFile(ActionEvent event) {
		// �ӱ�����ȡ���ļ���
		Path path = Paths.get(getTitle());
		if (Files.notExists(path)) {
			saveFileAs(event);
		} else {
			try {
				// �洢�ļ�
				textDAO.save(path.toString(), textArea.getText());
				// ����״̬��Ϊδ�޸�
				stareBar.setText("δ�޸�");
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(null, e.toString(), "д���ĵ�ʧ��", JOptionPane.ERROR_MESSAGE, null);
			}
		}
	}

	private void saveFileAs(ActionEvent event) {
		// ��ʾ�ĵ��Ի���
		int option = fileChooser.showDialog(null, null);
		// ���ȷ��ѡȡ�ĵ�
		if (option == JFileChooser.APPROVE_OPTION) {
			// �ڱ��������趨�ļ���
			setTitle(fileChooser.getSelectedFile().toString());
			// �����ĵ�
			textDAO.create(fileChooser.getSelectedFile().toString());
			saveFile(event);
		}
	}

	private void closeFile(ActionEvent event) {
		if (stareBar.getText().equals("δ�޸�")) {
			dispose();
		} else {
			int option = JOptionPane.showConfirmDialog(null, "�ĵ��Ѿ��޸ģ��Ƿ�洢", "�洢�ĵ�", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, null);
			switch (option) {
			case JOptionPane.YES_OPTION:
				saveFile(event);
				break;
			case JOptionPane.NO_OPTION:
				dispose();
			}

		}
	}

	private void cut(ActionEvent event) {
		textArea.cut();
		stareBar.setText("���޸�");
		PopupMenu.setVisible(false);
	}

	private void copy(ActionEvent event) {
		textArea.copy();
		PopupMenu.setVisible(false);
	}

	private void paste(ActionEvent event) {
		textArea.paste();
		stareBar.setText("���޸�");
		PopupMenu.setVisible(false);
	}

	private void jtextAreaActionperformed(KeyEvent event) {
		stareBar.setText("���޸�");
	}

	private void initAccelerator() {
		// ��ݼ�����
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			new Jnote(new FileTextDAO()).setVisible(true);

		});
	}
}
