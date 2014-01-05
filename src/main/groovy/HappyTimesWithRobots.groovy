
public class HappyTimes 
{


    public static void main(String[] args) {

        Robot r1 = new Robot("Big Robot");
        Robot r2 = new Robot("George v.2.1");
        Robot r3 = new Robot("R2D2");

        r1.setBehaviour(new AgressiveBehaviour());
        r2.setBehaviour(new DefensiveBehaviour());
        r3.setBehaviour(new NormalBehaviour());

        r1.move();
        r2.move();
        r3.move();

        System.out.println("\r\nNew behaviours: " +
                "\r\n\t'Big Robot' gets really scared" +
                "\r\n\t'George v.2.1' becomes really mad because " +
                "it's always attacked by other robots" +
                "\r\n\t and R2D2 keeps its calm\r\n");

        r1.setBehaviour(new DefensiveBehaviour());
        r2.setBehaviour(new AgressiveBehaviour());

        r1.move();
        r2.move();
        r3.move();
    }
} // end of happy class    

public interface IBehaviour {
    public int moveCommand();
}

public class Robot {
    IBehaviour behaviour;
    String name;

    public Robot(String name)
    {
        this.name = name;
    }

    public void setBehaviour(IBehaviour behaviour)
    {
        this.behaviour = behaviour;
    }

    public IBehaviour getBehaviour()
    {
        return behaviour;
    }

    public void move()
    {
        System.out.println(this.name + ": Based on current position " +
                     "the behaviour object decided the next move is:");
        int command = behaviour.moveCommand();
        // ... send the command to mechanisms
        System.out.println("\tThe result returned by behaviour object " +
                    "is sent to the movement mechanisms " + 
                    "for the robot '"  + this.name + "'\n");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


public class AgressiveBehaviour implements IBehaviour{
    public int moveCommand()
    {
        System.out.println("\tAgressive Behaviour: if find another robot attack it");
        return 1;
    }
}

public class DefensiveBehaviour implements IBehaviour{
    public int moveCommand()
    {
        System.out.println("\tDefensive Behaviour: if find another robot run from it");
        return -1;
    }
}

public class NormalBehaviour implements IBehaviour{
    public int moveCommand()
    {
        System.out.println("\tNormal Behaviour: if find another robot ignore it");
        return 0;
    }
}

