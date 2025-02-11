package VTTPpaf.cheatsheet.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import VTTPpaf.cheatsheet.exception.ResourceNotFoundException;
import VTTPpaf.cheatsheet.model.Customer;
import VTTPpaf.cheatsheet.utils.sql;


//using vttP-2025 database, room and customer tables

@Repository
public class SQLrepository {
    @Autowired
    private JdbcTemplate template;

    //SELECT * FROM customer
    public List<Customer> getAllCustomers(){
        List<Customer> customersQuery = new LinkedList<>();
        List<Customer> customersQueryRowSet = new LinkedList<>();

        //template.query retusn a list of objects
        customersQuery = template.query(sql.sql_getallCustomers, BeanPropertyRowMapper.newInstance(Customer.class));

        //queryforrowset can do the same, but it is more manual
        SqlRowSet sqlRowSet = template.queryForRowSet(sql.sql_getallCustomers);
        while (sqlRowSet.next()){
            Customer customer = new Customer();
            customer.setId(sqlRowSet.getInt("id"));
            customer.setFullname(sqlRowSet.getString("fullname"));
            customer.setEmail(sqlRowSet.getString("email"));

            customersQueryRowSet.add(customer);
        }
        return customersQuery;
        // return customersQueryRowSet;
    }


    //SELECT * FROM customer LIMIT ? OFFSET ?
    public List<Customer> getAllCustomers(int limit, int offset){
        List<Customer> cusList = new LinkedList<>();
        cusList = template.query(sql.sql_getCustomers_LimitOffSet, BeanPropertyRowMapper.newInstance(Customer.class), limit, offset);

        return cusList;
    }

    //SELECT * FROM customer where id = ?;
    public Customer getCustomerById(int id){
        Customer c = null;
        try{
            //template.queryForObject returns a singular objects
            c = template.queryForObject(sql.sql_getCustomersById, BeanPropertyRowMapper.newInstance(Customer.class), id);
        }catch(DataAccessException ex){
            throw new ResourceNotFoundException("ID does not exist in system: " + id);
        }
        return c;
    }

    //DELETE FROM customer where id = ?
    public boolean deleteCustomerById(int id){
        //returns number of rows affected by update
        int customerDeleted = template.update(sql.sql_deleteCustomerById, id);

        if(customerDeleted > 0){
            return true;
        }
        return false;
    }

    //UPDATE customer set fullname = ?, email = ? WHERE id = ?
    public boolean updateCustomerById(Customer customer){
        //returns number of rows affected by update
        int customerUpdated = template.update(sql.sql_updateCustomerById, customer.getFullname(), customer.getEmail(), customer.getId());

        if(customerUpdated > 0){
            return true;
        }
        return false;
    }

    //INSERT INTO customer (fullname, email) values (?, ?)
    public boolean insertCustomer(Customer customer){
        //returns number of rows affected by update
        int customerUpdated = template.update(sql.sql_insertCustomer, customer.getFullname(), customer.getEmail());

        if(customerUpdated > 0){
            return true;
        }
        return false;

    }

    //select * from customer where fullname like ?
    public List<Customer> getCustomerByNameLikeness(String name){
        List<Customer> cusList = new ArrayList<>();
        String wildcard = "%" + name + "%";
        //SELECT * FROM products WHERE product_name LIKE 'a___e';  5-character string starting with "a" and ending with "e"
        //SELECT * FROM products WHERE product_name LIKE '[!ab]%'; match any string except those starting with "a" or "b"

        cusList = template.query(sql.sql_getRelatedCustomerByName,BeanPropertyRowMapper.newInstance(Customer.class), wildcard);
        if(cusList.isEmpty()){
            throw new ResourceNotFoundException("query: " + name + " does not match any RSVP record");
        }

        return cusList;
    }

    //select count(*) from customer
    public int getCustomerCount(){
        Integer count = template.queryForObject(sql.sql_getCustomerCount, Integer.class);
        return count;
    }

}
