package com.example.restaurantlocator.Model;

public class Restaurant
{
    private String Rname, Rdetails, Catagory, pid, Image;

    public Restaurant()
    {

    }

    public Restaurant(String Rname, String Rdetails, String Catagory, String pid, String Image)
    {
        this.Rname = Rname;
        this.Rdetails = Rdetails;
        this.Catagory = Catagory;
        this.pid = pid;
        this.Image = Image;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String rname) {
        Rname = rname;
    }

    public String getRdetails() {
        return Rdetails;
    }

    public void setRdetails(String rdetails) {
        Rdetails = rdetails;
    }

    public String getCatagory() {
        return Catagory;
    }

    public void setCatagory(String catagory) {
        Catagory = catagory;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
