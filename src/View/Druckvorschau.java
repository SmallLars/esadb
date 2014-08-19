package View;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.Vector;

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
	
	private JPanel contentPane;

	JPanel panel;
	JLabel lblTest;
	JSlider slider;

	JScrollPane scrollPane;
	Seitenannzeige pages;

	Printable p;
	PageFormat pf;
	Vector<Seite> seiten;

	public Druckvorschau(JFrame parent, Printable p, PageFormat pageFormat) {
		super(parent, "Druckvorschau", true);
		setBounds(parent.getX()+ 50, parent.getY() + 50, 887, 729);

		this.p = p;
		if (pageFormat == null) {
			pf = PrinterJob.getPrinterJob().defaultPage();
		} else {
			pf = pageFormat;
		}

		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				//setVisible(false);
				//dispose();
			}
		});

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 879, 80);
		contentPane.add(panel);
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
		btnndern.setBounds(691, 11, 91, 23);
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
		slider.setMaximum(200);
		slider.setValue(100);
		slider.setMinorTickSpacing(10);
		slider.setMinimum(50);
		slider.setBounds(244, 11, 200, 50);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pages.setScale(slider.getValue());
			}
			
		});
		panel.add(slider);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 80, 879, 622);
		contentPane.add(scrollPane);
		
		pages = new Seitenannzeige(p, pageFormat);
		scrollPane.setViewportView(pages);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentResized(ComponentEvent e) {
				panel.setSize(contentPane.getWidth(), 80);
				scrollPane.setSize(contentPane.getWidth(), contentPane.getHeight() - 80);
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