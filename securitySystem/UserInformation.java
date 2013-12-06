package securitySystem;

/**
 * UserInformation.java
 * @author Teddy
 * 
 * This holds all the user's details that need to be stored and understood by
 * the server
 */
public class UserInformation {
    private String name, phoneNumber, email, password;
    
    public UserInformation(){
        name = "admin";
        phoneNumber = "";
        email = "";
        password = "admin";
    }

    public UserInformation(String name, String phoneNumber, String email, String password){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;   
    }
    
    public UserInformation(UserInformation ui){
        name = ui.getname();
        phoneNumber = ui.getphoneNumber();
        email = ui.getemail();
        password = ui.getpassword();
    }
    
    public void setname(String name){this.name = name;} 

    public void setphoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}    
    
    public void setemail(String email){this.email = email;}    
    
    public void setpassword(String password){this.password = password;}    


    public String getname(){return name;} 

    public String getphoneNumber(){return phoneNumber;}    
    
    public String getemail(){return email;}    
    
    public String getpassword(){return password;}    

    public String toString(){
        return "Username: "+name+"\nEmail: "+email+"\nPhoneNumber: "+phoneNumber+"\nPassword: "+password;
    }
}
