public class WeatherDto {
    private  String q;
    private  String id;
    private  String lat;
    private  String lon;
    private  String zip;
    private  String units;
    private  String lang;
    private  String mode;
    public WeatherDto(String q, String id,String lat, String lon,String zip,String units,String lang,String mode){
        this.q = q;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.zip = zip;
        this.units = units;
        this.lang = lang;
        this.mode = mode;
    }
}
