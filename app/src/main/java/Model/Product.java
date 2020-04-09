package Model;

public class Product extends BaseFirestore
{
    private String name;
    private int price;
    private String img_url;
    private String discription;
    private boolean instock;
    public Product(String name, String discription,int price, String img_url, boolean instock)
    {
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.instock = instock;
        this.discription=discription;
    }
    public Product(String n,int p)
    {
        this.name=n;
        this.price=p;
        this.img_url="";
        this.discription="";

    }
    public Product()
    {
        this.instock=true;
    }
    public String getDiscription()
    {
        return discription;
    }

    public void setDiscription(String discription)
    {
        this.discription = discription;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
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

    @Override
    public String getID()
    {
        return this.name;
    }
}
