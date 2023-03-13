//get required libraries
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PasswordManager {

    //Create connection object and initialize it 
    Connection con;
    public PasswordManager(Connection con){
        this.con = con;
    }

    /** Login method for the SQL database
     * @param username
     * @param password
     * @return boolean, Returns true if the login is correct and false if its not
     * @throws SQLException
     */
    public boolean login(String password) throws SQLException {
        
        //Send a query to the database to exectute the password
        Statement statement = this.con.createStatement();
        String q = String.format("select password from master");
        ResultSet resultSet = statement.executeQuery(q);
        
        //Check if the password is correct
        if(resultSet.next() && resultSet.getString(1).equals(password)){
            return true;
        }
        else {
            //Create popup saying that the login is wrong and to try again
            JFrame popup = new JFrame("Invalid password");
            JLabel popupMsg = new JLabel("Invalid password, please try again.");
            popupMsg.setBounds(50,10,500,50);
            popupMsg.setFont(new Font("Arial", Font.PLAIN, 20));
            popup.add(popupMsg);

            JButton button = new JButton("OK");
            button.setBounds(210,80,70,20);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(actionEvent2 -> {
                popup.dispose(); 
            });
            popup.add(button);

            popup.setLayout(null);
            popup.setSize(500, 150);
            popup.setVisible(true);
            return false;
        }
    }

    //static String username = "";
    static String password = "";

    JFrame menuFrame;
    JFrame loginFrame;

    /** This method lets you view all the passwords in the database
     * @throws SQLException
     */
    public void viewPassword() throws SQLException {
        menuFrame.setVisible(false);

        //Add the passwords 
        JFrame frame = new JFrame("Passwords");
        JPanel panel = new JPanel();

        //Query to select the passwords from the sql database
        Statement statement = this.con.createStatement();
        String q = "select * from data";
        ResultSet resultSet = statement.executeQuery(q);

        //All value fields for the passwords
        while (resultSet.next()) {
            JPanel passwordInfo = new JPanel();

            //ID Field
            JLabel idLabel = new JLabel("ID");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel idVal = new JLabel(String.valueOf(resultSet.getInt(1)));
            idVal.setFont(new Font("Arial", Font.PLAIN, 20));

            //Website Field 
            JLabel websiteLabel = new JLabel("Website");
            websiteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel websiteVal = new JLabel(resultSet.getString(2));
            websiteVal.setFont(new Font("Arial", Font.PLAIN, 20));

            //Username Field 
            JLabel usernameLabel = new JLabel("Username");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel usernameVal = new JLabel(resultSet.getString(3));
            usernameVal.setFont(new Font("Arial", Font.PLAIN, 20));

            //Password number Field 
            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel passwordVal = new JLabel(resultSet.getString(4));
            passwordVal.setFont(new Font("Arial", Font.PLAIN, 20));

            //Notes Field 
            JLabel noteLabel = new JLabel("Notes");
            noteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            JLabel noteVal = new JLabel(resultSet.getString(5));
            noteVal.setFont(new Font("Arial", Font.PLAIN, 20));

            //Adding all the labels to the frame 
            passwordInfo.add(idLabel);
            passwordInfo.add(idVal);
            passwordInfo.add(websiteLabel);
            passwordInfo.add(websiteVal);
            passwordInfo.add(usernameLabel);
            passwordInfo.add(usernameVal);
            passwordInfo.add(passwordLabel);
            passwordInfo.add(passwordVal);
            passwordInfo.add(noteLabel);
            passwordInfo.add(noteVal);


            //Setting the size of the frame 
            passwordInfo.setSize(1000, 400);
            passwordInfo.setBackground(new Color(166, 209, 230));
            passwordInfo.setBorder(new EmptyBorder(20, 50, 20, 50));
            GridLayout cardLayout = new GridLayout(0, 2);
            cardLayout.setHgap(5);
            cardLayout.setVgap(10);
            passwordInfo.setLayout(cardLayout);
            panel.add(passwordInfo);
        }

        //Back button
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBounds(450, 30, 200,50);
        backButton.setFocusPainted(false);

        //Action listener for the back button
        backButton.addActionListener(actionListener -> {
            frame.dispose();
            menuFrame.setVisible(true);
        });
        buttonPanel.add(backButton);
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(254, 251, 246));
        panel.add(buttonPanel);

        //Layout settings
        GridLayout layout = new GridLayout(0, 1);
        layout.setVgap(30);
        panel.setLayout(layout);
        panel.setBackground(new Color(254, 251, 246));
        panel.setBorder(new EmptyBorder(50, 0, 50, 0));

        //Scrollbar settings
        JScrollPane scrollBar = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(scrollBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1100,750));
        frame.pack();
        frame.setVisible(true);
    }

    /** This method allows a user to add a password and all other information
     * @throws SQLException
     */
    public void addPassword() throws SQLException  {

        menuFrame.setVisible(false);

        JFrame frame = new JFrame("Passwords");
        JPanel panel = new JPanel();

        //ID label and field to enter the ID
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField idVal = new JTextField("");
        idVal.setFont(new Font("Arial", Font.PLAIN, 20));
        
        //Listener that only takes numbers and not letters
        idVal.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        //Website label and field to enter the website
        JLabel websiteLabel = new JLabel("Website URL");
        websiteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField websiteVal = new JTextField("");
        websiteVal.setFont(new Font("Arial", Font.PLAIN, 20));

        //Username label and field to enter the URL 
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField usernameVal = new JTextField("");
        usernameVal.setFont(new Font("Arial", Font.PLAIN, 20));

        //Password label and field to enter password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField passwordVal = new JTextField("");
        passwordVal.setFont(new Font("Arial", Font.PLAIN, 20));

        //Notes label and field to enter any notes about the account
        JLabel noteLabel = new JLabel("Notes");
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField noteVal = new JTextField("");
        noteVal.setFont(new Font("Arial", Font.PLAIN, 20));

        //Add all the labels to the frame
        panel.add(idLabel);
        panel.add(idVal);
        panel.add(websiteLabel);
        panel.add(websiteVal);
        panel.add(usernameLabel);
        panel.add(usernameVal);
        panel.add(passwordLabel);
        panel.add(passwordVal);
        panel.add(noteLabel);
        panel.add(noteVal);

        //Frame settings 
        GridLayout cardLayout = new GridLayout(0, 2);
        cardLayout.setHgap(60);
        cardLayout.setVgap(40);
        panel.setLayout(cardLayout);

        panel.setSize(1000, 400);
        panel.setBackground(new Color(166, 209, 230));
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBounds(450, 30, 200,50);
        backButton.setFocusPainted(false);

        backButton.addActionListener(actionListener -> {
            frame.dispose();
            menuFrame.setVisible(true);
        });
        panel.add(backButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        submitButton.setBounds(450, 30, 200,50);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(actionListener -> {
            Statement statement = null;
            try {
                statement = this.con.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            // Parsing all the information entered
            int id = Integer.parseInt(idVal.getText());
            String website = websiteVal.getText();
            String username = usernameVal.getText();
            String password = passwordVal.getText();
            String notes = noteVal.getText();

            // Executing a query to insert the info into the SQL database
            String q = String.format("insert into data values (%d, '%s', '%s', '%s', '%s');", id, website, username,
                    password, notes);
            try {
                statement.executeUpdate(q);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            menuFrame.setVisible(true);
            frame.dispose();
        });
        panel.add(submitButton);

        panel.setBackground(new Color(254, 251, 246));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1100,750));
        frame.pack();
        frame.setVisible(true);
    }

    /**Method to edit password information
     * @throws SQLException
     */
    public void editPassword() throws SQLException {
        menuFrame.setVisible(false);

        JFrame frame = new JFrame("Edit password");

        JLabel label = new JLabel("Enter ID");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBounds(250,200,200,50);
        frame.add(label);

        JTextField idVal = new JTextField();
        idVal.setFont(new Font("Arial", Font.BOLD, 20));
        idVal.setBounds(500,200,200,50);
        frame.add(idVal);
        idVal.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBounds(275, 400, 150,40);
        backButton.setFocusPainted(false);

        backButton.addActionListener(actionListener -> {
            menuFrame.setVisible(true);
            frame.dispose();

        });
        frame.add(backButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        submitButton.setBounds(525, 400, 150,40);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(actionEvent -> {
            int id = Integer.parseInt(idVal.getText());
            String q = String.format("select * from data where Id = %d;", id);
            Statement statement = null;
            try {
                statement = this.con.createStatement();
                ResultSet resultSet = statement.executeQuery(q);
                if (resultSet.next()) {
                    System.out.println(resultSet.getString(2));
                    editPasswordHelper(id, frame);

                }
            else{
                JFrame popup = new JFrame("Invalid ID");
                JLabel popupMsg = new JLabel("The ID you entered is invalid.");
                popupMsg.setBounds(20,10,300,50);
                popupMsg.setFont(new Font("Arial", Font.PLAIN, 20));
                popup.add(popupMsg);

                JButton button = new JButton("OK");
                button.setBounds(120,60,70,20);
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.addActionListener(actionEvent2 -> {
                    popup.dispose();
                });
                popup.add(button);

                popup.setLayout(null);
                popup.setSize(350, 150);
                popup.setVisible(true);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
        frame.add(submitButton);
        frame.setLayout(null);
        frame.setSize(new Dimension(1100,750));
        frame.setVisible(true);
    }

    /**Panel that pops up when editing employees
     * @param id
     * @param parentFrame
     * @throws SQLException
     */
    public void editPasswordHelper(int id, JFrame parentFrame) throws SQLException {
        JFrame frame = new JFrame("Edit password");
        JPanel panel = new JPanel();

        Statement statement = null;
        try {
            statement = this.con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String q = String.format("select * from data where Id = %d;",id);
        ResultSet resultSet = statement.executeQuery(q);
        resultSet.next();

        // Enter all labels and values for each field
        JLabel websiteLabel = new JLabel("Website");
        websiteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField websiteVal = new JTextField(resultSet.getString(2));
        websiteVal.setFont(new Font("Arial", Font.PLAIN, 20));

    
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField usernameVal = new JTextField(resultSet.getString(3));
        usernameVal.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField passwordVal = new JTextField(resultSet.getString(4));
        passwordVal.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel noteLabel = new JLabel("Notes");
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField noteVal = new JTextField(resultSet.getString(5));
        noteVal.setFont(new Font("Arial", Font.PLAIN, 20));

        // Add all the labels and values to the panel
        panel.add(websiteLabel);
        panel.add(websiteVal);
        panel.add(usernameLabel);
        panel.add(usernameVal);
        panel.add(passwordLabel);
        panel.add(passwordVal);
        panel.add(noteLabel);
        panel.add(noteVal);

        GridLayout cardLayout = new GridLayout(0, 2);
        cardLayout.setHgap(60);
        cardLayout.setVgap(40);
        panel.setLayout(cardLayout);

        panel.setSize(1000, 400);
        panel.setBackground(new Color(166, 209, 230));
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBounds(450, 30, 200,50);
        backButton.setFocusPainted(false);

        backButton.addActionListener(actionListener -> {
            frame.dispose();
            menuFrame.setVisible(true);
        });
        panel.add(backButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        submitButton.setBounds(450, 30, 200,50);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(actionListener -> {
            Statement statemnt = null;
            try {
                statemnt = this.con.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String website = websiteVal.getText();
            String username = usernameVal.getText();
            String password = passwordVal.getText();
            String notes = noteVal.getText();

            // Query to update the SQL table with the new information
            String query = String.format("update data set Website = '%s', Username = '%s', Password = '%s', Notes = '%s' where id = %d;", 
            website, username, password, notes, id);
            try {
                statemnt.executeUpdate(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            menuFrame.setVisible(true);
            frame.dispose();

        });
        panel.add(submitButton);

        panel.setBackground(new Color(254, 251, 246));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1100,750));
        frame.pack();
        frame.setVisible(true);
        parentFrame.dispose();
    }

    /**
     * Method to delete a password from the sql database
     */
    public void deletePassword(){
        menuFrame.setVisible(false);

        JFrame frame = new JFrame("Delete password");

        JLabel label = new JLabel("Enter id");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBounds(250,200,200,50);
        frame.add(label);

        JTextField idVal = new JTextField();
        idVal.setFont(new Font("Arial", Font.BOLD, 20));
        idVal.setBounds(500,200,200,50);
        frame.add(idVal);
        idVal.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBounds(275, 400, 150,40);
        backButton.setFocusPainted(false);

        backButton.addActionListener(actionListener -> {
            menuFrame.setVisible(true);
            frame.dispose();

        });
        frame.add(backButton);

        JButton submitButton = new JButton("Delete");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        submitButton.setBounds(525, 400, 150,40);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(actionEvent -> {
            int id = Integer.parseInt(idVal.getText());
            String q = String.format("select * from data where id = %d;", id);
            Statement statement = null;
            try {
                statement = this.con.createStatement();
                ResultSet resultSet = statement.executeQuery(q);
                if (resultSet.next()) {
                    System.out.println(resultSet.getString(2));

                    String deleteQuery = String.format("delete from data where id = %d;", id);
                    statement.executeUpdate(deleteQuery);
                    menuFrame.setVisible(true);
                    frame.dispose();
                }
                else{
                    JFrame popup = new JFrame("Invalid ID");
                    JLabel popupMsg = new JLabel("The ID you entered is invalid.");
                    popupMsg.setBounds(20,10,300,50);
                    popupMsg.setFont(new Font("Arial", Font.PLAIN, 20));
                    popup.add(popupMsg);

                    JButton button = new JButton("OK");
                    button.setBounds(120,60,70,20);
                    button.setFont(new Font("Arial", Font.PLAIN, 20));
                    button.addActionListener(actionEvent2 -> {
                        popup.dispose();
                    });

                    popup.add(button);
                    popup.setLayout(null);
                    popup.setSize(350, 150);
                    popup.setVisible(true);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        frame.add(submitButton);
        frame.setLayout(null);
        frame.setSize(new Dimension(1100,750));
        frame.setVisible(true);
    }

    /**
     * Method to create the main menu after logging in
     */
    public void mainMenu(){
        menuFrame = new JFrame("Password Database");

        JLabel welcomeLabel = new JLabel("Welcome to the Password Database");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setBounds(300,75,800,50);
        menuFrame.add(welcomeLabel);

        JButton viewButton = new JButton("View all passwords");
        viewButton.setBounds(400, 200, 300, 40);
        viewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        viewButton.setFocusPainted(false);
        viewButton.addActionListener(actionEvent -> {
            try {
                viewPassword();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        menuFrame.add(viewButton);

        JButton addButton = new JButton("Add a Password");
        addButton.setBounds(400, 270, 300, 40);
        addButton.setFont(new Font("Arial", Font.PLAIN, 20));
        addButton.setFocusPainted(false);
        addButton.addActionListener(actionEvent -> {
            try {
                addPassword();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        menuFrame.add(addButton);

        JButton editButton = new JButton("Edit a Password");
        editButton.setBounds(400, 340, 300, 40);
        editButton.setFont(new Font("Arial", Font.PLAIN, 20));
        editButton.setFocusPainted(false);
        editButton.addActionListener(actionEvent -> {
            try {
                editPassword();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        menuFrame.add(editButton);

        JButton deleteButton = new JButton("Delete a Password");
        deleteButton.setBounds(400, 410, 300, 40);
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 20));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(deleteEvent -> {
            deletePassword();
        });
        menuFrame.add(deleteButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(400, 480, 300, 40);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(actionEvent -> {
            menuFrame.dispose();
        });
        menuFrame.add(exitButton);

        menuFrame.setSize(1100,750);
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setLayout(null);
        menuFrame.setVisible(true);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //local user credentials to access sql database on machine
        String dbName = "passwords";   
        String db_username = "";    //CHANGE THIS TO YOUR OWN MySQL USERNAME
        String db_password = ""; //CHANGE THIS TO YOUR OWN MySQL PASSWORD

        //Gets driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName, db_username, db_password);

        //Create instance of the EmployeeSystem class and passthrough the sql connection
        PasswordManager ems = new PasswordManager(con);

        //Username field 
        ems.loginFrame = new JFrame("Login");

        //Title label
        JLabel titLabel = new JLabel("Password Database");
        titLabel.setBounds(275, 50, 700, 100);
        titLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        ems.loginFrame.add(titLabel);


        //Password label and field to enter
        JLabel passwordLabel = new JLabel("Enter Master Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        passwordLabel.setBounds(100,250,350,50);
        ems.loginFrame.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setBounds(450,250,420,50);
        ems.loginFrame.add(passwordField);

        //Submit button
        JButton submitButton=new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 30));
        submitButton.setBounds(400,400,250, 50);

        //Action listener to check if the password is correct
        submitButton.addActionListener(actionEvent -> {
            password = String.valueOf(passwordField.getPassword());

            try {
                boolean auth = ems.login(password);
                if(auth){
                    ems.loginFrame.dispose();
                    ems.mainMenu();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        ems.loginFrame.add(submitButton);

        //Exit button 
        ems.loginFrame.setSize(1100,750);
        ems.loginFrame.setLayout(null);
        ems.loginFrame.setVisible(true);
        ems.loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}