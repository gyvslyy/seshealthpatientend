package group2.seshealthpatient.Model;

public class DataPacket {
    private String Title;
    private String DataText;
    private String Time;
    private Upload upload;

    public DataPacket(){

    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String ID) {

        this.Title = ID;
    }

    public String getDataText() {

        return DataText;
    }

    public void setDataText(String dataText) {

        DataText = dataText;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }
}
