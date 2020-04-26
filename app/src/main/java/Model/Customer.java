package Model;

public class Customer extends BaseFirestore
{
    private int past_orders;
    private String name;
//    private int age;
    private String email;
    private String password;
    private String phone_number;
    private boolean preferred_customer;

    private String address;
    private double latitude;
    private double longitude;
    @Override
    public String getID()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }
    public Customer(String n,String e,String pho,String pass)
    {
        name=n;
        email=e;
        password=pass;
        phone_number=pho;
//        age=0;
        preferred_customer=false;
        past_orders=0;
    }
    public Customer()
    {

    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isPreferred_customer()
    {
        return preferred_customer;
    }

    public void setPreferred_customer(boolean preferred_customer)
    {
        this.preferred_customer = preferred_customer;
    }

    public int getPast_orders()
    {
        return past_orders;
    }

    public void setPast_orders(int past_orders)
    {
        this.past_orders = past_orders;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

//    public int getAge()
//    {
//        return age;
//    }
//
//    public void setAge(int age)
//    {
//        this.age = age;
//    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
