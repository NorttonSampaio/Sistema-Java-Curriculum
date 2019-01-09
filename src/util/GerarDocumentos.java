package util;

import control.Pessoa;
import relatorios.GerarRelatorios;

public class GerarDocumentos {
    GerarRelatorios gerar;

    public GerarDocumentos() {
        gerar = new GerarRelatorios();
    }
    
    public void gerarCurriculum(Pessoa p){
        try{
            String sql = "From Pessoa where id = '"+p.getId()+"'";
            gerar.gerarRelatorios(sql, "src/relatorios/Curriculum.jasper");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
