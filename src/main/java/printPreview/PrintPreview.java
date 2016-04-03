package printPreview;


import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JPanel;

import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class PrintPreview extends JDialog implements ActionListener {
	
	private static final double ZOLL = 72 / 2.54;
	
	JPanel panel;
	JLabel lblMediaName;
	JSlider slider;

	JScrollPane scrollPane;
	PageView pages;

	Printable p;
	PageFormat pf;

	public PrintPreview(Frame parent, Printable p, PageFormat pageFormat) {
		super(parent, "Druckvorschau", true);
		Dimension d = new Dimension(887, 500);
		setMinimumSize(d);
		setSize(d);
		if (getGraphicsConfiguration() != null) setLocationRelativeTo(parent);
		getContentPane().setLayout(null);

		this.p = p;
		if (pageFormat == null) {
			pf = PrinterJob.getPrinterJob().defaultPage();
		} else {
			pf = pageFormat;
		}
		
		panel = new JPanel();
		panel.setBounds(0, 0, 879, 69);
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnChange = new JButton("Papierformat ändern");
		btnChange.setBounds(10, 11, 170, 23);
		btnChange.setActionCommand("CHANGE");
		btnChange.addActionListener(this);
		panel.add(btnChange);

		JButton btnPrint = new JButton("Drucken");
		btnPrint.setBounds(190, 11, 100, 23);
		btnPrint.setActionCommand("PRINT");
		btnPrint.addActionListener(this);
		panel.add(btnPrint);

		JButton btnClose = new JButton("Schließen");
		btnClose.setBounds(300, 11, 100, 23);
		btnClose.setActionCommand("CANCEL");
		btnClose.addActionListener(this);
		panel.add(btnClose);

		lblMediaName = new JLabel("");
		lblMediaName.setBounds(10, 45, 390, 14);
		setMediaSizeInfo();
		panel.add(lblMediaName);

		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(50);
		slider.setMaximum(400);
		slider.setValue(100);
		slider.setMinorTickSpacing(10);
		slider.setMinimum(50);
		slider.setBounds(410, 11, 459, 47);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pages.setScale(slider.getValue());
			}
			
		});
		panel.add(slider);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 69, 879, 404);
		getContentPane().add(scrollPane);
		
		pages = new PageView(p, pageFormat);
		scrollPane.setViewportView(pages);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentResized(ComponentEvent e) {
				panel.setSize(getContentPane().getWidth(), 69);
				scrollPane.setSize(getContentPane().getWidth(), getContentPane().getHeight() - 69);
				slider.setSize(getContentPane().getWidth() - 420, 47);
			}

			@Override
			public void componentShown(ComponentEvent e) {}
		});
	}

	public PageFormat showDialog() {
		setVisible(true);
		return pf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "CHANGE":
				pf = PrinterJob.getPrinterJob().pageDialog(pf);
				setMediaSizeInfo();
				pages.setPageFormat(pf);
				break;
			case "PRINT":
				PrinterJob pjob = PrinterJob.getPrinterJob();
				pjob.setPageable(new MyPageable(pages.getNumberOfPages(), pf, p));
			    if (pjob.printDialog() == false) return;
			    try {
					pjob.print();
				} catch (PrinterException e1) {
					JOptionPane.showMessageDialog(
						this,
						"Druckfehler: " + e1.getMessage(),
						"Fehler",
						JOptionPane.WARNING_MESSAGE
					);
				}
				break;
			case "CANCEL":
				setVisible(false);
				break;
		}
	}

	private void setMediaSizeInfo() {
		float width = (float) (pf.getWidth() / ZOLL * 10.);
		float height = (float) (pf.getHeight() / ZOLL * 10.);
		String name = getMediaName(Math.min(width, height), Math.max(width, height));
		String format = width > height ? "Querformat" : "Hochformat";
		lblMediaName.setText(String.format(	"%s im %s: %.1fmm x %.1fmm", name, format, pf.getWidth() / ZOLL, pf.getHeight() / ZOLL));
	}

	private String getMediaName(float width, float height) {
		MediaSizeName msn = MediaSize.findMedia(width, height, Size2DSyntax.MM);

		if (msn == null) return "Unbekannt";

		switch (msn.toString()) {
			case "iso-a0": return "DIN A0";
			case "iso-a1": return "DIN A1";
			case "iso-a2": return "DIN A2";
			case "iso-a3": return "DIN A3";
			case "iso-a4": return "DIN A4";
			case "iso-a5": return "DIN A5";
			case "iso-a6": return "DIN A6";
			case "iso-a7": return "DIN A7";
			case "iso-a8": return "DIN A8";
			case "iso-a9": return "DIN A9";
			case "iso-a10": return "DIN A10";
			case "iso-b0": return "DIN B0";
			case "iso-b1": return "DIN B1";
			case "iso-b2": return "DIN B2";
			case "iso-b3": return "DIN B3";
			case "iso-b4": return "DIN B4";
			case "iso-b5": return "DIN B5";
			case "iso-b6": return "DIN B6";
			case "iso-b7": return "DIN B7";
			case "iso-b8": return "DIN B8";
			case "iso-b9": return "DIN B9";
			case "iso-b10": return "DIN B10";
			case "na-letter": return "North American Letter";
			case "na-legal": return "North American Legal";
			case "na-8x10": return "North American 8x10 inch";
			case "na-5x7": return "North American 5x7 inch";
			case "executive": return "Executive";
			case "folio": return "Folio";
			case "invoice": return "Invoice";
			case "tabloid": return "Tabloid";
			case "ledger": return "Ledger";
			case "quarto": return "Quarto";
			case "iso-c0": return "DIN C0";
			case "iso-c1": return "DIN C1";
			case "iso-c2": return "DIN C2";
			case "iso-c3": return "DIN C3";
			case "iso-c4": return "DIN C4";
			case "iso-c5": return "DIN C5";
			case "iso-c6": return "DIN C6";
			case "iso-designated-long": return "ISO Designated Long size";
			case "na-10x13-envelope": return "North American 10x13 inch";
			case "na-9x12-envelope": return "North American 9x12 inch";
			case "na-number-10-envelope": return "North American number 10 business envelope";
			case "na-7x9-envelope": return "North American 7x9 inch envelope";
			case "na-9x11-envelope": return "North American 9x11 inch envelope";
			case "na-10x14-envelope": return "North American 10x14 inch envelope";
			case "na-number-9-envelope": return "North American number 9 business envelope";
			case "na-6x9-envelope": return "North American 6x9 inch envelope";
			case "na-10x15-envelope": return "North American 10x15 inch envelope";
			case "monarch-envelope": return "Monarch envelope";
			case "jis-b0": return "JIS B0";
			case "jis-b1": return "JIS B1";
			case "jis-b2": return "JIS B2";
			case "jis-b3": return "JIS B3";
			case "jis-b4": return "JIS B4";
			case "jis-b5": return "JIS B5";
			case "jis-b6": return "JIS B6";
			case "jis-b7": return "JIS B7";
			case "jis-b8": return "JIS B8";
			case "jis-b9": return "JIS B9";
			case "jis-b10": return "JIS B10";
			case "a": return "Engineering ANSI A";
			case "b": return "Engineering ANSI B";
			case "c": return "Engineering ANSI C";
			case "d": return "Engineering ANSI D";
			case "e": return "Engineering ANSI E";
			case "f": return "Engineering ANSI F";
			case "arch-a": return "Architectural ANSI A";
			case "arch-b": return "Architectural ANSI B";
			case "arch-c": return "Architectural ANSI C";
			case "arch-d": return "Architectural ANSI D";
			case "arch-e": return "Architectural ANSI E";
			case "japanese-postcard": return "Japanese Postcard";
			case "oufuko-postcard": return "Oufuko Postcard";
			case "italian-envelope": return "Italian Envelope";
			case "personal-envelope": return "Personal Envelope";
			case "na-number-11-envelope": return "North American Number 11 Envelope";
			case "na-number-12-envelope": return "North American Number 12 Envelope";
			case "na-number-14-envelope": return "North American Number 14 Envelope";
			default: return "Unbekannt";
		}
	}
}