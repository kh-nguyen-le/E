import junit.framework.TestCase;
import org.junit.*;

class StudentTest extends TestCase{    
    public StudentTest(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Student testStudent = new Student(firstName,lastName);
        
        //Correct
        Assert.assertEquals(firstName+" "+lastName+" Number: "+"1000000",testStudent.toString());
          
        //Wrong 
        Assert.assertEquals(firstName+lastName, testStudent.toString());

        //Wrong 
        Assert.assertEquals(firstName+lastName+"1000000", testStudent.toString());
    }
    
    public void testGetNumber(){
        String firstName = "Aniekan"; String lastName = "Akai";
        Student testStudent = new Student(firstName,lastName);
        int testNumber = testStudent.getNumber();
        
        //Correct
        Assert.assertEquals( 1000000, testNumber);
        
        //Wrong
        Assert.assertEquals(-1, testNumber);
    }
}