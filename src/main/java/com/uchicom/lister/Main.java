// (c) 2016 uchicom
package com.uchicom.lister;

import java.io.File;

import javax.swing.SwingUtilities;

import com.uchicom.lister.window.ListerFrame;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			File file = null;
			if (args.length == 1) {
				file = new File(args[0]);
			} else {
				file = new File("conf", "lister.properties");
			}
			try {
				ListerFrame frame = new ListerFrame(file);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
