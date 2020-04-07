package Model;

public class Product
{
    private String name;
    private String discription;
    private int price;
    private String img_url;
    private boolean instock;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDiscription()
    {
        return discription;
    }

    public void setDiscription(String discription)
    {
        this.discription = discription;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getImg_url()
    {
        return img_url;
    }

    public void setImg_url(String img_url)
    {
        this.img_url = img_url;
    }

    public boolean isInstock()
    {
        return instock;
    }

    public void setInstock(boolean instock)
    {
        this.instock = instock;
    }
}
