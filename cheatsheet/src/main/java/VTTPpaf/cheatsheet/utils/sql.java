package VTTPpaf.cheatsheet.utils;

public class sql {
    
    public static final String sql_getallCustomers = "SELECT * FROM customer";
    public static final String sql_getCustomers_LimitOffSet = "SELECT * FROM customer LIMIT ? OFFSET ?";
    public static final String sql_getCustomersById = "SELECT * FROM customer where id = ?";
    public static final String sql_deleteCustomerById = "DELETE FROM customer where id = ?";
    public static final String sql_updateCustomerById = "UPDATE customer set fullname = ?, email = ? WHERE id = ?";
    public static final String sql_insertCustomer = "INSERT INTO customer (fullname, email) values (?, ?)";

    public static final String sql_getRelatedCustomerByName= "select * from customer where name like ?";
    public static final String sql_getCustomerCount = "select count(*) from customer";

    //unused
    public static final String sql_getDistinctName= "select distinct fullname, email from customer";
    String subqueryLastUpdate = """
        UPDATE accounts
        SET balance = <new_balance>
        WHERE last_update = (
            SELECT last_update
            FROM accounts
            WHERE acct_id = <acct_id>
        );        
            """;


}
