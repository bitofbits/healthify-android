package Model;

public class Customer extends BaseFirestore
{
    private String name;
    private int age;
    private String email;
    private String password;
    private String phone_number;
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
        age=0;
    }
    public Customer()
    {

    }
    public void setPassword(String password)
    {
        this.password = password;
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

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
