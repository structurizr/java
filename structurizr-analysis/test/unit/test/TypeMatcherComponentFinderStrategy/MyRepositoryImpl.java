package test.TypeMatcherComponentFinderStrategy;

public class MyRepositoryImpl implements MyRepository {

    public void doSomething() {
        // make a reference to an inner class
        throw new RuntimeException() {
            @Override
            public String getMessage() {
                return "I'm an inner class.";
            }
        };
    }

}