package code.entity;

public class BankAccount {
    private int id;
    private String bankName;
    private String accountName;
    private float balances;
    private int userId;

    public BankAccount(int id, String bankName, String accountName, float balances, int userId) {
        this.id = id;
        this.bankName = bankName;
        this.accountName = accountName;
        this.balances = balances;
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public float getBalances() {
        return balances;
    }

    public void setBalances(float balances) {
        this.balances = balances;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return accountName + "\n" + balances + " \u20BD";
    }
}
