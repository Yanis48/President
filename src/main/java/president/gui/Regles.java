package president.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.google.common.io.CharStreams;

import president.Messages;

public class Regles {
	
	public static void afficherRegles() {
		JFrame frame = new JFrame(Messages.REGLES);
		JPanel htmlPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(htmlPanel);
		JPanel buttonPanel = new JPanel();
		
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setPreferredSize(new Dimension(900, 650));
		frame.getContentPane().add(scrollPane);
		frame.add(BorderLayout.SOUTH, buttonPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		try {
			String html = readResourceToString("/regles.html");
			JLabel label = new JLabel(html);
			htmlPanel.add(label);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JButton okButton = new JButton("Ok");
		buttonPanel.add(okButton);
		okButton.addActionListener(action -> frame.dispose());
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	private static String readResourceToString(String path) throws IOException {
		InputStream in = Regles.class.getResourceAsStream(path);
		if (in == null) {
			throw new IllegalArgumentException("Resource not found! " + path);
		}
		return CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
	}
}
