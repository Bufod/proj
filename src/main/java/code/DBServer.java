package code;

import code.entity.BankAccount;
import code.entity.Expense;
import code.entity.User;

import java.sql.*;
import java.util.ArrayList;

enum ConnectionInfo {
    dbHost("localhost"),
    dbPort("3306"),
    dbUser("root"),
    dbPass("1234"),
    dbName("segnorkopilkin");
    private String value;

    ConnectionInfo(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

public class DBServer {

    Connection database;

    public DBServer() {
        try {
            database = getDbConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Не удается подключится к базе данных");
        }

    }

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectStr = "jdbc:mysql://" + ConnectionInfo.dbHost + ":"
                + ConnectionInfo.dbPort + "/" + ConnectionInfo.dbName + "?useUnicode=true&serverTimezone=UTC&useSSL=false";

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(connectStr,
                ConnectionInfo.dbUser.toString(), ConnectionInfo.dbPass.toString());

    }

    public class Users {
        private final static String TABLE_NAME = "users";

        private final static String ID = "id";
        private final static String FIRSTNAME = "firstname";
        private final static String LASTNAME = "lastname";
        private final static String LOGIN = "login";
        private final static String PASSWORD = "password";

        private final static int COLUMN_ID = 1;
        private final static int COLUMN_FIRSTNAME = 2;
        private final static int COLUMN_LASTNAME = 3;
        private final static int COLUMN_LOGIN = 4;
        private final static int COLUMN_PASSWORD = 5;

        public boolean insert(int id, String firstname, String lastname, String login, String password) {
            String query = "INSERT INTO " + TABLE_NAME + "(" +
                    ID + "," + FIRSTNAME + "," + LASTNAME + "," + LOGIN + "," + PASSWORD + ")" +
                    " VALUES (?, ?, ?, ?, ?)";

            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, id);
                prstmt.setString(2, firstname);
                prstmt.setString(3, lastname);
                prstmt.setString(4, login);
                prstmt.setString(5, password);
                ok = prstmt.execute();
                prstmt.close();
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }

        public boolean delete(User user) {
            String query = "delete from " + TABLE_NAME + " where " + ID + " =?";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, user.getId());
                ok = prstmt.execute();
                prstmt.close();
                new Expenses().delete(user);
                new BankAccounts().delete(user);
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean insert(String firstname, String lastname, String login, String password) {
            String query = "INSERT INTO " + TABLE_NAME + "(" +
                    FIRSTNAME + "," + LASTNAME + "," + LOGIN + "," + PASSWORD + ")" +
                    " VALUES (?, ?, ?, ?)";

            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setString(1, firstname);
                prstmt.setString(2, lastname);
                prstmt.setString(3, login);
                prstmt.setString(4, password);
                ok = prstmt.execute();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ok;
        }

        //получение пользователя по login и password
        public User select(String login, String password) {
            ResultSet resultSet = null;
            String select = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                    LOGIN + " = ? AND " + PASSWORD + " = ?";
            User user = null;
            try {
                PreparedStatement prstmt = database.prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                prstmt.setString(1, login);
                prstmt.setString(2, password);
                resultSet = prstmt.executeQuery();
                if (resultSet.first()) {
                    int id = resultSet.getInt(COLUMN_ID);
                    String firstname = resultSet.getString(COLUMN_FIRSTNAME),
                            lastname = resultSet.getString(COLUMN_LASTNAME);
                    user = new User(id, firstname, lastname, login, password);
                }
                resultSet.close();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return user;
        }
    }

    public class BankAccounts {
        private final static String TABLE_NAME = "bank_accounts";

        private final static String ID = "id";
        private final static String BANK_NAME = "bank_name";
        private final static String ACCOUNT_NAME = "account_name";
        private final static String BALANCES = "balances";
        private final static String USER_ID = "user_id";

        private final static int COLUMN_ID = 1;
        private final static int COLUMN_BANK_NAME = 2;
        private final static int COLUMN_ACCOUNT_NAME = 3;
        private final static int COLUMN_BALANCES = 4;
        private final static int COLUMN_USER_ID = 5;

        public boolean insert(String bankName, String accountName, float balances, int userId) {
            String query = "INSERT INTO " + TABLE_NAME + "(" + BANK_NAME + ", " +
                    ACCOUNT_NAME + ", " + BALANCES + ", " + USER_ID + ")" +
                    "VALUES (?, ?, ?, ?);";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setString(1, bankName);
                prstmt.setString(2, accountName);
                prstmt.setFloat(3, balances);
                prstmt.setInt(4, userId);
                ok = prstmt.execute();
                prstmt.close();
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }

        public boolean delete(BankAccount bankAccount) {
            String query = "delete from " + TABLE_NAME + " where " + ID + " =?";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, bankAccount.getId());
                ok = prstmt.execute();
                prstmt.close();
                new Expenses().delete(bankAccount.getId());
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean delete(User user) {
            String query = "delete from " + TABLE_NAME + " where " + USER_ID + " =?";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, user.getId());
                ok = prstmt.execute();
                prstmt.close();
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public ArrayList<BankAccount> select(User user) {
            ResultSet resultSet = null;
            String select = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                    USER_ID + " = ?";
            ArrayList<BankAccount> bankAccounts = new ArrayList<>();
            try {
                PreparedStatement prstmt = database.prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                prstmt.setString(1, String.valueOf(user.getId()));
                resultSet = prstmt.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt(COLUMN_ID);
                    String bankName = resultSet.getString(COLUMN_BANK_NAME),
                            accountName = resultSet.getString(COLUMN_ACCOUNT_NAME);
                    float balances = resultSet.getFloat(COLUMN_BALANCES);
                    int userId = resultSet.getInt(COLUMN_USER_ID);
                    bankAccounts.add(new BankAccount(id, bankName, accountName, balances, userId));
                }
                resultSet.close();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return bankAccounts;
        }

        public boolean replanishAcc(BankAccount bankAccount, float ammount) {
            String query = "UPDATE " + TABLE_NAME +
                    " SET balances = ? " +
                    "WHERE bank_name = ? and user_id = ?;";

            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setFloat(1, bankAccount.getBalances() + ammount);
                prstmt.setString(2, bankAccount.getBankName());
                prstmt.setInt(3, bankAccount.getUserId());
                return prstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public class Categories {
        private final static String TABLE_NAME = "categories";

        private final static String ID = "id";
        private final static String NAME = "name";

        private final static int COLUMN_ID = 1;
        private final static int COLUMN_NAME = 2;

        public ArrayList<String> selectAll() {
            ResultSet resultSet = null;
            String select = "SELECT * FROM " + TABLE_NAME;
            ArrayList<String> categores = new ArrayList<>();
            try {
                PreparedStatement prstmt = database.prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                resultSet = prstmt.executeQuery();
                while (resultSet.next()) {
                    String catName = resultSet.getString(COLUMN_NAME);
                    categores.add(catName);
                }
                resultSet.close();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categores;
        }

        public int getIdByName(String name) {
            ResultSet resultSet = null;
            String select = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + NAME + " =?";
            Integer categoryId = null;
            try {
                PreparedStatement prstmt = database.prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                prstmt.setString(1, name);
                resultSet = prstmt.executeQuery();
                resultSet.first();
                categoryId = resultSet.getInt(COLUMN_ID);
                resultSet.close();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categoryId;
        }
    }

    public class Expenses {
        private final static String TABLE_NAME = "expenses";

        private final static String ID = "id";
        private final static String CATEGORY = "category";
        private final static String AMOUNT = "amount";
        private final static String BANK_ACCOUNT_ID = "bank_account_id";
        private final static String DESCRIPTION = "description";

        private final static int COLUMN_ID = 1;
        private final static int COLUMN_CATEGORY = 2;
        private final static int COLUMN_AMOUNT = 3;
        private final static int COLUMN_BANK_ACCOUNT_ID = 4;
        private final static int COLUMN_DESCRIPTION = 5;

        public boolean insert(String category, float amount, int bank_account_id, String description) {
            String query = "INSERT INTO " + TABLE_NAME + "(" + CATEGORY + ", " +
                    AMOUNT + ", " + BANK_ACCOUNT_ID + ", " + DESCRIPTION + ")" +
                    " VALUES (?, ?, ?, ?);";

            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, DBServer.this.new Categories().getIdByName(category));
                prstmt.setFloat(2, amount);
                prstmt.setInt(3, bank_account_id);
                prstmt.setString(4, description);
                ok = prstmt.execute();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ok;
        }

        public boolean delete(User user) {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE " + BANK_ACCOUNT_ID + " =(SELECT " +
                    BankAccounts.ID + " FROM " + BankAccounts.TABLE_NAME + " WHERE " + BankAccounts.USER_ID + " = ?)";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, user.getId());
                ok = prstmt.execute();
                prstmt.close();
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean delete(int id) {
            String query = "delete from " + TABLE_NAME + " where " + BANK_ACCOUNT_ID + " =?";
            boolean ok = false;
            try {
                PreparedStatement prstmt = database.prepareStatement(query);
                prstmt.setInt(1, id);
                ok = prstmt.execute();
                prstmt.close();
                return ok;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public ArrayList<Expense> selectAll(User user) {
            String query = "SELECT cat.name, " +
                    " ex.amount, " +
                    " ba.account_name, " +
                    " ex.description " +
                    "from expenses as ex " +
                    " join categories as cat on ex.category = cat.id " +
                    " join bank_accounts as ba on ex.bank_account_id = ba.id " +
                    "where ba.user_id = ?;";

            ResultSet resultSet = null;
            ArrayList<Expense> expenses = new ArrayList<>();
            try {
                PreparedStatement prstmt = database.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                prstmt.setInt(1, user.getId());
                resultSet = prstmt.executeQuery();
                while (resultSet.next()) {
                    String catName = resultSet.getString(1);
                    Float amount = resultSet.getFloat(2);
                    String accName = resultSet.getString(3),
                            description = resultSet.getString(4);
                    expenses.add(new Expense(catName, amount, accName, description));
                }
                resultSet.close();
                prstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return expenses;
        }

        public boolean deleteAll() throws SQLException {
            String query = "DELETE FROM " + TABLE_NAME;
            Statement stmt = database.createStatement();
            boolean ok = stmt.execute(query);
            stmt.close();
            return ok;
        }

    }
}
