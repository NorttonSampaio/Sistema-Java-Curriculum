package util;

import control.Pessoa;
import dao.NewHibernateUtil;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.hibernate.Session;

public class SessionManipulacao {
    Session s;
    public void save(Pessoa p, JInternalFrame ifr){
        try{
            s = NewHibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            s.save(p);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(ifr, ex);
            ex.printStackTrace();
        }finally{
            s.getTransaction().commit();
        }
    }
    
    public void update(Pessoa p, JInternalFrame ifr){
        try{
            s = NewHibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            s.merge(p);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(ifr, ex);
            ex.printStackTrace();
        }finally{
            s.getTransaction().commit();
        }
    }
    
    public void delete(Pessoa p, JInternalFrame ifr){
        try{
            s = NewHibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            s.delete(p);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(ifr, ex);
            ex.printStackTrace();
        }finally{
            s.getTransaction().commit();
        }
    }
}
