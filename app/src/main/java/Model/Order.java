package Model;

public class Order
{
    private Customer customer;
    private DeliveryPartner partner;
    private int cost;
    private String name;
    public Order(Customer c,DeliveryPartner p, int co,String na)
    {
        customer=c;
        partner=p;
        cost=co;
        name=na;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public DeliveryPartner getPartner()
    {
        return partner;
    }

    public void setPartner(DeliveryPartner partner)
    {
        this.partner = partner;
    }

    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }
}
