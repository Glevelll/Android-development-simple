# ScotchApp

Dating app. It has an interface that is taken from various libraries. The application is written in Java. An API is written for the application, which allows it to interact with the server. The api was written by the user kek-flip in Go. The direct development of the application was carried out with the user DragIv.

The application has an ergonomic interface and connection with the server for storing, modifying and retrieving data in the PostgreSQL DBMS. Development was carried out in Android Studio

<H3> Key technologies used: </H3>
Android SDK  <br/>
Gradle  <br/>
Using OOP  <br/>
Using Third Party Design Libraries  <br/>
HttpUrlConnection  <br/>
XML  <br/>
Git  <br/>
Cookie  <br/>
Exception Handling  <br/>
Multithreading is implemented in the Registration window as a demonstration of skills, but not implemented in other windows due to API features  <br/>


<H3> The structure of the application is as follows: </H3>
There are two main windows: for login and for registration. After a correct login, 3 fragments appear: profile, search and favorites. <br/>
From the profile fragment, you can go to the settings or go to the login window. <br/>
In the search snippet, you can rate other users, view their information, and search by filters. <br/>
In the favorites fragment, there is an opportunity to see those whom I liked and those who liked me. <br/>

<H3>Sign In</H3>
![image](https://github.com/Glevelll/Android-development/assets/113721736/6060d4d8-a16d-4553-b799-a9462e9850c3) <br/>
The login window contains a transition to the registration window and fields for entering a login and password. The entered login and password are sent to the server using a POST request. If successful, the server returns a cookie for the user, which will be necessary for further interaction of the authorized user with the application. If the data entry is incorrect, the user is informed about it.

<H3>Sign Up</H3>

<H3>Profile</H3>
<H3>Settings</H3>
<H3>Search</H3>
<H3>Filter</H3>
<H3>Full info</H3>
<H3>Favourite</H3>
