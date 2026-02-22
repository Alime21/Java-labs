package msku.ceng.madlab.week2;

public class Animal {
    private String type;
    private int picId;

    public Animal(int picId ,String type) {
        this.picId = picId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }
}
