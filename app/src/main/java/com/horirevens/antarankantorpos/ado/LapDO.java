package com.horirevens.antarankantorpos.ado;

/**
 * Created by horirevens on 1/23/17.
 */
public class LapDO {
    private String ado, proses, berhasil, gagal, jml_item;

    public LapDO(String ado, String proses, String berhasil, String gagal, String jml_item) {
        this.ado = ado;
        this.proses = proses;
        this.berhasil = berhasil;
        this.gagal = gagal;
        this.jml_item = jml_item;
    }

    public String getProses() {
        return proses;
    }
    public void setProses(String proses) {
        this.proses = proses;
    }

    public String getBerhasil() {
        return berhasil;
    }
    public void setBerhasil(String berhasil) {
        this.berhasil = berhasil;
    }

    public String getGagal() {
        return gagal;
    }
    public void setGagal(String gagal) {
        this.gagal = gagal;
    }

    public String getJml_item() {
        return jml_item;
    }
    public void setJml_item(String jml_item) {
        this.jml_item = jml_item;
    }

    public String getAdo() {
        return ado;
    }
    public void setAdo(String ado) {
        this.ado = ado;
    }
}
