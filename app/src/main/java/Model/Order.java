package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Order extends BaseFirestore
{
    /*
    status 0 : order placed.
    status 1 : order picked by up delivery person and out for delivery
    */
    private int status;
    private String otp;
    private String customer_email;
    private String order_id;
    private String partner;
    private int cost;
    private String name;
    private HashMap<String , ArrayList<String>> order_name;
    private int totalDiscount;
    public Order(String c, String allotedDeliveryPerson,int co,HashMap<String, ArrayList<String>> x,String otp, int totalDiscount, int status) {
        order_id=Integer.toString(c.hashCode());
        partner = allotedDeliveryPerson;
        customer_email = c;
        cost=co;
        order_name = x;
        this.otp = otp;
        this.totalDiscount = totalDiscount;
        this.status=status;
    }
    public Order()
    {

    }
    public int getTotalDiscount(){
        return this.totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getCustomer_email()
    {
        return customer_email;
    }

    public String getOtp()
    {
        return otp;
    }

    public void setOtp(String otp)
    {
        this.otp = otp;
    }

    public void setCustomer_email(String customer_email)
    {
        this.customer_email = customer_email;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

    public HashMap<String, ArrayList<String>> getOrder_name()
    {
        return order_name;
    }

    public void setOrder_name(HashMap<String, ArrayList<String>> order_name) {
        this.order_name = order_name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    @Override
    public String getID()
    {
        return order_id;
    }
}