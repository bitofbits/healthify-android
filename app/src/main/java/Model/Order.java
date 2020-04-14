package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Order extends BaseFirestore
{
    private String customer_email;
    private String order_id;
    private String partner;
    private int cost;
    private String name;
    private HashMap<String , ArrayList<String>> order_name;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }
    
    public Order(String c, String allotedDeliveryPerson,int co,HashMap<String, ArrayList<String>> x) {
        order_id=Integer.toString(c.hashCode());
        partner = allotedDeliveryPerson;
        customer_email = c;
        cost=co;
        order_name = x;
    }
    public Order()
    {

    }
    public String getCustomer_email()
    {
        return customer_email;
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