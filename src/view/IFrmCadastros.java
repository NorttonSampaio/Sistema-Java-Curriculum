/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.Pessoa;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.Session;
import util.GerarDocumentos;
import util.ManipularImagem;
import util.SessionManipulacao;

public class IFrmCadastros extends javax.swing.JInternalFrame {

    int idPessoa=-1;
    DefaultTableModel dtmPessoa;
    Pessoa[] vetorPessoa;    
    JFileChooser file;
    BufferedImage imagem;
    GerarDocumentos gerar;
    
    public IFrmCadastros() {
        initComponents();
        jogoDeBotoes('C');
        dtmPessoa = (DefaultTableModel)tblPessoa.getModel();
        carregarTabela("From Pessoa");
    }
    
    private void carregarTabela(String sql){
        Session s = null;
        try{
            sql =prepararSQL(sql);
            JOptionPane.showMessageDialog(null, sql);
            s=dao.NewHibernateUtil.getSessionFactory().getCurrentSession();
            s.beginTransaction();
            
            Query q = s.createQuery(sql);
            List l = q.list();
            Iterator i = l.iterator();
            
            dtmPessoa.setNumRows(0);
            
            Pessoa p;
            
            int cont=0;
            vetorPessoa = new Pessoa[l.size()];
            
            while(i.hasNext()){
                p=(Pessoa)i.next();
                vetorPessoa[cont]=p;
                cont++;
                dtmPessoa.addRow(p.getPessoa());
            }
            lblContRows.setText("Curriculuns Cadastrados: "+ String.valueOf(tblPessoa.getRowCount()));
        }catch(Exception ex){
            JOptionPane.showMessageDialog(IFrmCadastros.this, ex);
            ex.printStackTrace();
        }finally{
            s.getTransaction().commit();
        }
    }
    
    private String prepararSQL(String sql){
        
        String parametro = carregaValueCbxParametro();
        if(parametro.equals("nome") || parametro.equals("objetivo") || parametro.equals("cidade") || parametro.equals("uf")){
            sql+= " where "+parametro+" like '"+txtFiltro.getText()+"%'";
        }else{
            if(cbxFiltro.getSelectedIndex()!=0){
                sql+= " where "+parametro+" like '"+txtFiltro.getText()+"%'";
            }
        }
                
        return sql;
    }
    
    private String carregaValueCbxParametro(){
        switch(cbxFiltro.getSelectedItem().toString()){
            case "NOME":return "nome";
            case "OBJETIVO":return "objetivo";
            
            case "CIDADE":return "cidade";
            case "UF":return "uf";
            default:return "";
        }
    }
    
    private void jogoDeBotoes(char opc){
        //S - Salvar
        //N - Novo
        //A - Atualizar
        //C - Cancelar
        //E - Escolher/Selecionar
        if(opc=='S' || opc=='A' || opc=='C'){
            //O Usuario Clicou em Salvar/Alterar ou Cancelar
            btnSalvar.setEnabled(false);
            btnNovo.setEnabled(true);
            btnAtualizar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnGerar.setEnabled(false);
            habilitarCampos(false);
        }else if(opc=='N'){
            //O Usuario Clicou em Novo
            btnSalvar.setEnabled(true);
            btnNovo.setEnabled(false);
            btnAtualizar.setEnabled(false);
            btnCancelar.setEnabled(true);
            btnGerar.setEnabled(false);
            habilitarCampos(true);
        }else{
            //O Usuario Clicou em Selecionar
            btnSalvar.setEnabled(false);
            btnNovo.setEnabled(false);
            btnAtualizar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnGerar.setEnabled(true);
            habilitarCampos(true);
        }
    }
    
    private void habilitarCampos(boolean choice){
        txtId.setEnabled(choice);
        txtNome.setEnabled(choice);
        txtNacionalidade.setEnabled(choice);
        txtLogradouro.setEnabled(choice);
        spnNumero.setEnabled(choice);
        txtBairro.setEnabled(choice);
        txtCidade.setEnabled(choice);
        txtCep.setEnabled(choice);
        txtTelefone1.setEnabled(choice);
        txtTelefone2.setEnabled(choice);
        txtEmail.setEnabled(choice);
        
        txtObjetivo.setEnabled(choice);
        txtHabilidade.setEnabled(choice);
        txtFormacao.setEnabled(choice);
        txtExperiencia.setEnabled(choice);
        
        cbxEstadoCivil.setEnabled(choice);
        cbxUF.setEnabled(choice);
        
        dNasc.setEnabled(choice);
        
        lblImagem.setEnabled(choice);
    }
    
    private void limparCampos(){
        txtId.setText("");
        txtNome.setText("");
        txtNacionalidade.setText("");
        txtLogradouro.setText("");
        spnNumero.setValue(0);
        txtBairro.setText("");
        txtCidade.setText("");
        txtCep.setText("");
        txtTelefone1.setText("");
        txtTelefone2.setText("");
        txtEmail.setText("");
        
        txtObjetivo.setText("");
        txtHabilidade.setText("");
        txtFormacao.setText("");
        txtExperiencia.setText("");
        
        cbxEstadoCivil.setSelectedIndex(0);
        cbxUF.setSelectedIndex(0);
        
        dNasc.setDate(null);
        
        lblImagem.setText("IMAGEM");
        lblImagem.setIcon(null);
    }

    private boolean camposObrigatorios(){
        String cep = txtCep.getText().trim().replaceAll("[.-]", "").trim();
        String telefone1 = txtTelefone1.getText().trim().replaceAll("[()-]", "").trim();
        return  !txtNome.getText().trim().isEmpty() &&
                !txtNacionalidade.getText().trim().isEmpty() &&
                
                dNasc.getDate()!=null &&
                
                cbxEstadoCivil.getSelectedIndex()!=0 &&
                cbxUF.getSelectedIndex()!=0 &&
                
                !cep.isEmpty() &&
                !txtLogradouro.getText().trim().isEmpty() &&
                !txtBairro.getText().trim().isEmpty() &&
                !txtCidade.getText().trim().isEmpty() &&
                !telefone1.isEmpty();
    }
    
    private Pessoa retornarPessoa(){
        String foto="";
        try {
            if(imagem!=null){
                String caminho = getClass().getResource("../imagens/").toString().substring(6);
                
                File outputfile = new File(caminho+txtId.getText()+".jpg");
                ImageIO.write(imagem, "jpg", outputfile);
                foto = outputfile.getAbsolutePath();
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(IFrmCadastros.this, ex);
            ex.printStackTrace();
        }
        
        String nome = txtNome.getText().trim();
        String nacionalidade = txtNacionalidade.getText().trim();
        String rua = txtLogradouro.getText().trim();
        String numero = spnNumero.getValue().toString().trim();
        String bairro = txtBairro.getText().trim();
        String cidade = txtCidade.getText().trim();
        //String cep = txtCep.getText().trim().replaceAll("[.-]", "").trim();
        String cep = txtCep.getText().trim();
        //String tel1 = txtTelefone1.getText().trim().replaceAll("[()-]", "").trim();
        String tel1 = txtTelefone1.getText().trim();
        //String tel2 = txtTelefone2.getText().trim().replaceAll("[()-]", "").trim();
        String tel2 = txtTelefone2.getText().trim();
        String email =txtEmail.getText().trim();
        
        String objetivo = txtObjetivo.getText().trim();
        String habilidade = txtHabilidade.getText().trim();
        String formacao = txtFormacao.getText().trim();
        String experiencia = txtExperiencia.getText().trim();
        
        String estado = cbxEstadoCivil.getSelectedItem().toString().trim();
        String uf = cbxUF.getSelectedItem().toString().trim();
        
        return new Pessoa(foto, nome, nacionalidade, estado, dNasc.getDate(), rua, numero, bairro, cidade, uf, cep, tel1, tel2, email, objetivo, habilidade, formacao, experiencia);
    }
    
    private void saveOrUpdate(char opc) {
        //S- Salvar
        //A- Atualizar
        gerar = new GerarDocumentos();
        SessionManipulacao session = new SessionManipulacao();
        Pessoa p = retornarPessoa();
        if (opc == 'S') {
            session.save(p, this);
            JOptionPane.showMessageDialog(null, "Cliente Cadastrado com Sucesso");
        }else{
            p.setId(idPessoa);
            session.update(p, this);            
            JOptionPane.showMessageDialog(null, "Cliente Atualizado com Sucesso");
        }
        int op = JOptionPane.showConfirmDialog(this, "Gerar Curriculum?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(op==0)gerar.gerarCurriculum(p);
        carregarTabela("From Pessoa");
        idPessoa=-1;
        limparCampos();
        jogoDeBotoes(opc);
    }
    
    private void preencherPessoa(){
        idPessoa = Integer.valueOf(dtmPessoa.getValueAt(tblPessoa.getSelectedRow(), 0).toString());
        Pessoa p = new Pessoa();
        for(int cont=0; cont<vetorPessoa.length; cont++){
            if(idPessoa==vetorPessoa[cont].getId()){
                p=vetorPessoa[cont];
                idPessoa=p.getId();
                break;
            }
        }
        
        if(!p.getFoto().isEmpty()){
            try {
                imagem = ManipularImagem.setImagemDimensao(p.getFoto(), 187, 166);
                if(imagem!=null){
                    lblImagem.setIcon(new ImageIcon(imagem));
                    lblImagem.setText("");
                }else{
                    lblImagem.setIcon(null);
                    lblImagem.setText("IMAGEM");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(IFrmCadastros.this, ex);
               ex.printStackTrace();
            }
        }else{
            lblImagem.setText("IMAGEM");
            lblImagem.setIcon(null);
        }
        
        txtId.setText(p.getId().toString());
        txtNome.setText(p.getNome());
        txtNacionalidade.setText(p.getNacionalidade());
        txtLogradouro.setText(p.getRua());
        spnNumero.setValue(Integer.parseInt(p.getNumero()));
        txtBairro.setText(p.getBairro());
        txtCidade.setText(p.getCidade());
        txtCep.setText(p.getCep());
        txtTelefone1.setText(p.getTel1());
        txtTelefone2.setText(p.getTel2());
        txtEmail.setText(p.getEmail());
        
        txtObjetivo.setText(p.getObjetivo());
        txtHabilidade.setText(p.getHabilidade());
        txtFormacao.setText(p.getFormacao());
        txtExperiencia.setText(p.getExperiencia());
        
        cbxEstadoCivil.setSelectedItem(p.getEstadoCivil());
        cbxUF.setSelectedItem(p.getUf());
        
        dNasc.setDate(p.getDnasc());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpnCadastros = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNacionalidade = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbxEstadoCivil = new javax.swing.JComboBox<>();
        lblImagem = new javax.swing.JLabel();
        dNasc = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtLogradouro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxUF = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtCep = new javax.swing.JFormattedTextField();
        spnNumero = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        txtTelefone1 = new javax.swing.JFormattedTextField();
        txtTelefone2 = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObjetivo = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtHabilidade = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtFormacao = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtExperiencia = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        btnSalvar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        btnGerar = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoa = new javax.swing.JTable();
        lblContRows = new javax.swing.JLabel();
        btnSelecionar = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        cbxFiltro = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setClosable(true);

        jLabel1.setText("ID:");

        txtId.setEditable(false);

        jLabel2.setText("Nome Completo*:");

        jLabel3.setText("Nacionalidade*:");

        jLabel4.setText("Estado Civil*:");

        cbxEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));

        lblImagem.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblImagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagem.setText("IMAGEM");
        lblImagem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblImagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImagemMouseClicked(evt);
            }
        });

        jLabel5.setText("Data de Nasc.*:");

        jLabel6.setText("Logradouro*:");

        jLabel7.setText("Numero:");

        jLabel8.setText("Bairro*:");

        jLabel9.setText("Cidade*:");

        jLabel10.setText("UF*:");

        cbxUF.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        jLabel11.setText("CEP*:");

        try {
            txtCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel12.setText("Telefone 1*:");

        try {
            txtTelefone1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtTelefone2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel13.setText("Telefone 2:");

        jLabel14.setText("Email:");

        jLabel15.setText("Objetivo*: ");

        txtObjetivo.setColumns(20);
        txtObjetivo.setRows(5);
        jScrollPane2.setViewportView(txtObjetivo);

        jLabel16.setText("Habilidade*:");

        txtHabilidade.setColumns(20);
        txtHabilidade.setRows(5);
        jScrollPane3.setViewportView(txtHabilidade);

        jLabel17.setText("Formação*: ");

        txtFormacao.setColumns(20);
        txtFormacao.setRows(5);
        jScrollPane4.setViewportView(txtFormacao);

        jLabel18.setText("Experiencias*:");

        txtExperiencia.setColumns(20);
        txtExperiencia.setRows(5);
        jScrollPane5.setViewportView(txtExperiencia);

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar32.png"))); // NOI18N
        btnSalvar.setMnemonic('s');
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/novo32.png"))); // NOI18N
        btnNovo.setMnemonic('n');
        btnNovo.setText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/atualizar32.png"))); // NOI18N
        btnAtualizar.setMnemonic('a');
        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar32.png"))); // NOI18N
        btnCancelar.setMnemonic('c');
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel19.setText("Limpar Imagem");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        btnGerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressao32.png"))); // NOI18N
        btnGerar.setMnemonic('g');
        btnGerar.setText("Gerar Curriculum");
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });

        btnFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cancelar32.png"))); // NOI18N
        btnFechar.setMnemonic('f');
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblImagem, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNacionalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtLogradouro)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnNumero))
                                    .addComponent(dNasc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtEmail))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNome))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbxUF, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(57, 57, 57))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                            .addComponent(jScrollPane5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 865, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 865, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jSeparator2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(btnSalvar)
                        .addGap(18, 18, 18)
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAtualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(btnGerar)
                        .addGap(18, 18, 18)
                        .addComponent(btnFechar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dNasc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(txtNacionalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(spnNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(cbxUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblImagem, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnAtualizar)
                    .addComponent(btnCancelar)
                    .addComponent(btnNovo)
                    .addComponent(btnGerar)
                    .addComponent(btnFechar))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tpnCadastros.addTab("Cadastros", jPanel1);

        tblPessoa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Estado Civil", "D_Nasc", "Cidade", "Telefone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPessoa);
        if (tblPessoa.getColumnModel().getColumnCount() > 0) {
            tblPessoa.getColumnModel().getColumn(0).setPreferredWidth(10);
            tblPessoa.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        lblContRows.setText("Curriculuns Cadastrados: 0");

        btnSelecionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/selecionar32.png"))); // NOI18N
        btnSelecionar.setMnemonic('s');
        btnSelecionar.setText("Selecionar");
        btnSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarActionPerformed(evt);
            }
        });

        jLabel20.setText("Filtros:");

        cbxFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "NOME", "OBJETIVO/FUNÇÃO", "CIDADE", "UF" }));

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 952, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblContRows)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSelecionar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblContRows, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSelecionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tpnCadastros.addTab("Exibir", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tpnCadastros, javax.swing.GroupLayout.PREFERRED_SIZE, 967, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpnCadastros)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblImagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImagemMouseClicked
        // TODO add your handling code here:
        if(lblImagem.isEnabled()){
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(null);

            if (res == JFileChooser.APPROVE_OPTION) {
                File arquivo = fc.getSelectedFile();

                try {
                    imagem = ManipularImagem.setImagemDimensao(arquivo.getAbsolutePath(), 187, 166);

                    lblImagem.setIcon(new ImageIcon(imagem));
                    lblImagem.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    ex.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum arquivo.");
            }
        }
    }//GEN-LAST:event_lblImagemMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        if(camposObrigatorios())saveOrUpdate('S');
        else JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatorios");
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // TODO add your handling code here:
        limparCampos();
        txtNome.grabFocus();
        jogoDeBotoes('N');
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        if(camposObrigatorios())saveOrUpdate('A');
        else JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatorios");
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        jogoDeBotoes('C');
        limparCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarActionPerformed
        // TODO add your handling code here:
        int linha = -1;
        if(tblPessoa.getSelectedRow()>=0){
            //Prossiga
            linha = tblPessoa.getSelectedRow();
            lblImagem.setIcon(null);
            imagem=null;
            preencherPessoa();
            jogoDeBotoes('E');
        }else{
            JOptionPane.showMessageDialog(IFrmCadastros.this, "Selecione um registro da tabela");
        }
        tpnCadastros.setSelectedIndex(0);
    }//GEN-LAST:event_btnSelecionarActionPerformed

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        // TODO add your handling code here:
        lblImagem.setText("IMAGEM");
        lblImagem.setIcon(null);
        imagem = null;
    }//GEN-LAST:event_jLabel19MouseClicked

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed
        // TODO add your handling code here:
        gerar = new GerarDocumentos();
        Pessoa p = new Pessoa();
        p.setId(idPessoa);
        gerar.gerarCurriculum(p);
    }//GEN-LAST:event_btnGerarActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        carregarTabela("from Pessoa");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnGerar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionar;
    private javax.swing.JComboBox<String> cbxEstadoCivil;
    private javax.swing.JComboBox<String> cbxFiltro;
    private javax.swing.JComboBox<String> cbxUF;
    private com.toedter.calendar.JDateChooser dNasc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblContRows;
    private javax.swing.JLabel lblImagem;
    private javax.swing.JSpinner spnNumero;
    private javax.swing.JTable tblPessoa;
    private javax.swing.JTabbedPane tpnCadastros;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JFormattedTextField txtCep;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextArea txtExperiencia;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextArea txtFormacao;
    private javax.swing.JTextArea txtHabilidade;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLogradouro;
    private javax.swing.JTextField txtNacionalidade;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextArea txtObjetivo;
    private javax.swing.JFormattedTextField txtTelefone1;
    private javax.swing.JFormattedTextField txtTelefone2;
    // End of variables declaration//GEN-END:variables
}
