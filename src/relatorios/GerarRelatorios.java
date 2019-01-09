/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorios;

import dao.NewHibernateUtil;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Query;
import org.hibernate.Session;
//import view.TelaPrincipal;

/**
 *
 * @author Nortton
 */
public class GerarRelatorios {
    
    public GerarRelatorios(){
        
    }
    
    public void gerarRelatorios(String sql, String arquivo){
        Session s=null;
        try{
            s = NewHibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            
            Query q = s.createQuery(sql);
            List l = q.list();
            
            JRBeanCollectionDataSource dados= new JRBeanCollectionDataSource(l);
            JasperPrint jp = JasperFillManager.fillReport(arquivo, new HashMap(), dados);
            JasperViewer.viewReport(jp,false);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
            ex.printStackTrace();
        }finally{
            s.getTransaction().commit();
        }
    }
}