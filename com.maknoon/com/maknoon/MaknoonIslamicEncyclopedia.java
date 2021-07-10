package com.maknoon;

/*
 * Maknoon Islamic Encyclopedia
 * Version 2.8
 * In 2.6 -> Removed all images/icons/update/setting/fonts/UserGuide...etc
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.net.*;

public class MaknoonIslamicEncyclopedia extends JFrame
{
	static private JTabbedPane tabbedPane;
	static boolean language = true; // i.e. default is arabic

	// Used to indicate the Dubai Courts version which consists of only mawareth
	public static final boolean dubaiCourtsVer = false;

	public MaknoonIslamicEncyclopedia()
	{
		new DefaultLanguage(this);

		final String[] translations = StreamConverter("language/" + ((language) ? "MaknoonIslamicEncyclopediaArabic.txt" : "MaknoonIslamicEncyclopediaEnglish.txt"));

		setTitle(translations[dubaiCourtsVer ? 5 : 0]);

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height - 40);

		if (dubaiCourtsVer)
			setContentPane(new MawarethSystem(this));
		else
		{
			tabbedPane = new JTabbedPane();
			setContentPane(tabbedPane);

			tabbedPane.insertTab(translations[1], null, new MawarethSystem(this), translations[2], 0);
			tabbedPane.insertTab(translations[3], null, new ZakatSystem(), translations[4], 1);
		}

		setJMenuBar(createMenuBar());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH))
			setExtendedState(Frame.MAXIMIZED_BOTH);

		if (language)
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // getContentPane().applyComponentOrientation() does not work for 'JOptionPane.showOptionDialog'

		setVisible(true);
	}

	private String[] translations;

	// Version 1.4, This variable is made global to give MawarethSystem the access to re-load the class when OverFlowException happens.
	JMenuItem mawarethMenuItem;
	//private JPanel zakatTabPanel, mawarethTabPanel;

	JMenuBar createMenuBar()
	{
		translations = StreamConverter("language/" + ((language) ? "createMenuBarArabic.txt" : "createMenuBarEnglish.txt"));

		final JMenuBar menuBar = new JMenuBar();

		/* Version 1.9, Moved to languageSetting() since we just need it when changing the language online
		if(language) tabbedPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		else tabbedPane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		*/

		final JMenu mFile = new JMenu(translations[18]);
		mawarethMenuItem = new JMenuItem(translations[19]);

		if (dubaiCourtsVer)
		{
			final ActionListener lst = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setContentPane(new MawarethSystem(MaknoonIslamicEncyclopedia.this));
					SwingUtilities.updateComponentTreeUI(MaknoonIslamicEncyclopedia.this);
				}
			};
			mawarethMenuItem.addActionListener(lst);
		}
		else
		{
			/* Version 1.8
			final JButton mawarethButton = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/mawareth.png")));
			mawarethButton.setToolTipText(translations[19]);
			mawarethButton.setMargin(new Insets(0, 0, 0, 0));
			mawarethButton.setOpaque(false);
	        mawarethButton.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/mawareth_tab_over.png")));
	        //mawarethButton.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/mawareth.png")));
	        mawarethButton.setBorderPainted(false);
	        mawarethButton.setContentAreaFilled(false);

	        mawarethTabPanel = new JPanel(new BorderLayout());
			mawarethTabPanel.add(new JLabel(translations[1]), BorderLayout.CENTER);
			mawarethTabPanel.add(mawarethButton, language?BorderLayout.EAST:BorderLayout.WEST);
			mawarethTabPanel.setOpaque(false);

			tabbedPane.setTabComponentAt(0, mawarethTabPanel);
			*/

			final ActionListener lst = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					tabbedPane.removeTabAt(0);
					tabbedPane.insertTab(translations[1], null, new MawarethSystem(MaknoonIslamicEncyclopedia.this), translations[2], 0);
					tabbedPane.setSelectedIndex(0);
					//tabbedPane.setTabComponentAt(0, mawarethTabPanel);
				}
			};
			mawarethMenuItem.addActionListener(lst);
			//mawarethButton.addActionListener(lst);

			tabbedPane.setTabComponentAt(0, new JLabel(translations[1]));
		}
		mFile.add(mawarethMenuItem);

		if (!dubaiCourtsVer)
		{
			final JMenuItem zakatMenuItem = new JMenuItem(translations[20]);

			/* Version 1.8
			final JButton zakatButton = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/zakat.png")));
			zakatButton.setToolTipText(translations[20]);
			zakatButton.setMargin(new Insets(0, 0, 0, 0));
			zakatButton.setOpaque(false);
	        zakatButton.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/zakat_tab_over.png")));
	        //zakatButton.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/zakat.png")));
	        zakatButton.setBorderPainted( false );
	        zakatButton.setContentAreaFilled(false);

	        zakatTabPanel = new JPanel(new BorderLayout());
			zakatTabPanel.add(new JLabel(translations[3]), BorderLayout.CENTER);
			zakatTabPanel.add(zakatButton, language?BorderLayout.EAST:BorderLayout.WEST);
			zakatTabPanel.setOpaque(false);
			tabbedPane.setTabComponentAt(1, zakatTabPanel);
			*/

			final ActionListener lst = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					tabbedPane.removeTabAt(1);
					tabbedPane.insertTab(translations[3], null, new ZakatSystem(), translations[4], 1);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setTabComponentAt(1, new JLabel(translations[3]));
					//tabbedPane.setTabComponentAt(1, zakatTabPanel);
				}
			};
			zakatMenuItem.addActionListener(lst);
			//zakatButton.addActionListener(lst);

			tabbedPane.setTabComponentAt(1, new JLabel(translations[3]));
			mFile.add(zakatMenuItem);
		}

		JMenuItem item = new JMenuItem(translations[22]);
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		mFile.addSeparator();
		mFile.add(item);
		menuBar.add(mFile);

		final JMenu mHelp = new JMenu(translations[28]);
		menuBar.add(mHelp);

		item = new JMenuItem(translations[29]);
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//try{Desktop.getDesktop().open(new File("UserGuide/mawarethIntroduction.htm"));} // Version 2.1
				try
				{
					Desktop.getDesktop().browse(new URI("https://www.maknoon.com/community/threads/%D8%A8%D8%B1%D9%86%D8%A7%D9%85%D8%AC-%D8%A7%D9%84%D9%85%D9%88%D8%A7%D8%B1%D9%8A%D8%AB-%D9%88%D8%A7%D9%84%D8%B2%D9%83%D8%A7%D8%A9.104/"));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		mHelp.add(item);

		item = new JMenuItem(translations[31]);
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showOptionDialog(MaknoonIslamicEncyclopedia.this,
						translations[23] + System.lineSeparator() +
								translations[24] + System.lineSeparator() +
								translations[25] + System.lineSeparator() +
								translations[26], translations[31], JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{translations[22]}, translations[22]);
			}
		});
		mHelp.add(item);
		menuBar.add(mHelp);

		//if (language)
			//menuBar.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//else
			//menuBar.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		return menuBar;
	}

	void languageSetting()
	{
		setJMenuBar(createMenuBar());
		if (dubaiCourtsVer)
		{
			setContentPane(new MawarethSystem(this));
			SwingUtilities.updateComponentTreeUI(this);
		}
		else
		{
			tabbedPane.removeAll();

			//tabbedPane.setTabComponentAt(0, mawarethTabPanel);
			tabbedPane.insertTab(translations[1], null, new MawarethSystem(this), translations[2], 0);

			//tabbedPane.setTabComponentAt(1, zakatTabPanel);
			tabbedPane.insertTab(translations[3], null, new ZakatSystem(), translations[4], 1);

			tabbedPane.setSelectedIndex(0);
		}

		if (language)
			// Version 1.9 getContentPane() instead of tabbedPane
			// Version 2.8, getContentPane() removed since it is not affecting with 'JOptionPane.showOptionDialog'
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		else
			applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		// Version 2.8
		revalidate();
		repaint();
	}

	// Read arabic translation files.
	public static String[] StreamConverter(final String filePath)
	{
		try
		{
			//final BufferedReader in = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8)); // if you want to use none-static StreamConverter
			//final BufferedReader in = new BufferedReader(new InputStreamReader (this.getClass().getClassLoader().getResourceAsStream(filePath), StandardCharsets.UTF_8));
			final BufferedReader in = new BufferedReader(new InputStreamReader (Objects.requireNonNull(MaknoonIslamicEncyclopedia.class.getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8)); // if you want to use static StreamConverter

			final Vector<String> lines = new Vector<>();
			while (in.ready()) lines.addElement(in.readLine());
			in.close();

			return lines.toArray(new String[0]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.exit(0);
		return null;
	}

	public static void main(String[] args)
	{
		//[ifJar]
		com.formdev.flatlaf.FlatLightLaf.setup();
		UIManager.put("TitlePane.menuBarEmbedded", false);
		UIManager.put("Button.arc", 0);
		UIManager.put("MenuItem.selectionType", "underline");
		UIManager.put("OptionPane.maxCharactersPerLine", 0);
		//[endJar]

		//[sysLnF]

		SwingUtilities.invokeLater(MaknoonIslamicEncyclopedia::new);
	}
}