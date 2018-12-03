package com.example.trung.smartair;

public class smartair {
    public String index,indoor,outdoor,remote;

    public smartair(){

    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setIndoor(String indoor) {
        this.indoor = indoor;
    }

    public void setOutdoor(String outdoor) {
        this.outdoor = outdoor;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getIndex() {
        return index;
    }

    public String getIndoor() {
        return indoor;
    }

    public String getOutdoor() {
        return outdoor;
    }

    public String getRemote() {
        return remote;
    }


    public smartair(String index, String indoor, String outdoor, String remote){
        this.index = index;
        this.outdoor= outdoor;
        this.indoor=indoor;
        this.remote=remote;
    }
}
