package com.maknoon;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;

class ZakatSystem extends JDesktopPane
{
	// Version 1.6,  This enum will be used to indicate the madhab Name instead of integers.
	enum madhabName
	{GUMHOUR, MALIKI, HANBALI, SHAFEE, HANAFI, ALL}

	private madhabName madhab = madhabName.GUMHOUR;

	private int camelNo, camelShaahRequired, camelBintLaboonRequired, camelBinLaboonRequired, camelBintMakhadRequired, camelHuqaRequired, camelJadahRequired;
	private int cowNo, cowTabeeaOrTabeeahRequired;
	// cowMassunahRequired done float for hanafi madhab where it can be not integer number of cows !!!
	private float cowMassunahRequired;
	private int sheepNo, sheepRequired;
	private int horseNo, horseMoneyRequired, horseCost;

	private float goldAmount, goldRequired, silverAmount, silverRequired;
	private int goldFinenessUnit = 24, silverFinenessUnit = 1000; // Version 2.0, Silver adjustment

	private float oreAmount, oreRequired;
	private float friendlyDebtAmount, uglyDebtAmount, friendlyDebtRequired, uglyDebtRequired;
	private float goldValue, silverValue;
	private float businessAmount, businessRequired;
	private float freePlanetAmount, costPlanetAmount, freePlanetRequired, costPlanetRequired;

	private int honeyAmount;
	private float honeyRequired;

	private static final Insets insets = new Insets(0, 0, 0, 0);

	ZakatSystem()
	{
		final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "ZakatMenuArabic.txt" : "ZakatMenuEnglish.txt"));

		// Create the popup menu.
		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem menuItem1 = new JMenuItem(translations[0]);
		final JMenuItem menuItem2 = new JMenuItem(translations[1]);

		if (MaknoonIslamicEncyclopedia.language)
		{
			menuItem1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			menuItem2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		final ActionListener menuItemsListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				final String prefix;
				if (e.getSource().equals(menuItem2))
					prefix = "https://www.maknoon.com/community/threads/%D9%86%D8%A8%D8%B0%D8%A9-%D8%B9%D9%86-%D8%B9%D9%84%D9%85-%D8%A7%D9%84%D8%B2%D9%83%D8%A7%D8%A9.117/";
				else
					prefix = "https://www.maknoon.com/community/threads/%d8%a8%d8%b1%d9%86%d8%a7%d9%85%d8%ac-%d8%a7%d9%84%d9%85%d9%88%d8%a7%d8%b1%d9%8a%d8%ab-%d9%88%d8%a7%d9%84%d8%b2%d9%83%d8%a7%d8%a9.104/";

				try
				{
					//java.net.URI u = new File(prefix).toURI();
					//Desktop.getDesktop().browse(new java.net.URI(u.getScheme(), u.getAuthority(), u.getPath(), u.getQuery(), "zakat"));
					//Desktop.getDesktop().open(new File(prefix)); // Version 2.1, 'browse' is not working in Mac OSX
					Desktop.getDesktop().browse(new URI(prefix)); // Version 2.1, 'browse' is not working in Mac OSX
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		};

		menuItem1.addActionListener(menuItemsListener);
		menuItem2.addActionListener(menuItemsListener);
		popup.add(menuItem2);
		popup.add(menuItem1);

		// Add listener to components that can bring up popup menus.
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				maybeShowPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				maybeShowPopup(e);
			}

			private void maybeShowPopup(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					if (MaknoonIslamicEncyclopedia.language)
					{
						//popup.updateUI(); // Version 2.1, for getPreferredSize() to return correct value
						popup.show(e.getComponent(), e.getX() - popup.getPreferredSize().width, e.getY());
					}
					else
						popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		if (MaknoonIslamicEncyclopedia.language)
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		new informationFrame();
	}

	class informationFrame extends JInternalFrame
	{
		informationFrame()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "ZakatSystemArabic.txt" : "ZakatSystemEnglish.txt"));

			setTitle(translations[19]);
			setLayout(new GridBagLayout());
			setMaximizable(true);
			setResizable(true);
			setFrameIcon(null);

			// Gold and Silver Panels
			final JPanel goldAndSilverPanel = new JPanel(new BorderLayout());
			goldAndSilverPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[20], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JLabel goldLabel = new JLabel(translations[21]);
			final JTextField goldTextField = new JTextField(5);
			goldTextField.setEnabled(false);
			goldTextField.setText("0");

			final JRadioButton goldYesButton = new JRadioButton(translations[22]);
			final JRadioButton goldNoButton = new JRadioButton(translations[23], true);
			final JLabel goldUnitLabel = new JLabel(translations[24]);

			final JComboBox<String> goldFineness = new JComboBox<>(new String[]{"24/24", "22/24", "21/24", "20/24", "18/24", "15/24", "14/24", "10/24", "9/24", "8/24"});
			goldFineness.setEnabled(false);
			goldFineness.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					final StringTokenizer tokens = new StringTokenizer((String) Objects.requireNonNull(goldFineness.getSelectedItem()), "/");
					goldFinenessUnit = Integer.parseInt(tokens.nextToken());
				}
			});

			final ActionListener goldGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == goldYesButton)
					{
						goldTextField.setEnabled(true);
						goldFineness.setEnabled(true);
					}

					if (ae.getSource() == goldNoButton)
					{
						goldTextField.setEnabled(false);
						goldFineness.setEnabled(false);
						goldTextField.setText("0");
						goldAmount = 0;
					}
				}
			};

			goldYesButton.addActionListener(goldGroupListener);
			goldNoButton.addActionListener(goldGroupListener);

			final ButtonGroup goldGroup = new ButtonGroup();
			goldGroup.add(goldYesButton);
			goldGroup.add(goldNoButton);

			final JPanel goldPanel = new JPanel();
			goldPanel.add(goldYesButton);
			goldPanel.add(goldNoButton);
			goldPanel.add(goldTextField);
			goldPanel.add(goldUnitLabel);
			goldPanel.add(goldFineness);

			final JPanel goldAndSilverCenterPanel = new JPanel(new GridLayout(2, 1));
			goldAndSilverPanel.add(goldAndSilverCenterPanel, BorderLayout.CENTER);

			final JPanel goldAndSilverEastPanel = new JPanel(new GridLayout(2, 1));
			if (MaknoonIslamicEncyclopedia.language) goldAndSilverPanel.add(goldAndSilverEastPanel, BorderLayout.WEST);
			else goldAndSilverPanel.add(goldAndSilverEastPanel, BorderLayout.EAST);

			goldAndSilverCenterPanel.add(goldLabel);
			goldAndSilverEastPanel.add(goldPanel);

			final JLabel silverLabel = new JLabel(translations[25]);
			final JTextField silverTextField = new JTextField(5);
			silverTextField.setEnabled(false);
			silverTextField.setText("0");

			final JRadioButton silverYesButton = new JRadioButton(translations[26]);
			final JRadioButton silverNoButton = new JRadioButton(translations[27], true);
			final JLabel silverUnitLabel = new JLabel(translations[28]);

			// Version 2.0
			final JComboBox<String> silverFineness = new JComboBox<>(new String[]{translations[7], "980", "958", "950", "925", "900", "835", "833", "830", "800"}); // Fine Silver is 999 and above
			silverFineness.setEnabled(false);
			silverFineness.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 2.0
					if ((silverFineness.getSelectedItem()).equals(translations[7]))
						silverFinenessUnit = 1000;
					else
					{
						final StringTokenizer tokens = new StringTokenizer((String) silverFineness.getSelectedItem(), "/");
						silverFinenessUnit = Integer.parseInt(tokens.nextToken());
					}
				}
			});

			final ActionListener silverGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == silverYesButton)
					{
						silverTextField.setEnabled(true);
						silverFineness.setEnabled(true);
					}

					if (ae.getSource() == silverNoButton)
					{
						silverTextField.setEnabled(false);
						silverFineness.setEnabled(false);
						silverTextField.setText("0");
						silverAmount = 0;
					}
				}
			};

			silverYesButton.addActionListener(silverGroupListener);
			silverNoButton.addActionListener(silverGroupListener);

			final ButtonGroup silverGroup = new ButtonGroup();
			silverGroup.add(silverYesButton);
			silverGroup.add(silverNoButton);

			final JPanel silverPanel = new JPanel();
			silverPanel.add(silverYesButton);
			silverPanel.add(silverNoButton);
			silverPanel.add(silverTextField);
			silverPanel.add(silverUnitLabel);
			silverPanel.add(silverFineness);

			goldAndSilverCenterPanel.add(silverLabel);
			goldAndSilverEastPanel.add(silverPanel);

			add(goldAndSilverPanel, new GridBagConstraints(
					0, 0, 3, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			// Business Panels
			final JPanel businessPanel = new JPanel(new BorderLayout());
			businessPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[29], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JLabel businessLabel = new JLabel(translations[30]);
			final JTextField businessTextField = new JTextField(5);
			businessTextField.setEnabled(false);
			businessTextField.setText("0");

			final JRadioButton businessYesButton = new JRadioButton(translations[31]);
			final JRadioButton businessNoButton = new JRadioButton(translations[32], true);

			final ActionListener businessGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == businessYesButton)
						businessTextField.setEnabled(true);

					if (ae.getSource() == businessNoButton)
					{
						businessTextField.setEnabled(false);
						businessTextField.setText("0");
						businessAmount = 0;
					}
				}
			};

			businessYesButton.addActionListener(businessGroupListener);
			businessNoButton.addActionListener(businessGroupListener);

			final ButtonGroup businessGroup = new ButtonGroup();
			businessGroup.add(businessYesButton);
			businessGroup.add(businessNoButton);

			final JLabel businessUnitLabel = new JLabel(translations[33]);
			final JPanel businessInputPanel = new JPanel();
			businessInputPanel.add(businessYesButton);
			businessInputPanel.add(businessNoButton);
			businessInputPanel.add(businessTextField);
			businessInputPanel.add(businessUnitLabel);

			businessPanel.add(businessLabel, BorderLayout.CENTER);
			if (MaknoonIslamicEncyclopedia.language) businessPanel.add(businessInputPanel, BorderLayout.WEST);
			else businessPanel.add(businessInputPanel, BorderLayout.EAST);

			add(businessPanel, new GridBagConstraints(
					0, 1, 3, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			// Debt Panels
			final JPanel debtPanel = new JPanel(new BorderLayout());
			debtPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[34], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JLabel frindlyDebtLabel = new JLabel(translations[35]);
			final JTextField frindlyDebtTextField = new JTextField(5);
			frindlyDebtTextField.setEnabled(false);
			frindlyDebtTextField.setText("0");

			final JRadioButton frindlyDebtYesButton = new JRadioButton(translations[36]);
			final JRadioButton frindlyDebtNoButton = new JRadioButton(translations[37], true);

			final ActionListener frindlyDebtGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == frindlyDebtYesButton)
						frindlyDebtTextField.setEnabled(true);

					if (ae.getSource() == frindlyDebtNoButton)
					{
						frindlyDebtTextField.setEnabled(false);
						frindlyDebtTextField.setText("0");
						friendlyDebtAmount = 0;
					}
				}
			};

			frindlyDebtYesButton.addActionListener(frindlyDebtGroupListener);
			frindlyDebtNoButton.addActionListener(frindlyDebtGroupListener);

			final ButtonGroup frindlyDebtGroup = new ButtonGroup();
			frindlyDebtGroup.add(frindlyDebtYesButton);
			frindlyDebtGroup.add(frindlyDebtNoButton);

			final JLabel frindlyDebtUnitLabel = new JLabel(translations[38]);
			final JPanel frindlyDebtPanel = new JPanel();
			frindlyDebtPanel.add(frindlyDebtYesButton);
			frindlyDebtPanel.add(frindlyDebtNoButton);
			frindlyDebtPanel.add(frindlyDebtTextField);
			frindlyDebtPanel.add(frindlyDebtUnitLabel);

			final JPanel debtCenterPanel = new JPanel(new GridLayout(2, 1));
			debtPanel.add(debtCenterPanel, BorderLayout.CENTER);

			final JPanel debtEastPanel = new JPanel(new GridLayout(2, 1));
			if (MaknoonIslamicEncyclopedia.language) debtPanel.add(debtEastPanel, BorderLayout.WEST);
			else debtPanel.add(debtEastPanel, BorderLayout.EAST);

			debtCenterPanel.add(frindlyDebtLabel);
			debtEastPanel.add(frindlyDebtPanel);

			final JLabel uglyDebtLabel = new JLabel(translations[39]);
			final JTextField uglyDebtTextField = new JTextField(5);
			uglyDebtTextField.setEnabled(false);
			uglyDebtTextField.setText("0");

			final JRadioButton uglyDebtYesButton = new JRadioButton(translations[40]);
			final JRadioButton uglyDebtNoButton = new JRadioButton(translations[41], true);

			final ActionListener uglyDebtGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == uglyDebtYesButton)
						uglyDebtTextField.setEnabled(true);

					if (ae.getSource() == uglyDebtNoButton)
					{
						uglyDebtTextField.setEnabled(false);
						uglyDebtTextField.setText("0");
						uglyDebtAmount = 0;
					}
				}
			};

			uglyDebtYesButton.addActionListener(uglyDebtGroupListener);
			uglyDebtNoButton.addActionListener(uglyDebtGroupListener);

			final ButtonGroup uglyDebtGroup = new ButtonGroup();
			uglyDebtGroup.add(uglyDebtYesButton);
			uglyDebtGroup.add(uglyDebtNoButton);

			final JLabel uglyDebtUnitLabel = new JLabel(translations[42]);
			final JPanel uglyDebtPanel = new JPanel();
			uglyDebtPanel.add(uglyDebtYesButton);
			uglyDebtPanel.add(uglyDebtNoButton);
			uglyDebtPanel.add(uglyDebtTextField);
			uglyDebtPanel.add(uglyDebtUnitLabel);

			debtCenterPanel.add(uglyDebtLabel);
			debtEastPanel.add(uglyDebtPanel);

			add(debtPanel, new GridBagConstraints(
					0, 3, 3, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			// Other money Panels
			final JPanel otherMoneyPanel = new JPanel(new BorderLayout());
			otherMoneyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[43], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			// Honey Panels
			final JLabel honeyLabel = new JLabel(translations[44]);
			final JTextField honeyTextField = new JTextField(5);
			honeyTextField.setEnabled(false);
			honeyTextField.setText("0");

			final JRadioButton honeyYesButton = new JRadioButton(translations[45]);
			final JRadioButton honeyNoButton = new JRadioButton(translations[46], true);

			final ActionListener honeyGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == honeyYesButton)
						honeyTextField.setEnabled(true);

					if (ae.getSource() == honeyNoButton)
					{
						honeyTextField.setEnabled(false);
						honeyTextField.setText("0");
						honeyAmount = 0;
					}
				}
			};

			honeyYesButton.addActionListener(honeyGroupListener);
			honeyNoButton.addActionListener(honeyGroupListener);

			final ButtonGroup honeyGroup = new ButtonGroup();
			honeyGroup.add(honeyYesButton);
			honeyGroup.add(honeyNoButton);

			final JLabel honeyUnitLabel = new JLabel(translations[47]);
			final JPanel honeyInputPanel = new JPanel();
			honeyInputPanel.add(honeyYesButton);
			honeyInputPanel.add(honeyNoButton);
			honeyInputPanel.add(honeyTextField);
			honeyInputPanel.add(honeyUnitLabel);

			final JPanel otherMoneyCenterPanel = new JPanel(new GridLayout(2, 1));
			otherMoneyPanel.add(otherMoneyCenterPanel, BorderLayout.CENTER);

			final JPanel otherMoneyEastPanel = new JPanel(new GridLayout(2, 1));
			if (MaknoonIslamicEncyclopedia.language) otherMoneyPanel.add(otherMoneyEastPanel, BorderLayout.WEST);
			else otherMoneyPanel.add(otherMoneyEastPanel, BorderLayout.EAST);

			otherMoneyCenterPanel.add(honeyLabel);
			otherMoneyEastPanel.add(honeyInputPanel);

			final JLabel oreLabel = new JLabel(translations[48]);
			final JTextField oreTextField = new JTextField(5);
			oreTextField.setEnabled(false);
			oreTextField.setText("0");

			final JRadioButton oreYesButton = new JRadioButton(translations[49]);
			final JRadioButton oreNoButton = new JRadioButton(translations[50], true);

			final ActionListener oreGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == oreYesButton)
						oreTextField.setEnabled(true);

					if (ae.getSource() == oreNoButton)
					{
						oreTextField.setEnabled(false);
						oreTextField.setText("0");
						oreAmount = 0;
					}
				}
			};

			oreYesButton.addActionListener(oreGroupListener);
			oreNoButton.addActionListener(oreGroupListener);

			final ButtonGroup oreGroup = new ButtonGroup();
			oreGroup.add(oreYesButton);
			oreGroup.add(oreNoButton);

			final JLabel oreUnitLabel = new JLabel(translations[51]);
			final JPanel orePanel = new JPanel();
			orePanel.add(oreYesButton);
			orePanel.add(oreNoButton);
			orePanel.add(oreTextField);
			orePanel.add(oreUnitLabel);

			otherMoneyCenterPanel.add(oreLabel);
			otherMoneyEastPanel.add(orePanel);

			add(otherMoneyPanel, new GridBagConstraints(
					2, 2, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			//planet Panels
			final JPanel planetPanel = new JPanel(new BorderLayout());
			planetPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[52], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JLabel freePlanetLabel = new JLabel(translations[53]);
			final JTextField freePlanetTextField = new JTextField(5);
			freePlanetTextField.setEnabled(false);
			freePlanetTextField.setText("0");

			final JRadioButton freePlanetYesButton = new JRadioButton(translations[54]);
			final JRadioButton freePlanetNoButton = new JRadioButton(translations[55], true);

			final ActionListener freePlanetGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == freePlanetYesButton)
						freePlanetTextField.setEnabled(true);

					if (ae.getSource() == freePlanetNoButton)
					{
						freePlanetTextField.setEnabled(false);
						freePlanetTextField.setText("0");
						freePlanetAmount = 0;
					}
				}
			};

			freePlanetYesButton.addActionListener(freePlanetGroupListener);
			freePlanetNoButton.addActionListener(freePlanetGroupListener);

			final ButtonGroup freePlanetGroup = new ButtonGroup();
			freePlanetGroup.add(freePlanetYesButton);
			freePlanetGroup.add(freePlanetNoButton);

			final JLabel freePlaneUnitLabel = new JLabel(translations[56]);
			final JPanel freePlanetPanel = new JPanel();
			freePlanetPanel.add(freePlanetYesButton);
			freePlanetPanel.add(freePlanetNoButton);
			freePlanetPanel.add(freePlanetTextField);
			freePlanetPanel.add(freePlaneUnitLabel);

			final JPanel planetCenterPanel = new JPanel(new GridLayout(2, 1));
			planetPanel.add(planetCenterPanel, BorderLayout.CENTER);

			final JPanel planetEastPanel = new JPanel(new GridLayout(2, 1));
			if (MaknoonIslamicEncyclopedia.language) planetPanel.add(planetEastPanel, BorderLayout.WEST);
			else planetPanel.add(planetEastPanel, BorderLayout.EAST);

			planetCenterPanel.add(freePlanetLabel);
			planetEastPanel.add(freePlanetPanel);

			final JLabel costPlanetLabel = new JLabel(translations[57]);
			final JTextField costPlanetTextField = new JTextField(5);
			costPlanetTextField.setEnabled(false);
			costPlanetTextField.setText("0");

			final JRadioButton costPlanetYesButton = new JRadioButton(translations[58]);
			final JRadioButton costPlanetNoButton = new JRadioButton(translations[59], true);

			final ActionListener costPlanetGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == costPlanetYesButton)
						costPlanetTextField.setEnabled(true);

					if (ae.getSource() == costPlanetNoButton)
					{
						costPlanetTextField.setEnabled(false);
						costPlanetTextField.setText("0");
						costPlanetAmount = 0;
					}
				}
			};

			costPlanetYesButton.addActionListener(costPlanetGroupListener);
			costPlanetNoButton.addActionListener(costPlanetGroupListener);

			final ButtonGroup costPlanetGroup = new ButtonGroup();
			costPlanetGroup.add(costPlanetYesButton);
			costPlanetGroup.add(costPlanetNoButton);

			final JLabel costPlaneUnitLabel = new JLabel(translations[60]);
			final JPanel costPlanetPanel = new JPanel();
			costPlanetPanel.add(costPlanetYesButton);
			costPlanetPanel.add(costPlanetNoButton);
			costPlanetPanel.add(costPlanetTextField);
			costPlanetPanel.add(costPlaneUnitLabel);

			planetCenterPanel.add(costPlanetLabel);
			planetEastPanel.add(costPlanetPanel);

			add(planetPanel, new GridBagConstraints(
					0, 2, 2, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			//Animals Panels
			final JPanel animalsPanel = new JPanel(new BorderLayout());
			animalsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[61], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JLabel camelLabel = new JLabel(translations[62]);
			final JTextField camelTextField = new JTextField(5);
			camelTextField.setEnabled(false);
			camelTextField.setText("0");

			final JRadioButton camelYesButton = new JRadioButton(translations[63]);
			final JRadioButton camelNoButton = new JRadioButton(translations[64], true);

			final ActionListener camelGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == camelYesButton)
						camelTextField.setEnabled(true);

					if (ae.getSource() == camelNoButton)
					{
						camelTextField.setEnabled(false);
						camelTextField.setText("0");
						camelNo = 0;
					}
				}
			};

			camelYesButton.addActionListener(camelGroupListener);
			camelNoButton.addActionListener(camelGroupListener);

			final ButtonGroup camelGroup = new ButtonGroup();
			camelGroup.add(camelYesButton);
			camelGroup.add(camelNoButton);

			final JPanel camelPanel = new JPanel();
			camelPanel.add(camelYesButton);
			camelPanel.add(camelNoButton);
			camelPanel.add(camelTextField);

			final JPanel animalsCenterPanel = new JPanel(new GridLayout(4, 1));
			animalsPanel.add(animalsCenterPanel, BorderLayout.CENTER);

			final JPanel animalsEastPanel = new JPanel(new GridLayout(4, 1));
			if (MaknoonIslamicEncyclopedia.language) animalsPanel.add(animalsEastPanel, BorderLayout.WEST);
			else animalsPanel.add(animalsEastPanel, BorderLayout.EAST);

			animalsCenterPanel.add(camelLabel);
			animalsEastPanel.add(camelPanel);

			final JLabel cowLabel = new JLabel(translations[65]);
			final JTextField cowTextField = new JTextField(5);
			cowTextField.setEnabled(false);
			cowTextField.setText("0");

			final JRadioButton cowYesButton = new JRadioButton(translations[66]);
			final JRadioButton cowNoButton = new JRadioButton(translations[67], true);

			final ActionListener cowGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == cowYesButton)
						cowTextField.setEnabled(true);

					if (ae.getSource() == cowNoButton)
					{
						cowTextField.setEnabled(false);
						cowTextField.setText("0");
						cowNo = 0;
					}
				}
			};

			cowYesButton.addActionListener(cowGroupListener);
			cowNoButton.addActionListener(cowGroupListener);

			final ButtonGroup cowGroup = new ButtonGroup();
			cowGroup.add(cowYesButton);
			cowGroup.add(cowNoButton);

			final JPanel cowPanel = new JPanel();
			cowPanel.add(cowYesButton);
			cowPanel.add(cowNoButton);
			cowPanel.add(cowTextField);

			animalsCenterPanel.add(cowLabel);
			animalsEastPanel.add(cowPanel);

			final JLabel sheepLabel = new JLabel(translations[68]);
			final JTextField sheepTextField = new JTextField(5);
			sheepTextField.setEnabled(false);
			sheepTextField.setText("0");

			final JRadioButton sheepYesButton = new JRadioButton(translations[69]);
			final JRadioButton sheepNoButton = new JRadioButton(translations[70], true);

			final ActionListener sheepGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == sheepYesButton)
						sheepTextField.setEnabled(true);

					if (ae.getSource() == sheepNoButton)
					{
						sheepTextField.setEnabled(false);
						sheepTextField.setText("0");
						sheepNo = 0;
					}
				}
			};

			sheepYesButton.addActionListener(sheepGroupListener);
			sheepNoButton.addActionListener(sheepGroupListener);

			final ButtonGroup sheepGroup = new ButtonGroup();
			sheepGroup.add(sheepYesButton);
			sheepGroup.add(sheepNoButton);

			final JPanel sheepPanel = new JPanel();
			sheepPanel.add(sheepYesButton);
			sheepPanel.add(sheepNoButton);
			sheepPanel.add(sheepTextField);

			animalsCenterPanel.add(sheepLabel);
			animalsEastPanel.add(sheepPanel);

			final JLabel horseLabel = new JLabel(translations[71]);
			final JTextField horseTextField = new JTextField(5);
			horseTextField.setEnabled(false);
			horseTextField.setText("0");

			final JRadioButton horseYesButton = new JRadioButton(translations[72]);
			final JRadioButton horseNoButton = new JRadioButton(translations[73], true);

			final ActionListener horseGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == horseYesButton)
						horseTextField.setEnabled(true);

					if (ae.getSource() == horseNoButton)
					{
						horseTextField.setEnabled(false);
						horseTextField.setText("0");
						horseNo = 0;
					}
				}
			};

			horseYesButton.addActionListener(horseGroupListener);
			horseNoButton.addActionListener(horseGroupListener);

			final ButtonGroup horseGroup = new ButtonGroup();
			horseGroup.add(horseYesButton);
			horseGroup.add(horseNoButton);

			final JPanel horsePanel = new JPanel();
			horsePanel.add(horseYesButton);
			horsePanel.add(horseNoButton);
			horsePanel.add(horseTextField);

			animalsCenterPanel.add(horseLabel);
			animalsEastPanel.add(horsePanel);

			add(animalsPanel, new GridBagConstraints(
					2, 4, 1, 3, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			final JPanel madhebPanel = new JPanel();
			madhebPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[74], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JRadioButton jumhorMadhabButton = new JRadioButton(translations[75], true);
			final JRadioButton hanbaliMadhabButton = new JRadioButton(translations[76]);
			final JRadioButton shafieMadhabButton = new JRadioButton(translations[77]);
			final JRadioButton malikiMadhabButton = new JRadioButton(translations[78]);
			final JRadioButton hanafiMadhabButton = new JRadioButton(translations[79]);
			final JRadioButton allScientistsMadhabButton = new JRadioButton(translations[80]);

			madhebPanel.add(jumhorMadhabButton);
			madhebPanel.add(hanbaliMadhabButton);
			madhebPanel.add(shafieMadhabButton);
			madhebPanel.add(malikiMadhabButton);
			madhebPanel.add(hanafiMadhabButton);
			madhebPanel.add(allScientistsMadhabButton);

			final JPanel valuePanel = new JPanel();
			valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
			valuePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[81], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JPanel currencyValuePanel = new JPanel();
			final JLabel goldValueLabel = new JLabel(translations[82]);
			final JTextField goldValueTextField = new JTextField(6);
			//goldValueTextField.setBackground(Color.GREEN); // Version 2.0, Indicating that it is connecting to internet to find the latest value. Not working with weblaf, it needs re-style with code only works in weblaf
			goldValueTextField.setText("150");

			final JLabel silverValueLabel = new JLabel(translations[83]);
			final JTextField silverValueTextField = new JTextField(6);
			//silverValueTextField.setBackground(Color.GREEN); // Version 2.0, Indicating that it is connecting to internet to find the latest value.
			silverValueTextField.setText("2");

			final JLabel silverValueUnitLabel = new JLabel(translations[84]);
			currencyValuePanel.add(goldValueLabel);
			currencyValuePanel.add(goldValueTextField);
			currencyValuePanel.add(silverValueLabel);
			currencyValuePanel.add(silverValueTextField);
			currencyValuePanel.add(silverValueUnitLabel);
			valuePanel.add(currencyValuePanel);

			final JPanel horseValuePanel = new JPanel();
			final JLabel horseValueLabel = new JLabel(translations[85]);
			final JTextField horseValueTextField = new JTextField(5);
			horseValueTextField.setText("5000");

			final JLabel horseValueUnitLabel = new JLabel(translations[86]);
			horseValuePanel.add(horseValueLabel);
			horseValuePanel.add(horseValueTextField);
			horseValuePanel.add(horseValueUnitLabel);
			valuePanel.add(horseValuePanel);

			// Version 2.0, Shifted here for horseValue* initialization
			final ActionListener madahebGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					enableAll(); // Version 2.0
					if (ae.getSource() == jumhorMadhabButton)
					{
						madhab = madhabName.GUMHOUR;
						disableAll();
					}
					if (ae.getSource() == hanbaliMadhabButton)
					{
						madhab = madhabName.HANBALI;

						horseLabel.setEnabled(false);
						horseTextField.setEnabled(false);
						horseYesButton.setEnabled(false);
						horseNoButton.setEnabled(false);
						horseValueLabel.setEnabled(false);
						horseValueTextField.setEnabled(false);
						horseValueUnitLabel.setEnabled(false);
					}
					if (ae.getSource() == shafieMadhabButton)
					{
						madhab = madhabName.SHAFEE;
						disableAll();
					}
					if (ae.getSource() == malikiMadhabButton)
					{
						madhab = madhabName.MALIKI;
						disableAll();
					}
					if (ae.getSource() == hanafiMadhabButton)
					{
						madhab = madhabName.HANAFI;
					}
					if (ae.getSource() == allScientistsMadhabButton)
					{
						madhab = madhabName.ALL;
					}
				}

				// Version 2.0
				public void enableAll()
				{
					honeyLabel.setEnabled(true);
					if (honeyYesButton.isSelected()) honeyTextField.setEnabled(true);
					honeyYesButton.setEnabled(true);
					honeyNoButton.setEnabled(true);
					honeyUnitLabel.setEnabled(true);

					horseLabel.setEnabled(true);
					if (horseYesButton.isSelected()) horseTextField.setEnabled(true);
					horseYesButton.setEnabled(true);
					horseNoButton.setEnabled(true);

					horseValueLabel.setEnabled(true);
					horseValueTextField.setEnabled(true);
					horseValueUnitLabel.setEnabled(true);
				}

				public void disableAll()
				{
					honeyLabel.setEnabled(false);
					honeyTextField.setEnabled(false);
					honeyYesButton.setEnabled(false);
					honeyNoButton.setEnabled(false);
					honeyUnitLabel.setEnabled(false);
					horseLabel.setEnabled(false);
					horseTextField.setEnabled(false);
					horseYesButton.setEnabled(false);
					horseNoButton.setEnabled(false);
					horseValueLabel.setEnabled(false);
					horseValueTextField.setEnabled(false);
					horseValueUnitLabel.setEnabled(false);
				}
			};

			jumhorMadhabButton.addActionListener(madahebGroupListener);
			hanbaliMadhabButton.addActionListener(madahebGroupListener);
			shafieMadhabButton.addActionListener(madahebGroupListener);
			malikiMadhabButton.addActionListener(madahebGroupListener);
			hanafiMadhabButton.addActionListener(madahebGroupListener);
			allScientistsMadhabButton.addActionListener(madahebGroupListener);

			// Version 2.0, To disableAll()
			jumhorMadhabButton.doClick();
			jumhorMadhabButton.setSelected(true);

			final ButtonGroup madahebGroup = new ButtonGroup();
			madahebGroup.add(jumhorMadhabButton);
			madahebGroup.add(hanbaliMadhabButton);
			madahebGroup.add(shafieMadhabButton);
			madahebGroup.add(malikiMadhabButton);
			madahebGroup.add(hanafiMadhabButton);
			madahebGroup.add(allScientistsMadhabButton);

			add(madhebPanel, new GridBagConstraints(
					0, 4, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			add(valuePanel, new GridBagConstraints(
					0, 5, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			final JButton calculateButton = new JButton(translations[87]);
			calculateButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					boolean continueCalculate = true;

					try
					{
						camelNo = Integer.parseInt(camelTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[2], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						camelTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						cowNo = Integer.parseInt(cowTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[3], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						cowTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						sheepNo = Integer.parseInt(sheepTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[4], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						sheepTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						horseNo = Integer.parseInt(horseTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[17], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						horseTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						goldAmount = Float.parseFloat(goldTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[5], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						goldTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						silverAmount = Float.parseFloat(silverTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[6], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						silverTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						oreAmount = Float.parseFloat(oreTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[8], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						oreTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						friendlyDebtAmount = Float.parseFloat(frindlyDebtTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[9], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						frindlyDebtTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						uglyDebtAmount = Float.parseFloat(uglyDebtTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[10], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						uglyDebtTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						goldValue = Float.parseFloat(goldValueTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[11], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						continueCalculate = false;
					}

					try
					{
						silverValue = Float.parseFloat(silverValueTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[12], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						continueCalculate = false;
					}

					try
					{
						horseCost = Integer.parseInt(horseValueTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[18], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						horseValueTextField.setText("5000");
						continueCalculate = false;
					}

					//busniess
					try
					{
						businessAmount = Float.parseFloat(businessTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[13], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						businessTextField.setText("0");
						continueCalculate = false;
					}

					//honey
					try
					{
						honeyAmount = Integer.parseInt(honeyTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[14], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						honeyTextField.setText("0");
						continueCalculate = false;
					}

					// Planet
					try
					{
						freePlanetAmount = Float.parseFloat(freePlanetTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[15], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						freePlanetTextField.setText("0");
						continueCalculate = false;
					}

					try
					{
						costPlanetAmount = Float.parseFloat(costPlanetTextField.getText().trim());
					}
					catch (NumberFormatException e)
					{
						JOptionPane.showOptionDialog(ZakatSystem.this, translations[16], translations[1], JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{translations[0]}, translations[0]);
						costPlanetTextField.setText("0");
						continueCalculate = false;
					}

					if (continueCalculate)
					{
						//To show the results
						try
						{
							setClosed(true);
						}
						catch (java.beans.PropertyVetoException e)
						{
							e.printStackTrace();
						}
						new ZakatSystemResults();
					}
				}
			});

			add(calculateButton, new GridBagConstraints(
					0, 6, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));

			if (MaknoonIslamicEncyclopedia.language)
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

			ZakatSystem.this.add(this);
			pack();
			center(this);

			setVisible(true);
			if (screenSize.width == 800 && screenSize.height == 600)
			{
				try
				{
					this.setMaximum(true);
				}
				catch (java.beans.PropertyVetoException e)
				{
					e.printStackTrace();
				}
			}

			// Version 2.0, Update the price of the gold/silver online
			final Thread thread = new Thread()
			{
				public void run()
				{
					/*
					try
					{
						final HttpClient client = HttpClient.newHttpClient();
						final HttpRequest request = HttpRequest.newBuilder()
								.uri(URI.create("https://www.kitco.com/texten/texten.html"))
								.build();

						final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						System.out.println(response.body());

						//final CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
						//String result = response.get();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					*/

					BufferedReader br = null;
					try
					{
						final URL u = new URL("https://www.kitco.com/texten/texten.html");
						final URLConnection url = u.openConnection();

						// set the connection timeout to 10 seconds and the read timeout to 10 seconds
						url.setConnectTimeout(10000);
						url.setReadTimeout(10000);

						String line;
						br = new BufferedReader(new InputStreamReader(url.getInputStream()));
						while ((line = br.readLine()) != null)
						{
							line = line.trim();
							if (line.startsWith("Gold"))
							{
								final StringTokenizer tokens = new StringTokenizer(line, " ");
								tokens.nextToken();
								final float us = Float.parseFloat(tokens.nextToken()); // Dollar per troy ounce
								SwingUtilities.invokeLater(new Runnable()
								{
									@Override
									public void run()
									{
										goldValueTextField.setText(String.valueOf(MawarethSystem.round(us * 3.672 / 31.1034768)));
										goldValueTextField.setBackground(Color.GREEN);
									}
								});
							}

							if (line.startsWith("Silver"))
							{
								final StringTokenizer tokens = new StringTokenizer(line, " ");
								tokens.nextToken();
								final float us = Float.parseFloat(tokens.nextToken()); // Dollar per troy ounce

								SwingUtilities.invokeLater(new Runnable()
								{
									@Override
									public void run()
									{
										silverValueTextField.setText(String.valueOf(MawarethSystem.round(us * 3.672 / 31.1034768)));
										silverValueTextField.setBackground(Color.GREEN);
									}
								});
								break;
							}
						}
						br.close();

                        /* Version 2.2, [http://dgcsc.org/goldprices.xml] is dead.
                        // Gold can be obtained by the above also. Better to have 2 resources.
                        final URL goldLink = new URL("http://dgcsc.org/goldprices.xml");
                        final BufferedReader goldReader = new BufferedReader(new InputStreamReader(goldLink.openStream()));
                        if(goldReader.ready()) // Only one line
                        {
                            final String line = goldReader.readLine();
                            final int start = line.indexOf(">", line.indexOf("USD"));
                            final int last = line.indexOf("<", start);
                            final Float us = Float.parseFloat(line.substring(start+1, last));
                            goldValueTextField.setText(String.valueOf(MawarethSystem.round(us*3.672)));
                            goldValueTextField.setBackground(Color.WHITE);
                        }
                        goldReader.close();
                        */
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					finally
					{
						try
						{
							if (br != null)
								br.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
		}
	}

	void goldAndSilverCalculation()
	{
		// Version 1.6, (goldAmount*goldFinenessUnit/24) instead of goldAmount. The same for silver
		if ((goldAmount * goldFinenessUnit / 24) >= 85)
			goldRequired = goldAmount / 40 * goldFinenessUnit / 24;

		if ((silverAmount * silverFinenessUnit / 1000) >= 595)
			silverRequired = silverAmount / 40 * silverFinenessUnit / 1000;
	}

	void otherMoneyCalculation()
	{
		/*
		// Version 1.6, Nesab in Hanbali & Hanafi is the related to the least value of the two Gold or Silver.
		float nesab=0;
		if(madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)
			nesab = (goldValue*85)<(silverValue*595)?goldValue*85:silverValue*595;
		else
			// Calculation of money is referenced with gold
			nesab = goldValue*85;

		if(moneyAmount>=nesab)
			moneyRequired=moneyAmount/40;
		else
			moneyRequired=0;
		*/
		if (oreAmount != 0)
			oreRequired = oreAmount / 5;
	}

	void debtCalculation()
	{
		if (friendlyDebtAmount >= (goldValue * 85))
			friendlyDebtRequired = friendlyDebtAmount / 40;

		if (uglyDebtAmount >= (goldValue * 85))
			uglyDebtRequired = uglyDebtAmount / 40;
	}

	void animalsCalculation()
	{
		if (camelNo != 0)
		{
			camelShaahRequired = 0;
			camelBintLaboonRequired = 0;
			camelBinLaboonRequired = 0;
			camelBintMakhadRequired = 0;
			camelHuqaRequired = 0;
			camelJadahRequired = 0;

			if (camelNo < 5) ;
			else
			{
				if (camelNo < 25)
					camelShaahRequired = camelNo / 5;
				else
				{
					if (camelNo <= 35)
						camelBintMakhadRequired = 1;
					else
					{
						if (camelNo <= 45)
							camelBintLaboonRequired = 1;
						else
						{
							if (camelNo <= 60)
								camelHuqaRequired = 1;
							else
							{
								if (camelNo <= 75)
									camelJadahRequired = 1;
								else
								{
									if (camelNo <= 90)
										camelBintLaboonRequired = 2;
									else
									{
										if (camelNo <= 120)
											camelHuqaRequired = 2;
										else
										{
											// 'Jumhor' madhab
											if (madhab != madhabName.HANAFI)
											{
												/*
												 * Version 1.6
												 * Do not assign the value camelNo since it is used to indicate the provided
												 * number at the end (use adjustedCamelNo instead). It causes alot of problem
												 * in calculating under all madaheb and to display the real number in the information panel.
												 */
												// remove to be divisable by 10
												int adjustedCamelNo = (camelNo / 10) * 10;

												// test for integer number of camelBintLaboonRequired
												float Test = (float) adjustedCamelNo / 40;

												// extract the reminder
												float remider = Test - (int) Test;

												if (remider == 0)
													camelBintLaboonRequired = (int) Test;
												else
												{
													// test for integer number of camelHuqaRequired
													Test = (float) adjustedCamelNo / 50;

													// extract the reminder
													remider = Test - (int) Test;

													if (remider == 0)
														camelHuqaRequired = (int) Test;
													else
													{
														// test for integer number of both
														Test = (float) (adjustedCamelNo - 50) / 40;

														// extract the reminder
														remider = Test - (int) Test;

														if (remider == 0)
														{
															camelHuqaRequired = 1;
															camelBintLaboonRequired = (int) Test;
														}
														else
														{
															// test for integer number of both
															Test = (float) (adjustedCamelNo - 40) / 50;

															// extract the reminder
															remider = Test - (int) Test;

															if (remider == 0)
															{
																camelBintLaboonRequired = 1;
																camelHuqaRequired = (int) Test;
															}
															else
															{
																// special case test for 230
																Test = (float) (adjustedCamelNo - 100) / 40;

																// extract the reminder
																remider = Test - (int) Test;

																if (remider == 0)
																{
																	camelHuqaRequired = 2;
																	camelBintLaboonRequired = (int) Test;
																}
																else
																{
																	// test for integer number of both
																	Test = (float) (adjustedCamelNo - 80) / 50;

																	// extract the reminder
																	remider = Test - (int) Test;

																	if (remider == 0)
																	{
																		camelBintLaboonRequired = 2;
																		camelHuqaRequired = (int) Test;
																	}
																	// Version 1.7, Special case when 270
																	else
																	{
																		// test for integer number of both
																		Test = (float) (adjustedCamelNo - 150) / 40;

																		// extract the reminder
																		remider = Test - (int) Test;

																		if (remider == 0)
																		{
																			camelHuqaRequired = 3;
																			camelBintLaboonRequired = (int) Test;
																		}
																		else
																		{
																			// test for integer number of both
																			Test = (float) (adjustedCamelNo - 120) / 50;

																			// extract the reminder
																			remider = Test - (int) Test;

																			if (remider == 0)
																			{
																				camelBintLaboonRequired = 3;
																				camelHuqaRequired = (int) Test;
																			}
																			//else
																			// We don't need this since we test it with camelBintLaboonRequired=3. adding more means repeat itself.
																		}
																	}
																}
															}
														}
													}
												}
											}
											else
											{
												// i.e. hanafi madhab where camel number is above 120
												// test for integer number of camelHuqaRequired
												float Test = (float) camelNo / 50;

												// Extract the reminder
												float remider = camelNo - (int) Test * 50;

												// Update versino 1.6, since we start calculating after 120 and NOT after 100.
												remider = (camelNo < 150) ? (remider - 20) : remider;

												if (remider == 0)
													camelHuqaRequired = (int) Test;
												else
												{
													camelHuqaRequired = (int) Test;
													if (remider < 5) ;
													else
														if (remider < 25)
															camelShaahRequired = (int) remider / 5;
														else
															if (remider <= 35)
																camelBintMakhadRequired = 1;
															else
																if (remider <= 45)
																	camelBintLaboonRequired = 1;
																else
																	// To add with the previous huqa
																	camelHuqaRequired = camelHuqaRequired + 1;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (cowNo != 0)
		{
			cowTabeeaOrTabeeahRequired = 0;
			cowMassunahRequired = 0;

			if (cowNo < 30) ;
			else
			{
				if (cowNo <= 39)
					cowTabeeaOrTabeeahRequired = 1;
				else
				{
					if (cowNo <= 59)
					{
						// 'Jumhor' madhab
						if (madhab != madhabName.HANAFI)
							cowMassunahRequired = 1;
						else
							cowMassunahRequired = 1 + (float) (cowNo - 40) / 40;
					}
					else
					{
						/*
						 * Version 1.6
						 * Do not assign the value cowNo since it is used to indicate the provided
						 * number at the end (use adjustedCowNo instead). It causes alot of problem
						 * in calculating under all madaheb and to display the real number in the information panel.
						 */
						// remove to be divisable by 10
						int adjustedCowNo = (cowNo / 10) * 10;

						//test for integer number of camelBintLaboonRequired
						float Test = (float) adjustedCowNo / 30;

						//extract the reminder
						float remider = Test - (int) Test;

						if (remider == 0)
							cowTabeeaOrTabeeahRequired = (int) Test;
						else
						{
							//test for integer number of camelHuqaRequired
							Test = (float) adjustedCowNo / 40;

							//extract the reminder
							remider = Test - (int) Test;

							if (remider == 0)
								cowMassunahRequired = (int) Test;
							else
							{
								//test for integer number of both
								Test = (float) (adjustedCowNo - 40) / 30;

								//extract the reminder
								remider = Test - (int) Test;

								if (remider == 0)
								{
									cowMassunahRequired = 1;
									cowTabeeaOrTabeeahRequired = (int) Test;
								}
								else
								{
									//test for integer number of both
									Test = (float) (adjustedCowNo - 30) / 40;

									//extract the reminder
									remider = Test - (int) Test;

									if (remider == 0)
									{
										cowTabeeaOrTabeeahRequired = 1;
										cowMassunahRequired = (int) Test;
									}
									else
									{
										//special case test for 230
										Test = (float) (adjustedCowNo - 80) / 30;

										//extract the reminder
										remider = Test - (int) Test;

										if (remider == 0)
										{
											cowMassunahRequired = 2;
											cowTabeeaOrTabeeahRequired = (int) Test;
										}
										else
										{
											//test for integer number of both
											Test = (float) (adjustedCowNo - 60) / 40;

											//extract the reminder
											remider = Test - (int) Test;

											if (remider == 0)
											{
												cowTabeeaOrTabeeahRequired = 2;
												cowMassunahRequired = (int) Test;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (sheepNo != 0)
		{
			if (sheepNo < 40) ;
			else
				if (sheepNo <= 120)
					sheepRequired = 1;
				else
					if (sheepNo <= 200)
						sheepRequired = 2;
					else
						if (sheepNo <= 399)
							sheepRequired = 3;
						else
							sheepRequired = (sheepNo / 100);
		}

		if (horseNo != 0)
		{
			// Version 1.6
			horseMoneyRequired = 0;

			if (madhab == madhabName.HANAFI)
			{
				// horseNo<5 or horseNo<3 two are accepted in hanafi madhab
				if (horseNo < 5) ;
				else
					horseMoneyRequired = (horseNo * horseCost) / 40;
			}
		}
	}

	void businessCalculation()
	{
		// Version 1.6
		businessRequired = 0;

		// By default
		if (businessAmount >= (goldValue * 85))
			businessRequired = businessAmount / 40;

		if (madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)
			if ((goldValue * 85) > (silverValue * 595))
				if (businessAmount >= (silverValue * 595))
					businessRequired = businessAmount / 40;
	}

	private float goldRequiredPart, silverRequiredPart, businessRequiredPart;

	void goldSilverAndBusinessCalculation()
	{
		// Version 1.6
		goldRequiredPart = 0;
		silverRequiredPart = 0;
		businessRequiredPart = 0;

		if (madhab == madhabName.HANBALI /* Version 1.6 */ || madhab == madhabName.HANAFI)
		{
			// Version 1.6, (goldAmount*goldFinenessUnit/24) instead of goldAmount. The same for silver
			if ((((goldAmount * goldFinenessUnit / 24) / 85) + ((silverAmount * silverFinenessUnit / 1000) / 595) + ((goldValue * 85) > (silverValue * 595) ? (businessAmount / (silverValue * 595)) : (businessAmount / (goldValue * 85)))) >= 1)
			{
				if ((goldAmount * goldFinenessUnit / 24) < 85)
					goldRequiredPart = goldAmount / 40 * goldFinenessUnit / 24;

				if ((silverAmount * silverFinenessUnit / 1000) < 595)
					silverRequiredPart = silverAmount / 40 * silverFinenessUnit / 1000;

				float nesab;
				//if(madhab == madhabName.HANBALI || madhab == madhabName.HANAFI) // Version 2.0, This is always true
				nesab = (goldValue * 85) < (silverValue * 595) ? goldValue * 85 : silverValue * 595;
				//else
				// Calculation of money is referenced with gold
				//	nesab = goldValue*85;

				if (businessAmount < nesab)
					businessRequiredPart = businessAmount / 40;
			}
		}
	}

	void planetCalculation()
	{
		// Version 1.6, 653 instead of (5*60*2.4)
		if (freePlanetAmount >= 653)//*2.4 Kg in 1 sah
			freePlanetRequired = freePlanetAmount / 10;

		if (costPlanetAmount >= 653)
			costPlanetRequired = costPlanetAmount / 20;
	}

	void honeyCalculation()
	{
		// Version 1.6
		honeyRequired = 0;

		if (honeyAmount >= 62 && madhab == madhabName.HANBALI)
			honeyRequired = (float) honeyAmount / 10;

		// Version 1.6, madhab == madhabName.HANAFI (i.e. hanafi) also has honey zakat.
		if (madhab == madhabName.HANAFI)
			honeyRequired = (float) honeyAmount / 10;
	}

	class ZakatSystemResults extends JInternalFrame implements Printable
	{
		final JTable resultsTable;

		ZakatSystemResults()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "ZakatSystemResultsArabic.txt" : "ZakatSystemResultsEnglish.txt"));

			// Version 1.6, Make it local instead of global and convert it to Vector of Vector not Vector of ZakatResultsData.
			final Vector<Vector<String>> resultsDataVector = new Vector<>(10, 10);

			setTitle(translations[0]);
			setLayout(new BorderLayout());
			setMaximizable(true);
			setResizable(true);
			setFrameIcon(null);

			int madahebCount = 1;
			if (madhab == madhabName.ALL) madahebCount = 5;

			// Version 1.3, Calculation under all madahab.
			for (int y = 0; y < madahebCount; y++)
			{
				// For all cases
				if (madahebCount == 5)
				{
					// Version 1.6
					if (y == 0) madhab = madhabName.GUMHOUR;
					else
						if (y == 1) madhab = madhabName.HANBALI;
						else
							if (y == 2) madhab = madhabName.SHAFEE;
							else
								if (y == 3) madhab = madhabName.MALIKI;
								else
									if (y == 4) madhab = madhabName.HANAFI;

					resultsDataVector.addElement(toVector("", "", ""));
					if (y == 0)
						resultsDataVector.addElement(toVector("<html><div style='color:maroon;white-space:pre;'>" + translations[96], "", ""));
					if (y == 1)
						resultsDataVector.addElement(toVector("<html><div style='color:maroon;white-space:pre;'>" + translations[97], "", ""));
					if (y == 2)
						resultsDataVector.addElement(toVector("<html><div style='color:maroon;white-space:pre;'>" + translations[98], "", ""));
					if (y == 3)
						resultsDataVector.addElement(toVector("<html><div style='color:maroon;white-space:pre;'>" + translations[99], "", ""));
					if (y == 4)
						resultsDataVector.addElement(toVector("<html><div style='color:maroon;white-space:pre;'>" + translations[100], "", ""));
				}

				// Calling calculation functions
				goldAndSilverCalculation();
				otherMoneyCalculation();
				debtCalculation();
				animalsCalculation();
				businessCalculation();
				goldSilverAndBusinessCalculation();
				planetCalculation();
				honeyCalculation();

				if (camelNo != 0 || cowNo != 0 || sheepNo != 0 || (horseNo != 0 && madhab == madhabName.HANAFI) || goldAmount != 0 || silverAmount != 0 ||
						oreAmount != 0 || friendlyDebtAmount != 0 || uglyDebtAmount != 0 || businessAmount != 0 || freePlanetAmount != 0 ||
						costPlanetAmount != 0 || honeyAmount != 0)
				{
					// Gold and silver results
					// Version 1.6, change the condition from if(goldAmount!=0) to this condition:
					if ((goldAmount != 0 && (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI)) || ((goldRequired != 0 || (goldRequired == 0 && goldRequiredPart == 0 && goldAmount != 0)) && (madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)))
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[1], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (goldRequired != 0)
							resultsDataVector.addElement(toVector(translations[2], "<html><div style='white-space:pre;'>" + goldRequired + translations[3] + "<font color=green> (" + round(goldValue * goldRequired) + translations[110] + ')', translations[121]));
						else
							resultsDataVector.addElement(toVector(translations[4], translations[5], translations[6]));
					}

					// Version 1.6, change the condition from if(silverAmount!=0) to this condition:
					if ((silverAmount != 0 && (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI)) || ((silverRequired != 0 || (silverRequired == 0 && silverRequiredPart == 0 && silverAmount != 0)) && (madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)))
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[7], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (silverRequired != 0)
							resultsDataVector.addElement(toVector(translations[8], "<html><div style='white-space:pre;'>" + silverRequired + translations[9] + "<font color=green> (" + round(silverValue * silverRequired) + translations[110] + ')', translations[122]));
						else
							resultsDataVector.addElement(toVector(translations[10], translations[11], translations[12]));
					}

					// Ore results
					if (oreAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[18], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector(translations[19], String.valueOf(oreRequired), translations[126]));
					}

					// Business results
					// Version 1.6, change the condition from if(businessAmount!=0) to this condition:
					if ((businessAmount != 0 && (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI)) || ((businessRequired != 0 || (businessRequired == 0 && businessRequiredPart == 0 && businessAmount != 0)) && (madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)))
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[20], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (businessRequired != 0)
							resultsDataVector.addElement(toVector(translations[21], String.valueOf(businessRequired), (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI) ? translations[123] : translations[124]));
						else
							resultsDataVector.addElement(toVector(translations[22], translations[23], (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI) ? translations[24] : translations[125]));
					}

					// Gold, Silver And Business results
					if (goldRequiredPart != 0 || silverRequiredPart != 0 || businessRequiredPart != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:green;white-space:pre;'>" + translations[25], "<html><div style='color:green;white-space:pre;'>" + translations[26], "<html><div style='color:green;white-space:pre;'>" + translations[27]));
						resultsDataVector.addElement(toVector("", "", ""));

						if (goldRequiredPart != 0)
							resultsDataVector.addElement(toVector(translations[28], goldRequiredPart + translations[29], ""));

						if (silverRequiredPart != 0)
							resultsDataVector.addElement(toVector(translations[30], silverRequiredPart + translations[31], ""));

						if (businessRequiredPart != 0)
							resultsDataVector.addElement(toVector(translations[32], String.valueOf(businessRequiredPart), ""));
					}

					// Honey results
					if (honeyAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[33], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (madhab == madhabName.HANBALI || madhab == madhabName.HANAFI)
						{
							if (honeyRequired != 0)
								resultsDataVector.addElement(toVector(translations[34], String.valueOf(honeyRequired), (madhab == madhabName.HANBALI) ? translations[127] : translations[128]));
							else
								resultsDataVector.addElement(toVector(translations[35], translations[36], /*(madhab == madhabName.HANBALI)? no need since hanafi has alwasy honey zakat*/translations[37]));
						}
						else
							resultsDataVector.addElement(toVector(translations[38], translations[39], translations[40]));
					}

					// Planet results
					if (freePlanetAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[41], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (freePlanetRequired != 0)
							resultsDataVector.addElement(toVector(translations[42], String.valueOf(freePlanetRequired), translations[129]));
						else
							resultsDataVector.addElement(toVector(translations[43], translations[44], translations[45]));
					}

					if (costPlanetAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[46], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (costPlanetRequired != 0)
							resultsDataVector.addElement(toVector(translations[47], String.valueOf(costPlanetRequired), translations[130]));
						else
							resultsDataVector.addElement(toVector(translations[48], translations[49], translations[50]));
					}

					// Debt results
					if (friendlyDebtAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[51], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (friendlyDebtRequired != 0)
						{
							if (madhab == madhabName.HANBALI)
								resultsDataVector.addElement(toVector(translations[52], String.valueOf(friendlyDebtRequired), translations[53]));
							else
								resultsDataVector.addElement(toVector(translations[54], String.valueOf(friendlyDebtRequired), translations[55]));
						}
						else
							resultsDataVector.addElement(toVector(translations[56], translations[57], (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI) ? translations[58] : translations[131]));
					}

					if (uglyDebtAmount != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[59], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (uglyDebtRequired != 0)
						{
							if (madhab == madhabName.HANBALI)
								resultsDataVector.addElement(toVector(translations[60], String.valueOf(uglyDebtRequired), translations[61]));
							else
								resultsDataVector.addElement(toVector(translations[62], String.valueOf(uglyDebtRequired), translations[63]));
						}
						else
							resultsDataVector.addElement(toVector(translations[64], translations[65], (madhab != madhabName.HANBALI && madhab != madhabName.HANAFI) ? translations[66] : translations[131]));
					}

					// Animals Results
					if (camelNo != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[67], "", /* Version 1.5 */ madhab == madhabName.MALIKI ? translations[95] : ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (camelNo < 5)
							resultsDataVector.addElement(toVector(translations[68], translations[69], translations[70]));
						if (camelShaahRequired != 0)
							resultsDataVector.addElement(toVector(translations[71], String.valueOf(camelShaahRequired), ""));
						if (camelBintLaboonRequired != 0)
							resultsDataVector.addElement(toVector(translations[72], String.valueOf(camelBintLaboonRequired), ""));
						if (camelBinLaboonRequired != 0)
							resultsDataVector.addElement(toVector(translations[73], String.valueOf(camelBinLaboonRequired), ""));
						if (camelBintMakhadRequired != 0)
							resultsDataVector.addElement(toVector(translations[74], String.valueOf(camelBintMakhadRequired), ""));
						if (camelHuqaRequired != 0)
							resultsDataVector.addElement(toVector(translations[75], String.valueOf(camelHuqaRequired), ""));
						if (camelJadahRequired != 0)
							resultsDataVector.addElement(toVector(translations[76], String.valueOf(camelJadahRequired), ""));
					}

					if (cowNo != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[77], "", /* Version 1.5 */ madhab == madhabName.MALIKI ? translations[95] : ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (cowNo < 30)
							resultsDataVector.addElement(toVector(translations[78], translations[79], translations[80]));

						if (cowTabeeaOrTabeeahRequired != 0)
							resultsDataVector.addElement(toVector(translations[81], String.valueOf(cowTabeeaOrTabeeahRequired), ""));

						if (cowMassunahRequired != 0)
							resultsDataVector.addElement(toVector(translations[82], String.valueOf(cowMassunahRequired), ""));
					}

					if (sheepNo != 0)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[83], "", /* Version 1.5 */ madhab == madhabName.MALIKI ? translations[95] : ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (sheepNo < 40)
							resultsDataVector.addElement(toVector(translations[84], translations[85], translations[86]));

						if (sheepRequired != 0)
							resultsDataVector.addElement(toVector(translations[87], String.valueOf(sheepRequired), ""));
					}

					if (horseNo != 0 && madhab == madhabName.HANAFI)
					{
						resultsDataVector.addElement(toVector("", "", ""));
						resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[88], "", ""));
						resultsDataVector.addElement(toVector("", "", ""));

						if (horseNo < 5)
							resultsDataVector.addElement(toVector(translations[89], translations[90], translations[91]));

						if (horseMoneyRequired != 0)
							resultsDataVector.addElement(toVector(translations[92], String.valueOf(horseMoneyRequired), ""));
					}
				}
				else
					resultsDataVector.addElement(toVector("<html><div style='color:red;white-space:pre;'>" + translations[93], "", ""));
			}

			// Version 1.6, Removing the 'ZakatTableModel'.
			final Vector<String> columnNames = new Vector<>();
			columnNames.add(translations[13]);
			columnNames.add(translations[14]);
			columnNames.add(translations[15]);

			resultsTable = new JTable(resultsDataVector, columnNames)
			{
				public boolean isCellEditable(int row, int col)
				{
					return false;
				}
			};

			resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			resultsTable.getTableHeader().setReorderingAllowed(false);

			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			if (MaknoonIslamicEncyclopedia.language)
				renderer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

			for (int i = 0; i < resultsTable.getColumnCount(); i++)
			{
				final TableColumn column = resultsTable.getColumnModel().getColumn(i);
				column.setCellRenderer(renderer);
				if (i == 1) column.setPreferredWidth(50);
				if (i == 2) column.setPreferredWidth(300);
			}
			resultsTable.setPreferredScrollableViewportSize(new Dimension(900, 300));

			final JPanel zakatMainPanel = new JPanel(new BorderLayout());

			// Create the scroll pane and add the table to it.
			zakatMainPanel.add(new JScrollPane(resultsTable));

			final JPanel selectedZakatPanel = new JPanel(new BorderLayout());
			selectedZakatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[16], 0, 0, null, Color.red));

			final JPanel mainSelectedZakatPanel = new JPanel();
			mainSelectedZakatPanel.setLayout(new BoxLayout(mainSelectedZakatPanel, BoxLayout.Y_AXIS));
			selectedZakatPanel.add(mainSelectedZakatPanel, BorderLayout.CENTER);

			// Version 1.5, This condition is added to display that there is no entered entities.
			if (camelNo != 0 || cowNo != 0 || sheepNo != 0 || (horseNo != 0 && madhab == madhabName.HANAFI) || goldAmount != 0 || silverAmount != 0 ||
					oreAmount != 0 || friendlyDebtAmount != 0 || uglyDebtAmount != 0 || businessAmount != 0 || freePlanetAmount != 0 ||
					costPlanetAmount != 0 || honeyAmount != 0)
			{
				String selectedZakat;
				JLabel selectedZakatLabels;
				if (camelNo != 0 || cowNo != 0 || sheepNo != 0 || (horseNo != 0 && madhab == madhabName.HANAFI))
				{
					selectedZakat = "<font color=maroon>" + translations[101];
					if (camelNo != 0)
						selectedZakat = selectedZakat + " - " + translations[102] + "<font color=green> (" + camelNo + ")</font>";
					if (cowNo != 0)
						selectedZakat = selectedZakat + " - " + translations[103] + "<font color=green> (" + cowNo + ")</font>";
					if (sheepNo != 0)
						selectedZakat = selectedZakat + " - " + translations[104] + "<font color=green> (" + sheepNo + ")</font>";
					if (horseNo != 0)
						selectedZakat = selectedZakat + " - " + translations[105] + "<font color=green> (" + horseNo + ")</font>";

					selectedZakatLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedZakat + '.');
					final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
					decoratePanel.add(selectedZakatLabels);
					mainSelectedZakatPanel.add(decoratePanel);
				}

				if (goldAmount != 0 || silverAmount != 0 || oreAmount != 0 || friendlyDebtAmount != 0 || uglyDebtAmount != 0 || businessAmount != 0)
				{
					selectedZakat = "<font color=maroon>" + translations[109];
					if (goldAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[106] + "<font color=green> (" + goldAmount + translations[3] + '[' + translations[120] + goldFinenessUnit + "])</font>";
					if (silverAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[107] + "<font color=green> (" + silverAmount + translations[3] + '[' + translations[120] + (silverFinenessUnit == 1000 ? translations[108] : silverFinenessUnit) /* Version 2.0 */ + "])</font>";
					if (oreAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[111] + "<font color=green> (" + oreAmount + translations[110] + ")</font>";
					if (friendlyDebtAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[112] + "<font color=green> (" + friendlyDebtAmount + translations[110] + ")</font>";
					if (uglyDebtAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[113] + "<font color=green> (" + uglyDebtAmount + translations[110] + ")</font>";
					if (businessAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[114] + "<font color=green> (" + businessAmount + translations[110] + ")</font>";

					selectedZakatLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedZakat + '.');
					final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
					decoratePanel.add(selectedZakatLabels);
					mainSelectedZakatPanel.add(decoratePanel);
				}

				if (freePlanetAmount != 0 || costPlanetAmount != 0 || honeyAmount != 0)
				{
					selectedZakat = "<font color=maroon>" + translations[115];
					if (freePlanetAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[116] + "<font color=green> (" + freePlanetAmount + translations[117] + ")</font>";
					if (costPlanetAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[118] + "<font color=green> (" + costPlanetAmount + translations[117] + ")</font>";
					if (honeyAmount != 0)
						selectedZakat = selectedZakat + " - " + translations[119] + "<font color=green> (" + honeyAmount + translations[117] + ")</font>";

					selectedZakatLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedZakat + '.');
					final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
					decoratePanel.add(selectedZakatLabels);
					mainSelectedZakatPanel.add(decoratePanel);
				}
			}
			else
			{
				final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
				decoratePanel.add(new JLabel("<html><div style='color:maroon;white-space:pre;'>" + translations[94]));
				mainSelectedZakatPanel.add(decoratePanel);
			}

			final JPanel printPanel = new JPanel(new BorderLayout());
			final JButton printButton = new JButton(translations[17]);
			printButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Thread runner = new Thread()
					{
						public void run()
						{
							printData();
						}
					};
					runner.start();
				}
			});

			printPanel.add(printButton, BorderLayout.SOUTH);
			if (MaknoonIslamicEncyclopedia.language) selectedZakatPanel.add(printPanel, BorderLayout.WEST);
			else selectedZakatPanel.add(printPanel, BorderLayout.EAST);

			zakatMainPanel.add(selectedZakatPanel, BorderLayout.NORTH);
			add(zakatMainPanel);

			if (MaknoonIslamicEncyclopedia.language)
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

			ZakatSystem.this.add(this);
			pack();
			center(this);

			setVisible(true);
			try
			{
				this.setMaximum(true);
			}
			catch (java.beans.PropertyVetoException e)
			{
				e.printStackTrace();
			}
		}

		int maxNumPage = 1;

		public void printData()
		{
			try
			{
				maxNumPage = 1;
				final PrinterJob prnJob = PrinterJob.getPrinterJob();
				prnJob.setJobName("Zakat Results");
				prnJob.setPrintable(this);
				final PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
				if (prnJob.printDialog(attr))
					prnJob.print(attr);
			}
			catch (PrinterException e)
			{
				e.printStackTrace();
			}
		}

		public int print(Graphics pg, PageFormat pageFormat, int pageIndex)
		{
			if (pageIndex >= maxNumPage) return NO_SUCH_PAGE;

			pg.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
			int wPage = (int) pageFormat.getImageableWidth();
			int hPage = (int) pageFormat.getImageableHeight();

			int y = 0;

			pg.setColor(Color.BLUE);

			FontMetrics fm = pg.getFontMetrics();
			y += fm.getAscent();

			// Header Title
			String headerTitle;
			if (MaknoonIslamicEncyclopedia.language)
				headerTitle = "برنامج قسمة الزكاة من موسوعة مكنون الإسلامية";
			else
				headerTitle = "Zakat Program from Maknoon Islamic Encyclopedia";

			pg.drawString(headerTitle, (wPage - fm.stringWidth(headerTitle)) / 2, y);
			pg.setColor(Color.GRAY);

			y += 20; // space between title and table headers

			fm = pg.getFontMetrics();

			int nColumns = resultsTable.getColumnCount();
			int nRows = resultsTable.getRowCount();
			int[] x = new int[nColumns];
			x[0] = 0;

			// We will convert the resultsDataVector to 2D array
			final String[][] resultsDataArray = new String[nRows][nColumns];

			// This is used to know the color of the string
			final String[][] resultsHTMLDataArray = new String[nRows][nColumns];

			for (int i = 0; i < nRows; i++)
			{
				// Version 1.6
				for (int j = 0; j < nColumns; j++)
				{
					resultsDataArray[i][j] = MawarethSystem.HTMLFreeText((String) resultsTable.getValueAt(i, j));
					resultsHTMLDataArray[i][j] = (String) resultsTable.getValueAt(i, j);
				}
			}

			int h = fm.getAscent();
			y += h; // add ascent of header font because of baseline positioning

			int nCol;
			final TableColumnModel colModel = resultsTable.getColumnModel();
			for (nCol = 0; nCol < nColumns; nCol++)
			{
				final TableColumn tk = colModel.getColumn(nCol);
				//int width = tk.getWidth();
				final String title = (String) tk.getIdentifier();

				// here see the max width of the line
				int width = fm.stringWidth(title);
				for (int i = 0; i < resultsTable.getRowCount(); i++)
				{
					int len = fm.stringWidth(resultsDataArray[i][nCol]);
					if (width < len) width = len;
				}

				width = width + 10;

				if (x[nCol] + width > wPage)
				{
					nColumns = nCol;
					break;
				}

				if (nCol + 1 < nColumns)
					x[nCol + 1] = x[nCol] + width;

				if (MaknoonIslamicEncyclopedia.language)
					pg.drawString(title, wPage - fm.stringWidth(title) - x[nCol], y);
				else
					pg.drawString(title, x[nCol], y);
			}

			y += 20; // space between table header and table contents

			int header = y;
			h = fm.getHeight();

			final int rowH = Math.max((int) (h * 1.5), 10);
			final int rowPerPage = (hPage - header) / rowH;
			maxNumPage = Math.max((int) Math.ceil(resultsTable.getRowCount() / (double) rowPerPage), 1);

			int iniRow = pageIndex * rowPerPage;
			int endRow = Math.min(resultsTable.getRowCount(), iniRow + rowPerPage);

			for (int nRow = iniRow; nRow < endRow; nRow++)
			{
				y += h;
				for (nCol = 0; nCol < nColumns; nCol++)
				{
					String str = resultsHTMLDataArray[nRow][nCol];

					//if(str.indexOf("green")>-1)
					//	pg.setColor(Color.green);
					//else
					if (str.contains("=red"))// since Required is included !!!
						pg.setColor(Color.red);
					else
					{
						if (str.contains("maroon"))
							pg.setColor(new Color(128, 0, 0));
						else
							pg.setColor(Color.black);
					}

					str = resultsDataArray[nRow][nCol];

					if (MaknoonIslamicEncyclopedia.language)
						pg.drawString(str, wPage - fm.stringWidth(str) - x[nCol], y);
					else
						pg.drawString(str, x[nCol], y);
				}
			}

			// footer Title
			y += 20; // space between table and footer
			pg.setColor(Color.BLUE);
			pg.drawString("www.maknoon.com", (wPage - fm.stringWidth("www.maknoon.com")) / 2, y);

			System.gc();
			return PAGE_EXISTS;
		}
	}

	private static Vector<String> toVector(String e1, String e2, String e3)
	{
		Vector<String> v = new Vector<>();
		v.addElement(e1);
		v.addElement(e2);
		v.addElement(e3);

		return v;
	}

	// Version 1.3, This function is used to set the precision of the double in displaying the results.
	private static double round(double num)
	{
		BigDecimal val = new BigDecimal(num);
		val = val.setScale(2, RoundingMode.DOWN);
		return val.doubleValue();
	}

	static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public void center(final JInternalFrame frame)
	{
		final Rectangle frameBounds = frame.getBounds();
		final int width = (screenSize.width - frameBounds.width) / 2;
		final int height = (screenSize.height - frameBounds.height - 110) / 2;
		frame.setLocation(width, height);
	}
}