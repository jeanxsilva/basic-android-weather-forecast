package br.com.faesp.weatherforecast;

public class FederalUnities {
    public String name;
    public long id;

    public FederalUnities(long id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
