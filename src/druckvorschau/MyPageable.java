package druckvorschau;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;


public class MyPageable implements Pageable {
	private int numberOfPages;
	private PageFormat pageFormat;
	private Printable printable;

	public MyPageable(int numberOfPages, PageFormat pageFormat, Printable printable) {
		this.numberOfPages = numberOfPages;
		this.pageFormat = pageFormat;
		this.printable= printable; 
	}

	@Override
	public int getNumberOfPages() {
		return numberOfPages;
	}

	@Override
	public PageFormat getPageFormat(int page) throws IndexOutOfBoundsException {
		if (page >= numberOfPages) throw new IndexOutOfBoundsException();
		return pageFormat;
	}

	@Override
	public Printable getPrintable(int page) throws IndexOutOfBoundsException {
		if (page >= numberOfPages) throw new IndexOutOfBoundsException();
		return printable;
	}

}
