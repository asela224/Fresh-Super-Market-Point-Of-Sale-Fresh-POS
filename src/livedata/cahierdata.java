package livedata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Cashier;
import model.Sale;

public class cahierdata {
	
	
	public static Cashier cashierrec = new Cashier();
	public static double paybleAmount ;
	public static double billTotal ;
	public static double discountTotal ;
	
	
	public static double change;
	public static double payamount;
	public static ObservableList<Sale> saleitemsdatafromsaletable = FXCollections.observableArrayList();
	public static String slipno;


}
