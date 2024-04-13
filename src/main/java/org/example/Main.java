package org.example;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Connection connection;
    public static void main(String[] args) {
        //Establish connection to Database
        String sqlurl = "jdbc:postgresql://localhost:5432/Gym";
        String sqlusername = "postgres";
        String sqlpassword = "password";//very secure password
        System.out.println("Hello and welcome!");
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(sqlurl, sqlusername, sqlpassword);
        }
        catch(Exception e){System.out.println(e.toString());}

        //Generate starting menu
        int choice = startmenu();
        User user;
        switch (choice) {
            case (1):
                user = register();
                break;
            case(2):
                user = login();
                break;
            default:
                System.exit(0);
                user = null;
                break;
            }
            System.out.printf(", Welcome User " + user.getFirst() + " " + user.getLast() + "\n");

        //Generate main menu per role
        boolean logout = false;
        while(!logout) {
            int x;
            switch (user.getRole()) {
                case ("admin"):
                    x = adminMenu();
                    switch (x) {
                        case (1)://create group class
                            createClass();
                            break;
                        case(2):
                            deleteClass();
                            break;
                        case(3)://monitor equipment
                            equipmentDisplay();
                            break;
                        case(4)://process payment
                            makePayment();
                            break;
                        case(5)://logout
                            logout = true;
                            break;
                        default:
                            break;
                    }
                    break;

                case ("trainer"):
                    x = trainerMenu();
                    switch (x) {
                        case (1)://create personal session
                            createSession(user);
                            break;
                        case(2)://delete session
                            deleteSession(user);
                            break;
                        case(3)://view member by name
                            viewMember();
                            break;
                        case(4)://create routine
                            createRoutine(user);
                            break;
                        case(5)://delete routine
                            deleteRoutine(user);
                            break;
                        case(6):
                            logout = true;
                            break;
                        default:
                            break;
                    }
                    break;


                case ("member"):
                    x = userMenu();
                    switch (x) {
                        case (1)://view dashboard
                            dashBoard(user);
                            break;
                        case(2):
                            editGoal(user);
                            break;
                        case (3)://register for training
                            joinSession(user);
                            break;
                        case(4)://leave session
                            leaveSession(user);
                            break;
                        case (5)://register for class
                            joinClass(user);
                            break;
                        case(6)://view routines
                            viewRoutines();
                            break;
                        case (7)://Logout
                            logout = true;
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }
    }
    public static User register(){
        Scanner input = new Scanner(System.in);
        System.out.println("***************************");
        System.out.println("Welcome to Registration, please input required information.\n");
        System.out.println("Please Enter Email: ");
        String email = input.next();
        System.out.println("Please Enter Password: ");
        String password = input.next();
        password = hash(password);
        String role = "member";
        System.out.println("Please Enter First Name: ");
        String first_name = input.next();
        System.out.println("Please Enter Last Name: ");
        String last_name = input.next();
        try{
            if (connection != null){
                Statement statement = connection.createStatement();
                String query = "INSERT INTO Users (email,pwd,rolle,first_name, last_name,amount_owed) VALUES ('"+email+"','"+password+"','"+role+"','"+first_name+"','"+last_name+"',0);";
                //System.out.println(query);
                statement.execute(query);
                System.out.printf("User Registration Complete.");
            }
        }
        catch(Exception e){System.out.println(e.toString());}
        User user = new User(email, role, first_name, last_name);
        return user;
    }
    private static String hash(String password){
        //should actually be a cryptographic hash function here, this is a placeholder for now
        return (password);
    }
    private static User login(){
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("\n***************************");
            System.out.println("Please Enter Email: ");
            String email = input.next();
            System.out.println("Please Enter Password: ");
            String password = input.next();
            String hashed = hash(password);
            try{
                if (connection != null){
                    Statement statement = connection.createStatement();
                    String query = "SELECT COUNT(*) FROM Users WHERE pwd = '"+hashed+"' AND email = '"+email+"'";
                    statement.executeQuery(query);
                    ResultSet resultSet = statement.getResultSet();
                    resultSet.next();
                    int count = resultSet.getRow();
                    if (count==1){
                        Statement newstatement = connection.createStatement();
                        query = "SELECT first_name,last_name,rolle FROM Users WHERE pwd ='"+hashed+"' AND email = '"+email+"'";
                        newstatement.executeQuery(query);
                        ResultSet rs = newstatement.getResultSet();
                        if(rs.next()) {
                            String first_name = rs.getString("first_name");
                            String last_name = rs.getString("last_name");
                            String role = rs.getString("rolle");
                            User user = new User(email, role, first_name, last_name);
                            System.out.printf("Login Successful");
                            return user;
                        }
                        else{System.out.println("Seemingly Not working");}
                    }
                    else{
                        System.out.println("Invalid Login Credentials, Please Try Again.");
                    }
                }
            }
            catch(Exception e){System.out.println(e.toString());}
        }
    }
    private static int startmenu(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nWelcome to the Gym Administration System");
        System.out.println("Please Select An Option to Proceed");
        System.out.println(" (1) - Register as a new member");
        System.out.println(" (2) - Login as existing user");
        System.out.println(" (3) - Quit System");
        try{
            int number = input.nextInt();
            return(number);
        }
        catch(Exception e){}
        return(0);
    }
    private static int adminMenu(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nWelcome to the Admin Main Menu");
        System.out.println("Please Select An Option to Proceed");
        System.out.println(" (1) - Book a Group Fitness Class");
        System.out.println(" (2) - Delete a Group Fitness Class");
        System.out.println(" (3) - Monitor Equipment");
        System.out.println(" (4) - Process Payment for User");
        System.out.println(" (5) - Logout");
        try{
            int number = input.nextInt();
            return(number);
        }
        catch(Exception e){}
        return(0);
    }
    private static int trainerMenu(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nWelcome to the Trainer Main Menu");
        System.out.println("Please Select An Option to Proceed");
        System.out.println(" (1) - Create a new Personal Training Session");
        System.out.println(" (2) - Delete a Personal Training Session");
        System.out.println(" (3) - View Member Profile by Name");
        System.out.println(" (4) - Create Exercise Routine");
        System.out.println(" (5) - Delete Exercise Routine");
        System.out.println(" (6) - Logout");
        try{
            int number = input.nextInt();
            return(number);
        }
        catch(Exception e){}
        return(0);
    }
    private static int userMenu(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nWelcome to the Member Main Menu");
        System.out.println("Please Select An Option to Proceed");
        System.out.println(" (1) - View User Dashboard");
        System.out.println(" (2) - Edit/Create Goal");
        System.out.println(" (3) - Register for Personal Training");
        System.out.println(" (4) - Cancel Personal Training Session");
        System.out.println(" (5) - Register for Group Class");
        System.out.println(" (6) - View Exercise Routines");
        System.out.println(" (7) - Logout");
        try{
            int number = input.nextInt();
            return(number);
        }
        catch(Exception e){}
        return(0);
    }
    private static void createSession(User trainer){
        Scanner input = new Scanner(System.in);
        System.out.println("\nTo create a new available session, follow the prompt");
        System.out.println("Please enter the date of the session (YYYY-MM-DD) :");
        String session_date = input.next();
        System.out.println("Please enter the start time of the session (HH:MM:SS) :");
        String start_time = input.next();
        java.sql.Time stime = Time.valueOf(start_time);
        System.out.println("Please enter the end time of the session (HH:MM:SS) :");
        String end_time = input.next();
        System.out.println("Please enter the fee of the session ($$.¢¢) :");
        String sfee = input.next();
        try {
            Float fee = Float.parseFloat(sfee);
            java.sql.Date date = Date.valueOf(session_date);
            java.sql.Time etime = Time.valueOf(end_time);
            if (connection != null) {
                //decided to use prepared statement instead of regular statement so my brain doesn't explode
                String query = "INSERT INTO TrainerSessions(trainer_email,session_day,start_time,end_time,fee)" + "Values (?,?,?,?,?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, trainer.getEmail());
                    statement.setDate(2, date);
                    statement.setTime(3, stime);
                    statement.setTime(4, etime);
                    statement.setFloat(5, fee);
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A session was successfully created");
                    } else {
                        System.out.println("Operation Failed");
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
        catch(Exception e){System.out.println("Create Session Failed "+e.toString());}
    }
    private static void deleteSession(User trainer){
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Your Sessions********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM trainersessions WHERE trainer_email = '" + trainer.getEmail() + "' ORDER BY session_id ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String id = resultSet.getString("session_id");
                    String day = resultSet.getString("session_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    float fee = resultSet.getFloat("fee");

                    System.out.println("ID:"+id+" on "+day+" between "+stime+" and "+etime+" ,fee:$"+fee);

                }
                System.out.println("*********End of Your Sessions********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Please enter the ID of the session you would like to delete or -1 to quit");
        int id = input.nextInt();
        input.nextLine();
        if (id != -1) {
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "DELETE FROM trainerSessions WHERE session_id = '" + id + "';";
                    statement.execute(query);
                    System.out.println("Session Deleted Successfully");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        else{System.out.println("No Session Deleted, Going Back to Menu");}
    }
    private static void viewMember(){
        Scanner input = new Scanner(System.in);
        System.out.println("Search members by name");
        System.out.println("Enter user first name: ");
        String first_name = input.next();
        System.out.println("Enter user last name: ");
        String last_name = input.next();
        System.out.println("*****Search Results*****");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM USERS WHERE first_name = '"+first_name+"' AND last_name = '"+last_name+"'";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    String email = resultSet.getString("email");
                    String amount_owed = resultSet.getString("amount_owed");
                    String goal = resultSet.getString("goal");
                    int goalMetric = resultSet.getInt("goal_metric");
                    System.out.println("EMAIL: "+email+" BALANCE: "+amount_owed+" GOAL: "+goal+" METRIC: "+goalMetric);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    private static void joinSession(User member) {
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Available Sessions********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM TrainerSessions WHERE student_email = '' ORDER BY session_day ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    String id = resultSet.getString("session_id");
                    String email = resultSet.getString("trainer_email");
                    String day = resultSet.getString("session_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    String fee = resultSet.getString("fee");
                    System.out.println(id+" "+email+" "+day+" "+stime+" "+etime+" $"+fee);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("*********End of Available Sessions********");
        System.out.println("Please enter the ID of the session you would like to register for");
        int id = input.nextInt();
        input.nextLine();
        try {
            if (connection != null) {
                Statement selector = connection.createStatement();
                selector.execute("SELECT fee FROM TrainerSessions WHERE session_id = "+id);
                ResultSet rs = selector.getResultSet();
                rs.next();
                float fee = rs.getFloat("fee");

                Statement newstatement = connection.createStatement();
                newstatement.executeUpdate("UPDATE TrainerSessions SET student_email = '" + member.getEmail() + "' WHERE session_id = "+id);

                Statement feestatement1 = connection.createStatement();
                feestatement1.executeQuery("SELECT amount_owed FROM users WHERE email = '"+member.getEmail()+"'");
                ResultSet resultSet = feestatement1.getResultSet();
                resultSet.next();
                float balance = resultSet.getFloat("amount_owed");

                float newbalance = balance + fee;

                Statement feestatement3 = connection.createStatement();
                feestatement3.executeUpdate("UPDATE users SET amount_owed = "+newbalance+" WHERE email = '"+member.getEmail()+"'");

                System.out.println("Successfully registered in session "+id+" , account balance updated");
            }
        }
        catch(Exception e){System.out.println(e);}

    }
    private static void leaveSession(User member){
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Your Sessions********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM TrainerSessions WHERE student_email = '"+member.getEmail()+"' ORDER BY session_day ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    String id = resultSet.getString("session_id");
                    String email = resultSet.getString("trainer_email");
                    String day = resultSet.getString("session_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    String fee = resultSet.getString("fee");
                    System.out.println(id+" "+email+" "+day+" "+stime+" "+etime+" $"+fee);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("*********End of Your Sessions********");
        System.out.println("Please enter the ID of the session you would like to leave or -1 to quit");
        int id = input.nextInt();
        input.nextLine();
        if(id!=-1) {
            try {
                if (connection != null) {
                    Statement selector = connection.createStatement();
                    selector.execute("SELECT fee FROM TrainerSessions WHERE session_id = " + id);
                    ResultSet rs = selector.getResultSet();
                    rs.next();
                    float fee = rs.getFloat("fee");

                    Statement newstatement = connection.createStatement();
                    newstatement.executeUpdate("UPDATE TrainerSessions SET student_email = '' WHERE session_id = " + id);

                    Statement feestatement1 = connection.createStatement();
                    feestatement1.executeQuery("SELECT amount_owed FROM users WHERE email = '" + member.getEmail() + "'");
                    ResultSet resultSet = feestatement1.getResultSet();
                    resultSet.next();
                    float balance = resultSet.getFloat("amount_owed");

                    float newbalance = balance - fee;

                    Statement feestatement3 = connection.createStatement();
                    feestatement3.executeUpdate("UPDATE users SET amount_owed = " + newbalance + " WHERE email = '" + member.getEmail() + "'");

                    System.out.println("Successfully left session " + id + " , account balance updated");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else{System.out.println("No session left, returning to menu");}
    }
    private static boolean proccesspayment(){return true;}
    private static void makePayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please input Member Email");
        String memberEmail = input.next();
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT amount_owed FROM users WHERE email = '" + memberEmail + "'");
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                float balance = resultSet.getFloat("amount_owed");

                System.out.println("Current amount owed: $"+balance);

                System.out.println("Enter payment amount: ");
                float payment = input.nextFloat();
                System.out.println("Please process payment");
                if(proccesspayment()) {
                    balance = balance - payment;
                }

                Statement feestatement3 = connection.createStatement();
                feestatement3.executeUpdate("UPDATE users SET amount_owed = " + balance + " WHERE email = '" + memberEmail + "'");

                System.out.println("Payment made successfully, new balance is $"+balance);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    private static void createClass(){
        Scanner input = new Scanner(System.in);
        System.out.println("\nTo create a new group class, follow the prompt");
        System.out.println("Please Enter the name of the class: ");
        String name = input.nextLine();
        System.out.println("Please enter the trainers email: ");
        String trainer_email = input.nextLine();
        System.out.println("Please enter the date of the session (YYYY-MM-DD) :");
        String session_date = input.nextLine();
        System.out.println("Please enter the start time of the session (HH:MM:SS) :");
        String start_time = input.nextLine();
        System.out.println("Please enter the end time of the session (HH:MM:SS) :");
        String end_time = input.nextLine();
        System.out.println("Please enter the max capacity of the class: ");
        int capacity = input.nextInt();
        input.nextLine();
        System.out.println("Please enter the room name for the class:");
        String room = input.nextLine();
        System.out.println("Please enter the fee of the session ($$.¢¢) :");
        String sfee = input.nextLine();
        String[] emails = {};
        try {
            java.sql.Array emailArr = connection.createArrayOf("text",emails);
            Float fee = Float.parseFloat(sfee);
            java.sql.Date date = Date.valueOf(session_date);
            java.sql.Time stime = Time.valueOf(start_time);
            java.sql.Time etime = Time.valueOf(end_time);
            if (connection != null) {
                //decided to use prepared statement instead of regular statement so my brain doesn't explode
                String query = "INSERT INTO GroupClasses(title,trainer_email,class_day,start_time,end_time,max_capacity,students,student_emails,room,fee) VALUES (?,?,?,?,?,?,?,?,?,?);";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, name);
                    statement.setString(2, trainer_email);
                    statement.setDate(3,date);
                    statement.setTime(4, stime);
                    statement.setTime(5, etime);
                    statement.setInt(6, capacity);
                    statement.setInt(7,0);
                    statement.setArray(8,emailArr);
                    statement.setString(9,room);
                    statement.setFloat(10,fee);
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A class was successfully created");
                    } else {
                        System.out.println("Operation Failed");
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
        catch(Exception e){System.out.println("Create Session Failed "+e.toString());}
    }
    private static void deleteClass(){
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Classes********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM groupClasses ORDER BY class_id ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String id = resultSet.getString("class_id");
                    String trainer = resultSet.getString("trainer_email");
                    String day = resultSet.getString("class_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    int capacity = resultSet.getInt("max_capacity");
                    int current = resultSet.getInt("students");
                    float fee = resultSet.getFloat("fee");

                    System.out.println("ID:"+id+" with "+trainer+" on "+day+" between "+stime+" and "+etime+" "+current+"/"+capacity+" ,fee:$"+fee);

                }
                System.out.println("*********End of Classes********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Please enter the ID of the class you would like to delete or -1 to quit");
        int id = input.nextInt();
        input.nextLine();
        if (id != -1) {
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "DELETE FROM groupClasses WHERE class_id = '" + id + "';";
                    statement.execute(query);
                    System.out.println("Class Deleted Successfully");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        else{System.out.println("No Class Deleted, Going Back to Menu");}
    }
    private static void joinClass(User member){
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Available Classes********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM GroupClasses ORDER BY class_day ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    String id = resultSet.getString("class_id");
                    String title = resultSet.getString("title");
                    String email = resultSet.getString("trainer_email");
                    String day = resultSet.getString("class_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    int capacity = resultSet.getInt("max_capacity");
                    int students = resultSet.getInt("students");
                    String fee = resultSet.getString("fee");
                    System.out.println(id+" "+title+" "+email+" "+day+" "+stime+" "+etime+" "+students+"/"+capacity+" $"+fee);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("*********End of Available Classes********");
        System.out.println("Please enter the ID of the session you would like to register for");
        int id = input.nextInt();
        input.nextLine();
        try {
            if (connection != null) {
                Statement selector = connection.createStatement();
                selector.execute("SELECT student_emails,fee,students,max_capacity FROM GroupClasses WHERE class_id = "+id);
                ResultSet rs = selector.getResultSet();
                rs.next();
                float fee = rs.getFloat("fee");
                int students = rs.getInt("students");
                int capacity = rs.getInt("max_capacity");
                boolean notFull = students<=capacity;
                if(notFull) {
                    students = students+1;
                    java.sql.Array emails = rs.getArray("student_emails");
                    String[] arr = (String[]) emails.getArray();
                    String[] narr = Arrays.copyOf(arr, arr.length + 1);
                    narr[arr.length] = member.getEmail();
                    Array sarr = connection.createArrayOf("text",narr);

                    Statement feestatement1 = connection.createStatement();
                    feestatement1.executeQuery("SELECT amount_owed FROM users WHERE email = '" + member.getEmail() + "'");
                    ResultSet resultSet = feestatement1.getResultSet();
                    resultSet.next();
                    float balance = resultSet.getFloat("amount_owed");

                    float newbalance = balance + fee;

                    Statement feestatement3 = connection.createStatement();
                    feestatement3.executeUpdate("UPDATE users SET amount_owed = " + newbalance + " WHERE email = '" + member.getEmail() + "'");

                    String sql = "UPDATE GroupClasses SET student_emails = ?,students = ? WHERE class_id = ?";
                    PreparedStatement updator = connection.prepareStatement(sql);
                    updator.setArray(1,sarr);
                    updator.setInt(2,students);
                    updator.setInt(3,id);
                    updator.executeUpdate();

                    System.out.println("Successfully registered in class " + id + " , account balance updated");
                }
                else{
                    System.out.println("Class full, please try again.");
                }
            }
        }
        catch(Exception e){System.out.println(e);}

    }
    private static void createRoutine(User trainer){
        Scanner input = new Scanner(System.in);
        System.out.println("\nTo create a new group class, follow the prompt");
        System.out.println("Please Enter the text of the routine :");
        String routine_text = input.nextLine();
        System.out.println("Please enter the equipment required for the routine :");
        String equipment_needed = input.nextLine();
        System.out.println("Please enter the estimated duration of the routine:");
        int estimated_duration = input.nextInt();
        input.nextLine();
        try {
            if (connection != null) {
                //decided to use prepared statement instead of regular statement so my brain doesn't explode
                String query = "INSERT INTO exerciseRoutines(trainer_email,estimated_duration,equipment_needed,routine_text) VALUES (?,?,?,?);";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, trainer.getEmail());
                    statement.setInt(2, estimated_duration);
                    statement.setString(3,equipment_needed);
                    statement.setString(4,routine_text);
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("An exercise routine was successfully created");
                    } else {
                        System.out.println("Operation Failed");
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
        catch(Exception e){System.out.println("Create Routine Failed "+e.toString());}
    }
    private static void deleteRoutine(User trainer) {
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Your Created Routines********");
        System.out.println("ID Duration Equipment Routine");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM exerciseRoutines WHERE trainer_email = '" + trainer.getEmail() + "' ORDER BY routine_id ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String id = resultSet.getString("routine_id");
                    int duration = resultSet.getInt("estimated_duration");
                    String equip = resultSet.getString("equipment_needed");
                    String routine = resultSet.getString("routine_text");

                    System.out.println(id + " " + duration + "mins " + equip + " " + routine);

                }
                System.out.println("*********End of Your Created Routines********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Please enter the ID of the routine you would like to delete or -1 to quit");
        int id = input.nextInt();
        input.nextLine();
        if (id != -1) {
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "DELETE FROM exerciseRoutines WHERE routine_id = '" + id + "';";
                    statement.execute(query);
                    System.out.println("Routine Deleted Successfully");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        else{System.out.println("No Routine Deleted, Going Back to Menu");}
    }
    private static void viewRoutines(){
        Scanner input = new Scanner(System.in);
        System.out.println("********List of Routines********");
        System.out.println("ID Duration Equipment Routine Trainer");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM exerciseRoutines ORDER BY routine_id ASC;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String id = resultSet.getString("routine_id");
                    int duration = resultSet.getInt("estimated_duration");
                    String equip = resultSet.getString("equipment_needed");
                    String routine = resultSet.getString("routine_text");
                    String trainer = resultSet.getString("trainer_email");

                    System.out.println(id + " " + duration + "mins " + equip + " " + routine+" "+trainer);

                }
                System.out.println("*********End of Routines********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    private static void editGoal(User member){
        Scanner input = new Scanner(System.in);
        System.out.println("Current Goal | Metric");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT goal,goal_metric FROM users WHERE email = '"+member.getEmail()+"';";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                String goal = resultSet.getString("goal");
                float metric = resultSet.getFloat("goal_metric");
                System.out.println(goal+" | "+metric);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Would you like to change your goal? (y/n)");
        String x = input.nextLine();
        if(x.equals("y")){
            System.out.println("Please input your new goal");
            String ngoal = input.nextLine();
            try {
                Statement feestatement3 = connection.createStatement();
                feestatement3.executeUpdate("UPDATE users SET goal = '" + ngoal + "',goal_metric = 0 WHERE email = '" + member.getEmail() + "'");
                System.out.println("New goal set");
            }
            catch(Exception e){System.out.println(e.toString());}
        }
        else{
            System.out.println("Do you want to update your goal metric? (y/n)");
            x = input.nextLine();
            if(x.equals("y")){
                System.out.println("Please input your updated metric");
                float nmetric = input.nextFloat();
                try {
                    Statement feestatement3 = connection.createStatement();
                    feestatement3.executeUpdate("UPDATE users SET goal_metric = '" + nmetric + "' WHERE email = '" + member.getEmail() + "'");
                    System.out.println("Metric updated");
                }
                catch(Exception e){System.out.println(e.toString());}
            }
        }

    }
    private static void equipmentDisplay(){
        Scanner input = new Scanner(System.in);
        System.out.println("Equipment that May need maintenance soon (above 90% threshold)");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM equipment WHERE current_hours>=maintenance_hours*0.9;";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    int serial = resultSet.getInt("serial_number");
                    String title = resultSet.getString("title");
                    float current_hours = resultSet.getFloat("current_hours");
                    float maintenance_hours = resultSet.getFloat("maintenance_hours");
                    int value = resultSet.getInt("val");
                    System.out.println(title + " | " + serial + " | " + current_hours + "/" + maintenance_hours + " | VALUE: " + value);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Do you want to view other equipment? (y/n)");
        String x = input.nextLine();

        if(x.equals("y")) {
            System.out.println("All other equipment");
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM equipment WHERE current_hours<maintenance_hours*0.9;";
                    statement.executeQuery(query);
                    ResultSet resultSet = statement.getResultSet();
                    while(resultSet.next()) {
                        int serial = resultSet.getInt("serial_number");
                        String title = resultSet.getString("title");
                        float current_hours = resultSet.getFloat("current_hours");
                        float maintenance_hours = resultSet.getFloat("maintenance_hours");
                        int value = resultSet.getInt("val");
                        System.out.println(title + " | " + serial + " | " + current_hours + "/" + maintenance_hours + " | VALUE: " + value);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        System.out.println("Did you perform some service? (y/n)");
        x = input.nextLine();
        if(x.equals("y")) {
            System.out.println("Please enter the serial number of the item that has been serviced");
            int s = input.nextInt();
            input.nextLine();
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "UPDATE equipment SET current_hours = 0 WHERE serial_number = '"+s+"';";
                    statement.executeUpdate(query);
                    System.out.println("Equipment #"+s+" has been serviced and reset hours to 0");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        System.out.println("Do you want to insert a new piece of equipment (y/n)");
        x = input.nextLine();
        if(x.equals("y")) {
            System.out.println("Please enter the serial number of the new item");
            int serial = input.nextInt();
            input.nextLine();
            System.out.println("Please enter the serial title of the new item");
            String title = input.nextLine();
            System.out.println("Please enter the maintenance hours of the new item");
            float maintenance_hours = input.nextFloat();
            input.nextLine();
            System.out.println("Please enter the value of the new item");
            int value = input.nextInt();
            input.nextLine();
            try {
                if (connection != null) {
                    String query = "INSERT INTO equipment (serial_number,title,current_hours,maintenance_hours,val) VALUES (?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, serial);
                    statement.setString(2, title);
                    statement.setFloat(3, 0);
                    statement.setFloat(4, maintenance_hours);
                    statement.setInt(5, value);
                    statement.execute();
                    System.out.println("New item #" + serial + " added");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        System.out.println("Do you want to delete piece of equipment (y/n)");
        x = input.nextLine();
        if(x.equals("y")) {
            System.out.println("Please enter the serial number of the new item");
            int serial = input.nextInt();
            input.nextLine();
            try {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String query = "DELETE FROM equipment where serial_number = '" + serial + "'";
                    statement.execute(query);
                    System.out.println("Item #" + serial + " deleted");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    private static void dashBoard(User member){
        //Scanner input = new Scanner(System.in);
        System.out.println("********User Information********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM users WHERE email = '"+member.getEmail()+"';";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("rolle");
                    String first = resultSet.getString("first_name");
                    String last = resultSet.getString("last_name");
                    float bal = resultSet.getFloat("amount_owed");
                    String goal = resultSet.getString("goal");
                    float metric = resultSet.getFloat("goal_metric");

                    System.out.println("Email:"+email);
                    System.out.println("Role:"+role);
                    System.out.println("Name:"+first+" "+last);
                    System.out.println("Amount Owing:$"+bal);
                    System.out.println("Goal:"+goal);
                    System.out.println("Goal Progress:"+metric);
                }
                System.out.println("*********End of User Information********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("********Registered Sessions********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM trainerSessions WHERE student_email = '"+member.getEmail()+"';";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String email = resultSet.getString("trainer_email");
                    String day = resultSet.getString("session_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    float fee = resultSet.getFloat("fee");

                    System.out.println("Session with trainer "+email+" on "+day+" between "+stime+" and "+etime+", fee:$"+fee);
                }
                System.out.println("*********End of Registered Sessions********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("********Registered Classes********");
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM groupClasses WHERE '"+member.getEmail()+"' = ANY(student_emails);";
                statement.executeQuery(query);
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String trainer = resultSet.getString("trainer_email");
                    String day = resultSet.getString("class_day");
                    String stime = resultSet.getString("start_time");
                    String etime = resultSet.getString("end_time");
                    String room = resultSet.getString("room");
                    float fee = resultSet.getFloat("fee");

                    System.out.println("Class "+title+" with trainer "+trainer+" on "+day+" between "+stime+" and "+etime+" in "+room+", fee:$"+fee);
                }
                System.out.println("*********End of Registered Classes********");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}