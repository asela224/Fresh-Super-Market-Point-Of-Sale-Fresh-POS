package functs;

import java.text.DecimalFormat;

public class saleTableFormat {
		String amount;
	    public saleTableFormat(String amount) {
	       this.amount=amount;
	    }

	    @Override
	    public String toString() {
	      return new DecimalFormat("0.00").format(Double.parseDouble( amount));
	    }
	}

