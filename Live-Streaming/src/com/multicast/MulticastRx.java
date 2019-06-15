package com.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.design.Admin;
public class MulticastRx extends Thread {
	private InetAddress ia;
	public HashMap<String, Long> hm = new HashMap<String, Long>();
	Admin node;

	public MulticastRx(Admin node) {
		this.node = node;
		start();
		availability();
	}

	private void availability() {
		// TODO Auto-generated method stub
		new Thread(this) {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (true) {
					try {
						Date date = new Date();
						long pre = date.getTime();
						Thread.sleep(1000);
						Set<String> set = hm.keySet();
						Iterator<String> it = set.iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							long time = hm.get(key);
							if (time < pre) {
								hm.remove(key);
								refresh(hm);
								break;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						continue;
					}
				}
			}

		}.start();
	}

	private void refresh(HashMap<String, Long> hm) {
		// TODO Auto-generated method stub
		node.jtaPeers.setText("");
		Set<String> set = hm.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] data = key.split(",");
			String nnode = data[0];
			String nsys = data[2];
			node.jtaPeers.append(nnode + " " + nsys + "\n");

		}
	}

	private void print(String str) {
		// TODO Auto-generated method stub
		if (hm.containsKey(str)) {
			hm.put(str, new Date().getTime());
		} else {
				node.jtaPeers.append(str + "\n");
				hm.put(str, new Date().getTime());
				System.out.println(hm);
			
		}

	}

	public void run() {
		try {
			while (true) {
				ia = InetAddress.getByName("228.5.6.7");
				MulticastSocket ms = new MulticastSocket(4444);
				ms.joinGroup(ia);
				byte[] b = new byte[512];
				DatagramPacket dp = new DatagramPacket(b, b.length);
				ms.receive(dp);
				String str = new String(dp.getData());
				str = str.trim();
				print(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}