# What is Bookyard?
Bookyard is a trivial client/server application written in the Kotlin programmming language. It is written with the purpose of illustrating how to write a simple application using the Kotlin programming language and then how to secure it using Json Web Tokens (JWT's).

# What does it do?
The application is meant to return a list of book recommendations for a logged in user, based on his likes. All the data comes from a database server.

# What kind of a client and server does Bookyard have?
The application has two parts:
 1. A desktop application; and
 2. A web application that acts as a Web API. The endpoints return Json responses. The web application gets data from a database.

# What are the modules in the project and what do they do?
 1. Server: Embodies the HTTP Servlet API and its helper classes.
 2. Client: Embodies the Swing desktop application.
 3. Contracts : Embodies the types used to send and receive data between the client and the server.

# What technologies did you use to develop this application?
I am a C# developer so all of this was new to me. In order to localize complexity, I first learnt the Kotlin language over a week or so, then wrote a few trivial programs with it of the order of adding the elements of an array or using companion objects, object declarations, property setters and getters, read-only properties, and the like. Then, I  wrote the application in Java first, and then translated each line in Kotlin to arrive at the Kotlin implementation.

The source code for the Java application is in the folder named **Java** at the project root. The **Kotlin** folder has the same application written in Kotlin. The **db** folder has the database script. If you want to run the application, you'll need to feed this database script to a Microsoft SQL Server database.

I wrote the Java version using the Eclipse for Java EE, Mars 2 IDE. With the exception of this, everything else I used to write the Java version was the same as that I used to write the Kotlin version.

Here is a list of tools I used to develop the Kotlin version of this application:
 1. The desktop application is built using the Swing/AWT API (java.swing._*_, java.awt._*_).
 2. The web application is built using HTTP Servlets (javax.servlets .http.*)
 3. The application server is Apache Tomcat 8.0.x
 4. The database used is Microsoft SQL Server 2012.
 5. JDBC is used for database connectivity.
 5. The programming language is Kotlin.
 6. The IDE used is IntelliJ IDEA Ultimate Edition. I tried building this with Eclipse first. Though there is a Kotlin plug-in for Eclipse, the experience building with that plug-in is not that great. That's because there is little to no intellisense available for Kotlin when using the Eclipse plug-in. And the compilation / build process does not output the `.class` files to a location that you as a developer know about. It publishes the `.class` files to an internal folder it uses, which is no good because then you can't set that path as the %CLASSPATH% because you don't know where that location is.
 7. The endpoints on the server return Json responses. The Jackson library is used for serializing and deserializing objects into json string and vice-versa.
 8. The jwtk/jjwt library is used for building and parsing Json Web Tokens (JWT's).

# What is the workflow? In other words, what screen appears first and then what button do we click and then what other screen appears, and so on?
The client application provides a login dialog box. The user logs in with the user name **Sathyaish** and password **FooBar**.

> Since this application was written to illustrate the use of the Kotlin programming language only, its features have been kept to a bare minimum. There is no way to create a new user.

![Bookyard login dialog](https://raw.githubusercontent.com/Sathyaish/Bookyard/master/images/BookyardLogin.png)

Upon successful login, the login dialog disappears and a window displaying a list of books recommended for the user based on their likes appears.

[Bookyard Recommendations Window](https://raw.githubusercontent.com/Sathyaish/Bookyard/master/images/RecommendationsWindow.png)


# How does it work?

# What problems did you face when developing this application?
Being a C# developer, I faced a lot of problems at each step during development. I'd done a bit of Visual J++ back in 1999. That's the Microsoft version of Java, and was also a bit familiar with the Java language. And it was easy to pick up the Kotlin language as well.

Languages are easy. It's the tools and the variety of frameworks and their quirky behavior that takes all of the time and frustration. For instance, learning about the layouts in Swing, or aligning child elements horizontally or vertically within a graphical user interface container, or getting the Java class from a Kotlin `KClass`, or finding out how to set the %CLASSPATH% or how to set static references to JAR files with each IDE, or how to use an open source Java project that had no JAR files, how to make JAR files from Java source, how to use Maven, etc. These things took me all the time that I took to develop this.

Here is a list of some problems I faced during development, with the solutions:

 1. [Could you please explain this piece of code in terms of C# code?](http://stackoverflow.com/q/39010222/303685)
 2. [What exactly is a dense array?](http://stackoverflow.com/q/39030196/303685)
 3. [Can a covariant type parameter be in an input position in the constructor?](http://stackoverflow.com/q/39061831/303685)
 4. [How to create a Java Servlet application in IntelliJ IDEA Community Edition with Java EE 7 and Glassfish 4?](http://stackoverflow.com/q/39072303/303685)
 5. [How to create a servlet using Eclipse Version: Mars.1 Release (4.5.1)?](http://stackoverflow.com/q/39073274/303685)
 6. [Some questions from a .NET developer about running Java programs in Eclipse](http://stackoverflow.com/q/39075568/303685)
 7. [How to have Eclipse intellisense autocomplete member names?](http://stackoverflow.com/q/39076868/303685)
 8. [Why don't you have to add an import statement for the java.lang.Runnable interface?](http://stackoverflow.com/q/39142076/303685)
 9. [How to rename a variable in Eclipse JEE Mars 2?](http://stackoverflow.com/q/39142777/303685)
 10. [What does the percentage sign next to some of the intellisense members in Eclipse mean?](http://stackoverflow.com/q/39143312/303685)
 11. [Do JFrame windows in Swing run on their own separate threads?](http://stackoverflow.com/q/39156990/303685)
 12. [Doesn't using an anonymous class to add an action listener cause a memory leak?](http://stackoverflow.com/q/39157911/303685)
 13. [How do I close a JDialog with the behavior I want?](http://stackoverflow.com/q/39162901/303685)
 14. [How do I left align a JLabel inside a JPanel?](http://stackoverflow.com/q/39163624/303685)
 15. [What do I have to do to use an external open source library in my Java project?](http://stackoverflow.com/q/39178734/303685)
 16. [How do I reference one Eclipse project from another?](http://stackoverflow.com/q/39202217/303685)
 17. [How to set up Apache Tomcat 8 to run a servlet application on HTTPS](http://stackoverflow.com/q/39206371/303685)
 18. [Unable to connect to SQL Server via JDBC. No suitable driver found for jdbc:sqlserver://](http://stackoverflow.com/q/39219213/303685)
 19. [How do I get back my .class files?](http://stackoverflow.com/q/39220403/303685)
 20. [java.lang.NoClassDefFoundError for a class within a referenced project](http://stackoverflow.com/q/39242043/303685)
 21. [Where in Eclipse do I see a project's build progress?](http://stackoverflow.com/q/39242724/303685)
 22. [How to set Run Configuration and Debug Configuration in Eclipse to choose the class that has the main method as the start-up class?](http://stackoverflow.com/q/39245479/303685)
 23. [Select matching rows from a table where either one of two columns contain any value from a list of values](http://stackoverflow.com/q/39249164/303685)
 24. [Conversion of java.util.Date to java.sql.Date truncates the time part](http://stackoverflow.com/q/39270883/303685)
 25. [How to set just the left coordinates and the width of a JPanel?](http://stackoverflow.com/q/39293172/303685)
 26. [Does Kotlin provide any implementations of its collection interfaces?](http://stackoverflow.com/q/39345248/303685)
 27. [Convert a nullable type to its non-nullable type?](http://stackoverflow.com/q/39349700/303685)
 28. [What's the correct syntax for overriding an interface member function with a visibility modifier?](http://stackoverflow.com/q/39350353/303685)
 29. [Apache Tomcat 8.0 unable to load servlet class written in Kotlin using Eclipse for Java EE, Mars 2](http://stackoverflow.com/q/39351875/303685)
 30. [Where are the .class files?](http://stackoverflow.com/q/39352380/303685)
 31. [How do I tell Eclipse to compile my Kotlin source files into .class files without using an external build tool?](http://stackoverflow.com/q/39352745/303685)
 32. [Error running an Apache Tomcat servlet written in Kotlin](http://stackoverflow.com/q/39425467/303685)
 33. [How to select a different module to run when you click the Run button in IntelliJ IDEA?](http://stackoverflow.com/q/39425753/303685)
 34. [What compile-time type must I assign to receieve a return value of java.util.Map<TextAttribute, ?>](http://stackoverflow.com/q/39372238/303685)
 35. [How do I get the class name from a type name?](http://stackoverflow.com/q/39377043/303685)


# What do I need as a set-up to run this code if I download it?

# How do I launch the application?



The client application makes a secured Web request, i.e. an HTTPS POST request to the server at the end-point **/login**. The client packages the user name and password in a Json Web Token (JWT) that is signed with the client's *application secret*.

In response to a login request, the server send back a Json string with the following object structure (pseudo-code):

```
OperationResult<User> where:

class User
  public int id;
  public string userName;
  
class OperationResult<T>
  public bool successful;
  public String? errorMessage;
  public T? data;
```

To be contd...


# How can I reach you?
If you find any errors in this application or you need help setting this application up in your environment, please [create a new issue](https://github.com/Sathyaish/Bookyard/issues) in this repository. If you would like to hire me, please reach me at Sathyaish@gmail.com.
