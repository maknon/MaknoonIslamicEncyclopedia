package com.maknoon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DefaultLanguage extends JDialog
{
	private boolean arabicLanguage = true;
	DefaultLanguage(final MaknoonIslamicEncyclopedia mie)
	{
		super(mie, "اللغة المستخدمة (Default Language)", true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		final JPanel languageChoicePanel = new JPanel();
		languageChoicePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"اختر لغة البرنامج (Choose the program language)",0,0,null,Color.red));
		languageChoicePanel.setPreferredSize(new Dimension(400, 65));
		add(languageChoicePanel, BorderLayout.CENTER);

		final JRadioButton arabicLanguageRadioButton = new JRadioButton("العربية", true);
		final JRadioButton englishLanguageRadioButton = new JRadioButton("English");
		final ActionListener languageGroupListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(ae.getSource() == arabicLanguageRadioButton)
					arabicLanguage = true;
				if(ae.getSource() == englishLanguageRadioButton)
					arabicLanguage = false;
			}
		};
		arabicLanguageRadioButton.addActionListener(languageGroupListener);
		englishLanguageRadioButton.addActionListener(languageGroupListener);

		final ButtonGroup languageGroup = new ButtonGroup();
		languageGroup.add(arabicLanguageRadioButton);
		languageGroup.add(englishLanguageRadioButton);

		languageChoicePanel.add(arabicLanguageRadioButton);
		languageChoicePanel.add(englishLanguageRadioButton);

		final JPanel closePanel = new JPanel();
		add(closePanel, BorderLayout.SOUTH);

		final JButton closeButton = new JButton ("التالي (Next)");
		closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MaknoonIslamicEncyclopedia.language = arabicLanguage;
				dispose();
			}
		});

		closePanel.add(closeButton);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){closeButton.doClick();}});

		getContentPane().applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		englishLanguageRadioButton.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		pack();
		setLocationRelativeTo(null); // center in Screen. After pack()
		setVisible(true);
	}
}