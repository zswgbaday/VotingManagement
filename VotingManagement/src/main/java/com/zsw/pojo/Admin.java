package com.zsw.pojo;

public class Admin {
    private String ano;
    private String aname;
    private String apassword;
    private String alevel;

    public Admin(String ano, String aname, String apassword, String alevel) {
        this.ano = ano;
        this.aname = aname;
        this.apassword = apassword;
        this.alevel = alevel;
    }

    public Admin() {
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getApassword() {
        return apassword;
    }

    public void setApassword(String apassword) {
        this.apassword = apassword;
    }

    public String getAlevel() {
        return alevel;
    }

    public void setAlevel(String alevel) {
        this.alevel = alevel;
    }
}
