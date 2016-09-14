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
 5. The programming language is Kotlin.
 6. The IDE used is IntelliJ IDEA Ultimate Edition. I tried building this with Eclipse first. Though there is a Kotlin plug-in for Eclipse, the experience building with that plug-in is not that great. That's because there is little to no intellisense available for Kotlin when using the Eclipse plug-in. And the compilation / build process does not output the `.class` files to a location that you as a developer know about. It publishes the `.class` files to an internal folder it uses, which is no good because then you can't set that path as the %CLASSPATH% because you don't know where that location is.
 7. The endpoints on the server return Json responses. The Jackson library is used for serializing and deserializing objects into json string and vice-versa.
 8. The jwtk/jjwt library is used for building and parsing Json Web Tokens (JWT's).

# What is the workflow? In other words, what screen appears and then what button do we click and then what other screen appears, and so on?
The client application provides a login dialog box. The user logs in with the user name **Sathyaish** and password **FooBar**.

> Since this application was written to illustrate the use of the Kotlin programming language only, its features have been kept to a bare minimum. There is no way to create a new user.

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
