package com.maknoon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.NumericShaper;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.RoundingMode;
import java.net.URI;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

class MawarethSystem extends JDesktopPane
{
	private int cnt, khontha_p, khontha_w, arham_p, haml_p, bondman_p,
			missing_p, monasakha_c, monasakha_cnt, monasakha_s = 5/* Initially */,
	/*
	 * Version 1.2
	 * This variable is used to count the number of inheritors for horizontal Monasakha to not display this case
	 * after finishing selecting all inheritors
	 */
	horizontalMonasakhaCounter;
	private int[] inheritor;

	// Version 1.4, This enum will be used to indicate the madhab Name instead of integers.
	enum madhabName
	{
		GUMHOUR, MALIKI, HANBALI, SHAFEE, HANAFI
	}

	private madhabName madaheb = madhabName.GUMHOUR;

	private boolean maleSex = true;
	private boolean missingCaseSelected = false, hamlCaseSelected = false, khonthaCaseSelected = false,
			bondmanCaseSelected = false, /* Version 1.6 */
			willsCaseSelected = false;

	/*
	 * Version 1.2
	 * This variable is used to give the choice for the user whether he wants to use vertical monasakha
	 * or horizontal one.
	 */
	private boolean verticalMonasakha = true;

	/*
	 * Version 1.2
	 * This variable is used to give the choice for the user whether he wants to calculate arham case when each
	 * arham inheritor causes addition in the number of original inheritors.
	 */
	private boolean oneWayArhamChoice = false;


	// Version 1.9, The sex of the child who dead before his father.
	private boolean willsMaleSex = true;

	// Dont forget bin-abas, zayd bin thabit

	/*
	 * Version 1.1
	 * Making the size of the arrays dynamic i.e. at the run time to give the user the flexibility to assign the
	 * number of layers of monasakha case.
	 *
	 * Version 1.2
	 * An important update to this version is converting the whole code to be able to multiply, add, subtract
	 * and divide inheritance amount in fraction using Strings in the form: <int> / <int>.
	 */
	private String[] h;
	private String[] w;
	private String[] f;
	private String[] m;
	private String[] m_m;
	private String[] m_f;
	private String[] m_m_m;
	private String[] m_m_f;
	private String[] m_f_f;
	private String[] f_f;
	private String[] s;
	private String[] d;
	private String[] s_s;
	private String[] d_s;
	private String[] s_s_s;
	private String[] d_s_s;
	private String[] b;
	private String[] sister;
	private String[] m_b;
	private String[] m_sister;
	private String[] f_b;
	private String[] f_sister;
	private String[] s_b;
	private String[] s_b_f;
	private String[] u;
	private String[] u_f;
	private String[] s_u;
	private String[] s_u_f;

	// Version 1.6, Wills case inheritors.
	// Version 1.9, add w_s_d & w_d_d (was removed in Version 1.7)
	// Version 2.4, w_m / w_f added
	private String w_s_s, w_d_s, w_s_d, w_d_d, w_m, w_f;
	private int w_s_s_num, w_d_s_num, w_s_d_num, w_d_d_num, w_m_num, w_f_num;

	private int[] h_num;
	private int[] w_num;
	private int[] f_num;
	private int[] f_f_num;
	private int[] m_num;
	private int[] m_m_num;
	private int[] m_f_num;
	private int[] m_m_m_num;
	private int[] m_m_f_num;
	private int[] m_f_f_num;
	private int[] s_num;
	private int[] d_num;
	private int[] s_s_num;
	private int[] d_s_num;
	private int[] s_s_s_num;
	private int[] d_s_s_num;
	private int[] b_num;
	private int[] sister_num;
	private int[] m_b_num;
	private int[] m_sister_num;
	private int[] f_b_num;
	private int[] f_sister_num;
	private int[] s_b_num;
	private int[] s_b_f_num;
	private int[] u_num;
	private int[] u_f_num;
	private int[] s_u_num;
	private int[] s_u_f_num;

	// Version 1.7, This to calculate the base for each case.
	private int[] base; // base before calculating rud cases.
	private boolean[] faradWithTaseeb;
	private boolean[] foundAwle;

	/*
	 * Version 1.3
	 * Displaying some notes on how we get the result for each inheritor to the user as a teaching technique
	 */
	private String[] h_note;
	private String[] w_note;
	private String[] f_note;
	private String[] f_f_note;
	private String[] m_note;
	private String[] m_m_note;
	private String[] m_f_note;
	private String[] m_m_m_note;
	private String[] m_m_f_note;
	private String[] m_f_f_note;
	private String[] s_note;
	private String[] d_note;
	private String[] s_s_note;
	private String[] d_s_note;
	private String[] s_s_s_note;
	private String[] d_s_s_note;
	private String[] b_note;
	private String[] sister_note;
	private String[] m_b_note;
	private String[] m_sister_note;
	private String[] f_b_note;
	private String[] f_sister_note;
	private String[] s_b_note;
	private String[] s_b_f_note;
	private String[] u_note;
	private String[] u_f_note;
	private String[] s_u_note;
	private String[] s_u_f_note; /* Version 2.0 */
	private String[] haml_note;

	// Arham variables
	private String[] a_s_d;
	private String[] a_d_d;
	private String[] a_s_d_s;
	private String[] a_d_d_s;
	private String[] a_s_d_d;
	private String[] a_d_d_d;
	private String[] a_s_s_d;
	private String[] a_d_s_d;
	private String[] a_f_m;
	private String[] a_f_f_m;
	private String[] a_f_m_f;
	private String[] a_f_m_m;
	private String[] a_m_f_m;
	//private String[] a_m_m_f_m;
	private String[] a_s_sister;
	private String[] a_d_sister;
	private String[] a_d_b;// no a_s_b..........taaseeb
	private String[] a_d_msister;// msister.....mother sister
	private String[] a_s_msister;
	private String[] a_d_mb;// msister.....mother brother
	private String[] a_s_mb;
	private String[] a_s_fsister;// fsister.....father sister
	private String[] a_d_fsister;// fsister.....father sister
	private String[] a_d_fb;// no a_s_fb.........taaseeb
	private String[] a_ul;// all unclahs(ua)
	private String[] a_k;// Munc
	private String[] a_kl;// Mant
	private String[] a_s_ul;
	private String[] a_d_ul;
	private String[] a_d_u;
	private String[] a_s_k;
	private String[] a_d_k;
	private String[] a_s_kl;
	private String[] a_d_kl;
	//private String[] a_s_u_m;
	//private String[] a_d_u_m; // check point 3..6 in page 189 saabooony

	private int[] a_s_d_num;
	private int[] a_d_d_num;
	private int[] a_s_d_s_num;
	private int[] a_d_d_s_num;
	private int[] a_s_d_d_num;
	private int[] a_d_d_d_num;
	private int[] a_s_s_d_num;
	private int[] a_d_s_d_num;
	private int[] a_f_m_num;
	private int[] a_f_f_m_num;
	private int[] a_f_m_f_num;
	private int[] a_f_m_m_num;
	private int[] a_m_f_m_num;
	private int[] a_s_sister_num;
	private int[] a_d_sister_num;
	private int[] a_d_b_num;
	private int[] a_d_msister_num;
	private int[] a_s_msister_num;
	private int[] a_d_mb_num;
	private int[] a_s_mb_num;
	private int[] a_s_fsister_num;
	private int[] a_d_fsister_num;
	private int[] a_d_fb_num;
	private int[] a_ul_num;
	private int[] a_k_num;
	private int[] a_kl_num;
	private int[] a_s_ul_num;
	private int[] a_d_ul_num;
	private int[] a_d_u_num;
	private int[] a_s_k_num;
	private int[] a_d_k_num;
	private int[] a_s_kl_num;
	private int[] a_d_kl_num;

	/*
	 * Version 1.5
	 * Displaying some notes on how we get the result for each arham inheritor to the user as a teaching technique.
	 */
	private String[] a_s_d_note;
	private String[] a_d_d_note;
	private String[] a_s_d_s_note;
	private String[] a_d_d_s_note;
	private String[] a_s_d_d_note;
	private String[] a_d_d_d_note;
	private String[] a_s_s_d_note;
	private String[] a_d_s_d_note;
	private String[] a_f_m_note;
	private String[] a_f_f_m_note;
	private String[] a_f_m_f_note;
	private String[] a_f_m_m_note;
	private String[] a_m_f_m_note;
	private String[] a_s_sister_note;
	private String[] a_d_sister_note;
	private String[] a_d_b_note;
	private String[] a_d_msister_note;
	private String[] a_s_msister_note;
	private String[] a_d_mb_note;
	private String[] a_s_mb_note;
	private String[] a_s_fsister_note;
	private String[] a_d_fsister_note;
	private String[] a_d_fb_note;
	private String[] a_ul_note;
	private String[] a_k_note;
	private String[] a_kl_note;
	private String[] a_s_ul_note;
	private String[] a_d_ul_note;
	private String[] a_d_u_note;
	private String[] a_s_k_note;
	private String[] a_d_k_note;
	private String[] a_s_kl_note;
	private String[] a_d_kl_note;

	private String[] monasakha;
	private String[] muslim_trusts;
	private String[] monasakha_m;
	private String[] khontha;
	private String[] missing;
	private String[] haml;
	private String[] bondman;
	private final String[] mowqof = new String[31]; // For all inheritors

	private float tarekah;
	private final MaknoonIslamicEncyclopedia mie;

	MawarethSystem(final MaknoonIslamicEncyclopedia m)
	{
		mie = m;

		final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "MawarethSystemArabic.txt" : "MawarethSystemEnglish.txt"));

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
				String prefix;
				if (e.getSource().equals(menuItem2)) // Version 2.0
					prefix = "https://www.maknoon.com/community/threads/%d8%b9%d9%84%d9%85-%d8%a7%d9%84%d9%81%d8%b1%d8%a7%d8%a6%d8%b6.105/";
				else
					prefix = "https://www.maknoon.com/community/threads/%D8%A8%D8%B1%D9%86%D8%A7%D9%85%D8%AC-%D8%A7%D9%84%D9%85%D9%88%D8%A7%D8%B1%D9%8A%D8%AB-%D9%88%D8%A7%D9%84%D8%B2%D9%83%D8%A7%D8%A9.104/"; // #mawareth is not working, check http://stackoverflow.com/questions/300509/how-to-launch-a-file-protocol-url-with-an-anchor-from-java

				//try{Desktop.getDesktop().browse(new File(prefix).toURI());} // Version 2.1, 'browse' is not working in Mac OSX
				try
				{
					Desktop.getDesktop().browse(new URI(prefix));
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
						popup.show(e.getComponent(), e.getX() - popup.getPreferredSize().width + 2, e.getY());
					}
					else
						popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		if (MaknoonIslamicEncyclopedia.language)
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		new GeneralInformation();
	}

	// Version 1.7
	private boolean noTarekahColumn = true;
	class GeneralInformation extends JInternalFrame
	{
		GeneralInformation()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "GeneralInformationArabic.txt" : "GeneralInformationEnglish.txt"));

			setResizable(false);
			setTitle(translations[17]);
			setFrameIcon(null);

			final JRadioButton[] madahebRadioButton = new JRadioButton[6];
			for (int i = 0; i <= 5; i++)
				madahebRadioButton[i] = new JRadioButton(translations[i], false);

			// Initialize it to jumhor madhab
			madahebRadioButton[0].setSelected(true);

			final JRadioButton[] sixRadioButton = new JRadioButton[2];

			// Initialize it to male
			sixRadioButton[0] = new JRadioButton(translations[6], true);
			sixRadioButton[1] = new JRadioButton(translations[7], false);

			final JLabel monasakhaLayerLabel = new JLabel(translations[8]);
			final JComboBox<String> monasakhaLayerNumberComboBox = new JComboBox<>(new String[]{"5", "6", "7", "8", "9", "10"});
			monasakhaLayerNumberComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					monasakha_s = Integer.parseInt((String) Objects.requireNonNull(((JComboBox<?>) e.getSource()).getSelectedItem()));
				}
			});

			// Version 1.7, Set fixed this time to hide the parameter in the info frame for simplicity.
			monasakha_s = 15;

			final JTextField generalInformationFrameTextField = new JTextField(10);
			generalInformationFrameTextField.setText("10000");
			generalInformationFrameTextField.setEnabled(false);

			final JButton generalInformationFrameButton = new JButton(translations[9]);
			generalInformationFrameButton.setToolTipText(translations[10]);

			final JRadioButton yesTarekahButton = new JRadioButton(translations[11]);
			final JRadioButton noTarekahButton = new JRadioButton(translations[12], true);
			final ActionListener tarekahListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == yesTarekahButton)
					{
						generalInformationFrameTextField.setEnabled(true);
						noTarekahColumn = false;
					}
					else
					{
						generalInformationFrameTextField.setText("10000");
						generalInformationFrameTextField.setEnabled(false);
						noTarekahColumn = true;
					}
				}
			};
			yesTarekahButton.addActionListener(tarekahListener);
			noTarekahButton.addActionListener(tarekahListener);

			final ButtonGroup tarekahGroup = new ButtonGroup();
			tarekahGroup.add(yesTarekahButton);
			tarekahGroup.add(noTarekahButton);

			final ActionListener generalInformationListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					try
					{
						tarekah = Float.parseFloat(generalInformationFrameTextField.getText().trim());
						dispose();

						/*
						 * Version 1.1
						 * Making the size of the arrays dynamic i.e. at the run time to give the user the flexibility to assign the
						 * number of layers of monasakha case.
						 */
						inheritor = new int[monasakha_s];

						h = new String[monasakha_s];
						w = new String[monasakha_s];
						f = new String[monasakha_s];
						m = new String[monasakha_s];
						m_m = new String[monasakha_s];
						m_f = new String[monasakha_s];
						m_m_m = new String[monasakha_s];
						m_m_f = new String[monasakha_s];
						m_f_f = new String[monasakha_s];
						f_f = new String[monasakha_s];
						s = new String[monasakha_s];
						d = new String[monasakha_s];
						s_s = new String[monasakha_s];
						d_s = new String[monasakha_s];
						s_s_s = new String[monasakha_s];
						d_s_s = new String[monasakha_s];
						b = new String[monasakha_s];
						sister = new String[monasakha_s];
						m_b = new String[monasakha_s];
						m_sister = new String[monasakha_s];
						f_b = new String[monasakha_s];
						f_sister = new String[monasakha_s];
						s_b = new String[monasakha_s];
						s_b_f = new String[monasakha_s];
						u = new String[monasakha_s];
						u_f = new String[monasakha_s];
						s_u = new String[monasakha_s];
						s_u_f = new String[monasakha_s];

						h_num = new int[monasakha_s];
						w_num = new int[monasakha_s];
						f_num = new int[monasakha_s];
						f_f_num = new int[monasakha_s];
						m_num = new int[monasakha_s];
						m_m_num = new int[monasakha_s];
						m_f_num = new int[monasakha_s];
						m_m_m_num = new int[monasakha_s];
						m_m_f_num = new int[monasakha_s];
						m_f_f_num = new int[monasakha_s];
						s_num = new int[monasakha_s];
						d_num = new int[monasakha_s];
						s_s_num = new int[monasakha_s];
						d_s_num = new int[monasakha_s];
						s_s_s_num = new int[monasakha_s];
						d_s_s_num = new int[monasakha_s];
						b_num = new int[monasakha_s];
						sister_num = new int[monasakha_s];
						m_b_num = new int[monasakha_s];
						m_sister_num = new int[monasakha_s];
						f_b_num = new int[monasakha_s];
						f_sister_num = new int[monasakha_s];
						s_b_num = new int[monasakha_s];
						s_b_f_num = new int[monasakha_s];
						u_num = new int[monasakha_s];
						u_f_num = new int[monasakha_s];
						s_u_num = new int[monasakha_s];
						s_u_f_num = new int[monasakha_s];

						h_note = new String[monasakha_s];
						w_note = new String[monasakha_s];
						f_note = new String[monasakha_s];
						f_f_note = new String[monasakha_s];
						m_note = new String[monasakha_s];
						m_m_note = new String[monasakha_s];
						m_f_note = new String[monasakha_s];
						m_m_m_note = new String[monasakha_s];
						m_m_f_note = new String[monasakha_s];
						m_f_f_note = new String[monasakha_s];
						s_note = new String[monasakha_s];
						d_note = new String[monasakha_s];
						s_s_note = new String[monasakha_s];
						d_s_note = new String[monasakha_s];
						s_s_s_note = new String[monasakha_s];
						d_s_s_note = new String[monasakha_s];
						b_note = new String[monasakha_s];
						sister_note = new String[monasakha_s];
						m_b_note = new String[monasakha_s];
						m_sister_note = new String[monasakha_s];
						f_b_note = new String[monasakha_s];
						f_sister_note = new String[monasakha_s];
						s_b_note = new String[monasakha_s];
						s_b_f_note = new String[monasakha_s];
						u_note = new String[monasakha_s];
						u_f_note = new String[monasakha_s];
						s_u_note = new String[monasakha_s];
						s_u_f_note = new String[monasakha_s];

						// Arham variables
						a_s_d = new String[monasakha_s];
						a_d_d = new String[monasakha_s];
						a_s_d_s = new String[monasakha_s];
						a_d_d_s = new String[monasakha_s];
						a_s_d_d = new String[monasakha_s];
						a_d_d_d = new String[monasakha_s];
						a_s_s_d = new String[monasakha_s];
						a_d_s_d = new String[monasakha_s];
						a_f_m = new String[monasakha_s];
						a_f_f_m = new String[monasakha_s];
						a_f_m_f = new String[monasakha_s];
						a_f_m_m = new String[monasakha_s];
						a_m_f_m = new String[monasakha_s];/*a_m_m_f_m=new double[monasakha_s],*/
						a_s_sister = new String[monasakha_s];
						a_d_sister = new String[monasakha_s];
						a_d_b = new String[monasakha_s];// no a_s_b..........taaseeb
						a_d_msister = new String[monasakha_s];// msister.....mother sister
						a_s_msister = new String[monasakha_s];
						a_d_mb = new String[monasakha_s];// msister.....mother brother
						a_s_mb = new String[monasakha_s];
						a_s_fsister = new String[monasakha_s];// fsister.....father sister
						a_d_fsister = new String[monasakha_s];// fsister.....father sister
						a_d_fb = new String[monasakha_s];// no a_s_fb.........taaseeb
						a_ul = new String[monasakha_s];// all unclahs(ua)
						a_k = new String[monasakha_s];// Munc
						a_kl = new String[monasakha_s];// Mant
						a_s_ul = new String[monasakha_s];
						a_d_ul = new String[monasakha_s];
						a_d_u = new String[monasakha_s];
						a_s_k = new String[monasakha_s];
						a_d_k = new String[monasakha_s];
						a_s_kl = new String[monasakha_s];
						a_d_kl = new String[monasakha_s];/*a_s_u_m=new double[monasakha_s],*/
						/*a_d_u_m=new double[monasakha_s];*/// check point 3..6 in page 189 saabooony

						a_s_d_num = new int[monasakha_s];
						a_d_d_num = new int[monasakha_s];
						a_s_d_s_num = new int[monasakha_s];
						a_d_d_s_num = new int[monasakha_s];
						a_s_d_d_num = new int[monasakha_s];
						a_d_d_d_num = new int[monasakha_s];
						a_s_s_d_num = new int[monasakha_s];
						a_d_s_d_num = new int[monasakha_s];
						a_f_m_num = new int[monasakha_s];
						a_f_f_m_num = new int[monasakha_s];
						a_f_m_f_num = new int[monasakha_s];
						a_f_m_m_num = new int[monasakha_s];
						a_m_f_m_num = new int[monasakha_s];
						a_s_sister_num = new int[monasakha_s];
						a_d_sister_num = new int[monasakha_s];
						a_d_b_num = new int[monasakha_s];
						a_d_msister_num = new int[monasakha_s];
						a_s_msister_num = new int[monasakha_s];
						a_d_mb_num = new int[monasakha_s];
						a_s_mb_num = new int[monasakha_s];
						a_s_fsister_num = new int[monasakha_s];
						a_d_fsister_num = new int[monasakha_s];
						a_d_fb_num = new int[monasakha_s];
						a_ul_num = new int[monasakha_s];
						a_k_num = new int[monasakha_s];
						a_kl_num = new int[monasakha_s];
						a_s_ul_num = new int[monasakha_s];
						a_d_ul_num = new int[monasakha_s];
						a_d_u_num = new int[monasakha_s];
						a_s_k_num = new int[monasakha_s];
						a_d_k_num = new int[monasakha_s];
						a_s_kl_num = new int[monasakha_s];
						a_d_kl_num = new int[monasakha_s];

						a_s_d_note = new String[monasakha_s];
						a_d_d_note = new String[monasakha_s];
						a_s_d_s_note = new String[monasakha_s];
						a_d_d_s_note = new String[monasakha_s];
						a_s_d_d_note = new String[monasakha_s];
						a_d_d_d_note = new String[monasakha_s];
						a_s_s_d_note = new String[monasakha_s];
						a_d_s_d_note = new String[monasakha_s];
						a_f_m_note = new String[monasakha_s];
						a_f_f_m_note = new String[monasakha_s];
						a_f_m_f_note = new String[monasakha_s];
						a_f_m_m_note = new String[monasakha_s];
						a_m_f_m_note = new String[monasakha_s];
						a_s_sister_note = new String[monasakha_s];
						a_d_sister_note = new String[monasakha_s];
						a_d_b_note = new String[monasakha_s];
						a_d_msister_note = new String[monasakha_s];
						a_s_msister_note = new String[monasakha_s];
						a_d_mb_note = new String[monasakha_s];
						a_s_mb_note = new String[monasakha_s];
						a_s_fsister_note = new String[monasakha_s];
						a_d_fsister_note = new String[monasakha_s];
						a_d_fb_note = new String[monasakha_s];
						a_ul_note = new String[monasakha_s];
						a_k_note = new String[monasakha_s];
						a_kl_note = new String[monasakha_s];
						a_s_ul_note = new String[monasakha_s];
						a_d_ul_note = new String[monasakha_s];
						a_d_u_note = new String[monasakha_s];
						a_s_k_note = new String[monasakha_s];
						a_d_k_note = new String[monasakha_s];
						a_s_kl_note = new String[monasakha_s];
						a_d_kl_note = new String[monasakha_s];

						// Version 1.6
						w_s_s = "0/1";
						w_d_s = "0/1";
						w_s_d = "0/1";
						w_d_d = "0/1";

						// Version 2.4
						w_f = "0/1";
						w_m = "0/1";

						monasakha = new String[monasakha_s];
						muslim_trusts = new String[monasakha_s];
						monasakha_m = new String[monasakha_s];
						khontha = new String[monasakha_s];
						missing = new String[monasakha_s];
						bondman = new String[monasakha_s];
						haml = new String[monasakha_s];

						haml_note = new String[monasakha_s]; // Version 2.0

						// Version 1.7
						base = new int[monasakha_s]; // Initially 0
						faradWithTaseeb = new boolean[monasakha_s]; // Initially false
						foundAwle = new boolean[monasakha_s]; // Initially false

						// Initialisation
						for (int i = 0; i < monasakha_s; i++)
						{
							h[i] = "0/1";
							w[i] = "0/1";
							f[i] = "0/1";
							m[i] = "0/1";
							m_m[i] = "0/1";
							m_f[i] = "0/1";
							m_m_m[i] = "0/1";
							m_m_f[i] = "0/1";
							m_f_f[i] = "0/1";
							f_f[i] = "0/1";
							s[i] = "0/1";
							d[i] = "0/1";
							s_s[i] = "0/1";
							d_s[i] = "0/1";
							s_s_s[i] = "0/1";
							d_s_s[i] = "0/1";
							b[i] = "0/1";
							sister[i] = "0/1";
							m_b[i] = "0/1";
							m_sister[i] = "0/1";
							f_b[i] = "0/1";
							f_sister[i] = "0/1";
							s_b[i] = "0/1";
							s_b_f[i] = "0/1";
							u[i] = "0/1";
							u_f[i] = "0/1";
							s_u[i] = "0/1";
							s_u_f[i] = "0/1";

							// Arham variables
							a_s_d[i] = "0/1";
							a_d_d[i] = "0/1";
							a_s_d_s[i] = "0/1";
							a_d_d_s[i] = "0/1";
							a_s_d_d[i] = "0/1";
							a_d_d_d[i] = "0/1";
							a_s_s_d[i] = "0/1";
							a_d_s_d[i] = "0/1";
							a_f_m[i] = "0/1";
							a_f_f_m[i] = "0/1";
							a_f_m_f[i] = "0/1";
							a_f_m_m[i] = "0/1";
							a_m_f_m[i] = "0/1";
							//a_m_m_f_m[i] = new double[monasakha_s];
							a_s_sister[i] = "0/1";
							a_d_sister[i] = "0/1";
							a_d_b[i] = "0/1";// no a_s_b..........taaseeb
							a_d_msister[i] = "0/1";// msister.....mother sister
							a_s_msister[i] = "0/1";
							a_d_mb[i] = "0/1";// msister.....mother brother
							a_s_mb[i] = "0/1";
							a_s_fsister[i] = "0/1";// fsister.....father sister
							a_d_fsister[i] = "0/1";// fsister.....father sister
							a_d_fb[i] = "0/1";// no a_s_fb.........taaseeb
							a_ul[i] = "0/1";// all unclahs(ua)
							a_k[i] = "0/1";// Munc
							a_kl[i] = "0/1";// Mant
							a_s_ul[i] = "0/1";
							a_d_ul[i] = "0/1";
							a_d_u[i] = "0/1";
							a_s_k[i] = "0/1";
							a_d_k[i] = "0/1";
							a_s_kl[i] = "0/1";
							a_d_kl[i] = "0/1";

							monasakha[i] = "0/1";
							muslim_trusts[i] = "0/1";
							monasakha_m[i] = "0/1";
							khontha[i] = "0/1";
							bondman[i] = "0/1";
							missing[i] = "0/1";
							haml[i] = "0/1";
						}

						// Version 1.3
						for (int i = 0; i < mowqof.length; i++)
							mowqof[i] = "0/1";

						if (maleSex) new InheritorsInterface(true);
						else new InheritorsInterface(false);
					}
					catch (NumberFormatException nfe)
					{
						if (MaknoonIslamicEncyclopedia.language)
							JOptionPane.showOptionDialog(MawarethSystem.this, "أدخل قيمة صحيحة لمقدار التركة!", "تنبيه", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"متابعة"}, "متابعة");
						else
							JOptionPane.showMessageDialog(MawarethSystem.this, "Error, Please put a correct value!", "Error", JOptionPane.ERROR_MESSAGE);

						generalInformationFrameTextField.setText("10000");
						generalInformationFrameTextField.selectAll();
					}
				}
			};
			generalInformationFrameButton.addActionListener(generalInformationListener);
			generalInformationFrameTextField.addActionListener(generalInformationListener);

			final JPanel[] generalInformationFramePanels = new JPanel[4];
			for (int i = 0; i <= 3; i++)
			{
				generalInformationFramePanels[i] = new JPanel();
				generalInformationFramePanels[i].setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[i + 13], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));
			}

			//For madaheb Buttons
			final ActionListener madahebGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					/*
					for(int i=0; i<=5; i++)
						if(ae.getSource() == madahebRadioButton[i])
						{
							if(i == 5)allMadaheb = true;
							else
							{
								madaheb = i;
								allMadaheb = false;
							}
						}
					*/

					if (ae.getSource() == madahebRadioButton[0])
					{
						madaheb = madhabName.GUMHOUR;
						allMadaheb = false;
					}

					if (ae.getSource() == madahebRadioButton[1])
					{
						madaheb = madhabName.MALIKI;
						allMadaheb = false;
					}

					if (ae.getSource() == madahebRadioButton[2])
					{
						madaheb = madhabName.HANBALI;
						allMadaheb = false;
					}

					if (ae.getSource() == madahebRadioButton[3])
					{
						madaheb = madhabName.SHAFEE;
						allMadaheb = false;
					}

					if (ae.getSource() == madahebRadioButton[4])
					{
						madaheb = madhabName.HANAFI;
						allMadaheb = false;
					}

					if (ae.getSource() == madahebRadioButton[5])
						allMadaheb = true;
				}
			};

			final ButtonGroup madahebGroup = new ButtonGroup();
			for (int i = 0; i <= 5; i++)
			{
				madahebRadioButton[i].addActionListener(madahebGroupListener);
				madahebGroup.add(madahebRadioButton[i]);
				generalInformationFramePanels[0].add(madahebRadioButton[i]);
			}

			// For six Buttons
			final ActionListener sixGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == sixRadioButton[0]) maleSex = true;
					if (ae.getSource() == sixRadioButton[1]) maleSex = false;
				}
			};

			final ButtonGroup sixGroup = new ButtonGroup();
			for (int i = 0; i <= 1; i++)
			{
				sixRadioButton[i].addActionListener(sixGroupListener);
				sixGroup.add(sixRadioButton[i]);
			}

			/* Version 1.5
			 * Better than generalInformationFrameMainPanel.setLayout(new GridLayout(4,1))
			 * On the hand you will loose the JScrollPane i.e. you cannot see all the components
			 * if the screen is small.
			 */
			// Version 2.8, removed getContentPane()
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

			for (int i =/* Version 1.9 */MaknoonIslamicEncyclopedia.dubaiCourtsVer ? 1 : 0; i <= 3; i++)
			{
				switch (i)
				{
					case 1:
						generalInformationFramePanels[i].add(sixRadioButton[0]);
						generalInformationFramePanels[i].add(sixRadioButton[1]);
						break;
					case 2:
						generalInformationFramePanels[i].add(monasakhaLayerLabel);
						generalInformationFramePanels[i].add(monasakhaLayerNumberComboBox);
						break;
					case 3:
						generalInformationFramePanels[i].add(yesTarekahButton);
						generalInformationFramePanels[i].add(noTarekahButton);
						generalInformationFramePanels[i].add(generalInformationFrameTextField);
						generalInformationFramePanels[i].add(generalInformationFrameButton);
						break;
				}

				// Version 1.7, hide the monasakhaLayerLabel and set monasakha_s to 15 for simplicity.
				if (i != 2) add(generalInformationFramePanels[i]);
			}

			pack();
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
		}
	}

	class InheritorsInterface extends JInternalFrame
	{
		/*
		 * Version 1.1
		 * This panels are declared here to auto enable or disable them depending on your choice in
		 * online fashion. This case is happens at the beginning in monasakha case.
		 */
		final JPanel[] inheritorsFramePanel = new JPanel[28];

		InheritorsInterface(boolean male)
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "InheritorsInterfaceArabic.txt" : "InheritorsInterfaceEnglish.txt"));

			final JLabel[] inheritorsFrameLabel = new JLabel[28];
			final JRadioButton[] inheritorsFrameRadioButton = new JRadioButton[56];
			for (int i = 0, j = 0; i <= 27; i++)
			{
				inheritorsFrameLabel[i] = new JLabel(translations[i]);
				inheritorsFrameRadioButton[j] = new JRadioButton(translations[30], false);
				j++;
				inheritorsFrameRadioButton[j] = new JRadioButton(translations[31], true);
				j++;
			}

			final String[] patternExamples = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"};
			final JComboBox<String>[] inheritorsFrameComboBox = new JComboBox[19];

			// For wife
			inheritorsFrameComboBox[0] = new JComboBox<>(new String[]{"0", "1", "2", "3", "4"});
			inheritorsFrameComboBox[0].setEnabled(false);

			for (int i = 1; i <= 18; i++)
			{
				inheritorsFrameComboBox[i] = new JComboBox<>(patternExamples);
				inheritorsFrameComboBox[i].setEnabled(false);
			}

			final ActionListener inheritorsFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == inheritorsFrameRadioButton[0]) f_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[1]) f_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[2]) f_f_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[3]) f_f_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[4]) m_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[5]) m_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[6]) m_m_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[7]) m_m_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[8]) m_f_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[9]) m_f_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[10]) m_m_m_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[11]) m_m_m_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[12]) m_m_f_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[13]) m_m_f_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[14]) m_f_f_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[15]) m_f_f_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[16])
					{
						inheritorsFrameComboBox[0].setEnabled(true);
						inheritorsFrameComboBox[0].setSelectedIndex(1);
						w_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[17])
					{
						inheritorsFrameComboBox[0].setEnabled(false);
						inheritorsFrameComboBox[0].setSelectedIndex(0);
						w_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[18]) h_num[cnt] = 1;
					if (ae.getSource() == inheritorsFrameRadioButton[19]) h_num[cnt] = 0;
					if (ae.getSource() == inheritorsFrameRadioButton[20])
					{
						inheritorsFrameComboBox[1].setEnabled(true);
						inheritorsFrameComboBox[1].setSelectedIndex(1);
						s_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[21])
					{
						inheritorsFrameComboBox[1].setEnabled(false);
						inheritorsFrameComboBox[1].setSelectedIndex(0);
						s_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[22])
					{
						inheritorsFrameComboBox[2].setEnabled(true);
						inheritorsFrameComboBox[2].setSelectedIndex(1);
						d_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[23])
					{
						inheritorsFrameComboBox[2].setEnabled(false);
						inheritorsFrameComboBox[2].setSelectedIndex(0);
						d_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[24])
					{
						inheritorsFrameComboBox[3].setEnabled(true);
						inheritorsFrameComboBox[3].setSelectedIndex(1);
						s_s_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[25])
					{
						inheritorsFrameComboBox[3].setEnabled(false);
						inheritorsFrameComboBox[3].setSelectedIndex(0);
						s_s_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[26])
					{
						inheritorsFrameComboBox[4].setEnabled(true);
						inheritorsFrameComboBox[4].setSelectedIndex(1);
						d_s_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[27])
					{
						inheritorsFrameComboBox[4].setEnabled(false);
						inheritorsFrameComboBox[4].setSelectedIndex(0);
						d_s_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[28])
					{
						inheritorsFrameComboBox[5].setEnabled(true);
						inheritorsFrameComboBox[5].setSelectedIndex(1);
						s_s_s_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[29])
					{
						inheritorsFrameComboBox[5].setEnabled(false);
						inheritorsFrameComboBox[5].setSelectedIndex(0);
						s_s_s_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[30])
					{
						inheritorsFrameComboBox[6].setEnabled(true);
						inheritorsFrameComboBox[6].setSelectedIndex(1);
						d_s_s_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[31])
					{
						inheritorsFrameComboBox[6].setEnabled(false);
						inheritorsFrameComboBox[6].setSelectedIndex(0);
						d_s_s_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[32])
					{
						inheritorsFrameComboBox[9].setEnabled(true);
						inheritorsFrameComboBox[9].setSelectedIndex(1);
						m_b_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[33])
					{
						inheritorsFrameComboBox[9].setEnabled(false);
						inheritorsFrameComboBox[9].setSelectedIndex(0);
						m_b_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[34])
					{
						inheritorsFrameComboBox[10].setEnabled(true);
						inheritorsFrameComboBox[10].setSelectedIndex(1);
						m_sister_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[35])
					{
						inheritorsFrameComboBox[10].setEnabled(false);
						inheritorsFrameComboBox[10].setSelectedIndex(0);
						m_sister_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[36])
					{
						inheritorsFrameComboBox[7].setEnabled(true);
						inheritorsFrameComboBox[7].setSelectedIndex(1);
						b_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[37])
					{
						inheritorsFrameComboBox[7].setEnabled(false);
						inheritorsFrameComboBox[7].setSelectedIndex(0);
						b_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[38])
					{
						inheritorsFrameComboBox[8].setEnabled(true);
						inheritorsFrameComboBox[8].setSelectedIndex(1);
						sister_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[39])
					{
						inheritorsFrameComboBox[8].setEnabled(false);
						inheritorsFrameComboBox[8].setSelectedIndex(0);
						sister_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[40])
					{
						inheritorsFrameComboBox[11].setEnabled(true);
						inheritorsFrameComboBox[11].setSelectedIndex(1);
						f_b_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[41])
					{
						inheritorsFrameComboBox[11].setEnabled(false);
						inheritorsFrameComboBox[11].setSelectedIndex(0);
						f_b_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[42])
					{
						inheritorsFrameComboBox[12].setEnabled(true);
						inheritorsFrameComboBox[12].setSelectedIndex(1);
						f_sister_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[43])
					{
						inheritorsFrameComboBox[12].setEnabled(false);
						inheritorsFrameComboBox[12].setSelectedIndex(0);
						f_sister_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[44])
					{
						inheritorsFrameComboBox[13].setEnabled(true);
						inheritorsFrameComboBox[13].setSelectedIndex(1);
						s_b_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[45])
					{
						inheritorsFrameComboBox[13].setEnabled(false);
						inheritorsFrameComboBox[13].setSelectedIndex(0);
						s_b_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[46])
					{
						inheritorsFrameComboBox[14].setEnabled(true);
						inheritorsFrameComboBox[14].setSelectedIndex(1);
						s_b_f_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[47])
					{
						inheritorsFrameComboBox[14].setEnabled(false);
						inheritorsFrameComboBox[14].setSelectedIndex(0);
						s_b_f_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[48])
					{
						inheritorsFrameComboBox[15].setEnabled(true);
						inheritorsFrameComboBox[15].setSelectedIndex(1);
						u_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[49])
					{
						inheritorsFrameComboBox[15].setEnabled(false);
						inheritorsFrameComboBox[15].setSelectedIndex(0);
						u_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[50])
					{
						inheritorsFrameComboBox[16].setEnabled(true);
						inheritorsFrameComboBox[16].setSelectedIndex(1);
						u_f_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[51])
					{
						inheritorsFrameComboBox[16].setEnabled(false);
						inheritorsFrameComboBox[16].setSelectedIndex(0);
						u_f_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[52])
					{
						inheritorsFrameComboBox[17].setEnabled(true);
						inheritorsFrameComboBox[17].setSelectedIndex(1);
						s_u_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[53])
					{
						inheritorsFrameComboBox[17].setEnabled(false);
						inheritorsFrameComboBox[17].setSelectedIndex(0);
						s_u_num[cnt] = 0;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[54])
					{
						inheritorsFrameComboBox[18].setEnabled(true);
						inheritorsFrameComboBox[18].setSelectedIndex(1);
						s_u_f_num[cnt] = 1;
					}
					if (ae.getSource() == inheritorsFrameRadioButton[55])
					{
						inheritorsFrameComboBox[18].setEnabled(false);
						inheritorsFrameComboBox[18].setSelectedIndex(0);
						s_u_f_num[cnt] = 0;
					}

					/*
					 * Version 1.1
					 * This function is used to auto enable or disable inheritors panels depending on your
					 * choice in online fashion. This case happens at the begining in monasakha case.
					 */
					if (monasakha_c != 0 /* Version 1.6, Enable autoEnableInheritors() in allMadaheb case */ || allMadaheb || /* Version 1.8, Enable it in the 2nd round of Wills case */ willsCaseSelected)
						autoEnableInheritors();
				}
			};

			ButtonGroup inheritorsFrameRadioButtonGroup;
			for (int i = 0; i <= 55; )
			{
				inheritorsFrameRadioButtonGroup = new ButtonGroup();
				for (int j = 0; j <= 1; j++)
				{
					inheritorsFrameRadioButton[i].addActionListener(inheritorsFrameRadioButtonGroupListener);
					inheritorsFrameRadioButtonGroup.add(inheritorsFrameRadioButton[i]);
					i++;
				}
			}

			final ActionListener inheritorsFrameComboBoxGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (e.getSource() == inheritorsFrameComboBox[0])
					{
						w_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (w_num[cnt] == 0)
							inheritorsFrameRadioButton[17].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[1])
					{
						s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_num[cnt] == 0)
							inheritorsFrameRadioButton[21].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[2])
					{
						d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (d_num[cnt] == 0)
							inheritorsFrameRadioButton[23].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[3])
					{
						s_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_s_num[cnt] == 0)
							inheritorsFrameRadioButton[25].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[4])
					{
						d_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (d_s_num[cnt] == 0)
							inheritorsFrameRadioButton[27].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[5])
					{
						s_s_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_s_s_num[cnt] == 0)
							inheritorsFrameRadioButton[29].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[6])
					{
						d_s_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (d_s_s_num[cnt] == 0)
							inheritorsFrameRadioButton[31].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[7])
					{
						b_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (b_num[cnt] == 0)
							inheritorsFrameRadioButton[37].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[8])
					{
						sister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (sister_num[cnt] == 0)
							inheritorsFrameRadioButton[39].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[9])
					{
						m_b_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (m_b_num[cnt] == 0)
							inheritorsFrameRadioButton[33].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[10])
					{
						m_sister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (m_sister_num[cnt] == 0)
							inheritorsFrameRadioButton[35].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[11])
					{
						f_b_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (f_b_num[cnt] == 0)
							inheritorsFrameRadioButton[41].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[12])
					{
						f_sister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (f_sister_num[cnt] == 0)
							inheritorsFrameRadioButton[43].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[13])
					{
						s_b_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_b_num[cnt] == 0)
							inheritorsFrameRadioButton[45].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[14])
					{
						s_b_f_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_b_f_num[cnt] == 0)
							inheritorsFrameRadioButton[47].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[15])
					{
						u_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (u_num[cnt] == 0)
							inheritorsFrameRadioButton[49].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[16])
					{
						u_f_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (u_f_num[cnt] == 0)
							inheritorsFrameRadioButton[51].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[17])
					{
						s_u_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_u_num[cnt] == 0)
							inheritorsFrameRadioButton[53].doClick();
					}
					if (e.getSource() == inheritorsFrameComboBox[18])
					{
						s_u_f_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (s_u_f_num[cnt] == 0)
							inheritorsFrameRadioButton[55].doClick();
					}

					/*
					 * Version 1.1
					 * This function is used to auto enable or disable inheritors panels depending on your
					 * choice in online fashion. This case is happens at the beginning monasakha case.
					 */
					if (monasakha_c != 0 /* Version 1.6, Enable autoEnableInheritors() in allMadaheb case */ || allMadaheb || /* Version 1.8, Enable it in the 2nd round of Wills case */ willsCaseSelected)
						autoEnableInheritors();
				}
			};

			for (int i = 0; i <= 18; i++)
				inheritorsFrameComboBox[i].addActionListener(inheritorsFrameComboBoxGroupListener);

			final JPanel inheritorsFrameMainPanel = new JPanel(new GridLayout(14, 2));

			/*
			 * Version 1.1
			 * Previously inheritorsFramePanel was declare here as a single panel (i.e. not array of panels, which
			 * is good for memory) and override it each time but since we need now each panel to handle enabling
			 * or disabling them online in monasakha case.
			 */
			JPanel inheritorsFrameButtonPanel;
			for (int i = 0, q = 0; i <= 27; i++)
			{
				inheritorsFrameButtonPanel = new JPanel();
				inheritorsFramePanel[i] = new JPanel(new BorderLayout());
				inheritorsFramePanel[i].setBorder(BorderFactory.createEtchedBorder()); // Version 2.0

				switch (i)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 9:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						break;
					case 8:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[0]);
						break;
					case 10:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[1]);
						break;
					case 11:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[2]);
						break;
					case 12:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[3]);
						break;
					case 13:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[4]);
						break;
					case 14:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[5]);
						break;
					case 15:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[6]);
						break;
					case 16:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[9]);
						break;
					case 17:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[10]);
						break;
					case 18:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[7]);
						break;
					case 19:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[8]);
						break;
					case 20:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[11]);
						break;
					case 21:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[12]);
						break;
					case 22:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[13]);
						break;
					case 23:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[14]);
						break;
					case 24:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[15]);
						break;
					case 25:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[16]);
						break;
					case 26:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[17]);
						break;
					case 27:
						inheritorsFramePanel[i].add(inheritorsFrameLabel[i], BorderLayout.CENTER);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameRadioButton[q++]);
						inheritorsFrameButtonPanel.add(inheritorsFrameComboBox[18]);
						break;
				}

				if (MaknoonIslamicEncyclopedia.language)
					inheritorsFramePanel[i].add(inheritorsFrameButtonPanel, BorderLayout.WEST);
				else
					inheritorsFramePanel[i].add(inheritorsFrameButtonPanel, BorderLayout.EAST);

				inheritorsFrameMainPanel.add(inheritorsFramePanel[i]);
			}

			if (male)
				enabledContainer(inheritorsFramePanel[9], false);
				// -> inheritorsFrameRadioButton[18]&[19] and inheritorsFrameLabel[9]
			else
				enabledContainer(inheritorsFramePanel[8], false);
			// -> inheritorsFrameRadioButton[16]&[17] and inheritorsFrameLabel[8]

			setLayout(new BorderLayout());
			add(new JScrollPane(inheritorsFrameMainPanel), BorderLayout.CENTER);
			setTitle(translations[28]);
			setSize(screenSize.width-10, screenSize.height-200);
			setMaximizable(true);
			setResizable(true);
			setFrameIcon(null);

			final JButton inheritorsFrameButton = new JButton(translations[29]);
			add(inheritorsFrameButton, BorderLayout.SOUTH);
			inheritorsFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dispose();

					// Version 1.8, Handle Wills case
					if (willsCaseSelected) // i.e. if it is already selected before (2nd round)
					{
						inheritorsCalculation();

						// Decrease the counter [layer] since we are only interested in calculating the inheritance of the s_s, d_s(or s_d, d_d) of the deceased son (from the 2nd round)
						cnt--;

						// Version 2.4, m[] / f[] is added
						String dead = multiply(MawarethSystem.this.add(new String[]{multiply(s_num[cnt + 1], s[cnt + 1]), multiply(d_num[cnt + 1], d[cnt + 1]), m[cnt + 1], f[cnt + 1]}), /* Version 1.9 */willsMaleSex ? s[cnt] : d[cnt]);

						// Re-initialise, to not interfere in Taseeb conditions
						h[cnt] = "0/1"; // Version 1.9
						w[cnt] = "0/1";
						f[cnt] = "0/1";
						m[cnt] = "0/1";
						f_f[cnt] = "0/1";
						m_m[cnt] = "0/1";
						m_f[cnt] = "0/1";
						m_m_m[cnt] = "0/1";
						m_m_f[cnt] = "0/1";
						m_f_f[cnt] = "0/1";
						s[cnt] = "0/1";
						d[cnt] = "0/1";

						// Version 2.0, solve the errors when there is Hajb in the 1st round e.g. m, 4 w, 2 d, 1 sister, 3 w_d_s OR 2 d, 2 d_s, 2 s_s, 2 w_s_s, 2 w_d_s
						s_s[cnt] = "0/1";
						d_s[cnt] = "0/1";
						s_s_s[cnt] = "0/1";
						d_s_s[cnt] = "0/1";
						sister[cnt] = "0/1";
						b[cnt] = "0/1";
						m_b[cnt] = "0/1";
						m_sister[cnt] = "0/1";
						f_b[cnt] = "0/1";
						f_sister[cnt] = "0/1";
						s_b[cnt] = "0/1";
						s_b_f[cnt] = "0/1";
						u[cnt] = "0/1";
						u_f[cnt] = "0/1";
						s_u[cnt] = "0/1";
						s_u_f[cnt] = "0/1";

						// Decrease it again to reflect only the lived inheritors
						if (willsMaleSex) // Version 1.9
							s_num[cnt] = s_num[cnt] - 1;
						else
							d_num[cnt] = d_num[cnt] - 1;

						// It will override the previous numbers since they are the same inheritors (only s_num or d_num is less by one). We will not use cnt++ since this will affect displaying the results.
						inheritorsCalculation();

						if (bigger(dead, "1/3")) dead = "1/3";

						final String remainingTarekah = subtract(1, dead);

						if (h_num[cnt] != 0) h[cnt] = multiply(h[cnt], remainingTarekah); // Version 1.9
						if (w_num[cnt] != 0) w[cnt] = multiply(w[cnt], remainingTarekah);
						if (f_num[cnt] != 0) f[cnt] = multiply(f[cnt], remainingTarekah);
						if (m_num[cnt] != 0) m[cnt] = multiply(m[cnt], remainingTarekah);
						if (f_f_num[cnt] != 0) f_f[cnt] = multiply(f_f[cnt], remainingTarekah);
						if (m_m_num[cnt] != 0) m_m[cnt] = multiply(m_m[cnt], remainingTarekah);
						if (m_f_num[cnt] != 0) m_f[cnt] = multiply(m_f[cnt], remainingTarekah);
						if (m_m_m_num[cnt] != 0) m_m_m[cnt] = multiply(m_m_m[cnt], remainingTarekah);
						if (m_m_f_num[cnt] != 0) m_m_f[cnt] = multiply(m_m_f[cnt], remainingTarekah);
						if (m_f_f_num[cnt] != 0) m_f_f[cnt] = multiply(m_f_f[cnt], remainingTarekah);
						if (d_num[cnt] != 0) d[cnt] = multiply(d[cnt], remainingTarekah);
						if (s_num[cnt] != 0) s[cnt] = multiply(s[cnt], remainingTarekah);

						// Version 2.0
						if (s_s_num[cnt] != 0) s_s[cnt] = multiply(s_s[cnt], remainingTarekah);
						if (d_s_num[cnt] != 0) d_s[cnt] = multiply(d_s[cnt], remainingTarekah);
						if (s_s_s_num[cnt] != 0) s_s_s[cnt] = multiply(s_s_s[cnt], remainingTarekah);
						if (d_s_s_num[cnt] != 0) d_s_s[cnt] = multiply(d_s_s[cnt], remainingTarekah);
						if (sister_num[cnt] != 0) sister[cnt] = multiply(sister[cnt], remainingTarekah);
						if (b_num[cnt] != 0) b[cnt] = multiply(b[cnt], remainingTarekah);
						if (m_b_num[cnt] != 0) m_b[cnt] = multiply(m_b[cnt], remainingTarekah);
						if (m_sister_num[cnt] != 0) m_sister[cnt] = multiply(m_sister[cnt], remainingTarekah);
						if (f_b_num[cnt] != 0) f_b[cnt] = multiply(f_b[cnt], remainingTarekah);
						if (f_sister_num[cnt] != 0) f_sister[cnt] = multiply(f_sister[cnt], remainingTarekah);
						if (s_b_num[cnt] != 0) s_b[cnt] = multiply(s_b[cnt], remainingTarekah);
						if (s_b_f_num[cnt] != 0) s_b_f[cnt] = multiply(s_b_f[cnt], remainingTarekah);
						if (u_num[cnt] != 0) u[cnt] = multiply(u[cnt], remainingTarekah);
						if (u_f_num[cnt] != 0) u_f[cnt] = multiply(u_f[cnt], remainingTarekah);
						if (s_u_num[cnt] != 0) s_u[cnt] = multiply(s_u[cnt], remainingTarekah);
						if (s_u_f_num[cnt] != 0) s_u_f[cnt] = multiply(s_u_f[cnt], remainingTarekah);

						// Version 2.4
						w_m_num = m_num[cnt + 1];
						w_f_num = f_num[cnt + 1];

						if (w_f_num == 1)
							w_f = multiply(dead, "1/6");

						if (w_m_num == 1)
							w_m = multiply(dead, "1/6");

						dead = subtract(dead, w_f);
						dead = subtract(dead, w_m);

						if (willsMaleSex) // Version 1.9
						{
							w_s_s_num = s_num[cnt + 1];
							w_d_s_num = d_num[cnt + 1];

							if (w_s_s_num > 0)
							{
								if (w_d_s_num == 0)
									w_s_s = divide(dead, w_s_s_num);
								else
								{
									w_s_s = multiply(divide(dead, (w_d_s_num + w_s_s_num * 2)), 2);
									w_d_s = divide(dead, (w_d_s_num + w_s_s_num * 2));
								}
							}
							else
								if (w_d_s_num > 0)
									w_d_s = divide(dead, w_d_s_num);
						}
						else
						{
							w_s_d_num = s_num[cnt + 1];
							w_d_d_num = d_num[cnt + 1];

							if (w_s_d_num > 0)
							{
								if (w_d_d_num == 0)
									w_s_d = divide(dead, w_s_d_num);
								else
								{
									w_s_d = multiply(divide(dead, (w_d_d_num + w_s_d_num * 2)), 2);
									w_d_d = divide(dead, (w_d_d_num + w_s_d_num * 2));
								}
							}
							else
								if (w_d_d_num > 0)
									w_d_d = divide(dead, w_d_d_num);
						}

						new MawarethSystemResults();
						return;
					}

					/*
					 * Version 1.1
					 * Include Arham case when:
					 *
					 * 1. There is h[cnt] or w[cnt]
					 * 2. Under all madhab case.
					 */
					int casechoice;
					if (m_num[cnt] == 0 && m_sister_num[cnt] == 0 && m_b_num[cnt] == 0 && b_num[cnt] == 0
							&& f_num[cnt] == 0 && f_f_num[cnt] == 0 && m_m_num[cnt] == 0 && m_f_num[cnt] == 0
							&& m_m_m_num[cnt] == 0 && m_m_f_num[cnt] == 0 && m_f_f_num[cnt] == 0 && s_num[cnt] == 0
							&& d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
							&& d_s_s_num[cnt] == 0 && sister_num[cnt] == 0 && f_b_num[cnt] == 0 && f_sister_num[cnt] == 0
							&& s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0 && u_num[cnt] == 0 && u_f_num[cnt] == 0
							&& s_u_num[cnt] == 0 && s_u_f_num[cnt] == 0)
					{
						if (h_num[cnt] == 0 && w_num[cnt] == 0)
						{
							// Version 1.8, Removed the if((madaheb != madhabName.SHAFEE ... since it is enable now for mirath under late SHAFEE & MALIKI scholars
							//if((madaheb != madhabName.SHAFEE && madaheb != madhabName.MALIKI) /* Version 1.6, To pass it in case of selecting MALIKI/SHAFEE then allMadaheb */ || allMadaheb)
							{
								if (allMadaheb)
									// Disable all except arham since we cannot apply all madaheb choice for these cases.
									casechoice = 8;
								else
									// Disable monasakha, khontha and missing cases
									casechoice = 2;
							}
							//else
							/*
							 * Put here comments on the screen to teach them that malek and shafee
							 * not consider arham case.
							 *
							 * Disable arham, monasakhah, khontha and missing cases
							 */
							//	casechoice = 4;
						}
						else
						{
							// Version 1.8, Removed the if((madaheb != madhabName.SHAFEE ... since it is enable now for mirath under late SHAFEE & MALIKI scholars
							//if((madaheb != madhabName.SHAFEE && madaheb != madhabName.MALIKI) /* Version 1.6, To pass it in case of selecting MALIKI/SHAFEE then allMadaheb */ || allMadaheb)
							{
								if (allMadaheb)
									// Disable all except arham since we cannot apply all madaheb choice for these cases.
									casechoice = 8;
								else
									// Disable khontha
									casechoice = 6;
							}
							//else
							// Disable arham and khontha cases
							//	casechoice = 7;
						}

						if (monasakha_c != 0)
						{
							if ((cnt + 1) < monasakha_s /* Version 1.7, to exclude vertical monasakha case when no inheritors */ && (h_num[cnt] != 0 || w_num[cnt] != 0 || !verticalMonasakha))
							{
								// Version 1.2,  This condition to not display this case after finishing selecting all inheritors.
								if (cnt >= horizontalMonasakhaCounter && horizontalMonasakhaCounter != 0)
									// Disable all cases
									new SpecialCasesInterface(5);
								else
									// Disable all except monasakha case
									new SpecialCasesInterface(3);
							}
							else
								// Disable all cases
								new SpecialCasesInterface(5);
						}
						else
							// Disable arham choice
							new SpecialCasesInterface(casechoice);
					}
					else
					{
						if (allMadaheb)
							// Disable all
							casechoice = 5;
						else
						{
							// Version 1.7, Disable Khontha case when there is no possiblity for one !
							if (s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 &&
									s_s_s_num[cnt] == 0 && d_s_s_num[cnt] == 0 && b_num[cnt] == 0 && sister_num[cnt] == 0 &&
									m_b_num[cnt] == 0 && m_sister_num[cnt] == 0 && f_b_num[cnt] == 0 && f_sister_num[cnt] == 0 &&
									s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0 && u_num[cnt] == 0 && u_f_num[cnt] == 0 && s_u_num[cnt] == 0 &&
									s_u_f_num[cnt] == 0)
								// Disable arham & khontha cases
								casechoice = 7;
							else
								// Disable arham choice
								casechoice = 1;
						}

						// This condition will not be applicable when all madhab case
						if (monasakha_c != 0)
						{
							if ((cnt + 1) < monasakha_s)
							{
								// Version 1.2, This condition to not display this case after finishing selecting all inheritors.
								if (cnt >= horizontalMonasakhaCounter && horizontalMonasakhaCounter != 0)
									// Disable all cases
									new SpecialCasesInterface(5);
								else
									// Disable all except monasakha case
									new SpecialCasesInterface(3);
							}
							else
								// Disable all cases
								new SpecialCasesInterface(5);
						}
						else
							new SpecialCasesInterface(casechoice);
					}
				}
			});

			//pack(); It causes layout issue (bug) in RTL in Metal LaF
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
			try
			{
				setMaximum(true);
			}
			catch (java.beans.PropertyVetoException e)
			{
				e.printStackTrace();
			}
		}

		/*
		 * Version 1.1
		 * This is used to auto enable or disable Panels depending on your choice in online fashion.
		 * This case happends in monasakha case and All-Madaheb case.
		 */
		public void autoEnableInheritors()
		{
			for (int i = 0; i <= 27; i++)
			{
				// Enable all except father, mother, wife, hausbend, sun and daughter since no one can block them
				if (i != 0 && i != 2 && i != 8 && i != 9 && i != 10 && i != 11) // Remove this condition, No need for it.
					enabledContainer(inheritorsFramePanel[i], true);
			}

			if (f_num[cnt] != 0)
				// Disable f_f[cnt]
				enabledContainer(inheritorsFramePanel[1], false);

			if (m_num[cnt] != 0)
			{
				// Disable m_m[cnt] && m_f[cnt] && m_m_m[cnt] && m_m_f[cnt] && m_f_f[cnt]
				for (int i = 3; i <= 7; i++)
					enabledContainer(inheritorsFramePanel[i], false);
			}
			else
			{
				if (m_m_num[cnt] != 0)
				{
					if (m_f_num[cnt] != 0)
					{
						// Disable m_m_m[cnt] && m_m_f[cnt] && m_f_f[cnt]
						for (int i = 5; i <= 7; i++)
							enabledContainer(inheritorsFramePanel[i], false);
					}
					else
					{
						// You should check madaheb = madhabName.GUMHOUR ........ see page 403 saboony
						if ((madaheb == madhabName.SHAFEE || madaheb == madhabName.MALIKI || madaheb == madhabName.GUMHOUR) /* Version 1.6, To enable autoEnableInheritors in allMadaheb case */ && !allMadaheb)
						{
							// Disable m_m_m[cnt]
							enabledContainer(inheritorsFramePanel[5], false);
						}
					}
				}
			}

			if (s_num[cnt] != 0)
			{
				// Disable s_s[cnt] && d_s[cnt] && s_s_s[cnt] && d_s_s[cnt]
				for (int i = 12; i <= 15; i++)
					enabledContainer(inheritorsFramePanel[i], false);
			}
			else
			{
				if (s_s_num[cnt] != 0)
				{
					// Disable s_s_s[cnt] && d_s_s[cnt]
					enabledContainer(inheritorsFramePanel[14], false);
					enabledContainer(inheritorsFramePanel[15], false);
				}
			}

			// Please continue the conditions
            /* Version 2.0, Removed: Not needed
			if(!(m_num[cnt]>0 || (f_num[cnt]==0 && s_num[cnt]==0 && s_s_num[cnt]==0 && s_s_s_num[cnt]==0)
				|| d_num[cnt]>0 || d_s_num[cnt]>0 || d_s_s_num[cnt]>0))
			{
				/*
				 * Disable m_b[cnt] && m_sister[cnt] && b[cnt] && sister[cnt]&& f_b[cnt] && f_sister[cnt] &&
				 * s_b[cnt] && s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
				 *
				for(int i=16; i<=27; i++)
					enabledContainer(inheritorsFramePanel[i], false);
			}
			else
			*/
			{
				if (!(f_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0))
				{
					/*
					 * Disable m_b[cnt] && m_sister[cnt](Please check since it was there in all mawareth versions).
					 * In addition, disable b[cnt] && sister[cnt] && f_b[cnt] && f_sister[cnt] && s_b[cnt] &&
					 * s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
					 */
					for (int i = 16; i <= 27; i++)
						enabledContainer(inheritorsFramePanel[i], false);
				}
				else
				{
					// Version 2.0
					if (f_f_num[cnt] > 0 && f_num[cnt] == 0)
					{
						if (madaheb == madhabName.HANAFI)
						{
							/*
							 * Disable m_b[cnt] && m_sister[cnt] && b[cnt] && sister[cnt] && f_b[cnt] && f_sister[cnt] && s_b[cnt] &&
							 * s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
							 */
							for (int i = 16; i <= 27; i++)
								enabledContainer(inheritorsFramePanel[i], false);
						}
						else
						{
							// Disable s_b[cnt] && s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
							for (int i = 22; i <= 27; i++)
								enabledContainer(inheritorsFramePanel[i], false);

							// Even if m_b[cnt] && m_sister[cnt] are not included in all cases but they have affects in the special cases e.g. Al-Malikia
							if ((madaheb == madhabName.HANBALI || madaheb == madhabName.SHAFEE || madaheb == madhabName.GUMHOUR) && !allMadaheb)
							{
								enabledContainer(inheritorsFramePanel[16], false);
								enabledContainer(inheritorsFramePanel[17], false);
							}
						}
					}

					if (!((b_num[cnt] == 0 || f_f_num[cnt] > 0) && madaheb != madhabName.HANAFI) /* Version 1.6, To enable autoEnableInheritors in allMadaheb case */ && !allMadaheb)
					{
						// Disable f_b[cnt] && f_sister[cnt] && s_b[cnt] && s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
						for (int i = 20; i <= 27; i++)
							enabledContainer(inheritorsFramePanel[i], false);
					}
					else
					{
						if (f_b_num[cnt] != 0)
						{
							// Disable s_b[cnt] && s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
							for (int i = 22; i <= 27; i++)
								enabledContainer(inheritorsFramePanel[i], false);
						}
						else
						{
							if (s_b_num[cnt] != 0)
							{
								// Disable s_b_f[cnt] && u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
								for (int i = 23; i <= 27; i++)
									enabledContainer(inheritorsFramePanel[i], false);
							}
							else
							{
								if (s_b_f_num[cnt] != 0)
								{
									// Disable u[cnt] && u_f[cnt] && s_u[cnt] && s_u_f[cnt]
									for (int i = 24; i <= 27; i++)
										enabledContainer(inheritorsFramePanel[i], false);
								}
								else
								{
									if (u_num[cnt] != 0)
									{
										// Disable u_f[cnt] && s_u[cnt] && s_u_f[cnt]
										for (int i = 25; i <= 27; i++)
											enabledContainer(inheritorsFramePanel[i], false);
									}
									else
									{
										if (u_f_num[cnt] != 0)
										{
											// Disable s_u[cnt] && s_u_f[cnt]
											enabledContainer(inheritorsFramePanel[26], false);
											enabledContainer(inheritorsFramePanel[27], false);
										}
										else
										{
											if (s_u_num[cnt] != 0)
											{
												// Disable s_u_f[cnt]
												enabledContainer(inheritorsFramePanel[27], false);
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

	/*
	 * Version 1.1
	 * For monasakha case be aware that monasakha_c is assign to 0 after choosing nothing case
	 * in the second time.
	 * For this reason we will have another variable to indicate this for further improvement if needed.
	 */
	private boolean monasakhaCaseSelected = false;
	private boolean lastRotation = false;
	private boolean allMadaheb = false;

	// Version 1.8/2.0, Adding Arham case under late Shafee & Maliki scholars
	private JCheckBox arhamChoiceCheckBox;

	class SpecialCasesInterface extends JInternalFrame
	{
		boolean enterchoice;

		SpecialCasesInterface(final int caseEnabledChoice)
		{
			/*
			 * SpecialCasesInterface choices
			 *
			 * caseEnabledChoice=1; Disable arham choice
			 * caseEnabledChoice=2; Disable monasakha, khontha and missing choices
			 * caseEnabledChoice=3; Disable all except monasakha choice
			 * caseEnabledChoice=4; Disable arham, monasakhah, khontha and missing cases
			 * caseEnabledChoice=5; Disable all
			 *
			 * Version 1.1
			 * caseEnabledChoice=6; Disable khontha
			 * caseEnabledChoice=7; Disable arham and khontha cases
			 * caseEnabledChoice=8; Disable all except arham
			 *
			 * Version 1.6
			 * In all of these cases 'Wills diligence' case is disable.
			 */
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "SpecialCasesInterfaceArabic.txt" : "SpecialCasesInterfaceEnglish.txt"));

			setResizable(false);
			setTitle(translations[35]);
			setFrameIcon(null);
			setLayout(new BorderLayout());
			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);

			final JPanel specialFrameMainPanel = new JPanel(new GridLayout(8, 1));
			specialFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[0], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));
			add(specialFrameMainPanel, BorderLayout.NORTH);

			final JPanel mainPanel = new JPanel(new BorderLayout());
			add(mainPanel, BorderLayout.CENTER);

			// Version 1.6, Adding 'Wills diligence' case.
			final JRadioButton[] specialFrameRadioButton = new JRadioButton[8];

			// Version 1.8, Adding Arham case under late Shafee & Maliki scholars
			specialFrameRadioButton[0] = new JRadioButton(translations[1], false);
			final JPanel arhamChoicePanel = new JPanel(new BorderLayout());
			specialFrameMainPanel.add(arhamChoicePanel);
			arhamChoicePanel.add(specialFrameRadioButton[0], BorderLayout.CENTER);

			arhamChoiceCheckBox = new JCheckBox(translations[2]);
			arhamChoiceCheckBox.setOpaque(false);
			arhamChoiceCheckBox.setEnabled(false);
			if (MaknoonIslamicEncyclopedia.language) arhamChoicePanel.add(arhamChoiceCheckBox, BorderLayout.WEST);
			else arhamChoicePanel.add(arhamChoiceCheckBox, BorderLayout.EAST);

			for (int i = 1; i < 8; i++)
			{
				specialFrameRadioButton[i] = new JRadioButton(translations[i + 2], false);
				if (i != 6 || (/* no need: i==6 && */MaknoonIslamicEncyclopedia.dubaiCourtsVer))
					specialFrameMainPanel.add(specialFrameRadioButton[i]);
			}

			if (!MaknoonIslamicEncyclopedia.dubaiCourtsVer) // Version 1.9
				specialFrameMainPanel.setLayout(new GridLayout(7, 1));

			// Initially
			specialFrameRadioButton[7].setSelected(true);

			// To cancel some features
			if (caseEnabledChoice == 1)
			{
				specialFrameRadioButton[0].setEnabled(false);

				// Version 1.9, removed.
				//if(!(s_num[cnt]>0)) // Version 1.6
				//specialFrameRadioButton[6].setEnabled(false);
			}

			if (caseEnabledChoice == 2)
			{
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[3].setEnabled(false);
				specialFrameRadioButton[4].setEnabled(false);
				specialFrameRadioButton[5].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 3)
			{
				specialFrameRadioButton[0].setEnabled(false);
				specialFrameRadioButton[1].setEnabled(false);
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[4].setEnabled(false);
				specialFrameRadioButton[5].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 4)
			{
				specialFrameRadioButton[0].setEnabled(false);
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[3].setEnabled(false);
				specialFrameRadioButton[4].setEnabled(false);
				specialFrameRadioButton[5].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 5)
			{
				specialFrameRadioButton[0].setEnabled(false);
				specialFrameRadioButton[1].setEnabled(false);
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[3].setEnabled(false);
				specialFrameRadioButton[4].setEnabled(false);
				specialFrameRadioButton[5].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 6)
			{
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 7)
			{
				specialFrameRadioButton[0].setEnabled(false);
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			if (caseEnabledChoice == 8)
			{
				specialFrameRadioButton[1].setEnabled(false);
				specialFrameRadioButton[2].setEnabled(false);
				specialFrameRadioButton[3].setEnabled(false);
				specialFrameRadioButton[4].setEnabled(false);
				specialFrameRadioButton[5].setEnabled(false);
				specialFrameRadioButton[6].setEnabled(false); // Version 1.6
			}

			// Version 1.2, Adding bondman cases for only hanbali madhab.
			if (madaheb != madhabName.HANBALI)
				specialFrameRadioButton[5].setEnabled(false);

			// Version 1.2, Adding monasakha cases i.e. vertical and horizontal monasakha choices.
			final JPanel verticalHorizontalMonasakhaChoices = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
			verticalHorizontalMonasakhaChoices.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[10], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JPanel monasakhaAndKhonthaCasesPanel = new JPanel(new BorderLayout());
			monasakhaAndKhonthaCasesPanel.add(verticalHorizontalMonasakhaChoices, BorderLayout.CENTER);
			mainPanel.add(monasakhaAndKhonthaCasesPanel, BorderLayout.NORTH);

			final JRadioButton verticalMonasakhaRadioButton = new JRadioButton(translations[11]);
			final JRadioButton horizontalMonasakhaRadioButton = new JRadioButton(translations[12]);
			verticalMonasakhaRadioButton.setEnabled(false);
			horizontalMonasakhaRadioButton.setEnabled(false);

			verticalHorizontalMonasakhaChoices.add(horizontalMonasakhaRadioButton);
			verticalHorizontalMonasakhaChoices.add(verticalMonasakhaRadioButton);

			final ActionListener monasakhaGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == verticalMonasakhaRadioButton)
						verticalMonasakha = true;

					if (ae.getSource() == horizontalMonasakhaRadioButton)
						verticalMonasakha = false;
				}
			};
			verticalMonasakhaRadioButton.addActionListener(monasakhaGroupListener);
			horizontalMonasakhaRadioButton.addActionListener(monasakhaGroupListener);

			final ButtonGroup monasakhaGroup = new ButtonGroup();
			monasakhaGroup.add(verticalMonasakhaRadioButton);
			monasakhaGroup.add(horizontalMonasakhaRadioButton);

			if (verticalMonasakha)
				verticalMonasakhaRadioButton.setSelected(true);
			else
				horizontalMonasakhaRadioButton.setSelected(true);

			// Version 1.3
			final JButton garaqLabel = new JButton(translations[13]);
			garaqLabel.setEnabled(false);
			garaqLabel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					JOptionPane.showOptionDialog(MawarethSystem.this, translations[27] + System.lineSeparator() + translations[28] + System.lineSeparator() + translations[29] + System.lineSeparator() +
							translations[30] + System.lineSeparator() + translations[31], translations[26], JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{translations[21]}, translations[21]);
				}
			});
			verticalHorizontalMonasakhaChoices.add(new JPanel());
			verticalHorizontalMonasakhaChoices.add(garaqLabel);

			// Version 1.3, Adding Khontha case choices
			final JPanel khonthaChoices = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
			khonthaChoices.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[14], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));
			monasakhaAndKhonthaCasesPanel.add(khonthaChoices, BorderLayout.NORTH);

			final JRadioButton hopefullySolvedCaseButton = new JRadioButton(translations[15]);
			final JRadioButton hopelessSolvedCaseButton = new JRadioButton(translations[16]);
			hopefullySolvedCaseButton.setEnabled(false);
			hopelessSolvedCaseButton.setEnabled(false);

			khonthaChoices.add(hopefullySolvedCaseButton);
			khonthaChoices.add(hopelessSolvedCaseButton);

			final ActionListener khonthaGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == hopefullySolvedCaseButton)
					{
						khontha_w = 2;/* Shaafee and others */
					}
					if (ae.getSource() == hopelessSolvedCaseButton)
					{
						khontha_w = 1;/* maliki */
					}
				}
			};
			hopefullySolvedCaseButton.addActionListener(khonthaGroupListener);
			hopelessSolvedCaseButton.addActionListener(khonthaGroupListener);

			final ButtonGroup khonthaGroup = new ButtonGroup();
			khonthaGroup.add(hopefullySolvedCaseButton);
			khonthaGroup.add(hopelessSolvedCaseButton);

			// Version 1.2, Adding arham case choices
			final JPanel arhamChoices = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
			arhamChoices.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[17], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));
			mainPanel.add(arhamChoices, BorderLayout.CENTER);

			final JRadioButton oneWayArhamRadioButton = new JRadioButton(translations[18]);
			final JRadioButton manyWayArhamRadioButton = new JRadioButton(translations[19], true);
			oneWayArhamRadioButton.setEnabled(false);
			manyWayArhamRadioButton.setEnabled(false);

			arhamChoices.add(manyWayArhamRadioButton);
			arhamChoices.add(oneWayArhamRadioButton);

			final ActionListener arhamGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == oneWayArhamRadioButton)
					{
						oneWayArhamChoice = true;
					}
					if (ae.getSource() == manyWayArhamRadioButton)
					{
						oneWayArhamChoice = false;
					}
				}
			};
			oneWayArhamRadioButton.addActionListener(arhamGroupListener);
			manyWayArhamRadioButton.addActionListener(arhamGroupListener);

			final ButtonGroup arhamGroup = new ButtonGroup();
			arhamGroup.add(oneWayArhamRadioButton);
			arhamGroup.add(manyWayArhamRadioButton);

			// Version 1.9, For 'Wills diligence'. It can be son or daughter who is dead.
			final JPanel willsPanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
			willsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[32], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));
			if (MaknoonIslamicEncyclopedia.dubaiCourtsVer) mainPanel.add(willsPanel, BorderLayout.SOUTH);

			final JRadioButton sonRadioButton = new JRadioButton(translations[33], true);
			final JRadioButton daughterRadioButton = new JRadioButton(translations[34]);
			sonRadioButton.setEnabled(false);
			daughterRadioButton.setEnabled(false);

			willsPanel.add(sonRadioButton);
			willsPanel.add(daughterRadioButton);

			final ActionListener willsGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == sonRadioButton) willsMaleSex = true;
					if (ae.getSource() == daughterRadioButton) willsMaleSex = false;
				}
			};
			sonRadioButton.addActionListener(willsGroupListener);
			daughterRadioButton.addActionListener(willsGroupListener);

			final ButtonGroup willsGroup = new ButtonGroup();
			willsGroup.add(sonRadioButton);
			willsGroup.add(daughterRadioButton);

			enterchoice = false;
			final ActionListener specialFrameGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					enterchoice = true;
					if (ae.getSource() == specialFrameRadioButton[0])
					{
						if (madaheb == madhabName.SHAFEE || madaheb == madhabName.MALIKI || allMadaheb)
							arhamChoiceCheckBox.setEnabled(true);

						if (madaheb != madhabName.HANAFI)
						{
							oneWayArhamRadioButton.setEnabled(true);
							manyWayArhamRadioButton.setEnabled(true);
						}

						arham_p = 1;
					}
					else
					{
						arham_p = 0;
						oneWayArhamRadioButton.setEnabled(false);
						manyWayArhamRadioButton.setEnabled(false);
						arhamChoiceCheckBox.setEnabled(false);
					}

					hamlCaseSelected = (ae.getSource() == specialFrameRadioButton[1]);
					if (ae.getSource() == specialFrameRadioButton[2])
					{
						khonthaCaseSelected = true;

						/*
						 * Version 1.4
						 * This condition is added here instead of above to make sure that when there is no
						 * khontha case -> khontha_w == 0 to not enter the mowqof condition in the results class.
						 * This solved some problems in the allMadaheb case in which mowqof is calculated and errors
						 * happened e.g. wife, sister and father sister in hanbali madhab when selecting allMadaheb
						 * (try this with version below 1.4).
						 *
						 * In addition it causes alot of problems in monasakha and other cases.
						 */
						if (madaheb == madhabName.MALIKI)
						{
							hopelessSolvedCaseButton.setSelected(true);
							khontha_w = 1;
						}
						else
						{
							hopefullySolvedCaseButton.setSelected(true);
							khontha_w = 2;
						}

						hopefullySolvedCaseButton.setEnabled(true);
						hopelessSolvedCaseButton.setEnabled(true);
					}
					else
					{
						khonthaCaseSelected = false;
						hopefullySolvedCaseButton.setEnabled(false);
						hopelessSolvedCaseButton.setEnabled(false);

						// To not enter the mowqof condition in the results class.
						khontha_w = 0;
					}

					if (ae.getSource() == specialFrameRadioButton[3])
					{
						if (caseEnabledChoice == 1 /* Version 1.7 */ || caseEnabledChoice == 6 || caseEnabledChoice == 7)
						{
							verticalMonasakhaRadioButton.setEnabled(true);
							horizontalMonasakhaRadioButton.setEnabled(true);
							garaqLabel.setEnabled(true);
						}

						monasakha_c = 1;
					}
					else
					{
						monasakha_c = 0;
						verticalMonasakhaRadioButton.setEnabled(false);
						horizontalMonasakhaRadioButton.setEnabled(false);
						garaqLabel.setEnabled(false);
					}

					missingCaseSelected = (ae.getSource() == specialFrameRadioButton[4]);
					bondmanCaseSelected = (ae.getSource() == specialFrameRadioButton[5]);

					// Version 1.6
					if (ae.getSource() == specialFrameRadioButton[6])
					{
						willsCaseSelected = true;
						sonRadioButton.setEnabled(true);
						daughterRadioButton.setEnabled(true);
					}
					else
					{
						willsCaseSelected = false;
						sonRadioButton.setEnabled(false);
						daughterRadioButton.setEnabled(false);
					}
				}
			};

			final ButtonGroup specialFrameGroup = new ButtonGroup();
			for (int i = 0; i < 8; i++)
			{
				specialFrameRadioButton[i].addActionListener(specialFrameGroupListener);
				specialFrameGroup.add(specialFrameRadioButton[i]);
			}

			final JButton specialFrameButton = new JButton(translations[20]);
			add(specialFrameButton, BorderLayout.SOUTH);
			specialFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// To be the default if nothing selected especially for monasakha case that can call this many times
					if (!enterchoice)
					{
						bondmanCaseSelected = false;
						arham_p = 0;
						hamlCaseSelected = false;
						khonthaCaseSelected = false;

						/*
						 * Version 1.1
						 * Although monasakha case is Selected and for this reason we will have another variable
						 * to indicate this for further improvement if needed.
						 */
						monasakha_c = 0;
						missingCaseSelected = false;

						// Version 1.6
						willsCaseSelected = false;
					}

					if (allMadaheb && arham_p == 0)
					{
						/*
						 * Version 1.4
						 * The initialising is separated from calculating since some times we need to assign 0
						 * to some _num like f_sister_num in some cases.
						 */
						for (int i = 0; i < 5; i++)
						{
							// Initialise _num[?] for all
							// This remains the same to not confuse the special cases of one madhab with the others.
							h_num[i] = h_num[0];
							w_num[i] = w_num[0];
							f_num[i] = f_num[0];
							m_num[i] = m_num[0];
							m_m_num[i] = m_m_num[0];
							m_f_num[i] = m_f_num[0];
							m_m_m_num[i] = m_m_m_num[0];
							m_m_f_num[i] = m_m_f_num[0];
							m_f_f_num[i] = m_f_f_num[0];
							f_f_num[i] = f_f_num[0];
							s_num[i] = s_num[0];
							d_num[i] = d_num[0];
							s_s_num[i] = s_s_num[0];
							d_s_num[i] = d_s_num[0];
							s_s_s_num[i] = s_s_s_num[0];
							d_s_s_num[i] = d_s_s_num[0];
							b_num[i] = b_num[0];
							sister_num[i] = sister_num[0];
							m_b_num[i] = m_b_num[0];
							m_sister_num[i] = m_sister_num[0];
							f_b_num[i] = f_b_num[0];
							f_sister_num[i] = f_sister_num[0];
							s_b_num[i] = s_b_num[0];
							s_b_f_num[i] = s_b_f_num[0];
							u_num[i] = u_num[0];
							u_f_num[i] = u_f_num[0];
							s_u_num[i] = s_u_num[0];
							s_u_f_num[i] = s_u_f_num[0];
						}

						for (int i = 0; i < 5; i++)
						{
							// Version 1.3, We change the way to calculate all madhab to be separate for all of them
							if (i == 0) madaheb = madhabName.GUMHOUR;
							else
								if (i == 1) madaheb = madhabName.MALIKI;
								else
									if (i == 2) madaheb = madhabName.HANBALI;
									else
										if (i == 3) madaheb = madhabName.SHAFEE;
										else
											if (i == 4) madaheb = madhabName.HANAFI;

							inheritorsCalculation();
							if (i == (5 - 1))
								lastRotation = true;

							new MawarethSystemResults();
							cnt++;
						}
					}
					else
					{
						if (arham_p == 0 && !hamlCaseSelected && !khonthaCaseSelected && monasakha_c == 0 && !missingCaseSelected && !bondmanCaseSelected /* Version 1.6 */ && !willsCaseSelected)
						{
							inheritorsCalculation();
							new MawarethSystemResults();
						}
						else
						{
							if (arham_p != 0)
							{
								// Version 1.8
								if ((madaheb == madhabName.SHAFEE || madaheb == madhabName.MALIKI) && !arhamChoiceCheckBox.isSelected())
								{
									inheritorsCalculation();
									new MawarethSystemResults();
								}
								else
									new ArhamInterface();
							}

							if (hamlCaseSelected)
								new HamlCase();

							if (khonthaCaseSelected)
							{
								inheritorsCalculation();// To calculate the first time
								new KhonthaCase();
							}

							if (monasakha_c != 0)
							{
								/*
								 * Version 1.1
								 * For monasakha case be aware that monasakha_c is assign to 0 after choosing nothing case
								 * in the second time.
								 * For this reason we will have another variable to indicate this for further improvement if needed.
								 */
								monasakhaCaseSelected = true;
								inheritorsCalculation();

								// Version 1.2, This condition will be applied for the first one only since in the next time horizontalMonasakhaCounter will have a value
								if (!verticalMonasakha && horizontalMonasakhaCounter == 0)
								{
									if (!h[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!w[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!f[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!f_f[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m_m[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m_f[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m_m_m[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m_m_f[0].equals("0/1")) horizontalMonasakhaCounter++;
									if (!m_f_f[0].equals("0/1")) horizontalMonasakhaCounter++;

									// Version 2.2, To add the _num since it is accepting now more than one deceased from the same type
									if (!s[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_num[0];
									if (!d[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + d_num[0];
									if (!s_s[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_s_num[0];
									if (!d_s[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + d_s_num[0];
									if (!s_s_s[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_s_s_num[0];
									if (!d_s_s[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + d_s_s_num[0];
									if (!b[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + b_num[0];
									if (!sister[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + sister_num[0];
									if (!m_b[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + m_b_num[0];
									if (!m_sister[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + m_sister_num[0];
									if (!f_b[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + f_b_num[0];
									if (!f_sister[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + f_sister_num[0];
									if (!s_b[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_b_num[0];
									if (!s_b_f[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_b_f_num[0];
									if (!u[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + u_num[0];
									if (!u_f[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + u_f_num[0];
									if (!s_u[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_u_num[0];
									if (!s_u_f[0].equals("0/1"))
										horizontalMonasakhaCounter = horizontalMonasakhaCounter + s_u_f_num[0];
								}

								new MonasakhaCase();
							}

							if (missingCaseSelected)
							{
								inheritorsCalculation();
								new MissingCase();
							}

							if (bondmanCaseSelected)
								new BondmanCase();

							// Version 1.6, Implementing Wills case
							// Version 1.8, Modification to handle it correctly since it is consider as Waseya now
							if (willsCaseSelected)
							{
								JOptionPane.showOptionDialog(MawarethSystem.this, translations[24] + System.lineSeparator() + translations[25], translations[23], JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{translations[21]}, translations[21]);

								if (willsMaleSex) // Version 1.9
									// Increase the # s_num for the to caluclate the inheritance for the already deceased son
									s_num[cnt] = s_num[cnt] + 1;
								else
									d_num[cnt] = d_num[cnt] + 1;

								inheritorsCalculation();

								// We need to calculate the inheritance for the s_s, d_s(or d_d, s_d) of the deceased son
								cnt++;

								if (willsMaleSex) // Version 1.9
									new InheritorsInterface(true);
								else
									new InheritorsInterface(false);
							}
						}
					}

					dispose();
				}
			});

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			pack();
			center(this);
			MawarethSystem.this.add(this);

			// Version 1.7, If nothing is enabled, why need to display it?
			if (caseEnabledChoice == 5)
				specialFrameButton.doClick();
			else
			{
				setVisible(true);

				// Version 1.7, Just a hint for the user in case of monasakha
				if (caseEnabledChoice == 3)
					JOptionPane.showOptionDialog(MawarethSystem.this, translations[22], translations[23], JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{translations[21]}, translations[21]);
			}
		}
	}

	class ArhamInterface extends JInternalFrame
	{
		/*
		 * Version 1.1
		 * This panels are declared here to auto enable or disable them depending on your choice in
		 * online fashion. This case is happens at the beginning monasakha case.
		 */
		final JPanel[] arhamFramePanel = new JPanel[33];

		ArhamInterface()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "ArhamInterfaceArabic.txt" : "ArhamInterfaceEnglish.txt"));

			final JLabel[] arhamFrameLabel = new JLabel[33];
			final JRadioButton[] arhamFrameRadioButton = new JRadioButton[66];
			for (int i = 0, j = 0; i <= 32; i++)
			{
				arhamFrameLabel[i] = new JLabel(translations[i]);
				arhamFrameRadioButton[j] = new JRadioButton(translations[35], false);
				j++;
				arhamFrameRadioButton[j] = new JRadioButton(translations[36], true);
				j++;
			}

			final String[] patternExamples = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"};
			final JComboBox<String>[] arhamFrameComboBox = new JComboBox[28];
			for (int i = 0; i <= 27; i++)
			{
				arhamFrameComboBox[i] = new JComboBox<>(patternExamples);
				arhamFrameComboBox[i].setEnabled(false);
			}

			final ActionListener arhamFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					if (ae.getSource() == arhamFrameRadioButton[0]) a_f_m_num[cnt] = 1;
					if (ae.getSource() == arhamFrameRadioButton[1]) a_f_m_num[cnt] = 0;
					if (ae.getSource() == arhamFrameRadioButton[2]) a_f_m_f_num[cnt] = 1;
					if (ae.getSource() == arhamFrameRadioButton[3]) a_f_m_f_num[cnt] = 0;
					if (ae.getSource() == arhamFrameRadioButton[4]) a_f_m_m_num[cnt] = 1;
					if (ae.getSource() == arhamFrameRadioButton[5]) a_f_m_m_num[cnt] = 0;
					if (ae.getSource() == arhamFrameRadioButton[6]) a_f_f_m_num[cnt] = 1;
					if (ae.getSource() == arhamFrameRadioButton[7]) a_f_f_m_num[cnt] = 0;
					if (ae.getSource() == arhamFrameRadioButton[8]) a_m_f_m_num[cnt] = 1;
					if (ae.getSource() == arhamFrameRadioButton[9]) a_m_f_m_num[cnt] = 0;
					if (ae.getSource() == arhamFrameRadioButton[10])
					{
						arhamFrameComboBox[0].setEnabled(true);
						arhamFrameComboBox[0].setSelectedIndex(1);
						a_d_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[11])
					{
						arhamFrameComboBox[0].setEnabled(false);
						arhamFrameComboBox[0].setSelectedIndex(0);
						a_d_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[12])
					{
						arhamFrameComboBox[1].setEnabled(true);
						arhamFrameComboBox[1].setSelectedIndex(1);
						a_s_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[13])
					{
						arhamFrameComboBox[1].setEnabled(false);
						arhamFrameComboBox[1].setSelectedIndex(0);
						a_s_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[14])
					{
						arhamFrameComboBox[2].setEnabled(true);
						arhamFrameComboBox[2].setSelectedIndex(1);
						a_s_d_s_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[15])
					{
						arhamFrameComboBox[2].setEnabled(false);
						arhamFrameComboBox[2].setSelectedIndex(0);
						a_s_d_s_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[16])
					{
						arhamFrameComboBox[3].setEnabled(true);
						arhamFrameComboBox[3].setSelectedIndex(1);
						a_d_d_s_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[17])
					{
						arhamFrameComboBox[3].setEnabled(false);
						arhamFrameComboBox[3].setSelectedIndex(0);
						a_d_d_s_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[18])
					{
						arhamFrameComboBox[4].setEnabled(true);
						arhamFrameComboBox[4].setSelectedIndex(1);
						a_s_d_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[19])
					{
						arhamFrameComboBox[4].setEnabled(false);
						arhamFrameComboBox[4].setSelectedIndex(0);
						a_s_d_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[20])
					{
						arhamFrameComboBox[5].setEnabled(true);
						arhamFrameComboBox[5].setSelectedIndex(1);
						a_d_d_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[21])
					{
						arhamFrameComboBox[5].setEnabled(false);
						arhamFrameComboBox[5].setSelectedIndex(0);
						a_d_d_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[22])
					{
						arhamFrameComboBox[6].setEnabled(true);
						arhamFrameComboBox[6].setSelectedIndex(1);
						a_s_s_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[23])
					{
						arhamFrameComboBox[6].setEnabled(false);
						arhamFrameComboBox[6].setSelectedIndex(0);
						a_s_s_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[24])
					{
						arhamFrameComboBox[7].setEnabled(true);
						arhamFrameComboBox[7].setSelectedIndex(1);
						a_d_s_d_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[25])
					{
						arhamFrameComboBox[7].setEnabled(false);
						arhamFrameComboBox[7].setSelectedIndex(0);
						a_d_s_d_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[26])
					{
						arhamFrameComboBox[8].setEnabled(true);
						arhamFrameComboBox[8].setSelectedIndex(1);
						a_d_b_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[27])
					{
						arhamFrameComboBox[8].setEnabled(false);
						arhamFrameComboBox[8].setSelectedIndex(0);
						a_d_b_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[28])
					{
						arhamFrameComboBox[9].setEnabled(true);
						arhamFrameComboBox[9].setSelectedIndex(1);
						a_s_sister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[29])
					{
						arhamFrameComboBox[9].setEnabled(false);
						arhamFrameComboBox[9].setSelectedIndex(0);
						a_s_sister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[30])
					{
						arhamFrameComboBox[10].setEnabled(true);
						arhamFrameComboBox[10].setSelectedIndex(1);
						a_d_sister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[31])
					{
						arhamFrameComboBox[10].setEnabled(false);
						arhamFrameComboBox[10].setSelectedIndex(0);
						a_d_sister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[32])
					{
						arhamFrameComboBox[11].setEnabled(true);
						arhamFrameComboBox[11].setSelectedIndex(1);
						a_d_fb_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[33])
					{
						arhamFrameComboBox[11].setEnabled(false);
						arhamFrameComboBox[11].setSelectedIndex(0);
						a_d_fb_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[34])
					{
						arhamFrameComboBox[12].setEnabled(true);
						arhamFrameComboBox[12].setSelectedIndex(1);
						a_s_fsister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[35])
					{
						arhamFrameComboBox[12].setEnabled(false);
						arhamFrameComboBox[12].setSelectedIndex(0);
						a_s_fsister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[36])
					{
						arhamFrameComboBox[13].setEnabled(true);
						arhamFrameComboBox[13].setSelectedIndex(1);
						a_d_fsister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[37])
					{
						arhamFrameComboBox[13].setEnabled(false);
						arhamFrameComboBox[13].setSelectedIndex(0);
						a_d_fsister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[38])
					{
						arhamFrameComboBox[14].setEnabled(true);
						arhamFrameComboBox[14].setSelectedIndex(1);
						a_s_mb_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[39])
					{
						arhamFrameComboBox[14].setEnabled(false);
						arhamFrameComboBox[14].setSelectedIndex(0);
						a_s_mb_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[40])
					{
						arhamFrameComboBox[15].setEnabled(true);
						arhamFrameComboBox[15].setSelectedIndex(1);
						a_d_mb_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[41])
					{
						arhamFrameComboBox[15].setEnabled(false);
						arhamFrameComboBox[15].setSelectedIndex(0);
						a_d_mb_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[42])
					{
						arhamFrameComboBox[16].setEnabled(true);
						arhamFrameComboBox[16].setSelectedIndex(1);
						a_s_msister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[43])
					{
						arhamFrameComboBox[16].setEnabled(false);
						arhamFrameComboBox[16].setSelectedIndex(0);
						a_s_msister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[44])
					{
						arhamFrameComboBox[17].setEnabled(true);
						arhamFrameComboBox[17].setSelectedIndex(1);
						a_d_msister_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[45])
					{
						arhamFrameComboBox[17].setEnabled(false);
						arhamFrameComboBox[17].setSelectedIndex(0);
						a_d_msister_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[46])
					{
						arhamFrameComboBox[18].setEnabled(true);
						arhamFrameComboBox[18].setSelectedIndex(1);
						a_ul_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[47])
					{
						arhamFrameComboBox[18].setEnabled(false);
						arhamFrameComboBox[18].setSelectedIndex(0);
						a_ul_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[48])
					{
						arhamFrameComboBox[19].setEnabled(true);
						arhamFrameComboBox[19].setSelectedIndex(1);
						a_kl_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[49])
					{
						arhamFrameComboBox[19].setEnabled(false);
						arhamFrameComboBox[19].setSelectedIndex(0);
						a_kl_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[50])
					{
						arhamFrameComboBox[20].setEnabled(true);
						arhamFrameComboBox[20].setSelectedIndex(1);
						a_k_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[51])
					{
						arhamFrameComboBox[20].setEnabled(false);
						arhamFrameComboBox[20].setSelectedIndex(0);
						a_k_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[52])
					{
						arhamFrameComboBox[21].setEnabled(true);
						arhamFrameComboBox[21].setSelectedIndex(1);
						a_d_u_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[53])
					{
						arhamFrameComboBox[21].setEnabled(false);
						arhamFrameComboBox[21].setSelectedIndex(0);
						a_d_u_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[54])
					{
						arhamFrameComboBox[22].setEnabled(true);
						arhamFrameComboBox[22].setSelectedIndex(1);
						a_s_kl_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[55])
					{
						arhamFrameComboBox[22].setEnabled(false);
						arhamFrameComboBox[22].setSelectedIndex(0);
						a_s_kl_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[56])
					{
						arhamFrameComboBox[23].setEnabled(true);
						arhamFrameComboBox[23].setSelectedIndex(1);
						a_d_kl_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[57])
					{
						arhamFrameComboBox[23].setEnabled(false);
						arhamFrameComboBox[23].setSelectedIndex(0);
						a_d_kl_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[58])
					{
						arhamFrameComboBox[24].setEnabled(true);
						arhamFrameComboBox[24].setSelectedIndex(1);
						a_s_k_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[59])
					{
						arhamFrameComboBox[24].setEnabled(false);
						arhamFrameComboBox[24].setSelectedIndex(0);
						a_s_k_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[60])
					{
						arhamFrameComboBox[25].setEnabled(true);
						arhamFrameComboBox[25].setSelectedIndex(1);
						a_d_k_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[61])
					{
						arhamFrameComboBox[25].setEnabled(false);
						arhamFrameComboBox[25].setSelectedIndex(0);
						a_d_k_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[62])
					{
						arhamFrameComboBox[26].setEnabled(true);
						arhamFrameComboBox[26].setSelectedIndex(1);
						a_s_ul_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[63])
					{
						arhamFrameComboBox[26].setEnabled(false);
						arhamFrameComboBox[26].setSelectedIndex(0);
						a_s_ul_num[cnt] = 0;
					}
					if (ae.getSource() == arhamFrameRadioButton[64])
					{
						arhamFrameComboBox[27].setEnabled(true);
						arhamFrameComboBox[27].setSelectedIndex(1);
						a_d_ul_num[cnt] = 1;
					}
					if (ae.getSource() == arhamFrameRadioButton[65])
					{
						arhamFrameComboBox[27].setEnabled(false);
						arhamFrameComboBox[27].setSelectedIndex(0);
						a_d_ul_num[cnt] = 0;
					}

					/*
					 * Version 1.1
					 * This function is used to auto enable or disable inheritors panels depending on your
					 * choice in online fashion. This function is suitable for hanafi madhab since others depends
					 * on the original inheritors.
					 */
					if (madaheb == madhabName.HANAFI)
						autoEnableInheritors();
				}
			};

			ButtonGroup arhamFrameRadioButtonGroup;
			for (int i = 0; i <= 65; )
			{
				arhamFrameRadioButtonGroup = new ButtonGroup();
				for (int j = 0; j <= 1; j++)
				{
					arhamFrameRadioButton[i].addActionListener(arhamFrameRadioButtonGroupListener);
					arhamFrameRadioButtonGroup.add(arhamFrameRadioButton[i]);
					i++;
				}
			}

			final ActionListener arhamFrameComboBoxGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (e.getSource() == arhamFrameComboBox[0])
					{
						a_d_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_d_num[cnt] == 0)
							arhamFrameRadioButton[11].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[1])
					{
						a_s_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_d_num[cnt] == 0)
							arhamFrameRadioButton[13].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[2])
					{
						a_s_d_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_d_s_num[cnt] == 0)
							arhamFrameRadioButton[15].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[3])
					{
						a_d_d_s_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_d_s_num[cnt] == 0)
							arhamFrameRadioButton[17].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[4])
					{
						a_s_d_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_d_d_num[cnt] == 0)
							arhamFrameRadioButton[19].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[5])
					{
						a_d_d_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_d_d_num[cnt] == 0)
							arhamFrameRadioButton[21].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[6])
					{
						a_s_s_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_s_d_num[cnt] == 0)
							arhamFrameRadioButton[23].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[7])
					{
						a_d_s_d_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_s_d_num[cnt] == 0)
							arhamFrameRadioButton[25].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[8])
					{
						a_d_b_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_b_num[cnt] == 0)
							arhamFrameRadioButton[27].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[9])
					{
						a_s_sister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_sister_num[cnt] == 0)
							arhamFrameRadioButton[29].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[10])
					{
						a_d_sister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_sister_num[cnt] == 0)
							arhamFrameRadioButton[31].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[11])
					{
						a_d_fb_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_fb_num[cnt] == 0)
							arhamFrameRadioButton[33].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[12])
					{
						a_s_fsister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_fsister_num[cnt] == 0)
							arhamFrameRadioButton[35].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[13])
					{
						a_d_fsister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_fsister_num[cnt] == 0)
							arhamFrameRadioButton[37].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[14])
					{
						a_s_mb_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_mb_num[cnt] == 0)
							arhamFrameRadioButton[39].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[15])
					{
						a_d_mb_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_mb_num[cnt] == 0)
							arhamFrameRadioButton[41].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[16])
					{
						a_s_msister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_msister_num[cnt] == 0)
							arhamFrameRadioButton[43].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[17])
					{
						a_d_msister_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_msister_num[cnt] == 0)
							arhamFrameRadioButton[45].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[18])
					{
						a_ul_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_ul_num[cnt] == 0)
							arhamFrameRadioButton[47].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[19])
					{
						a_kl_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_kl_num[cnt] == 0)
							arhamFrameRadioButton[49].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[20])
					{
						a_k_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_k_num[cnt] == 0)
							arhamFrameRadioButton[51].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[21])
					{
						a_d_u_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_u_num[cnt] == 0)
							arhamFrameRadioButton[53].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[22])
					{
						a_s_kl_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_kl_num[cnt] == 0)
							arhamFrameRadioButton[55].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[23])
					{
						a_d_kl_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_kl_num[cnt] == 0)
							arhamFrameRadioButton[57].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[24])
					{
						a_s_k_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_k_num[cnt] == 0)
							arhamFrameRadioButton[59].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[25])
					{
						a_d_k_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_k_num[cnt] == 0)
							arhamFrameRadioButton[61].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[26])
					{
						a_s_ul_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_s_ul_num[cnt] == 0)
							arhamFrameRadioButton[63].doClick();
					}
					if (e.getSource() == arhamFrameComboBox[27])
					{
						a_d_ul_num[cnt] = Integer.parseInt((String) ((JComboBox<?>) e.getSource()).getSelectedItem());
						if (a_d_ul_num[cnt] == 0)
							arhamFrameRadioButton[65].doClick();
					}

					/*
					 * Version 1.1
					 * This function is used to auto enable or disable inheritors panels depending on your
					 * choice in online fashion. This function is suitable for hanafi madhab since others depends
					 * on the original inheritors.
					 */
					if (madaheb == madhabName.HANAFI)
						autoEnableInheritors();
				}
			};

			for (int i = 0; i <= 27; i++)
				arhamFrameComboBox[i].addActionListener(arhamFrameComboBoxGroupListener);

			final JPanel arhamFrameMainPanel = new JPanel(new GridLayout(17, 2));
			JPanel arhamFrameButtonPanel;
			for (int i = 0, q = 0, j = 0; i <= 32; i++)
			{
				arhamFrameButtonPanel = new JPanel();
				arhamFramePanel[i] = new JPanel(new BorderLayout());
				arhamFramePanel[i].setBorder(BorderFactory.createEtchedBorder()); // Version 2.0

				switch (i)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						arhamFramePanel[i].add(arhamFrameLabel[i], BorderLayout.CENTER);
						arhamFrameButtonPanel.add(arhamFrameRadioButton[q++]);
						arhamFrameButtonPanel.add(arhamFrameRadioButton[q++]);
						break;
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					case 22:
					case 23:
					case 24:
					case 25:
					case 26:
					case 27:
					case 28:
					case 29:
					case 30:
					case 31:
					case 32:
						arhamFramePanel[i].add(arhamFrameLabel[i], BorderLayout.CENTER);
						arhamFrameButtonPanel.add(arhamFrameRadioButton[q++]);
						arhamFrameButtonPanel.add(arhamFrameRadioButton[q++]);
						arhamFrameButtonPanel.add(arhamFrameComboBox[j++]);
						break;
				}

				if (MaknoonIslamicEncyclopedia.language)
					arhamFramePanel[i].add(arhamFrameButtonPanel, BorderLayout.WEST);
				else
					arhamFramePanel[i].add(arhamFrameButtonPanel, BorderLayout.EAST);

				arhamFrameMainPanel.add(arhamFramePanel[i]);
			}

			setTitle(translations[33]);
			setMaximizable(true);
			setResizable(true);
			setFrameIcon(null);
			setLayout(new BorderLayout());
			add(new JScrollPane(arhamFrameMainPanel), BorderLayout.CENTER);

			final JButton arhamFrameButton = new JButton(translations[34]);
			add(arhamFrameButton, BorderLayout.SOUTH);
			arhamFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// i.e. arham case for all madaheb
					if (allMadaheb)
					{
						for (int i = 0; i < 5; i++)
						{
							// Version 1.7, You missed h & w !
							h_num[i] = h_num[0];
							w_num[i] = w_num[0];

							a_s_d_num[i] = a_s_d_num[0];
							a_d_d_num[i] = a_d_d_num[0];
							a_s_d_s_num[i] = a_s_d_s_num[0];
							a_d_d_s_num[i] = a_d_d_s_num[0];
							a_s_d_d_num[i] = a_s_d_d_num[0];
							a_d_d_d_num[i] = a_d_d_d_num[0];
							a_s_s_d_num[i] = a_s_s_d_num[0];
							a_d_s_d_num[i] = a_d_s_d_num[0];
							a_f_m_num[i] = a_f_m_num[0];
							a_f_f_m_num[i] = a_f_f_m_num[0];
							a_f_m_f_num[i] = a_f_m_f_num[0];
							a_f_m_m_num[i] = a_f_m_m_num[0];
							a_m_f_m_num[i] = a_m_f_m_num[0];
							a_s_sister_num[i] = a_s_sister_num[0];
							a_d_sister_num[i] = a_d_sister_num[0];
							a_d_b_num[i] = a_d_b_num[0];
							a_d_msister_num[i] = a_d_msister_num[0];
							a_s_msister_num[i] = a_s_msister_num[0];
							a_d_mb_num[i] = a_d_mb_num[0];
							a_s_mb_num[i] = a_s_mb_num[0];
							a_s_fsister_num[i] = a_s_fsister_num[0];
							a_d_fsister_num[i] = a_d_fsister_num[0];
							a_d_fb_num[i] = a_d_fb_num[0];
							a_ul_num[i] = a_ul_num[0];
							a_k_num[i] = a_k_num[0];
							a_kl_num[i] = a_kl_num[0];
							a_s_ul_num[i] = a_s_ul_num[0];
							a_d_ul_num[i] = a_d_ul_num[0];
							a_d_u_num[i] = a_d_u_num[0];
							a_s_k_num[i] = a_s_k_num[0];
							a_d_k_num[i] = a_d_k_num[0];
							a_s_kl_num[i] = a_s_kl_num[0];
							a_d_kl_num[i] = a_d_kl_num[0];

							// Version 1.3, We change the way to calculate all madhab to be separate for all of them
							if (i == 0) madaheb = madhabName.GUMHOUR;
							else
								if (i == 1) madaheb = madhabName.MALIKI;
								else
									if (i == 2) madaheb = madhabName.HANBALI;
									else
										if (i == 3) madaheb = madhabName.SHAFEE;
										else
											if (i == 4) madaheb = madhabName.HANAFI;

							// Version 1.7, Since SHAFEE & MALIKI do not have arham case.
							if ((i == 1 || i == 3) /* Version 1.8 */ && !arhamChoiceCheckBox.isSelected())
								inheritorsCalculation();
							else
								arhamCalculation();

							if (i == (5 - 1))
								lastRotation = true;

							new MawarethSystemResults();
							cnt++;
						}
					}
					else
					{
						arhamCalculation();
						new MawarethSystemResults();
					}
					dispose();
				}
			});

			pack();
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
			try
			{
				setMaximum(true);
			}
			catch (java.beans.PropertyVetoException e)
			{
				e.printStackTrace();
			}
		}

		/*
		 * Version 1.1
		 * This is used to auto enable or disable Panels depending on your choice in online fashion.
		 * This function need to be corrected.
		 */
		public void autoEnableInheritors()
		{
			for (int i = 0; i <= 32; i++)
			{
				// Enable all except daughter's daughters and daughter's Sons since no one can block them
				if (i != 5 && i != 6)
					enabledContainer(arhamFramePanel[i], true);
			}

			if (!(a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0))
			{
				// Disable a_s_d_s[cnt] && a_d_d_s[cnt] && a_s_d_d[cnt] && a_d_d_d[cnt] && a_s_s_d[cnt] && a_d_s_d[cnt].
				for (int i = 7; i <= 12; i++)
					enabledContainer(arhamFramePanel[i], false);
			}
			else
			{
				if (!(a_s_d_s_num[cnt] == 0 && a_d_d_s_num[cnt] == 0))
				{
					// Disable a_s_d_d[cnt] && a_d_d_d[cnt] && a_s_s_d[cnt] && a_d_s_d[cnt]
					for (int i = 9; i <= 12; i++)
						enabledContainer(arhamFramePanel[i], false);
				}
			}

			if (!(a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0 && a_s_d_s_num[cnt] == 0 && a_d_d_s_num[cnt] == 0 &&
					a_s_d_d_num[cnt] == 0 && a_d_d_d_num[cnt] == 0 && a_s_s_d_num[cnt] == 0 && a_d_s_d_num[cnt] == 0))
			{
				/*
				 * Disable a_f_m[cnt] && a_f_m_f[cnt] && a_f_m_m[cnt] && a_f_f_m[cnt] && a_m_f_m[cnt]
				 * && a_d_b[cnt] && a_s_sister[cnt] && a_d_sister[cnt] && a_d_fb[cnt] && a_s_fsister[cnt]
				 * && a_d_fsister[cnt] && a_s_mb[cnt] && a_d_mb[cnt] && a_s_msister[cnt] && a_d_msister[cnt]
				 * && a_ul[cnt] && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt] && a_s_k[cnt]
				 * && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
				 */
				for (int i = 0; i <= 32; i++)
				{
					if (i <= 4 || i >= 13)
						enabledContainer(arhamFramePanel[i], false);
				}
			}
			else
			{
				if (a_f_m_num[cnt] != 0)
				{
					/*
					 * Disable a_f_m_f[cnt] && a_f_m_m[cnt] && a_f_f_m[cnt] && a_m_f_m[cnt] && a_d_b[cnt]
					 * && a_s_sister[cnt] && a_d_sister[cnt] && a_d_fb[cnt] && a_s_fsister[cnt]
					 * && a_d_fsister[cnt] && a_s_mb[cnt] && a_d_mb[cnt] && a_s_msister[cnt] && a_d_msister[cnt]
					 * && a_ul[cnt] && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt]
					 * && a_s_k[cnt] && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
					 */
					for (int i = 1; i <= 32; i++)
					{
						if (i <= 4 || i >= 13)
							enabledContainer(arhamFramePanel[i], false);
					}
				}
				else
				{
					if (!(a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0))
					{
						/*
						 * Disable a_f_f_m[cnt] && a_m_f_m[cnt] && a_d_b[cnt] && a_s_sister[cnt]
						 * && a_d_sister[cnt] && a_d_fb[cnt] && a_s_fsister[cnt] && a_d_fsister[cnt]
						 * && a_s_mb[cnt] && a_d_mb[cnt] && a_s_msister[cnt] && a_d_msister[cnt]
						 * && a_ul[cnt] && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt]
						 * && a_s_k[cnt] && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
						 */
						for (int i = 3; i <= 32; i++)
						{
							if (i <= 4 || i >= 13)
								enabledContainer(arhamFramePanel[i], false);
						}
					}
					else
					{
						if (!(a_f_f_m_num[cnt] == 0 && a_m_f_m_num[cnt] == 0))
						{
							/*
							 * Disable a_d_b[cnt] && a_s_sister[cnt] && a_d_sister[cnt] && a_d_fb[cnt]
							 * && a_s_fsister[cnt] && a_d_fsister[cnt] && a_s_mb[cnt] && a_d_mb[cnt]
							 * && a_s_msister[cnt] && a_d_msister[cnt] && a_ul[cnt] && a_kl[cnt] && a_k[cnt]
							 * && a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt] && a_s_k[cnt] && a_d_k[cnt]
							 * && a_s_ul[cnt] && a_d_ul[cnt].
							 */
							for (int i = 13; i <= 32; i++)
								enabledContainer(arhamFramePanel[i], false);
						}
						else
						{
							if (!(a_d_sister_num[cnt] == 0 && a_s_sister_num[cnt] == 0 && a_d_b_num[cnt] == 0))
							{
								/*
								 * Disable a_d_fb[cnt] && a_s_fsister[cnt] && a_d_fsister[cnt] && a_s_mb[cnt]
								 * && a_d_mb[cnt] && a_s_msister[cnt] && a_d_msister[cnt] && a_ul[cnt]
								 * && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt]
								 * && a_s_k[cnt] && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
								 */
								for (int i = 16; i <= 32; i++)
									enabledContainer(arhamFramePanel[i], false);
							}
							else
							{
								if (!(a_d_fb_num[cnt] == 0 && a_s_fsister_num[cnt] == 0 && a_d_fsister_num[cnt] == 0))
								{
									/*
									 * Disable a_s_mb[cnt] && a_d_mb[cnt] && a_s_msister[cnt] && a_d_msister[cnt]
									 * && a_ul[cnt] && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt]
									 * && a_d_kl[cnt] && a_s_k[cnt] && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
									 */
									for (int i = 19; i <= 32; i++)
										enabledContainer(arhamFramePanel[i], false);
								}
								else
								{
									if (!(a_s_mb_num[cnt] == 0 && a_d_mb_num[cnt] == 0 && a_s_msister_num[cnt] == 0 && a_d_msister_num[cnt] == 0))
									{
										/*
										 * Disable a_ul[cnt] && a_kl[cnt] && a_k[cnt] && a_d_u[cnt] && a_s_kl[cnt]
										 * && a_d_kl[cnt] && a_s_k[cnt] && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
										 */
										for (int i = 23; i <= 32; i++)
											enabledContainer(arhamFramePanel[i], false);
									}
									else
									{
										if (!(a_ul_num[cnt] == 0 && a_kl_num[cnt] == 0 && a_k_num[cnt] == 0))
										{
											/*
											 * Disable a_d_u[cnt] && a_s_kl[cnt] && a_d_kl[cnt] && a_s_k[cnt]
											 * && a_d_k[cnt] && a_s_ul[cnt] && a_d_ul[cnt].
											 */
											for (int i = 26; i <= 32; i++)
												enabledContainer(arhamFramePanel[i], false);
										}
										else
										{
											if (!(a_d_u_num[cnt] == 0 /* Version 1.5 */ && a_s_kl_num[cnt] == 0 && a_d_kl_num[cnt] == 0 && a_s_k_num[cnt] == 0 && a_d_k_num[cnt] == 0))
											{
												// Disable a_s_ul[cnt] && a_d_ul[cnt]
												enabledContainer(arhamFramePanel[31], false);
												enabledContainer(arhamFramePanel[32], false);
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

	class KhonthaCase extends JInternalFrame
	{
		KhonthaCase()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "KhonthaCaseArabic.txt" : "KhonthaCaseEnglish.txt"));

			khonthaCaseSelected = true;

			final JRadioButton[] khonthaFrameRadioButton = new JRadioButton[18];
			for (int i = 0; i <= 17; i++)
				khonthaFrameRadioButton[i] = new JRadioButton(translations[i], false);

			final int[] khontha_index = new int[18];
			final ActionListener khonthaFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					for (int i = 0; i <= 17; i++)
					{
						if (ae.getSource() == khonthaFrameRadioButton[i])
						{
							khontha_index[i] = 1;
							for (int j = 0; j <= 17; j++)
								if (i != j)
									khontha_index[j] = 0;
						}
					}
				}
			};

			final ButtonGroup khonthaFrameRadioButtonGroup = new ButtonGroup();
			for (int i = 0; i <= 17; i++)
			{
				khonthaFrameRadioButton[i].addActionListener(khonthaFrameRadioButtonGroupListener);
				khonthaFrameRadioButtonGroup.add(khonthaFrameRadioButton[i]);
				khonthaFrameRadioButton[i].setEnabled(false);
			}

			if (s_num[cnt] != 0) khonthaFrameRadioButton[0].setEnabled(true);
			if (d_num[cnt] != 0) khonthaFrameRadioButton[1].setEnabled(true);
			if (s_s_num[cnt] != 0) khonthaFrameRadioButton[2].setEnabled(true);
			if (d_s_num[cnt] != 0) khonthaFrameRadioButton[3].setEnabled(true);
			if (s_s_s_num[cnt] != 0) khonthaFrameRadioButton[4].setEnabled(true);
			if (d_s_s_num[cnt] != 0) khonthaFrameRadioButton[5].setEnabled(true);
			if (b_num[cnt] != 0) khonthaFrameRadioButton[6].setEnabled(true);
			if (sister_num[cnt] != 0) khonthaFrameRadioButton[7].setEnabled(true);
			if (m_b_num[cnt] != 0) khonthaFrameRadioButton[8].setEnabled(true);
			if (m_sister_num[cnt] != 0) khonthaFrameRadioButton[9].setEnabled(true);
			if (f_b_num[cnt] != 0) khonthaFrameRadioButton[10].setEnabled(true);
			if (f_sister_num[cnt] != 0) khonthaFrameRadioButton[11].setEnabled(true);
			if (s_b_num[cnt] != 0) khonthaFrameRadioButton[12].setEnabled(true);
			if (s_b_f_num[cnt] != 0) khonthaFrameRadioButton[13].setEnabled(true);
			if (u_num[cnt] != 0) khonthaFrameRadioButton[14].setEnabled(true);
			if (u_f_num[cnt] != 0) khonthaFrameRadioButton[15].setEnabled(true);
			if (s_u_num[cnt] != 0) khonthaFrameRadioButton[16].setEnabled(true);
			if (s_u_f_num[cnt] != 0) khonthaFrameRadioButton[17].setEnabled(true);

			final JPanel khonthaFrameMainPanel = new JPanel(new GridLayout(18, 1));
			khonthaFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[18], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			setLayout(new BorderLayout());
			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			setResizable(true);
			setFrameIcon(null);
			setTitle(translations[24]);
			setSize(320, 520);

			for (int i = 0; i <= 17; i++)
				khonthaFrameMainPanel.add(khonthaFrameRadioButton[i]);

			add(new JScrollPane(khonthaFrameMainPanel), BorderLayout.CENTER);

			final JButton khonthaFrameCancelButton = new JButton(translations[19]);
			khonthaFrameCancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					khonthaCaseSelected = false;
					new MawarethSystemResults();
					dispose();
				}
			});

			final JPanel khonthaCaseChoicePanel = new JPanel(new GridLayout(1, 2));
			add(khonthaCaseChoicePanel, BorderLayout.SOUTH);

			final JButton khonthaFrameButton = new JButton(translations[20]);
			khonthaCaseChoicePanel.add(khonthaFrameButton);
			khonthaCaseChoicePanel.add(khonthaFrameCancelButton);
			khonthaFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 1.1, This condition is used to prevent the user from not selecting anything
					boolean continuekhonthaCase = false;
					for (int i = 0; i <= 17; i++)
						if (khontha_index[i] != 0)
						{
							continuekhonthaCase = true;
							break;
						}

					if (!continuekhonthaCase)
						JOptionPane.showOptionDialog(MawarethSystem.this, translations[21], translations[22], JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{translations[23]}, translations[23]);
					else
					{
						if (khontha_index[0] != 0)
						{
							s_num[cnt + 1] = s_num[cnt] - 1;//May be there are more than one son!
							s_num[cnt] = s_num[cnt] - 1;//because we will put his part to khontha
							khontha[cnt] = s[cnt];//this is for the khontha case to be compared after change

							// Omit the value of s[cnt] if it is one i.e. s_num=1 and here is =0 after the previous statment
							if (s_num[cnt] == 0) s[cnt] = "0/1";
							d_num[cnt + 1] = d_num[cnt] + 1;
							khontha_p = 1;
						}

						if (khontha_index[1] != 0)
						{
							d_num[cnt + 1] = d_num[cnt] - 1;
							d_num[cnt] = d_num[cnt] - 1;
							khontha[cnt] = d[cnt];

							if (d_num[cnt] == 0) d[cnt] = "0/1";
							s_num[cnt + 1] = s_num[cnt] + 1;
							khontha_p = 2;
						}

						if (khontha_index[2] != 0)
						{
							s_s_num[cnt + 1] = s_s_num[cnt] - 1;
							s_s_num[cnt] = s_s_num[cnt] - 1;
							khontha[cnt] = s_s[cnt];

							if (s_s_num[cnt] == 0) s_s[cnt] = "0/1";
							d_s_num[cnt + 1] = d_s_num[cnt] + 1;
							khontha_p = 3;
						}

						if (khontha_index[3] != 0)
						{
							d_s_num[cnt + 1] = d_s_num[cnt] - 1;
							d_s_num[cnt] = d_s_num[cnt] - 1;
							khontha[cnt] = d_s[cnt];

							if (d_s_num[cnt] == 0) d_s[cnt] = "0/1";
							s_s_num[cnt + 1] = s_s_num[cnt] + 1;
							khontha_p = 4;
						}

						if (khontha_index[4] != 0)
						{
							s_s_s_num[cnt + 1] = s_s_s_num[cnt] - 1;
							s_s_s_num[cnt] = s_s_s_num[cnt] - 1;
							khontha[cnt] = s_s_s[cnt];

							if (s_s_s_num[cnt] == 0) s_s_s[cnt] = "0/1";
							d_s_s_num[cnt + 1] = d_s_s_num[cnt] + 1;
							khontha_p = 5;
						}

						if (khontha_index[5] != 0)
						{
							d_s_s_num[cnt + 1] = d_s_s_num[cnt] - 1;
							d_s_s_num[cnt] = d_s_s_num[cnt] - 1;
							khontha[cnt] = d_s_s[cnt];

							if (d_s_s_num[cnt] == 0) d_s_s[cnt] = "0/1";
							s_s_s_num[cnt + 1] = s_s_s_num[cnt] + 1;
							khontha_p = 6;
						}

						if (khontha_index[6] != 0)
						{
							b_num[cnt + 1] = b_num[cnt] - 1;
							b_num[cnt] = b_num[cnt] - 1;
							khontha[cnt] = b[cnt];

							if (b_num[cnt] == 0) b[cnt] = "0/1";
							sister_num[cnt + 1] = sister_num[cnt] + 1;
							khontha_p = 7;
						}

						if (khontha_index[7] != 0)
						{
							sister_num[cnt + 1] = sister_num[cnt] - 1;
							sister_num[cnt] = sister_num[cnt] - 1;
							khontha[cnt] = sister[cnt];

							if (sister_num[cnt] == 0) sister[cnt] = "0/1";
							b_num[cnt + 1] = b_num[cnt] + 1;
							khontha_p = 8;
						}

						if (khontha_index[8] != 0)
						{
							m_b_num[cnt + 1] = m_b_num[cnt] - 1;
							m_b_num[cnt] = m_b_num[cnt] - 1;
							khontha[cnt] = m_b[cnt];

							if (m_b_num[cnt] == 0) m_b[cnt] = "0/1";
							m_sister_num[cnt + 1] = m_sister_num[cnt] + 1;
							khontha_p = 9;
						}

						if (khontha_index[9] != 0)
						{
							m_sister_num[cnt + 1] = m_sister_num[cnt] - 1;
							m_sister_num[cnt] = m_sister_num[cnt] - 1;
							khontha[cnt] = m_sister[cnt];

							if (m_sister_num[cnt] == 0) m_sister[cnt] = "0/1";
							m_b_num[cnt + 1] = m_b_num[cnt] + 1;
							khontha_p = 10;
						}

						if (khontha_index[10] != 0)
						{
							f_b_num[cnt + 1] = f_b_num[cnt] - 1;
							f_b_num[cnt] = f_b_num[cnt] - 1;
							khontha[cnt] = f_b[cnt];

							if (f_b_num[cnt] == 0) f_b[cnt] = "0/1";
							f_sister_num[cnt + 1] = f_sister_num[cnt] + 1;
							khontha_p = 11;
						}

						if (khontha_index[11] != 0)
						{
							f_sister_num[cnt + 1] = f_sister_num[cnt] - 1;
							f_sister_num[cnt] = f_sister_num[cnt] - 1;
							khontha[cnt] = f_sister[cnt];

							if (f_sister_num[cnt] == 0) f_sister[cnt] = "0/1";
							f_b_num[cnt + 1] = f_b_num[cnt] + 1;
							khontha_p = 12;
						}

						if (khontha_index[12] != 0)
						{
							s_b_num[cnt + 1] = s_b_num[cnt] - 1;//There is no sister_brother (i.e. Arham)
							s_b_num[cnt] = s_b_num[cnt] - 1;
							khontha[cnt] = s_b[cnt];

							if (s_b_num[cnt] == 0) s_b[cnt] = "0/1";
							khontha_p = 13;
						}

						if (khontha_index[13] != 0)
						{
							s_b_f_num[cnt + 1] = s_b_f_num[cnt] - 1;
							s_b_f_num[cnt] = s_b_f_num[cnt] - 1;
							khontha[cnt] = s_b_f[cnt];

							if (s_b_f_num[cnt] == 0) s_b_f[cnt] = "0/1";
							khontha_p = 14;
						}

						if (khontha_index[14] != 0)
						{
							u_num[cnt + 1] = u_num[cnt] - 1;
							u_num[cnt] = u_num[cnt] - 1;
							khontha[cnt] = u[cnt];

							if (u_num[cnt] == 0) u[cnt] = "0/1";
							khontha_p = 15;
						}

						if (khontha_index[15] != 0)
						{
							u_f_num[cnt + 1] = u_f_num[cnt] - 1;
							u_f_num[cnt] = u_f_num[cnt] - 1;
							khontha[cnt] = u_f[cnt];

							if (u_f_num[cnt] == 0) u_f[cnt] = "0/1";
							khontha_p = 16;
						}

						if (khontha_index[16] != 0)
						{
							s_u_num[cnt + 1] = s_u_num[cnt] - 1;
							s_u_num[cnt] = s_u_num[cnt] - 1;
							khontha[cnt] = s_u[cnt];

							if (s_u_num[cnt] == 0) s_u[cnt] = "0/1";
							khontha_p = 17;
						}

						if (khontha_index[17] != 0)
						{
							s_u_f_num[cnt + 1] = s_u_f_num[cnt] - 1;
							s_u_f_num[cnt] = s_u_f_num[cnt] - 1;
							khontha[cnt] = s_u_f[cnt];

							if (s_u_f_num[cnt] == 0) s_u_f[cnt] = "0/1";
							khontha_p = 18;
						}

						//Reinitilise all _num to the second stage
						h_num[1] = h_num[0];
						w_num[1] = w_num[0];
						f_num[1] = f_num[0];
						m_num[1] = m_num[0];
						m_m_num[1] = m_m_num[0];
						m_f_num[1] = m_f_num[0];
						m_m_m_num[1] = m_m_m_num[0];
						m_m_f_num[1] = m_m_f_num[0];
						m_f_f_num[1] = m_f_f_num[0];
						f_f_num[1] = f_f_num[0];

						//To not initilise the previous parameters
						if ((s_num[cnt] != 0 || d_num[cnt] != 0) && khontha_p != 1 && khontha_p != 2)
						{
							s_num[1] = s_num[0];
							d_num[1] = d_num[0];
						}

						if ((s_s_num[cnt] != 0 || d_s_num[cnt] != 0) && khontha_p != 3 && khontha_p != 4)
						{
							s_s_num[1] = s_s_num[0];
							d_s_num[1] = d_s_num[0];
						}

						if ((s_s_s_num[cnt] != 0 || d_s_s_num[cnt] != 0) && khontha_p != 5 && khontha_p != 6)
						{
							s_s_s_num[1] = s_s_s_num[0];
							d_s_s_num[1] = d_s_s_num[0];
						}

						if ((b_num[cnt] != 0 || sister_num[cnt] != 0) && khontha_p != 7 && khontha_p != 8)
						{
							b_num[1] = b_num[0];
							sister_num[1] = sister_num[0];
						}

						if ((m_b_num[cnt] != 0 || m_sister_num[cnt] != 0) && khontha_p != 9 && khontha_p != 10)
						{
							m_b_num[1] = m_b_num[0];
							m_sister_num[1] = m_sister_num[0];
						}

						if ((f_b_num[cnt] != 0 || f_sister_num[cnt] != 0) && khontha_p != 11 && khontha_p != 12)
						{
							f_b_num[1] = f_b_num[0];
							f_sister_num[1] = f_sister_num[0];
						}

						if (s_b_num[cnt] != 0 && khontha_p != 13) s_b_num[1] = s_b_num[0];
						if (s_b_f_num[cnt] != 0 && khontha_p != 14) s_b_f_num[1] = s_b_f_num[0];
						if (u_num[cnt] != 0 && khontha_p != 15) u_num[1] = u_num[0];
						if (u_f_num[cnt] != 0 && khontha_p != 16) u_f_num[1] = u_f_num[0];
						if (s_u_num[cnt] != 0 && khontha_p != 17) s_u_num[1] = s_u_num[0];
						if (s_u_f_num[cnt] != 0 && khontha_p != 18) s_u_f_num[1] = s_u_f_num[0];

						//increase the counter
						cnt++;

						//Re-calculate
						inheritorsCalculation();
						new MawarethSystemResults();
						dispose();
					}
				}
			});

			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
			setVisible(true);
		}
	}

	class HamlCase extends JInternalFrame
	{
		HamlCase()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "HamlCaseArabic.txt" : "HamlCaseEnglish.txt"));

			hamlCaseSelected = true;

			final JRadioButton[] hamlFrameRadioButton = new JRadioButton[12];
			for (int i = 0; i <= 11; i++)
				hamlFrameRadioButton[i] = new JRadioButton(translations[i], false);

			final int[] haml_index = new int[12];
			final ActionListener hamlFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					for (int i = 0; i <= 11; i++)
					{
						if (ae.getSource() == hamlFrameRadioButton[i])
						{
							haml_index[i] = 1;
							for (int j = 0; j <= 11; j++)
								if (i != j)
									haml_index[j] = 0;
						}
					}
				}
			};

			final ButtonGroup hamlFrameRadioButtonGroup = new ButtonGroup();
			for (int i = 0; i <= 11; i++)
			{
				hamlFrameRadioButton[i].addActionListener(hamlFrameRadioButtonGroupListener);
				hamlFrameRadioButtonGroup.add(hamlFrameRadioButton[i]);
			}

			final JPanel hamlFrameMainPanel = new JPanel(new GridLayout(12, 1));
			hamlFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[12], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			setLayout(new BorderLayout());
			setResizable(true);
			setSize(370, 520);

			for (int i = 0; i <= 11; i++)
				hamlFrameMainPanel.add(hamlFrameRadioButton[i]);

			// Version 1.9, to hide the case with haml from the wife since she is the one who dead!
			if (!maleSex) hamlFrameRadioButton[0].setEnabled(false);

			add(new JScrollPane(hamlFrameMainPanel), BorderLayout.CENTER);

			final JButton hamlFrameButton = new JButton(translations[13]);
			add(hamlFrameButton, BorderLayout.SOUTH);
			hamlFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 1.1, This condition is used to prevent the user from not selecting anything
					boolean continueHamlCase = false;
					for (int i = 0; i <= 11; i++)
						if (haml_index[i] != 0)
						{
							continueHamlCase = true;
							break;
						}

					if (!continueHamlCase)
						JOptionPane.showOptionDialog(MawarethSystem.this, translations[14], translations[15], JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{translations[16]}, translations[16]);
					else
					{
						/*
						 * Version 1.7
						 * Concatonate Initilisation _num[] for all.
						 *
						 * Logic:
						 * [Levels:Array Index:cnt] Descriptions. madhabName
						 *
						 * [0] when haml is dead. HANBALI/HANAFI/SHAFEE/MALIKI
						 * [1] when haml is 1 male. HANBALI/HANAFI/MALIKI
						 * [1] when haml is 2 males. SHAFEE
						 * [2] when haml is 1 female. HANBALI/HANAFI/MALIKI
						 * [2] when haml is 2 females. SHAFEE
						 * [3] when haml is 1 male & 1 female. HANBALI
						 * [3] final case. HANAFI
						 * [3] when haml is 4 males. SHAFEE
						 * [4] when haml is 2 males. HANBALI
						 * [4] when haml is 4 females. SHAFEE
						 * [5] when haml is 2 females. HANBALI
						 * [5] final case. SHAFEE
						 * [6] final case. HANBALI
						 */
						for (int k = 1; k <= 6; k++)
						{
							h_num[k] = h_num[0];
							w_num[k] = w_num[0];
							f_num[k] = f_num[0];
							m_num[k] = m_num[0];
							m_m_num[k] = m_m_num[0];
							m_f_num[k] = m_f_num[0];
							m_m_m_num[k] = m_m_m_num[0];
							m_m_f_num[k] = m_m_f_num[0];
							m_f_f_num[k] = m_f_f_num[0];
							f_f_num[k] = f_f_num[0];
							s_num[k] = s_num[0];
							d_num[k] = d_num[0];
							s_s_num[k] = s_s_num[0];
							d_s_num[k] = d_s_num[0];
							s_s_s_num[k] = s_s_s_num[0];
							d_s_s_num[k] = d_s_s_num[0];
							b_num[k] = b_num[0];
							sister_num[k] = sister_num[0];
							m_b_num[k] = m_b_num[0];
							m_sister_num[k] = m_sister_num[0];
							f_b_num[k] = f_b_num[0];
							f_sister_num[k] = f_sister_num[0];
							s_b_num[k] = s_b_num[0];
							s_b_f_num[k] = s_b_f_num[0];
							u_num[k] = u_num[0];
							u_f_num[k] = u_f_num[0];
							s_u_num[k] = s_u_num[0];
							s_u_f_num[k] = s_u_f_num[0];
						}

						// Version 1.7, Adding cases when haml is dead/(2 males)/(2 females) ...
						if (haml_index[0] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_num[1] = s_num[0] + 2;
								d_num[2] = d_num[0] + 2;
								s_num[3] = s_num[0] + 4;
								d_num[4] = d_num[0] + 4;
							}
							else
							{
								// i.e. HANBALI, HANAFI, MALIKI
								s_num[1] = s_num[0] + 1;
								d_num[2] = d_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								s_num[3] = s_num[0] + 1;
								d_num[3] = d_num[0] + 1;
								s_num[4] = s_num[0] + 2;
								d_num[5] = d_num[0] + 2;
							}

							haml_p = 1;
						}

						if (haml_index[1] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_s_num[1] = s_s_num[0] + 2;
								d_s_num[2] = d_s_num[0] + 2;
								s_s_num[3] = s_s_num[0] + 4;
								d_s_num[4] = d_s_num[0] + 4;
							}
							else
							{
								s_s_num[1] = s_s_num[0] + 1;
								d_s_num[2] = d_s_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								s_s_num[3] = s_s_num[0] + 1;
								d_s_num[3] = d_s_num[0] + 1;
								s_s_num[4] = s_s_num[0] + 2;
								d_s_num[5] = d_s_num[0] + 2;
							}

							haml_p = 2;
						}

						if (haml_index[2] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_s_s_num[1] = s_s_s_num[0] + 2;
								d_s_s_num[2] = d_s_s_num[0] + 2;
								s_s_s_num[3] = s_s_s_num[0] + 4;
								d_s_s_num[4] = d_s_s_num[0] + 4;
							}
							else
							{
								s_s_s_num[1] = s_s_s_num[0] + 1;
								d_s_s_num[2] = d_s_s_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								s_s_s_num[3] = s_s_s_num[0] + 1;
								d_s_s_num[3] = d_s_s_num[0] + 1;
								s_s_s_num[4] = s_s_s_num[0] + 2;
								d_s_s_num[5] = d_s_s_num[0] + 2;
							}

							haml_p = 3;
						}

						if (haml_index[3] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								b_num[1] = b_num[0] + 2;
								sister_num[2] = sister_num[0] + 2;
								b_num[3] = b_num[0] + 4;
								sister_num[4] = sister_num[0] + 4;
							}
							else
							{
								b_num[1] = b_num[0] + 1;
								sister_num[2] = sister_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								b_num[3] = b_num[0] + 1;
								sister_num[3] = sister_num[0] + 1;
								b_num[4] = b_num[0] + 2;
								sister_num[5] = sister_num[0] + 2;
							}

							haml_p = 4;
						}

						if (haml_index[4] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								m_b_num[1] = m_b_num[0] + 2;
								m_sister_num[2] = m_sister_num[0] + 2;
								m_b_num[3] = m_b_num[0] + 4;
								m_sister_num[4] = m_sister_num[0] + 4;
							}
							else
							{
								m_b_num[1] = m_b_num[0] + 1;
								m_sister_num[2] = m_sister_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								m_b_num[3] = m_b_num[0] + 1;
								m_sister_num[3] = m_sister_num[0] + 1;
								m_b_num[4] = m_b_num[0] + 2;
								m_sister_num[5] = m_sister_num[0] + 2;
							}

							haml_p = 5;
						}

						if (haml_index[5] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								f_b_num[1] = f_b_num[0] + 2;
								f_sister_num[2] = f_sister_num[0] + 2;
								f_b_num[3] = f_b_num[0] + 4;
								f_sister_num[4] = f_sister_num[0] + 4;
							}
							else
							{
								f_b_num[1] = f_b_num[0] + 1;
								f_sister_num[2] = f_sister_num[0] + 1;
							}

							if (madaheb == madhabName.HANBALI)
							{
								f_b_num[3] = f_b_num[0] + 1;
								f_sister_num[3] = f_sister_num[0] + 1;
								f_b_num[4] = f_b_num[0] + 2;
								f_sister_num[5] = f_sister_num[0] + 2;
							}

							haml_p = 6;
						}

						if (haml_index[6] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_b_num[1] = s_b_num[0] + 2;
								s_b_num[3] = s_b_num[0] + 4;
							}
							else
								s_b_num[1] = s_b_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								s_b_num[3] = s_b_num[0] + 1;
								s_b_num[4] = s_b_num[0] + 2;
							}

							haml_p = 7;
						}

						if (haml_index[7] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_b_f_num[1] = s_b_f_num[0] + 2;
								s_b_f_num[3] = s_b_f_num[0] + 4;
							}
							else
								s_b_f_num[1] = s_b_f_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								s_b_f_num[3] = s_b_f_num[0] + 1;
								s_b_f_num[4] = s_b_f_num[0] + 2;
							}

							haml_p = 8;
						}

						if (haml_index[8] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								u_num[1] = u_num[0] + 2;
								u_num[3] = u_num[0] + 4;
							}
							else
								u_num[1] = u_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								u_num[3] = u_num[0] + 1;
								u_num[4] = u_num[0] + 2;
							}

							haml_p = 9;
						}

						if (haml_index[9] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								u_f_num[1] = u_f_num[0] + 2;
								u_f_num[3] = u_f_num[0] + 4;
							}
							else
								u_f_num[1] = u_f_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								u_f_num[3] = u_f_num[0] + 1;
								u_f_num[4] = u_f_num[0] + 2;
							}

							haml_p = 10;
						}

						if (haml_index[10] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_u_num[1] = s_u_num[0] + 2;
								s_u_num[3] = s_u_num[0] + 4;
							}
							else
								s_u_num[1] = s_u_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								s_u_num[3] = s_u_num[0] + 1;
								s_u_num[4] = s_u_num[0] + 2;
							}

							haml_p = 11;
						}

						if (haml_index[11] != 0)
						{
							if (madaheb == madhabName.SHAFEE)
							{
								s_u_f_num[1] = s_u_f_num[0] + 2;
								s_u_f_num[3] = s_u_f_num[0] + 4;
							}
							else
								s_u_f_num[1] = s_u_f_num[0] + 1;

							if (madaheb == madhabName.HANBALI)
							{
								s_u_f_num[3] = s_u_f_num[0] + 1;
								s_u_f_num[4] = s_u_f_num[0] + 2;
							}

							haml_p = 12;
						}

						inheritorsCalculation(); //when cnt=0;
						cnt++;
						inheritorsCalculation(); //when cnt=1;
						cnt++;
						inheritorsCalculation(); //when cnt=2;

						if (madaheb == madhabName.HANBALI || madaheb == madhabName.SHAFEE)
						{
							cnt++;
							inheritorsCalculation(); //when cnt=3;
							cnt++;
							inheritorsCalculation(); //when cnt=4;

							if (madaheb == madhabName.HANBALI)
							{
								cnt++;
								inheritorsCalculation(); //when cnt=5;
							}
						}

						new MawarethSystemResults();
						dispose();
					}
				}
			});

			center(this);
			MawarethSystem.this.add(this);
			setTitle(translations[17]);
			setFrameIcon(null);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
			setVisible(true);
		}
	}

	class MonasakhaCase extends JInternalFrame
	{
		MonasakhaCase()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "MonasakhaCaseArabic.txt" : "MonasakhaCaseEnglish.txt"));

			monasakha_c = 1;

			final JLabel[] monasakhaFrameLabel = new JLabel[28];
			final JRadioButton[] monasakhaFrameRadioButton = new JRadioButton[28];
			for (int i = 0; i <= 27; i++)
			{
				monasakhaFrameLabel[i] = new JLabel(translations[i]);
				monasakhaFrameRadioButton[i] = new JRadioButton("", false);
			}

			final int[] monasakha_index = new int[28];
			final ActionListener monasakhaFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					for (int i = 0; i <= 27; i++)
					{
						if (ae.getSource() == monasakhaFrameRadioButton[i])
						{
							monasakha_index[i] = 1;
							for (int j = 0; j <= 27; j++)
								if (i != j)
									monasakha_index[j] = 0;
						}
					}
				}
			};

			final ButtonGroup monasakhaFrameRadioButtonGroup = new ButtonGroup();
			for (int i = 0; i <= 27; i++)
			{
				monasakhaFrameRadioButton[i].addActionListener(monasakhaFrameRadioButtonGroupListener);
				monasakhaFrameRadioButtonGroup.add(monasakhaFrameRadioButton[i]);
				monasakhaFrameRadioButton[i].setEnabled(false);
				monasakhaFrameLabel[i].setEnabled(false);
			}

			// Version 1.2, Making the choice to the user whether he wants to use vertical monasakha or horizontal one
			if (!verticalMonasakha)
				monasakha_cnt = 0;

			if (!h[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[0].setEnabled(true);
				monasakhaFrameLabel[0].setEnabled(true);
			}

			if (!w[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[1].setEnabled(true);
				monasakhaFrameLabel[1].setEnabled(true);
			}

			if (!f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[2].setEnabled(true);
				monasakhaFrameLabel[2].setEnabled(true);
			}

			if (!f_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[3].setEnabled(true);
				monasakhaFrameLabel[3].setEnabled(true);
			}

			if (!m[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[4].setEnabled(true);
				monasakhaFrameLabel[4].setEnabled(true);
			}

			if (!m_m[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[5].setEnabled(true);
				monasakhaFrameLabel[5].setEnabled(true);
			}

			if (!m_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[6].setEnabled(true);
				monasakhaFrameLabel[6].setEnabled(true);
			}

			if (!m_m_m[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[7].setEnabled(true);
				monasakhaFrameLabel[7].setEnabled(true);
			}

			if (!m_m_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[8].setEnabled(true);
				monasakhaFrameLabel[8].setEnabled(true);
			}

			if (!m_f_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[9].setEnabled(true);
				monasakhaFrameLabel[9].setEnabled(true);
			}

			if (!s[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[10].setEnabled(true);
				monasakhaFrameLabel[10].setEnabled(true);
			}

			if (!d[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[11].setEnabled(true);
				monasakhaFrameLabel[11].setEnabled(true);
			}

			if (!s_s[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[12].setEnabled(true);
				monasakhaFrameLabel[12].setEnabled(true);
			}

			if (!d_s[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[13].setEnabled(true);
				monasakhaFrameLabel[13].setEnabled(true);
			}

			if (!s_s_s[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[14].setEnabled(true);
				monasakhaFrameLabel[14].setEnabled(true);
			}

			if (!d_s_s[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[15].setEnabled(true);
				monasakhaFrameLabel[15].setEnabled(true);
			}

			if (!b[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[16].setEnabled(true);
				monasakhaFrameLabel[16].setEnabled(true);
			}

			if (!sister[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[17].setEnabled(true);
				monasakhaFrameLabel[17].setEnabled(true);
			}

			if (!m_b[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[18].setEnabled(true);
				monasakhaFrameLabel[18].setEnabled(true);
			}

			if (!m_sister[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[19].setEnabled(true);
				monasakhaFrameLabel[19].setEnabled(true);
			}

			if (!f_b[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[20].setEnabled(true);
				monasakhaFrameLabel[20].setEnabled(true);
			}

			if (!f_sister[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[21].setEnabled(true);
				monasakhaFrameLabel[21].setEnabled(true);
			}

			if (!s_b[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[22].setEnabled(true);
				monasakhaFrameLabel[22].setEnabled(true);
			}

			if (!s_b_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[23].setEnabled(true);
				monasakhaFrameLabel[23].setEnabled(true);
			}

			if (!u[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[24].setEnabled(true);
				monasakhaFrameLabel[24].setEnabled(true);
			}

			if (!u_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[25].setEnabled(true);
				monasakhaFrameLabel[25].setEnabled(true);
			}

			if (!s_u[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[26].setEnabled(true);
				monasakhaFrameLabel[26].setEnabled(true);
			}

			if (!s_u_f[monasakha_cnt].equals("0/1"))
			{
				monasakhaFrameRadioButton[27].setEnabled(true);
				monasakhaFrameLabel[27].setEnabled(true);
			}

			// Version 1.2, Making the choice to the user whether he wants to use vertical monasakha or horizontal one
			if (!verticalMonasakha)
			{
				final int[] inheritor_counter = new int[29]; // = Max(inheritor[?])
				for (int i = 0; i < cnt; i++)
				{
					int index;
					if (inheritor[i] == 28) index = 1; // For wife. miss order !
					else
						if (inheritor[i] == 1) index = 0; // For husband. miss order !
						else index = inheritor[i];

					if (inheritor[i] <= 9) //h, f, m, m_m, m_f ...
					{
						monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText() + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى)" : " (dead)"));
						monasakhaFrameRadioButton[index].setEnabled(false);
						monasakhaFrameLabel[index].setEnabled(false);
					}
					else
					{
						switch (inheritor[i])
						{
							case 10:
							{
								inheritor_counter[10]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[10] + ")"); // delim = " ("
								if (inheritor_counter[10] == s_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 11:
							{
								inheritor_counter[11]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[11] + ")");
								if (inheritor_counter[11] == d_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 12:
							{
								inheritor_counter[12]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[12] + ")");
								if (inheritor_counter[12] == s_s_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 13:
							{
								inheritor_counter[13]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[13] + ")");
								if (inheritor_counter[13] == d_s_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 14:
							{
								inheritor_counter[14]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[14] + ")");
								if (inheritor_counter[14] == s_s_s_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 15:
							{
								inheritor_counter[15]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[15] + ")");
								if (inheritor_counter[15] == d_s_s_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 16:
							{
								inheritor_counter[16]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[16] + ")");
								if (inheritor_counter[16] == b_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 17:
							{
								inheritor_counter[17]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[17] + ")");
								if (inheritor_counter[17] == sister_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 18:
							{
								inheritor_counter[18]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[18] + ")");
								if (inheritor_counter[18] == m_b_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 19:
							{
								inheritor_counter[19]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[19] + ")");
								if (inheritor_counter[19] == m_sister_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 20:
							{
								inheritor_counter[20]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[20] + ")");
								if (inheritor_counter[20] == f_b_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 21:
							{
								inheritor_counter[21]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[21] + ")");
								if (inheritor_counter[21] == f_sister_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 22:
							{
								inheritor_counter[22]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[22] + ")");
								if (inheritor_counter[22] == s_b_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 23:
							{
								inheritor_counter[23]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[23] + ")");
								if (inheritor_counter[23] == s_b_f_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 24:
							{
								inheritor_counter[24]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[24] + ")");
								if (inheritor_counter[24] == u_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 25:
							{
								inheritor_counter[25]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[25] + ")");
								if (inheritor_counter[25] == u_f_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 26:
							{
								inheritor_counter[26]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[26] + ")");
								if (inheritor_counter[26] == s_u_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}

							case 27:
							{
								inheritor_counter[27]++;
								monasakhaFrameLabel[index].setText(monasakhaFrameLabel[index].getText().split("( \\()")[0] + ((MaknoonIslamicEncyclopedia.language) ? " (متوفى " : " (dead ") + inheritor_counter[27] + ")");
								if (inheritor_counter[27] == s_u_f_num[0])
								{
									monasakhaFrameRadioButton[index].setEnabled(false);
									monasakhaFrameLabel[index].setEnabled(false);
								}
								break;
							}
						}
					}
				}
			}

			// Main panel for missing case
			final JPanel monasakhaFrameMainPanel = new JPanel();
			monasakhaFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[28] + (monasakha_cnt + 1) + "] " + ((MaknoonIslamicEncyclopedia.language) ? "؟" : "?"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JPanel borderPanel1 = new JPanel();//right as arabic
			final JPanel borderPanel2 = new JPanel();//left as arabic

			final JPanel GridPanel1 = new JPanel();//right-right
			final JPanel GridPanel2 = new JPanel();//right-center
			final JPanel GridPanel3 = new JPanel();//left-right
			final JPanel GridPanel4 = new JPanel();//left-center

			setLayout(new BorderLayout());
			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			setResizable(true);
			setFrameIcon(null);
			setTitle(translations[33]);
			monasakhaFrameMainPanel.setLayout(new GridLayout(1, 2));

			borderPanel1.setLayout(new BorderLayout());
			borderPanel2.setLayout(new BorderLayout());

			GridPanel1.setLayout(new GridLayout(14, 1));
			GridPanel2.setLayout(new GridLayout(14, 1));
			GridPanel3.setLayout(new GridLayout(14, 1));
			GridPanel4.setLayout(new GridLayout(14, 1));

			for (int i = 0; i <= 13; i++)
			{
				GridPanel1.add(monasakhaFrameRadioButton[i]);
				GridPanel2.add(monasakhaFrameLabel[i]);
			}

			for (int i = 14; i <= 27; i++)
			{
				GridPanel3.add(monasakhaFrameRadioButton[i]);
				GridPanel4.add(monasakhaFrameLabel[i]);
			}

			monasakhaFrameMainPanel.add(borderPanel1);
			monasakhaFrameMainPanel.add(borderPanel2);

			borderPanel1.add(GridPanel2, BorderLayout.CENTER);
			borderPanel2.add(GridPanel4, BorderLayout.CENTER);

			if (MaknoonIslamicEncyclopedia.language)
			{
				borderPanel1.add(GridPanel1, BorderLayout.EAST);
				borderPanel2.add(GridPanel3, BorderLayout.EAST);
			}
			else
			{
				borderPanel1.add(GridPanel1, BorderLayout.WEST);
				borderPanel2.add(GridPanel3, BorderLayout.WEST);
			}

			add(new JScrollPane(monasakhaFrameMainPanel), BorderLayout.CENTER);

			final JButton monasakhaFrameButton = new JButton(translations[29]);
			add(monasakhaFrameButton, BorderLayout.SOUTH);
			monasakhaFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 1.1, This condition is used to prevent the user from not selecting anything
					boolean continueMonasakhaCase = false;
					for (int i = 0; i <= 27; i++)
					{
						if (monasakha_index[i] != 0)
						{
							continueMonasakhaCase = true;
							break;
						}
					}

					if (!continueMonasakhaCase)
						JOptionPane.showOptionDialog(MawarethSystem.this, translations[30], translations[31], JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{translations[32]}, translations[32]);
					else
					{
						if (monasakha_index[0] != 0)
						{
							monasakha[cnt] = h[monasakha_cnt];
							inheritor[cnt] = 1;

							// To make the interface easy to use by not repeating the question of the maleSex of the dead
							maleSex = true;
						}

						if (monasakha_index[1] != 0)
						{
							monasakha[cnt] = w[monasakha_cnt];
							inheritor[cnt] = 28;//forget order.........!!!!!!!!!!
							maleSex = false;
						}

						if (monasakha_index[2] != 0)
						{
							monasakha[cnt] = f[monasakha_cnt];
							inheritor[cnt] = 2;
							maleSex = true;
						}

						if (monasakha_index[3] != 0)
						{
							monasakha[cnt] = f_f[monasakha_cnt];
							inheritor[cnt] = 3;
							maleSex = true;
						}

						if (monasakha_index[4] != 0)
						{
							monasakha[cnt] = m[monasakha_cnt];
							inheritor[cnt] = 4;
							maleSex = false;
						}

						if (monasakha_index[5] != 0)
						{
							monasakha[cnt] = m_m[monasakha_cnt];
							inheritor[cnt] = 5;
							maleSex = false;
						}

						if (monasakha_index[6] != 0)
						{
							monasakha[cnt] = m_f[monasakha_cnt];
							inheritor[cnt] = 6;
							maleSex = false;
						}

						if (monasakha_index[7] != 0)
						{
							monasakha[cnt] = m_m_m[monasakha_cnt];
							inheritor[cnt] = 7;
							maleSex = false;
						}

						if (monasakha_index[8] != 0)
						{
							monasakha[cnt] = m_m_f[monasakha_cnt];
							inheritor[cnt] = 8;
							maleSex = false;
						}

						if (monasakha_index[9] != 0)
						{
							monasakha[cnt] = m_f_f[monasakha_cnt];
							inheritor[cnt] = 9;
							maleSex = false;
						}

						if (monasakha_index[10] != 0)
						{
							monasakha[cnt] = s[monasakha_cnt];
							inheritor[cnt] = 10;
							maleSex = true;
						}

						if (monasakha_index[11] != 0)
						{
							monasakha[cnt] = d[monasakha_cnt];
							inheritor[cnt] = 11;
							maleSex = false;
						}

						if (monasakha_index[12] != 0)
						{
							monasakha[cnt] = s_s[monasakha_cnt];
							inheritor[cnt] = 12;
							maleSex = true;
						}

						if (monasakha_index[13] != 0)
						{
							monasakha[cnt] = d_s[monasakha_cnt];
							inheritor[cnt] = 13;
							maleSex = false;
						}

						if (monasakha_index[14] != 0)
						{
							monasakha[cnt] = s_s_s[monasakha_cnt];
							inheritor[cnt] = 14;
							maleSex = true;
						}

						if (monasakha_index[15] != 0)
						{
							monasakha[cnt] = d_s_s[monasakha_cnt];
							inheritor[cnt] = 15;
							maleSex = false;
						}

						if (monasakha_index[16] != 0)
						{
							monasakha[cnt] = b[monasakha_cnt];
							inheritor[cnt] = 16;
							maleSex = true;
						}

						if (monasakha_index[17] != 0)
						{
							monasakha[cnt] = sister[monasakha_cnt];
							inheritor[cnt] = 17;
							maleSex = false;
						}

						if (monasakha_index[18] != 0)
						{
							monasakha[cnt] = m_b[monasakha_cnt];
							inheritor[cnt] = 18;
							maleSex = true;
						}

						if (monasakha_index[19] != 0)
						{
							monasakha[cnt] = m_sister[monasakha_cnt];
							inheritor[cnt] = 19;
							maleSex = false;
						}

						if (monasakha_index[20] != 0)
						{
							monasakha[cnt] = f_b[monasakha_cnt];
							inheritor[cnt] = 20;
							maleSex = true;
						}

						if (monasakha_index[21] != 0)
						{
							monasakha[cnt] = f_sister[monasakha_cnt];
							inheritor[cnt] = 21;
							maleSex = false;
						}

						if (monasakha_index[22] != 0)
						{
							monasakha[cnt] = s_b[monasakha_cnt];
							inheritor[cnt] = 22;
							maleSex = true;
						}

						if (monasakha_index[23] != 0)
						{
							monasakha[cnt] = s_b_f[monasakha_cnt];
							inheritor[cnt] = 23;
							maleSex = true;
						}

						if (monasakha_index[24] != 0)
						{
							monasakha[cnt] = u[monasakha_cnt];
							inheritor[cnt] = 24;
							maleSex = true;
						}

						if (monasakha_index[25] != 0)
						{
							monasakha[cnt] = u_f[monasakha_cnt];
							inheritor[cnt] = 25;
							maleSex = true;
						}

						if (monasakha_index[26] != 0)
						{
							monasakha[cnt] = s_u[monasakha_cnt];
							inheritor[cnt] = 26;
							maleSex = true;
						}

						if (monasakha_index[27] != 0)
						{
							monasakha[cnt] = s_u_f[monasakha_cnt];
							inheritor[cnt] = 27;
							maleSex = true;
						}

						for (int i = 0; i <= 27; i++)
							monasakha_index[i] = 0;

						// Version 1.7, Just a hint to the user.
						for (int i = 0; i <= 27; i++)
						{
							if (monasakhaFrameRadioButton[i].isSelected())
							{
								if (MaknoonIslamicEncyclopedia.language)
									JOptionPane.showOptionDialog(MawarethSystem.this, "عليك الآن باختيار ورثة الميت (" + monasakhaFrameLabel[i].getText().split("( \\()")[0] + ") الذي قمت باختياره.", "تنبيه", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"متابعة"}, "متابعة");
								else
									JOptionPane.showMessageDialog(MawarethSystem.this, "You have now to select the Inheritors of the one that you have selected (" + monasakhaFrameLabel[i].getText().split("( \\()")[0] + ").", "Guide", JOptionPane.INFORMATION_MESSAGE);
								break;
							}
						}

						/*
						 * Version 1.1
						 * This update is to enable monasakha case to have many layers instead of one at the
						 * beginning. There is still one thing to do it and that is to enable to calculate many deaths
						 * from one layers.
						 */
						cnt++;
						dispose();

						if (maleSex)
							new InheritorsInterface(true);
							/*
							 * MawarethSystem.this.add(new InheritorsInterface(true));
							 * this will not work, please check !!
							 *
							 * Also this code will work but the layout of the panels is not correct
							 *
							 * {
									MawarethSystem.this.add(new InheritorsInterface(true));

									JInternalFrame salam [] = MawarethSystem.this.getAllFrames();

									try{salam[0].setMaximum(true);SwingUtilities.updateComponentTreeUI(salam[0]);}
									catch (java.beans.PropertyVetoException pve) {}
								}
							 */
						else
							new InheritorsInterface(false);

						// Only to display the last layer
						monasakha_cnt = cnt;
					}
				}
			});

			setSize(380, 520);
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
		}
	}

	class MissingCase extends JInternalFrame
	{
		MissingCase()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "MissingCaseArabic.txt" : "MissingCaseEnglish.txt"));

			missingCaseSelected = true;

			final JRadioButton[] missingFrameRadioButton = new JRadioButton[28];
			for (int i = 0; i <= 27; i++)
				missingFrameRadioButton[i] = new JRadioButton(translations[i], false);

			final int[] missing_index = new int[28];
			final ActionListener missingFrameRadioButtonGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					for (int i = 0; i <= 27; i++)
					{
						if (ae.getSource() == missingFrameRadioButton[i])
						{
							missing_index[i] = 1;
							for (int j = 0; j <= 27; j++)
								if (i != j)
									missing_index[j] = 0;
						}
					}
				}
			};

			final ButtonGroup missingFrameRadioButtonGroup = new ButtonGroup();
			for (int i = 0; i <= 27; i++)
			{
				missingFrameRadioButton[i].addActionListener(missingFrameRadioButtonGroupListener);
				missingFrameRadioButtonGroup.add(missingFrameRadioButton[i]);
				missingFrameRadioButton[i].setEnabled(false);
			}

			if (h_num[cnt] != 0) missingFrameRadioButton[0].setEnabled(true);
			if (w_num[cnt] != 0) missingFrameRadioButton[1].setEnabled(true);
			if (f_num[cnt] != 0) missingFrameRadioButton[2].setEnabled(true);
			if (m_num[cnt] != 0) missingFrameRadioButton[3].setEnabled(true);
			if (m_m_num[cnt] != 0) missingFrameRadioButton[4].setEnabled(true);
			if (m_f_num[cnt] != 0) missingFrameRadioButton[5].setEnabled(true);
			if (m_m_m_num[cnt] != 0) missingFrameRadioButton[6].setEnabled(true);
			if (m_m_f_num[cnt] != 0) missingFrameRadioButton[7].setEnabled(true);
			if (m_f_f_num[cnt] != 0) missingFrameRadioButton[8].setEnabled(true);
			if (f_f_num[cnt] != 0) missingFrameRadioButton[9].setEnabled(true);
			if (s_num[cnt] != 0) missingFrameRadioButton[10].setEnabled(true);
			if (d_num[cnt] != 0) missingFrameRadioButton[11].setEnabled(true);
			if (s_s_num[cnt] != 0) missingFrameRadioButton[12].setEnabled(true);
			if (d_s_num[cnt] != 0) missingFrameRadioButton[13].setEnabled(true);
			if (s_s_s_num[cnt] != 0) missingFrameRadioButton[14].setEnabled(true);
			if (d_s_s_num[cnt] != 0) missingFrameRadioButton[15].setEnabled(true);
			if (b_num[cnt] != 0) missingFrameRadioButton[16].setEnabled(true);
			if (sister_num[cnt] != 0) missingFrameRadioButton[17].setEnabled(true);
			if (m_b_num[cnt] != 0) missingFrameRadioButton[18].setEnabled(true);
			if (m_sister_num[cnt] != 0) missingFrameRadioButton[19].setEnabled(true);
			if (f_b_num[cnt] != 0) missingFrameRadioButton[20].setEnabled(true);
			if (f_sister_num[cnt] != 0) missingFrameRadioButton[21].setEnabled(true);
			if (s_b_num[cnt] != 0) missingFrameRadioButton[22].setEnabled(true);
			if (s_b_f_num[cnt] != 0) missingFrameRadioButton[23].setEnabled(true);
			if (u_num[cnt] != 0) missingFrameRadioButton[24].setEnabled(true);
			if (u_f_num[cnt] != 0) missingFrameRadioButton[25].setEnabled(true);
			if (s_u_num[cnt] != 0) missingFrameRadioButton[26].setEnabled(true);
			if (s_u_f_num[cnt] != 0) missingFrameRadioButton[27].setEnabled(true);

			final JPanel missingFrameMainPanel = new JPanel(new GridLayout(14, 2));
			missingFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[28], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			for (int i = 0; i <= 27; i++)
				missingFrameMainPanel.add(missingFrameRadioButton[i]);

			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			setResizable(true);
			add(new JScrollPane(missingFrameMainPanel), BorderLayout.CENTER);

			final JButton missingFrameButton = new JButton(translations[29]);
			add(missingFrameButton, BorderLayout.SOUTH);
			missingFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 1.1, This condition is used to prevent the user from not selecting anything
					boolean continueMissingCase = false;
					for (int i = 0; i <= 27; i++)
						if (missing_index[i] != 0)
						{
							continueMissingCase = true;
							break;
						}

					if (!continueMissingCase)
						JOptionPane.showOptionDialog(MawarethSystem.this, translations[30], translations[31], JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{translations[32]}, translations[32]);
					else
					{
						if (missing_index[0] != 0)
						{
							h_num[cnt + 1] = h_num[cnt] - 1;
							h_num[cnt] = h_num[cnt] - 1;
							missing[cnt] = h[cnt];

							if (h_num[cnt] == 0) h[cnt] = "0/1";
							missing_p = 1;
						}

						if (missing_index[1] != 0)
						{
							w_num[cnt + 1] = w_num[cnt] - 1;
							w_num[cnt] = w_num[cnt] - 1;
							missing[cnt] = w[cnt];

							if (w_num[cnt] == 0) w[cnt] = "0/1";
							missing_p = 2;
						}

						if (missing_index[2] != 0)
						{
							f_num[cnt + 1] = f_num[cnt] - 1;
							f_num[cnt] = f_num[cnt] - 1;
							missing[cnt] = f[cnt];

							if (f_num[cnt] == 0) f[cnt] = "0/1";
							missing_p = 3;
						}

						if (missing_index[3] != 0)
						{
							m_num[cnt + 1] = m_num[cnt] - 1;
							m_num[cnt] = m_num[cnt] - 1;
							missing[cnt] = m[cnt];

							if (m_num[cnt] == 0) m[cnt] = "0/1";
							missing_p = 4;
						}

						if (missing_index[4] != 0)
						{
							m_m_num[cnt + 1] = m_m_num[cnt] - 1;
							m_m_num[cnt] = m_m_num[cnt] - 1;
							missing[cnt] = m_m[cnt];

							if (m_m_num[cnt] == 0) m_m[cnt] = "0/1";
							missing_p = 5;
						}

						if (missing_index[5] != 0)
						{
							m_f_num[cnt + 1] = m_f_num[cnt] - 1;
							m_f_num[cnt] = m_f_num[cnt] - 1;
							missing[cnt] = m_f[cnt];

							if (m_f_num[cnt] == 0) m_f[cnt] = "0/1";
							missing_p = 6;
						}

						if (missing_index[6] != 0)
						{
							m_m_m_num[cnt + 1] = m_m_m_num[cnt] - 1;
							m_m_m_num[cnt] = m_m_m_num[cnt] - 1;
							missing[cnt] = m_m_m[cnt];

							if (m_m_m_num[cnt] == 0) m_m_m[cnt] = "0/1";
							missing_p = 7;
						}

						if (missing_index[7] != 0)
						{
							m_m_f_num[cnt + 1] = m_m_f_num[cnt] - 1;
							m_m_f_num[cnt] = m_m_f_num[cnt] - 1;
							missing[cnt] = m_m_f[cnt];

							if (m_m_f_num[cnt] == 0) m_m_f[cnt] = "0/1";
							missing_p = 8;
						}

						if (missing_index[8] != 0)
						{
							m_f_f_num[cnt + 1] = m_f_f_num[cnt] - 1;
							m_f_f_num[cnt] = m_f_f_num[cnt] - 1;
							missing[cnt] = m_f_f[cnt];

							if (m_f_f_num[cnt] == 0) m_f_f[cnt] = "0/1";
							missing_p = 9;
						}

						if (missing_index[9] != 0)
						{
							f_f_num[cnt + 1] = f_f_num[cnt] - 1;
							f_f_num[cnt] = f_f_num[cnt] - 1;
							missing[cnt] = f_f[cnt];

							if (f_f_num[cnt] == 0) f_f[cnt] = "0/1";
							missing_p = 10;
						}

						if (missing_index[10] != 0)
						{
							s_num[cnt + 1] = s_num[cnt] - 1;
							s_num[cnt] = s_num[cnt] - 1;
							missing[cnt] = s[cnt];

							if (s_num[cnt] == 0) s[cnt] = "0/1";
							missing_p = 11;
						}

						if (missing_index[11] != 0)
						{
							d_num[cnt + 1] = d_num[cnt] - 1;
							d_num[cnt] = d_num[cnt] - 1;
							missing[cnt] = d[cnt];

							if (d_num[cnt] == 0) d[cnt] = "0/1";
							missing_p = 12;
						}

						if (missing_index[12] != 0)
						{
							s_s_num[cnt + 1] = s_s_num[cnt] - 1;
							s_s_num[cnt] = s_s_num[cnt] - 1;
							missing[cnt] = s_s[cnt];

							if (s_s_num[cnt] == 0) s_s[cnt] = "0/1";
							missing_p = 13;
						}

						if (missing_index[13] != 0)
						{
							d_s_num[cnt + 1] = d_s_num[cnt] - 1;
							d_s_num[cnt] = d_s_num[cnt] - 1;
							missing[cnt] = d_s[cnt];

							if (d_s_num[cnt] == 0) d_s[cnt] = "0/1";
							missing_p = 14;
						}

						if (missing_index[14] != 0)
						{
							s_s_s_num[cnt + 1] = s_s_s_num[cnt] - 1;
							s_s_s_num[cnt] = s_s_s_num[cnt] - 1;
							missing[cnt] = s_s_s[cnt];

							if (s_s_s_num[cnt] == 0) s_s_s[cnt] = "0/1";
							missing_p = 15;
						}

						if (missing_index[15] != 0)
						{
							d_s_s_num[cnt + 1] = d_s_s_num[cnt] - 1;
							d_s_s_num[cnt] = d_s_s_num[cnt] - 1;
							missing[cnt] = d_s_s[cnt];

							if (d_s_s_num[cnt] == 0) d_s_s[cnt] = "0/1";
							missing_p = 16;
						}

						if (missing_index[16] != 0)
						{
							b_num[cnt + 1] = b_num[cnt] - 1;
							b_num[cnt] = b_num[cnt] - 1;
							missing[cnt] = b[cnt];

							if (b_num[cnt] == 0) b[cnt] = "0/1";
							missing_p = 17;
						}

						if (missing_index[17] != 0)
						{
							sister_num[cnt + 1] = sister_num[cnt] - 1;
							sister_num[cnt] = sister_num[cnt] - 1;
							missing[cnt] = sister[cnt];

							if (sister_num[cnt] == 0) sister[cnt] = "0/1";
							missing_p = 18;
						}

						if (missing_index[18] != 0)
						{
							m_b_num[cnt + 1] = m_b_num[cnt] - 1;
							m_b_num[cnt] = m_b_num[cnt] - 1;
							missing[cnt] = m_b[cnt];

							if (m_b_num[cnt] == 0) m_b[cnt] = "0/1";
							missing_p = 19;
						}

						if (missing_index[19] != 0)
						{
							m_sister_num[cnt + 1] = m_sister_num[cnt] - 1;
							m_sister_num[cnt] = m_sister_num[cnt] - 1;
							missing[cnt] = m_sister[cnt];

							if (m_sister_num[cnt] == 0) m_sister[cnt] = "0/1";
							missing_p = 20;
						}

						if (missing_index[20] != 0)
						{
							f_b_num[cnt + 1] = f_b_num[cnt] - 1;
							f_b_num[cnt] = f_b_num[cnt] - 1;
							missing[cnt] = f_b[cnt];

							if (f_b_num[cnt] == 0) f_b[cnt] = "0/1";
							missing_p = 21;
						}

						if (missing_index[21] != 0)
						{
							f_sister_num[cnt + 1] = f_sister_num[cnt] - 1;
							f_sister_num[cnt] = f_sister_num[cnt] - 1;
							missing[cnt] = f_sister[cnt];

							if (f_sister_num[cnt] == 0) f_sister[cnt] = "0/1";
							missing_p = 22;
						}

						if (missing_index[22] != 0)
						{
							s_b_num[cnt + 1] = s_b_num[cnt] - 1;
							s_b_num[cnt] = s_b_num[cnt] - 1;
							missing[cnt] = s_b[cnt];

							if (s_b_num[cnt] == 0) s_b[cnt] = "0/1";
							missing_p = 23;
						}

						if (missing_index[23] != 0)
						{
							s_b_f_num[cnt + 1] = s_b_f_num[cnt] - 1;
							s_b_f_num[cnt] = s_b_f_num[cnt] - 1;
							missing[cnt] = s_b_f[cnt];

							if (s_b_f_num[cnt] == 0) s_b_f[cnt] = "0/1";
							missing_p = 24;
						}

						if (missing_index[24] != 0)
						{
							u_num[cnt + 1] = u_num[cnt] - 1;
							u_num[cnt] = u_num[cnt] - 1;
							missing[cnt] = u[cnt];

							if (u_num[cnt] == 0) u[cnt] = "0/1";
							missing_p = 25;
						}

						if (missing_index[25] != 0)
						{
							u_f_num[cnt + 1] = u_f_num[cnt] - 1;
							u_f_num[cnt] = u_f_num[cnt] - 1;
							missing[cnt] = u_f[cnt];

							if (u_f_num[cnt] == 0) u_f[cnt] = "0/1";
							missing_p = 26;
						}

						if (missing_index[26] != 0)
						{
							s_u_num[cnt + 1] = s_u_num[cnt] - 1;
							s_u_num[cnt] = s_u_num[cnt] - 1;
							missing[cnt] = s_u[cnt];

							if (s_u_num[cnt] == 0) s_u[cnt] = "0/1";
							missing_p = 27;
						}

						if (missing_index[27] != 0)
						{
							s_u_f_num[cnt + 1] = s_u_f_num[cnt] - 1;
							s_u_f_num[cnt] = s_u_f_num[cnt] - 1;
							missing[cnt] = s_u_f[cnt];

							if (s_u_f_num[cnt] == 0) s_u_f[cnt] = "0/1";
							missing_p = 28;
						}

						//Reinitilise all _num to the second stage when they are not intilise in the previous stage
						if (h_num[cnt] != 0) h_num[1] = h_num[0];
						if (w_num[cnt] != 0) w_num[1] = w_num[0];
						if (f_num[cnt] != 0) f_num[1] = f_num[0];
						if (m_num[cnt] != 0) m_num[1] = m_num[0];
						if (m_m_num[cnt] != 0) m_m_num[1] = m_m_num[0];
						if (m_f_num[cnt] != 0) m_f_num[1] = m_f_num[0];
						if (m_m_m_num[cnt] != 0) m_m_m_num[1] = m_m_m_num[0];
						if (m_m_f_num[cnt] != 0) m_m_f_num[1] = m_m_f_num[0];
						if (m_f_f_num[cnt] != 0) m_f_f_num[1] = m_f_f_num[0];
						if (f_f_num[cnt] != 0) f_f_num[1] = f_f_num[0];
						if (s_num[cnt] != 0) s_num[1] = s_num[0];
						if (d_num[cnt] != 0) d_num[1] = d_num[0];
						if (s_s_num[cnt] != 0) s_s_num[1] = s_s_num[0];
						if (d_s_num[cnt] != 0) d_s_num[1] = d_s_num[0];
						if (s_s_s_num[cnt] != 0) s_s_s_num[1] = s_s_s_num[0];
						if (d_s_s_num[cnt] != 0) d_s_s_num[1] = d_s_s_num[0];
						if (b_num[cnt] != 0) b_num[1] = b_num[0];
						if (sister_num[cnt] != 0) sister_num[1] = sister_num[0];
						if (m_b_num[cnt] != 0) m_b_num[1] = m_b_num[0];
						if (m_sister_num[cnt] != 0) m_sister_num[1] = m_sister_num[0];
						if (f_b_num[cnt] != 0) f_b_num[1] = f_b_num[0];
						if (f_sister_num[cnt] != 0) f_sister_num[1] = f_sister_num[0];
						if (s_b_num[cnt] != 0) s_b_num[1] = s_b_num[0];
						if (s_b_f_num[cnt] != 0) s_b_f_num[1] = s_b_f_num[0];
						if (u_num[cnt] != 0) u_num[1] = u_num[0];
						if (u_f_num[cnt] != 0) u_f_num[1] = u_f_num[0];
						if (s_u_num[cnt] != 0) s_u_num[1] = s_u_num[0];
						if (s_u_f_num[cnt] != 0) s_u_f_num[1] = s_u_f_num[0];

						//Increase the counter
						cnt++;

						//Re-calculation
						inheritorsCalculation();
						new MawarethSystemResults();
						dispose();
					}
				}
			});

			setSize(380, 520);
			setFrameIcon(null);
			setTitle(translations[33]);
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
		}
	}

	// Version 1.2, This class is used to calculate bondman in hanbali madhab.
	class BondmanCase extends JInternalFrame
	{
		int numerator, denominator;

		BondmanCase()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "BondmanCaseArabic.txt" : "BondmanCaseEnglish.txt"));

			bondmanCaseSelected = true;

			final JLabel[] bondmanFrameLabel = new JLabel[28];
			final String[] bondmanPatterns = {"1/1", "3/4", "2/3", "1/2", "1/3", "1/4"};
			final JComboBox<String>[] bondmanFrameComboBox = new JComboBox[28];
			for (int i = 0; i <= 27; i++)
			{
				bondmanFrameLabel[i] = new JLabel(translations[i]);
				bondmanFrameComboBox[i] = new JComboBox<>(new DefaultComboBoxModel<>(bondmanPatterns));
				bondmanFrameComboBox[i].setEnabled(false);
			}

			final int[] bondman_index = new int[28];
			final ActionListener bondmanFrameComboboxGroupListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					for (int i = 0; i <= 27; i++)
					{
						if (ae.getSource() == bondmanFrameComboBox[i])
						{
							bondman_index[i] = 1;

							final StringTokenizer tokens = new StringTokenizer((String) bondmanFrameComboBox[i].getSelectedItem(), "/");
							numerator = Integer.parseInt(tokens.nextToken());
							denominator = Integer.parseInt(tokens.nextToken());

							if (denominator > monasakha_s)
							{
								JOptionPane.showMessageDialog(MawarethSystem.this, "monasakha_s is not enought, please increase it.", "Warning!", JOptionPane.ERROR_MESSAGE);
								bondman_index[i] = 0;

								// This is used to initialize others to the default state
								bondmanFrameComboBox[i].setModel(new DefaultComboBoxModel<>(bondmanPatterns));
							}
							else
							{
								for (int j = 0; j <= 27; j++)
								{
									if (i != j)
									{
										// Version 1.3
										bondmanFrameComboBox[j].setModel(new DefaultComboBoxModel<>(bondmanPatterns));
										bondman_index[j] = 0;
									}
								}
							}
						}
					}
				}
			};

			for (int i = 0; i <= 27; i++)
			{
				bondmanFrameComboBox[i].addActionListener(bondmanFrameComboboxGroupListener);
				bondmanFrameComboBox[i].setEnabled(false);
				bondmanFrameLabel[i].setEnabled(false);
			}

			if (h_num[cnt] != 0)
			{
				bondmanFrameComboBox[0].setEnabled(true);
				bondmanFrameLabel[0].setEnabled(true);
			}

			if (w_num[cnt] != 0)
			{
				bondmanFrameComboBox[1].setEnabled(true);
				bondmanFrameLabel[1].setEnabled(true);
			}

			if (f_num[cnt] != 0)
			{
				bondmanFrameComboBox[2].setEnabled(true);
				bondmanFrameLabel[2].setEnabled(true);
			}

			if (f_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[3].setEnabled(true);
				bondmanFrameLabel[3].setEnabled(true);
			}

			if (m_num[cnt] != 0)
			{
				bondmanFrameComboBox[4].setEnabled(true);
				bondmanFrameLabel[4].setEnabled(true);
			}

			if (m_m_num[cnt] != 0)
			{
				bondmanFrameComboBox[5].setEnabled(true);
				bondmanFrameLabel[5].setEnabled(true);
			}

			if (m_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[6].setEnabled(true);
				bondmanFrameLabel[6].setEnabled(true);
			}

			if (m_m_m_num[cnt] != 0)
			{
				bondmanFrameComboBox[7].setEnabled(true);
				bondmanFrameLabel[7].setEnabled(true);
			}

			if (m_m_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[8].setEnabled(true);
				bondmanFrameLabel[8].setEnabled(true);
			}

			if (m_f_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[9].setEnabled(true);
				bondmanFrameLabel[9].setEnabled(true);
			}

			if (s_num[cnt] != 0)
			{
				bondmanFrameComboBox[10].setEnabled(true);
				bondmanFrameLabel[10].setEnabled(true);
			}

			if (d_num[cnt] != 0)
			{
				bondmanFrameComboBox[11].setEnabled(true);
				bondmanFrameLabel[11].setEnabled(true);
			}

			if (s_s_num[cnt] != 0)
			{
				bondmanFrameComboBox[12].setEnabled(true);
				bondmanFrameLabel[12].setEnabled(true);
			}

			if (d_s_num[cnt] != 0)
			{
				bondmanFrameComboBox[13].setEnabled(true);
				bondmanFrameLabel[13].setEnabled(true);
			}

			if (s_s_s_num[cnt] != 0)
			{
				bondmanFrameComboBox[14].setEnabled(true);
				bondmanFrameLabel[14].setEnabled(true);
			}

			if (d_s_s_num[cnt] != 0)
			{
				bondmanFrameComboBox[15].setEnabled(true);
				bondmanFrameLabel[15].setEnabled(true);
			}

			if (b_num[cnt] != 0)
			{
				bondmanFrameComboBox[16].setEnabled(true);
				bondmanFrameLabel[16].setEnabled(true);
			}

			if (sister_num[cnt] != 0)
			{
				bondmanFrameComboBox[17].setEnabled(true);
				bondmanFrameLabel[17].setEnabled(true);
			}

			if (m_b_num[cnt] != 0)
			{
				bondmanFrameComboBox[18].setEnabled(true);
				bondmanFrameLabel[18].setEnabled(true);
			}

			if (m_sister_num[cnt] != 0)
			{
				bondmanFrameComboBox[19].setEnabled(true);
				bondmanFrameLabel[19].setEnabled(true);
			}

			if (f_b_num[cnt] != 0)
			{
				bondmanFrameComboBox[20].setEnabled(true);
				bondmanFrameLabel[20].setEnabled(true);
			}

			if (f_sister_num[cnt] != 0)
			{
				bondmanFrameComboBox[21].setEnabled(true);
				bondmanFrameLabel[21].setEnabled(true);
			}

			if (s_b_num[cnt] != 0)
			{
				bondmanFrameComboBox[22].setEnabled(true);
				bondmanFrameLabel[22].setEnabled(true);
			}

			if (s_b_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[23].setEnabled(true);
				bondmanFrameLabel[23].setEnabled(true);
			}

			if (u_num[cnt] != 0)
			{
				bondmanFrameComboBox[24].setEnabled(true);
				bondmanFrameLabel[24].setEnabled(true);
			}

			if (u_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[25].setEnabled(true);
				bondmanFrameLabel[25].setEnabled(true);
			}

			if (s_u_num[cnt] != 0)
			{
				bondmanFrameComboBox[26].setEnabled(true);
				bondmanFrameLabel[26].setEnabled(true);
			}

			if (s_u_f_num[cnt] != 0)
			{
				bondmanFrameComboBox[27].setEnabled(true);
				bondmanFrameLabel[27].setEnabled(true);
			}

			// Main panel for missing case
			final JPanel bondmanFrameMainPanel = new JPanel();
			bondmanFrameMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[28], TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_JUSTIFICATION, null, Color.red));

			final JPanel borderPanel1 = new JPanel();//right as arabic
			final JPanel borderPanel2 = new JPanel();//left as arabic

			final JPanel GridPanel1 = new JPanel();//right-right
			final JPanel GridPanel2 = new JPanel();//right-center
			final JPanel GridPanel3 = new JPanel();//left-right
			final JPanel GridPanel4 = new JPanel();//left-center

			//putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			setLayout(new BorderLayout());
			setResizable(true);
			setFrameIcon(null);
			setTitle(translations[33]);

			bondmanFrameMainPanel.setLayout(new GridLayout(1, 2));

			borderPanel1.setLayout(new BorderLayout());
			borderPanel2.setLayout(new BorderLayout());

			GridPanel1.setLayout(new GridLayout(14, 1));
			GridPanel2.setLayout(new GridLayout(14, 1));
			GridPanel3.setLayout(new GridLayout(14, 1));
			GridPanel4.setLayout(new GridLayout(14, 1));

			JPanel bondmanFrameComboBoxDesignPanel;
			for (int i = 0; i <= 13; i++)
			{
				bondmanFrameComboBoxDesignPanel = new JPanel();
				bondmanFrameComboBoxDesignPanel.add(bondmanFrameComboBox[i]);
				GridPanel1.add(bondmanFrameComboBoxDesignPanel);
				GridPanel2.add(bondmanFrameLabel[i]);
			}

			for (int i = 14; i <= 27; i++)
			{
				bondmanFrameComboBoxDesignPanel = new JPanel();
				bondmanFrameComboBoxDesignPanel.add(bondmanFrameComboBox[i]);
				GridPanel3.add(bondmanFrameComboBoxDesignPanel);
				GridPanel4.add(bondmanFrameLabel[i]);
			}

			bondmanFrameMainPanel.add(borderPanel1);
			bondmanFrameMainPanel.add(borderPanel2);

			borderPanel1.add(GridPanel2, BorderLayout.CENTER);
			borderPanel2.add(GridPanel4, BorderLayout.CENTER);

			if (MaknoonIslamicEncyclopedia.language)
			{
				borderPanel1.add(GridPanel1, BorderLayout.EAST);
				borderPanel2.add(GridPanel3, BorderLayout.EAST);
			}
			else
			{
				borderPanel1.add(GridPanel1, BorderLayout.WEST);
				borderPanel2.add(GridPanel3, BorderLayout.WEST);
			}

			add(new JScrollPane(bondmanFrameMainPanel), BorderLayout.CENTER);

			final JButton bondmanFrameButton = new JButton(translations[29]);
			add(bondmanFrameButton, BorderLayout.SOUTH);
			bondmanFrameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Version 1.1, This condition is used to prevent the user from not selecting anything
					boolean continuebondmanCase = false;
					for (int i = 0; i <= 27; i++)
					{
						if (bondman_index[i] != 0)
						{
							continuebondmanCase = true;
							break;
						}
					}

					if (!continuebondmanCase)
						JOptionPane.showOptionDialog(MawarethSystem.this, translations[30], translations[31], JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{translations[32]}, translations[32]);
					else
					{
						for (int i = 0; i < denominator; i++)
						{
							//Reinitialise all _num to the second stage when they are not initialise in the previous stage
							if (h_num[0] != 0) h_num[i] = h_num[0];
							if (w_num[0] != 0) w_num[i] = w_num[0];
							if (f_num[0] != 0) f_num[i] = f_num[0];
							if (m_num[0] != 0) m_num[i] = m_num[0];
							if (m_m_num[0] != 0) m_m_num[i] = m_m_num[0];
							if (m_f_num[0] != 0) m_f_num[i] = m_f_num[0];
							if (m_m_m_num[0] != 0) m_m_m_num[i] = m_m_m_num[0];
							if (m_m_f_num[0] != 0) m_m_f_num[i] = m_m_f_num[0];
							if (m_f_f_num[0] != 0) m_f_f_num[i] = m_f_f_num[0];
							if (f_f_num[0] != 0) f_f_num[i] = f_f_num[0];
							if (s_num[0] != 0) s_num[i] = s_num[0];
							if (d_num[0] != 0) d_num[i] = d_num[0];
							if (s_s_num[0] != 0) s_s_num[i] = s_s_num[0];
							if (d_s_num[0] != 0) d_s_num[i] = d_s_num[0];
							if (s_s_s_num[0] != 0) s_s_s_num[i] = s_s_s_num[0];
							if (d_s_s_num[0] != 0) d_s_s_num[i] = d_s_s_num[0];
							if (b_num[0] != 0) b_num[i] = b_num[0];
							if (sister_num[0] != 0) sister_num[i] = sister_num[0];
							if (m_b_num[0] != 0) m_b_num[i] = m_b_num[0];
							if (m_sister_num[0] != 0) m_sister_num[i] = m_sister_num[0];
							if (f_b_num[0] != 0) f_b_num[i] = f_b_num[0];
							if (f_sister_num[0] != 0) f_sister_num[i] = f_sister_num[0];
							if (s_b_num[0] != 0) s_b_num[i] = s_b_num[0];
							if (s_b_f_num[0] != 0) s_b_f_num[i] = s_b_f_num[0];
							if (u_num[0] != 0) u_num[i] = u_num[0];
							if (u_f_num[0] != 0) u_f_num[i] = u_f_num[0];
							if (s_u_num[0] != 0) s_u_num[i] = s_u_num[0];
							if (s_u_f_num[0] != 0) s_u_f_num[i] = s_u_f_num[0];
						}

						for (; cnt < denominator; cnt++)
						{
							if (numerator > 0)
								inheritorsCalculation();

							if (bondman_index[0] != 0)
							{
								h_num[cnt]--;
								if (numerator > 0) bondman[cnt] = h[cnt];
								if (h_num[cnt] == 0) h[cnt] = "0/1";
								bondman_p = 1;
							}

							if (bondman_index[1] != 0)
							{
								w_num[cnt]--;
								if (numerator > 0) bondman[cnt] = w[cnt];
								if (w_num[cnt] == 0) w[cnt] = "0/1";
								bondman_p = 2;
							}

							if (bondman_index[2] != 0)
							{
								f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = f[cnt];
								if (f_num[cnt] == 0) f[cnt] = "0/1";
								bondman_p = 3;
							}

							if (bondman_index[3] != 0)
							{
								f_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = f_f[cnt];
								if (f_f_num[cnt] == 0) f_f[cnt] = "0/1";
								bondman_p = 4;
							}

							if (bondman_index[4] != 0)
							{
								m_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m[cnt];
								if (m_num[cnt] == 0) m[cnt] = "0/1";
								bondman_p = 5;
							}

							if (bondman_index[5] != 0)
							{
								m_m_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_m[cnt];
								if (m_m_num[cnt] == 0) m_m[cnt] = "0/1";
								bondman_p = 6;
							}

							if (bondman_index[6] != 0)
							{
								m_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_f[cnt];
								if (m_f_num[cnt] == 0) m_f[cnt] = "0/1";
								bondman_p = 7;
							}

							if (bondman_index[7] != 0)
							{
								m_m_m_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_m_m[cnt];
								if (m_m_m_num[cnt] == 0) m_m_m[cnt] = "0/1";
								bondman_p = 8;
							}

							if (bondman_index[8] != 0)
							{
								m_m_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_m_f[cnt];
								if (m_m_f_num[cnt] == 0) m_m_f[cnt] = "0/1";
								bondman_p = 9;
							}

							if (bondman_index[9] != 0)
							{
								m_f_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_f_f[cnt];
								if (m_f_f_num[cnt] == 0) m_f_f[cnt] = "0/1";
								bondman_p = 10;
							}

							if (bondman_index[10] != 0)
							{
								s_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s[cnt];
								if (s_num[cnt] == 0) s[cnt] = "0/1";
								bondman_p = 11;
							}

							if (bondman_index[11] != 0)
							{
								d_num[cnt]--;
								if (numerator > 0) bondman[cnt] = d[cnt];
								if (d_num[cnt] == 0) d[cnt] = "0/1";
								bondman_p = 12;
							}

							if (bondman_index[12] != 0)
							{
								s_s_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_s[cnt];
								if (s_s_num[cnt] == 0) s_s[cnt] = "0/1";
								bondman_p = 13;
							}

							if (bondman_index[13] != 0)
							{
								d_s_num[cnt]--;
								if (numerator > 0) bondman[cnt] = d_s[cnt];
								if (d_s_num[cnt] == 0) d_s[cnt] = "0/1";
								bondman_p = 14;
							}

							if (bondman_index[14] != 0)
							{
								s_s_s_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_s_s[cnt];
								if (s_s_s_num[cnt] == 0) s_s_s[cnt] = "0/1";
								bondman_p = 15;
							}

							if (bondman_index[15] != 0)
							{
								d_s_s_num[cnt]--;
								if (numerator > 0) bondman[cnt] = d_s_s[cnt];
								if (d_s_s_num[cnt] == 0) d_s_s[cnt] = "0/1";
								bondman_p = 16;
							}

							if (bondman_index[16] != 0)
							{
								b_num[cnt]--;
								if (numerator > 0) bondman[cnt] = b[cnt];
								if (b_num[cnt] == 0) b[cnt] = "0/1";
								bondman_p = 17;
							}

							if (bondman_index[17] != 0)
							{
								sister_num[cnt]--;
								if (numerator > 0) bondman[cnt] = sister[cnt];
								if (sister_num[cnt] == 0) sister[cnt] = "0/1";
								bondman_p = 18;
							}

							if (bondman_index[18] != 0)
							{
								m_b_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_b[cnt];
								if (m_b_num[cnt] == 0) m_b[cnt] = "0/1";
								bondman_p = 19;
							}

							if (bondman_index[19] != 0)
							{
								m_sister_num[cnt]--;
								if (numerator > 0) bondman[cnt] = m_sister[cnt];
								if (m_sister_num[cnt] == 0) m_sister[cnt] = "0/1";
								bondman_p = 20;
							}

							if (bondman_index[20] != 0)
							{
								f_b_num[cnt]--;
								if (numerator > 0) bondman[cnt] = f_b[cnt];
								if (f_b_num[cnt] == 0) f_b[cnt] = "0/1";
								bondman_p = 21;
							}

							if (bondman_index[21] != 0)
							{
								f_sister_num[cnt]--;
								if (numerator > 0) bondman[cnt] = f_sister[cnt];
								if (f_sister_num[cnt] == 0) f_sister[cnt] = "0/1";
								bondman_p = 22;
							}

							if (bondman_index[22] != 0)
							{
								s_b_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_b[cnt];
								if (s_b_num[cnt] == 0) s_b[cnt] = "0/1";
								bondman_p = 23;
							}

							if (bondman_index[23] != 0)
							{
								s_b_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_b_f[cnt];
								if (s_b_f_num[cnt] == 0) s_b_f[cnt] = "0/1";
								bondman_p = 24;
							}

							if (bondman_index[24] != 0)
							{
								u_num[cnt]--;
								if (numerator > 0) bondman[cnt] = u[cnt];
								if (u_num[cnt] == 0) u[cnt] = "0/1";
								bondman_p = 25;
							}

							if (bondman_index[25] != 0)
							{
								u_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = u_f[cnt];
								if (u_f_num[cnt] == 0) u_f[cnt] = "0/1";
								bondman_p = 26;
							}

							if (bondman_index[26] != 0)
							{
								s_u_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_u[cnt];
								if (s_u_num[cnt] == 0) s_u[cnt] = "0/1";
								bondman_p = 27;
							}

							if (bondman_index[27] != 0)
							{
								s_u_f_num[cnt]--;
								if (numerator > 0) bondman[cnt] = s_u_f[cnt];
								if (s_u_f_num[cnt] == 0) s_u_f[cnt] = "0/1";
								bondman_p = 28;
							}

							if (numerator > 0)
								numerator--;
							else
								inheritorsCalculation();
						}

						// To remove the last increamental of the global counter cnt
						cnt--;
						new MawarethSystemResults();
						dispose();
					}
				}
			});

			setSize(380, 580);
			center(this);
			MawarethSystem.this.add(this);

			if (MaknoonIslamicEncyclopedia.language)
			{
				applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			setVisible(true);
		}
	}

	// resultsDataVector should be global since in madhabName.ALL, We need to build on resultsDataVector in each rotation.
	private final Vector<ResultsData> resultsDataVector = new Vector<>(10, 10);
	private final Vector<String> noteVector = new Vector<>(10, 10);

	public class MawarethSystemResults extends JInternalFrame implements Printable
	{
		// Version 1.7, Made global
		boolean emptyMowqofColumn = true;

		public MawarethSystemResults()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "MawarethSystemResultsArabic.txt" : "MawarethSystemResultsEnglish.txt"));

			setTitle(translations[1]);
			setMaximizable(true);
			setResizable(true);
			setFrameIcon(null);

			// Version 1.7, hamlCaseSelected instead of haml_p!=0
			if (hamlCaseSelected) hamlCalculationHelper();
			if (missing_p != 0) missingCalculationHelper();

			if (khontha_w != 0) // To not enter when monasakha is taken place
				khonthaCalculationHelper();

			// To change cnt depends on monasakha or khontha
			if (khontha_p != 0 || missing_p != 0) cnt = 2;

			// Version 1.7, hamlCaseSelected instead of haml_p!=0
			if (hamlCaseSelected)
			{
				switch (madaheb)
				{
					case MALIKI:
						cnt = 0;
						break;
					case HANBALI:
						cnt = 6;
						break;
					case SHAFEE:
						cnt = 5;
						break;
					default:
						cnt = 3;
						break;
				}
			}

			// Final results after monasakha or khontha
			for (int i = 0; i < monasakha_s; i++)
				monasakha_m[i] = "1/1"; // Because it is multiplayer

			// Calculate monasakha_m (i.e. multipleyer) for all layers
			if (inheritor[0] != 0) // To not calculate when khontha
			{
				if (verticalMonasakha)
				{
					for (int i = 1; i <= cnt; i++)
						/*
						 * Version 1.2
						 * use [i-1] instead of layer[i-1] to be able to handle the two cases of the monasakha case
						 * previously code: monasakha_m[i]=multiply(monasakha_m[layer[i-1]], monasakha[layer[i-1]]);
						 */
						monasakha_m[i] = multiply(monasakha_m[i - 1], monasakha[i - 1]);
				}
				else
				{
					// Version 1.2, monasakha_m[0] since it is horizontal case.
					for (int i = 1; i <= cnt; i++)
						monasakha_m[i] = multiply(monasakha_m[0], monasakha[i - 1]);
				}

				for (int i = 0; i <= cnt; i++) // Version 2.0, Removed ( ... && inheritor[0]!=0) and included in the above condition.
				{
					h[i] = multiply(monasakha_m[i], h[i]);
					w[i] = multiply(monasakha_m[i], w[i]);
					f[i] = multiply(monasakha_m[i], f[i]);
					m[i] = multiply(monasakha_m[i], m[i]);
					m_m[i] = multiply(monasakha_m[i], m_m[i]);
					m_f[i] = multiply(monasakha_m[i], m_f[i]);
					m_m_m[i] = multiply(monasakha_m[i], m_m_m[i]);
					m_m_f[i] = multiply(monasakha_m[i], m_m_f[i]);
					m_f_f[i] = multiply(monasakha_m[i], m_f_f[i]);
					f_f[i] = multiply(monasakha_m[i], f_f[i]);
					s[i] = multiply(monasakha_m[i], s[i]);
					d[i] = multiply(monasakha_m[i], d[i]);
					s_s[i] = multiply(monasakha_m[i], s_s[i]);
					d_s[i] = multiply(monasakha_m[i], d_s[i]);
					s_s_s[i] = multiply(monasakha_m[i], s_s_s[i]);
					d_s_s[i] = multiply(monasakha_m[i], d_s_s[i]);
					b[i] = multiply(monasakha_m[i], b[i]);
					sister[i] = multiply(monasakha_m[i], sister[i]);
					m_b[i] = multiply(monasakha_m[i], m_b[i]);
					m_sister[i] = multiply(monasakha_m[i], m_sister[i]);
					f_b[i] = multiply(monasakha_m[i], f_b[i]);
					f_sister[i] = multiply(monasakha_m[i], f_sister[i]);
					s_b[i] = multiply(monasakha_m[i], s_b[i]);
					s_b_f[i] = multiply(monasakha_m[i], s_b_f[i]);
					u[i] = multiply(monasakha_m[i], u[i]);
					u_f[i] = multiply(monasakha_m[i], u_f[i]);
					s_u[i] = multiply(monasakha_m[i], s_u[i]);
					s_u_f[i] = multiply(monasakha_m[i], s_u_f[i]);
					muslim_trusts[i] = multiply(monasakha_m[i], muslim_trusts[i]);
				}
			}

			int LCDDenominator = 1;
			//int arham_awle_to=1;

			// Initially until we finish reading the special cases LCD
			for (int y = (allMadaheb ? cnt : 0); y <= cnt; y++)
			{
				/*
				 * Version 1.2
				 * Finding the Least Common Denominator for inheritors.
				 *
				if(arham_p==0)
					;// Done in inheritorsCalculation() to solve rud cases.
				else
				{
					/* TO DO: Remove this, no need
					if(h[y]!="0/1")easy++;
					if(w[y]!="0/1")easy++;
					if(a_s_d[y]!="0/1")easy++;
					if(a_d_d[y]!="0/1")easy++;
					if(a_s_d_s[y]!="0/1")easy++;
					if(a_d_d_s[y]!="0/1")easy++;
					if(a_s_d_d[y]!="0/1")easy++;
					if(a_d_d_d[y]!="0/1")easy++;
					if(a_s_s_d[y]!="0/1")easy++;
					if(a_d_s_d[y]!="0/1")easy++;
					if(a_f_m[y]!="0/1")easy++;
					if(a_f_f_m[y]!="0/1")easy++;
					if(a_f_m_f[y]!="0/1")easy++;
					if(a_f_m_m[y]!="0/1")easy++;
					if(a_m_f_m[y]!="0/1")easy++;
					if(a_s_sister[y]!="0/1")easy++;
					if(a_d_sister[y]!="0/1")easy++;
					if(a_d_b[y]!="0/1")easy++;
					if(a_d_msister[y]!="0/1")easy++;
					if(a_s_msister[y]!="0/1")easy++;
					if(a_d_mb[y]!="0/1")easy++;
					if(a_s_mb[y]!="0/1")easy++;
					if(a_s_fsister[y]!="0/1")easy++;
					if(a_d_fsister[y]!="0/1")easy++;
					if(a_d_fb[y]!="0/1")easy++;
					if(a_ul[y]!="0/1")easy++;
					if(a_k[y]!="0/1")easy++;
					if(a_kl[y]!="0/1")easy++;
					if(a_s_ul[y]!="0/1")easy++;
					if(a_d_ul[y]!="0/1")easy++;
					if(a_d_u[y]!="0/1")easy++;
					if(a_s_k[y]!="0/1")easy++;
					if(a_d_k[y]!="0/1")easy++;
					if(a_s_kl[y]!="0/1")easy++;
					if(a_d_kl[y]!="0/1")easy++;
				}
				*/

				if (arham_p == 0)
				{
					LCDDenominator = LCD(new int[]{getDenominator(h[y]), getDenominator(w[y]), getDenominator(f[y]), getDenominator(f_f[y]),
							getDenominator(m[y]), getDenominator(m_m[y]), getDenominator(m_f[y]), getDenominator(m_m_m[y]), getDenominator(m_m_f[y]),
							getDenominator(m_f_f[y]), getDenominator(s[y]), getDenominator(d[y]), getDenominator(s_s[y]), getDenominator(d_s[y]),
							getDenominator(s_s_s[y]), getDenominator(d_s_s[y]), getDenominator(b[y]), getDenominator(sister[y]), getDenominator(m_b[y]),
							getDenominator(m_sister[y]), getDenominator(f_b[y]), getDenominator(f_sister[y]), getDenominator(s_b[y]), getDenominator(s_b_f[y]),
							getDenominator(u[y]), getDenominator(u_f[y]), getDenominator(s_u[y]), getDenominator(s_u_f[y]), getDenominator(khontha[y]),
							getDenominator(missing[y]), getDenominator(haml[y]), getDenominator(muslim_trusts[y]),
							// Version 1.6/1.9, w_s_s, w_d_s, w_d_d, w_s_d are added.
							// Version 2.4, added w_f , w_m
							getDenominator(willsMaleSex ? w_s_s : w_s_d), getDenominator(willsMaleSex ? w_d_s : w_d_d), getDenominator(w_f), getDenominator(w_m),
							// add to calculate for all layers
							LCDDenominator, getDenominator(bondman[y])});

					/*
					 * This condition is done to make sure that it will calculate it for the main layer of
					 * inheritors and avoid problems with special cases.
					 *
					if(y==0 || allMadaheb)
					{
						if(taseeb && !farad)
						{
							//awle_to = 1;
							awle_to = LCD(new int[]{getDenominator(s[y]), getDenominator(d[y]), getDenominator(s_s[y]), getDenominator(d_s[y]), getDenominator(s_s_s[y]),
							getDenominator(d_s_s[y]), getDenominator(b[y]), getDenominator(sister[y]), getDenominator(f_b[y]), getDenominator(f_sister[y]),
							getDenominator(s_b[y]), getDenominator(s_b_f[y]), getDenominator(u[y]), getDenominator(u_f[y]), getDenominator(s_u[y]), getDenominator(s_u_f[y]),
							// Udate version 1.7
							getDenominator(f_f[y]),
							awle_to});
						}
						else
						{
							// Version 1.7, Change [cnt] to [y] (a lot of them !!)
							awle_to = LCD(new int[]{getDenominator(h[y]), getDenominator(multiply(w[y], w_num[y])),
							// Version 1.7 f & f_f are always 6 (fixed) unless mirathAlJadAndEkhwah=true
							// Version 1.7, Removed: LCD(getDenominator(f[y]), getDenominator(f_f[y]))
							(mirathAlJadAndEkhwah)?getDenominator(f_f[y]):((f_num[y]!=0 || f_f_num[y]!=0)?6:1),
							getDenominator(m[y]),
							// Version 1.6, To work if denominator is not 6 due to awle
							getDenominator(add(new String[]{m_m[y], m_f[y]})),
							// Version 1.3
							((s_num[y]==0)?getDenominator(multiply(d[y], d_num[y])):1),
							// Version 1.3
							((s_s_num[y]==0 && !(s_s_s_num[y]>0 && d_num[y]>=2))?getDenominator(multiply(d_s[y], d_s_num[y])):1),
							// Version 1.3
							((s_s_s_num[y]==0)?getDenominator(multiply(d_s_s[y], d_s_s_num[y])):1),
							// Version 1.6, To work if denominator is not 6 due to awle
							getDenominator(add(new String[]{m_m_m[y], m_m_f[y], m_f_f[y]})),
							getDenominator(multiply(m_b[y], m_b_num[y])),
							getDenominator(multiply(m_sister[y], m_sister_num[y])),
							awle_to,// add to calculate for all layers
							// Version 1.6
							(b_num[y]==0)?getDenominator(multiply(sister[y], sister_num[y])):1,
							(f_b_num[y]==0)?getDenominator(multiply(f_sister[y], f_sister_num[y])):1});
						}
					}
					*/
				}
				else
				{
					LCDDenominator = LCD(new int[]{getDenominator(h[y]), getDenominator(w[y]), getDenominator(a_f_m[y]), getDenominator(a_f_f_m[y]),
							getDenominator(a_f_m_f[y]), getDenominator(a_f_m_m[y]), getDenominator(a_m_f_m[y]), getDenominator(a_d_d[y]), getDenominator(a_s_d[y]),
							getDenominator(a_s_d_s[y]), getDenominator(a_d_d_s[y]), getDenominator(a_s_d_d[y]), getDenominator(a_d_d_d[y]), getDenominator(a_s_s_d[y]),
							getDenominator(a_d_s_d[y]), getDenominator(a_s_mb[y]), getDenominator(a_d_mb[y]), getDenominator(a_s_msister[y]), getDenominator(a_d_msister[y]),
							getDenominator(a_d_b[y]), getDenominator(a_s_sister[y]), getDenominator(a_d_sister[y]), getDenominator(a_d_fb[y]), getDenominator(a_s_fsister[y]),
							getDenominator(a_d_fsister[y]), getDenominator(a_ul[y]), getDenominator(a_kl[y]), getDenominator(a_k[y]), getDenominator(a_s_ul[y]), getDenominator(a_d_ul[y]),
							getDenominator(a_d_u[y]), getDenominator(a_s_kl[y]), getDenominator(a_d_kl[y]), getDenominator(a_d_k[y]), getDenominator(a_s_k[y]), getDenominator(muslim_trusts[y]),
							// add to calculate for all layers.
							LCDDenominator});

					/*
					arham_awle_to = LCD(new int[]{getDenominator(h[y]), getDenominator(multiply(w[y], w_num[y])), getDenominator(a_f_m[y]), getDenominator(a_f_f_m[y]),
						getDenominator(a_f_m_f[y]), getDenominator(a_f_m_m[y]), getDenominator(a_m_f_m[y]), getDenominator(multiply(a_d_d[y], a_d_d_num[y])),
						getDenominator(multiply(a_s_d[y], a_s_d_num[y])), getDenominator(multiply(a_s_d_s[y], a_s_d_s_num[y])), getDenominator(multiply(a_d_d_s[y], a_d_d_s_num[y])),
						getDenominator(multiply(a_s_d_d[y], a_s_d_d_num[y])), getDenominator(multiply(a_d_d_d[y], a_d_d_d_num[y])), getDenominator(multiply(a_s_s_d[y], a_s_s_d_num[y])),
						getDenominator(multiply(a_d_s_d[y], a_d_s_d_num[y])), getDenominator(multiply(a_s_mb[y], a_s_mb_num[y])), getDenominator(multiply(a_d_mb[y], a_d_mb_num[y])),
						getDenominator(multiply(a_s_msister[y], a_s_msister_num[y])), getDenominator(multiply(a_d_msister[y], a_d_msister_num[y])), getDenominator(multiply(a_d_b[y], a_d_b_num[y])),
						getDenominator(multiply(a_s_sister[y], a_s_sister_num[y])), getDenominator(multiply(a_d_sister[y], a_d_sister_num[y])), getDenominator(multiply(a_d_fb[y], a_d_fb_num[y])),
						getDenominator(multiply(a_s_fsister[y], a_s_fsister_num[y])), getDenominator(multiply(a_d_fsister[y], a_d_fsister_num[y])), getDenominator(multiply(a_ul[y], a_ul_num[y])),
						getDenominator(multiply(a_kl[y], a_kl_num[y])), getDenominator(multiply(a_k[y], a_k_num[y])), getDenominator(multiply(a_s_ul[y], a_s_ul_num[y])),
						getDenominator(multiply(a_d_ul[y], a_d_ul_num[y])), getDenominator(multiply(a_d_u[y], a_d_u_num[y])), getDenominator(multiply(a_s_kl[y], a_s_kl_num[y])),
						getDenominator(multiply(a_d_kl[y], a_d_kl_num[y])), getDenominator(multiply(a_d_k[y], a_d_k_num[y])), getDenominator(multiply(a_s_k[y], a_s_k_num[y])),
						getDenominator(muslim_trusts[y]), arham_awle_to});
						*/
				}
			}

			// Version 1.3, We change the way to calculate all madhab to be separate for all of them
			switch (madaheb)
			{
				case GUMHOUR:
					// Version 2.8, 'white-space:pre' to disable text wrap when <HTML> is used which appears in many (not all) windows and Mac
					resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[88], "", "", "", ""));
					break;
				case MALIKI:
					resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[89]/* Version 1.7 */ + ((arham_p != 0) ? translations[0] : ""), "", "", "", ""));
					break;
				case HANBALI:
					resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[90], "", "", "", ""));
					break;
				case SHAFEE:
					resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[91]/* Version 1.7 */ + ((arham_p != 0) ? translations[0] : ""), "", "", "", ""));
					break;
				case HANAFI:
					resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[92], "", "", "", ""));
					break;
			}
			resultsDataVector.addElement(new ResultsData("", "", "", "", ""));

			// Version 1.7, include the special cases info for all layers instead of the first one.
			for (int y = (allMadaheb ? cnt : 0); y <= cnt; y++)
			{
				// Version 2.0
				if (madaheb == madhabName.MALIKI /* Version 2.1 */ && hamlCaseSelected)
				{
					// Version 1.7
					if (malikiAffectedHaml)
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + translations[221], "", "", "", ""));
						break;
					}
					else
					{
						// Version 2.0, In case of no inheritors except haml
						if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 0 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0
								&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && sister_num[y] == 0 && f_b_num[y] == 0 && f_sister_num[y] == 0 && s_b_num[y] == 0 &&
								s_b_f_num[y] == 0 && u_num[y] == 0 && u_f_num[y] == 0 && s_u_num[y] == 0 && s_u_f_num[y] == 0)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + translations[231], "", "", "", ""));
							break;
						}
					}
				}

				if (khontha_p != 0)// To not enter when monasakha is taken place
				{
					if (y == 0)
					{
						for (int k = 1; k <= 18; k++)
							if (khontha_p == k)
								resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[29] + translations[29 + k], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (y == 1)
					{
						if (khontha_p == 1)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[49], "", "", "", ""));
						if (khontha_p == 2)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[50], "", "", "", ""));
						if (khontha_p == 3)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[51], "", "", "", ""));
						if (khontha_p == 4)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[52], "", "", "", ""));
						if (khontha_p == 5)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[53], "", "", "", ""));
						if (khontha_p == 6)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[54], "", "", "", ""));
						if (khontha_p == 7)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[55], "", "", "", ""));
						if (khontha_p == 8)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[56], "", "", "", ""));
						if (khontha_p == 9)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[57], "", "", "", ""));
						if (khontha_p == 10)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[58], "", "", "", ""));
						// please check the order of 11 12 and the previous. Start
						if (khontha_p == 11)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[59], "", "", "", ""));
						if (khontha_p == 12)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[60], "", "", "", ""));
						// End
						if (khontha_p == 13)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						if (khontha_p == 14)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						if (khontha_p == 15)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						if (khontha_p == 16)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						if (khontha_p == 17)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						if (khontha_p == 18)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[48] + translations[61], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (y == 2)
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[62], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}
				}

				if (hamlCaseSelected && madaheb != madhabName.MALIKI) // Version 1.7, hamlCaseSelected instead of haml_p!=0, To not enter when monasakha is taken place
				{
					if (y == 0)
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[215], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (y == 1 && madaheb != madhabName.SHAFEE)
					{
						for (int k = 1; k <= 12; k++)
						{
							if (haml_p == k)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[63] + translations[63 + k], "", "", "", ""));
								break;
							}
						}

						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (y == 2 && madaheb != madhabName.SHAFEE)
					{
						for (int e = 1; e <= 6; e++)
						{
							if (haml_p == e)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[76] + translations[76 + e], "", "", "", ""));
								break;
							}
						}
						if (haml_p >= 7)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[76] + translations[83], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (madaheb == madhabName.HANBALI && (y == 3 || y == 4 || y == 5))
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[213 + y], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if (madaheb == madhabName.SHAFEE && (y == 1 || y == 2 || y == 3 || y == 4))
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[216 + y], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}

					if ((y == 3 && madaheb != madhabName.HANBALI && madaheb != madhabName.SHAFEE) || (y == 6) /* i.e. for HANBALI */ || (y == 5 && madaheb == madhabName.SHAFEE))
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[84], "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}
				}

				if (missing_p != 0)
				{
					String missingInheritor = null;
					if (missing_p == 1) missingInheritor = translations[93];
					if (missing_p == 2) missingInheritor = translations[94];
					if (missing_p == 3) missingInheritor = translations[95];
					if (missing_p == 4) missingInheritor = translations[97];
					if (missing_p == 5) missingInheritor = translations[98];
					if (missing_p == 6) missingInheritor = translations[99];
					if (missing_p == 7) missingInheritor = translations[100];
					if (missing_p == 8) missingInheritor = translations[101];
					if (missing_p == 9) missingInheritor = translations[102];
					if (missing_p == 10) missingInheritor = translations[96];

					for (int i = 11; i <= 28; i++)
					{
						if (missing_p == i)
						{
							missingInheritor = translations[92 + i];
							break;
						}
					}

					if (y == 0)
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[85] + " (" + missingInheritor + ')', "", "", "", ""));
					else
					{
						if (y == 1)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[86] + " (" + missingInheritor + ')', "", "", "", ""));
						else//i.e. y==3
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[87] + " (" + missingInheritor + ')', "", "", "", ""));
					}
					resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
				}

				// Version 1.7, Changes for Haml case after adding the cases when haml is dead/(2 males)/(2 females)... etc, hamlCaseSelected instead of haml_p!=0
				// All of this condition is just to not display these special comments in the last rotation of special cases e.g. khontha, missing, haml ...
				//if(!((khontha_p!=0 || missing_p!=0) && y==2) && !(hamlCaseSelected && ((y==3 && madaheb!=madhabName.HANBALI)||y==6||(y==5 && madaheb!=madhabName.SHAFEE))) && !bondmanCaseSelected) Version 2.0, Removed and replaced with the below one
				if (!((khontha_p != 0 || missing_p != 0) && y == 2) && !(hamlCaseSelected && ((y == 3 && madaheb != madhabName.HANBALI && madaheb != madhabName.SHAFEE) || y == 6 || (y == 5 && madaheb == madhabName.SHAFEE))) && !bondmanCaseSelected)
				{
					// Version 1.7, This is to handle the base and awle correctly.
					boolean foundAkdareyaSpecialCase = false;
					boolean foundMoshtarakahSpecialCase = false;

					// The results, special cases
					// Version 1.5
					if (!missingCaseSelected && (!hamlCaseSelected /* Version 1.7, when haml is not affecting in maliki madhab -> normal case */ || madaheb == madhabName.MALIKI) && !khonthaCaseSelected && arham_p == 0)
					{
						//Case.1	Al-Omareya
						if (m_num[y] > 0)
						{
							// Version 2.0, [y] instead of [cnt]
							if ((w_num[y] > 0 || h_num[y] > 0) && f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 &&
									s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0 && d_s_s_num[y] == 0 &&
									(b_num[y] + sister_num[y] + m_b_num[y] + m_sister_num[y] + f_b_num[y] + f_sister_num[y] < 2))
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[2], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.2	Al-Masaalah Al-Moshtarakah
						if (madaheb != madhabName.HANAFI && madaheb != madhabName.HANBALI)
						{
							// Version 2.0, [y] instead of [cnt]
							if (h_num[y] == 1 && MawarethSystem.this.add(new String[]{m[y], m_m[y], m_f[y], m_m_m[y], m_m_f[y], m_f_f[y]}).equals("1/6") &&
									(m_sister_num[y] + m_b_num[y]) >= 2 && ((b_num[y] + sister_num[y] > 0) && b_num[y] != 0) && f_num[y] == 0
									&& f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0)
							{
								foundMoshtarakahSpecialCase = true;
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[3], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.3	Al-Masaalah Al-Akdareya
						if (madaheb != madhabName.HANAFI)
						{
							// Version 2.0, [y] instead of [cnt]
							if (h_num[y] == 1 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0 && f_num[y] == 0
									&& f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && ((sister_num[y] == 1 && f_sister_num[y] == 0) || (f_sister_num[y] == 1 &&
									sister_num[y] == 0)) && f_b_num[y] == 0)
							{
								foundAkdareyaSpecialCase = true;
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[4], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.4	Al-Masaalah Al-Denareyah Al-soukrah
						// Version 1.7, Removed: m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 0 && w_num[y] == 3 && m_num[y] == 0 && m_sister_num[y] == 4 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && m_m_num[y] == 1 && m_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0
								&& s_s_s_num[y] == 0 && d_s_s_num[y] == 0 && sister_num[y] == 0 && f_b_num[y] == 0 && f_sister_num[y] == 8 && tarekah == 17)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[5], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						//Case.5	Al-Masaalah Al-Denareyah Al-koubrah
						// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,f_b_num,f_sister_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 0 && w_num[y] == 1 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 12
								&& f_num[y] == 0 && f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 2 && s_s_num[y] == 0 && d_s_num[y] == 0
								&& s_s_s_num[y] == 0 && d_s_s_num[y] == 0 && sister_num[y] == 1 && tarekah == 600)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[6], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						// Version 1.6, This condition is added to not display it in hanafi madhab.
						if (madaheb != madhabName.HANAFI)
						{
							//Case.6	Al-Masaalah Al-kharqaa
							// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
							if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 0 && f_sister_num[y] == 0)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[7], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.7	Al-Masaalah Al-Menbareia
						// Version 1.7, w_num[y]==1 NOT h_num[y]==1, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,f_f_num,m_sister_num,m_b_num,b_num,sister_num,f_b_num,f_sister_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 0 && w_num[y] == 1 && m_num[y] == 1 && f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 2 /*&& s_s_num[y]==0 && s_s_s_num[y]==0 && d_s_s_num[y]==0 && d_s_num[y]==0*/)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[8], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						// Version 1.4, This condition is added to not display it in hanafi madhab.
						if (madaheb != madhabName.HANAFI)
						{
							//Case.8	Al-Masaalah Al-Ashreia
							// Version 1.7, Removed: s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
							if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 0 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0
									&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 1 && f_sister_num[y] == 0)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[9], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}

							//Case.9	Al-Masaalah Al-Ashreneia
							// Version 1.7, Removed: s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
							if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 0 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0
									&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 0 && f_sister_num[y] == 2)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[10], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}

							//Case.10	Mokhtasarat Zayd
							// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
							if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 1 && f_sister_num[y] == 1)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[11], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}

							//Case.11	Teseeneiat Zayd
							// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
							if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 2 && f_sister_num[y] == 1)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[12], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.12	That Al-Forokh
						// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 1 && w_num[y] == 0 && m_num[y] == 1 && /* Version 1.6 */ (m_sister_num[y] + m_b_num[y] == 2) && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && ((sister_num[y] == 1 && f_sister_num[y] == 1) || (sister_num[y] >= 2 && f_sister_num[y] == 0))
								&& f_b_num[y] == 0)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[13], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						//Case.13	Al-mobahalh
						// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 1 && w_num[y] == 0 && m_num[y] == 1 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && sister_num[y] == 1 && f_b_num[y] == 0 && f_sister_num[y] == 0)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[14], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						//Case.14	Al-Yatematan
						// Version 1.7, Removed: s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 1 && w_num[y] == 0 && m_num[y] == 0 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0
								&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && ((sister_num[y] == 1 && /* Version 1.7 */(f_sister_num[y] == 0 || (f_sister_num[y] != 0 && f_b_num[y] != 0))) || (sister_num[y] == 0 && f_sister_num[y] == 1 && f_b_num[y] == 0)))
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[15], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						//Case.15	Am Al-Aramel
						// Version 1.7, Removed: s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
						if (h_num[y] == 0 && w_num[y] == 3 && m_num[y] == 0 && m_sister_num[y] == 4 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && ((m_m_num[y] == 1 && m_f_num[y] == 1 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0)
								|| (m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 1 && m_m_f_num[y] == 1))
								// m_f_f_num can have cases with m_m_f_num. each Madhab has his own calculation in this but removed here for simplicity. this case is happed when two grand*_mother.
								&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && f_b_num[y] == 0 && /* Version 2.0 */((sister_num[y] == 8 && f_sister_num[y] == 0) || (sister_num[y] == 0 && f_sister_num[y] == 8)))
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[16], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}

						//Case.16	Al-Malikia
						if (madaheb == madhabName.MALIKI)
						{
							// Version 2.0, [y] instead of [cnt]
							if (h_num[y] == 1 && w_num[y] == 0 && m_num[y] == 1 && (m_sister_num[y] > 0 || m_b_num[y] > 0) && b_num[y] == 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 0 && f_b_num[y] > 0 && f_sister_num[y] == 0)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[17], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.17	Shebah Al-Malikia
						if (madaheb == madhabName.MALIKI)
						{
							// Version 2.0, [y] instead of [cnt]
							if (h_num[y] == 1 && w_num[y] == 0 && m_num[y] == 1 && (m_sister_num[y] > 0 || m_b_num[y] > 0) && b_num[y] > 0
									&& f_num[y] == 0 && f_f_num[y] == 1 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
									&& d_s_s_num[y] == 0 && sister_num[y] == 0 && f_b_num[y] == 0 && f_sister_num[y] == 0)
							{
								resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[18], "", "", "", ""));
								resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
							}
						}

						//Case.18	No inheritors
						if (h_num[y] == 0 && w_num[y] == 0 && m_num[y] == 0 && m_sister_num[y] == 0 && m_b_num[y] == 0 && b_num[y] == 0
								&& f_num[y] == 0 && f_f_num[y] == 0 && m_m_num[y] == 0 && m_f_num[y] == 0 && m_m_m_num[y] == 0 && m_m_f_num[y] == 0
								&& m_f_f_num[y] == 0 && s_num[y] == 0 && d_num[y] == 0 && s_s_num[y] == 0 && d_s_num[y] == 0 && s_s_s_num[y] == 0
								&& d_s_s_num[y] == 0 && sister_num[y] == 0 && f_b_num[y] == 0 && f_sister_num[y] == 0 && s_b_num[y] == 0 &&
								s_b_f_num[y] == 0 && u_num[y] == 0 && u_f_num[y] == 0 && s_u_num[y] == 0 && s_u_f_num[y] == 0)
						{
							resultsDataVector.addElement(new ResultsData("<html><div style='color:green;white-space:pre;'>" + translations[19], "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}
					}

					// Version 1.7, there is only base in taseeb && !farad cases.
					if (!faradWithTaseeb[y] && !(hamlCaseSelected && base[y] != 0 /* 'Removed: To not display both when they are equal' && LCDDenominator!=base[y]*/)) // Version 2.0, Add '&& !(hamlCaseSelected ...' for the case with f_f && sisters <=5 where base actually is the taseeb but LCDDenominator is for all layers in haml case. It happens when y==0
					{
						resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + ((arham_p != 0) ? ((madaheb == madhabName.GUMHOUR || madaheb == madhabName.HANBALI) ? translations[191] : ((madaheb == madhabName.HANAFI) ? translations[20] : translations[23])) : translations[23]) + LCDDenominator, "", "", "", ""));
						resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
					}
					else
					{
						if (foundAwle[y])
						{
							int awleBase = 0;

							if ((base[y] == 7) /* Version 1.3 */ || (base[y] == 8) || (base[y] == 9) || (base[y] == 10))
								awleBase = 6;
							if ((base[y] == 13) || (base[y] == 15) || (base[y] == 17)) awleBase = 12;
							if ((base[y] == 27)) awleBase = 24;

							// Version 1.7, Akdareya case is hard coded.
							if (foundAkdareyaSpecialCase)
								resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + ((arham_p != 0 && (madaheb == madhabName.GUMHOUR || madaheb == madhabName.HANBALI)) ? translations[191] : translations[23]) + '6' + translations[21] + '9' + translations[22] + LCDDenominator, "", "", "", ""));
							else
								resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + ((arham_p != 0 && (madaheb == madhabName.GUMHOUR || madaheb == madhabName.HANBALI)) ? translations[191] : translations[23]) + awleBase + translations[21] + base[y] + translations[22] + LCDDenominator, "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}
						else
						{
							// Version 1.7, Moshtarakah case is hard coded.
							if (foundMoshtarakahSpecialCase)
								resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + ((arham_p != 0 && (madaheb == madhabName.GUMHOUR || madaheb == madhabName.HANBALI)) ? translations[191] : translations[23]) + '6' + translations[24] + LCDDenominator, "", "", "", ""));
							else
								resultsDataVector.addElement(new ResultsData("<html><div style='color:maroon;white-space:pre;'>" + ((arham_p != 0 && (madaheb == madhabName.GUMHOUR || madaheb == madhabName.HANBALI)) ? translations[191] : translations[23]) + base[y] + translations[24] + LCDDenominator, "", "", "", ""));
							resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
						}
					}
				}

				if (arham_p == 0)
				{
					// Version 1.4, The order of the operations is changed. Dividing is done first then multiplication to avoid OverFlowException.
					h[y] = String.valueOf(getNumerator(h[y]) * (LCDDenominator / getDenominator(h[y]))) + '/' + LCDDenominator;
					w[y] = String.valueOf(getNumerator(w[y]) * (LCDDenominator / getDenominator(w[y]))) + '/' + LCDDenominator;
					f[y] = String.valueOf(getNumerator(f[y]) * (LCDDenominator / getDenominator(f[y]))) + '/' + LCDDenominator;
					f_f[y] = String.valueOf(getNumerator(f_f[y]) * (LCDDenominator / getDenominator(f_f[y]))) + '/' + LCDDenominator;
					m[y] = String.valueOf(getNumerator(m[y]) * (LCDDenominator / getDenominator(m[y]))) + '/' + LCDDenominator;
					m_m[y] = String.valueOf(getNumerator(m_m[y]) * (LCDDenominator / getDenominator(m_m[y]))) + '/' + LCDDenominator;
					m_f[y] = String.valueOf(getNumerator(m_f[y]) * (LCDDenominator / getDenominator(m_f[y]))) + '/' + LCDDenominator;
					m_m_m[y] = String.valueOf(getNumerator(m_m_m[y]) * (LCDDenominator / getDenominator(m_m_m[y]))) + '/' + LCDDenominator;
					m_m_f[y] = String.valueOf(getNumerator(m_m_f[y]) * (LCDDenominator / getDenominator(m_m_f[y]))) + '/' + LCDDenominator;
					m_f_f[y] = String.valueOf(getNumerator(m_f_f[y]) * (LCDDenominator / getDenominator(m_f_f[y]))) + '/' + LCDDenominator;
					s[y] = String.valueOf(getNumerator(s[y]) * (LCDDenominator / getDenominator(s[y]))) + '/' + LCDDenominator;
					d[y] = String.valueOf(getNumerator(d[y]) * (LCDDenominator / getDenominator(d[y]))) + '/' + LCDDenominator;
					s_s[y] = String.valueOf(getNumerator(s_s[y]) * (LCDDenominator / getDenominator(s_s[y]))) + '/' + LCDDenominator;
					d_s[y] = String.valueOf(getNumerator(d_s[y]) * (LCDDenominator / getDenominator(d_s[y]))) + '/' + LCDDenominator;
					s_s_s[y] = String.valueOf(getNumerator(s_s_s[y]) * (LCDDenominator / getDenominator(s_s_s[y]))) + '/' + LCDDenominator;
					d_s_s[y] = String.valueOf(getNumerator(d_s_s[y]) * (LCDDenominator / getDenominator(d_s_s[y]))) + '/' + LCDDenominator;
					b[y] = String.valueOf(getNumerator(b[y]) * (LCDDenominator / getDenominator(b[y]))) + '/' + LCDDenominator;
					sister[y] = String.valueOf(getNumerator(sister[y]) * (LCDDenominator / getDenominator(sister[y]))) + '/' + LCDDenominator;
					m_b[y] = String.valueOf(getNumerator(m_b[y]) * (LCDDenominator / getDenominator(m_b[y]))) + '/' + LCDDenominator;
					m_sister[y] = String.valueOf(getNumerator(m_sister[y]) * (LCDDenominator / getDenominator(m_sister[y]))) + '/' + LCDDenominator;
					f_b[y] = String.valueOf(getNumerator(f_b[y]) * (LCDDenominator / getDenominator(f_b[y]))) + '/' + LCDDenominator;
					f_sister[y] = String.valueOf(getNumerator(f_sister[y]) * (LCDDenominator / getDenominator(f_sister[y]))) + '/' + LCDDenominator;
					s_b[y] = String.valueOf(getNumerator(s_b[y]) * (LCDDenominator / getDenominator(s_b[y]))) + '/' + LCDDenominator;
					s_b_f[y] = String.valueOf(getNumerator(s_b_f[y]) * (LCDDenominator / getDenominator(s_b_f[y]))) + '/' + LCDDenominator;
					u[y] = String.valueOf(getNumerator(u[y]) * (LCDDenominator / getDenominator(u[y]))) + '/' + LCDDenominator;
					u_f[y] = String.valueOf(getNumerator(u_f[y]) * (LCDDenominator / getDenominator(u_f[y]))) + '/' + LCDDenominator;
					s_u[y] = String.valueOf(getNumerator(s_u[y]) * (LCDDenominator / getDenominator(s_u[y]))) + '/' + LCDDenominator;
					s_u_f[y] = String.valueOf(getNumerator(s_u_f[y]) * (LCDDenominator / getDenominator(s_u_f[y]))) + '/' + LCDDenominator;
					khontha[y] = String.valueOf(getNumerator(khontha[y]) * (LCDDenominator / getDenominator(khontha[y]))) + '/' + LCDDenominator;
					missing[y] = String.valueOf(getNumerator(missing[y]) * (LCDDenominator / getDenominator(missing[y]))) + '/' + LCDDenominator;
					bondman[y] = String.valueOf(getNumerator(bondman[y]) * (LCDDenominator / getDenominator(bondman[y]))) + '/' + LCDDenominator;
					haml[y] = String.valueOf(getNumerator(haml[y]) * (LCDDenominator / getDenominator(haml[y]))) + '/' + LCDDenominator;
					muslim_trusts[y] = String.valueOf(getNumerator(muslim_trusts[y]) * (LCDDenominator / getDenominator(muslim_trusts[y]))) + '/' + LCDDenominator;

					// Version 2.4
					w_f = String.valueOf(getNumerator(w_f) * (LCDDenominator / getDenominator(w_f))) + '/' + LCDDenominator;
					w_m = String.valueOf(getNumerator(w_m) * (LCDDenominator / getDenominator(w_m))) + '/' + LCDDenominator;

					if (willsMaleSex)
					{
						// Version 1.6
						w_s_s = String.valueOf(getNumerator(w_s_s) * (LCDDenominator / getDenominator(w_s_s))) + '/' + LCDDenominator;
						w_d_s = String.valueOf(getNumerator(w_d_s) * (LCDDenominator / getDenominator(w_d_s))) + '/' + LCDDenominator;
					}
					else // Version 1.9
					{
						w_s_d = String.valueOf(getNumerator(w_s_d) * (LCDDenominator / getDenominator(w_s_d))) + '/' + LCDDenominator;
						w_d_d = String.valueOf(getNumerator(w_d_d) * (LCDDenominator / getDenominator(w_d_d))) + '/' + LCDDenominator;
					}

					/*
					 * Version 1.4
					 * This to return zero inheritors to "0/1" to not display in the results table.
					 * And we didn't use simplify() function to not remove the common LCDDenominator.
					 * This return back after we removed it from version 1.3 but it causes problems
					 * in displaying results in bondman cases.
					 */
					if (getNumerator(h[y]) == 0) h[y] = "0/1";
					if (getNumerator(w[y]) == 0) w[y] = "0/1";
					if (getNumerator(f[y]) == 0) f[y] = "0/1";
					if (getNumerator(f_f[y]) == 0) f_f[y] = "0/1";
					if (getNumerator(m[y]) == 0) m[y] = "0/1";
					if (getNumerator(m_m[y]) == 0) m_m[y] = "0/1";
					if (getNumerator(m_f[y]) == 0) m_f[y] = "0/1";
					if (getNumerator(m_m_m[y]) == 0) m_m_m[y] = "0/1";
					if (getNumerator(m_m_f[y]) == 0) m_m_f[y] = "0/1";
					if (getNumerator(m_f_f[y]) == 0) m_f_f[y] = "0/1";
					if (getNumerator(s[y]) == 0) s[y] = "0/1";
					if (getNumerator(d[y]) == 0) d[y] = "0/1";
					if (getNumerator(s_s[y]) == 0) s_s[y] = "0/1";
					if (getNumerator(d_s[y]) == 0) d_s[y] = "0/1";
					if (getNumerator(s_s_s[y]) == 0) s_s_s[y] = "0/1";
					if (getNumerator(d_s_s[y]) == 0) d_s_s[y] = "0/1";
					if (getNumerator(b[y]) == 0) b[y] = "0/1";
					if (getNumerator(sister[y]) == 0) sister[y] = "0/1";
					if (getNumerator(m_b[y]) == 0) m_b[y] = "0/1";
					if (getNumerator(m_sister[y]) == 0) m_sister[y] = "0/1";
					if (getNumerator(f_b[y]) == 0) f_b[y] = "0/1";
					if (getNumerator(f_sister[y]) == 0) f_sister[y] = "0/1";
					if (getNumerator(s_b[y]) == 0) s_b[y] = "0/1";
					if (getNumerator(s_b_f[y]) == 0) s_b_f[y] = "0/1";
					if (getNumerator(u[y]) == 0) u[y] = "0/1";
					if (getNumerator(u_f[y]) == 0) u_f[y] = "0/1";
					if (getNumerator(s_u[y]) == 0) s_u[y] = "0/1";
					if (getNumerator(s_u_f[y]) == 0) s_u_f[y] = "0/1";
					if (getNumerator(khontha[y]) == 0) khontha[y] = "0/1";
					if (getNumerator(missing[y]) == 0) missing[y] = "0/1";
					if (getNumerator(bondman[y]) == 0) bondman[y] = "0/1";
					if (getNumerator(haml[y]) == 0) haml[y] = "0/1";
					if (getNumerator(muslim_trusts[y]) == 0) muslim_trusts[y] = "0/1";

					// Version 2.4
					if (getNumerator(w_f) == 0) w_f = "0/1";
					if (getNumerator(w_m) == 0) w_m = "0/1";

					if (willsMaleSex)
					{
						// Version 1.6
						if (getNumerator(w_s_s) == 0) w_s_s = "0/1";
						if (getNumerator(w_d_s) == 0) w_d_s = "0/1";
					}
					else // Version 1.9
					{
						if (getNumerator(w_s_d) == 0) w_s_d = "0/1";
						if (getNumerator(w_d_d) == 0) w_d_d = "0/1";
					}

					if (bondmanCaseSelected)
					{
						if (y != 0)
						{
							if (h_num[0] != 0) h[0] = (getNumerator(h[0]) + getNumerator(h[y])) + "/1";
							if (w_num[0] != 0) w[0] = (getNumerator(w[0]) + getNumerator(w[y])) + "/1";
							if (f_num[0] != 0) f[0] = (getNumerator(f[0]) + getNumerator(f[y])) + "/1";
							if (f_f_num[0] != 0)
								f_f[0] = (getNumerator(f_f[0]) + getNumerator(f_f[y])) + "/1";
							if (m_num[0] != 0) m[0] = (getNumerator(m[0]) + getNumerator(m[y])) + "/1";
							if (m_m_num[0] != 0)
								m_m[0] = (getNumerator(m_m[0]) + getNumerator(m_m[y])) + "/1";
							if (m_f_num[0] != 0)
								m_f[0] = (getNumerator(m_f[0]) + getNumerator(m_f[y])) + "/1";
							if (m_m_m_num[0] != 0)
								m_m_m[0] = (getNumerator(m_m_m[0]) + getNumerator(m_m_m[y])) + "/1";
							if (m_m_f_num[0] != 0)
								m_m_f[0] = (getNumerator(m_m_f[0]) + getNumerator(m_m_f[y])) + "/1";
							if (m_f_f_num[0] != 0)
								m_f_f[0] = (getNumerator(m_f_f[0]) + getNumerator(m_f_f[y])) + "/1";
							if (s_num[0] != 0) s[0] = (getNumerator(s[0]) + getNumerator(s[y])) + "/1";
							if (d_num[0] != 0) d[0] = (getNumerator(d[0]) + getNumerator(d[y])) + "/1";
							if (s_s_num[0] != 0)
								s_s[0] = (getNumerator(s_s[0]) + getNumerator(s_s[y])) + "/1";
							if (d_s_num[0] != 0)
								d_s[0] = (getNumerator(d_s[0]) + getNumerator(d_s[y])) + "/1";
							if (s_s_s_num[0] != 0)
								s_s_s[0] = (getNumerator(s_s_s[0]) + getNumerator(s_s_s[y])) + "/1";
							if (d_s_s_num[0] != 0)
								d_s_s[0] = (getNumerator(d_s_s[0]) + getNumerator(d_s_s[y])) + "/1";
							if (b_num[0] != 0) b[0] = (getNumerator(b[0]) + getNumerator(b[y])) + "/1";
							if (sister_num[0] != 0)
								sister[0] = (getNumerator(sister[0]) + getNumerator(sister[y])) + "/1";
							if (m_b_num[0] != 0)
								m_b[0] = (getNumerator(m_b[0]) + getNumerator(m_b[y])) + "/1";
							if (m_sister_num[0] != 0)
								m_sister[0] = (getNumerator(m_sister[0]) + getNumerator(m_sister[y])) + "/1";
							if (f_b_num[0] != 0)
								f_b[0] = (getNumerator(f_b[0]) + getNumerator(f_b[y])) + "/1";
							if (f_sister_num[0] != 0)
								f_sister[0] = (getNumerator(f_sister[0]) + getNumerator(f_sister[y])) + "/1";
							if (s_b_num[0] != 0)
								s_b[0] = (getNumerator(s_b[0]) + getNumerator(s_b[y])) + "/1";
							if (s_b_f_num[0] != 0)
								s_b_f[0] = (getNumerator(s_b_f[0]) + getNumerator(s_b_f[y])) + "/1";
							if (u_num[0] != 0) u[0] = (getNumerator(u[0]) + getNumerator(u[y])) + "/1";
							if (u_f_num[0] != 0)
								u_f[0] = (getNumerator(u_f[0]) + getNumerator(u_f[y])) + "/1";
							if (s_u_num[0] != 0)
								s_u[0] = (getNumerator(s_u[0]) + getNumerator(s_u[y])) + "/1";
							if (s_u_f_num[0] != 0)
								s_u_f[0] = (getNumerator(s_u_f[0]) + getNumerator(s_u_f[y])) + "/1";
							if (getNumerator(bondman[0]) != 0)
								bondman[0] = (getNumerator(bondman[0]) + getNumerator(bondman[y])) + "/1";

							// Version 1.7, To handle the case when no inheritor except the bondman
							muslim_trusts[0] = (getNumerator(muslim_trusts[0]) + getNumerator(muslim_trusts[y])) + "/1";
						}

						if (y != cnt) continue;
						else
						{
							LCDDenominator = (cnt + 1) * LCDDenominator;

							if (h_num[0] != 0)
								h[0] = String.valueOf(getNumerator(h[0])) + '/' + LCDDenominator;
							if (w_num[0] != 0)
								w[0] = String.valueOf(getNumerator(w[0])) + '/' + LCDDenominator;
							if (f_num[0] != 0)
								f[0] = String.valueOf(getNumerator(f[0])) + '/' + LCDDenominator;
							if (f_f_num[0] != 0)
								f_f[0] = String.valueOf(getNumerator(f_f[0])) + '/' + LCDDenominator;
							if (m_num[0] != 0)
								m[0] = String.valueOf(getNumerator(m[0])) + '/' + LCDDenominator;
							if (m_m_num[0] != 0)
								m_m[0] = String.valueOf(getNumerator(m_m[0])) + '/' + LCDDenominator;
							if (m_f_num[0] != 0)
								m_f[0] = String.valueOf(getNumerator(m_f[0])) + '/' + LCDDenominator;
							if (m_m_m_num[0] != 0)
								m_m_m[0] = String.valueOf(getNumerator(m_m_m[0])) + '/' + LCDDenominator;
							if (m_m_f_num[0] != 0)
								m_m_f[0] = String.valueOf(getNumerator(m_m_f[0])) + '/' + LCDDenominator;
							if (m_f_f_num[0] != 0)
								m_f_f[0] = String.valueOf(getNumerator(m_f_f[0])) + '/' + LCDDenominator;
							if (s_num[0] != 0)
								s[0] = String.valueOf(getNumerator(s[0])) + '/' + LCDDenominator;
							if (d_num[0] != 0)
								d[0] = String.valueOf(getNumerator(d[0])) + '/' + LCDDenominator;
							if (s_s_num[0] != 0)
								s_s[0] = String.valueOf(getNumerator(s_s[0])) + '/' + LCDDenominator;
							if (d_s_num[0] != 0)
								d_s[0] = String.valueOf(getNumerator(d_s[0])) + '/' + LCDDenominator;
							if (s_s_s_num[0] != 0)
								s_s_s[0] = String.valueOf(getNumerator(s_s_s[0])) + '/' + LCDDenominator;
							if (d_s_s_num[0] != 0)
								d_s_s[0] = String.valueOf(getNumerator(d_s_s[0])) + '/' + LCDDenominator;
							if (b_num[0] != 0)
								b[0] = String.valueOf(getNumerator(b[0])) + '/' + LCDDenominator;
							if (sister_num[0] != 0)
								sister[0] = String.valueOf(getNumerator(sister[0])) + '/' + LCDDenominator;
							if (m_b_num[0] != 0)
								m_b[0] = String.valueOf(getNumerator(m_b[0])) + '/' + LCDDenominator;
							if (m_sister_num[0] != 0)
								m_sister[0] = String.valueOf(getNumerator(m_sister[0])) + '/' + LCDDenominator;
							if (f_b_num[0] != 0)
								f_b[0] = String.valueOf(getNumerator(f_b[0])) + '/' + LCDDenominator;
							if (f_sister_num[0] != 0)
								f_sister[0] = String.valueOf(getNumerator(f_sister[0])) + '/' + LCDDenominator;
							if (s_b_num[0] != 0)
								s_b[0] = String.valueOf(getNumerator(s_b[0])) + '/' + LCDDenominator;
							if (s_b_f_num[0] != 0)
								s_b_f[0] = String.valueOf(getNumerator(s_b_f[0])) + '/' + LCDDenominator;
							if (u_num[0] != 0)
								u[0] = String.valueOf(getNumerator(u[0])) + '/' + LCDDenominator;
							if (u_f_num[0] != 0)
								u_f[0] = String.valueOf(getNumerator(u_f[0])) + '/' + LCDDenominator;
							if (s_u_num[0] != 0)
								s_u[0] = String.valueOf(getNumerator(s_u[0])) + '/' + LCDDenominator;
							if (s_u_f_num[0] != 0)
								s_u_f[0] = String.valueOf(getNumerator(s_u_f[0])) + '/' + LCDDenominator;
							if (getNumerator(bondman[0]) != 0)
								bondman[0] = String.valueOf(getNumerator(bondman[0])) + '/' + LCDDenominator;

							// Version 1.7, To handle the case when no inheritor except the bondman
							if (getNumerator(muslim_trusts[0]) != 0)
								muslim_trusts[0] = String.valueOf(getNumerator(muslim_trusts[0])) + '/' + LCDDenominator;
						}
					}

					// Mowqof for missing, khontha and haml cases
					if (khontha_w == 2 || missing_p != 0 /* Version 1.7, Remove || haml_p!=0 */) //(khontha_w !=0 && khontha_w != 1)
					{
						// Take the difference for all
						mowqof[0] = String.valueOf(Math.abs(getNumerator(h[0]) - getNumerator(h[1]))) + '/' + LCDDenominator;
						mowqof[1] = String.valueOf(Math.abs(getNumerator(w[0]) - getNumerator(w[1]))) + '/' + LCDDenominator;
						mowqof[2] = String.valueOf(Math.abs(getNumerator(f[0]) - getNumerator(f[1]))) + '/' + LCDDenominator;
						mowqof[3] = String.valueOf(Math.abs(getNumerator(f_f[0]) - getNumerator(f_f[1]))) + '/' + LCDDenominator;
						mowqof[4] = String.valueOf(Math.abs(getNumerator(m[0]) - getNumerator(m[1]))) + '/' + LCDDenominator;
						mowqof[5] = String.valueOf(Math.abs(getNumerator(m_m[0]) - getNumerator(m_m[1]))) + '/' + LCDDenominator;
						mowqof[6] = String.valueOf(Math.abs(getNumerator(m_f[0]) - getNumerator(m_f[1]))) + '/' + LCDDenominator;
						mowqof[7] = String.valueOf(Math.abs(getNumerator(m_m_m[0]) - getNumerator(m_m_m[1]))) + '/' + LCDDenominator;
						mowqof[8] = String.valueOf(Math.abs(getNumerator(m_m_f[0]) - getNumerator(m_m_f[1]))) + '/' + LCDDenominator;
						mowqof[9] = String.valueOf(Math.abs(getNumerator(m_f_f[0]) - getNumerator(m_f_f[1]))) + '/' + LCDDenominator;
						mowqof[10] = String.valueOf(Math.abs(getNumerator(s[0]) - getNumerator(s[1]))) + '/' + LCDDenominator;
						mowqof[11] = String.valueOf(Math.abs(getNumerator(d[0]) - getNumerator(d[1]))) + '/' + LCDDenominator;
						mowqof[12] = String.valueOf(Math.abs(getNumerator(s_s[0]) - getNumerator(s_s[1]))) + '/' + LCDDenominator;
						mowqof[13] = String.valueOf(Math.abs(getNumerator(d_s[0]) - getNumerator(d_s[1]))) + '/' + LCDDenominator;
						mowqof[14] = String.valueOf(Math.abs(getNumerator(s_s_s[0]) - getNumerator(s_s_s[1]))) + '/' + LCDDenominator;
						mowqof[15] = String.valueOf(Math.abs(getNumerator(d_s_s[0]) - getNumerator(d_s_s[1]))) + '/' + LCDDenominator;
						mowqof[16] = String.valueOf(Math.abs(getNumerator(b[0]) - getNumerator(b[1]))) + '/' + LCDDenominator;
						mowqof[17] = String.valueOf(Math.abs(getNumerator(sister[0]) - getNumerator(sister[1]))) + '/' + LCDDenominator;
						mowqof[18] = String.valueOf(Math.abs(getNumerator(m_b[0]) - getNumerator(m_b[1]))) + '/' + LCDDenominator;
						mowqof[19] = String.valueOf(Math.abs(getNumerator(m_sister[0]) - getNumerator(m_sister[1]))) + '/' + LCDDenominator;
						mowqof[20] = String.valueOf(Math.abs(getNumerator(f_b[0]) - getNumerator(f_b[1]))) + '/' + LCDDenominator;
						mowqof[21] = String.valueOf(Math.abs(getNumerator(f_sister[0]) - getNumerator(f_sister[1]))) + '/' + LCDDenominator;
						mowqof[22] = String.valueOf(Math.abs(getNumerator(s_b[0]) - getNumerator(s_b[1]))) + '/' + LCDDenominator;
						mowqof[23] = String.valueOf(Math.abs(getNumerator(s_b_f[0]) - getNumerator(s_b_f[1]))) + '/' + LCDDenominator;
						mowqof[24] = String.valueOf(Math.abs(getNumerator(u[0]) - getNumerator(u[1]))) + '/' + LCDDenominator;
						mowqof[25] = String.valueOf(Math.abs(getNumerator(u_f[0]) - getNumerator(u_f[1]))) + '/' + LCDDenominator;
						mowqof[26] = String.valueOf(Math.abs(getNumerator(s_u[0]) - getNumerator(s_u[1]))) + '/' + LCDDenominator;
						mowqof[27] = String.valueOf(Math.abs(getNumerator(s_u_f[0]) - getNumerator(s_u_f[1]))) + '/' + LCDDenominator;
						mowqof[28] = String.valueOf(Math.abs(getNumerator(khontha[0]) - getNumerator(khontha[1]))) + '/' + LCDDenominator;
						mowqof[29] = String.valueOf(Math.abs(getNumerator(missing[0]) - getNumerator(missing[1]))) + '/' + LCDDenominator;
						//mowqof[30]	=String.valueOf(Math.abs(getNumerator(haml[0])-getNumerator(haml[1])))+"/"+LCDDenominator;

						for (int i = 0; i < 31; i++)
							if (getNumerator(mowqof[i]) == 0) mowqof[i] = "0/1";
					}
					//else
					//resultsDataVector.addElement(new ResultsData (translations[27], "", "", "", ""));

					// Version 1.7, hamlCaseSelected instead of haml_p!=0
					if (hamlCaseSelected)
					{
						int[] maxDiff = new int[31];
						for (int k = 0; k < cnt; k++)
						{
							if (maxDiff[0] < (getNumerator(h[k]) - getNumerator(h[cnt])))
								maxDiff[0] = getNumerator(h[k]) - getNumerator(h[cnt]);
							if (maxDiff[1] < (getNumerator(w[k]) - getNumerator(w[cnt])))
								maxDiff[1] = getNumerator(w[k]) - getNumerator(w[cnt]);
							if (maxDiff[2] < (getNumerator(f[k]) - getNumerator(f[cnt])))
								maxDiff[2] = getNumerator(f[k]) - getNumerator(f[cnt]);
							if (maxDiff[3] < (getNumerator(f_f[k]) - getNumerator(f_f[cnt])))
								maxDiff[3] = getNumerator(f_f[k]) - getNumerator(f_f[cnt]);
							if (maxDiff[4] < (getNumerator(m[k]) - getNumerator(m[cnt])))
								maxDiff[4] = getNumerator(m[k]) - getNumerator(m[cnt]);
							if (maxDiff[5] < (getNumerator(m_m[k]) - getNumerator(m_m[cnt])))
								maxDiff[5] = getNumerator(m_m[k]) - getNumerator(m_m[cnt]);
							if (maxDiff[6] < (getNumerator(m_f[k]) - getNumerator(m_f[cnt])))
								maxDiff[6] = getNumerator(m_f[k]) - getNumerator(m_f[cnt]); // Version 2.0, Change [cnt] to [6]  !!!
							if (maxDiff[7] < (getNumerator(m_m_m[k]) - getNumerator(m_m_m[cnt])))
								maxDiff[7] = getNumerator(m_m_m[k]) - getNumerator(m_m_m[cnt]);
							if (maxDiff[8] < (getNumerator(m_m_f[k]) - getNumerator(m_m_f[cnt])))
								maxDiff[8] = getNumerator(m_m_f[k]) - getNumerator(m_m_f[cnt]);
							if (maxDiff[9] < (getNumerator(m_f_f[k]) - getNumerator(m_f_f[cnt])))
								maxDiff[9] = getNumerator(m_f_f[k]) - getNumerator(m_f_f[cnt]);
							if (maxDiff[10] < (getNumerator(s[k]) - getNumerator(s[cnt])))
								maxDiff[10] = getNumerator(s[k]) - getNumerator(s[cnt]);
							if (maxDiff[11] < (getNumerator(d[k]) - getNumerator(d[cnt])))
								maxDiff[11] = getNumerator(d[k]) - getNumerator(d[cnt]);
							if (maxDiff[12] < (getNumerator(s_s[k]) - getNumerator(s_s[cnt])))
								maxDiff[12] = getNumerator(s_s[k]) - getNumerator(s_s[cnt]);
							if (maxDiff[13] < (getNumerator(d_s[k]) - getNumerator(d_s[cnt])))
								maxDiff[13] = getNumerator(d_s[k]) - getNumerator(d_s[cnt]);
							if (maxDiff[14] < (getNumerator(s_s_s[k]) - getNumerator(s_s_s[cnt])))
								maxDiff[14] = getNumerator(s_s_s[k]) - getNumerator(s_s_s[cnt]);
							if (maxDiff[15] < (getNumerator(d_s_s[k]) - getNumerator(d_s_s[cnt])))
								maxDiff[15] = getNumerator(d_s_s[k]) - getNumerator(d_s_s[cnt]);
							if (maxDiff[16] < (getNumerator(b[k]) - getNumerator(b[cnt])))
								maxDiff[16] = getNumerator(b[k]) - getNumerator(b[cnt]);
							if (maxDiff[17] < (getNumerator(sister[k]) - getNumerator(sister[cnt])))
								maxDiff[17] = getNumerator(sister[k]) - getNumerator(sister[cnt]);
							if (maxDiff[18] < (getNumerator(m_b[k]) - getNumerator(m_b[cnt])))
								maxDiff[18] = getNumerator(m_b[k]) - getNumerator(m_b[cnt]);
							if (maxDiff[19] < (getNumerator(m_sister[k]) - getNumerator(m_sister[cnt])))
								maxDiff[19] = getNumerator(m_sister[k]) - getNumerator(m_sister[cnt]);
							if (maxDiff[20] < (getNumerator(f_b[k]) - getNumerator(f_b[cnt])))
								maxDiff[20] = getNumerator(f_b[k]) - getNumerator(f_b[cnt]);
							if (maxDiff[21] < (getNumerator(f_sister[k]) - getNumerator(f_sister[cnt])))
								maxDiff[21] = getNumerator(f_sister[k]) - getNumerator(f_sister[cnt]);
							if (maxDiff[22] < (getNumerator(s_b[k]) - getNumerator(s_b[cnt])))
								maxDiff[22] = getNumerator(s_b[k]) - getNumerator(s_b[cnt]);
							if (maxDiff[23] < (getNumerator(s_b_f[k]) - getNumerator(s_b_f[cnt])))
								maxDiff[23] = getNumerator(s_b_f[k]) - getNumerator(s_b_f[cnt]);
							if (maxDiff[24] < (getNumerator(u[k]) - getNumerator(u[cnt])))
								maxDiff[24] = getNumerator(u[k]) - getNumerator(u[cnt]);
							if (maxDiff[25] < (getNumerator(u_f[k]) - getNumerator(u_f[cnt])))
								maxDiff[25] = getNumerator(u_f[k]) - getNumerator(u_f[cnt]);
							if (maxDiff[26] < (getNumerator(s_u[k]) - getNumerator(s_u[cnt])))
								maxDiff[26] = getNumerator(s_u[k]) - getNumerator(s_u[cnt]);
							if (maxDiff[27] < (getNumerator(s_u_f[k]) - getNumerator(s_u_f[cnt])))
								maxDiff[27] = getNumerator(s_u_f[k]) - getNumerator(s_u_f[cnt]);
							if (maxDiff[30] < getNumerator(haml[k])) maxDiff[30] = getNumerator(haml[k]);
						}

						for (int i = 0; i < 31; i++)
						{
							mowqof[i] = String.valueOf(maxDiff[i]) + '/' + LCDDenominator;
							if (maxDiff[i] == 0) mowqof[i] = "0/1";
						}
					}

					/*
					 * Version 1.5
					 * Moved to here to avoid displaying the inheritors with 0 balance in case of bondmanCaseSelected.
					 * to make sure that it will be affected by:
					 * if(getNumerator(x[y])==0)
					 *
					 * TO DO: Remove the conditions:
					 * if(getNumerator(x[y])==0) x[y]="0/1";
					 *
					 * And replace the following:
					 * if(!x[y].equals("0/1") || (y==2 && getNumerator...
					 *
					 * with:
					 * if(getNumerator(x[y])!=0 || (y==2 && getNumerator... after deeply checking.
					 */
					if (bondmanCaseSelected) y = 0;

					/*
					 * This to return zero inheritors to "0/1" to not display in the results table.
					 * And we didn't use simplify() function to not remove the common LCDDenominator.
					 */
					if (getNumerator(h[y]) == 0) h[y] = "0/1";
					if (getNumerator(w[y]) == 0) w[y] = "0/1";
					if (getNumerator(f[y]) == 0) f[y] = "0/1";
					if (getNumerator(f_f[y]) == 0) f_f[y] = "0/1";
					if (getNumerator(m[y]) == 0) m[y] = "0/1";
					if (getNumerator(m_m[y]) == 0) m_m[y] = "0/1";
					if (getNumerator(m_f[y]) == 0) m_f[y] = "0/1";
					if (getNumerator(m_m_m[y]) == 0) m_m_m[y] = "0/1";
					if (getNumerator(m_m_f[y]) == 0) m_m_f[y] = "0/1";
					if (getNumerator(m_f_f[y]) == 0) m_f_f[y] = "0/1";
					if (getNumerator(s[y]) == 0) s[y] = "0/1";
					if (getNumerator(d[y]) == 0) d[y] = "0/1";
					if (getNumerator(s_s[y]) == 0) s_s[y] = "0/1";
					if (getNumerator(d_s[y]) == 0) d_s[y] = "0/1";
					if (getNumerator(s_s_s[y]) == 0) s_s_s[y] = "0/1";
					if (getNumerator(d_s_s[y]) == 0) d_s_s[y] = "0/1";
					if (getNumerator(b[y]) == 0) b[y] = "0/1";
					if (getNumerator(sister[y]) == 0) sister[y] = "0/1";
					if (getNumerator(m_b[y]) == 0) m_b[y] = "0/1";
					if (getNumerator(m_sister[y]) == 0) m_sister[y] = "0/1";
					if (getNumerator(f_b[y]) == 0) f_b[y] = "0/1";
					if (getNumerator(f_sister[y]) == 0) f_sister[y] = "0/1";
					if (getNumerator(s_b[y]) == 0) s_b[y] = "0/1";
					if (getNumerator(s_b_f[y]) == 0) s_b_f[y] = "0/1";
					if (getNumerator(u[y]) == 0) u[y] = "0/1";
					if (getNumerator(u_f[y]) == 0) u_f[y] = "0/1";
					if (getNumerator(s_u[y]) == 0) s_u[y] = "0/1";
					if (getNumerator(s_u_f[y]) == 0) s_u_f[y] = "0/1";
					if (getNumerator(khontha[y]) == 0) khontha[y] = "0/1";
					if (getNumerator(missing[y]) == 0) missing[y] = "0/1";
					if (getNumerator(bondman[y]) == 0) bondman[y] = "0/1";
					if (getNumerator(haml[y]) == 0) haml[y] = "0/1";
					if (getNumerator(muslim_trusts[y]) == 0) muslim_trusts[y] = "0/1";

					// Version 2.4
					if (getNumerator(w_f) == 0) w_f = "0/1";
					if (getNumerator(w_m) == 0) w_m = "0/1";

					if (willsMaleSex)
					{
						// Version 1.6
						if (getNumerator(w_s_s) == 0) w_s_s = "0/1";
						if (getNumerator(w_d_s) == 0) w_d_s = "0/1";
					}
					else // Version 1.9
					{
						if (getNumerator(w_s_d) == 0) w_s_d = "0/1";
						if (getNumerator(w_d_d) == 0) w_d_d = "0/1";
					}

					/*
					 * Version 1.1
					 * For each condition here there are some improvements such as:
					 *
					 * 1. Instead of displaying the whole tarekah we round it to 3 precision using round() function.
					 * 2. Not displaying moqof if it is zero using y==2?(mowqof[0]=="0/1"?"":mowqof[0])):""
					 *
					 * Version 1.3
					 *
					 * Add  || (y==2 && getNumerator(mowqof[0])!=0 && h[y].equals("0/1")) to display the inheritors with 0 balance but with moqof
					 * and (h[y].equals("0/1")==true)?("0/"+String.valueOf(LCDDenominator)):h[y] instead of h[y]
					 */
					//if(bondmanCaseSelected)y=0;

					// Version 1.3
					for (int i = noteVector.size(); i < resultsDataVector.size(); i++)
						noteVector.add("");

					if (!h[y].equals("0/1") || (y == cnt && getNumerator(mowqof[0]) != 0 && h[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[93], h[y].equals("0/1") ? ("0/" + LCDDenominator) : h[y], String.valueOf(h_num[y]), round(tarekahMultiply(h[y], tarekah)), y == cnt ? (mowqof[0].equals("0/1") ? "" : mowqof[0]) : ""));
						noteVector.add(h_note[y]);
					}

					if (!w[y].equals("0/1") || (y == cnt && getNumerator(mowqof[1]) != 0 && w[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[94], w[y].equals("0/1") ? ("0/" + LCDDenominator) : w[y], String.valueOf(w_num[y]), round(tarekahMultiply(w[y], tarekah)), y == cnt ? (mowqof[1].equals("0/1") ? "" : mowqof[1]) : ""));
						noteVector.add(w_note[y]);
					}

					if (!f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[2]) != 0 && f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[95], f[y].equals("0/1") ? ("0/" + LCDDenominator) : f[y], String.valueOf(f_num[y]), round(tarekahMultiply(f[y], tarekah)), y == cnt ? (mowqof[2].equals("0/1") ? "" : mowqof[2]) : ""));
						noteVector.add(f_note[y]);
					}

					if (!f_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[3]) != 0 && f_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[96], f_f[y].equals("0/1") ? ("0/" + LCDDenominator) : f_f[y], String.valueOf(f_f_num[y]), round(tarekahMultiply(f_f[y], tarekah)), y == cnt ? (mowqof[3].equals("0/1") ? "" : mowqof[3]) : ""));
						noteVector.add(f_f_note[y]);
					}
					if (!m[y].equals("0/1") || (y == cnt && getNumerator(mowqof[4]) != 0 && m[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[97], m[y].equals("0/1") ? ("0/" + LCDDenominator) : m[y], String.valueOf(m_num[y]), round(tarekahMultiply(m[y], tarekah)), y == cnt ? (mowqof[4].equals("0/1") ? "" : mowqof[4]) : ""));
						noteVector.add(m_note[y]);
					}

					if (!m_m[y].equals("0/1") || (y == cnt && getNumerator(mowqof[5]) != 0 && m_m[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[98], m_m[y].equals("0/1") ? ("0/" + LCDDenominator) : m_m[y], String.valueOf(m_m_num[y]), round(tarekahMultiply(m_m[y], tarekah)), y == cnt ? (mowqof[5].equals("0/1") ? "" : mowqof[5]) : ""));
						noteVector.add(m_m_note[y]);
					}

					if (!m_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[6]) != 0 && m_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[99], m_f[y].equals("0/1") ? ("0/" + LCDDenominator) : m_f[y], String.valueOf(m_f_num[y]), round(tarekahMultiply(m_f[y], tarekah)), y == cnt ? (mowqof[6].equals("0/1") ? "" : mowqof[6]) : ""));
						noteVector.add(m_f_note[y]);
					}

					if (!m_m_m[y].equals("0/1") || (y == cnt && getNumerator(mowqof[7]) != 0 && m_m_m[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[100], m_m_m[y].equals("0/1") ? ("0/" + LCDDenominator) : m_m_m[y], String.valueOf(m_m_m_num[y]), round(tarekahMultiply(m_m_m[y], tarekah)), y == cnt ? (mowqof[7].equals("0/1") ? "" : mowqof[7]) : ""));
						noteVector.add(m_m_m_note[y]);
					}

					if (!m_m_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[8]) != 0 && m_m_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[101], m_m_f[y].equals("0/1") ? ("0/" + LCDDenominator) : m_m_f[y], String.valueOf(m_m_f_num[y]), round(tarekahMultiply(m_m_f[y], tarekah)), y == cnt ? (mowqof[8].equals("0/1") ? "" : mowqof[8]) : ""));
						noteVector.add(m_m_f_note[y]);
					}

					if (!m_f_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[9]) != 0 && m_f_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[102], m_f_f[y].equals("0/1") ? ("0/" + LCDDenominator) : m_f_f[y], String.valueOf(m_f_f_num[y]), round(tarekahMultiply(m_f_f[y], tarekah)), y == cnt ? (mowqof[9].equals("0/1") ? "" : mowqof[9]) : ""));
						noteVector.add(m_f_f_note[y]);
					}

					if (!s[y].equals("0/1") || (y == cnt && getNumerator(mowqof[10]) != 0 && s[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[103], s[y].equals("0/1") ? ("0/" + LCDDenominator) : s[y], String.valueOf(s_num[y]), round(tarekahMultiply(s[y], tarekah)), y == cnt ? (mowqof[10].equals("0/1") ? "" : mowqof[10]) : ""));
						noteVector.add(s_note[y]);
					}

					if ((!d[y].equals("0/1") /* Version 1.9, Removed '&& /* Version 1.6 * / !(willsCaseSelected && d_num[y]==0)'*/) || (y == cnt && getNumerator(mowqof[11]) != 0 && d[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[104], d[y].equals("0/1") ? ("0/" + LCDDenominator) : d[y], String.valueOf(d_num[y]), round(tarekahMultiply(d[y], tarekah)), y == cnt ? (mowqof[11].equals("0/1") ? "" : mowqof[11]) : ""));
						noteVector.add(d_note[y]);
					}

					if (!s_s[y].equals("0/1") || (y == cnt && getNumerator(mowqof[12]) != 0 && s_s[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[105], s_s[y].equals("0/1") ? ("0/" + LCDDenominator) : s_s[y], String.valueOf(s_s_num[y]), round(tarekahMultiply(s_s[y], tarekah)), y == cnt ? (mowqof[12].equals("0/1") ? "" : mowqof[12]) : ""));
						noteVector.add(s_s_note[y]);
					}

					if (!d_s[y].equals("0/1") || (y == cnt && getNumerator(mowqof[13]) != 0 && d_s[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[106], d_s[y].equals("0/1") ? ("0/" + LCDDenominator) : d_s[y], String.valueOf(d_s_num[y]), round(tarekahMultiply(d_s[y], tarekah)), y == cnt ? (mowqof[13].equals("0/1") ? "" : mowqof[13]) : ""));
						noteVector.add(d_s_note[y]);
					}

					if (!s_s_s[y].equals("0/1") || (y == cnt && getNumerator(mowqof[14]) != 0 && s_s_s[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[107], s_s_s[y].equals("0/1") ? ("0/" + LCDDenominator) : s_s_s[y], String.valueOf(s_s_s_num[y]), round(tarekahMultiply(s_s_s[y], tarekah)), y == cnt ? (mowqof[14].equals("0/1") ? "" : mowqof[14]) : ""));
						noteVector.add(s_s_s_note[y]);
					}

					if (!d_s_s[y].equals("0/1") || (y == cnt && getNumerator(mowqof[15]) != 0 && d_s_s[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[108], d_s_s[y].equals("0/1") ? ("0/" + LCDDenominator) : d_s_s[y], String.valueOf(d_s_s_num[y]), round(tarekahMultiply(d_s_s[y], tarekah)), y == cnt ? (mowqof[15].equals("0/1") ? "" : mowqof[15]) : ""));
						noteVector.add(d_s_s_note[y]);
					}

					if (!b[y].equals("0/1") || (y == cnt && getNumerator(mowqof[16]) != 0 && b[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[109], b[y].equals("0/1") ? ("0/" + LCDDenominator) : b[y], String.valueOf(b_num[y]), round(tarekahMultiply(b[y], tarekah)), y == cnt ? (mowqof[16].equals("0/1") ? "" : mowqof[16]) : ""));
						noteVector.add(b_note[y]);
					}

					if (!sister[y].equals("0/1") || (y == cnt && getNumerator(mowqof[17]) != 0 && sister[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[110], sister[y].equals("0/1") ? ("0/" + LCDDenominator) : sister[y], String.valueOf(sister_num[y]), round(tarekahMultiply(sister[y], tarekah)), y == cnt ? (mowqof[17].equals("0/1") ? "" : mowqof[17]) : ""));
						noteVector.add(sister_note[y]);
					}

					if (!m_b[y].equals("0/1") || (y == cnt && getNumerator(mowqof[18]) != 0 && m_b[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[111], m_b[y].equals("0/1") ? ("0/" + LCDDenominator) : m_b[y], String.valueOf(m_b_num[y]), round(tarekahMultiply(m_b[y], tarekah)), y == cnt ? (mowqof[18].equals("0/1") ? "" : mowqof[18]) : ""));
						noteVector.add(m_b_note[y]);
					}

					if (!m_sister[y].equals("0/1") || (y == cnt && getNumerator(mowqof[19]) != 0 && m_sister[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[112], m_sister[y].equals("0/1") ? ("0/" + LCDDenominator) : m_sister[y], String.valueOf(m_sister_num[y]), round(tarekahMultiply(m_sister[y], tarekah)), y == cnt ? (mowqof[19].equals("0/1") ? "" : mowqof[19]) : ""));
						noteVector.add(m_sister_note[y]);
					}

					if (!f_b[y].equals("0/1") || (y == cnt && getNumerator(mowqof[20]) != 0 && f_b[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[113], f_b[y].equals("0/1") ? ("0/" + LCDDenominator) : f_b[y], String.valueOf(f_b_num[y]), round(tarekahMultiply(f_b[y], tarekah)), y == cnt ? (mowqof[20].equals("0/1") ? "" : mowqof[20]) : ""));
						noteVector.add(f_b_note[y]);
					}

					if (!f_sister[y].equals("0/1") || (y == cnt && getNumerator(mowqof[21]) != 0 && f_sister[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[114], f_sister[y].equals("0/1") ? ("0/" + LCDDenominator) : f_sister[y], String.valueOf(f_sister_num[y]), round(tarekahMultiply(f_sister[y], tarekah)), y == cnt ? (mowqof[21].equals("0/1") ? "" : mowqof[21]) : ""));
						noteVector.add(f_sister_note[y]);
					}

					if (!s_b[y].equals("0/1") || (y == cnt && getNumerator(mowqof[22]) != 0 && s_b[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[115], s_b[y].equals("0/1") ? ("0/" + LCDDenominator) : s_b[y], String.valueOf(s_b_num[y]), round(tarekahMultiply(s_b[y], tarekah)), y == cnt ? (mowqof[22].equals("0/1") ? "" : mowqof[22]) : ""));
						noteVector.add(s_b_note[y]);
					}

					if (!s_b_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[23]) != 0 && s_b_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[116], s_b_f[y].equals("0/1") ? ("0/" + LCDDenominator) : s_b_f[y], String.valueOf(s_b_f_num[y]), round(tarekahMultiply(s_b_f[y], tarekah)), y == cnt ? (mowqof[23].equals("0/1") ? "" : mowqof[23]) : ""));
						noteVector.add(s_b_f_note[y]);
					}

					if (!u[y].equals("0/1") || (y == cnt && getNumerator(mowqof[24]) != 0 && u[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[117], u[y].equals("0/1") ? ("0/" + LCDDenominator) : u[y], String.valueOf(u_num[y]), round(tarekahMultiply(u[y], tarekah)), y == cnt ? (mowqof[24].equals("0/1") ? "" : mowqof[24]) : ""));
						noteVector.add(u_note[y]);
					}

					if (!u_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[25]) != 0 && u_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[118], u_f[y].equals("0/1") ? ("0/" + LCDDenominator) : u_f[y], String.valueOf(u_f_num[y]), round(tarekahMultiply(u_f[y], tarekah)), y == cnt ? (mowqof[25].equals("0/1") ? "" : mowqof[25]) : ""));
						noteVector.add(u_f_note[y]);
					}

					if (!s_u[y].equals("0/1") || (y == cnt && getNumerator(mowqof[26]) != 0 && s_u[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[119], s_u[y].equals("0/1") ? ("0/" + LCDDenominator) : s_u[y], String.valueOf(s_u_num[y]), round(tarekahMultiply(s_u[y], tarekah)), y == cnt ? (mowqof[26].equals("0/1") ? "" : mowqof[26]) : ""));
						noteVector.add(s_u_note[y]);
					}

					if (!s_u_f[y].equals("0/1") || (y == cnt && getNumerator(mowqof[27]) != 0 && s_u_f[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[120], s_u_f[y].equals("0/1") ? ("0/" + LCDDenominator) : s_u_f[y], String.valueOf(s_u_f_num[y]), round(tarekahMultiply(s_u_f[y], tarekah)), y == cnt ? (mowqof[27].equals("0/1") ? "" : mowqof[27]) : ""));
						noteVector.add(s_u_f_note[y]);
					}

					if (!khontha[y].equals("0/1") || (y == cnt && getNumerator(mowqof[28]) != 0 && khontha[y].equals("0/1")))
						resultsDataVector.addElement(new ResultsData(translations[121], khontha[y].equals("0/1") ? ("0/" + LCDDenominator) : khontha[y], "1", round(tarekahMultiply(khontha[y], tarekah)), y == cnt ? (mowqof[28].equals("0/1") ? "" : mowqof[28]) : ""));
					if (!missing[y].equals("0/1") || (y == cnt && getNumerator(mowqof[29]) != 0 && missing[y].equals("0/1")))
						resultsDataVector.addElement(new ResultsData(translations[122], missing[y].equals("0/1") ? ("0/" + LCDDenominator) : missing[y], "1", round(tarekahMultiply(missing[y], tarekah)), y == cnt ? (mowqof[29].equals("0/1") ? "" : mowqof[29]) : ""));
					if (!haml[y].equals("0/1") || (y == cnt && getNumerator(mowqof[30]) != 0 && haml[y].equals("0/1")))
					{
						resultsDataVector.addElement(new ResultsData(translations[123], haml[y].equals("0/1") ? ("0/" + LCDDenominator) : haml[y], /* Cannot be 1 since in SHAFEE it might be 2 */"", round(tarekahMultiply(haml[y], tarekah)), y == cnt ? (mowqof[30].equals("0/1") ? "" : mowqof[30]) : ""));
						noteVector.add(haml_note[y]);
					}

					// Version 1.5, remove mowqof[30] for bondman!
					if (!bondman[y].equals("0/1"))
						resultsDataVector.addElement(new ResultsData(translations[26], bondman[y], "1", round(tarekahMultiply(bondman[y], tarekah)), ""));
					if (!muslim_trusts[y].equals("0/1"))
						resultsDataVector.addElement(new ResultsData(translations[124], muslim_trusts[y], ""/* Version 1.7, Removed '1'*/, round(tarekahMultiply(muslim_trusts[y], tarekah)), ""));

					if (willsMaleSex)
					{
						// Version 1.6
						if (!w_s_s.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[213], w_s_s, String.valueOf(w_s_s_num), round(tarekahMultiply(w_s_s, tarekah)), ""));
						if (!w_d_s.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[214], w_d_s, String.valueOf(w_d_s_num), round(tarekahMultiply(w_d_s, tarekah)), ""));

						// Version 2.4
						if (!w_f.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[236], w_f, String.valueOf(w_f_num), round(tarekahMultiply(w_f, tarekah)), ""));
						if (!w_m.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[235], w_m, String.valueOf(w_m_num), round(tarekahMultiply(w_m, tarekah)), ""));
					}
					else // Version 1.9
					{
						if (!w_s_d.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[229], w_s_d, String.valueOf(w_s_d_num), round(tarekahMultiply(w_s_d, tarekah)), ""));
						if (!w_d_d.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[230], w_d_d, String.valueOf(w_d_d_num), round(tarekahMultiply(w_d_d, tarekah)), ""));

						// Version 2.4
						if (!w_f.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[238], w_f, String.valueOf(w_f_num), round(tarekahMultiply(w_f, tarekah)), ""));
						if (!w_m.equals("0/1"))
							resultsDataVector.addElement(new ResultsData(translations[237], w_m, String.valueOf(w_m_num), round(tarekahMultiply(w_m, tarekah)), ""));
					}

					if (bondmanCaseSelected) break;
				}
				else
				{
					//i.e. arham==1
					// Version 1.4, The order of the operations is changed. Dividing is done first then multiplication to avoid OverFlowException.
					h[y] = String.valueOf(getNumerator(h[y]) * (LCDDenominator / getDenominator(h[y]))) + '/' + LCDDenominator;
					w[y] = String.valueOf(getNumerator(w[y]) * (LCDDenominator / getDenominator(w[y]))) + '/' + LCDDenominator;
					a_f_m[y] = String.valueOf(getNumerator(a_f_m[y]) * (LCDDenominator / getDenominator(a_f_m[y]))) + '/' + LCDDenominator;
					a_f_f_m[y] = String.valueOf(getNumerator(a_f_f_m[y]) * (LCDDenominator / getDenominator(a_f_f_m[y]))) + '/' + LCDDenominator;
					a_f_m_f[y] = String.valueOf(getNumerator(a_f_m_f[y]) * (LCDDenominator / getDenominator(a_f_m_f[y]))) + '/' + LCDDenominator;
					a_f_m_m[y] = String.valueOf(getNumerator(a_f_m_m[y]) * (LCDDenominator / getDenominator(a_f_m_m[y]))) + '/' + LCDDenominator;
					a_m_f_m[y] = String.valueOf(getNumerator(a_m_f_m[y]) * (LCDDenominator / getDenominator(a_m_f_m[y]))) + '/' + LCDDenominator;
					a_d_d[y] = String.valueOf(getNumerator(a_d_d[y]) * (LCDDenominator / getDenominator(a_d_d[y]))) + '/' + LCDDenominator;
					a_s_d[y] = String.valueOf(getNumerator(a_s_d[y]) * (LCDDenominator / getDenominator(a_s_d[y]))) + '/' + LCDDenominator;
					a_s_d_s[y] = String.valueOf(getNumerator(a_s_d_s[y]) * (LCDDenominator / getDenominator(a_s_d_s[y]))) + '/' + LCDDenominator;
					a_d_d_s[y] = String.valueOf(getNumerator(a_d_d_s[y]) * (LCDDenominator / getDenominator(a_d_d_s[y]))) + '/' + LCDDenominator;
					a_s_d_d[y] = String.valueOf(getNumerator(a_s_d_d[y]) * (LCDDenominator / getDenominator(a_s_d_d[y]))) + '/' + LCDDenominator;
					a_d_d_d[y] = String.valueOf(getNumerator(a_d_d_d[y]) * (LCDDenominator / getDenominator(a_d_d_d[y]))) + '/' + LCDDenominator;
					a_s_s_d[y] = String.valueOf(getNumerator(a_s_s_d[y]) * (LCDDenominator / getDenominator(a_s_s_d[y]))) + '/' + LCDDenominator;
					a_d_s_d[y] = String.valueOf(getNumerator(a_d_s_d[y]) * (LCDDenominator / getDenominator(a_d_s_d[y]))) + '/' + LCDDenominator;
					a_s_mb[y] = String.valueOf(getNumerator(a_s_mb[y]) * (LCDDenominator / getDenominator(a_s_mb[y]))) + '/' + LCDDenominator;
					a_d_mb[y] = String.valueOf(getNumerator(a_d_mb[y]) * (LCDDenominator / getDenominator(a_d_mb[y]))) + '/' + LCDDenominator;
					a_s_msister[y] = String.valueOf(getNumerator(a_s_msister[y]) * (LCDDenominator / getDenominator(a_s_msister[y]))) + '/' + LCDDenominator;
					a_d_msister[y] = String.valueOf(getNumerator(a_d_msister[y]) * (LCDDenominator / getDenominator(a_d_msister[y]))) + '/' + LCDDenominator;
					a_d_b[y] = String.valueOf(getNumerator(a_d_b[y]) * (LCDDenominator / getDenominator(a_d_b[y]))) + '/' + LCDDenominator;
					a_s_sister[y] = String.valueOf(getNumerator(a_s_sister[y]) * (LCDDenominator / getDenominator(a_s_sister[y]))) + '/' + LCDDenominator;
					a_d_sister[y] = String.valueOf(getNumerator(a_d_sister[y]) * (LCDDenominator / getDenominator(a_d_sister[y]))) + '/' + LCDDenominator;
					a_d_fb[y] = String.valueOf(getNumerator(a_d_fb[y]) * (LCDDenominator / getDenominator(a_d_fb[y]))) + '/' + LCDDenominator;
					a_s_fsister[y] = String.valueOf(getNumerator(a_s_fsister[y]) * (LCDDenominator / getDenominator(a_s_fsister[y]))) + '/' + LCDDenominator;
					a_d_fsister[y] = String.valueOf(getNumerator(a_d_fsister[y]) * (LCDDenominator / getDenominator(a_d_fsister[y]))) + '/' + LCDDenominator;
					a_ul[y] = String.valueOf(getNumerator(a_ul[y]) * (LCDDenominator / getDenominator(a_ul[y]))) + '/' + LCDDenominator;
					a_kl[y] = String.valueOf(getNumerator(a_kl[y]) * (LCDDenominator / getDenominator(a_kl[y]))) + '/' + LCDDenominator;
					a_k[y] = String.valueOf(getNumerator(a_k[y]) * (LCDDenominator / getDenominator(a_k[y]))) + '/' + LCDDenominator;
					a_s_ul[y] = String.valueOf(getNumerator(a_s_ul[y]) * (LCDDenominator / getDenominator(a_s_ul[y]))) + '/' + LCDDenominator;
					a_d_ul[y] = String.valueOf(getNumerator(a_d_ul[y]) * (LCDDenominator / getDenominator(a_d_ul[y]))) + '/' + LCDDenominator;
					a_d_u[y] = String.valueOf(getNumerator(a_d_u[y]) * (LCDDenominator / getDenominator(a_d_u[y]))) + '/' + LCDDenominator;
					a_s_kl[y] = String.valueOf(getNumerator(a_s_kl[y]) * (LCDDenominator / getDenominator(a_s_kl[y]))) + '/' + LCDDenominator;
					a_d_kl[y] = String.valueOf(getNumerator(a_d_kl[y]) * (LCDDenominator / getDenominator(a_d_kl[y]))) + '/' + LCDDenominator;
					a_d_k[y] = String.valueOf(getNumerator(a_d_k[y]) * (LCDDenominator / getDenominator(a_d_k[y]))) + '/' + LCDDenominator;
					a_s_k[y] = String.valueOf(getNumerator(a_s_k[y]) * (LCDDenominator / getDenominator(a_s_k[y]))) + '/' + LCDDenominator;
					muslim_trusts[y] = String.valueOf(getNumerator(muslim_trusts[y]) * (LCDDenominator / getDenominator(muslim_trusts[y]))) + '/' + LCDDenominator;

					// This to return zero inheritors to "0/1" to not display in the results table.
					if (getNumerator(h[y]) == 0) h[y] = "0/1";
					if (getNumerator(w[y]) == 0) w[y] = "0/1";
					if (getNumerator(a_f_m[y]) == 0) a_f_m[y] = "0/1";
					if (getNumerator(a_f_f_m[y]) == 0) a_f_f_m[y] = "0/1";
					if (getNumerator(a_f_m_f[y]) == 0) a_f_m_f[y] = "0/1";
					if (getNumerator(a_f_m_m[y]) == 0) a_f_m_m[y] = "0/1";
					if (getNumerator(a_m_f_m[y]) == 0) a_m_f_m[y] = "0/1";
					if (getNumerator(a_d_d[y]) == 0) a_d_d[y] = "0/1";
					if (getNumerator(a_s_d[y]) == 0) a_s_d[y] = "0/1";
					if (getNumerator(a_s_d_s[y]) == 0) a_s_d_s[y] = "0/1";
					if (getNumerator(a_d_d_s[y]) == 0) a_d_d_s[y] = "0/1";
					if (getNumerator(a_s_d_d[y]) == 0) a_s_d_d[y] = "0/1";
					if (getNumerator(a_d_d_d[y]) == 0) a_d_d_d[y] = "0/1";
					if (getNumerator(a_s_s_d[y]) == 0) a_s_s_d[y] = "0/1";
					if (getNumerator(a_d_s_d[y]) == 0) a_d_s_d[y] = "0/1";
					if (getNumerator(a_s_mb[y]) == 0) a_s_mb[y] = "0/1";
					if (getNumerator(a_d_mb[y]) == 0) a_d_mb[y] = "0/1";
					if (getNumerator(a_s_msister[y]) == 0) a_s_msister[y] = "0/1";
					if (getNumerator(a_d_msister[y]) == 0) a_d_msister[y] = "0/1";
					if (getNumerator(a_d_b[y]) == 0) a_d_b[y] = "0/1";
					if (getNumerator(a_s_sister[y]) == 0) a_s_sister[y] = "0/1";
					if (getNumerator(a_d_sister[y]) == 0) a_d_sister[y] = "0/1";
					if (getNumerator(a_d_fb[y]) == 0) a_d_fb[y] = "0/1";
					if (getNumerator(a_s_fsister[y]) == 0) a_s_fsister[y] = "0/1";
					if (getNumerator(a_d_fsister[y]) == 0) a_d_fsister[y] = "0/1";
					if (getNumerator(a_ul[y]) == 0) a_ul[y] = "0/1";
					if (getNumerator(a_kl[y]) == 0) a_kl[y] = "0/1";
					if (getNumerator(a_k[y]) == 0) a_k[y] = "0/1";
					if (getNumerator(a_s_ul[y]) == 0) a_s_ul[y] = "0/1";
					if (getNumerator(a_d_ul[y]) == 0) a_d_ul[y] = "0/1";
					if (getNumerator(a_d_u[y]) == 0) a_d_u[y] = "0/1";
					if (getNumerator(a_s_kl[y]) == 0) a_s_kl[y] = "0/1";
					if (getNumerator(a_d_kl[y]) == 0) a_d_kl[y] = "0/1";
					if (getNumerator(a_d_k[y]) == 0) a_d_k[y] = "0/1";
					if (getNumerator(a_s_k[y]) == 0) a_s_k[y] = "0/1";
					if (getNumerator(muslim_trusts[y]) == 0) muslim_trusts[y] = "0/1";

					// Version 1.5, Adding comments
					for (int j = noteVector.size(); j < resultsDataVector.size(); j++)
						noteVector.add("");

					if (!h[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[125], h[y], String.valueOf(h_num[y]), round(tarekahMultiply(h[y], tarekah)), ""));
						noteVector.add(h_note[y]);
					}
					if (!w[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[126], w[y], String.valueOf(w_num[y]), round(tarekahMultiply(w[y], tarekah)), ""));
						noteVector.add(w_note[y]);
					}
					if (!a_f_m[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[127], a_f_m[y], String.valueOf(a_f_m_num[y]), round(tarekahMultiply(a_f_m[y], tarekah)), ""));
						noteVector.add(a_f_m_note[y]);
					}
					if (!a_f_f_m[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[128], a_f_f_m[y], String.valueOf(a_f_f_m_num[y]), round(tarekahMultiply(a_f_f_m[y], tarekah)), ""));
						noteVector.add(a_f_f_m_note[y]);
					}
					if (!a_f_m_f[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[129], a_f_m_f[y], String.valueOf(a_f_m_f_num[y]), round(tarekahMultiply(a_f_m_f[y], tarekah)), ""));
						noteVector.add(a_f_m_f_note[y]);
					}
					if (!a_f_m_m[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[130], a_f_m_m[y], String.valueOf(a_f_m_m_num[y]), round(tarekahMultiply(a_f_m_m[y], tarekah)), ""));
						noteVector.add(a_f_m_m_note[y]);
					}
					if (!a_m_f_m[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[131], a_m_f_m[y], String.valueOf(a_m_f_m_num[y]), round(tarekahMultiply(a_m_f_m[y], tarekah)), ""));
						noteVector.add(a_m_f_m_note[y]);
					}
					if (!a_d_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[132], a_d_d[y], String.valueOf(a_d_d_num[y]), round(tarekahMultiply(a_d_d[y], tarekah)), ""));
						noteVector.add(a_d_d_note[y]);
					}
					if (!a_s_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[133], a_s_d[y], String.valueOf(a_s_d_num[y]), round(tarekahMultiply(a_s_d[y], tarekah)), ""));
						noteVector.add(a_s_d_note[y]);
					}
					if (!a_s_d_s[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[134], a_s_d_s[y], String.valueOf(a_s_d_s_num[y]), round(tarekahMultiply(a_s_d_s[y], tarekah)), ""));
						noteVector.add(a_s_d_s_note[y]);
					}
					if (!a_d_d_s[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[135], a_d_d_s[y], String.valueOf(a_d_d_s_num[y]), round(tarekahMultiply(a_d_d_s[y], tarekah)), ""));
						noteVector.add(a_d_d_s_note[y]);
					}
					if (!a_s_d_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[136], a_s_d_d[y], String.valueOf(a_s_d_d_num[y]), round(tarekahMultiply(a_s_d_d[y], tarekah)), ""));
						noteVector.add(a_s_d_d_note[y]);
					}
					if (!a_d_d_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[137], a_d_d_d[y], String.valueOf(a_d_d_d_num[y]), round(tarekahMultiply(a_d_d_d[y], tarekah)), ""));
						noteVector.add(a_d_d_d_note[y]);
					}
					if (!a_s_s_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[138], a_s_s_d[y], String.valueOf(a_s_s_d_num[y]), round(tarekahMultiply(a_s_s_d[y], tarekah)), ""));
						noteVector.add(a_s_s_d_note[y]);
					}
					if (!a_d_s_d[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[139], a_d_s_d[y], String.valueOf(a_d_s_d_num[y]), round(tarekahMultiply(a_d_s_d[y], tarekah)), ""));
						noteVector.add(a_d_s_d_note[y]);
					}
					if (!a_s_mb[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[140], a_s_mb[y], String.valueOf(a_s_mb_num[y]), round(tarekahMultiply(a_s_mb[y], tarekah)), ""));
						noteVector.add(a_s_mb_note[y]);
					}
					if (!a_d_mb[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[141], a_d_mb[y], String.valueOf(a_d_mb_num[y]), round(tarekahMultiply(a_d_mb[y], tarekah)), ""));
						noteVector.add(a_d_mb_note[y]);
					}
					if (!a_s_msister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[142], a_s_msister[y], String.valueOf(a_s_msister_num[y]), round(tarekahMultiply(a_s_msister[y], tarekah)), ""));
						noteVector.add(a_s_msister_note[y]);
					}
					if (!a_d_msister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[143], a_d_msister[y], String.valueOf(a_d_msister_num[y]), round(tarekahMultiply(a_d_msister[y], tarekah)), ""));
						noteVector.add(a_d_msister_note[y]);
					}
					if (!a_d_b[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[144], a_d_b[y], String.valueOf(a_d_b_num[y]), round(tarekahMultiply(a_d_b[y], tarekah)), ""));
						noteVector.add(a_d_b_note[y]);
					}
					if (!a_s_sister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[145], a_s_sister[y], String.valueOf(a_s_sister_num[y]), round(tarekahMultiply(a_s_sister[y], tarekah)), ""));
						noteVector.add(a_s_sister_note[y]);
					}
					if (!a_d_sister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[146], a_d_sister[y], String.valueOf(a_d_sister_num[y]), round(tarekahMultiply(a_d_sister[y], tarekah)), ""));
						noteVector.add(a_d_sister_note[y]);
					}
					if (!a_d_fb[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[147], a_d_fb[y], String.valueOf(a_d_fb_num[y]), round(tarekahMultiply(a_d_fb[y], tarekah)), ""));
						noteVector.add(a_d_fb_note[y]);
					}
					if (!a_s_fsister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[148], a_s_fsister[y], String.valueOf(a_s_fsister_num[y]), round(tarekahMultiply(a_s_fsister[y], tarekah)), ""));
						noteVector.add(a_s_fsister_note[y]);
					}
					if (!a_d_fsister[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[149], a_d_fsister[y], String.valueOf(a_d_fsister_num[y]), round(tarekahMultiply(a_d_fsister[y], tarekah)), ""));
						noteVector.add(a_d_fsister_note[y]);
					}
					if (!a_ul[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[150], a_ul[y], String.valueOf(a_ul_num[y]), round(tarekahMultiply(a_ul[y], tarekah)), ""));
						noteVector.add(a_ul_note[y]);
					}
					if (!a_kl[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[151], a_kl[y], String.valueOf(a_kl_num[y]), round(tarekahMultiply(a_kl[y], tarekah)), ""));
						noteVector.add(a_kl_note[y]);
					}
					if (!a_k[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[152], a_k[y], String.valueOf(a_k_num[y]), round(tarekahMultiply(a_k[y], tarekah)), ""));
						noteVector.add(a_k_note[y]);
					}
					if (!a_s_ul[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[153], a_s_ul[y], String.valueOf(a_s_ul_num[y]), round(tarekahMultiply(a_s_ul[y], tarekah)), ""));
						noteVector.add(a_s_ul_note[y]);
					}
					if (!a_d_ul[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[154], a_d_ul[y], String.valueOf(a_d_ul_num[y]), round(tarekahMultiply(a_d_ul[y], tarekah)), ""));
						noteVector.add(a_d_ul_note[y]);
					}
					if (!a_d_u[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[155], a_d_u[y], String.valueOf(a_d_u_num[y]), round(tarekahMultiply(a_d_u[y], tarekah)), ""));
						noteVector.add(a_d_u_note[y]);
					}
					if (!a_s_kl[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[156], a_s_kl[y], String.valueOf(a_s_kl_num[y]), round(tarekahMultiply(a_s_kl[y], tarekah)), ""));
						noteVector.add(a_s_kl_note[y]);
					}
					if (!a_d_kl[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[157], a_d_kl[y], String.valueOf(a_d_kl_num[y]), round(tarekahMultiply(a_d_kl[y], tarekah)), ""));
						noteVector.add(a_d_kl_note[y]);
					}
					if (!a_d_k[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[158], a_d_k[y], String.valueOf(a_d_k_num[y]), round(tarekahMultiply(a_d_k[y], tarekah)), ""));
						noteVector.add(a_d_k_note[y]);
					}
					if (!a_s_k[y].equals("0/1"))
					{
						resultsDataVector.addElement(new ResultsData(translations[159], a_s_k[y], String.valueOf(a_s_k_num[y]), round(tarekahMultiply(a_s_k[y], tarekah)), ""));
						noteVector.add(a_s_k_note[y]);
					}
					if (!muslim_trusts[y].equals("0/1"))
						resultsDataVector.addElement(new ResultsData(translations[160], muslim_trusts[y], "", round(tarekahMultiply(muslim_trusts[y], tarekah)), ""));
				}
				resultsDataVector.addElement(new ResultsData("", "", "", "", ""));

				// For monasakha
				if (inheritor[y] != 0)// To not show when other cases is happen
				{
					String deathInheritor = translations[189] + (verticalMonasakha ? (y + 1) : "1");

					// This conditions are used to display the type of the dead person
					if (inheritor[y] == 1)
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[161] + deathInheritor, "", "", "", ""));
					if (inheritor[y] == 28)
						resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[162] + deathInheritor, "", "", "", ""));

					for (int k = 2; k <= 27; k++)
						if (inheritor[y] == k)
							resultsDataVector.addElement(new ResultsData("<html><div style='color:red;white-space:pre;'>" + translations[161 + k] + deathInheritor, "", "", "", ""));

					// To not show at the end
					if (y != cnt) resultsDataVector.addElement(new ResultsData("", "", "", "", ""));
				}
			}

			// This is only to display the amount of tarekah
			resultsDataVector.addElement(new ResultsData("", "", "", "<html><div style='color:red;white-space:pre;'>" + round(tarekah), ""));

			// Version 1.3
			for (int i = noteVector.size(); i < resultsDataVector.size(); i++)
				noteVector.add("");

			// Version 1.3, We changed the way to calculate all madhab to be separate for all of them
			if (!allMadaheb || (allMadaheb && lastRotation))
			{
				// Creating the table model by passing the vector of ResultsData objects
				final MawarethTableModel resultsTableModel = new MawarethTableModel(resultsDataVector, translations[222], translations[223], translations[224], translations[225], translations[226]);

				// Creating the JTable by passing the table model
				final JTable resultsTable = new JTable(resultsTableModel);
				/*
				{
					@Override
					public Component prepareRenderer(TableCellRenderer renderer, int row, int col)
					{
						Component c = super.prepareRenderer(renderer, row, col);
						if (col == 0)
						{
							String color = (String) getValueAt(row, col);
							c.setForeground(getColor(color));
						}
						else
							c.setForeground(getForeground());
						return c;
					}

					private Color getColor(String col)
					{
						Color color = null;
						if (col.startsWith("<red>"))
						{
							color = Color.GREEN;
						}
						else
							color = getForeground();

						return color;
					}
				};
				*/
				resultsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				// Version 1.6, Remove mowqof column if it is not used.
				for (int u = 0; u < resultsDataVector.size(); u++)
				{
					final ResultsData rd = resultsDataVector.elementAt(u);
					if (rd.getInheritorsMowqof().length() != 0)
					{
						emptyMowqofColumn = false;
						break;
					}
				}

				if (emptyMowqofColumn)
					resultsTable.getColumnModel().removeColumn(resultsTable.getColumnModel().getColumn(4));

				// Version 1.7
				if (noTarekahColumn)
					resultsTable.getColumnModel().removeColumn(resultsTable.getColumnModel().getColumn(3));

				final SelectionListener listener = new SelectionListener(resultsTable);
				resultsTable.getSelectionModel().addListSelectionListener(listener);
				resultsTable.addMouseListener(listener);

				final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				if (MaknoonIslamicEncyclopedia.language)
					renderer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

				// Version 1.6, resultsTable.getColumnCount() instead of 5 to handle removing mowqof column.
				for (int i = 0; i < resultsTable.getColumnCount(); i++)
				{
					final TableColumn column = resultsTable.getColumnModel().getColumn(i);
					column.setCellRenderer(renderer);
					if (i == 0) column.setPreferredWidth(300);
				}

				resultsTable.setPreferredScrollableViewportSize(new Dimension(700, 300));
				final JPanel mawarethMainPanel = new JPanel(new BorderLayout());

				// Create the scroll pane and add the table to it.
				mawarethMainPanel.add(new JScrollPane(resultsTable));

				final JPanel selectedInheritorsPanel = new JPanel(new BorderLayout());
				selectedInheritorsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), translations[193], 0, 0, null, Color.red));

				final JPanel mainSelectedInheritorsPanel = new JPanel();
				mainSelectedInheritorsPanel.setLayout(new BoxLayout(mainSelectedInheritorsPanel, BoxLayout.Y_AXIS));
				selectedInheritorsPanel.add(mainSelectedInheritorsPanel, BorderLayout.CENTER);

				/*
				 * Version 1.1
				 * This part is used to display the all selected inheritors
				 * Be aware that we didn't use monasakha_c == 0 in the condidtion since it is assign to 0 in the second
				 * rotation in the special cases. On the other hand we intreduce another variable 'monasakhaCaseSelected'
				 *
				 * Version 1.2
				 * Some comments for all special cases are added for more information.
				 */
				String selectedInheritors = "";
				if (arham_p == 0)
				{
					int counter = 0;
					if (monasakhaCaseSelected) counter = cnt;
					for (int i = 0; i <= counter; i++)
					{
						selectedInheritors = "";
						if (monasakhaCaseSelected && i != 0)
						{
							String deathInheritor = translations[189] + (verticalMonasakha ? i : "1");

							// These conditions are used to display the maleSex of the dead person.
							if (inheritor[i - 1] == 1) selectedInheritors = translations[161] + deathInheritor;
							if (inheritor[i - 1] == 28) selectedInheritors = translations[162] + deathInheritor;

							for (int k = 2; k <= 27; k++)
								if (inheritor[i - 1] == k) selectedInheritors = translations[161 + k] + deathInheritor;

							selectedInheritors = "<font color=maroon>" + selectedInheritors + translations[190] + "</font>";
						}

						if (h_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[93];
						if (w_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[94] + "<font color=green> (" + w_num[i] + ")</font>";
						if (f_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[95];
						if (f_f_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[96];
						if (m_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[97];
						if (m_m_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[98];
						if (m_f_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[99];
						if (m_m_m_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[100];
						if (m_m_f_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[101];
						if (m_f_f_num[i] != 0) selectedInheritors = selectedInheritors + " - " + translations[102];
						if (s_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[103] + "<font color=green> (" + s_num[i] + ")</font>";
						if (d_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[104] + "<font color=green> (" + d_num[i] + ")</font>";
						if (s_s_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[105] + "<font color=green> (" + s_s_num[i] + ")</font>";
						if (d_s_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[106] + "<font color=green> (" + d_s_num[i] + ")</font>";
						if (s_s_s_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[107] + "<font color=green> (" + s_s_s_num[i] + ")</font>";
						if (d_s_s_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[108] + "<font color=green> (" + d_s_s_num[i] + ")</font>";
						if (b_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[109] + "<font color=green> (" + b_num[i] + ")</font>";
						if (sister_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[110] + "<font color=green> (" + sister_num[i] + ")</font>";
						if (m_b_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[111] + "<font color=green> (" + m_b_num[i] + ")</font>";
						if (m_sister_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[112] + "<font color=green> (" + m_sister_num[i] + ")</font>";
						if (f_b_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[113] + "<font color=green> (" + f_b_num[i] + ")</font>";
						if (f_sister_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[114] + "<font color=green> (" + f_sister_num[i] + ")</font>";
						if (s_b_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[115] + "<font color=green> (" + s_b_num[i] + ")</font>";
						if (s_b_f_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[116] + "<font color=green> (" + s_b_f_num[i] + ")</font>";
						if (u_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[117] + "<font color=green> (" + u_num[i] + ")</font>";
						if (u_f_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[118] + "<font color=green> (" + u_f_num[i] + ")</font>";
						if (s_u_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[119] + "<font color=green> (" + s_u_num[i] + ")</font>";
						if (s_u_f_num[i] != 0)
							selectedInheritors = selectedInheritors + " - " + translations[120] + "<font color=green> (" + s_u_f_num[i] + ")</font>";

						if (bondmanCaseSelected)
						{
							String bondmanInheritor = null;
							for (int k = 1; k <= 28; k++)
								if (bondman_p == k) bondmanInheritor = translations[160 + k];

							selectedInheritors = selectedInheritors + " - " + translations[26] + " <font color=green>" + bondmanInheritor + "</font>";
						}

						if (missingCaseSelected)
						{
							String missingInheritor = null;
							if (missing_p == 1) missingInheritor = translations[93];
							if (missing_p == 2) missingInheritor = translations[94];
							if (missing_p == 3) missingInheritor = translations[95];
							if (missing_p == 4) missingInheritor = translations[97];
							if (missing_p == 5) missingInheritor = translations[98];
							if (missing_p == 6) missingInheritor = translations[99];
							if (missing_p == 7) missingInheritor = translations[100];
							if (missing_p == 8) missingInheritor = translations[101];
							if (missing_p == 9) missingInheritor = translations[102];
							if (missing_p == 10) missingInheritor = translations[96];

							for (int k = 11; k <= 28; k++)
								if (missing_p == k) missingInheritor = translations[92 + k];

							if (selectedInheritors.equals(""))
								selectedInheritors = " - " + translations[233] + "<font color=green>" + missingInheritor + "</font>";
							else
								selectedInheritors = selectedInheritors + " - " + translations[28] + "<font color=green>" + missingInheritor + "</font>";
						}

						if (hamlCaseSelected)
						{
							String hamlInheritor = null;
							for (int k = 1; k <= 12; k++)
								if (haml_p == k) hamlInheritor = translations[194 + k];

							if (selectedInheritors.equals(""))
								selectedInheritors = " - " + translations[232] + "<font color=green>" + hamlInheritor + "</font>";
							else
								selectedInheritors = selectedInheritors + " - " + translations[194] + "<font color=green>" + hamlInheritor + "</font>";
						}

						if (khonthaCaseSelected)
						{
							String khonthaInheritor = null;
							for (int k = 1; k <= 18; k++)
								if (khontha_p == k) khonthaInheritor = translations[29 + k];

							if (selectedInheritors.equals(""))
								selectedInheritors = " - " + translations[234] + "<font color=green>" + khonthaInheritor + "</font>";
							else
								selectedInheritors = selectedInheritors + " - " + translations[207] + "<font color=green>" + khonthaInheritor + "</font>";
						}

						/*
						 * Version 1.6
						 * Make sure that there is an inheritor.
						 * Change from: !selectedInheritors.equals("") to: selectedInheritors.indexOf(" - ")>-1
						 * To work in monasakha case also (in the second rotation).
						 */
						if (selectedInheritors.contains(" - "))
						{
							// It is very important that <html> should be at the beginning of the string to be considered as HTML
							final JLabel selectedInheritorsLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedInheritors);
							final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
							decoratePanel.add(selectedInheritorsLabels);
							mainSelectedInheritorsPanel.add(decoratePanel);
						}
						else
						{
							// Version 1.6
							final JLabel selectedInheritorsLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedInheritors + ' ' + translations[27]);
							final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
							decoratePanel.add(selectedInheritorsLabels);
							mainSelectedInheritorsPanel.add(decoratePanel);
						}
					}
				}
				else
				{
					if (h_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[125];
					if (w_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[126] + "<font color=green> (" + w_num[0] + ")</font>";
					if (a_f_m_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[127];
					if (a_f_f_m_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[128];
					if (a_f_m_f_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[129];
					if (a_f_m_m_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[130];
					if (a_m_f_m_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[131];
					if (a_d_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[132] + "<font color=green> (" + a_d_d_num[0] + ")</font>";
					if (a_s_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[133] + "<font color=green> (" + a_s_d_num[0] + ")</font>";
					if (a_s_d_s_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[134] + "<font color=green> (" + a_s_d_s_num[0] + ")</font>";
					if (a_d_d_s_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[135] + "<font color=green> (" + a_d_d_s_num[0] + ")</font>";
					if (a_s_d_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[136] + "<font color=green> (" + a_s_d_d_num[0] + ")</font>";
					if (a_d_d_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[137] + "<font color=green> (" + a_d_d_d_num[0] + ")</font>";
					if (a_s_s_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[138] + "<font color=green> (" + a_s_s_d_num[0] + ")</font>";
					if (a_d_s_d_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[139] + "<font color=green> (" + a_d_s_d_num[0] + ")</font>";
					if (a_s_mb_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[140] + "<font color=green> (" + a_s_mb_num[0] + ")</font>";
					if (a_d_mb_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[141] + "<font color=green> (" + a_d_mb_num[0] + ")</font>";
					if (a_s_msister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[142] + "<font color=green> (" + a_s_msister_num[0] + ")</font>"; // Version 1.7, change translations[145] to translations[142]
					if (a_d_msister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[143] + "<font color=green> (" + a_d_msister_num[0] + ")</font>"; // Version 1.7, change translations[146] to translations[143]
					if (a_d_b_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[144] + "<font color=green> (" + a_d_b_num[0] + ")</font>";
					if (a_s_sister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[145] + "<font color=green> (" + a_s_sister_num[0] + ")</font>"; // Version 1.7, change translations[142] to translations[145]
					if (a_d_sister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[146] + "<font color=green> (" + a_d_sister_num[0] + ")</font>"; // Version 1.7, change translations[143] to translations[146]
					if (a_d_fb_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[147] + "<font color=green> (" + a_d_fb_num[0] + ")</font>";
					if (a_s_fsister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[148] + "<font color=green> (" + a_s_fsister_num[0] + ")</font>";
					if (a_d_fsister_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[149] + "<font color=green> (" + a_d_fsister_num[0] + ")</font>";
					if (a_ul_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[150] + "<font color=green> (" + a_ul_num[0] + ")</font>";
					if (a_kl_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[151] + "<font color=green> (" + a_kl_num[0] + ")</font>";
					if (a_k_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[152] + "<font color=green> (" + a_k_num[0] + ")</font>";
					if (a_s_ul_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[153] + "<font color=green> (" + a_s_ul_num[0] + ")</font>";
					if (a_d_ul_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[154] + "<font color=green> (" + a_d_ul_num[0] + ")</font>";
					if (a_d_u_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[155] + "<font color=green> (" + a_d_u_num[0] + ")</font>";
					if (a_s_kl_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[156] + "<font color=green> (" + a_s_kl_num[0] + ")</font>";
					if (a_d_kl_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[157] + "<font color=green> (" + a_d_kl_num[0] + ")</font>";
					if (a_d_k_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[158] + "<font color=green> (" + a_d_k_num[0] + ")</font>";
					if (a_s_k_num[0] != 0)
						selectedInheritors = selectedInheritors + " - " + translations[159] + "<font color=green> (" + a_s_k_num[0] + ")</font>";

					// Version 2.2
					if (!selectedArhamInheritors.equals(""))
						selectedInheritors = selectedInheritors + selectedArhamInheritors;

					if (selectedInheritors.length() != 0)
					{
						// It is very important that <html> should be at the beginning of the string to be considered as HTML
						final JLabel selectedInheritorsLabels = new JLabel("<html><div style='white-space:pre;'>" + selectedInheritors.substring(2) + '.');
						final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
						decoratePanel.add(selectedInheritorsLabels);
						mainSelectedInheritorsPanel.add(decoratePanel);
					}
					else
					{
						final JLabel selectedInheritorsLabels = new JLabel("<html><div style='white-space:pre;'>" + translations[27] + '.');
						final JPanel decoratePanel = new JPanel(new FlowLayout(MaknoonIslamicEncyclopedia.language ? FlowLayout.RIGHT : FlowLayout.LEFT));
						decoratePanel.add(selectedInheritorsLabels);
						mainSelectedInheritorsPanel.add(decoratePanel);
					}
				}
				mawarethMainPanel.add(selectedInheritorsPanel, BorderLayout.NORTH);

				final JPanel printPanel = new JPanel(new BorderLayout());
				final JPanel southPrintPanel = new JPanel(new GridLayout(2, 1));

				final JButton printButton = new JButton(translations[227]);
				printButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						final Thread runner = new Thread()
						{
							public void run()
							{
								printData(resultsTable, false);
							}
						};
						runner.start();
					}
				});

				// Version 1.6, Printing with Names.
				final JButton detailedPrintButton = new JButton(translations[228]);
				detailedPrintButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						detailedPrintableResultsFrame();
					}
				});

				southPrintPanel.add(detailedPrintButton);
				southPrintPanel.add(printButton);

				printPanel.add(southPrintPanel, BorderLayout.SOUTH);
				if (MaknoonIslamicEncyclopedia.language) selectedInheritorsPanel.add(printPanel, BorderLayout.WEST);
				else selectedInheritorsPanel.add(printPanel, BorderLayout.EAST);

				add(mawarethMainPanel);

				pack();
				center(this);
				MawarethSystem.this.add(this);

				if (MaknoonIslamicEncyclopedia.language)
				{
					applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
					//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				}

				setVisible(true);
				try
				{
					setMaximum(true);
				}
				catch (java.beans.PropertyVetoException e)
				{
					e.printStackTrace();
				}
			}
		}

		// Version 1.7
		public void detailedPrintableResultsFrame()
		{
			final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + (MaknoonIslamicEncyclopedia.language ? "detailedPrintableResultsFrameArabic.txt" : "detailedPrintableResultsFrameEnglish.txt"));
			final JInternalFrame detailedPrintableResults = new JInternalFrame();
			detailedPrintableResults.setTitle(translations[0]);
			detailedPrintableResults.setMaximizable(true);
			detailedPrintableResults.setResizable(true);
			detailedPrintableResults.setClosable(true);
			detailedPrintableResults.setFrameIcon(null);

			// Version 1.7
			final Vector<Vector<String>> rowsData = new Vector<>();
			final Vector<String> columnNames = new Vector<>();
			columnNames.add(translations[6]);
			columnNames.add(translations[7]);
			columnNames.add(translations[8]);

			// Version 1.7
			if (!noTarekahColumn) columnNames.add(translations[9]);
			if (!emptyMowqofColumn) columnNames.add(translations[10]);

			for (int u = 0; u < resultsDataVector.size(); u++)
			{
				final ResultsData rd = resultsDataVector.elementAt(u);
				final Vector<String> rowData = new Vector<>();
				rowData.add("");
				rowData.add(rd.getInheritors());
				rowData.add(rd.getInheritorsShare());

				// Version 1.7
				if (!noTarekahColumn) rowData.add(rd.getInheritorsAmount());

				// i.e. there is mowqof column.
				if (!emptyMowqofColumn) rowData.add(rd.getInheritorsMowqof());

				rowsData.add(rowData);

				try
				{
					final int count = Integer.parseInt(rd.getInheritorsCount());

					// It will not pass to here unless rd.getInheritorsCount() is a number.
					for (int q = 0; q < count - 1; q++)
						// Version 1.8, new Vector<>(rowData) OR <Vector>rowData.clone() instead of rowData to not deplicate the editing.
						rowsData.add(new Vector<>(rowData));
				}
				catch (NumberFormatException nfe)
				{
					//nfe.printStackTrace();
				}
			}

			final JTable detailedResultsTable = new JTable(rowsData, columnNames)
			{
				public boolean isCellEditable(int row, int col)
				{
					return (col == 0);
				}
			};
			//detailedResultsTable.putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);

			TableColumn column;
			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			if (MaknoonIslamicEncyclopedia.language)
				renderer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			for (int i = 0; i < detailedResultsTable.getColumnCount(); i++)
			{
				column = detailedResultsTable.getColumnModel().getColumn(i);
				column.setCellRenderer(renderer);
				if (i == 0)
					column.setPreferredWidth(300);
				else
					if (i == 1)
						column.setPreferredWidth(250);
			}

			final JButton printButton = new JButton(translations[11]);
			printButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					final Thread runner = new Thread()
					{
						public void run()
						{
							printData(detailedResultsTable, true);
						}
					};
					runner.start();
				}
			});

			detailedResultsTable.setPreferredScrollableViewportSize(new Dimension(700, 300));
			detailedPrintableResults.add(new JScrollPane(detailedResultsTable), BorderLayout.CENTER);
			detailedPrintableResults.add(printButton, BorderLayout.SOUTH);
			MawarethSystem.this.add(detailedPrintableResults);

			if (MaknoonIslamicEncyclopedia.language)
			{
				detailedPrintableResults.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				//MawarethSystem.this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Version 2.8
			}

			detailedPrintableResults.pack();
			center(detailedPrintableResults);
			detailedPrintableResults.setVisible(true);
			try
			{
				detailedPrintableResults.setMaximum(true);
			}
			catch (java.beans.PropertyVetoException e)
			{
				e.printStackTrace();
			}
		}

		// 0 -> continue printing & row index is 0, -1 -> stop printing the current page
		int rowIndex = 0;

		// This is used to check that the current pageIndex is different from the last pageIndex since Printable.print() can be called multiple times with the same pageIndex. Check Printable API
		int lastPageIndex = 0, lastRowIndex = 0;

		// Version 1.6
		JTable printedTable;

		// Used to hide footer/header from summarized printing
		boolean detailedPrint = false;

		public void printData(final JTable table, boolean printType)
		{
			printedTable = table;
			detailedPrint = printType;

			try
			{
				rowIndex = 0;
				lastPageIndex = 0;
				lastRowIndex = 0;
				final PrinterJob prnJob = PrinterJob.getPrinterJob();
				prnJob.setJobName("Mawareth Results");
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

		// print can be called multiple times with the same pageIndex. Check Printable API
		public int print(Graphics pg, PageFormat pageFormat, int pageIndex)
		{
			if (lastPageIndex == pageIndex) rowIndex = lastRowIndex;
			lastPageIndex = pageIndex;

			//System.out.println("rowIndex: "+rowIndex+"    pageIndex: "+pageIndex);
			if (rowIndex == -1) return NO_SUCH_PAGE;

			lastRowIndex = rowIndex; // To preserve the current value for the next time pageIndex is the same

			final Graphics2D g2d = (Graphics2D) pg;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

			final int wPage = (int) pageFormat.getImageableWidth();
			final int hPage = (int) pageFormat.getImageableHeight();
			int y = 0;

			final FontMetrics fm = g2d.getFontMetrics();
			y += fm.getAscent();

			if (pageIndex == 0)
			{
				final String headerTitle = "بسم الله الرحمن الرحيم";
				g2d.drawString(headerTitle, (wPage - fm.stringWidth(headerTitle)) / 2, y);
			}

			g2d.setColor(Color.GRAY);

			y += 20; // space between title and table headers

			int nColumns = printedTable.getColumnCount();
			int nRows = printedTable.getRowCount();
			final int[] x = new int[nColumns];
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
					resultsDataArray[i][j] = HTMLFreeText((String) printedTable.getValueAt(i, j));
					resultsHTMLDataArray[i][j] = (String) printedTable.getValueAt(i, j);
				}
			}

			y += fm.getAscent(); // add ascent of header font because of baseline positioning

			int nCol;
			final TableColumnModel colModel = printedTable.getColumnModel();
			for (nCol = 0; nCol < nColumns; nCol++)
			{
				final TableColumn tk = colModel.getColumn(nCol);
				final String title = (String) tk.getIdentifier();

				// here see the max width of the line
				int width = fm.stringWidth(title);
				for (int i = 0; i < nRows; i++)
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

				g2d.drawString(title, MaknoonIslamicEncyclopedia.language ? (wPage - fm.stringWidth(title) - x[nCol]) : x[nCol], y);
			}

			y += 20; // Space between table header and table contents
			int h = fm.getHeight();

			for (; rowIndex < nRows; rowIndex++)
			{
				y += h;

				if (y >= (hPage - 30)) // 30 space for the footer
					break;

				for (nCol = 0; nCol < nColumns; nCol++)
				{
					String str = resultsHTMLDataArray[rowIndex][nCol];

					if (str.contains("green"))
						//g2d.setColor(Color.green);
						g2d.setColor(new Color(51, 153, 51));
					else
						if (str.contains("red"))
							g2d.setColor(Color.red);
						else
							if (str.contains("maroon"))
								g2d.setColor(new Color(128, 0, 0));
							else
								g2d.setColor(Color.black);

					str = resultsDataArray[rowIndex][nCol];
					g2d.drawString(str, MaknoonIslamicEncyclopedia.language ? (wPage - fm.stringWidth(str) - x[nCol]) : x[nCol], y);
				}
			}

			g2d.setColor(Color.black);

			if (rowIndex == nRows)
				rowIndex = -1;

			if (MaknoonIslamicEncyclopedia.dubaiCourtsVer)
			{
				final String footerTitle = MaknoonIslamicEncyclopedia.language ? "جميع الحقوق محفوظة لمحاكم دبي" : " Copyright©2021, Dubai Courts. All rights reserved.";
				g2d.drawString(footerTitle, (wPage - fm.stringWidth(footerTitle)) / 2, (hPage - fm.getAscent()));
			}
			else
				g2d.drawString("©maknoon.com", (wPage - fm.stringWidth("©maknoon.com")) / 2, (hPage - fm.getAscent()));

			return PAGE_EXISTS;
		}

		/*
		 * Version 1.2
		 * This function is used to multiply the inheritor amount by tarekah only.
		 * It is another version of the general multiply function that belongs to MawarethSystem.
		 */
		public float tarekahMultiply(String number1, float number2)
		{
			int number1Numerator, number1Denominator;

			final StringTokenizer tokens = new StringTokenizer(number1, "/");
			number1Numerator = Integer.parseInt(tokens.nextToken());
			number1Denominator = Integer.parseInt(tokens.nextToken());

			return (number1Numerator * number2 / number1Denominator);
		}
	}

	// Version 1.3
	class SelectionListener extends MouseAdapter implements ListSelectionListener
	{
		final JTable table;
		final JPopupMenu popup = new JPopupMenu();
		final JPanel popupPanel = new JPanel();
		final JLabel popupLabel = new JLabel();
		//JTextArea popupTextArea = new JTextArea(2, 0);

		// It is necessary to keep the table since it is not possible
		// to determine the table from the event's source
		SelectionListener(JTable table)
		{
			// TODO: multi-line continue popup
			this.table = table;
			//popupTextArea.setLineWrap(true);
			//popupTextArea.setWrapStyleWord(true);
			//popupTextArea.setEditable(false);
			//if(language)popupTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			popupPanel.add(popupLabel);
			popup.add(popupPanel);
		}

		public void valueChanged(ListSelectionEvent e)
		{
			popupLabel.setText(noteVector.elementAt(table.getSelectedRow()));

			// The mouse button has not yet been released
			if (e.getValueIsAdjusting()) popup.setVisible(false);
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}

		private void maybeShowPopup(MouseEvent e)
		{
			// To avoid selecting noteVector.elementAt(-1) when pressing menu trigger at the beginning.
			if (table.getSelectedRow() != -1)
			{
				if (noteVector.elementAt(table.getSelectedRow()) == null /* Version 1.7 */ || bondmanCaseSelected)
					popup.setVisible(false);
				else
				{
					if ((noteVector.elementAt(table.getSelectedRow())).length() == 0)
						popup.setVisible(false);
					else
					{
						if (MaknoonIslamicEncyclopedia.language)
						{
							//popup.updateUI(); // Version 2.0, for getPreferredSize() to return correct value
							popup.show(e.getComponent(), e.getX() - popup.getPreferredSize().width, e.getY());
						}
						else
							popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
	}

	// Version 1.7, when malikiAffectedHaml=true; inheritors should wait till haml is finished in MALIKI madhab.
	private boolean malikiAffectedHaml = false;
	void hamlCalculationHelper()
	{
		final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "hamlCalculationHelperArabic.txt" : "hamlCalculationHelperEnglish.txt"));

		int hamlLevel;
		switch (madaheb)
		{
			case HANBALI:
				hamlLevel = 6;
				break;
			case SHAFEE:
				hamlLevel = 5;
				break;
			default:
				hamlLevel = 3;
				break;
		}

		if (haml_p == 1)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s[1]);
				haml[2] = multiply(2, d[2]);
				haml[3] = multiply(4, s[3]);
				haml[4] = multiply(4, d[4]);
			}
			else
			{
				haml[1] = s[1];
				haml[2] = d[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{d[3], s[3]});
				haml[4] = multiply(2, s[4]);
				haml[5] = multiply(2, d[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_num[i] = s_num[0];
				d_num[i] = d_num[0];

				if (s_num[i] == 0) s[i] = "0/1";
				if (d_num[i] == 0) d[i] = "0/1";
			}
		}

		if (haml_p == 2)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_s[1]);
				haml[2] = multiply(2, d_s[2]);
				haml[3] = multiply(4, s_s[3]);
				haml[4] = multiply(4, d_s[4]);
			}
			else
			{
				haml[1] = s_s[1];
				haml[2] = d_s[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{s_s[3], d_s[3]});
				haml[4] = multiply(2, s_s[4]);
				haml[5] = multiply(2, d_s[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_s_num[i] = s_s_num[0];
				d_s_num[i] = d_s_num[0];

				if (s_s_num[i] == 0) s_s[i] = "0/1";
				if (d_s_num[i] == 0) d_s[i] = "0/1";
			}
		}

		if (haml_p == 3)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_s_s[1]);
				haml[2] = multiply(2, d_s_s[2]);
				haml[3] = multiply(4, s_s_s[3]);
				haml[4] = multiply(4, d_s_s[4]);
			}
			else
			{
				haml[1] = s_s_s[1];
				haml[2] = d_s_s[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{s_s_s[3], d_s_s[3]});
				haml[4] = multiply(2, s_s_s[4]);
				haml[5] = multiply(2, d_s_s[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_s_s_num[i] = s_s_s_num[0];
				d_s_s_num[i] = d_s_s_num[0];

				if (s_s_s_num[i] == 0) s_s_s[i] = "0/1";
				if (d_s_s_num[i] == 0) d_s_s[i] = "0/1";
			}
		}

		if (haml_p == 4)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, b[1]);
				haml[2] = multiply(2, sister[2]);
				haml[3] = multiply(4, b[3]);
				haml[4] = multiply(4, sister[4]);
			}
			else
			{
				haml[1] = b[1];
				haml[2] = sister[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{b[3], sister[3]});
				haml[4] = multiply(2, b[4]);
				haml[5] = multiply(2, sister[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				b_num[i] = b_num[0];
				sister_num[i] = sister_num[0];

				if (b_num[i] == 0) b[i] = "0/1";
				if (sister_num[i] == 0) sister[i] = "0/1";
			}
		}

		if (haml_p == 5)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, m_b[1]);
				haml[2] = multiply(2, m_sister[2]);
				haml[3] = multiply(4, m_b[3]);
				haml[4] = multiply(4, m_sister[4]);
			}
			else
			{
				haml[1] = m_b[1];
				haml[2] = m_sister[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{m_b[3], m_sister[3]});
				haml[4] = multiply(2, m_b[4]);
				haml[5] = multiply(2, m_sister[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				m_b_num[i] = m_b_num[0];
				m_sister_num[i] = m_sister_num[0];

				if (m_b_num[i] == 0) m_b[i] = "0/1";
				if (m_sister_num[i] == 0) m_sister[i] = "0/1";
			}
		}

		if (haml_p == 6)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, f_b[1]);
				haml[2] = multiply(2, f_sister[2]);
				haml[3] = multiply(4, f_b[3]);
				haml[4] = multiply(4, f_sister[4]);
			}
			else
			{
				haml[1] = f_b[1];
				haml[2] = f_sister[2];
			}

			if (madaheb == madhabName.HANBALI)
			{
				haml[3] = add(new String[]{f_b[3], f_sister[3]});
				haml[4] = multiply(2, f_b[4]);
				haml[5] = multiply(2, f_sister[5]);
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				f_b_num[i] = f_b_num[0];
				f_sister_num[i] = f_sister_num[0];

				if (f_b_num[i] == 0) f_b[i] = "0/1";
				if (f_sister_num[i] == 0) f_sister[i] = "0/1";
			}
		}

		if (haml_p == 7)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_b[1]);
				haml[3] = multiply(4, s_b[3]);
			}
			else
				haml[1] = s_b[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, s_b[4]);

				// Version 2.0
				haml[3] = s_b[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_b_num[i] = s_b_num[0];
				if (s_b_num[i] == 0) s_b[i] = "0/1";
			}
		}

		if (haml_p == 8)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_b_f[1]);
				haml[3] = multiply(4, s_b_f[3]);
			}
			else
				haml[1] = s_b_f[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, s_b_f[4]);

				// Version 2.0
				haml[3] = s_b_f[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_b_f_num[i] = s_b_f_num[0];
				if (s_b_f_num[i] == 0) s_b_f[i] = "0/1";
			}
		}

		if (haml_p == 9)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, u[1]);
				haml[3] = multiply(4, u[3]);
			}
			else
				haml[1] = u[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, u[4]);

				// Version 2.0
				haml[3] = u[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				u_num[i] = u_num[0];
				if (u_num[i] == 0) u[i] = "0/1";
			}
		}

		if (haml_p == 10)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, u_f[1]);
				haml[3] = multiply(4, u_f[3]);
			}
			else
				haml[1] = u_f[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, u_f[4]);

				// Version 2.0
				haml[3] = u_f[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				u_f_num[i] = u_f_num[0];
				if (u_f_num[i] == 0) u_f[i] = "0/1";
			}
		}

		if (haml_p == 11)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_u[1]);
				haml[3] = multiply(4, s_u[3]);
			}
			else
				haml[1] = s_u[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, s_u[4]);

				// Version 2.0
				haml[3] = s_u[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_u_num[i] = s_u_num[0];
				if (s_u_num[i] == 0) s_u[i] = "0/1";
			}
		}

		if (haml_p == 12)
		{
			if (madaheb == madhabName.SHAFEE)
			{
				haml[1] = multiply(2, s_u_f[1]);
				haml[3] = multiply(4, s_u_f[3]);
			}
			else
				haml[1] = s_u_f[1];

			if (madaheb == madhabName.HANBALI)
			{
				haml[4] = multiply(2, s_u_f[4]);

				// Version 2.0
				haml[3] = s_u_f[3];
				haml_note[3] = translations[2];
			}

			for (int i = 1; i < hamlLevel; i++)
			{
				s_u_f_num[i] = s_u_f_num[0];
				if (s_u_f_num[i] == 0) s_u_f[i] = "0/1";
			}
		}

		// In SHAFEE, in case of any difference because of the number of haml, stop everything i.e. the inheritor = 0
		if (madaheb == madhabName.SHAFEE)
		{
			if (equal(h[1], (h[3])) && equal(h[2], (h[4])))
			{
				if (bigger(h[3], h[4])) h[hamlLevel] = h[4];
				else h[hamlLevel] = h[3];
			}
			else
				h[hamlLevel] = "0/" + getDenominator(h[0]);

			if (equal(w[1], (w[3])) && equal(w[2], (w[4])))
			{
				if (bigger(w[3], w[4])) w[hamlLevel] = w[4];
				else w[hamlLevel] = w[3];
			}
			else
				w[hamlLevel] = "0/" + getDenominator(w[0]);

			if (equal(f[1], (f[3])) && equal(f[2], (f[4])))
			{
				if (bigger(f[3], f[4])) f[hamlLevel] = f[4];
				else f[hamlLevel] = f[3];
			}
			else
				f[hamlLevel] = "0/" + getDenominator(f[0]);

			if (equal(m[1], (m[3])) && equal(m[2], (m[4])))
			{
				if (bigger(m[3], m[4])) m[hamlLevel] = m[4];
				else m[hamlLevel] = m[3];
			}
			else
				m[hamlLevel] = "0/" + getDenominator(m[0]);

			if (equal(m_m[1], (m_m[3])) && equal(m_m[2], (m_m[4])))
			{
				if (bigger(m_m[3], m_m[4])) m_m[hamlLevel] = m_m[4];
				else m_m[hamlLevel] = m_m[3];
			}
			else
				m_m[hamlLevel] = "0/" + getDenominator(m_m[0]);

			if (equal(m_f[1], (m_f[3])) && equal(m_f[2], (m_f[4])))
			{
				if (bigger(m_f[3], m_f[4])) m_f[hamlLevel] = m_f[4];
				else m_f[hamlLevel] = m_f[3];
			}
			else
				m_f[hamlLevel] = "0/" + getDenominator(m_f[0]);

			if (equal(m_m_m[1], (m_m_m[3])) && equal(m_m_m[2], (m_m_m[4])))
			{
				if (bigger(m_m_m[3], m_m_m[4])) m_m_m[hamlLevel] = m_m_m[4];
				else m_m_m[hamlLevel] = m_m_m[3];
			}
			else
				m_m_m[hamlLevel] = "0/" + getDenominator(m_m_m[0]);

			if (equal(m_m_f[1], (m_m_f[3])) && equal(m_m_f[2], (m_m_f[4])))
			{
				if (bigger(m_m_f[3], m_m_f[4])) m_m_f[hamlLevel] = m_m_f[4];
				else m_m_f[hamlLevel] = m_m_f[3];
			}
			else
				m_m_f[hamlLevel] = "0/" + getDenominator(m_m_f[0]);

			if (equal(m_f_f[1], (m_f_f[3])) && equal(m_f_f[2], (m_f_f[4])))
			{
				if (bigger(m_f_f[3], m_f_f[4])) m_f_f[hamlLevel] = m_f_f[4];
				else m_f_f[hamlLevel] = m_f_f[3];
			}
			else
				m_f_f[hamlLevel] = "0/" + getDenominator(m_f_f[0]);

			if (equal(f_f[1], (f_f[3])) && equal(f_f[2], (f_f[4])))
			{
				if (bigger(f_f[3], f_f[4])) f_f[hamlLevel] = f_f[4];
				else f_f[hamlLevel] = f_f[3];
			}
			else
			{
				// Version 2.0, There is no way f_f will be blocked by haml.
				//f_f[hamlLevel]="0/"+getDenominator(f_f[0]);
				f_f[hamlLevel] = "1/6";
				f_f_note[hamlLevel] = translations[1];
			}

			if (equal(s[1], (s[3])) && equal(s[2], (s[4])))
			{
				if (bigger(s[3], s[4])) s[hamlLevel] = s[4];
				else s[hamlLevel] = s[3];
			}
			else
			{
				s[hamlLevel] = "0/" + getDenominator(s[0]);
				s_note[hamlLevel] = translations[0];
			}

			if (equal(d[1], (d[3])) && equal(d[2], (d[4])))
			{
				if (bigger(d[3], d[4])) d[hamlLevel] = d[4];
				else d[hamlLevel] = d[3];
			}
			else
			{
				d[hamlLevel] = "0/" + getDenominator(d[0]);
				d_note[hamlLevel] = translations[0];
			}

			if (equal(s_s[1], (s_s[3])) && equal(s_s[2], (s_s[4])))
			{
				if (bigger(s_s[3], s_s[4])) s_s[hamlLevel] = s_s[4];
				else s_s[hamlLevel] = s_s[3];
			}
			else
			{
				s_s[hamlLevel] = "0/" + getDenominator(s_s[0]);
				s_s_note[hamlLevel] = translations[0];
			}

			if (equal(d_s[1], (d_s[3])) && equal(d_s[2], (d_s[4])))
			{
				if (bigger(d_s[3], d_s[4])) d_s[hamlLevel] = d_s[4];
				else d_s[hamlLevel] = d_s[3];
			}
			else
			{
				d_s[hamlLevel] = "0/" + getDenominator(d_s[0]);
				d_s_note[hamlLevel] = translations[0];
			}

			if (equal(s_s_s[1], (s_s_s[3])) && equal(s_s_s[2], (s_s_s[4])))
			{
				if (bigger(s_s_s[3], s_s_s[4])) s_s_s[hamlLevel] = s_s_s[4];
				else s_s_s[hamlLevel] = s_s_s[3];
			}
			else
			{
				s_s_s[hamlLevel] = "0/" + getDenominator(s_s_s[0]);
				s_s_s_note[hamlLevel] = translations[0];
			}

			if (equal(d_s_s[1], (d_s_s[3])) && equal(d_s_s[2], (d_s_s[4])))
			{
				if (bigger(d_s_s[3], d_s_s[4])) d_s_s[hamlLevel] = d_s_s[4];
				else d_s_s[hamlLevel] = d_s_s[3];
			}
			else
			{
				d_s_s[hamlLevel] = "0/" + getDenominator(d_s_s[0]);
				d_s_s_note[hamlLevel] = translations[0];
			}

			if (equal(b[1], (b[3])) && equal(b[2], (b[4])))
			{
				if (bigger(b[3], b[4])) b[hamlLevel] = b[4];
				else b[hamlLevel] = b[3];
			}
			else
			{
				b[hamlLevel] = "0/" + getDenominator(b[0]);
				b_note[hamlLevel] = translations[0];
			}

			if (equal(sister[1], (sister[3])) && equal(sister[2], (sister[4])))
			{
				if (bigger(sister[3], sister[4])) sister[hamlLevel] = sister[4];
				else sister[hamlLevel] = sister[3];
			}
			else
			{
				sister[hamlLevel] = "0/" + getDenominator(sister[0]);
				sister_note[hamlLevel] = translations[0];
			}

			if (equal(m_b[1], (m_b[3])) && equal(m_b[2], (m_b[4])))
			{
				if (bigger(m_b[3], m_b[4])) m_b[hamlLevel] = m_b[4];
				else m_b[hamlLevel] = m_b[3];
			}
			else
			{
				m_b[hamlLevel] = "0/" + getDenominator(m_b[0]);
				m_b_note[hamlLevel] = translations[0];
			}

			if (equal(m_sister[1], (m_sister[3])) && equal(m_sister[2], (m_sister[4])))
			{
				if (bigger(m_sister[3], m_sister[4])) m_sister[hamlLevel] = m_sister[4];
				else m_sister[hamlLevel] = m_sister[3];
			}
			else
			{
				m_sister[hamlLevel] = "0/" + getDenominator(m_sister[0]);
				m_sister_note[hamlLevel] = translations[0];
			}

			if (equal(f_b[1], (f_b[3])) && equal(f_b[2], (f_b[4])))
			{
				if (bigger(f_b[3], f_b[4])) f_b[hamlLevel] = f_b[4];
				else f_b[hamlLevel] = f_b[3];
			}
			else
			{
				f_b[hamlLevel] = "0/" + getDenominator(f_b[0]);
				f_b_note[hamlLevel] = translations[0];
			}

			if (equal(f_sister[1], (f_sister[3])) && equal(f_sister[2], (f_sister[4])))
			{
				if (bigger(f_sister[3], f_sister[4])) f_sister[hamlLevel] = f_sister[4];
				else f_sister[hamlLevel] = f_sister[3];
			}
			else
			{
				f_sister[hamlLevel] = "0/" + getDenominator(f_sister[0]);
				f_sister_note[hamlLevel] = translations[0];
			}

			if (equal(s_b[1], (s_b[3])) && equal(s_b[2], (s_b[4])))
			{
				if (bigger(s_b[3], s_b[4])) s_b[hamlLevel] = s_b[4];
				else s_b[hamlLevel] = s_b[3];
			}
			else
			{
				s_b[hamlLevel] = "0/" + getDenominator(s_b[0]);
				s_b_note[hamlLevel] = translations[0];
			}

			if (equal(s_b_f[1], (s_b_f[3])) && equal(s_b_f[2], (s_b_f[4])))
			{
				if (bigger(s_b_f[3], s_b_f[4])) s_b_f[hamlLevel] = s_b_f[4];
				else s_b_f[hamlLevel] = s_b_f[3];
			}
			else
			{
				s_b_f[hamlLevel] = "0/" + getDenominator(s_b_f[0]);
				s_b_f_note[hamlLevel] = translations[0];
			}

			if (equal(u[1], (u[3])) && equal(u[2], (u[4])))
			{
				if (bigger(u[3], u[4])) u[hamlLevel] = u[4];
				else u[hamlLevel] = u[3];
			}
			else
			{
				u[hamlLevel] = "0/" + getDenominator(u[0]);
				u_note[hamlLevel] = translations[0];
			}

			if (equal(u_f[1], (u_f[3])) && equal(u_f[2], (u_f[4])))
			{
				if (bigger(u_f[3], u_f[4])) u_f[hamlLevel] = u_f[4];
				else u_f[hamlLevel] = u_f[3];
			}
			else
			{
				u_f[hamlLevel] = "0/" + getDenominator(u_f[0]);
				u_f_note[hamlLevel] = translations[0];
			}

			if (equal(s_u[1], (s_u[3])) && equal(s_u[2], (s_u[4])))
			{
				if (bigger(s_u[3], s_u[4])) s_u[hamlLevel] = s_u[4];
				else s_u[hamlLevel] = s_u[3];
			}
			else
			{
				s_u[hamlLevel] = "0/" + getDenominator(s_u[0]);
				s_u_note[hamlLevel] = translations[0];
			}

			if (equal(s_u_f[1], (s_u_f[3])) && equal(s_u_f[2], (s_u_f[4])))
			{
				if (bigger(s_u_f[3], s_u_f[4])) s_u_f[hamlLevel] = s_u_f[4];
				else s_u_f[hamlLevel] = s_u_f[3];
			}
			else
			{
				s_u_f[hamlLevel] = "0/" + getDenominator(s_u_f[0]);
				s_u_f_note[hamlLevel] = translations[0];
			}
		}
		else
		{
			if (madaheb == madhabName.MALIKI)
			{
				if (!equal(h[0], (h[1])) || !equal(h[0], (h[2]))) malikiAffectedHaml = true;
				if (!equal(w[0], (w[1])) || !equal(w[0], (w[2]))) malikiAffectedHaml = true;
				if (!equal(f[0], (f[1])) || !equal(f[0], (f[2]))) malikiAffectedHaml = true;
				if (!equal(m[0], (m[1])) || !equal(m[0], (m[2]))) malikiAffectedHaml = true;
				if (!equal(m_m[0], (m_m[1])) || !equal(m_m[0], (m_m[2]))) malikiAffectedHaml = true;
				if (!equal(m_f[0], (m_f[1])) || !equal(m_f[0], (m_f[2]))) malikiAffectedHaml = true;
				if (!equal(m_m_m[0], (m_m_m[1])) || !equal(m_m_m[0], (m_m_m[2]))) malikiAffectedHaml = true;
				if (!equal(m_m_f[0], (m_m_f[1])) || !equal(m_m_f[0], (m_m_f[2]))) malikiAffectedHaml = true;
				if (!equal(m_f_f[0], (m_f_f[1])) || !equal(m_f_f[0], (m_f_f[2]))) malikiAffectedHaml = true;
				if (!equal(f_f[0], (f_f[1])) || !equal(f_f[0], (f_f[2]))) malikiAffectedHaml = true;
				if (!equal(s[0], (s[1])) || !equal(s[0], (s[2]))) malikiAffectedHaml = true;
				if (!equal(d[0], (d[1])) || !equal(d[0], (d[2]))) malikiAffectedHaml = true;
				if (!equal(s_s[0], (s_s[1])) || !equal(s_s[0], (s_s[2]))) malikiAffectedHaml = true;
				if (!equal(d_s[0], (d_s[1])) || !equal(d_s[0], (d_s[2]))) malikiAffectedHaml = true;
				if (!equal(s_s_s[0], (s_s_s[1])) || !equal(s_s_s[0], (s_s_s[2]))) malikiAffectedHaml = true;
				if (!equal(d_s_s[0], (d_s_s[1])) || !equal(d_s_s[0], (d_s_s[2]))) malikiAffectedHaml = true;
				if (!equal(b[0], (b[1])) || !equal(b[0], (b[2]))) malikiAffectedHaml = true;
				if (!equal(sister[0], (sister[1])) || !equal(sister[0], (sister[2]))) malikiAffectedHaml = true;
				if (!equal(m_b[0], (m_b[1])) || !equal(m_b[0], (m_b[2]))) malikiAffectedHaml = true;
				if (!equal(m_sister[0], (m_sister[1])) || !equal(m_sister[0], (m_sister[2]))) malikiAffectedHaml = true;
				if (!equal(f_b[0], (f_b[1])) || !equal(f_b[0], (f_b[2]))) malikiAffectedHaml = true;
				if (!equal(f_sister[0], (f_sister[1])) || !equal(f_sister[0], (f_sister[2]))) malikiAffectedHaml = true;
				if (!equal(s_b[0], (s_b[1])) || !equal(s_b[0], (s_b[2]))) malikiAffectedHaml = true;
				if (!equal(s_b_f[0], (s_b_f[1])) || !equal(s_b_f[0], (s_b_f[2]))) malikiAffectedHaml = true;
				if (!equal(u[0], (u[1])) || !equal(u[0], (u[2]))) malikiAffectedHaml = true;
				if (!equal(u_f[0], (u_f[1])) || !equal(u_f[0], (u_f[2]))) malikiAffectedHaml = true;
				if (!equal(s_u[0], (s_u[1])) || !equal(s_u[0], (s_u[2]))) malikiAffectedHaml = true;
				if (!equal(s_u_f[0], (s_u_f[1])) || !equal(s_u_f[0], (s_u_f[2]))) malikiAffectedHaml = true;
			}
			else
			{
				// Initialy
				h[hamlLevel] = h[0];
				w[hamlLevel] = w[0];
				f[hamlLevel] = f[0];
				m[hamlLevel] = m[0];
				m_m[hamlLevel] = m_m[0];
				m_f[hamlLevel] = m_f[0];
				m_m_m[hamlLevel] = m_m_m[0];
				m_m_f[hamlLevel] = m_m_f[0];
				m_f_f[hamlLevel] = m_f_f[0];
				f_f[hamlLevel] = f_f[0];
				s[hamlLevel] = s[0];
				d[hamlLevel] = d[0];
				s_s[hamlLevel] = s_s[0];
				d_s[hamlLevel] = d_s[0];
				s_s_s[hamlLevel] = s_s_s[0];
				d_s_s[hamlLevel] = d_s_s[0];
				b[hamlLevel] = b[0];
				sister[hamlLevel] = sister[0];
				m_b[hamlLevel] = m_b[0];
				m_sister[hamlLevel] = m_sister[0];
				f_b[hamlLevel] = f_b[0];
				f_sister[hamlLevel] = f_sister[0];
				s_b[hamlLevel] = s_b[0];
				s_b_f[hamlLevel] = s_b_f[0];
				u[hamlLevel] = u[0];
				u_f[hamlLevel] = u_f[0];
				s_u[hamlLevel] = s_u[0];
				s_u_f[hamlLevel] = s_u_f[0];

				for (int i = 1; i < hamlLevel; i++)
				{
					if (bigger(h[hamlLevel], h[i])) h[hamlLevel] = h[i];
					if (bigger(w[hamlLevel], w[i])) w[hamlLevel] = w[i];
					if (bigger(f[hamlLevel], f[i])) f[hamlLevel] = f[i];
					if (bigger(m[hamlLevel], m[i])) m[hamlLevel] = m[i];
					if (bigger(m_m[hamlLevel], m_m[i])) m_m[hamlLevel] = m_m[i];
					if (bigger(m_f[hamlLevel], m_f[i])) m_f[hamlLevel] = m_f[i];
					if (bigger(m_m_m[hamlLevel], m_m_m[i])) m_m_m[hamlLevel] = m_m_m[i];
					if (bigger(m_m_f[hamlLevel], m_m_f[i])) m_m_f[hamlLevel] = m_m_f[i];
					if (bigger(m_f_f[hamlLevel], m_f_f[i])) m_f_f[hamlLevel] = m_f_f[i];
					if (bigger(f_f[hamlLevel], f_f[i])) f_f[hamlLevel] = f_f[i];
					if (bigger(s[hamlLevel], s[i])) s[hamlLevel] = s[i];
					if (bigger(d[hamlLevel], d[i])) d[hamlLevel] = d[i];
					if (bigger(s_s[hamlLevel], s_s[i])) s_s[hamlLevel] = s_s[i];
					if (bigger(d_s[hamlLevel], d_s[i])) d_s[hamlLevel] = d_s[i];
					if (bigger(s_s_s[hamlLevel], s_s_s[i])) s_s_s[hamlLevel] = s_s_s[i];
					if (bigger(d_s_s[hamlLevel], d_s_s[i])) d_s_s[hamlLevel] = d_s_s[i];
					if (bigger(b[hamlLevel], b[i])) b[hamlLevel] = b[i];
					if (bigger(sister[hamlLevel], sister[i])) sister[hamlLevel] = sister[i];
					if (bigger(m_b[hamlLevel], m_b[i])) m_b[hamlLevel] = m_b[i];
					if (bigger(m_sister[hamlLevel], m_sister[i])) m_sister[hamlLevel] = m_sister[i];
					if (bigger(f_b[hamlLevel], f_b[i])) f_b[hamlLevel] = f_b[i];
					if (bigger(f_sister[hamlLevel], f_sister[i])) f_sister[hamlLevel] = f_sister[i];
					if (bigger(s_b[hamlLevel], s_b[i])) s_b[hamlLevel] = s_b[i];
					if (bigger(s_b_f[hamlLevel], s_b_f[i])) s_b_f[hamlLevel] = s_b_f[i];
					if (bigger(u[hamlLevel], u[i])) u[hamlLevel] = u[i];
					if (bigger(u_f[hamlLevel], u_f[i])) u_f[hamlLevel] = u_f[i];
					if (bigger(s_u[hamlLevel], s_u[i])) s_u[hamlLevel] = s_u[i];
					if (bigger(s_u_f[hamlLevel], s_u_f[i])) s_u_f[hamlLevel] = s_u_f[i];

					// Version 1.7, In all cases haml[3|6]=0 since there is a case when he/she is dead.
					//haml[3|6]="0/1"; // by default it is =0
				}
			}
		}
	}

	void khonthaCalculationHelper()
	{
		if (khontha_w == 1)
		{
			if (khontha_p == 1)
			{
				khontha[1] = d[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (d_num[1] == 1) d[1] = "0/1";

				//To return it bach
				d_num[1] = d_num[1] - 1;
			}

			if (khontha_p == 2)
			{
				khontha[1] = s[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (s_num[1] == 1) s[1] = "0/1";
				s_num[1] = s_num[1] - 1;
			}

			if (khontha_p == 3)
			{
				khontha[1] = d_s[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (d_s_num[1] == 1) d_s[1] = "0/1";
				d_s_num[1] = d_s_num[1] - 1;
			}

			if (khontha_p == 4)
			{
				khontha[1] = s_s[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (s_s_num[1] == 1) s_s[1] = "0/1";
				s_s_num[1] = s_s_num[1] - 1;
			}

			if (khontha_p == 5)
			{
				khontha[1] = d_s_s[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (d_s_s_num[1] == 1) d_s_s[1] = "0/1";
				d_s_s_num[1] = d_s_s_num[1] - 1;
			}

			if (khontha_p == 6)
			{
				khontha[1] = s_s_s[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (s_s_s_num[1] == 1) s_s_s[1] = "0/1";
				s_s_s_num[1] = s_s_s_num[1] - 1;
			}

			if (khontha_p == 7)
			{
				khontha[1] = sister[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (sister_num[1] == 1) sister[1] = "0/1";
				sister_num[1] = sister_num[1] - 1;
			}

			if (khontha_p == 8)
			{
				khontha[1] = b[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (b_num[1] == 1) b[1] = "0/1";
				b_num[1] = b_num[1] - 1;
			}

			if (khontha_p == 9)
			{
				khontha[1] = m_sister[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (m_sister_num[1] == 1) m_sister[1] = "0/1";
				m_sister_num[1] = m_sister_num[1] - 1;
			}

			if (khontha_p == 10)
			{
				khontha[1] = m_b[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (m_b_num[1] == 1) m_b[1] = "0/1";
				m_b_num[1] = m_b_num[1] - 1;
			}

			if (khontha_p == 11)
			{
				khontha[1] = f_sister[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (f_sister_num[1] == 1) f_sister[1] = "0/1";
				f_sister_num[1] = f_sister_num[1] - 1;
			}

			if (khontha_p == 12)
			{
				khontha[1] = f_b[1];
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);

				if (f_b_num[1] == 1) f_b[1] = "0/1";
				f_b_num[1] = f_b_num[1] - 1;
			}

			if (khontha_p == 13)
			{
				khontha[1] = "0/1";                        //because no female here
				khontha[2] = divide(add(new String[]{khontha[0], khontha[1]}), 2);
			}

			if (khontha_p == 14) khontha[2] = divide(khontha[0], 2);
			if (khontha_p == 15) khontha[2] = divide(khontha[0], 2);
			if (khontha_p == 16) khontha[2] = divide(khontha[0], 2);
			if (khontha_p == 17) khontha[2] = divide(khontha[0], 2);
			if (khontha_p == 18) khontha[2] = divide(khontha[0], 2);

			//Take the average for all
			h[2] = divide(add(new String[]{h[0], h[1]}), 2);
			w[2] = divide(add(new String[]{w[0], w[1]}), 2);
			f[2] = divide(add(new String[]{f[0], f[1]}), 2);
			m[2] = divide(add(new String[]{m[0], m[1]}), 2);
			m_m[2] = divide(add(new String[]{m_m[0], m_m[1]}), 2);
			m_f[2] = divide(add(new String[]{m_f[0], m_f[1]}), 2);
			m_m_m[2] = divide(add(new String[]{m_m_m[0], m_m_m[1]}), 2);
			m_m_f[2] = divide(add(new String[]{m_m_f[0], m_m_f[1]}), 2);
			m_f_f[2] = divide(add(new String[]{m_f_f[0], m_f_f[1]}), 2);
			f_f[2] = divide(add(new String[]{f_f[0], f_f[1]}), 2);
			s[2] = divide(add(new String[]{s[0], s[1]}), 2);
			d[2] = divide(add(new String[]{d[0], d[1]}), 2);
			s_s[2] = divide(add(new String[]{s_s[0], s_s[1]}), 2);
			d_s[2] = divide(add(new String[]{d_s[0], d_s[1]}), 2);
			s_s_s[2] = divide(add(new String[]{s_s_s[0], s_s_s[1]}), 2);
			d_s_s[2] = divide(add(new String[]{d_s_s[0], d_s_s[1]}), 2);
			b[2] = divide(add(new String[]{b[0], b[1]}), 2);
			sister[2] = divide(add(new String[]{sister[0], sister[1]}), 2);
			m_b[2] = divide(add(new String[]{m_b[0], m_b[1]}), 2);
			m_sister[2] = divide(add(new String[]{m_sister[0], m_sister[1]}), 2);
			f_b[2] = divide(add(new String[]{f_b[0], f_b[1]}), 2);
			f_sister[2] = divide(add(new String[]{f_sister[0], f_sister[1]}), 2);
			s_b[2] = divide(add(new String[]{s_b[0], s_b[1]}), 2);
			s_b_f[2] = divide(add(new String[]{s_b_f[0], s_b_f[1]}), 2);
			u[2] = divide(add(new String[]{u[0], u[1]}), 2);
			u_f[2] = divide(add(new String[]{u_f[0], u_f[1]}), 2);
			s_u[2] = divide(add(new String[]{s_u[0], s_u[1]}), 2);
			s_u_f[2] = divide(add(new String[]{s_u_f[0], s_u_f[1]}), 2);

			// Version 1.3
			muslim_trusts[2] = divide(add(new String[]{muslim_trusts[0], muslim_trusts[1]}), 2);
		}

		if (khontha_w == 2)
		{
			//////////////
			//khontha case
			//////////////

			if (khontha_p == 1)
			{
				khontha[1] = d[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = d[1];

				if (d_num[1] == 1) d[1] = "0/1";

				//To return it bach
				d_num[1] = d_num[1] - 1;
			}

			if (khontha_p == 2)
			{
				khontha[1] = s[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = s[1];

				if (s_num[1] == 1) s[1] = "0/1";
				s_num[1] = s_num[1] - 1;
			}

			if (khontha_p == 3)
			{
				khontha[1] = d_s[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = d_s[1];

				if (d_s_num[1] == 1) d_s[1] = "0/1";
				d_s_num[1] = d_s_num[1] - 1;
			}

			if (khontha_p == 4)
			{
				khontha[1] = s_s[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = s_s[1];

				if (s_s_num[1] == 1) s_s[1] = "0/1";
				s_s_num[1] = s_s_num[1] - 1;
			}

			if (khontha_p == 5)
			{
				khontha[1] = d_s_s[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = d_s_s[1];

				if (d_s_s_num[1] == 1) d_s_s[1] = "0/1";
				d_s_s_num[1] = d_s_s_num[1] - 1;
			}

			if (khontha_p == 6)
			{
				khontha[1] = s_s_s[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = s_s_s[1];

				if (s_s_s_num[1] == 1) s_s_s[1] = "0/1";
				s_s_s_num[1] = s_s_s_num[1] - 1;
			}

			if (khontha_p == 7)
			{
				khontha[1] = sister[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = sister[1];

				if (sister_num[1] == 1) sister[1] = "0/1";
				sister_num[1] = sister_num[1] - 1;
			}

			if (khontha_p == 8)
			{
				khontha[1] = b[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = b[1];

				if (b_num[1] == 1) b[1] = "0/1";
				b_num[1] = b_num[1] - 1;
			}

			if (khontha_p == 9)
			{
				khontha[1] = m_sister[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = m_sister[1];

				if (m_sister_num[1] == 1) m_sister[1] = "0/1";
				m_sister_num[1] = m_sister_num[1] - 1;
			}

			if (khontha_p == 10)
			{
				khontha[1] = m_b[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = m_b[1];

				if (m_b_num[1] == 1)
					m_b[1] = "0/1";
				m_b_num[1] = m_b_num[1] - 1;
			}

			if (khontha_p == 11)
			{
				khontha[1] = f_sister[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = f_sister[1];

				if (f_sister_num[1] == 1)
					f_sister[1] = "0/1";
				f_sister_num[1] = f_sister_num[1] - 1;
			}

			if (khontha_p == 12)
			{
				khontha[1] = f_b[1];

				if (less(khontha[0], khontha[1]))
					khontha[2] = khontha[0];
				else
					khontha[2] = f_b[1];

				if (f_b_num[1] == 1) f_b[1] = "0/1";
				f_b_num[1] = f_b_num[1] - 1;
			}

			//We cancelled checking khonthe[1] & khonthe[2] when khontha_p = 13........18 because they are always =0
			//For all inheritors
			if (less(h[0], h[1]))
				h[2] = h[0];
			else
				h[2] = h[1];

			if (less(w[0], w[1]))
				w[2] = w[0];
			else
				w[2] = w[1];

			if (less(f[0], f[1]))
				f[2] = f[0];
			else
				f[2] = f[1];

			if (less(m[0], m[1]))
				m[2] = m[0];
			else
				m[2] = m[1];

			if (less(m_m[0], m_m[1]))
				m_m[2] = m_m[0];
			else
				m_m[2] = m_m[1];

			if (less(m_f[0], m_f[1]))
				m_f[2] = m_f[0];
			else
				m_f[2] = m_f[1];

			if (less(m_m_m[0], m_m_m[1]))
				m_m_m[2] = m_m_m[0];
			else
				m_m_m[2] = m_m_m[1];

			if (less(m_m_f[0], m_m_f[1]))
				m_m_f[2] = m_m_f[0];
			else
				m_m_f[2] = m_m_f[1];

			if (less(m_f_f[0], m_f_f[1]))
				m_f_f[2] = m_f_f[0];
			else
				m_f_f[2] = m_f_f[1];

			if (less(f_f[0], f_f[1]))
				f_f[2] = f_f[0];
			else
				f_f[2] = f_f[1];

			if (less(s[0], s[1]))
				s[2] = s[0];
			else
				s[2] = s[1];

			if (less(d[0], d[1]))
				d[2] = d[0];
			else
				d[2] = d[1];

			if (less(s_s[0], s_s[1]))
				s_s[2] = s_s[0];
			else
				s_s[2] = s_s[1];

			if (less(d_s[0], d_s[1]))
				d_s[2] = d_s[0];
			else
				d_s[2] = d_s[1];

			if (less(s_s_s[0], s_s_s[1]))
				s_s_s[2] = s_s_s[0];
			else
				s_s_s[2] = s_s_s[1];

			if (less(d_s_s[0], d_s_s[1]))
				d_s_s[2] = d_s_s[0];
			else
				d_s_s[2] = d_s_s[1];

			if (less(b[0], b[1]))
				b[2] = b[0];
			else
				b[2] = b[1];

			if (less(sister[0], sister[1]))
				sister[2] = sister[0];
			else
				sister[2] = sister[1];

			if (less(m_b[0], m_b[1]))
				m_b[2] = m_b[0];
			else
				m_b[2] = m_b[1];

			if (less(m_sister[0], m_sister[1]))
				m_sister[2] = m_sister[0];
			else
				m_sister[2] = m_sister[1];

			if (less(f_b[0], f_b[1]))
				f_b[2] = f_b[0];
			else
				f_b[2] = f_b[1];

			if (less(f_sister[0], f_sister[1]))
				f_sister[2] = f_sister[0];
			else
				f_sister[2] = f_sister[1];

			if (less(s_b[0], s_b[1]))
				s_b[2] = s_b[0];
			else
				s_b[2] = s_b[1];

			if (less(s_b_f[0], s_b_f[1]))
				s_b_f[2] = s_b_f[0];
			else
				s_b_f[2] = s_b_f[1];

			if (less(u[0], u[1]))
				u[2] = u[0];
			else
				u[2] = u[1];

			if (less(u_f[0], u_f[1]))
				u_f[2] = u_f[0];
			else
				u_f[2] = u_f[1];

			if (less(s_u[0], s_u[1]))
				s_u[2] = s_u[0];
			else
				s_u[2] = s_u[1];

			if (less(s_u_f[0], s_u_f[1]))
				s_u_f[2] = s_u_f[0];
			else
				s_u_f[2] = s_u_f[1];
		}

		//Initilise _num[2] for all (i.e. monasakha & khontha)
		h_num[2] = h_num[1];
		w_num[2] = w_num[1];
		f_num[2] = f_num[1];
		m_num[2] = m_num[1];
		m_m_num[2] = m_m_num[1];
		m_f_num[2] = m_f_num[1];
		m_m_m_num[2] = m_m_m_num[1];
		m_m_f_num[2] = m_m_f_num[1];
		m_f_f_num[2] = m_f_f_num[1];
		f_f_num[2] = f_f_num[1];
		s_num[2] = s_num[1];
		d_num[2] = d_num[1];
		s_s_num[2] = s_s_num[1];
		d_s_num[2] = d_s_num[1];
		s_s_s_num[2] = s_s_s_num[1];
		d_s_s_num[2] = d_s_s_num[1];
		b_num[2] = b_num[1];
		sister_num[2] = sister_num[1];
		m_b_num[2] = m_b_num[1];
		m_sister_num[2] = m_sister_num[1];
		f_b_num[2] = f_b_num[1];
		f_sister_num[2] = f_sister_num[1];
		s_b_num[2] = s_b_num[1];
		s_b_f_num[2] = s_b_f_num[1];
		u_num[2] = u_num[1];
		u_f_num[2] = u_f_num[1];
		s_u_num[2] = s_u_num[1];
		s_u_f_num[2] = s_u_f_num[1];
	}

	void missingCalculationHelper()
	{
		//No comparsion because missing[2] & missing[1] are always =0
		//For all inheritors
		if (less(h[0], h[1]))
			h[2] = h[0];
		else
			h[2] = h[1];

		if (less(w[0], w[1]))
			w[2] = w[0];
		else
			w[2] = w[1];

		if (less(f[0], f[1]))
			f[2] = f[0];
		else
			f[2] = f[1];

		if (less(m[0], m[1]))
			m[2] = m[0];
		else
			m[2] = m[1];

		if (less(m_m[0], m_m[1]))
			m_m[2] = m_m[0];
		else
			m_m[2] = m_m[1];

		if (less(m_f[0], m_f[1]))
			m_f[2] = m_f[0];
		else
			m_f[2] = m_f[1];

		if (less(m_m_m[0], m_m_m[1]))
			m_m_m[2] = m_m_m[0];
		else
			m_m_m[2] = m_m_m[1];

		if (less(m_m_f[0], m_m_f[1]))
			m_m_f[2] = m_m_f[0];
		else
			m_m_f[2] = m_m_f[1];

		if (less(m_f_f[0], m_f_f[1]))
			m_f_f[2] = m_f_f[0];
		else
			m_f_f[2] = m_f_f[1];

		if (less(f_f[0], f_f[1]))
			f_f[2] = f_f[0];
		else
			f_f[2] = f_f[1];

		if (less(s[0], s[1]))
			s[2] = s[0];
		else
			s[2] = s[1];

		if (less(d[0], d[1]))
			d[2] = d[0];
		else
			d[2] = d[1];

		if (less(s_s[0], s_s[1]))
			s_s[2] = s_s[0];
		else
			s_s[2] = s_s[1];

		if (less(d_s[0], d_s[1]))
			d_s[2] = d_s[0];
		else
			d_s[2] = d_s[1];

		if (less(s_s_s[0], s_s_s[1]))
			s_s_s[2] = s_s_s[0];
		else
			s_s_s[2] = s_s_s[1];

		if (less(d_s_s[0], d_s_s[1]))
			d_s_s[2] = d_s_s[0];
		else
			d_s_s[2] = d_s_s[1];

		if (less(b[0], b[1]))
			b[2] = b[0];
		else
			b[2] = b[1];

		if (less(sister[0], sister[1]))
			sister[2] = sister[0];
		else
			sister[2] = sister[1];

		if (less(m_b[0], m_b[1]))
			m_b[2] = m_b[0];
		else
			m_b[2] = m_b[1];

		if (less(m_sister[0], m_sister[1]))
			m_sister[2] = m_sister[0];
		else
			m_sister[2] = m_sister[1];

		if (less(f_b[0], f_b[1]))
			f_b[2] = f_b[0];
		else
			f_b[2] = f_b[1];

		if (less(f_sister[0], f_sister[1]))
			f_sister[2] = f_sister[0];
		else
			f_sister[2] = f_sister[1];

		if (less(s_b[0], s_b[1]))
			s_b[2] = s_b[0];
		else
			s_b[2] = s_b[1];

		if (less(s_b_f[0], s_b_f[1]))
			s_b_f[2] = s_b_f[0];
		else
			s_b_f[2] = s_b_f[1];

		if (less(u[0], u[1]))
			u[2] = u[0];
		else
			u[2] = u[1];

		if (less(u_f[0], u_f[1]))
			u_f[2] = u_f[0];
		else
			u_f[2] = u_f[1];

		if (less(s_u[0], s_u[1]))
			s_u[2] = s_u[0];
		else
			s_u[2] = s_u[1];

		if (less(s_u_f[0], s_u_f[1]))
			s_u_f[2] = s_u_f[0];
		else
			s_u_f[2] = s_u_f[1];

		//Initilise _num[2] for all
		h_num[2] = h_num[1];
		w_num[2] = w_num[1];
		f_num[2] = f_num[1];
		m_num[2] = m_num[1];
		m_m_num[2] = m_m_num[1];
		m_f_num[2] = m_f_num[1];
		m_m_m_num[2] = m_m_m_num[1];
		m_m_f_num[2] = m_m_f_num[1];
		m_f_f_num[2] = m_f_f_num[1];
		f_f_num[2] = f_f_num[1];
		s_num[2] = s_num[1];
		d_num[2] = d_num[1];
		s_s_num[2] = s_s_num[1];
		d_s_num[2] = d_s_num[1];
		s_s_s_num[2] = s_s_s_num[1];
		d_s_s_num[2] = d_s_s_num[1];
		b_num[2] = b_num[1];
		sister_num[2] = sister_num[1];
		m_b_num[2] = m_b_num[1];
		m_sister_num[2] = m_sister_num[1];
		f_b_num[2] = f_b_num[1];
		f_sister_num[2] = f_sister_num[1];
		s_b_num[2] = s_b_num[1];
		s_b_f_num[2] = s_b_f_num[1];
		u_num[2] = u_num[1];
		u_f_num[2] = u_f_num[1];
		s_u_num[2] = s_u_num[1];
		s_u_f_num[2] = s_u_f_num[1];
	}

	void inheritorsCalculation()
	{
		final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "inheritorsCalculationArabic.txt" : "inheritorsCalculationEnglish.txt"));

		// Version 1.3, This initialization prevents a lot of problems.
		String taaseeb = "0/1";
		String best = "0/1";

		// Version 1.7, To get the base of the question when f_f with brothers
		boolean farad = false;
		boolean taseeb = false;
		int f_f_base = 1;

		//father cases
		if (f_num[cnt] == 1)
		{
			if (s_num[cnt] > 0 || d_num[cnt] > 0 || s_s_num[cnt] > 0 || d_s_num[cnt] > 0 || s_s_s_num[cnt] > 0 || d_s_s_num[cnt] > 0)
			{
				f[cnt] = "1/6";
				f_note[cnt] = translations[0];
			}
		}

		//father_father case when he must has 1/6 at least
		if (f_f_num[cnt] == 1 && f_num[cnt] == 0)
		{
			if (s_num[cnt] > 0 || s_s_num[cnt] > 0 || s_s_s_num[cnt] > 0 || d_num[cnt] > 0 || d_s_num[cnt] > 0 || d_s_s_num[cnt] > 0)
			{
				f_f[cnt] = "1/6";
				f_f_note[cnt] = translations[1];

				f_f_base = 6;
				//farad = true; delayed to the end to not confuse with f_f with brothers cases.
			}
		}

		//mother cases
		if (m_num[cnt] > 0)
		{
			if ((b_num[cnt] + f_b_num[cnt] + m_b_num[cnt] + sister_num[cnt] + f_sister_num[cnt] + m_sister_num[cnt]) >= 2 || s_num[cnt] > 0
					|| s_s_num[cnt] > 0 || s_s_s_num[cnt] > 0 || d_num[cnt] > 0 || d_s_num[cnt] > 0 || d_s_s_num[cnt] > 0)
			{
				m[cnt] = "1/6";
				m_note[cnt] = translations[2];
			}
			else
			{
				m[cnt] = "1/3";
				m_note[cnt] = translations[3];
			}
		}

		// Grand mother cases
		if (m_num[cnt] == 0)
		{
			if (f_num[cnt] == 0)
			{
				if ((m_f_num[cnt] + m_m_num[cnt]) > 0)
				{
					if (m_f_num[cnt] != 0)
					{
						m_f[cnt] = "1/" + (6 * (m_m_num[cnt] + m_f_num[cnt]));
						m_f_note[cnt] = translations[4];
					}

					if (m_m_num[cnt] != 0)
					{
						m_m[cnt] = "1/" + (6 * (m_m_num[cnt] + m_f_num[cnt]));
						m_m_note[cnt] = translations[5];
					}
				}
			}
			else
			{
				if (madaheb == madhabName.HANBALI /* Version 1.8, Removed: || madaheb==madhabName.HANAFI*/)
				{
					if (m_f_num[cnt] != 0)
					{
						m_f[cnt] = "1/" + (6 * (m_m_num[cnt] + m_f_num[cnt]));
						m_f_note[cnt] = translations[6];
					}

					if (m_m_num[cnt] != 0)
					{
						m_m[cnt] = "1/" + (6 * (m_m_num[cnt] + m_f_num[cnt]));
						m_m_note[cnt] = translations[7];
					}
				}
				else
					if (m_m_num[cnt] != 0)
					{
						m_m[cnt] = "1/6";
						m_m_note[cnt] = translations[8];
					}
			}
		}

		// Grand grand mother cases. GUMHOUR is the same as SHAFEE in these cases
		// Version 1.8, a lot of changes in these cases, so check it with version 1.7 when needed.
		if (m_m_num[cnt] == 0 && m_num[cnt] == 0)
		{
			if (f_f_num[cnt] == 0 && f_num[cnt] == 0)
			{
				if (m_f_num[cnt] == 1)
				{
					if (madaheb != madhabName.HANBALI && madaheb != madhabName.HANAFI)
					{
						if (m_m_m_num[cnt] != 0)
						{
							m_m_m[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_f_num[cnt]));
							m_m_m_note[cnt] = translations[9];
						}

						m_f[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_f_num[cnt]));
						m_f_note[cnt] = translations[10];
					}
				}
				else
				{
					if (madaheb == madhabName.MALIKI)
					{
						if (m_m_m_num[cnt] != 0)
						{
							m_m_m[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_m_f_num[cnt])); // No m_f_f_num[cnt] since Maliki only have m*_f and m*_m
							m_m_m_note[cnt] = translations[13];
						}

						if (m_m_f_num[cnt] != 0)
						{
							m_m_f[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_f_note[cnt] = translations[16];
						}
					}
					else
					{
						if (m_m_m_num[cnt] != 0)
						{
							m_m_m[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_m_note[cnt] = translations[14];
						}

						if (m_f_f_num[cnt] != 0)
						{
							m_f_f[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_f_f_note[cnt] = translations[15];
						}

						if (m_m_f_num[cnt] != 0)
						{
							m_m_f[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_f_note[cnt] = translations[17];
						}
					}
				}
			}
			else
			{
				if (madaheb == madhabName.HANBALI)
				{
					if (m_f_num[cnt] == 0)
					{
						if (m_m_m_num[cnt] != 0)
						{
							m_m_m[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_m_note[cnt] = translations[18];
						}

						if (m_f_f_num[cnt] != 0)
						{
							m_f_f[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_f_f_note[cnt] = translations[19];
						}

						if (m_m_f_num[cnt] != 0)
						{
							m_m_f[cnt] = "1/" + (6 * (m_f_f_num[cnt] + m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_f_note[cnt] = translations[20];
						}
					}
				}
				else
				{
					if (m_f_num[cnt] == 0)
					{
						if (m_m_m_num[cnt] != 0)
						{
							if (f_num[cnt] == 0)
							{
								m_m_m[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_m_f_num[cnt]));
								m_m_m_note[cnt] = translations[21];
							}
							else
							{
								m_m_m[cnt] = "1/6";
								m_m_m_note[cnt] = translations[11];
							}
						}

						if (m_m_f_num[cnt] != 0 && f_num[cnt] == 0)
						{
							m_m_f[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_m_f_num[cnt]));
							m_m_f_note[cnt] = translations[22];
						}
					}
					else
					{
						if (madaheb != madhabName.HANAFI)
						{
							if (m_m_m_num[cnt] != 0)
							{
								if (f_num[cnt] == 0)
								{
									m_m_m[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_f_num[cnt]));
									m_m_m_note[cnt] = translations[12];
								}
								else
								{
									m_m_m[cnt] = "1/6";
									m_m_m_note[cnt] = translations[48];
								}
							}

							if (m_f_num[cnt] != 0 && f_num[cnt] == 0)
							{
								m_f[cnt] = "1/" + (6 * (m_m_m_num[cnt] + m_f_num[cnt]));
								m_f_note[cnt] = translations[137];
							}
						}
					}
				}
			}
		}

		// mother_brother & sister
		if ((m_b_num[cnt] > 0 || m_sister_num[cnt] > 0) && f_num[cnt] == 0 && f_f_num[cnt] == 0 && s_s_num[cnt] == 0 && s_num[cnt] == 0
				&& d_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
		{
			if ((m_b_num[cnt] + m_sister_num[cnt]) >= 2)
			{
				if (m_b_num[cnt] != 0)
				{
					m_b[cnt] = "1/" + (3 * (m_b_num[cnt] + m_sister_num[cnt]));
					m_b_note[cnt] = translations[23];
				}

				if (m_sister_num[cnt] != 0)
				{
					m_sister[cnt] = "1/" + (3 * (m_b_num[cnt] + m_sister_num[cnt]));
					m_sister_note[cnt] = translations[24];
				}
			}
			else
			{
				if ((m_b_num[cnt] + m_sister_num[cnt]) == 1)
				{
					if (m_b_num[cnt] != 0)
					{
						m_b[cnt] = "1/6";
						m_b_note[cnt] = translations[25];
					}

					if (m_sister_num[cnt] != 0)
					{
						m_sister[cnt] = "1/6";
						m_sister_note[cnt] = translations[26];
					}
				}
			}
		}

		// Husband cases
		if (h_num[cnt] == 1)
		{
			if (d_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
			{
				h[cnt] = "1/2";
				h_note[cnt] = translations[27];
			}
			else
			{
				h[cnt] = "1/4";
				h_note[cnt] = translations[28];
			}
		}

		// Wife cases
		if (w_num[cnt] >= 1)
		{
			if (d_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
			{
				w[cnt] = "1/4";
				w_note[cnt] = translations[29];
			}
			else
			{
				w[cnt] = "1/8";
				w_note[cnt] = translations[30];
			}

			w[cnt] = divide(w[cnt], w_num[cnt]);
		}

		// Daughter cases
		if (d_num[cnt] > 0 /* Version 1.6 */ && s_num[cnt] == 0)
		{
			if (d_num[cnt] == 1 /* Version 1.6, Removing: && s_num[cnt]==0 */)
			{
				d[cnt] = "1/2";
				d_note[cnt] = translations[31];
			}
			else
				if (d_num[cnt] >= 2 /* Version 1.6, Removing: && s_num[cnt]==0 */)
				{
					// Version 1.6, Moving here.
					d[cnt] = divide("2/3", d_num[cnt]);

					d_note[cnt] = translations[32];
				}
		}

		// daughter_son cases
		if (s_num[cnt] == 0)
		{
			if (s_s_num[cnt] == 0)
			{
				if (d_num[cnt] == 0)
				{
					if (d_s_num[cnt] == 1)
					{
						d_s[cnt] = "1/2";
						d_s_note[cnt] = translations[33];
					}

					if (d_s_num[cnt] >= 2)
					{
						d_s[cnt] = divide("2/3", d_s_num[cnt]);
						d_s_note[cnt] = translations[34];
					}
				}

				if (d_num[cnt] == 1)
				{
					// Version 1.6, removing Version 1.3. Fatal Error !
					if (d_s_num[cnt] >= 1 /* Version 1.3 && s_s_s_num[cnt]==0 */)
					{
						d_s[cnt] = divide("1/6", d_s_num[cnt]);
						d_s_note[cnt] = translations[35];
					}
				}
			}
		}

		// daughter_son_son cases
		if (s_num[cnt] == 0)
		{
			if (s_s_num[cnt] == 0)
			{
				if (s_s_s_num[cnt] == 0)
				{
					if (d_num[cnt] == 0 && d_s_num[cnt] == 0)
					{
						if (d_s_s_num[cnt] == 1)
						{
							d_s_s[cnt] = "1/2";
							d_s_s_note[cnt] = translations[36];
						}

						if (d_s_s_num[cnt] >= 2)
						{
							d_s_s[cnt] = divide("2/3", d_s_s_num[cnt]);
							d_s_s_note[cnt] = translations[37];
						}
					}

					// Version 1.3, This condition was not under if(s_s_s_num[cnt]==0)
					if ((d_num[cnt] == 1 && d_s_num[cnt] == 0) || (d_num[cnt] == 0 && d_s_num[cnt] == 1))
					{
						if (d_s_s_num[cnt] >= 1)
						{
							d_s_s[cnt] = divide("1/6", d_s_s_num[cnt]);
							d_s_s_note[cnt] = translations[38];
						}
					}
				}
			}
		}

		//sister cases
		//Dont forget to see sisters in father_father (sister*sister_num) because taaseeb
		if (sister_num[cnt] > 0)
		{
			if (s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 && f_f_num[cnt] == 0)
			{
				if (d_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
				{
					if (b_num[cnt] == 0)
					{
						if (sister_num[cnt] == 1)
						{
							sister[cnt] = "1/2";
							sister_note[cnt] = translations[39];
						}

						if (sister_num[cnt] >= 2)
						{
							sister[cnt] = "2/3";
							sister_note[cnt] = translations[40];
							sister[cnt] = divide(sister[cnt], sister_num[cnt]);
						}
					}
				}
				else
				{
					/*
					 * Version 1.6
					 *
					 * Replace this:
					 * ( d_num[cnt]>0 || (d_num[cnt]==0 && d_s_num[cnt]>0) || (d_num[cnt]==0 && d_s_num[cnt]==0 && d_s_s_num[cnt]>0))
					 *
					 * with this:
					 * (d_num[cnt]>0 || d_s_num[cnt]>0 || d_s_s_num[cnt]>0)
					 */
					if ((d_num[cnt] > 0 || d_s_num[cnt] > 0 || d_s_s_num[cnt] > 0) && b_num[cnt] == 0)
					{
						// Version 1.3, No need to add multiply(b[cnt], b_num[cnt]) since b_num[cnt]==0 is mentioned above
						taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), multiply(w[cnt], w_num[cnt]), m[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt],
								m_m_f[cnt], m_f_f[cnt], multiply(d[cnt], d_num[cnt]), multiply(d_s[cnt], d_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt]),
								multiply(m_b[cnt], m_b_num[cnt]), multiply(m_sister[cnt], m_sister_num[cnt])});

						if (less(taaseeb, "1/1"))
						{
							sister[cnt] = subtract(1, taaseeb);
							sister[cnt] = divide(sister[cnt], sister_num[cnt]);
							sister_note[cnt] = translations[41];
						}
						/* Version 1.6, Removed
						else
						{
							sister[cnt]="1/6";
							sister_note[cnt] = translations[42];
							sister[cnt]=divide(sister[cnt], sister_num[cnt]);
						}
						*/

						// She will act(i.e. the sister) as brother(b)
						f_b_num[cnt] = 0;
						f_sister_num[cnt] = 0;
					}
				}
			}
		}

		//father_sisters cases
		if (f_sister_num[cnt] > 0)
			if (s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && b_num[cnt] == 0 && f_num[cnt] == 0 && f_f_num[cnt] == 0 && f_b_num[cnt] == 0)
			{
				/*
				 * Version 1.3
				 * No need to add multiply(b[cnt], b_num[cnt]) since b_num[cnt]==0 is mentioned above
				 */
				taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), multiply(w[cnt], w_num[cnt]), m[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt], m_m_f[cnt], m_f_f[cnt],
						multiply(d[cnt], d_num[cnt]), multiply(d_s[cnt], d_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt]), multiply(m_b[cnt], m_b_num[cnt]),
						multiply(m_sister[cnt], m_sister_num[cnt]), /* Version 1.7, Removed f[cnt], f_f[cnt] */ multiply(sister[cnt], sister_num[cnt])});

				if (d_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
				{
					//if(f_b_num[cnt]==0)/* Version 1.3, No need since this condition is mentioned above */
					//{
					if (sister_num[cnt] == 0)
					{
						if (f_sister_num[cnt] == 1)
						{
							f_sister[cnt] = "1/2";
							f_sister_note[cnt] = translations[43];
						}
						else
						{
							f_sister[cnt] = "2/3";
							f_sister_note[cnt] = translations[44];
							f_sister[cnt] = divide(f_sister[cnt], f_sister_num[cnt]);
						}
					}
					else
					{
						if (sister_num[cnt] == 1)
						{
							f_sister[cnt] = "1/6";
							f_sister_note[cnt] = translations[45];
							f_sister[cnt] = divide(f_sister[cnt], f_sister_num[cnt]);
						}
					}
					//}
				}
				else
				{
					/*
					 * Version 1.6
					 *
					 * Replace this:
					 * ( d_num[cnt]>0 || (d_num[cnt]==0 && d_s_num[cnt]>0) || (d_num[cnt]==0 && d_s_num[cnt]==0 && d_s_s_num[cnt]>0))
					 *
					 * with this:
					 * (d_num[cnt]>0 || d_s_num[cnt]>0 || d_s_s_num[cnt]>0)
					 */
					if (d_num[cnt] > 0 || d_s_num[cnt] > 0 || d_s_s_num[cnt] > 0) /* && f_b_num[cnt]==0,  Version 1.3, No need since this condition is mentioned above */
					{
						if (sister_num[cnt] == 0)
						{
							if (less(taaseeb, "1/1"))
							{
								f_sister[cnt] = subtract(1, taaseeb);
								f_sister_note[cnt] = translations[46];
								f_sister[cnt] = divide(f_sister[cnt], f_sister_num[cnt]);
							}
							/* Version 1.6, Removed
							else
							{
								f_sister[cnt]="1/6";
								f_sister_note[cnt] = translations[47];
								f_sister[cnt]=divide(f_sister[cnt], f_sister_num[cnt]);
							}
							*/
						}
					}
				}
			}

		// father_father cases
		// Version 1.7, Hanafi madhab is removed since it is the default already done before.		/* , No need since it is calculating at the begining as it is the standard.
		if (madaheb != madhabName.HANAFI)
		{
			if (f_f_num[cnt] == 1 && f_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && (sister_num[cnt] != 1 || (sister_num[cnt] == 1 && b_num[cnt] > 0) || ((sister_num[cnt] == 1 || f_sister_num[cnt] == 1/* Version 1.3, check it was: f_sister[cnt].equals("1/1")*/) && b_num[cnt] == 0 && (d_num[cnt] > 0 || d_s_num[cnt] > 0/* Version 1.3, check it was: d_s_num[cnt]==0 */ || d_s_s_num[cnt] > 0))))
			{
				taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), multiply(w[cnt], w_num[cnt]), m[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt], m_m_f[cnt], m_f_f[cnt], multiply(d[cnt], d_num[cnt]), multiply(d_s[cnt], d_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt])});

				if (d_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
					best = divide(subtract(1, taaseeb), 3); // For inisitialising only not real in all cases

				if (b_num[cnt] > 0 || sister_num[cnt] > 0 || f_b_num[cnt] > 0 || f_sister_num[cnt] > 0)
				{
					if ((b_num[cnt] > 0 || sister_num[cnt] > 0) && f_b_num[cnt] == 0 && f_sister_num[cnt] == 0)
					{
						f_f[cnt] = multiply(2, (divide(subtract(1, taaseeb), (2 * b_num[cnt] + sister_num[cnt] + 2 * f_f_num[cnt]))));
						f_f_note[cnt] = translations[49];
						f_f_base = 1; // To remove the effect of the original f_f_base

						// TO DO: let it if(less(f_f[cnt], best) || f_f[cnt].equals(best))
						// This is better for the f_f_base since f_f will be farad at that time not taseeb although he will get the same.
						if (less(f_f[cnt], best))
						{
							f_f[cnt] = best;
							f_f_note[cnt] = translations[50];

							farad = true;
							f_f_base = getDenominator(f_f[cnt]); // 1/3 of the rest which farad. p146 'alkolasah ... algamedi book'
						}

						if (!taaseeb.equals("0/1")) //i.e. there is FARATH
							if (less(f_f[cnt], "1/6"))
							{
								f_f[cnt] = "1/6";
								f_f_note[cnt] = translations[51];

								farad = true;
								f_f_base = 6;
							}

						if (b_num[cnt] > 0)
						{
							b[cnt] = multiply(2, divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * b_num[cnt] + sister_num[cnt])));
							b_note[cnt] = translations[52];
						}
						//Dont forget that b[cnt] can be (-ve) if f_f[cnt] =1/6 & taaseeb > 5/6 (i.e. awle)
						//see page 105 , saboony

						if (less(b[cnt], "0/1"))
							b[cnt] = "0/1"; //For the reason above

						if (sister_num[cnt] > 0)
						{
							sister[cnt] = divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * b_num[cnt] + sister_num[cnt]));
							sister_note[cnt] = translations[53];
						}

						if (less(sister[cnt], "0/1"))
							sister[cnt] = "0/1"; //For the reason above
					}
					else
					{
						if ((f_b_num[cnt] > 0 || f_sister_num[cnt] > 0) && b_num[cnt] == 0 && sister_num[cnt] == 0)
						{
							f_f[cnt] = multiply(2, (divide(subtract(1, taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt]))));
							f_f_note[cnt] = translations[54];
							f_f_base = 1;

							if (less(f_f[cnt], best))
							{
								f_f[cnt] = best;
								f_f_note[cnt] = translations[55];

								farad = true;
								f_f_base = getDenominator(f_f[cnt]);
							}

							if (!taaseeb.equals("0/1")) //i.e. there is FARATH
							{
								if (less(f_f[cnt], "1/6"))
								{
									f_f[cnt] = "1/6";
									f_f_note[cnt] = translations[56];

									farad = true;
									f_f_base = 6;
								}
							}

							if (f_b_num[cnt] > 0)
							{
								f_b[cnt] = multiply(2, divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt])));
								f_b_note[cnt] = translations[57];
							}

							if (less(f_b[cnt], "0/1"))
								f_b[cnt] = "0/1";

							if (f_sister_num[cnt] > 0)
							{
								f_sister[cnt] = divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt]));
								f_sister_note[cnt] = translations[58];
							}

							if (less(f_sister[cnt], "0/1"))
								f_sister[cnt] = "0/1";
						}
						else
						{
							//father_fahter with both b[cnt] & f_b[cnt] & sister[cnt] &f_sister[cnt]
							f_f[cnt] = multiply(2, divide(subtract(1, taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt] + 2 * b_num[cnt] + sister_num[cnt])));
							f_f_note[cnt] = translations[59];
							f_f_base = 1;

							if (less(f_f[cnt], best))
							{
								f_f[cnt] = best;
								f_f_note[cnt] = translations[60];

								farad = true;
								f_f_base = getDenominator(f_f[cnt]);
							}

							if (!taaseeb.equals("0/1")) //i.e. there is FARATH
							{
								if (less(f_f[cnt], "1/6"))
								{
									f_f[cnt] = "1/6";
									f_f_note[cnt] = translations[61];

									farad = true;
									f_f_base = 6;
								}
							}

							if (b_num[cnt] > 0)
							{
								b[cnt] = multiply(2, divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * b_num[cnt] + sister_num[cnt])));
								b_note[cnt] = translations[62];
							}

							if (less(b[cnt], "0/1"))
								b[cnt] = "0/1";

							if (sister_num[cnt] > 0)
							{
								sister[cnt] = divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * b_num[cnt] + sister_num[cnt]));
								sister_note[cnt] = translations[63];
							}

							if (less(sister[cnt], "0/1"))
								sister[cnt] = "0/1";

							if (f_b_num[cnt] > 0)
								f_b[cnt] = multiply(2, divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * b_num[cnt] + sister_num[cnt])));

							if (less(f_b[cnt], "0/1"))
								f_b[cnt] = "0/1";

							if (f_sister_num[cnt] > 0)
								f_sister[cnt] = divide((subtract(subtract(1, taaseeb), f_f[cnt])), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * b_num[cnt] + sister_num[cnt]));

							if (less(f_sister[cnt], "0/1"))
								f_sister[cnt] = "0/1";

							if (b_num[cnt] > 0)
								b[cnt] = add(new String[]{b[cnt], multiply(2, divide(add(new String[]{multiply(f_b[cnt], f_b_num[cnt]), multiply(f_sister[cnt], f_sister_num[cnt])}), (2 * b_num[cnt] + sister_num[cnt])))});

							if (sister_num[cnt] > 0)
								sister[cnt] = add(new String[]{sister[cnt], divide(add(new String[]{multiply(f_b[cnt], f_b_num[cnt]), multiply(f_sister[cnt], f_sister_num[cnt])}), (2 * b_num[cnt] + sister_num[cnt]))});

							f_b[cnt] = "0/1";
							f_sister[cnt] = "0/1";
						}
					}
				}
			}
			else
			{
				if (f_f_num[cnt] == 1 && f_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && sister_num[cnt] == 1 && b_num[cnt] == 0)
				{
					// Version 1.4, Remove this code since it causes a lot of problems.
					//you can do it above but to make it clear
					//sister[cnt]="1/2";
					//sister_note[cnt]=translations[64];

					//when NO FARATH i.e. father_father has a chance of 1/3 of all Tarekah
					if (h_num[cnt] == 0 && w_num[cnt] == 0 && m_num[cnt] == 0 && m_m_num[cnt] == 0 && m_f_num[cnt] == 0 && m_m_m_num[cnt] == 0 && m_m_f_num[cnt] == 0 && m_f_f_num[cnt] == 0 && d_num[cnt] == 0 && d_s_num[cnt] == 0 && d_s_s_num[cnt] == 0)
					{
						best = "1/3"; //For inisitialising only not real in all cases

						if (f_b_num[cnt] == 0 && f_sister_num[cnt] == 0)
						{
							/*
							 * these code lines are equal sine taseeb is 1 i.e. no FARATH.
							 * f_f[cnt]=multiply(2, "1/"+String.valueOf(2*f_f_num[cnt]+sister_num[cnt]));
							 * f_f[cnt]=multiply(2, (divide(subtract(1, taaseeb), (2*f_f_num[cnt]+sister_num[cnt]))));
							 */
							f_f[cnt] = multiply(2, "1/" + (2 * f_f_num[cnt] + sister_num[cnt]));
							f_f_note[cnt] = translations[65];
							f_f_base = 1;

							if (less(f_f[cnt], best))
							{
								f_f[cnt] = best;
								f_f_note[cnt] = translations[66];

								farad = true;
								f_f_base = 3;
							}
							/*
							 * Version 1.4
							 * sister should be assigned as half of the f_f.
							 * BUGES if not present: try with:
							 * sister, f_f.
							 */
							//else // In all cases, because we remove sister[cnt] = 1/2 at the begining.
							{
								sister[cnt] = divide(subtract(1, taaseeb), (2 * f_f_num[cnt] + sister_num[cnt]));
								sister_note[cnt] = translations[131];
							}
						}
						else
						{
							// Version 1.3, No need for this condition.
							//if((f_b_num[cnt]>0 || f_sister_num[cnt]>0))
							//{
							f_f[cnt] = multiply(2, "1/" + (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt] + sister_num[cnt]));
							f_f_note[cnt] = translations[67];
							f_f_base = 1;

							if (less(f_f[cnt], best))
							{
								f_f[cnt] = best;
								f_f_note[cnt] = translations[68];

								farad = true;
								f_f_base = 3;
							}
							//else
							{
								/*
								 * Version 1.4
								 * sister should be assigned as half of the f_f.
								 * BUGES if not present: try with: We didn't find until now
								 */
								sister[cnt] = "1/" + (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt] + sister_num[cnt]);
								sister_note[cnt] = translations[134];
								if (less(sister[cnt], "1/2"))
								{
									final String taaseebRest = subtract(subtract(1, f_f[cnt]), sister[cnt]);
									if (bigger(add(new String[]{taaseebRest, sister[cnt]}), "1/2"))
									{
										sister[cnt] = "1/2";
										sister_note[cnt] = translations[135];
									}
									else
									{
										sister[cnt] = add(new String[]{sister[cnt], taaseebRest});
										sister_note[cnt] = translations[136];
									}
								}
							}

							if (f_b_num[cnt] > 0)
							{
								f_b[cnt] = multiply(2, divide(subtract(subtract(1, f_f[cnt]), sister[cnt]), (2 * f_b_num[cnt] + f_sister_num[cnt])));
								f_b_note[cnt] = translations[69];
							}

							if (less(f_b[cnt], "0/1"))
								f_b[cnt] = "0/1";

							if (f_sister_num[cnt] > 0)
							{
								f_sister[cnt] = divide(subtract(subtract(1, f_f[cnt]), sister[cnt]), (2 * f_b_num[cnt] + f_sister_num[cnt]));
								f_sister_note[cnt] = translations[70];
							}

							if (less(f_sister[cnt], "0/1"))
								f_sister[cnt] = "0/1";
						}
					}
					else //i.e. there is FARATH
					{
						taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), multiply(w[cnt], w_num[cnt]), m[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt], m_m_f[cnt], m_f_f[cnt], multiply(d[cnt], d_num[cnt]), multiply(d_s[cnt], d_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt])});
						best = divide(subtract(1, taaseeb), 3); //For inisitialising only not real in all cases

						if (/* Version 1.3, This part is not reached at all since b_num[cnt]==0 ->  b_num[cnt]>0 && */ f_b_num[cnt] == 0 && f_sister_num[cnt] == 0)
						{
							f_f[cnt] = multiply(2, (divide(subtract(1, taaseeb), (2 * f_f_num[cnt] + sister_num[cnt]))));
							f_f_note[cnt] = translations[71];
							f_f_base = 1;

							/*
							 * Version 1.4
							 * sister should be assigned as half of the f_f.
							 * BUGES if not present: try with:
							 * m_m, sister, f_f.
							 * m, sister, f_f.
							 *
							if(bigger(f_f[cnt], best) && bigger(f_f[cnt], "1/6"))
							{
								sister[cnt]=divide(subtract(1, taaseeb), (2*f_f_num[cnt]+sister_num[cnt]));
								sister_note[cnt]=translations[131];
							}
							else
							*/ // Put them down.
							{
								if (less(f_f[cnt], best))
								{
									f_f[cnt] = best;
									f_f_note[cnt] = translations[72];

									farad = true;
									f_f_base = getDenominator(f_f[cnt]);
								}

								if (less(f_f[cnt], "1/6"))
								{
									f_f[cnt] = "1/6";
									f_f_note[cnt] = translations[73];

									farad = true;
									f_f_base = 6;
								}
							}

							// Version 1.4, see the note up.
							sister[cnt] = divide(subtract(1, taaseeb), (2 * f_f_num[cnt] + sister_num[cnt]));
							sister_note[cnt] = translations[131];
						}
						else
						{
							// Version 1.3, No need for this condition.
							//if((f_b_num[cnt]>0 || f_sister_num[cnt]>0))
							//{
							f_f[cnt] = multiply(2, (divide(subtract(1, taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt] + sister_num[cnt]))));
							f_f_note[cnt] = translations[74];
							f_f_base = 1;

							/*
							 * Version 1.4
							 * sister should be assigned as half of the f_f.
							 * BUGES if not present: try with:
							 * w, sister, f_f, f_b.
							 *
							if(bigger(f_f[cnt], best) && bigger(f_f[cnt], "1/6"))
							{
								sister[cnt]=divide(subtract(1, taaseeb), (2*f_b_num[cnt]+f_sister_num[cnt]+2*f_f_num[cnt]+sister_num[cnt]));
								sister_note[cnt]=translations[131];
								if(less(sister[cnt], "1/2"))
								{
									final String taaseebRest = subtract(subtract(subtract(1, f_f[cnt]), sister[cnt]), taaseeb);
									if(bigger(add(taaseebRest, sister[cnt]), "1/2"))
									{
										sister[cnt] = "1/2";
										sister_note[cnt]=translations[132];
									}
									else
									{
										sister[cnt] = add(sister[cnt], taaseebRest);
										sister_note[cnt]=translations[133];
									}
								}
							}
							else
							*/ // Put them down.
							{
								if (less(f_f[cnt], best))
								{
									f_f[cnt] = best;
									f_f_note[cnt] = translations[75];

									farad = true;
									f_f_base = getDenominator(f_f[cnt]);
								}

								if (less(f_f[cnt], "1/6"))
								{
									f_f[cnt] = "1/6";
									f_f_note[cnt] = translations[76];

									farad = true;
									f_f_base = 6;
								}
							}

							// Version 1.4, see the note up.
							sister[cnt] = divide(subtract(1, taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt] + 2 * f_f_num[cnt] + sister_num[cnt]));
							sister_note[cnt] = translations[131];
							if (less(sister[cnt], "1/2"))
							{
								final String taaseebRest = subtract(subtract(subtract(1, f_f[cnt]), sister[cnt]), taaseeb);
								if (bigger(add(new String[]{taaseebRest, sister[cnt]}), "1/2"))
								{
									sister[cnt] = "1/2";
									sister_note[cnt] = translations[132];
								}
								else
								{
									sister[cnt] = add(new String[]{sister[cnt], taaseebRest});
									sister_note[cnt] = translations[133];
								}
							}

							if (f_b_num[cnt] > 0)
							{
								f_b[cnt] = multiply(2, (divide(subtract(subtract(subtract(1, f_f[cnt]), sister[cnt]), taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt]))));
								f_b_note[cnt] = translations[77];
							}

							if (less(f_b[cnt], "0/1"))
								f_b[cnt] = "0/1";

							if (f_sister_num[cnt] > 0)
							{
								f_sister[cnt] = divide(subtract(subtract(subtract(1, f_f[cnt]), sister[cnt]), taaseeb), (2 * f_b_num[cnt] + f_sister_num[cnt]));
								f_sister_note[cnt] = translations[78];
							}

							if (less(f_sister[cnt], "0/1"))
								f_sister[cnt] = "0/1";
						}
					}
				}
			}
		}

		// Special cases, check conditions.
		//Case.1	Al-Omareya
		if (m_num[cnt] > 0)
		{
			// Version 1.7, Removed: f_f_num,m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num, and modified for brothers
			if ((w_num[cnt] > 0 || h_num[cnt] > 0) && f_num[cnt] == 1 && s_num[cnt] == 0 && d_num[cnt] == 0 &&
					s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && d_s_s_num[cnt] == 0 &&
					(b_num[cnt] + sister_num[cnt] + m_b_num[cnt] + m_sister_num[cnt] + f_b_num[cnt] + f_sister_num[cnt] < 2))
			{
				// Version 1.7, calculating taaseeb is removed since no need for it.
				// Version 1.5, This is to solve the bug of Al-Omareya (h + f + m)
				taaseeb = add(new String[]{multiply(w[cnt], w_num[cnt]), /* No need f[cnt] */ h[cnt]});

				m[cnt] = divide(subtract(1, taaseeb), 3);
				m_note[cnt] = translations[79];
			}
		}

		//Case.2	Al-Masaalah Al-Moshtarakah
		if (madaheb != madhabName.HANAFI && madaheb != madhabName.HANBALI)
		{
			// Version 1.7, Removed: s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_numf_b_num,f_sister_num
			if (h_num[cnt] == 1 && add(new String[]{m[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt], m_m_f[cnt], m_f_f[cnt]}).equals("1/6") &&
					(m_sister_num[cnt] + m_b_num[cnt]) >= 2 && ((b_num[cnt] + sister_num[cnt] > 0) && b_num[cnt] != 0) && f_num[cnt] == 0
					&& f_f_num[cnt] == 0 && s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
					&& d_s_s_num[cnt] == 0)
			{
				//	h[cnt],m[cnt] are done automatic
				String tempa = add(new String[]{multiply(m_b[cnt], m_b_num[cnt]), multiply(m_sister[cnt], m_sister_num[cnt])});//i.e. =1/3

				// Version 1.2
				if (m_b_num[cnt] > 0)
				{
					m_b[cnt] = divide(tempa, (m_sister_num[cnt] + m_b_num[cnt] + b_num[cnt] + sister_num[cnt]));
					m_b_note[cnt] = translations[80];
				}

				if (m_sister_num[cnt] > 0)
				{
					m_sister[cnt] = divide(tempa, (m_sister_num[cnt] + m_b_num[cnt] + b_num[cnt] + sister_num[cnt]));
					m_sister_note[cnt] = translations[81];
				}

				if (b_num[cnt] > 0)
				{
					b[cnt] = divide(tempa, (m_sister_num[cnt] + m_b_num[cnt] + b_num[cnt] + sister_num[cnt]));
					b_note[cnt] = translations[82];
				}

				if (sister_num[cnt] > 0)
				{
					sister[cnt] = divide(tempa, (m_sister_num[cnt] + m_b_num[cnt] + b_num[cnt] + sister_num[cnt]));
					sister_note[cnt] = translations[83];
				}
			}
		}

		//Case.3	Al-Masaalah Al-Akdareya
		if (madaheb != madhabName.HANAFI)
		{
			// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
			if (h_num[cnt] == 1 && m_num[cnt] == 1 && m_sister_num[cnt] == 0 && m_b_num[cnt] == 0 && b_num[cnt] == 0 && f_num[cnt] == 0
					&& f_f_num[cnt] == 1 && s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
					&& d_s_s_num[cnt] == 0 && ((sister_num[cnt] == 1 && f_sister_num[cnt] == 0) || (f_sister_num[cnt] == 1 &&
					sister_num[cnt] == 0)) && f_b_num[cnt] == 0)
			{
				//h[cnt] & m[cnt] are done automatic
				if (sister_num[cnt] == 1) // !=0
				{
					sister[cnt] = "1/2"; // Version 1.3, It was "1/"+String.valueOf(2*sister_num[cnt]);
					sister[cnt] = divide(add(new String[]{multiply(sister[cnt], sister_num[cnt]), f_f[cnt]}), 3);
					sister_note[cnt] = translations[84];
					//(sister_num[cnt]+2*f_f_num[cnt]) i.e. f_f_num[cnt]=1

					f_f[cnt] = multiply(2, sister[cnt]);
					f_f_note[cnt] = translations[85];
					//NOT 2*((double)sister[cnt]*sister_num[cnt]+f_f[cnt]) / (sister_num[cnt]+2);
				}
				else //i.e. f_sister[cnt]==1
				{
					f_sister[cnt] = "1/2"; // Version 1.3, It was "1/"+String.valueOf(2*f_sister_num[cnt]);
					f_sister[cnt] = divide(add(new String[]{multiply(f_sister[cnt], f_sister_num[cnt]), f_f[cnt]}), 3);
					f_sister_note[cnt] = translations[86];

					f_f[cnt] = multiply(2, f_sister[cnt]);
					f_f_note[cnt] = translations[87];
				}
			}
		}

		//Case.4	Al-Masaalah Al-Denareyah Al-soukrah
		//Case.5	Al-Masaalah Al-Denareyah Al-koubrah
		//Case.6	Al-Masaalah Al-kharqaa
		//Case.7	Al-Masaalah Al-Menbareia
		//Case.8	Al-Masaalah Al-Ashreia
		//Case.9	Al-Masaalah Al-Ashreneia
		//Case.10	Mokhtasarat Zayd
		//Case.11	Teseeneiat Zayd
		//Case.12	That Al-Forokh
		//Case.13	Al-mobahalh
		//Case.14	Al-Yatematan
		//Case.15	Am Al-Aramel
		//No calculation for them here see the results

		//Case.16	Al-Malikia
		if (madaheb == madhabName.MALIKI)
		{
			// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
			if (h_num[cnt] == 1 && w_num[cnt] == 0 && m_num[cnt] == 1 && (m_sister_num[cnt] > 0 || m_b_num[cnt] > 0) && b_num[cnt] == 0
					&& f_num[cnt] == 0 && f_f_num[cnt] == 1 && s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
					&& d_s_s_num[cnt] == 0 && sister_num[cnt] == 0 && f_b_num[cnt] > 0 && f_sister_num[cnt] == 0)
			{
				f_b[cnt] = "0/1";
			}
		}

		//Case.17	Shebah Al-Malikia
		if (madaheb == madhabName.MALIKI)
		{
			// Version 1.7, Removed: m_m_num,m_f_num,m_m_m_num,m_m_f_num,m_f_f_num,s_b_num,s_b_f_num,u_num,u_f_num,s_u_num,s_u_f_num
			if (h_num[cnt] == 1 && w_num[cnt] == 0 && m_num[cnt] == 1 && (m_sister_num[cnt] > 0 || m_b_num[cnt] > 0) && b_num[cnt] > 0
					&& f_num[cnt] == 0 && f_f_num[cnt] == 1 && s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
					&& d_s_s_num[cnt] == 0 && sister_num[cnt] == 0 && f_b_num[cnt] == 0 && f_sister_num[cnt] == 0)
			{
				b[cnt] = "0/1";
			}
		}

		//Case.18	No inheritors
		//No calculation here see the results
		if (h_num[cnt] == 0 && w_num[cnt] == 0 && m_num[cnt] == 0 && m_sister_num[cnt] == 0 && m_b_num[cnt] == 0 && b_num[cnt] == 0
				&& f_num[cnt] == 0 && f_f_num[cnt] == 0 && m_m_num[cnt] == 0 && m_f_num[cnt] == 0 && m_m_m_num[cnt] == 0 && m_m_f_num[cnt] == 0
				&& m_f_f_num[cnt] == 0 && s_num[cnt] == 0 && d_num[cnt] == 0 && s_s_num[cnt] == 0 && d_s_num[cnt] == 0 && s_s_s_num[cnt] == 0
				&& d_s_s_num[cnt] == 0 && sister_num[cnt] == 0 && f_b_num[cnt] == 0 && f_sister_num[cnt] == 0 && s_b_num[cnt] == 0 &&
				s_b_f_num[cnt] == 0 && u_num[cnt] == 0 && u_f_num[cnt] == 0 && s_u_num[cnt] == 0 && s_u_f_num[cnt] == 0)
		{
			muslim_trusts[cnt] = "1/1";
		}

		/*
		 * Version 1.7
		 * Calculating the base of the case here instead of being in MawarethResults class.
		 * This is mainly to avoid the rud/awle cases problems.
		 * In addition it handles the case for f_f with b and/or sister and/or fb and/or f_sister
		 */
		boolean mirathAlJadAndEkhwah = false;
		if (madaheb != madhabName.HANAFI && f_f_num[cnt] == 1 && f_num[cnt] == 0 && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && (sister_num[cnt] != 0 || b_num[cnt] != 0 || f_b_num[cnt] != 0 || f_sister_num[cnt] != 0))
			mirathAlJadAndEkhwah = true;

		if (!h[cnt].equals("0/1")) farad = true;
		if (!w[cnt].equals("0/1")) farad = true;
		if (!f[cnt].equals("0/1")) farad = true; // Only if there is child. without child only taseeb.
		if (!m[cnt].equals("0/1")) farad = true;
		if (!m_m[cnt].equals("0/1")) farad = true;
		if (!m_f[cnt].equals("0/1")) farad = true;
		if (!m_m_m[cnt].equals("0/1")) farad = true;
		if (!m_m_f[cnt].equals("0/1")) farad = true;
		if (!m_f_f[cnt].equals("0/1")) farad = true;

		// This condition s[cnt].equals("0/1") is to avoid countering daughters to be taseeb
		if (!d[cnt].equals("0/1") && s[cnt].equals("0/1")) farad = true;
		if (!d_s[cnt].equals("0/1") && s_s[cnt].equals("0/1")) farad = true;
		if (!d_s_s[cnt].equals("0/1") && s_s_s[cnt].equals("0/1")) farad = true;
		if (!sister[cnt].equals("0/1") && b[cnt].equals("0/1"))
			// Version 1.7
			if (mirathAlJadAndEkhwah && !sister[cnt].equals("1/2")) taseeb = true;
			else farad = true;

		if (!m_b[cnt].equals("0/1")) farad = true;
		if (!m_sister[cnt].equals("0/1")) farad = true;
		if (!f_sister[cnt].equals("0/1") && f_b[cnt].equals("0/1"))
			// Version 1.7
			if (mirathAlJadAndEkhwah && !f_sister[cnt].equals("1/2")) taseeb = true;
			else farad = true;

		if (f_f[cnt].equals("1/6") && !mirathAlJadAndEkhwah) farad = true;

		/*
		// Version 1.7
		if(mirathAlJadAndEkhwah)
			if(f_f[cnt].equals("1/3"))
				if(!farad)// This depends on the above cases of farad
				{
					farad = true;
					f_f_base = 3;
				}
				else
					taseeb = true;

		if(!f_f[cnt].equals("0/1"))
			// Version 1.7
			if(mirathAlJadAndEkhwah && !f_f[cnt].equals("1/6")) taseeb = true; // Check this
			else farad = true;
		*/

		if (farad)
		{
			faradWithTaseeb[cnt] = true;

			/*
			if(mirathAlJadAndEkhwah)
			{
				if(f_f[cnt].equals("1/6"))
					f_f_base=6;
			}
			else
			{
				if(f[cnt].equals("1/6") || f_f[cnt].equals("1/6")) // (f_num[cnt]!=0 || f_f_num[cnt]!=0) not work unless there is child
					f_f_base=6;
			}
			*/
			// Be aware that we will not repeat this below if taaseeb<1 since in farad case, there is no effect of taseeb.
			// Version 1.7, Change [cnt] to [cnt] (a lot of them !!)
			base[cnt] = LCD(new int[]{getDenominator(h[cnt]), getDenominator(multiply(w[cnt], w_num[cnt])),
					// Version 1.7 f & f_f are always 6 (fixed) unless mirathAlJadAndEkhwah=true
					// Version 1.7, Removed: LCD(getDenominator(f[cnt]), getDenominator(f_f[cnt]))
					// (mirathAlJadAndEkhwah)?getDenominator(f_f[cnt]):((f_num[cnt]!=0 || f_f_num[cnt]!=0)?6:1),  No need for this since there is no way that f_f get more that 6 as farad!!
					f_f_base,
					getDenominator(f[cnt]),
					getDenominator(m[cnt]),
					// Version 1.6, To work if denominator is not 6 due to awle
					getDenominator(add(new String[]{m_m[cnt], m_f[cnt]})),
					// Version 1.3
					((s_num[cnt] == 0) ? getDenominator(multiply(d[cnt], d_num[cnt])) : 1),
					// Version 1.3
					((s_s_num[cnt] == 0 && !(s_s_s_num[cnt] > 0 && d_num[cnt] >= 2)) ? getDenominator(multiply(d_s[cnt], d_s_num[cnt])) : 1),
					// Version 1.3
					((s_s_s_num[cnt] == 0) ? getDenominator(multiply(d_s_s[cnt], d_s_s_num[cnt])) : 1),
					// Version 1.6, To work if denominator is not 6 due to awle
					getDenominator(add(new String[]{m_m_m[cnt], m_m_f[cnt], m_f_f[cnt]})),
					getDenominator(multiply(m_b[cnt], m_b_num[cnt])),
					getDenominator(multiply(m_sister[cnt], m_sister_num[cnt])),
					// Version 1.6
					(b_num[cnt] == 0 /* Version 1.7 */ && (!mirathAlJadAndEkhwah || sister[cnt].equals("1/2"))) ? getDenominator(multiply(sister[cnt], sister_num[cnt])) : 1,
					(f_b_num[cnt] == 0 /* Version 1.7 */ && !mirathAlJadAndEkhwah || f_sister[cnt].equals("1/2")) ? getDenominator(multiply(f_sister[cnt], f_sister_num[cnt])) : 1});
		}
		//else
		//	if(taseeb && !farad) at the end

		taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), m[cnt], multiply(w[cnt], w_num[cnt]), f[cnt], f_f[cnt], m_m[cnt], m_f[cnt],
				m_m_m[cnt], m_m_f[cnt], m_f_f[cnt], multiply(s[cnt], s_num[cnt]), multiply(d[cnt], d_num[cnt]), multiply(s_s[cnt], s_s_num[cnt]),
				multiply(d_s[cnt], d_s_num[cnt]), multiply(s_s_s[cnt], s_s_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt]), multiply(b[cnt], b_num[cnt]),
				multiply(sister[cnt], sister_num[cnt]), multiply(m_b[cnt], m_b_num[cnt]), multiply(m_sister[cnt], m_sister_num[cnt]), multiply(f_b[cnt],
				f_b_num[cnt]), multiply(f_sister[cnt], f_sister_num[cnt]), multiply(s_b[cnt], s_b_num[cnt]), multiply(s_b_f[cnt], s_b_f_num[cnt]),
				multiply(u[cnt], u_num[cnt]), multiply(u_f[cnt], u_f_num[cnt]), multiply(s_u[cnt], s_u_num[cnt]), multiply(s_u_f[cnt], s_u_f_num[cnt])});

		if (less(taaseeb, "1/1"))
		{
			if (s_num[cnt] > 0)
			{
				if (d_num[cnt] == 0)
				{
					s[cnt] = divide(subtract(1, taaseeb), s_num[cnt]);
					s_note[cnt] = translations[88];
				}
				else
				{
					s[cnt] = multiply(divide(subtract(1, taaseeb), (d_num[cnt] + s_num[cnt] * 2)), 2);
					s_note[cnt] = translations[89];

					d[cnt] = divide(subtract(1, taaseeb), (d_num[cnt] + s_num[cnt] * 2));
					d_note[cnt] = translations[90];
				}
			}

			if ((s_s_num[cnt] > 0) && s_num[cnt] == 0)
			{
				if (d_s_num[cnt] == 0)
				{
					s_s[cnt] = divide(subtract(1, taaseeb), s_s_num[cnt]);
					s_s_note[cnt] = translations[91];
				}
				else
				{
					s_s[cnt] = multiply(divide(subtract(1, taaseeb), (d_s_num[cnt] + s_s_num[cnt] * 2)), 2);
					s_s_note[cnt] = translations[92];

					d_s[cnt] = divide(subtract(1, taaseeb), (d_s_num[cnt] + s_s_num[cnt] * 2));
					d_s_note[cnt] = translations[93];
				}
			}

			if ((s_s_s_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0)
			{
				if (d_s_s_num[cnt] == 0)
				{
					//all madhab axcept the latest of hanafi madhab
					if (d_s_num[cnt] > 0 /* Version 1.6 */ && d_num[cnt] >= 2)
					{
						s_s_s[cnt] = multiply(divide(subtract(1, taaseeb), (d_s_num[cnt] + s_s_s_num[cnt] * 2)), 2);
						s_s_s_note[cnt] = translations[94];

						d_s[cnt] = divide(subtract(1, taaseeb), (d_s_num[cnt] + s_s_s_num[cnt] * 2));
						d_s_note[cnt] = translations[95];
					}
					else
					{
						s_s_s[cnt] = divide(subtract(1, taaseeb), s_s_s_num[cnt]);
						s_s_s_note[cnt] = translations[96];
					}
				}
				else
				{
					if (d_s_num[cnt] > 0 /* Version 1.6 */ && d_num[cnt] >= 2)
					{
						s_s_s[cnt] = multiply(divide(subtract(1, taaseeb), (d_s_s_num[cnt] + s_s_s_num[cnt] * 2 + d_s_num[cnt])), 2);
						s_s_s_note[cnt] = translations[97];

						d_s_s[cnt] = divide(subtract(1, taaseeb), (d_s_s_num[cnt] + s_s_s_num[cnt] * 2 + d_s_num[cnt]));
						d_s_s_note[cnt] = translations[98];

						d_s[cnt] = divide(subtract(1, taaseeb), (d_s_s_num[cnt] + s_s_s_num[cnt] * 2 + d_s_num[cnt]));
						d_s_note[cnt] = translations[99];
					}
					else
					{
						s_s_s[cnt] = multiply(divide(subtract(1, taaseeb), (d_s_s_num[cnt] + s_s_s_num[cnt] * 2)), 2);
						s_s_s_note[cnt] = translations[100];

						d_s_s[cnt] = divide(subtract(1, taaseeb), (d_s_s_num[cnt] + s_s_s_num[cnt] * 2));
						d_s_s_note[cnt] = translations[101];
					}
				}
			}

			if ((f_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0)
			{
				f[cnt] = add(new String[]{f[cnt], subtract(1, taaseeb)});
				f_note[cnt] = translations[102];
			}

			if ((f_f_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0)
			{
				f_f[cnt] = add(new String[]{f_f[cnt], subtract(1, taaseeb)});
				f_f_note[cnt] = translations[103];
			}

			if ((b_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 && f_f_num[cnt] == 0)
			{
				if (sister_num[cnt] == 0)
				{
					b[cnt] = divide(subtract(1, taaseeb), b_num[cnt]);
					b_note[cnt] = translations[104];
				}
				else
				{
					b[cnt] = multiply(divide(subtract(1, taaseeb), (sister_num[cnt] + b_num[cnt] * 2)), 2);
					b_note[cnt] = translations[105];

					sister[cnt] = divide(subtract(1, taaseeb), (sister_num[cnt] + b_num[cnt] * 2));
					sister_note[cnt] = translations[106];
				}
			}

			if ((f_b_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0) // no sister_num[cnt]==0
			{
				if (f_sister_num[cnt] == 0)
				{
					f_b[cnt] = divide(subtract(1, taaseeb), f_b_num[cnt]);
					f_b_note[cnt] = translations[107];
				}
				else
				{
					f_b[cnt] = multiply(divide(subtract(1, taaseeb), (f_sister_num[cnt] + f_b_num[cnt] * 2)), 2);
					f_b_note[cnt] = translations[108];

					f_sister[cnt] = divide(subtract(1, taaseeb), (f_sister_num[cnt] + f_b_num[cnt] * 2));
					f_sister_note[cnt] = translations[109];
				}
			}

			if ((s_b_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0 && f_b_num[cnt] == 0)
			{
				s_b[cnt] = divide(subtract(1, taaseeb), s_b_num[cnt]);
				s_b_note[cnt] = translations[110];
			}

			if ((s_b_f_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0 && f_b_num[cnt] == 0 && s_b_num[cnt] == 0)
			{
				s_b_f[cnt] = divide(subtract(1, taaseeb), s_b_f_num[cnt]);
				s_b_f_note[cnt] = translations[111];
			}

			if ((u_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 && f_f_num[cnt] == 0
					&& b_num[cnt] == 0 && f_b_num[cnt] == 0 && s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0)
			{
				u[cnt] = divide(subtract(1, taaseeb), u_num[cnt]);
				u_note[cnt] = translations[112];
			}

			if ((u_f_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0 && f_b_num[cnt] == 0 && s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0 && u_num[cnt] == 0)
			{
				u_f[cnt] = divide(subtract(1, taaseeb), u_f_num[cnt]);
				u_f_note[cnt] = translations[113];
			}

			if ((s_u_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0 && f_b_num[cnt] == 0 && s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0 &&
					u_num[cnt] == 0 && u_f_num[cnt] == 0)
			{
				s_u[cnt] = divide(subtract(1, taaseeb), s_u_num[cnt]);
				s_u_note[cnt] = translations[114];
			}

			if ((s_u_f_num[cnt] > 0) && s_num[cnt] == 0 && s_s_num[cnt] == 0 && s_s_s_num[cnt] == 0 && f_num[cnt] == 0 &&
					f_f_num[cnt] == 0 && b_num[cnt] == 0 && f_b_num[cnt] == 0 && s_b_num[cnt] == 0 && s_b_f_num[cnt] == 0 &&
					u_num[cnt] == 0 && u_f_num[cnt] == 0 && s_u_num[cnt] == 0)
			{
				s_u_f[cnt] = divide(subtract(1, taaseeb), s_u_f_num[cnt]);
				s_u_f_note[cnt] = translations[115];
			}

			// For rud case
			taaseeb = add(new String[]{multiply(h[cnt], h_num[cnt]), m[cnt], multiply(w[cnt], w_num[cnt]), f_f[cnt], f[cnt], m_m[cnt], m_f[cnt], m_m_m[cnt], m_m_f[cnt],
					m_f_f[cnt], multiply(s[cnt], s_num[cnt]), multiply(d[cnt], d_num[cnt]), multiply(s_s[cnt], s_s_num[cnt]), multiply(d_s[cnt], d_s_num[cnt]),
					multiply(s_s_s[cnt], s_s_s_num[cnt]), multiply(d_s_s[cnt], d_s_s_num[cnt]), multiply(b[cnt], b_num[cnt]), multiply(sister[cnt], sister_num[cnt]),
					multiply(m_b[cnt], m_b_num[cnt]), multiply(m_sister[cnt], m_sister_num[cnt]), multiply(f_b[cnt], f_b_num[cnt]), multiply(f_sister[cnt], f_sister_num[cnt]),
					multiply(s_b[cnt], s_b_num[cnt]), multiply(s_b_f[cnt], s_b_f_num[cnt]), multiply(u[cnt], u_num[cnt]), multiply(u_f[cnt], u_f_num[cnt]),
					multiply(s_u[cnt], s_u_num[cnt]), multiply(s_u_f[cnt], s_u_f_num[cnt])});

			if (less(taaseeb, "1/1"))
			{
				if ((madaheb == madhabName.SHAFEE || madaheb == madhabName.MALIKI) /* Version 1.8, There is rud in arham case under late shafee & maliki madaheb */ && !arhamChoiceCheckBox.isSelected())
					muslim_trusts[cnt] = subtract(1, taaseeb);
				else
				{
					/*
					 * Version 1.2
					 * Adding m_f_f, m_m_f and m_m_m to the rud case.
					 *
					 * Update Version 1.3
					 * Adding d_s_s to the rud case.
					 */
					String w_rud, h_rud, d_rud, d_s_rud, d_s_s_rud, sister_rud, f_sister_rud, m_rud, m_m_rud, m_f_rud, m_f_f_rud, m_m_f_rud, m_m_m_rud, m_sister_rud, m_b_rud;

					//rud case
					w_rud = multiply(w[cnt], w_num[cnt]);
					h_rud = multiply(h[cnt], h_num[cnt]);
					d_rud = multiply(d[cnt], d_num[cnt]);
					d_s_rud = multiply(d_s[cnt], d_s_num[cnt]);
					d_s_s_rud = multiply(d_s_s[cnt], d_s_s_num[cnt]);
					sister_rud = multiply(sister[cnt], sister_num[cnt]);
					f_sister_rud = multiply(f_sister[cnt], f_sister_num[cnt]);
					m_rud = m[cnt];
					m_m_rud = m_m[cnt];
					m_f_rud = m_f[cnt];
					m_f_f_rud = m_f_f[cnt];
					m_m_f_rud = m_m_f[cnt];
					m_m_m_rud = m_m_m[cnt];
					m_sister_rud = multiply(m_sister[cnt], m_sister_num[cnt]);
					m_b_rud = multiply(m_b[cnt], m_b_num[cnt]);

					String temp = add(new String[]{d_rud, d_s_rud, sister_rud, f_sister_rud, m_rud, m_m_rud, m_f_rud, m_sister_rud, m_b_rud, m_f_f_rud, m_m_f_rud, m_m_m_rud, d_s_s_rud});

					if (temp.equals("0/1")) //i.e. no FARATH to be rud
					{
						if (w_num[cnt] > 0)
							w_rud = "1/1";
						else
							if (h_num[cnt] == 1)
								h_rud = "1/1";
					}

					// Don't forget we add here +h[cnt]*h_num[cnt]+w[cnt]*w_num[cnt]
					temp = add(new String[]{d_rud, d_s_rud, sister_rud, f_sister_rud, m_rud, m_m_f_rud, m_m_m_rud, m_m_rud, m_f_f_rud, m_f_rud, m_sister_rud, h_rud, m_b_rud, w_rud, d_s_s_rud});
					temp = subtract(1, temp);

					if (less(temp, "1/1"))
					{
						int LCDNumber = LCD(new int[]{getDenominator(d_rud), getDenominator(d_s_rud), getDenominator(sister_rud), getDenominator(f_sister_rud), getDenominator(m_rud), getDenominator(d_s_s_rud),
								getDenominator(m_f_f_rud), getDenominator(m_m_f_rud), getDenominator(m_m_rud), getDenominator(m_m_m_rud), getDenominator(m_f_rud), getDenominator(m_sister_rud), getDenominator(m_rud),
								getDenominator(m_m_rud), getDenominator(m_b_rud)});

						//if(LCDNumber <=0) System.out.println(LCDNumber);

						int d_numerator = (LCDNumber / getDenominator(d_rud)) * getNumerator(d_rud);
						int d_s_numerator = (LCDNumber / getDenominator(d_s_rud)) * getNumerator(d_s_rud);
						int d_s_s_numerator = (LCDNumber / getDenominator(d_s_s_rud)) * getNumerator(d_s_s_rud);
						int sister_numerator = (LCDNumber / getDenominator(sister_rud)) * getNumerator(sister_rud);
						int f_sister_numerator = (LCDNumber / getDenominator(f_sister_rud)) * getNumerator(f_sister_rud);
						int m_numerator = (LCDNumber / getDenominator(m_rud)) * getNumerator(m_rud);
						int m_m_numerator = (LCDNumber / getDenominator(m_m_rud)) * getNumerator(m_m_rud);
						int m_f_numerator = (LCDNumber / getDenominator(m_f_rud)) * getNumerator(m_f_rud);
						int m_f_f_numerator = (LCDNumber / getDenominator(m_f_f_rud)) * getNumerator(m_f_f_rud);
						int m_m_f_numerator = (LCDNumber / getDenominator(m_m_f_rud)) * getNumerator(m_m_f_rud);
						int m_m_m_numerator = (LCDNumber / getDenominator(m_m_m_rud)) * getNumerator(m_m_m_rud);
						int m_sister_numerator = (LCDNumber / getDenominator(m_sister_rud)) * getNumerator(m_sister_rud);
						int m_b_numerator = (LCDNumber / getDenominator(m_b_rud)) * getNumerator(m_b_rud);

						int newDenominator = d_numerator + d_s_numerator + d_s_s_numerator + sister_numerator + f_sister_numerator + m_numerator
								+ m_m_numerator + m_f_numerator + m_f_f_numerator + m_m_f_numerator + m_m_m_numerator + m_sister_numerator + m_b_numerator;

						d_rud = String.valueOf(d_numerator) + '/' + newDenominator;
						d_s_rud = String.valueOf(d_s_numerator) + '/' + newDenominator;
						d_s_s_rud = String.valueOf(d_s_s_numerator) + '/' + newDenominator;
						sister_rud = String.valueOf(sister_numerator) + '/' + newDenominator;
						f_sister_rud = String.valueOf(f_sister_numerator) + '/' + newDenominator;
						m_rud = String.valueOf(m_numerator) + '/' + newDenominator;
						m_m_rud = String.valueOf(m_m_numerator) + '/' + newDenominator;
						m_f_rud = String.valueOf(m_f_numerator) + '/' + newDenominator;
						m_f_f_rud = String.valueOf(m_f_f_numerator) + '/' + newDenominator;
						m_m_f_rud = String.valueOf(m_m_f_numerator) + '/' + newDenominator;
						m_m_m_rud = String.valueOf(m_m_m_numerator) + '/' + newDenominator;
						m_sister_rud = String.valueOf(m_sister_numerator) + '/' + newDenominator;
						m_b_rud = String.valueOf(m_b_numerator) + '/' + newDenominator;

						// To make zero amount = "0/1"
						d_rud = simplify(d_rud);
						d_s_rud = simplify(d_s_rud);
						d_s_s_rud = simplify(d_s_s_rud);
						sister_rud = simplify(sister_rud);
						f_sister_rud = simplify(f_sister_rud);
						m_rud = simplify(m_rud);
						m_m_rud = simplify(m_m_rud);
						m_f_rud = simplify(m_f_rud);
						m_f_f_rud = simplify(m_f_f_rud);
						m_m_f_rud = simplify(m_m_f_rud);
						m_m_m_rud = simplify(m_m_m_rud);
						m_sister_rud = simplify(m_sister_rud);
						m_b_rud = simplify(m_b_rud);

						if (w_num[cnt] > 0 || h_num[cnt] == 1)
						{
							if (w_num[cnt] > 0) temp = subtract(1, w_rud);
							else temp = subtract(1, h_rud);

							d_rud = multiply(d_rud, temp);
							d_s_rud = multiply(d_s_rud, temp);
							d_s_s_rud = multiply(d_s_s_rud, temp);
							sister_rud = multiply(sister_rud, temp);
							f_sister_rud = multiply(f_sister_rud, temp);
							m_rud = multiply(m_rud, temp);
							m_m_rud = multiply(m_m_rud, temp);
							m_f_rud = multiply(m_f_rud, temp);
							m_f_f_rud = multiply(m_f_f_rud, temp);
							m_m_f_rud = multiply(m_m_f_rud, temp);
							m_m_m_rud = multiply(m_m_m_rud, temp);
							m_sister_rud = multiply(m_sister_rud, temp);
							m_b_rud = multiply(m_b_rud, temp);
						}
					}

					// To avoid divide by ZERO
					if (!d_rud.equals("0/1"))
					{
						d[cnt] = divide(d_rud, d_num[cnt]);
						d_note[cnt] = d_note[cnt] + translations[116];
					}

					if (!d_s_rud.equals("0/1"))
					{
						d_s[cnt] = divide(d_s_rud, d_s_num[cnt]);
						d_s_note[cnt] = d_s_note[cnt] + translations[117];
					}

					if (!d_s_s_rud.equals("0/1"))
					{
						d_s_s[cnt] = divide(d_s_s_rud, d_s_s_num[cnt]);
						d_s_s_note[cnt] = d_s_s_note[cnt] + translations[118];
					}

					if (!sister_rud.equals("0/1"))
					{
						sister[cnt] = divide(sister_rud, sister_num[cnt]);
						sister_note[cnt] = sister_note[cnt] + translations[119];
					}

					if (!f_sister_rud.equals("0/1"))
					{
						f_sister[cnt] = divide(f_sister_rud, f_sister_num[cnt]);
						f_sister_note[cnt] = f_sister_note[cnt] + translations[120];
					}

					if (!m_rud.equals("0/1"))
					{
						m[cnt] = divide(m_rud, m_num[cnt]);
						m_note[cnt] = m_note[cnt] + translations[121];
					}

					if (!m_m_rud.equals("0/1"))
					{
						m_m[cnt] = divide(m_m_rud, m_m_num[cnt]);
						m_m_note[cnt] = m_m_note[cnt] + translations[122];
					}

					if (!m_f_rud.equals("0/1"))
					{
						m_f[cnt] = divide(m_f_rud, m_f_num[cnt]);
						m_f_note[cnt] = m_f_note[cnt] + translations[123];
					}

					if (!m_f_f_rud.equals("0/1"))
					{
						m_f_f[cnt] = divide(m_f_f_rud, m_f_f_num[cnt]);
						m_f_f_note[cnt] = m_f_f_note[cnt] + translations[124];
					}

					if (!m_m_f_rud.equals("0/1"))
					{
						m_m_f[cnt] = divide(m_m_f_rud, m_m_f_num[cnt]);
						m_m_f_note[cnt] = m_m_f_note[cnt] + translations[125];
					}

					if (!m_m_m_rud.equals("0/1"))
					{
						m_m_m[cnt] = divide(m_m_m_rud, m_m_m_num[cnt]);
						m_m_m_note[cnt] = m_m_m_note[cnt] + translations[126];
					}

					if (!m_sister_rud.equals("0/1"))
					{
						m_sister[cnt] = divide(m_sister_rud, m_sister_num[cnt]);
						m_sister_note[cnt] = m_sister_note[cnt] + translations[127];
					}

					if (!m_b_rud.equals("0/1"))
					{
						m_b[cnt] = divide(m_b_rud, m_b_num[cnt]);
						m_b_note[cnt] = m_b_note[cnt] + translations[128];
					}

					if (!h_rud.equals("0/1"))
					{
						h[cnt] = h_rud;
						h_note[cnt] = h_note[cnt] + translations[129];
					}

					if (!w_rud.equals("0/1"))
					{
						w[cnt] = divide(w_rud, w_num[cnt]);
						w_note[cnt] = w_note[cnt] + translations[130];
					}
				}
			}
		}
		else
		{
			//bin-Abas not accept Awle
			if (bigger(taaseeb, "1/1"))
			{
				// Awle case
				// Version 1.7
				foundAwle[cnt] = true;

				String awle = add(new String[]{multiply(h[cnt], h_num[cnt] * 24), multiply(m[cnt], 24), multiply(w[cnt], w_num[cnt] * 24), multiply(f[cnt], 24), multiply(f_f[cnt], 24),
						multiply(m_m[cnt], 24), multiply(m_f[cnt], 24), multiply(m_m_m[cnt], 24), multiply(m_m_f[cnt], 24), multiply(m_f_f[cnt], 24), multiply(s[cnt], s_num[cnt] * 24),
						multiply(d[cnt], d_num[cnt] * 24), multiply(s_s[cnt], s_s_num[cnt] * 24), multiply(d_s[cnt], d_s_num[cnt] * 24), multiply(s_s_s[cnt], s_s_s_num[cnt] * 24),
						multiply(d_s_s[cnt], d_s_s_num[cnt] * 24), multiply(b[cnt], b_num[cnt] * 24), multiply(sister[cnt], sister_num[cnt] * 24), multiply(m_b[cnt], m_b_num[cnt] * 24),
						multiply(m_sister[cnt], m_sister_num[cnt] * 24), multiply(f_b[cnt], f_b_num[cnt] * 24), multiply(f_sister[cnt], f_sister_num[cnt] * 24), multiply(s_b[cnt],
						s_b_num[cnt] * 24), multiply(s_b_f[cnt], s_b_f_num[cnt] * 24), multiply(u[cnt], u_num[cnt] * 24), multiply(u_f[cnt], u_f_num[cnt] * 24), multiply(s_u[cnt],
						s_u_num[cnt] * 24), multiply(s_u_f[cnt], s_u_f_num[cnt] * 24)});

				// Version 2.2, Add notes and conditions for performance.
				if (h_num[cnt] != 0)
				{
					h[cnt] = divide(multiply(h[cnt], 24), awle);
					h_note[cnt] = h_note[cnt] + translations[138];
				}

				if (w_num[cnt] != 0)
				{
					w[cnt] = divide(multiply(w[cnt], 24), awle);
					w_note[cnt] = w_note[cnt] + translations[139];
				}

				if (f_num[cnt] != 0)
				{
					f[cnt] = divide(multiply(f[cnt], 24), awle);
					f_note[cnt] = f_note[cnt] + translations[138];
				}

				if (f_f_num[cnt] != 0)
				{
					f_f[cnt] = divide(multiply(f_f[cnt], 24), awle);
					f_f_note[cnt] = f_f_note[cnt] + translations[138];
				}

				if (m_num[cnt] != 0)
				{
					m[cnt] = divide(multiply(m[cnt], 24), awle);
					m_note[cnt] = m_note[cnt] + translations[139];
				}

				if (m_m_num[cnt] != 0)
				{
					m_m[cnt] = divide(multiply(m_m[cnt], 24), awle);
					m_m_note[cnt] = m_m_note[cnt] + translations[139];
				}

				if (m_f_num[cnt] != 0)
				{
					m_f[cnt] = divide(multiply(m_f[cnt], 24), awle);
					m_f_note[cnt] = m_f_note[cnt] + translations[139];
				}

				if (m_m_m_num[cnt] != 0)
				{
					m_m_m[cnt] = divide(multiply(m_m_m[cnt], 24), awle);
					m_m_m_note[cnt] = m_m_m_note[cnt] + translations[139];
				}

				if (m_m_f_num[cnt] != 0)
				{
					m_m_f[cnt] = divide(multiply(m_m_f[cnt], 24), awle);
					m_m_f_note[cnt] = m_m_f_note[cnt] + translations[139];
				}

				if (m_f_f_num[cnt] != 0)
				{
					m_f_f[cnt] = divide(multiply(m_f_f[cnt], 24), awle);
					m_f_f_note[cnt] = m_f_f_note[cnt] + translations[139];
				}

				if (s_num[cnt] != 0)
				{
					s[cnt] = divide(multiply(s[cnt], 24), awle);
					s_note[cnt] = s_note[cnt] + translations[138];
				}

				if (d_num[cnt] != 0)
				{
					d[cnt] = divide(multiply(d[cnt], 24), awle);
					d_note[cnt] = d_note[cnt] + translations[139];
				}

				if (s_s_num[cnt] != 0)
				{
					s_s[cnt] = divide(multiply(s_s[cnt], 24), awle);
					s_s_note[cnt] = s_s_note[cnt] + translations[138];
				}

				if (d_s_num[cnt] != 0)
				{
					d_s[cnt] = divide(multiply(d_s[cnt], 24), awle);
					d_s_note[cnt] = d_s_note[cnt] + translations[139];
				}

				if (s_s_s_num[cnt] != 0)
				{
					s_s_s[cnt] = divide(multiply(s_s_s[cnt], 24), awle);
					s_s_s_note[cnt] = s_s_s_note[cnt] + translations[138];
				}

				if (d_s_s_num[cnt] != 0)
				{
					d_s_s[cnt] = divide(multiply(d_s_s[cnt], 24), awle);
					d_s_s_note[cnt] = d_s_s_note[cnt] + translations[139];
				}

				if (b_num[cnt] != 0)
				{
					b[cnt] = divide(multiply(b[cnt], 24), awle);
					b_note[cnt] = b_note[cnt] + translations[138];
				}

				if (sister_num[cnt] != 0)
				{
					sister[cnt] = divide(multiply(sister[cnt], 24), awle);
					sister_note[cnt] = sister_note[cnt] + translations[139];
				}

				if (m_b_num[cnt] != 0)
				{
					m_b[cnt] = divide(multiply(m_b[cnt], 24), awle);
					m_b_note[cnt] = m_b_note[cnt] + translations[138];
				}

				if (m_sister_num[cnt] != 0)
				{
					m_sister[cnt] = divide(multiply(m_sister[cnt], 24), awle);
					m_sister_note[cnt] = m_sister_note[cnt] + translations[139];
				}

				if (f_b_num[cnt] != 0)
				{
					f_b[cnt] = divide(multiply(f_b[cnt], 24), awle);
					f_b_note[cnt] = f_b_note[cnt] + translations[138];
				}

				if (f_sister_num[cnt] != 0)
				{
					f_sister[cnt] = divide(multiply(f_sister[cnt], 24), awle);
					f_sister_note[cnt] = f_sister_note[cnt] + translations[139];
				}

				// TODO: No Awle with u_f and other taseeb guys !! so why we add them here.
				if (s_b_num[cnt] != 0)
				{
					s_b[cnt] = divide(multiply(s_b[cnt], 24), awle);
					s_b_note[cnt] = s_b_note[cnt] + translations[138];
				}

				if (s_b_f_num[cnt] != 0)
				{
					s_b_f[cnt] = divide(multiply(s_b_f[cnt], 24), awle);
					s_b_f_note[cnt] = s_b_f_note[cnt] + translations[138];
				}

				if (u_num[cnt] != 0)
				{
					u[cnt] = divide(multiply(u[cnt], 24), awle);
					u_note[cnt] = u_note[cnt] + translations[138];
				}

				if (u_f_num[cnt] != 0)
				{
					u_f[cnt] = divide(multiply(u_f[cnt], 24), awle);
					u_f_note[cnt] = u_f_note[cnt] + translations[138];
				}

				if (s_u_num[cnt] != 0)
				{
					s_u[cnt] = divide(multiply(s_u[cnt], 24), awle);
					s_u_note[cnt] = s_u_note[cnt] + translations[138];
				}

				if (s_u_f_num[cnt] != 0)
				{
					s_u_f[cnt] = divide(multiply(s_u_f[cnt], 24), awle);
					s_u_f_note[cnt] = s_u_f_note[cnt] + translations[138];
				}

				// Version 1.7, Re-calculating with Awle case.
				base[cnt] = LCD(new int[]{getDenominator(h[cnt]), getDenominator(multiply(w[cnt], w_num[cnt])),
						getDenominator(f_f[cnt]),
						getDenominator(f[cnt]),
						getDenominator(m[cnt]),
						getDenominator(add(new String[]{m_m[cnt], m_f[cnt]})),
						((s_num[cnt] == 0) ? getDenominator(multiply(d[cnt], d_num[cnt])) : 1),
						((s_s_num[cnt] == 0 && !(s_s_s_num[cnt] > 0 && d_num[cnt] >= 2)) ? getDenominator(multiply(d_s[cnt], d_s_num[cnt])) : 1),
						((s_s_s_num[cnt] == 0) ? getDenominator(multiply(d_s_s[cnt], d_s_s_num[cnt])) : 1),
						getDenominator(add(new String[]{m_m_m[cnt], m_m_f[cnt], m_f_f[cnt]})),
						getDenominator(multiply(m_b[cnt], m_b_num[cnt])),
						getDenominator(multiply(m_sister[cnt], m_sister_num[cnt])),
						(b_num[cnt] == 0 /* Version 1.7 */ && (!mirathAlJadAndEkhwah /* No need since 1/2 will not remain after awle */ || sister[cnt].equals("1/2"))) ? getDenominator(multiply(sister[cnt], sister_num[cnt])) : 1,
						(f_b_num[cnt] == 0 /* Version 1.7 */ && (!mirathAlJadAndEkhwah || f_sister[cnt].equals("1/2"))) ? getDenominator(multiply(f_sister[cnt], f_sister_num[cnt])) : 1});
			}
		}

		// Moved here after calculating taseeb members.
		if (!s[cnt].equals("0/1")) taseeb = true;
		if (!s_s[cnt].equals("0/1")) taseeb = true;
		if (!s_s_s[cnt].equals("0/1")) taseeb = true;
		if (!b[cnt].equals("0/1")) taseeb = true;
		if (!f_b[cnt].equals("0/1")) taseeb = true;
		if (!s_b[cnt].equals("0/1")) taseeb = true;
		if (!s_b_f[cnt].equals("0/1")) taseeb = true;
		if (!u[cnt].equals("0/1")) taseeb = true;
		if (!u_f[cnt].equals("0/1")) taseeb = true;
		if (!s_u[cnt].equals("0/1")) taseeb = true;
		if (!s_u_f[cnt].equals("0/1")) taseeb = true;

		// Version 1.7
		if (taseeb && !farad)
		{
			faradWithTaseeb[cnt] = false; // No need for this. By default it is false.

			// Version 1.7, Calculating the base in taseeb case only.
			base[cnt] = LCD(new int[]{getDenominator(s[cnt]), getDenominator(d[cnt]), getDenominator(s_s[cnt]), getDenominator(d_s[cnt]), getDenominator(s_s_s[cnt]),
					getDenominator(d_s_s[cnt]), getDenominator(b[cnt]), getDenominator(sister[cnt]), getDenominator(f_b[cnt]), getDenominator(f_sister[cnt]),
					getDenominator(s_b[cnt]), getDenominator(s_b_f[cnt]), getDenominator(u[cnt]), getDenominator(u_f[cnt]), getDenominator(s_u[cnt]), getDenominator(s_u_f[cnt]),
					// Update version 1.7, In case of mirathAlJadAndEkhwah = true, or simply father with wife/husband
					getDenominator(f_f[cnt]), getDenominator(f[cnt])});
		}
	}

	String selectedArhamInheritors = ""; // Version 2.2

	void arhamCalculation()
	{
		final String[] translations = MaknoonIslamicEncyclopedia.StreamConverter("language/" + ((MaknoonIslamicEncyclopedia.language) ? "arhamCalculationArabic.txt" : "arhamCalculationEnglish.txt"));

		// Version 1.7
		if (a_s_d_num[cnt] == 0 && a_d_d_num[cnt] == 0 && a_s_d_s_num[cnt] == 0 && a_d_d_s_num[cnt] == 0 && a_s_d_d_num[cnt] == 0 && a_d_d_d_num[cnt] == 0 && a_s_s_d_num[cnt] == 0 &&
				a_d_s_d_num[cnt] == 0 && a_f_m_num[cnt] == 0 && a_f_f_m_num[cnt] == 0 && a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0 && a_m_f_m_num[cnt] == 0 && a_s_sister_num[cnt] == 0 &&
				a_d_sister_num[cnt] == 0 && a_d_b_num[cnt] == 0 && a_d_msister_num[cnt] == 0 && a_s_msister_num[cnt] == 0 && a_d_mb_num[cnt] == 0 && a_s_mb_num[cnt] == 0 && a_s_fsister_num[cnt] == 0 &&
				a_d_fsister_num[cnt] == 0 && a_d_fb_num[cnt] == 0 && a_ul_num[cnt] == 0 && a_k_num[cnt] == 0 && a_kl_num[cnt] == 0 && a_s_ul_num[cnt] == 0 && a_d_ul_num[cnt] == 0 && a_d_u_num[cnt] == 0 &&
				a_s_k_num[cnt] == 0 && a_d_k_num[cnt] == 0 && a_s_kl_num[cnt] == 0 && a_d_kl_num[cnt] == 0)
		{
			if (h_num[cnt] == 1)
			{
				h[cnt] = "1/1";
				h_note[cnt] = translations[80];
				return;
			}

			// Wife cases
			if (w_num[cnt] >= 1)
			{
				w[cnt] = "1/1";
				w[cnt] = divide(w[cnt], w_num[cnt]);
				w_note[cnt] = translations[81];
				return;
			}
		}

		if (madaheb != madhabName.HANAFI) // i.e. hanbali, late shafee & maliki
		{
			// Version 1.8, Enabling Arham under late Shafee & Maliki scholars.
			if ((madaheb == madhabName.SHAFEE || madaheb == madhabName.MALIKI) && arhamChoiceCheckBox.isSelected())
			{
				// The first layer of arham will 'hajb' the second layer of arham.
				if ((a_s_d_num[cnt] + a_d_d_num[cnt] + a_s_d_s_num[cnt] + a_d_d_s_num[cnt] + a_f_m_num[cnt] + a_f_m_f_num[cnt] + a_f_m_m_num[cnt] + a_s_sister_num[cnt] +
						a_d_sister_num[cnt] + a_d_b_num[cnt] + a_d_msister_num[cnt] + a_s_msister_num[cnt] + a_d_mb_num[cnt] + a_s_mb_num[cnt] + a_s_fsister_num[cnt] +
						a_d_fsister_num[cnt] + a_d_fb_num[cnt] + a_ul_num[cnt] + a_k_num[cnt] + a_kl_num[cnt] + a_d_u_num[cnt]) > 0)
				{
					// Version 2.2, To not loose these 'mahjob' arham inheritors listed in the list panel.
					if (cnt == 0)
					{
						if (a_s_d_d_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[82] + "<font color=green> (" + a_s_d_d_num[0] + ")</font>";
						if (a_d_d_d_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[83] + "<font color=green> (" + a_d_d_d_num[0] + ")</font>";
						if (a_s_s_d_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[84] + "<font color=green> (" + a_s_s_d_num[0] + ")</font>";
						if (a_d_s_d_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[85] + "<font color=green> (" + a_d_s_d_num[0] + ")</font>";
						if (a_f_f_m_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[86];
						if (a_m_f_m_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[87];
						if (a_s_ul_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[88] + "<font color=green> (" + a_s_ul_num[0] + ")</font>";
						if (a_d_ul_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[89] + "<font color=green> (" + a_d_ul_num[0] + ")</font>";
						if (a_s_kl_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[90] + "<font color=green> (" + a_s_kl_num[0] + ")</font>";
						if (a_d_kl_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[91] + "<font color=green> (" + a_d_kl_num[0] + ")</font>";
						if (a_d_k_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[92] + "<font color=green> (" + a_d_k_num[0] + ")</font>";
						if (a_s_k_num[0] != 0)
							selectedArhamInheritors = selectedArhamInheritors + " - " + translations[93] + "<font color=green> (" + a_s_k_num[0] + ")</font>";
					}

					a_s_d_d_num[cnt] = 0;
					a_d_d_d_num[cnt] = 0;
					a_s_s_d_num[cnt] = 0;
					a_d_s_d_num[cnt] = 0;
					a_f_f_m_num[cnt] = 0;
					a_m_f_m_num[cnt] = 0;
					a_s_ul_num[cnt] = 0;
					a_d_ul_num[cnt] = 0;
					a_s_k_num[cnt] = 0;
					a_d_k_num[cnt] = 0;
					a_s_kl_num[cnt] = 0;
					a_d_kl_num[cnt] = 0;
				}
			}

			if (a_f_m_num[cnt] == 1 || a_f_f_m_num[cnt] == 1 || a_m_f_m_num[cnt] == 1) m_num[cnt] = 1;

			// Version 1.7, adding a_f_m_f, a_f_m_m in arhamCalculation() although they are there in ArhamInterface()!
			if (a_f_m_f_num[cnt] == 1) m_f_num[cnt] = 1;
			if (a_f_m_m_num[cnt] == 1) m_m_num[cnt] = 1;

			// Version 1.2, There is an addition condition here to check the arham choice i.e. oneWayArhamChoice or manyWayChoice
			if (a_d_d_num[cnt] > 0 || a_s_d_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					d_num[cnt] = 1;
				else
					d_num[cnt] = a_d_d_num[cnt] + a_s_d_num[cnt];
			}

			if (a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0 && (a_s_d_d_num[cnt] > 0 || a_d_d_d_num[cnt] > 0 || a_s_s_d_num[cnt] > 0 || a_d_s_d_num[cnt] > 0))
			{
				if (oneWayArhamChoice)
					d_num[cnt] = 1;
				else
					d_num[cnt] = a_s_d_d_num[cnt] + a_d_d_d_num[cnt] + a_s_s_d_num[cnt] + a_d_s_d_num[cnt];
			}

			if (a_s_d_s_num[cnt] > 0 || a_d_d_s_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					d_s_num[cnt] = 1;
				else
					d_s_num[cnt] = a_s_d_s_num[cnt] + a_d_d_s_num[cnt];
			}

			if (a_s_mb_num[cnt] > 0 || a_d_mb_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					m_b_num[cnt] = 1;
				else
					m_b_num[cnt] = a_s_mb_num[cnt] + a_d_mb_num[cnt];
			}

			if (a_s_msister_num[cnt] > 0 || a_d_msister_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					m_sister_num[cnt] = 1;
				else
					m_sister_num[cnt] = a_s_msister_num[cnt] + a_d_msister_num[cnt];
			}

			if (a_d_b_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					b_num[cnt] = 1;
				else
					b_num[cnt] = a_d_b_num[cnt];
			}

			if (a_s_sister_num[cnt] > 0 || a_d_sister_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					sister_num[cnt] = 1;
				else
					sister_num[cnt] = a_s_sister_num[cnt] + a_d_sister_num[cnt];
			}

			if (a_d_fb_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					f_b_num[cnt] = 1;
				else
					f_b_num[cnt] = a_d_fb_num[cnt];
			}

			if (a_s_fsister_num[cnt] > 0 || a_d_fsister_num[cnt] > 0)
			{
				if (oneWayArhamChoice)
					f_sister_num[cnt] = 1;
				else
					f_sister_num[cnt] = a_s_fsister_num[cnt] + a_d_fsister_num[cnt];
			}

			if (a_ul_num[cnt] > 0 || a_s_ul_num[cnt] > 0 || a_d_ul_num[cnt] > 0) f_num[cnt] = 1;
			if (a_d_u_num[cnt] > 0) // No a_s_u_num[cnt]>0
			{
				if (oneWayArhamChoice)
					u_num[cnt] = 1;
				else
					u_num[cnt] = a_d_u_num[cnt];
			}

			if (a_kl_num[cnt] > 0 || a_k_num[cnt] > 0 /* Version 1.7, Removed: || a_s_kl_num[cnt]>0 || a_d_kl_num[cnt]>0 || a_s_k_num[cnt]>0 || a_d_k_num[cnt]>0*/)
				m_num[cnt] = 1;

			// Version 1.7
			if (a_f_f_m_num[cnt] == 0 && a_m_f_m_num[cnt] == 0 && a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0)
				if (a_s_kl_num[cnt] > 0 || a_d_kl_num[cnt] > 0 || a_s_k_num[cnt] > 0 || a_d_k_num[cnt] > 0)
					m_num[cnt] = 1;

			// Husband cases
			if (h_num[cnt] == 1)
			{
				h[cnt] = "1/2";
				h_note[cnt] = translations[0];
			}

			// Wife cases
			if (w_num[cnt] >= 1)
			{
				w[cnt] = "1/4";
				w[cnt] = divide(w[cnt], w_num[cnt]);
				w_note[cnt] = translations[1];
			}

			// To not affect the calculation
			h_num[cnt] = 0;
			w_num[cnt] = 0;

			// Calculate with respect to inheritors
			inheritorsCalculation();

			// To return their values
			if (bigger(h[cnt], "0/1")) h_num[cnt] = 1;
			if (bigger(w[cnt], "0/1")) w_num[cnt] = getNumerator(divide(1, multiply(w[cnt], 4)));

			// Reinitialise
			if (a_f_m_num[cnt] == 1)
			{
				a_f_m[cnt] = multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt])));
				a_f_m_note[cnt] = translations[2];
			}

			// Version 1.7
			if (a_f_m_f_num[cnt] == 1) // No need for ( ... && a_kl_num[cnt]==0 && a_k_num[cnt]==0) since m will block m_f
			{
				a_f_m_f[cnt] = multiply(m_f[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt])));
				a_f_m_f_note[cnt] = translations[78];
			}

			// Version 1.7
			if (a_f_m_m_num[cnt] == 1) // No need for ( ... && a_kl_num[cnt]==0 && a_k_num[cnt]==0) since m will block m_m
			{
				a_f_m_m[cnt] = multiply(m_m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt])));
				a_f_m_m_note[cnt] = translations[79];
			}

			/*
			 * Version 1.7
			 * Adding if( ... && a_f_m_f_num[cnt]==0 && a_f_m_m_num[cnt]==0)
			 * Reducing one layer:
			 * a_f_m_f  ->  m_f (inheritor)
			 * a_f_m_m  ->  m_m (inheritor)
			 * a_f_f_m  ->  f_m (NOT inheritor)
			 * a_m_f_m  ->  f_m (NOT inheritor)
			 */
			if (a_f_m_num[cnt] == 0 && a_kl_num[cnt] == 0 && a_k_num[cnt] == 0 && a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0)
			{
				if (a_f_f_m_num[cnt] == 1)
				{
					// Version 1.5, a_f_f_m & a_m_f_m are equally sharing now.
					a_f_f_m[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_f_f_m_num[cnt] + a_m_f_m_num[cnt]));
					a_f_f_m_note[cnt] = translations[3];
				}

				if (a_m_f_m_num[cnt] == 1)
				{
					a_m_f_m[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_f_f_m_num[cnt] + a_m_f_m_num[cnt]));
					a_m_f_m_note[cnt] = translations[4];
				}
			}

			if (a_d_d_num[cnt] > 0)
			{
				a_d_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_d_num[cnt] + a_s_d_num[cnt]));
				a_d_d_note[cnt] = translations[5];
			}

			if (a_s_d_num[cnt] > 0)
			{
				a_s_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_d_num[cnt] + a_s_d_num[cnt]));
				a_s_d_note[cnt] = translations[6];
			}

			if (a_s_d_s_num[cnt] > 0)
			{
				a_s_d_s[cnt] = divide(multiply(multiply(d_s[cnt], d_s_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_d_s_num[cnt] + a_s_d_s_num[cnt]));
				a_s_d_s_note[cnt] = translations[7];
			}

			if (a_d_d_s_num[cnt] > 0)
			{
				a_d_d_s[cnt] = divide(multiply(multiply(d_s[cnt], d_s_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_d_s_num[cnt] + a_s_d_s_num[cnt]));
				a_d_d_s_note[cnt] = translations[8];
			}

			if (a_s_d_d_num[cnt] > 0 && a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0)
			{
				a_s_d_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_d_d_num[cnt] + a_d_d_d_num[cnt] + a_s_s_d_num[cnt] + a_d_s_d_num[cnt]));
				a_s_d_d_note[cnt] = translations[9];
			}

			if (a_d_d_d_num[cnt] > 0 && a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0)
			{
				a_d_d_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_d_d_num[cnt] + a_d_d_d_num[cnt] + a_s_s_d_num[cnt] + a_d_s_d_num[cnt]));
				a_d_d_d_note[cnt] = translations[10];
			}

			if (a_s_s_d_num[cnt] > 0 && a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0)
			{
				a_s_s_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_d_d_num[cnt] + a_d_d_d_num[cnt] + a_s_s_d_num[cnt] + a_d_s_d_num[cnt]));
				a_s_s_d_note[cnt] = translations[11];
			}

			if (a_d_s_d_num[cnt] > 0 && a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0)
			{
				a_d_s_d[cnt] = divide(multiply(multiply(d[cnt], d_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_d_d_num[cnt] + a_d_d_d_num[cnt] + a_s_s_d_num[cnt] + a_d_s_d_num[cnt]));
				a_d_s_d_note[cnt] = translations[12];
			}

			if (a_s_mb_num[cnt] > 0)
			{
				a_s_mb[cnt] = divide(multiply(multiply(m_b[cnt], m_b_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_mb_num[cnt] + a_s_mb_num[cnt]));
				a_s_mb_note[cnt] = translations[13];
			}

			if (a_d_mb_num[cnt] > 0)
			{
				a_d_mb[cnt] = divide(multiply(multiply(m_b[cnt], m_b_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_mb_num[cnt] + a_s_mb_num[cnt]));
				a_d_mb_note[cnt] = translations[14];
			}

			if (a_s_msister_num[cnt] > 0)
			{
				a_s_msister[cnt] = divide(multiply(multiply(m_sister[cnt], m_sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_msister_num[cnt] + a_s_msister_num[cnt]));
				a_s_msister_note[cnt] = translations[15];
			}

			if (a_d_msister_num[cnt] > 0)
			{
				a_d_msister[cnt] = divide(multiply(multiply(m_sister[cnt], m_sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_msister_num[cnt] + a_s_msister_num[cnt]));
				a_d_msister_note[cnt] = translations[16];
			}

			if (a_d_b_num[cnt] > 0)
			{
				a_d_b[cnt] = divide(multiply(multiply(b[cnt], b_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), a_d_b_num[cnt]);
				a_d_b_note[cnt] = translations[17];
			}

			if (a_s_sister_num[cnt] > 0)
			{
				a_s_sister[cnt] = divide(multiply(multiply(sister[cnt], sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_sister_num[cnt] + a_s_sister_num[cnt]));
				a_s_sister_note[cnt] = translations[18];
			}

			if (a_d_sister_num[cnt] > 0)
			{
				a_d_sister[cnt] = divide(multiply(multiply(sister[cnt], sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_sister_num[cnt] + a_s_sister_num[cnt]));
				a_d_sister_note[cnt] = translations[19];
			}

			if (a_d_fb_num[cnt] > 0)
			{
				a_d_fb[cnt] = divide(multiply(multiply(f_b[cnt], f_b_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), a_d_fb_num[cnt]);
				a_d_fb_note[cnt] = translations[20];
			}

			if (a_s_fsister_num[cnt] > 0)
			{
				a_s_fsister[cnt] = divide(multiply(multiply(f_sister[cnt], f_sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_fsister_num[cnt] + a_s_fsister_num[cnt]));
				a_s_fsister_note[cnt] = translations[21];
			}

			if (a_d_fsister_num[cnt] > 0)
			{
				a_d_fsister[cnt] = divide(multiply(multiply(f_sister[cnt], f_sister_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_fsister_num[cnt] + a_s_fsister_num[cnt]));
				a_d_fsister_note[cnt] = translations[22];
			}

			if (a_ul_num[cnt] > 0)
			{
				a_ul[cnt] = divide(multiply(f[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), a_ul_num[cnt]);
				a_ul_note[cnt] = translations[23];
			}

			if (a_ul_num[cnt] == 0)
			{
				if (a_s_ul_num[cnt] > 0)
				{
					a_s_ul[cnt] = divide(multiply(f[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_ul_num[cnt] + a_s_ul_num[cnt]));
					a_s_ul_note[cnt] = translations[24];
				}

				if (a_d_ul_num[cnt] > 0)
				{
					a_d_ul[cnt] = divide(multiply(f[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_ul_num[cnt] + a_s_ul_num[cnt]));
					a_d_ul_note[cnt] = translations[25];
				}
			}

			if (a_d_u_num[cnt] > 0)
			{
				a_d_u[cnt] = divide(multiply(multiply(u[cnt], u_num[cnt]), subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), a_d_u_num[cnt]);
				a_d_u_note[cnt] = translations[26];
			}

			// It was: if(a_f_m_num[cnt]==0 && a_f_f_m_num[cnt]==0 && a_m_f_m_num[cnt]==0)
			if (a_f_m_num[cnt] == 0 /* Version 1.7, Removed: && a_f_f_m_num[cnt]==0 */)
			{
				if (a_kl_num[cnt] > 0)
				{
					a_kl[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_kl_num[cnt] + a_k_num[cnt]));
					a_kl_note[cnt] = translations[27];
				}

				if (a_k_num[cnt] > 0)
				{
					a_k[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_kl_num[cnt] + a_k_num[cnt]));
					a_k_note[cnt] = translations[28];
				}

				if (a_k_num[cnt] == 0 && a_kl_num[cnt] == 0 /* Version 1.7 */ && a_f_f_m_num[cnt] == 0 && a_m_f_m_num[cnt] == 0 && a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0)
				{
					if (a_s_k_num[cnt] > 0)
					{
						a_s_k[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_k_num[cnt] + a_s_k_num[cnt] + a_s_kl_num[cnt] + a_d_kl_num[cnt]));
						a_s_k_note[cnt] = translations[29];
					}

					if (a_d_k_num[cnt] > 0)
					{
						a_d_k[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_d_k_num[cnt] + a_s_k_num[cnt] + a_s_kl_num[cnt] + a_d_kl_num[cnt]));
						a_d_k_note[cnt] = translations[30];
					}
					//}

					//if(a_kl_num[cnt]==0 && a_k_num[cnt]==0)	// if(a_kl_num[cnt]==0).........check
					//{
					if (a_s_kl_num[cnt] > 0)
					{
						a_s_kl[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_kl_num[cnt] + a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_k_num[cnt]));
						a_s_kl_note[cnt] = translations[31];
					}

					if (a_d_kl_num[cnt] > 0)
					{
						a_d_kl[cnt] = divide(multiply(m[cnt], subtract(subtract(1, h[cnt]), multiply(w[cnt], w_num[cnt]))), (a_s_kl_num[cnt] + a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_k_num[cnt]));
						a_d_kl_note[cnt] = translations[32];
					}
				}
			}
		}
		else
		{
			String husbandOrWife = "1/1";

			// Version 1.7
			if (h_num[cnt] == 1)
			{
				h[cnt] = "1/2";
				h_note[cnt] = translations[0];
				husbandOrWife = "1/2";
			}

			// Version 1.7
			if (w_num[cnt] >= 1)
			{
				w[cnt] = "1/4";
				w[cnt] = divide(w[cnt], w_num[cnt]);
				w_note[cnt] = translations[1];
				husbandOrWife = "3/4";
			}

			// Madaheb==4 hanafi
			//first child
			if (a_d_d_num[cnt] > 0)
			{
				a_d_d[cnt] = "1/" + (a_d_d_num[cnt] + a_s_d_num[cnt] * 2);
				a_d_d[cnt] = multiply(a_d_d[cnt], husbandOrWife);
				a_d_d_note[cnt] = translations[33];
			}

			if (a_s_d_num[cnt] > 0)
			{
				a_s_d[cnt] = "2/" + (a_d_d_num[cnt] + a_s_d_num[cnt] * 2);
				a_s_d[cnt] = multiply(a_s_d[cnt], husbandOrWife);
				a_s_d_note[cnt] = translations[34];
			}

			if (a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0)
			{
				if (a_d_d_s_num[cnt] > 0)
				{
					a_d_d_s[cnt] = "1/" + (a_d_d_s_num[cnt] + a_s_d_s_num[cnt] * 2);
					a_d_d_s[cnt] = multiply(a_d_d_s[cnt], husbandOrWife);
					a_d_d_s_note[cnt] = translations[35];
				}

				if (a_s_d_s_num[cnt] > 0)
				{
					a_s_d_s[cnt] = "2/" + (a_d_d_s_num[cnt] + a_s_d_s_num[cnt] * 2);
					a_s_d_s[cnt] = multiply(a_s_d_s[cnt], husbandOrWife);
					a_s_d_s_note[cnt] = translations[36];
				}

				if ((a_s_d_s_num[cnt] == 0 && a_d_d_s_num[cnt] == 0)/* Version 1.3 || madaheb!=4*/)
				{
					if (a_s_d_d_num[cnt] > 0)
					{
						a_s_d_d[cnt] = "2/" + (a_d_d_d_num[cnt] + a_d_s_d_num[cnt] + a_s_d_d_num[cnt] * 2 + a_s_s_d_num[cnt] * 2);
						a_s_d_d[cnt] = multiply(a_s_d_d[cnt], husbandOrWife);
						a_s_d_d_note[cnt] = translations[37];
					}

					if (a_d_d_d_num[cnt] > 0)
					{
						a_d_d_d[cnt] = "1/" + (a_d_d_d_num[cnt] + a_d_s_d_num[cnt] + a_s_d_d_num[cnt] * 2 + a_s_s_d_num[cnt] * 2);
						a_d_d_d[cnt] = multiply(a_d_d_d[cnt], husbandOrWife);
						a_d_d_d_note[cnt] = translations[38];
					}

					if (a_s_s_d_num[cnt] > 0)
					{
						a_s_s_d[cnt] = "2/" + (a_d_d_d_num[cnt] + a_d_s_d_num[cnt] + a_s_d_d_num[cnt] * 2 + a_s_s_d_num[cnt] * 2);
						a_s_s_d[cnt] = multiply(a_s_s_d[cnt], husbandOrWife);
						a_s_s_d_note[cnt] = translations[39];
					}

					if (a_d_s_d_num[cnt] > 0)
					{
						a_d_s_d[cnt] = "1/" + (a_d_d_d_num[cnt] + a_d_s_d_num[cnt] + a_s_d_d_num[cnt] * 2 + a_s_s_d_num[cnt] * 2);
						a_d_s_d[cnt] = multiply(a_d_s_d[cnt], husbandOrWife);
						a_d_s_d_note[cnt] = translations[40];
					}
				}
			}

			if (a_d_d_num[cnt] == 0 && a_s_d_num[cnt] == 0 && a_s_d_s_num[cnt] == 0 && a_d_d_s_num[cnt] == 0 /* Version 1.5, Add all a_?_?_d to avoid errors*/ && a_s_d_d_num[cnt] == 0 && a_d_d_d_num[cnt] == 0 && a_s_s_d_num[cnt] == 0 && a_d_s_d_num[cnt] == 0)
			{
				//second fathers
				if (a_f_m_num[cnt] != 0)
				{
					a_f_m[cnt] = "1/1";
					a_f_m[cnt] = multiply(a_f_m[cnt], husbandOrWife);
					a_f_m_note[cnt] = translations[41];
				}

				if (a_f_m_num[cnt] == 0)
				{
					if (a_f_m_f_num[cnt] != 0)
						if (a_f_m_m_num[cnt] == 0)
						{
							a_f_m_f[cnt] = "1/1";
							a_f_m_f[cnt] = multiply(a_f_m_f[cnt], husbandOrWife);
							a_f_m_f_note[cnt] = translations[42];
						}
						else
						{
							a_f_m_f[cnt] = "2/3";
							a_f_m_f[cnt] = multiply(a_f_m_f[cnt], husbandOrWife);
							a_f_m_f_note[cnt] = translations[43];
						}

					if (a_f_m_m_num[cnt] != 0)
						if (a_f_m_f_num[cnt] == 0)
						{
							a_f_m_m[cnt] = "1/1";
							a_f_m_m[cnt] = multiply(a_f_m_m[cnt], husbandOrWife);
							a_f_m_m_note[cnt] = translations[44];
						}
						else
						{
							a_f_m_m[cnt] = "1/3";
							a_f_m_m[cnt] = multiply(a_f_m_m[cnt], husbandOrWife);
							a_f_m_m_note[cnt] = translations[45];
						}

					if (a_f_m_f_num[cnt] == 0 && a_f_m_m_num[cnt] == 0)
					{
						if (a_f_f_m_num[cnt] != 0)
							if (a_m_f_m_num[cnt] == 0)
							{
								a_f_f_m[cnt] = "1/1";
								a_f_f_m[cnt] = multiply(a_f_f_m[cnt], husbandOrWife);
								a_f_f_m_note[cnt] = translations[46];
							}
							else
							{
								a_f_f_m[cnt] = "2/3";
								a_f_f_m[cnt] = multiply(a_f_f_m[cnt], husbandOrWife);
								a_f_f_m_note[cnt] = translations[47];
							}

						if (a_m_f_m_num[cnt] != 0)
							if (a_f_f_m_num[cnt] == 0)
							{
								a_m_f_m[cnt] = "1/1";
								a_m_f_m[cnt] = multiply(a_m_f_m[cnt], husbandOrWife);
								a_m_f_m_note[cnt] = translations[48];
							}
							else
							{
								a_m_f_m[cnt] = "1/3";
								a_m_f_m[cnt] = multiply(a_m_f_m[cnt], husbandOrWife);
								a_m_f_m_note[cnt] = translations[49];
							}

						if (a_f_f_m_num[cnt] == 0 && a_m_f_m_num[cnt] == 0)
						{
							//third brothers
							if (a_d_b_num[cnt] > 0)
							{
								a_d_b[cnt] = "1/" + (a_d_b_num[cnt] + a_s_sister_num[cnt] * 2 + a_d_sister_num[cnt]);
								a_d_b[cnt] = multiply(a_d_b[cnt], husbandOrWife);
								a_d_b_note[cnt] = translations[50];
							}

							if (a_s_sister_num[cnt] > 0)
							{
								a_s_sister[cnt] = "2/" + (a_d_b_num[cnt] + a_s_sister_num[cnt] * 2 + a_d_sister_num[cnt]);
								a_s_sister[cnt] = multiply(a_s_sister[cnt], husbandOrWife);
								a_s_sister_note[cnt] = translations[51];
							}

							if (a_d_sister_num[cnt] > 0)
							{
								a_d_sister[cnt] = "1/" + (a_d_b_num[cnt] + a_s_sister_num[cnt] * 2 + a_d_sister_num[cnt]);
								a_d_sister[cnt] = multiply(a_d_sister[cnt], husbandOrWife);
								a_d_sister_note[cnt] = translations[52];
							}

							if (a_d_sister_num[cnt] == 0 && a_s_sister_num[cnt] == 0 && a_d_b_num[cnt] == 0)
							{
								if (a_d_fb_num[cnt] > 0)
								{
									a_d_fb[cnt] = "1/" + (a_d_fb_num[cnt] + a_s_fsister_num[cnt] * 2 + a_d_fsister_num[cnt]);
									a_d_fb[cnt] = multiply(a_d_fb[cnt], husbandOrWife);
									a_d_fb_note[cnt] = translations[53];
								}

								if (a_s_fsister_num[cnt] > 0)
								{
									a_s_fsister[cnt] = "2/" + (a_d_fb_num[cnt] + a_s_fsister_num[cnt] * 2 + a_d_fsister_num[cnt]);
									a_s_fsister[cnt] = multiply(a_s_fsister[cnt], husbandOrWife);
									a_s_fsister_note[cnt] = translations[54];
								}

								if (a_d_fsister_num[cnt] > 0)
								{
									a_d_fsister[cnt] = "1/" + (a_d_fb_num[cnt] + a_s_fsister_num[cnt] * 2 + a_d_fsister_num[cnt]);
									a_d_fsister[cnt] = multiply(a_d_fsister[cnt], husbandOrWife);
									a_d_fsister_note[cnt] = translations[55];
								}

								if (a_d_fb_num[cnt] == 0 && a_s_fsister_num[cnt] == 0 && a_d_fsister_num[cnt] == 0)
								{
									if (a_s_mb_num[cnt] > 0)
									{
										a_s_mb[cnt] = "2/" + (a_d_mb_num[cnt] + a_s_mb_num[cnt] * 2 + a_s_msister_num[cnt] * 2 + a_d_msister_num[cnt]);
										a_s_mb[cnt] = multiply(a_s_mb[cnt], husbandOrWife);
										a_s_mb_note[cnt] = translations[56];
									}

									if (a_d_mb_num[cnt] > 0)
									{
										a_d_mb[cnt] = "1/" + (a_d_mb_num[cnt] + a_s_mb_num[cnt] * 2 + a_s_msister_num[cnt] * 2 + a_d_msister_num[cnt]);
										a_d_mb[cnt] = multiply(a_d_mb[cnt], husbandOrWife);
										a_d_mb_note[cnt] = translations[57];
									}

									if (a_s_msister_num[cnt] > 0)
									{
										a_s_msister[cnt] = "2/" + (a_d_mb_num[cnt] + a_s_mb_num[cnt] * 2 + a_s_msister_num[cnt] * 2 + a_d_msister_num[cnt]);
										a_s_msister[cnt] = multiply(a_s_msister[cnt], husbandOrWife);
										a_s_msister_note[cnt] = translations[58];
									}

									if (a_d_msister_num[cnt] > 0)
									{
										a_d_msister[cnt] = "1/" + (a_d_mb_num[cnt] + a_s_mb_num[cnt] * 2 + a_s_msister_num[cnt] * 2 + a_d_msister_num[cnt]);
										a_d_msister[cnt] = multiply(a_d_msister[cnt], husbandOrWife);
										a_d_msister_note[cnt] = translations[59];
									}

									if (a_d_mb_num[cnt] == 0 && a_s_msister_num[cnt] == 0 && a_d_msister_num[cnt] == 0)
									{
										//fouth Fant
										if (a_ul_num[cnt] != 0)
											if (a_kl_num[cnt] == 0 && a_k_num[cnt] == 0)
											{
												a_ul[cnt] = "1/1";
												a_ul[cnt] = multiply(a_ul[cnt], husbandOrWife);
												a_ul_note[cnt] = translations[60];
											}
											else
											{
												a_ul[cnt] = "2/3";
												a_ul[cnt] = multiply(a_ul[cnt], husbandOrWife);
												a_ul_note[cnt] = translations[61];
											}

										if (a_kl_num[cnt] > 0)
										{
											// Version 1.5, To avoid errors when there is only a_kl.
											if (a_ul_num[cnt] != 0)
											{
												a_kl[cnt] = "1/" + (3 * (a_kl_num[cnt] + a_k_num[cnt] * 2));
												a_kl[cnt] = multiply(a_kl[cnt], husbandOrWife);
												a_kl_note[cnt] = translations[62];
											}
											else
											{
												a_kl[cnt] = "1/" + (a_kl_num[cnt] + a_k_num[cnt] * 2);
												a_kl[cnt] = multiply(a_kl[cnt], husbandOrWife);
												a_kl_note[cnt] = translations[63];
											}
										}

										if (a_k_num[cnt] > 0)
										{
											// Version 1.5
											if (a_ul_num[cnt] != 0)
											{
												a_k[cnt] = "2/" + (3 * (a_kl_num[cnt] + a_k_num[cnt] * 2));
												a_k[cnt] = multiply(a_k[cnt], husbandOrWife);
												a_k_note[cnt] = translations[64];
											}
											else
											{
												a_k[cnt] = "2/" + (a_kl_num[cnt] + a_k_num[cnt] * 2);
												a_k[cnt] = multiply(a_k[cnt], husbandOrWife);
												a_k_note[cnt] = translations[65];
											}
										}

										if (a_ul_num[cnt] == 0 && a_kl_num[cnt] == 0 && a_k_num[cnt] == 0)
										{
											if (a_d_u_num[cnt] != 0)
											{
												if (a_s_kl_num[cnt] == 0 && a_d_kl_num[cnt] == 0 && a_s_k_num[cnt] == 0 && a_d_k_num[cnt] == 0)
												{
													a_d_u[cnt] = "1/1";
													a_d_u[cnt] = multiply(a_d_u[cnt], husbandOrWife);
													a_d_u_note[cnt] = translations[66];
												}
												else
												{
													a_d_u[cnt] = "2/3";
													a_d_u[cnt] = multiply(a_d_u[cnt], husbandOrWife);
													a_d_u_note[cnt] = translations[67];
												}

												if (a_s_kl_num[cnt] > 0)
												{
													a_s_kl[cnt] = "2/" + (3 * (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2));
													a_s_kl[cnt] = multiply(a_s_kl[cnt], husbandOrWife);
													a_s_kl_note[cnt] = translations[68];
												}

												if (a_d_kl_num[cnt] > 0)
												{
													a_d_kl[cnt] = "1/" + (3 * (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2));
													a_d_kl[cnt] = multiply(a_d_kl[cnt], husbandOrWife);
													a_d_kl_note[cnt] = translations[69];
												}

												if (a_d_k_num[cnt] > 0)
												{
													a_d_k[cnt] = "1/" + (3 * (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2));
													a_d_k[cnt] = multiply(a_d_k[cnt], husbandOrWife);
													a_d_k_note[cnt] = translations[70];
												}

												// Version 1.5, Missing causing errors in calculating a_s_k
												if (a_s_k_num[cnt] > 0)
												{
													a_s_k[cnt] = "2/" + (3 * (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2));
													a_s_k[cnt] = multiply(a_s_k[cnt], husbandOrWife);
													a_s_k_note[cnt] = translations[71];
												}
											}
											else
											{
												if (a_s_kl_num[cnt] > 0)
												{
													a_s_kl[cnt] = "2/" + (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2);
													a_s_kl[cnt] = multiply(a_s_kl[cnt], husbandOrWife);
													a_s_kl_note[cnt] = translations[72];
												}

												if (a_d_kl_num[cnt] > 0)
												{
													a_d_kl[cnt] = "1/" + (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2);
													a_d_kl[cnt] = multiply(a_d_kl[cnt], husbandOrWife);
													a_d_kl_note[cnt] = translations[73];
												}

												if (a_d_k_num[cnt] > 0)
												{
													a_d_k[cnt] = "1/" + (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2);
													a_d_k[cnt] = multiply(a_d_k[cnt], husbandOrWife);
													a_d_k_note[cnt] = translations[74];
												}

												if (a_s_k_num[cnt] > 0)
												{
													a_s_k[cnt] = "2/" + (a_d_kl_num[cnt] + a_d_k_num[cnt] + a_s_kl_num[cnt] * 2 + a_s_k_num[cnt] * 2);
													a_s_k[cnt] = multiply(a_s_k[cnt], husbandOrWife);
													a_s_k_note[cnt] = translations[75];
												}
											}

											if (a_d_u_num[cnt] == 0 /* Version 1.5*/ && a_s_kl_num[cnt] == 0 && a_d_kl_num[cnt] == 0 && a_s_k_num[cnt] == 0 && a_d_k_num[cnt] == 0)
											{
												if (a_d_ul_num[cnt] > 0)
												{
													a_d_ul[cnt] = "1/" + (a_d_ul_num[cnt] + a_s_ul_num[cnt] * 2);
													a_d_ul[cnt] = multiply(a_d_ul[cnt], husbandOrWife);
													a_d_ul_note[cnt] = translations[76];
												}

												if (a_s_ul_num[cnt] > 0)
												{
													a_s_ul[cnt] = "2/" + (a_d_ul_num[cnt] + a_s_ul_num[cnt] * 2);
													a_s_ul[cnt] = multiply(a_s_ul[cnt], husbandOrWife);
													a_s_ul_note[cnt] = translations[77];
												}
											}

											if (a_d_u_num[cnt] == 0 && a_s_kl_num[cnt] == 0 && a_d_k_num[cnt] == 0 /* Version 1.5*/ && a_s_k_num[cnt] == 0 && a_d_kl_num[cnt] == 0 && a_d_ul_num[cnt] == 0 && a_s_ul_num[cnt] == 0)
												muslim_trusts[cnt] = "1/1";
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

	public static String HTMLFreeText(String HTMLText)
	{
		// Version 2.0, Removed.
		//HTMLText.replaceAll("<wbr>", "");

		String HTMLTextUpdate = "";
		boolean contStore = true;
		for (int i = 0; i < HTMLText.length(); i++)
		{
			if (HTMLText.charAt(i) == '<')
				contStore = false;
			else
			{
				if (contStore)
					HTMLTextUpdate = HTMLTextUpdate + HTMLText.charAt(i);

				if (HTMLText.charAt(i) == '>')
					contStore = true;
			}
		}

		// Version 1.9, To display Arabic numbers
		if (MaknoonIslamicEncyclopedia.language)
		{
			final char[] charArray = HTMLTextUpdate.toCharArray();
			final NumericShaper shaper = NumericShaper.getShaper(NumericShaper.ARABIC);
			shaper.shape(charArray, 0, charArray.length);

			return new String(charArray);
		}
		else
			return HTMLTextUpdate;
	}

	// Version 1.1, This function is used to set the precision of the double in displaying the results.
	static String round(final double num)
	{
		BigDecimal val = new BigDecimal(num);
		val = val.setScale(2, RoundingMode.DOWN);
		return val.toPlainString();
	}

	// Version 1.1, This function is used to work as setEnabled(false) for JPanel since it has a problem.
	private static void enabledContainer(Container cont, boolean enabled)
	{
		final Component[] components = cont.getComponents();
		for (Component c : components)
		{
			// Version 2.0, Enable/disable JComboBox in case you choose 3 s_s, then choose s, the s_s combobox will not be disable.
			if (c instanceof JPanel)
			{
				final String buttonLabel = MaknoonIslamicEncyclopedia.language ? "نعم" : "Yes";
				final Component[] panelComponents = ((Container) c).getComponents();
				boolean yes = false;
				for (Component panelC : panelComponents)
				{
					if (panelC instanceof JRadioButton)
					{
						panelC.setEnabled(enabled);
						final JRadioButton button = (JRadioButton) panelC;
						if (button.getText().equals(buttonLabel))
							yes = button.isSelected();
					}
					else // i.e. JComboBox. It should be the last (in order) in the panelComponents.
					{
						if (enabled)
						{
							if (yes)
								panelC.setEnabled(enabled);
						}
						else
							panelC.setEnabled(enabled);
					}
				}
			}
			else
				c.setEnabled(enabled); // i.e. JLabel

            /* Version 2.0, Replaced by above
			if(c instanceof JComboBox)
                break;
            else
                c.setEnabled(enabled);

			if(c instanceof Container)
				enabledContainer((Container)c, enabled);
		    */
		}
	}

	String multiply(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		if (number1Numerator == 0 || number2Numerator == 0) return "0/1";

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1Numerator > (Integer.MAX_VALUE / number2Numerator) || number1Denominator > (Integer.MAX_VALUE / number2Denominator))
			throw new OverFlowException("Multiply String x String");

		return simplify(String.valueOf(number1Numerator * number2Numerator) + '/' + (number1Denominator * number2Denominator));
	}

	String multiply(final String number1, final int number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		if (number1Numerator == 0 || number2 == 0) return "0/1";

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1Numerator > (Integer.MAX_VALUE / number2))
			throw new OverFlowException("Multiply String x int");

		return simplify(String.valueOf(number1Numerator * number2) + '/' + number1Denominator);
	}

	String multiply(final int number1, final String number2)
	{
		return multiply(number2, number1);
	}

	/*
	 * This function can be reduced by the multiply function but we will use it
	 * with this name to not loss the control of the code.
	 */
	String divide(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		if (number1Denominator == 0 || number2Numerator == 0)
		{
			System.out.println("Mawareth System1: Divide by zero");
			return "0/1";
		}

		if (number1Numerator == 0 || number2Denominator == 0) return "0/1";

		// Version 1.4, Handling overflow exceptions (if all the input parametrs are +ve and each one is int). This couses alot of wronge errors in many cases.
		if (number1Numerator > (Integer.MAX_VALUE / number2Denominator) || number1Denominator > (Integer.MAX_VALUE / number2Numerator))
			throw new OverFlowException("Divide String x String");

		return simplify(String.valueOf(number1Numerator * number2Denominator) + '/' + (number1Denominator * number2Numerator));
	}

	String divide(final String number1, final int number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		if (number1Denominator == 0 || number2 == 0)
		{
			System.out.println("Mawareth System2: Divide by zero");
			return "0/1";
		}

		if (number1Numerator == 0) return "0/1";

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1Denominator > (Integer.MAX_VALUE / number2))
			throw new OverFlowException("Divide String x int");

		return simplify(String.valueOf(number1Numerator) + '/' + (number1Denominator * number2));
	}

	String divide(final int number1, final String number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		if (number2Numerator == 0)
		{
			System.out.println("Mawareth System3: Divide by zero");
			return "0/1";
		}

		if (number1 == 0 || number2Denominator == 0) return "0/1";

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1 > (Integer.MAX_VALUE / number2Denominator))
			throw new OverFlowException("Divide int x String");

		return simplify(String.valueOf(number1 * number2Denominator) + '/' + number2Numerator);
	}

	// Version 1.7, Updated to handle multiple numbers instead of 2
	String add(final String[] numbers)
	{
		if (numbers.length < 2)
		{
			System.out.println("Invalid arguments count, add(String[])");
			return "0/1";
		}

		final int[] numbersNumerators = new int[numbers.length];
		final int[] numbersDenominators = new int[numbers.length];

		StringTokenizer tokens;
		for (int i = 0; i < numbers.length; i++)
		{
			tokens = new StringTokenizer(numbers[i], "/");
			numbersNumerators[i] = Integer.parseInt(tokens.nextToken());
			numbersDenominators[i] = Integer.parseInt(tokens.nextToken());
		}

		final int denominator = LCD(numbersDenominators);
		int numerator = 0;
		for (int i = 0; i < numbers.length; i++)
			numerator = numerator + (numbersNumerators[i] * (denominator / numbersDenominators[i]));

		return simplify(String.valueOf(numerator) + '/' + denominator);
	}

    /*
	public String add(final String number1, final int number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.valueOf(tokens.nextToken());
		final int number1Denominator = Integer.valueOf(tokens.nextToken());

		// Version 1.4, Handling overflow exceptions (if all the input parametrs are +ve and each one is int). This couses alot of wronge errors in many cases.
		if(number2 > (Integer.MAX_VALUE/number1Denominator))
			throw new OverFlowException("Add String x int");
		else
			// i.e. all of the multiplications are within the range.
			if(number1Numerator > (Integer.MAX_VALUE - (number2*number1Denominator)))
				throw new OverFlowException("Add String x int");

		return simplify(String.valueOf(number1Numerator+number2*number1Denominator)+"/"+String.valueOf(number1Denominator));
	}
	*/

	//public String add(final int number1, final String number2){return add(number2, number1);}

	/*
	 * This function can be reduced by the add function but we will use it
	 * with this name to not loss the control of the code.
	 */
	String subtract(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1Numerator > (Integer.MAX_VALUE / number2Denominator) || number2Numerator > (Integer.MAX_VALUE / number1Denominator) || number1Denominator > (Integer.MAX_VALUE / number2Denominator))
			throw new OverFlowException("Subtract String x String");

		return simplify(String.valueOf(number1Numerator * number2Denominator - number2Numerator * number1Denominator) + '/' + (number1Denominator * number2Denominator));
	}

    /*
	public String subtract(final String number1, final int number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.valueOf(tokens.nextToken());
		final int number1Denominator = Integer.valueOf(tokens.nextToken());

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if(number2 > (Integer.MAX_VALUE/number1Denominator))
			throw new OverFlowException("Subtract String x int");

		return simplify(String.valueOf(number1Numerator-number2*number1Denominator)+"/"+String.valueOf(number1Denominator));
	}
	*/

	String subtract(final int number1, final String number2)
	{
		final StringTokenizer tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		// Version 1.4, Handling overflow exceptions (if all the input parameters are +ve and each one is int). This causes a lot of wrong errors in many cases.
		if (number1 > (Integer.MAX_VALUE / number2Denominator))
			throw new OverFlowException("Subtract int x String");

		return simplify(String.valueOf(number1 * number2Denominator - number2Numerator) + '/' + number2Denominator);
	}

	private static Vector<Integer> decompose(int number)
	{
		final Vector<Integer> ret = new Vector<>(10, 10);

		/*
		 * Version 1.4
		 * This is to prevent Integer.MAX_VALUE (2147483647) in LCD which takes huge time and processing power
		 * to do nothing. In addition Integer.MAX_VALUE + 1 gives -ve which we don't want.
		 * As a result this will led the whole LCD to be 0 so that we can indicate the user that
		 * the denominator is huge and unrealistic.
		 *
		 * N.B. This is done in LCD.
		 *
		if(number <= 0 || number == Integer.MAX_VALUE)
		{
			ret.addElement(0);
			return ret;
		}
		*/

		for (int i = 2; i <= number; i++)
		{
			while (number % i == 0)
			{
				ret.addElement(i);
				number = number / i;
			}
			if (number == 1) break;
		}
		return ret;
	}

	String simplify(final String number)
	{
		boolean isNegative = false;
		final StringTokenizer tokens = new StringTokenizer(number, "/");
		int numberNumerator = Integer.parseInt(tokens.nextToken());
		int numberDenominator = Integer.parseInt(tokens.nextToken());

		// Version 1.3, This function is developed to handle -ve numbers
		if ((numberNumerator < 0 && numberDenominator > 0) || (numberDenominator < 0 && numberNumerator > 0))
			isNegative = true;

		numberNumerator = Math.abs(numberNumerator);
		numberDenominator = Math.abs(numberDenominator);

		if (numberNumerator == 0) return "0/1";

		final Vector<Integer> numeratorPrimeNumbers = decompose(numberNumerator);
		final Vector<Integer> denominatorPrimeNumbers = decompose(numberDenominator);

		final int[] numeratorPrimes = new int[numeratorPrimeNumbers.size()];
		final int[] denominatorPrimes = new int[denominatorPrimeNumbers.size()];

		for (int k = 0; k < numeratorPrimeNumbers.size(); k++)
			numeratorPrimes[k] = numeratorPrimeNumbers.elementAt(k);

		for (int k = 0; k < denominatorPrimeNumbers.size(); k++)
			denominatorPrimes[k] = denominatorPrimeNumbers.elementAt(k);

		for (int k = 0; k < numeratorPrimeNumbers.size(); k++)
		{
			for (int j = 0; j < denominatorPrimeNumbers.size(); j++)
			{
				if (numeratorPrimes[k] == (denominatorPrimes[j]))
				{
					// initialise both to 1 i.e. they delete their effect
					numeratorPrimes[k] = 1;
					denominatorPrimes[j] = 1;
					break;
				}
			}
		}

		numberNumerator = 1;
		for (int i = 0; i < numeratorPrimeNumbers.size(); i++)
			numberNumerator *= numeratorPrimes[i];

		numberDenominator = 1;
		for (int i = 0; i < denominatorPrimeNumbers.size(); i++)
			numberDenominator *= denominatorPrimes[i];

		return (((!isNegative) ? String.valueOf(numberNumerator) : ('-' + String.valueOf(numberNumerator))) + '/' + numberDenominator);
	}

	private static boolean less(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());
		final double number1Total = (double) number1Numerator / number1Denominator;

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());
		final double number2Total = (double) number2Numerator / number2Denominator;

		return (number1Total < number2Total);
	}

	private static boolean bigger(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());
		final double number1Total = (double) number1Numerator / number1Denominator;

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());
		final double number2Total = (double) number2Numerator / number2Denominator;

		return (number1Total > number2Total);
	}

	// Version 1.7
	private static boolean equal(final String number1, final String number2)
	{
		StringTokenizer tokens = new StringTokenizer(number1, "/");
		final int number1Numerator = Integer.parseInt(tokens.nextToken());
		final int number1Denominator = Integer.parseInt(tokens.nextToken());

		tokens = new StringTokenizer(number2, "/");
		final int number2Numerator = Integer.parseInt(tokens.nextToken());
		final int number2Denominator = Integer.parseInt(tokens.nextToken());

		return ((number1Numerator * number2Denominator) == (number1Denominator * number2Numerator));
	}

	// Version 1.7, Updated to handle multiple denominators instead of 2
	int LCD(final int[] denominators)
	{
		if (denominators.length < 2)
		{
			System.out.println("Invalid arguments count, LCD");
			return 1;
		}

		int resultedLCD = denominators[0]; // Initially
		for (int a = 1; a < denominators.length; a++)
		{
			/*
			 * Version 1.4
			 * This is to prevent Integer.MAX_VALUE (2147483647) in LCD which takes huge time and processing power
			 * to do nothing. In addition Integer.MAX_VALUE + 1 gives -ve which we don't want.
			 * As a result this will led the whole LCD to be 0 so that we can indicate the user that
			 * the denominator is huge and unrealistic.
			 *
			 * This is to speed up the process if one is 0 and the other is huge
			 * i.e. near Integer.MAX_VALUE (2147483647).
			 */
			if (resultedLCD <= 0 || resultedLCD == Integer.MAX_VALUE || denominators[a] <= 0 || denominators[a] == Integer.MAX_VALUE)
				throw new OverFlowException("LCD exceeds Integer.MAX_VALUE");

			final Vector<Integer> resultedLCDPrimeNumbers = decompose(resultedLCD);
			final Vector<Integer> denominatorPrimeNumbers = decompose(denominators[a]);
			final Vector<Integer> allPrimeNumbers = new Vector<>(10, 10);

			allPrimeNumbers.addElement(1);
			int LCD = 1;

			for (int i = 0; i < resultedLCDPrimeNumbers.size(); i++)
			{
				boolean notFound = true;
				for (int j = 0; j < allPrimeNumbers.size(); j++)
				{
					if (resultedLCDPrimeNumbers.elementAt(i).equals(allPrimeNumbers.elementAt(j)))
					{
						notFound = false;
						break;
					}
				}

				if (notFound) allPrimeNumbers.addElement(resultedLCDPrimeNumbers.elementAt(i));
			}

			for (int i = 0; i < denominatorPrimeNumbers.size(); i++)
			{
				boolean notFound = true;
				for (int j = 0; j < allPrimeNumbers.size(); j++)
				{
					if (denominatorPrimeNumbers.elementAt(i).equals(allPrimeNumbers.elementAt(j)))
					{
						notFound = false;
						break;
					}
				}

				if (notFound) allPrimeNumbers.addElement(denominatorPrimeNumbers.elementAt(i));
			}

			// Start from 1 i.e. ignoring the first element which is 1 that is not in any one of them
			for (int i = 1; i < allPrimeNumbers.size(); i++)
			{
				int count = 0;
				for (int j = 0; j < resultedLCDPrimeNumbers.size(); j++)
					if (allPrimeNumbers.elementAt(i).equals(resultedLCDPrimeNumbers.elementAt(j))) count++;

				int max = count;
				count = 0;
				for (int j = 0; j < denominatorPrimeNumbers.size(); j++)
					if (allPrimeNumbers.elementAt(i).equals(denominatorPrimeNumbers.elementAt(j))) count++;

				if (max < count)
					max = count;

				LCD *= Math.pow(allPrimeNumbers.elementAt(i), max);
			}

			resultedLCD = LCD;
		}
		return resultedLCD;
	}

	private static int getDenominator(final String number)
	{
		final String[] tokens = number.split("/");
		return Integer.parseInt(tokens[1]);
	}

	private static int getNumerator(final String number)
	{
		final String[] tokens = number.split("/");
		return Integer.parseInt(tokens[0]);
	}

	// Version 1.4, This class is used to throw an exception if numeric operation overflow happens (exceeds Integer.MAX_VALUE 2147483647).
	class OverFlowException extends ArithmeticException
	{
		OverFlowException(String s)
		{
			super(s);
			if (MaknoonIslamicEncyclopedia.language)
				JOptionPane.showOptionDialog(MawarethSystem.this, "تجاوزت بعض العمليات الحسابية حتى الآن 2147483647 وهذا لن تحتاجه البتة في أية مسألة واقعية، وهذا لأنك قمت بإدخال أعداد كبيرة (كما في المناسخات المتعددة الطبقات)." + System.getProperty("line.separator") + "ولذلك عليك بإعادة الحساب مرة أخرى بالضغط على 'بدء حساب الفرائض من القائمة'", "خطأ", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"خروج"}, "خروج");
			else
				JOptionPane.showMessageDialog(MawarethSystem.this, "Some math operations exceed 2147483647 and you will not need this at all. It seems you entered huge numbers in your calculations (maybe in multi-level monasakha case)." + System.getProperty("line.separator") + "So you have to re-enter you numbers again. Press 'Start Mawareth' from the main menu.", "Error!", JOptionPane.ERROR_MESSAGE);

			// Re-Load the class again.
			mie.mawarethMenuItem.doClick();
		}
	}

	static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public void center(final JInternalFrame frame)
	{
		//final Dimension desktopSize = getSize();
		//final Dimension frameSize = frame.getSize();

		//final Rectangle desktopBounds = getBounds();
		final Rectangle frameBounds = frame.getBounds();
		//final int width = (desktopBounds.width - frameBounds.width) / 2;
		final int width = (screenSize.width - frameBounds.width) / 2;
		//final int height = (desktopBounds.height - frameBounds.height) / 2;
		final int height = (screenSize.height - frameBounds.height - 110) / 2;
		frame.setLocation(width, height);
	}
}