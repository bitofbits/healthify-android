package Model;

public class PromoCodes extends BaseFirestore
{
    private String Code;
    private double discount_percent;
    public PromoCodes()
    {
    }
    public PromoCodes(String code,double discount_percent)
    {
        this.Code=code;
        this.discount_percent=discount_percent;
    }
    public String getCode()
    {
        return Code;
    }

    public void setCode(String code)
    {
        Code = code;
    }

    public double getDiscount_percent()
    {
        return discount_percent;
    }

    public void setDiscount_percent(double discount_percent)
    {
        this.discount_percent = discount_percent;
    }
    @Override
    public String getID()
    {
        return Code;
    }
}
