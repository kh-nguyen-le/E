
/**
 * Write a description of class runMVC here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class runMVC
{
    public runMVC()
    {
        View view = new View();
        
        Controller controller = new Controller();
        controller.addView(view);
        
        view.addController(controller);
    }
}
