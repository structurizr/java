# this needs to point to a binary/compiled version of the Spring PetClinic app
export SPRING_PETCLINIC_WEBAPP=~/sandbox/spring/spring-petclinic/target/spring-petclinic-1.0.0-SNAPSHOT

echo "Running Spring PetClinic example using binary version of webapp at $SPRING_PETCLINIC_WEBAPP"
java -classpath $SPRING_PETCLINIC_WEBAPP/WEB-INF/classes:build/dist/core/*:build/dist/core/lib/*::build/dist/spring/*:build/dist/spring/lib/*:build/examples/production-classes com.structurizr.example.spring.petclinic.SpringPetClinic