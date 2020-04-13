package Model;

public class DeliveryPartner extends BaseFirestore
{
    private String email;
    private String mobile_number;
    private String name;
    private String password;
    private boolean isonline;
    private int alloted_till_now;
    public DeliveryPartner()
    {

    }
    public DeliveryPartner(String e_mail,String num,String name, String pass, Boolean online, int till_now)
    {
        this.email = e_mail;
        this.mobile_number = num;
        this.name = name;
        this.password=pass;
        this.isonline = online;
        this.alloted_till_now = till_now;
    }
    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobile_number()
    {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number)
    {
        this.mobile_number = mobile_number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isIsonline()
    {
        return isonline;
    }

    public void setIsonline(boolean isonline)
    {
        this.isonline = isonline;
    }

    public int getAlloted_till_now()
    {
        return alloted_till_now;
    }

    public void setAlloted_till_now(int alloted_till_now)
    {
        this.alloted_till_now = alloted_till_now;
    }

    @Override
    public String getID()
    {
        return email;
    }

}
