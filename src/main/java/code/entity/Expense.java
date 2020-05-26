package code.entity;

public class Expense {
    private String category;
    private Float amount;
    private String accountName;
    private String description;

    public Expense(String category, Float amount, String accountName, String description) {
        this.category = category;
        this.amount = amount;
        this.accountName = accountName;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
