package by.sunnycore.recognition;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("recognition.app")
public class RecognitionComponent extends Thread {
	private Logger logger = Logger.getLogger(RecognitionComponent.class);

	private JFrame frame;
	private JFileChooser fileChooser;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void run() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setMinimumSize(new Dimension(800, 520));
		frame.setBounds(20, 20, 800, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		final JMenu fileMenu = new JMenu();
		fileMenu.setText("file");
		menuBar.add(fileMenu);

		initializeOpenFilesMenu(fileMenu);

		frame.setVisible(true);
	}

	private void initializeOpenFilesMenu(final JMenu fileMenu) {
		final JMenuItem openFileMenu = new JMenuItem();
		openFileMenu.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				int returnValue = openFileChooser(openFileMenu);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					logger.debug("ok button for select file created");
				} else if (returnValue == JFileChooser.CANCEL_OPTION) {
					logger.debug("user canceled selecting file");
				}
			}
		});
		openFileMenu.setText("open");
		fileMenu.add(openFileMenu);
		final JMenuItem saveFileMenu = new JMenuItem();
		saveFileMenu.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				logger.debug("save to file called");
			}
		});
		saveFileMenu.setText("save");
		fileMenu.add(saveFileMenu);
	}

	private int openFileChooser(final java.awt.Component openFileMenu) {
		logger.debug("opening file chooser dialog");
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File("C:/"));
		int returnValue = fileChooser.showOpenDialog(openFileMenu);
		return returnValue;
	}
}
