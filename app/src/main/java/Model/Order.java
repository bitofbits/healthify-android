package Model;

import java.util.HashMap;

public class Order extends BaseFirestore
{
    private String customer_email;
    private String order_id;
    private String partner;
    private int cost;
    private String name;
    private HashMap<String , Long> order_name;
    private String orderCategory;

    public String getOrderCategory(){
        return this.orderCategory;
    }
    public void setOrderCategory(String orderCategory){
        this.orderCategory = orderCategory;
    }
    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }
    public Order(String c, String alloteddeliveryperson,int co,HashMap<String,Long> x)
    {
        order_id=Integer.toString(c.hashCode());
        partner = alloteddeliveryperson;
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

    public HashMap<String, Long> getOrder_name()
    {
        return order_name;
    }

    public void setOrder_name(HashMap<String, Long> order_name)
    {
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
//    public Customer getCustomer()
//    {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer)
//    {
//        this.customer = customer;
//    }

//    public DeliveryPartner getPartner()
//    {
//        return partner;
//    }
//
//    public void setPartner(DeliveryPartner partner)
//    {
//        this.partner = partner;
//    }

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