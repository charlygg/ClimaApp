package com.clima.model;

public class OpenWeatherMaps {
    private Coords Coords;
    private Weather[] weather;
    private String base;
    private ClimaModel climaModel;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private long timezone;
    private long id;
    private String name;
    private long cod;

    public OpenWeatherMaps(){

    }

    public OpenWeatherMaps(com.clima.model.Coords coords, Weather[] weather, String base, ClimaModel climaModel, Wind wind, Clouds clouds, long dt, Sys sys, long timezone, long id, String name, long cod) {
        Coords = coords;
        this.weather = weather;
        this.base = base;
        this.climaModel = climaModel;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coords getCoords() { return Coords; }
    public void setCoords(Coords value) { this.Coords = value; }

    public Weather[] getWeather() { return weather; }
    public void setWeather(Weather[] value) { this.weather = value; }

    public String getBase() { return base; }
    public void setBase(String value) { this.base = value; }

    public ClimaModel getClimaModel() { return climaModel; }
    public void setClimaModel(ClimaModel value) { this.climaModel = value; }

    public Wind getWind() { return wind; }
    public void setWind(Wind value) { this.wind = value; }

    public Clouds getClouds() { return clouds; }
    public void setClouds(Clouds value) { this.clouds = value; }

    public long getDt() { return dt; }
    public void setDt(long value) { this.dt = value; }

    public Sys getSys() { return sys; }
    public void setSys(Sys value) { this.sys = value; }

    public long getTimezone() { return timezone; }
    public void setTimezone(long value) { this.timezone = value; }

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public long getCod() { return cod; }
    public void setCod(long value) { this.cod = value; }
}
