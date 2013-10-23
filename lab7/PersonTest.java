import junit.framework.TestCase;
import org.junit.*;

class PersonTest extends TestCase{    
    
    public PersonTest(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);
        
        //Correct
        Assert.assertEquals(firstName+" "+lastName, testPerson.toString());
          
        //Wrong 
        Assert.assertEquals(firstName+lastName, testPerson.toString());

        //Wrong 
        Assert.assertEquals(firstName+lastName+"1000000", testPerson.toString());
    }
    
    public void testSetLastName(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
    
        testPerson.setLastName("Edwards");
        
        //Correct
        Assert.assertEquals(firstName+" "+"Edwards", testPerson.toString());
        
        //Wrong
        Assert.assertEquals(firstName+" "+lastName, testPerson.toString() );
        
        //Wrong
        Assert.assertEquals("Edwards"+" "+lastName, testPerson.toString());
    }
    
    public void testGetFirstName(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        String actualFirstName = testPerson.getFirstName();
        
        //Correct
        Assert.assertEquals(firstName, actualFirstName);
        
        //Wrong
        Assert.assertEquals(lastName, actualFirstName);
        
        //Wrong
        Assert.assertEquals(firstName+" "+lastName, actualFirstName);
    }
    
    public void testGetLastName(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        String actualLastName = testPerson.getLastName();
        
        //Correct
        Assert.assertEquals(lastName, actualLastName);
        
        //Wrong
        Assert.assertEquals(firstName, actualLastName);
        
        //Wrong
        Assert.assertEquals(firstName+" "+lastName, actualLastName);
    }
   
   public void testGetFullName() {
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        String actualFullName = testPerson.getFullName();
        
        //Correct
        Assert.assertEquals(firstName+" "+lastName, actualFullName);
        
        //Wrong
        Assert.assertEquals(firstName+lastName, actualFullName);
        
        //Wrong
        Assert.assertEquals(firstName+" ", actualFullName);
    }
   
   
   public void testSetAddress() throws Exception{
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        Address testAddress = new Address("1156 Colonel By Drive", "Ottawa", "K1S 5B6");
        
        testPerson.setAddress(testAddress);
        
        //Correct
        Assert.assertEquals("1156 Colonel By Drive"+", "+ "Ottawa"+". "+"K1S 5B6", testPerson.getAddress());
        
        //Wrong
        Assert.assertEquals("1156 Colonel By Drive"+ "Ottawa"+"K1S 5B6", testPerson.getAddress());
        
    }
  
   public void testGetAddress() throws Exception { 
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        Address testAddress = new Address("1156 Colonel By Drive", "Ottawa", "K1S 5B6");
        
        Address actualDefaultAddress = testPerson.getAddress();
        
        testPerson.setAddress(testAddress);
        Address actualSetAddress = testPerson.getAddress();
        
        //Correct
        Assert.assertEquals("Planet Earth", actualDefaultAddress.toString());
        
        //Wrong
        Assert.assertEquals("1156 Colonel By Drive", actualDefaultAddress.toString());
        
        //Wrong
        Assert.assertEquals(firstName+" ", actualDefaultAddress.toString());

       //Correct
        Assert.assertEquals("1156 Colonel By Drive"+", "+"Ottawa"+", "+"K1S 5B6", actualSetAddress.toString());
        
        //Wrong
        Assert.assertEquals("Planet Earth", actualSetAddress.toString());
        
        //Wrong
        Assert.assertEquals(firstName+" ", actualSetAddress.toString());
   }

   public void testGetCourses() { 
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        Course [] testDefaultCourses = testPerson.getCourses();
        
        //Correct
        Assert.assertEquals("None", testDefaultCourses.toString());

        //wrong
        Assert.assertEquals("SYSC 3010A: Comp SYS Project", testDefaultCourses.toString());

        //wrong
        Assert.assertEquals(firstName, testDefaultCourses.toString());
    }
   
   public void testAddCourse() {
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        Course testCourse = new Course("SYSC 3010A", "Comp. Sys. Project");
        testPerson.addCourse(testCourse);
        Course [] testDefaultCourses = testPerson.getCourses();
        
        //Correct
        Assert.assertEquals("SYSC 3010A: Comp. Sys. Project", testDefaultCourses.toString());

        //wrong
        Assert.assertEquals("SYSC 3010A: Comp. Sys. Project", testDefaultCourses.toString());   
        
   }
   
   public void testRemove(Course course) {
        
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        
        
        Course testCourse = new Course("SYSC 3010A", "Comp. Sys. Project");
        testPerson.remove(testCourse);
        Course [] testDefaultCourses = testPerson.getCourses();
        
        //Correct
        Assert.assertEquals("", testDefaultCourses.toString());

        //wrong
        Assert.assertEquals("SYSC 3010A: Comp. Sys. Project", testDefaultCourses.toString());   
   };

    public void testToString() { 

        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson = new Person(firstName,lastName);        

        String actualPerson = testPerson.toString();

        //Correct
        Assert.assertEquals("Aniekan Akai", actualPerson);
        
        //Wrong
        Assert.assertEquals("AniekanAkai", actualPerson);

    }
    public void testEquals() { 
        String firstName = "Aniekan"; String lastName = "Akai";
        Person testPerson1 = new Person(firstName,lastName);        
        Person testPerson2 = new Person(firstName,lastName);
        
        //Correct
        Assert.assertEquals(true, (testPerson1.toString() == testPerson2.toString()));
        
        //Wrong
        Assert.assertEquals(false, (testPerson1.toString() == testPerson2.toString()));
    
                //Wrong
        Assert.assertEquals(null, (testPerson1.toString() == testPerson2.toString()));
    
    }
}