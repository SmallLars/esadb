package View;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;


@SuppressWarnings("serial")
public class Druckvorschau extends JFrame {
	
	private static final double ZOLL = 72 / 2.54;
	
	private JPanel contentPane;

	JPanel panel;
	JLabel lblTest;
	JSlider slider;

	JScrollPane scrollPane;
	JPanel pages;

	Printable p;
	PageFormat pf;
	Vector<Seite> seiten;

	public Druckvorschau(JFrame parent, Printable p, PageFormat pageFormat) {
		super("Druckvorschau");
		setBounds(parent.getBounds());
		this.setIconImage(parent.getIconImage());
		parent.setEnabled(false);

		this.p = p;
		if (pageFormat == null) {
			pf = PrinterJob.getPrinterJob().defaultPage();
		} else {
			pf = pageFormat;
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				parent.setEnabled(true);
				dispose();
			}
		});

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 792, 80);
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
				showPages();
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
				Seite s = seiten.get(0);
				for (int i = 0; i < seiten.size(); i++) {
					s = seiten.get(i);
					s.setPageScale(slider.getValue());
					s.setLocation(10 + i * (s.getWidth() + 10), 10);
				}
				Dimension d = new Dimension(10 + seiten.size() * (s.getWidth() + 10), s.getHeight() + 20);
				pages.setPreferredSize(d);
				pages.repaint();
				scrollPane.revalidate();
			}
			
		});
		panel.add(slider);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 80, 792, 493);
		contentPane.add(scrollPane);
		
		pages = new JPanel();
		pages.setLayout(null);
		pages.setLocation(0, 0);
		pages.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		MouseInputAdapter dragScroll = new DragScroll(pages);
		pages.addMouseMotionListener(dragScroll);
		pages.addMouseListener(dragScroll);
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

		setVisible(true);
		showPages();
	}

	private void showPages() {
		lblTest.setText(String.format(	"%s: %4.1f - %4.1f\n",
				MediaSize.findMedia((float) (pf.getWidth() / ZOLL * 10), (float) (pf.getHeight() / ZOLL * 10), Size2DSyntax.MM),
				pf.getWidth() / ZOLL,
				pf.getHeight() / ZOLL)
		);

		pages.removeAll();
		if (seiten == null) {
			seiten = new Vector<Seite>();
		} else {
			seiten.clear();
		}

		int anzahl;
		for (anzahl = 0; true; anzahl++) {
			int c = Printable.NO_SUCH_PAGE;
			try {
				c = p.print(pages.getGraphics().create(0, 0, 0, 0), pf, anzahl);
			} catch (PrinterException e) {
				e.printStackTrace();
			}
			if (c == Printable.NO_SUCH_PAGE) break;
		}

		Seite s = new Seite(p, pf, 0);
		for (int i = 0; i < anzahl; i++) {
			s = new Seite(p, pf, i);
			s.setPageScale(slider.getValue());
			s.setLocation(10 + i * (s.getWidth() + 10), 10);
			pages.add(s);
			seiten.add(s);
		}
		
		Dimension d = new Dimension(10 + anzahl * (s.getWidth() + 10), s.getHeight() + 20);
		pages.setPreferredSize(d);
		pages.repaint();
	}
}