# pager

Run tests:
- IntelliJ: Import the project, then select Run All tests
- Command line: mvn test

Notes
- Service tests are covering only the acceptance criterias described in the use case.
- All the adapters have been implemented as infrastructure services. 
An annotation @ExternalActor is used to describe the direction of the communication and their type.
- In the pager bounded context the aggregate is Alert.
- The Application and Infrastructure layer are not visible in the solution. 
The repository and infrastructure services will be implemented in the Infrastructure layer. 
This is the place where the conversion to and from the domain entity and value objects from and to the types for all the SPIs will stand.
- There are no Spring annotations on the domain types. 
The annotated types will lay in the Application and Infrastructure Layer and injected at run time by Spring.
