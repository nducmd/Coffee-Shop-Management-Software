/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Database;
import java.sql.Connection;
import java.util.Hashtable;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author nducmd
 */
public class InvoiceReportController {

    public InvoiceReportController() {
    }
    
    public void showInvoice(int sales_history_id) {
        try {
            Hashtable map = new Hashtable();
            JasperReport rpt = JasperCompileManager.compileReport(getClass().getResourceAsStream("/Report/InvoiceReport.jrxml"));
            map.put("sId", sales_history_id);
            
            Connection connection = Database.connectDB();
          
            JasperPrint p = JasperFillManager.fillReport(rpt, map, connection);
            
            JasperViewer.viewReport(p, false);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
