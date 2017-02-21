
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

	private JMenuBar menuBar; // 菜单列

	private TextDAO textDAO;
	private JFileChooser fileChooser;

	private JMenu fileMenu; // 文档菜单
	private JMenuItem menuOpen; // 打开文档（菜单项目）
	private JMenuItem menuSave;
	private JMenuItem menuSaveAs;
	private JMenuItem menuClose;

	private JMenu editMenu; // 文档菜单
	private JMenuItem menuCut; // 打开文档（菜单项目）
	private JMenuItem menuCopy;
	private JMenuItem menuPaste;

	private JMenu aboutMenu; // 文档菜单
	private JMenuItem menuAbout; // 打开文档（菜单项目）

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
		setTitle("新增纯文本文档");
		setSize(400, 300);
		initFileMenu();
		initEditMenu();
		initAboutMenu();
		initMenuBar();
		initTextArea();
		initStateBar();
		// 显示菜单
		PopupMenu = editMenu.getPopupMenu();
		// 开启文档对话框
		fileChooser = new JFileChooser();
	}

	private void initTextArea() {
		// 文字编辑区域
		textArea = new JTextArea();
		textArea.setFont(new Font("黑体", Font.PLAIN, 16));
		textArea.setLineWrap(true);// 自动换行
		JScrollPane pane1 = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane1, BorderLayout.CENTER);
	}

	private void initStateBar() {
		// 状态栏
		stareBar = new JLabel("未修改");
		stareBar.setHorizontalAlignment(SwingConstants.LEFT);
		stareBar.setBorder(BorderFactory.createLoweredBevelBorder());// 设置状态栏外观
		getContentPane().add(stareBar, BorderLayout.SOUTH);
	}

	private void initFileMenu() {
		// 设置文档菜单
		fileMenu = new JMenu("文档");
		menuOpen = new JMenuItem("开启文档");
		menuSave = new JMenuItem("保存文档");
		menuSaveAs = new JMenuItem("另存为…");
		menuClose = new JMenuItem("关闭");

		fileMenu.add(menuOpen);
		fileMenu.addSeparator();// 分割线
		fileMenu.add(menuSave);
		fileMenu.add(menuSaveAs);
		fileMenu.addSeparator();// 分割线
		fileMenu.add(menuClose);
	}

	private void initEditMenu() {
		// 设置编辑菜单
		editMenu = new JMenu("编辑");
		menuCut = new JMenuItem("剪切");
		menuCopy = new JMenuItem("复制");
		menuPaste = new JMenuItem("粘贴");

		editMenu.add(menuCut);
		editMenu.add(menuCopy);
		editMenu.add(menuPaste);

	}

	private void initAboutMenu() {
		// 设置关于菜单
		aboutMenu = new JMenu("关于");
		menuAbout = new JMenuItem("关于Note");
		aboutMenu.add(menuAbout);

	}

	private void initMenuBar() {
		// 菜单列
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		// 设置菜单列
		setJMenuBar(menuBar);

	}

	private void initEventListeners() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initAccelerator();
		// 按下窗口关闭按钮事件处理
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				closeWindow(event);
			}

		});
		initMenuListener();

		// 编辑区键盘事件
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				jtextAreaActionperformed(event);
			}
		});

		// 编辑区鼠标事件
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
			JOptionPane.showOptionDialog(null, "Note 1.0  小宇科技出品", "关于 Note", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,

					null, null, null);
		});

	}

	private void closeWindow(WindowEvent event) {
		closeFile(new ActionEvent(event.getSource(), event.getID(), "windowsClosing"));
	}

	private void openFile(ActionEvent event) {
		if (stareBar.getText().equals("未修改")) {
			showfileDialog();
		} else {
			int option = JOptionPane.showConfirmDialog(null, "文档已修改，是否存储", "存储文档", JOptionPane.YES_NO_OPTION,
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
		int option = fileChooser.showDialog(null, null);// 文档选取对话框
		// 用户按下确认键

		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				setTitle(fileChooser.getSelectedFile().toString());
				textArea.setText("");
				stareBar.setText("未修改");
				String text = textDAO.read(fileChooser.getSelectedFile().toString());
				textArea.setText(text);
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(null, e.toString(), "文档打开失败", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void saveFile(ActionEvent event) {
		// 从标题栏取得文件名
		Path path = Paths.get(getTitle());
		if (Files.notExists(path)) {
			saveFileAs(event);
		} else {
			try {
				// 存储文件
				textDAO.save(path.toString(), textArea.getText());
				// 设置状态栏为未修改
				stareBar.setText("未修改");
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(null, e.toString(), "写入文档失败", JOptionPane.ERROR_MESSAGE, null);
			}
		}
	}

	private void saveFileAs(ActionEvent event) {
		// 显示文档对话框
		int option = fileChooser.showDialog(null, null);
		// 如果确认选取文档
		if (option == JFileChooser.APPROVE_OPTION) {
			// 在标题栏上设定文件名
			setTitle(fileChooser.getSelectedFile().toString());
			// 建立文档
			textDAO.create(fileChooser.getSelectedFile().toString());
			saveFile(event);
		}
	}

	private void closeFile(ActionEvent event) {
		if (stareBar.getText().equals("未修改")) {
			dispose();
		} else {
			int option = JOptionPane.showConfirmDialog(null, "文档已经修改，是否存储", "存储文档", JOptionPane.YES_NO_CANCEL_OPTION,
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
		stareBar.setText("已修改");
		PopupMenu.setVisible(false);
	}

	private void copy(ActionEvent event) {
		textArea.copy();
		PopupMenu.setVisible(false);
	}

	private void paste(ActionEvent event) {
		textArea.paste();
		stareBar.setText("已修改");
		PopupMenu.setVisible(false);
	}

	private void jtextAreaActionperformed(KeyEvent event) {
		stareBar.setText("已修改");
	}

	private void initAccelerator() {
		// 快捷键设置
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
