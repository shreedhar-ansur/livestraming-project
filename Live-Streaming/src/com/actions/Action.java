package com.actions;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Action {
	public void loadFiles(JTextArea jta) {
		// TODO Auto-generated method stub
		File[] file = new File("ServerDB").listFiles();
		for (int i = 0; i < file.length; i++) {
			jta.append(file[i].getName() + "\n");
		}

	}

	public String getSource() {
		// TODO Auto-generated method stub
		return new SourceAndPort().getUser();
	}

	public int getPort() {
		// TODO Auto-generated method stub
		return new SourceAndPort().getPort();
	}

	public void sendREQ(String source, String text, int port, String text2) {
		// TODO Auto-generated method stub
		try {
			Socket soc = new Socket("localhost", 4576);
			ObjectOutputStream oos = new ObjectOutputStream(soc
					.getOutputStream());
			oos.writeObject("REQ");
			oos.writeObject(source);
			oos.writeObject(port);
			oos.writeObject(text);
			oos.writeObject(text2);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void mediaPlayer(final String path, final JPanel panel) {
		new Thread() {
			public void run() {
				try {
					Player p = Manager.createRealizedPlayer(new File(path)
							.toURL());
					Component ctrlpanel = p.getControlPanelComponent();
					Component player = p.getVisualComponent();
					player.setBounds(10, 20, 300, 170);
					ctrlpanel.setBounds(10, 191, 300, 20);
					panel.add(player);

					panel.add(ctrlpanel);
					panel.repaint();
					p.start();
					System.out.println(" Player Started");
				} catch (NoPlayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CannotRealizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void setProperty(String file, String source, String text) {
		// TODO Auto-generated method stub
		try {
			Properties properties = new Properties();
			FileOutputStream fos = new FileOutputStream(file, true);
			properties.setProperty(source, text);
			properties.store(fos, source);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getBW() {
		// TODO Auto-generated method stub
		return new SourceAndPort().getBW();
	}

}
