package druckvorschau;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.swing.JPanel;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class Druckvorschau extends JDialog {
	
	private static final double ZOLL = 72 / 2.54;
	
	JPanel panel;
	JLabel lblTest;
	JSlider slider;

	JScrollPane scrollPane;
	Seitenanzeige pages;

	Printable p;
	PageFormat pf;

	public Druckvorschau(Frame parent, Printable p, PageFormat pageFormat) {
		super(parent, "Druckvorschau", true);
		setBounds(parent.getX()+ 50, parent.getY() + 50, 887, 729);
		getContentPane().setLayout(null);

		this.p = p;
		if (pageFormat == null) {
			pf = PrinterJob.getPrinterJob().defaultPage();
		} else {
			pf = pageFormat;
		}
		
		panel = new JPanel();
		panel.setBounds(0, 0, 879, 80);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblTest = new JLabel("Test");
		lblTest.setText(String.format(	"%s: %4.1f - %4.1f\n",
										MediaSize.findMedia((float) (pf.getWidth() / ZOLL * 10), (float) (pf.getHeight() / ZOLL * 10), Size2DSyntax.MM),
										pf.getWidth() / ZOLL,
										pf.getHeight() / ZOLL)
		);
		lblTest.setBounds(10, 11, 224, 14);
		panel.add(lblTest);
		
		JButton btnndern = new JButton("Ändern");
		btnndern.setBounds(10, 38, 91, 23);
		btnndern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pf = PrinterJob.getPrinterJob().pageDialog(pf);
				lblTest.setText(String.format(	"%s: %4.1f - %4.1f\n",
						MediaSize.findMedia((float) (pf.getWidth() / ZOLL * 10), (float) (pf.getHeight() / ZOLL * 10), Size2DSyntax.MM),
						pf.getWidth() / ZOLL,
						pf.getHeight() / ZOLL)
				);
				pages.setPageFormat(pf);
			}
		});
		panel.add(btnndern);
		
		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(50);
		slider.setMaximum(400);
		slider.setValue(100);
		slider.setMinorTickSpacing(10);
		slider.setMinimum(50);
		slider.setBounds(195, 11, 350, 50);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pages.setScale(slider.getValue());
			}
			
		});
		panel.add(slider);
		
		JButton btnDrucken = new JButton("Drucken");
		btnDrucken.setBounds(625, 25, 91, 23);
		btnDrucken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrinterJob pjob = PrinterJob.getPrinterJob();
				MyPageable pageable = new MyPageable(pages.getNumberOfPages(), pf, p);
				pjob.setPageable(pageable);
			    if (pjob.printDialog() == false) return;
			    try {
					pjob.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnDrucken);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setBounds(737, 25, 100, 23);
		btnAbbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		panel.add(btnAbbrechen);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 80, 879, 622);
		getContentPane().add(scrollPane);
		
		pages = new Seitenanzeige(p, pageFormat);
		scrollPane.setViewportView(pages);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentResized(ComponentEvent e) {
				panel.setSize(getContentPane().getWidth(), 80);
				scrollPane.setSize(getContentPane().getWidth(), getContentPane().getHeight() - 80);
			}

			@Override
			public void componentShown(ComponentEvent e) {}
		});
	}

	public PageFormat showDialog() {
		setVisible(true);
		return pf;
	}
}