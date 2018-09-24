package group2.seshealthpatient.Model;

public class Profile {
public String firstname;
public String lastname;
public String age;
public String gender;
public String height;
public String weight;
public String address;
public String mc;
public Upload upload;






    public Profile() {

    }


    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getAddress() {
        return address;
    }

    public String getMc() {
        return mc;
    }



    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }
}
