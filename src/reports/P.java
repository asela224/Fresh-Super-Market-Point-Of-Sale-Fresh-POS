package reports;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import livedata.cahierdata;
import model.Sale;

import java.awt.print.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class P implements Printable {

	cahierdata printdata=new cahierdata();
	public P() {}
	
	
	public P(cahierdata cd) {
		this();
		this.printdata=cd;
		
	}
	
	
	
   public int print(Graphics g, PageFormat pf, int page) throws
                                                       PrinterException {

       if (page > 0) { /* We have only one page, and 'page' is zero-based */
           return NO_SUCH_PAGE;
       }

       /* User (0,0) is typically outside the imageable area, so we must
        * translate by the X and Y values in the PageFormat to avoid clipping
        */
       Graphics2D g2d = (Graphics2D)g;
       g2d.translate(pf.getImageableX(), pf.getImageableY());
       g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
       /* Now we perform our rendering */
       ObservableList<Sale> saledata=  printdata.saleitemsdatafromsaletable;
       

       
       
       
       
   
       
       int y=20;
       int yShift = 10;
       int headerRectHeight=15;
       int headerRectHeighta=40;
       
       g2d.drawString("-------------------------------------",12,y);y+=yShift;
       g2d.drawString("        Fresh Super Market   ",12,y);y+=yShift;
       g2d.drawString("   Colombo road, Kurunegala. ",12,y);y+=yShift;
       g2d.drawString("   www.facebook.com/freshsm ",12,y);y+=yShift;
       g2d.drawString("        +94712007915      ",12,y);y+=yShift;
       g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;
       
     /* g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString("          Fresh Super Market      ",12,y);y+=yShift;
       g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight; */
 
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString("        qty   price   disc.  sale ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
      
       
      double totaldiscounts=0d;
       
       
       try {
    		

    		for (int i = 0 , itemid=1; i < saledata.size(); i++,itemid++) {
    		
    			
    		//	  g2d.drawString(saledata.get(i).getName(),10,y);y+=yShift;
     		//   g2d.drawString(saledata.get(i).getBarcode()+ "     "+saledata.get(i).getQuantity()+"     " +saledata.get(i).getUnitamount()*saledata.get(i).getQuantity() +"     "+saledata.get(i).getDiscount()+"     "+ saledata.get(i).getTotalamount()+" ",10,y);y+=(yShift*2);
     		  
     		     
     		  g2d.drawString(""+(i+1)+" " +saledata.get(i).getName(),10,y);y+=yShift;
     		  
     		  int x=10;
     		  
     		  g2d.drawString(""+saledata.get(i).getBarcode(),x,y);x+=40;
     		  g2d.drawString(""+saledata.get(i).getQuantity(),x,y);x+=30;
     		  g2d.drawString(""+new DecimalFormat("0.00").format(saledata.get(i).getUnitamount()*saledata.get(i).getQuantity()),x,y);x+=50;
     		  g2d.drawString(""+new DecimalFormat("0.00").format(saledata.get(i).getDiscount()),x,y);x+=30;
     		  g2d.drawString(""+new DecimalFormat("0.00").format(saledata.get(i).getTotalamount()),x,y);y+=(yShift*2);
		      
     		 totaldiscounts+=saledata.get(i).getDiscount();
    			
     		//new DecimalFormat("0.00").format(totalall)
    		}

    		}
    		catch(Exception e) {System.out.println("error when add items to bill");}
    	       
    	       
       
       
       
     
           
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString(" Total Amount:               "+new DecimalFormat("0.00").format(printdata.billTotal)+"   ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString(" Total Discount:             "+new DecimalFormat("0.00").format(printdata.discountTotal)+"   ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString(" Payble Amount:              "+new DecimalFormat("0.00").format(printdata.paybleAmount)+"   ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString(" Cash      :                 "+new DecimalFormat("0.00").format(printdata.payamount)+"   ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString(" Balance   :                 "+new DecimalFormat("0.00").format(printdata.change)+"   ",10,y);y+=yShift;
       g2d.drawString("-------------------------------------",10,y);y+=yShift*2;
       
       g2d.drawString(" Bill No   :    "+printdata.slipno,10,y);y+=yShift;
       
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
       Date date = new Date();  
     
       
       g2d.drawString(" Time      :    "+  formatter.format(date),10,y);y+=yShift;
       
       g2d.drawString("-------------------------------------",10,y);y+=yShift;
       g2d.drawString("          Free Home Delivery         ",10,y);y+=yShift;
       g2d.drawString("              0712007915             ",10,y);y+=yShift;
       g2d.drawString("*************************************",10,y);y+=yShift;
       g2d.drawString("       THANK YOU COME AGAIN            ",10,y);y+=yShift;
       g2d.drawString("*************************************",10,y);y+=yShift;
       g2d.drawString("       SOFTWARE :ATN Campus        ",10,y);y+=yShift;
       g2d.drawString("    CONTACT: asela224@gmail.com       ",10,y);y+=yShift;       
       g2d.drawString("*************************************",10,y);y+=yShift;
       
       
       

       /* tell the caller that this page is part of the printed document */
       return PAGE_EXISTS;
   }

   public void print(){
	   PrinterJob job = PrinterJob.getPrinterJob();
       job.setPrintable(this);
       boolean ok = job.printDialog();
       if (ok) {
           try {
                job.print();
           } catch (PrinterException ex) {
            /* The job did not successfully complete */
           }
       }
	   
       
   }

   public  void m() {
	   
	  
	
	   print();
   }
}