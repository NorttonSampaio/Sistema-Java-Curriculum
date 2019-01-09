package view;

import control.Pessoa;
import util.GerarDocumentos;

public class Principal {
    GerarDocumentos gerar;
    private void gerarRel(){
        gerar = new GerarDocumentos();
        Pessoa p = new Pessoa();
        p.setId(1);
        gerar.gerarCurriculum(p);
    }
}