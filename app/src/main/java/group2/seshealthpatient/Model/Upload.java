package group2.seshealthpatient.Model;

public class Upload {

    private String name;
    private String type;
    private String Url;


    public  Upload(){

    }

    public Upload(String name, String type, String url){
        this.name = name;
        this.type = type;
        this.Url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
