package br.com.faesp.weatherforecast;

public class Cities {
    public String name;
    public long id;

    public Cities(long id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}