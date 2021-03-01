package kz.xyzdev.mebel.Models;

public class ProductModels {

    private String name;
    private  String features;
    private String imgurl;
    private String colors;
    private String price;
    private String model;
    private String catalog;
    private String country;
    private String materials;
    private Boolean liked;

    public ProductModels(){

        }

    public ProductModels(String name, String features, String imgurl, String colors, String price, String model, String catalog, String country,String materials, Boolean liked) {
        this.name = name;
        this.features = features;
        this.imgurl = imgurl;
        this.colors = colors;
        this.price = price;
        this.materials = materials;
        this.liked = liked;
        this.model = model;
        this.catalog = catalog;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
