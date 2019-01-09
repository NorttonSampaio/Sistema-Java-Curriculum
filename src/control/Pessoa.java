package control;
// Generated 20/02/2018 11:14:45 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Pessoa generated by hbm2java
 */
public class Pessoa  implements java.io.Serializable {


     private Integer id;
     private String foto;
     private String nome;
     private String nacionalidade;
     private String estadoCivil;
     private Date dnasc;
     private String rua;
     private String numero;
     private String bairro;
     private String cidade;
     private String uf;
     private String cep;
     private String tel1;
     private String tel2;
     private String email;
     private String objetivo;
     private String habilidade;
     private String formacao;
     private String experiencia;

    public Pessoa() {
    }

    public String[] getPessoa(){
        return new String[]{
            getId().toString(),
            getNome(),
            getEstadoCivil(),
            getDnasc().toString(),
            getCidade().toString(),
            getTel1().toString()};
    }
	
    public Pessoa(String nome, String nacionalidade, String estadoCivil, Date dnasc, String rua, String bairro, String cidade, String uf, String cep, String tel1, String objetivo, String habilidade, String formacao, String experiencia) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.estadoCivil = estadoCivil;
        this.dnasc = dnasc;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.tel1 = tel1;
        this.objetivo = objetivo;
        this.habilidade = habilidade;
        this.formacao = formacao;
        this.experiencia = experiencia;
    }
    public Pessoa(String foto, String nome, String nacionalidade, String estadoCivil, Date dnasc, String rua, String numero, String bairro, String cidade, String uf, String cep, String tel1, String tel2, String email, String objetivo, String habilidade, String formacao, String experiencia) {
       this.foto = foto;
       this.nome = nome;
       this.nacionalidade = nacionalidade;
       this.estadoCivil = estadoCivil;
       this.dnasc = dnasc;
       this.rua = rua;
       this.numero = numero;
       this.bairro = bairro;
       this.cidade = cidade;
       this.uf = uf;
       this.cep = cep;
       this.tel1 = tel1;
       this.tel2 = tel2;
       this.email = email;
       this.objetivo = objetivo;
       this.habilidade = habilidade;
       this.formacao = formacao;
       this.experiencia = experiencia;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFoto() {
        return this.foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNacionalidade() {
        return this.nacionalidade;
    }
    
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
    public String getEstadoCivil() {
        return this.estadoCivil;
    }
    
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    public Date getDnasc() {
        return this.dnasc;
    }
    
    public void setDnasc(Date dnasc) {
        this.dnasc = dnasc;
    }
    public String getRua() {
        return this.rua;
    }
    
    public void setRua(String rua) {
        this.rua = rua;
    }
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getBairro() {
        return this.bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getCidade() {
        return this.cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getUf() {
        return this.uf;
    }
    
    public void setUf(String uf) {
        this.uf = uf;
    }
    public String getCep() {
        return this.cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    public String getTel1() {
        return this.tel1;
    }
    
    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }
    public String getTel2() {
        return this.tel2;
    }
    
    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getObjetivo() {
        return this.objetivo;
    }
    
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
    public String getHabilidade() {
        return this.habilidade;
    }
    
    public void setHabilidade(String habilidade) {
        this.habilidade = habilidade;
    }
    public String getFormacao() {
        return this.formacao;
    }
    
    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
    public String getExperiencia() {
        return this.experiencia;
    }
    
    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }




}

