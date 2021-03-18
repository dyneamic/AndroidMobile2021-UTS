package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import java.io.Serializable;

public class ModelLagu implements Serializable {
    String laguPath;
    String laguName;
    String laguAlbum;
    String laguArtist;

    public String getLaguPath() {
        return laguPath;
    }

    public void setLaguPath(String laguPath) {
        this.laguPath = laguPath;
    }

    public String getLaguName() {
        return laguName;
    }

    public void setLaguName(String laguName) {
        this.laguName = laguName;
    }

    public String getLaguAlbum() {
        return laguAlbum;
    }

    public void setLaguAlbum(String laguAlbum) {
        this.laguAlbum = laguAlbum;
    }

    public String getLaguArtist() {
        return laguArtist;
    }

    public void setLaguArtist(String laguArtist) {
        this.laguArtist = laguArtist;
    }
}
